/**
 * Copyright 2009-2015 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.gnu.org/copyleft/lesser.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.threecrickets.succinct.chunk.Block;
import com.threecrickets.succinct.chunk.Chunk;
import com.threecrickets.succinct.chunk.Tag;
import com.threecrickets.succinct.chunk.Text;
import com.threecrickets.succinct.chunk.block.Comment;
import com.threecrickets.succinct.chunk.block.Define;
import com.threecrickets.succinct.chunk.block.Flatten;
import com.threecrickets.succinct.chunk.block.rich.ForEach;
import com.threecrickets.succinct.chunk.tag.Cast;
import com.threecrickets.succinct.chunk.tag.External;
import com.threecrickets.succinct.chunk.tag.FormatCast;

/**
 * All tags begin with <code>${</code> and end with <code>}</code>. Some tags
 * begin blocks (conditional clauses, iteration loops, etc.) which continue
 * until an empty tag, <code>${}</code>. Blocks can be nested, such that an
 * empty tag signifies the end of the innermost block.
 * <p>
 * To keep the implementation lean, only the following five tags are handled
 * here, because they are processed specially during the initial parsing. For
 * all other tags, see the {@link RichTemplate} class.
 * <ul>
 * <li>Cast: <code>${=<i>key</i>}</code> casts the key.</li>
 * <li>Format: <code>${=key:format}</code> uses a {@link Formatter} for format
 * the value cast by the tag.</li>
 * <li>Import: <code>${import <i>template-name</i>}</code> imports the template
 * using the {@link TemplateSource}, and inserts it in place of the tag. Note
 * that all imports are processed while parsing, so that this does not effect
 * casting performance in any way.</li>
 * <li>Using: <code>${tag (<i>iterable-key</i>)<i>key</i>}</code> uses the
 * nested template filler <code>nested-key</code> to process the tag. For
 * example: <code>${for-each (book)author}</code>. Note that this construct only
 * makes sense for tags that accept a single key as their argument.</li>
 * <li>Using Import:
 * <code>${import (<i>iterable-key</i>)<i>template-name</i>}</code> is replaced
 * by the nested block structure
 * <code>${for-each nested-key}${import <i>template-name</i>}${}</code> . This
 * special implementation of the Using tag is only for use in
 * {@link RichTemplate}, but is defined here because it needs to be processed
 * during parsing.</li>
 * <li>Define: <code>${define <i>key</i>}</code> defines the block as filling
 * tag. This is useful when importing templates that require certain tags. The
 * block can in turn include any other tags, and thus it works like a
 * mini-import. Like imports, defines are processed while parsing.</li>
 * <li>Comment: <code>${#}</code> does not parse the block. It does not become
 * part of the template's parsed structure, and thus it does not take any
 * memory.</li>
 * <li>Flatten: <code>${flatten}</code> removes all tabs, carriage returns and
 * newlines in the block. Note that this does not apply to casting, only to
 * literal text within the template! As with comments, the excess whitespace
 * does not became part of the template's parsed structure, and thus it does not
 * take any memory. It's useful for organizing the template with whitespace
 * without affecting its presentation.</li>
 * <li>External: <code>${external <i>external-key</i>}</code> calls the
 * {@link ExternalHandler} to process this tag. External handlers can be used
 * for complex casting such as expression language evaluation, and delegation or
 * hooking to other templating/rendering systems.</li>
 * </ul>
 * 
 * @author Tal Liron
 * @see RichTemplate
 */
public class BasicTemplate implements Template
{
	//
	// Constants
	//

	public static final String TAG_IMPORT_BEGIN = Syntax.get( "Import.begin" );

	public static final int TAG_IMPORT_BEGIN_LENGTH = TAG_IMPORT_BEGIN.length();

	public static final String TAG_IMPORT_END = Syntax.get( "Import.end" );

	public static final int TAG_IMPORT_END_LENGTH = TAG_IMPORT_END.length();

	//
	// Construction
	//

	public BasicTemplate( String content, TemplateSource importTemplateSource ) throws ParseException, TemplateSourceException
	{
		this.importTemplateSource = importTemplateSource;
		init( new StringBuilder( content ) );
	}

	//
	// Template
	//

	public void setExternalHandler( ExternalHandler externalHandler )
	{
		this.externalHandler = externalHandler;
	}

	public ExternalHandler getExternalHandler()
	{
		return externalHandler;
	}

	public void setFormatter( Formatter formatter )
	{
		this.formatter = formatter;
	}

	public Formatter getFormatter()
	{
		return formatter;
	}

	public void cast( Filler filler, Appendable out ) throws CastException
	{
		try
		{
			for( Chunk chunk : chunks )
				cast( chunk, filler, out, false );
		}
		catch( IOException x )
		{
			throw new CastException( x );
		}
	}

	public void softCast( Filler filler, Appendable out )
	{
		for( Chunk chunk : chunks )
			try
			{
				cast( chunk, filler, out, true );
			}
			catch( IOException x )
			{
				// This should never happen in soft casts
			}
			catch( CastException x )
			{
				// This should never happen in soft casts
			}
	}

	public Iterator<Object> streamCast( Filler filler )
	{
		return new CastIterator( filler );
	}

	public void transform( Filler filler ) throws ParseException, TemplateSourceException
	{
		StringBuilder buffer = new StringBuilder( initialCapacity );
		for( Chunk chunk : chunks )
			try
			{
				cast( chunk, filler, buffer, true );
			}
			catch( IOException x )
			{
				// This should never happen in soft casts
			}
			catch( CastException x )
			{
				// This should never happen in soft casts
			}
		init( buffer );
	}

	public String[] getRequiredTags()
	{
		Set<String> tags = new HashSet<String>();
		for( Chunk chunk : chunks )
			addRequiredTags( tags, chunk );
		String[] r = new String[tags.size()];
		tags.toArray( r );
		return r;
	}

	//
	// Object
	//

	@Override
	public String toString()
	{
		StringBuilder buffer = new StringBuilder( initialCapacity );
		for( Chunk chunk : chunks )
			buffer.append( chunk );
		return buffer.toString();
	}

	// //////////////////////////////////////////////////////////////////////////
	// Protected

	protected Chunk parseTag( String tag ) throws ParseException
	{
		if( tag.startsWith( Cast.MARK ) )
		{
			int formatIndex = tag.indexOf( FormatCast.MARK );
			if( formatIndex != -1 )
			{
				String theTag = tag.substring( Cast.MARK_LENGTH, formatIndex );
				String format = tag.substring( formatIndex + FormatCast.MARK_LENGTH );
				String[] using = Chunk.parseUsing( theTag );
				return new FormatCast( using[0], using[1], format );
			}
			else
			{
				tag = tag.substring( Cast.MARK_LENGTH );
				String[] using = Chunk.parseUsing( tag );
				return new Cast( using[0], using[1] );
			}
		}
		else if( tag.startsWith( External.MARK ) )
			return new External( tag.substring( External.MARK_LENGTH ) );
		else if( tag.startsWith( Define.MARK ) )
			return new Define( tag.substring( Define.MARK_LENGTH ) );
		else if( tag.equals( Comment.MARK ) )
			return new Comment();
		else if( tag.equals( Flatten.MARK ) )
			return new Flatten();
		else
			throw new ParseException( "Unsupported tag: " + tag );
	}

	protected void cast( Chunk chunk, Filler filler, Appendable out, boolean soft ) throws CastException, IOException
	{
		try
		{
			if( chunk instanceof FormatCast )
			{
				FormatCast formatCast = (FormatCast) chunk;
				Cast cast = new Cast( formatCast.string, formatCast.using );
				StringBuilder buffer = new StringBuilder();
				cast( cast, filler, buffer, soft );
				out.append( formatter.formatTemplateValue( formatCast.format, buffer.toString() ) );
			}
			else if( chunk instanceof Cast )
			{
				Define define = defines.get( chunk.string );
				if( define != null )
				{
					for( Chunk nestedChunk : define.chunks )
						cast( nestedChunk, filler, out, soft );
				}
				else
				{
					Object cast = chunk.getUsing( filler ).getValue( chunk.string );
					if( cast instanceof CompositeValue )
					{
						for( Object subValue : (CompositeValue) cast )
						{
							if( subValue instanceof CompositeValue.External )
							{
								if( externalHandler != null )
								{
									Object external = externalHandler.handleExternalTag( ( (CompositeValue.External) subValue ).externalKey );
									if( external != null )
										out.append( external.toString() );
								}
							}
							else
								out.append( subValue.toString() );
						}
					}
					else if( cast == null )
					{
						throw new CastException( chunk.string );
					}
					else
						out.append( cast.toString() );
				}
			}
			else if( chunk instanceof External )
			{
				if( externalHandler != null )
				{
					Object external = externalHandler.handleExternalTag( chunk.string );
					if( external != null )
						out.append( external.toString() );
				}
			}
			else if( chunk instanceof Text )
			{
				out.append( chunk.string );
			}
		}
		catch( CastException x )
		{
			if( soft )
				out.append( chunk.toString() );
			else
				throw x;
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static final long serialVersionUID = 1L;

	private final List<Chunk> chunks = new ArrayList<Chunk>();

	private final Map<String, Define> defines = new HashMap<String, Define>();

	private final transient TemplateSource importTemplateSource;

	private int initialCapacity;

	private transient ExternalHandler externalHandler = null;

	private transient Formatter formatter = null;

	private boolean init( StringBuilder content ) throws ParseException, TemplateSourceException
	{
		boolean castExists = false;
		processImports( content );
		initialCapacity = content.length();
		chunks.clear();

		final List<List<Chunk>> stack = new ArrayList<List<Chunk>>();
		List<Chunk> current = chunks;
		int length = content.length();
		int last = 0;
		int tagStart = 0;
		int tagActualStart;
		int tagEnd;
		String tag;
		Chunk chunk, lastChunk;
		String text;
		int flatten = 0;

		tagStart = content.indexOf( Tag.BEGIN, tagStart );
		while( tagStart != -1 )
		{
			if( tagStart > last )
			{
				text = content.substring( last, tagStart );
				current.add( new Text( ( flatten > 0 ) ? TemplateUtil.removeWhiteSpace( text ) : text ) );
			}

			tagActualStart = tagStart + Tag.BEGIN_LENGTH;
			tagEnd = content.indexOf( Tag.END, tagActualStart );
			if( tagEnd == -1 )
				throw new ParseException( "Template parsing error - no end for tag" );
			last = tagEnd + Tag.END_LENGTH;
			tag = content.substring( tagActualStart, tagEnd ).trim();
			chunk = null;

			if( tag.length() == 0 )
			{
				// End block
				if( stack.isEmpty() )
					throw new ParseException( "Template parsing error - block end tag does not have a corresponding beginning" );
				// Pop stack
				current = stack.remove( 0 );
				int size = current.size();
				if( size > 0 )
				{
					lastChunk = current.get( size - 1 );
					if( lastChunk instanceof Flatten )
					{
						current.remove( size - 1 );
						current.addAll( ( (Flatten) lastChunk ).chunks );
						flatten--;
					}
				}
			}
			else
			{
				chunk = parseTag( tag );
				if( chunk != null )
				{
					if( chunk instanceof Block )
					{
						if( chunk instanceof Define )
						{
							castExists = true;
							defines.put( chunk.string, (Define) chunk );
						}
						else if( chunk instanceof Flatten )
						{
							flatten++;
							current.add( chunk );
						}
						else if( chunk instanceof Comment )
						{
						}
						else
						{
							castExists = true;
							current.add( chunk );
						}
						// Push stack
						stack.add( 0, current );
						current = ( (Block) chunk ).chunks;
					}
					else
					{
						current.add( chunk );
						castExists = true;
					}
				}
			}

			tagStart = content.indexOf( Tag.BEGIN, last );
		}

		if( current != chunks )
			throw new ParseException( "Template parsing error - block tag not ended" );

		if( length > last )
			current.add( new Text( content.substring( last ) ) );

		return castExists;
	}

	private void processImports( StringBuilder content ) throws ParseException, TemplateSourceException
	{
		int last = 0;
		int tagStart = 0;
		int tagActualStart;
		int tagEnd;
		String tag, imported;
		String[] parsed;

		tagStart = content.indexOf( TAG_IMPORT_BEGIN, tagStart );
		while( tagStart != -1 )
		{
			tagActualStart = tagStart + TAG_IMPORT_BEGIN_LENGTH;
			tagEnd = content.indexOf( TAG_IMPORT_END, tagActualStart );
			if( tagEnd == -1 )
				throw new ParseException( "Template parsing error - no end for import tag" );
			last = tagEnd + TAG_IMPORT_END_LENGTH;
			tag = content.substring( tagActualStart, tagEnd ).trim();

			parsed = Chunk.parseUsing( tag );
			if( parsed[1] != null )
				// Import using
				imported = Tag.BEGIN + ForEach.MARK + parsed[1] + Tag.END + TAG_IMPORT_BEGIN + parsed[0] + TAG_IMPORT_END + Tag.BEGIN + Tag.END;
			else
				imported = importTemplateSource != null ? importTemplateSource.getTemplate( tag ) : "";

			content.replace( tagStart, last, imported );

			last = tagStart;
			tagStart = content.indexOf( TAG_IMPORT_BEGIN, last );
		}
	}

	private void addRequiredTags( Set<String> tags, Chunk chunk )
	{
		String[] requiredTags = chunk.getRequiredTags();
		for( String requiredTag : requiredTags )
		{
			Define define = defines.get( requiredTag );
			if( define != null )
			{
				for( Chunk subChunk : define.chunks )
					addRequiredTags( tags, subChunk );
			}
			else if( chunk instanceof External )
				tags.add( "$" + requiredTag );
			else
				tags.add( requiredTag );
		}
		if( chunk instanceof Block )
			for( Chunk subChunk : ( (Block) chunk ).chunks )
				addRequiredTags( tags, subChunk );
	}

	//
	// Types
	//

	private class CastIterator implements Iterator<Object>
	{
		//
		// Iterator
		//

		public boolean hasNext()
		{
			return iterator.hasNext();
		}

		public Object next()
		{
			// TODO: this doesn't iterate inner chunks! this breaks external tag
			// usage!
			Chunk chunk = iterator.next();
			StringBuilder buffer = new StringBuilder( 512 );
			try
			{
				cast( chunk, filling, buffer, false );
			}
			catch( IOException x )
			{
				return x;
			}
			catch( CastException x )
			{
				return x;
			}
			return buffer;
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		// //////////////////////////////////////////////////////////////////////
		// Private

		private CastIterator( Filler filling )
		{
			this.filling = filling;
		}

		private final Filler filling;

		private final Iterator<Chunk> iterator = chunks.iterator();
	}
}
