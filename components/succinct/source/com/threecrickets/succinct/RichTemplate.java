/**
 * Copyright 2000-2017 Three Crickets LLC.
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
import java.util.Iterator;

import com.threecrickets.succinct.chunk.Block;
import com.threecrickets.succinct.chunk.Chunk;
import com.threecrickets.succinct.chunk.block.rich.ForEach;
import com.threecrickets.succinct.chunk.block.rich.If;
import com.threecrickets.succinct.chunk.block.rich.IfFirst;
import com.threecrickets.succinct.chunk.block.rich.IfHas;
import com.threecrickets.succinct.chunk.block.rich.IfHasMany;
import com.threecrickets.succinct.chunk.block.rich.IfHasMore;
import com.threecrickets.succinct.chunk.block.rich.IfHasNone;
import com.threecrickets.succinct.chunk.block.rich.IfHasOne;
import com.threecrickets.succinct.chunk.block.rich.IfNotFirst;
import com.threecrickets.succinct.chunk.tag.rich.Count;
import com.threecrickets.succinct.chunk.tag.rich.Index0;
import com.threecrickets.succinct.chunk.tag.rich.Index1;
import com.threecrickets.succinct.filler.NestedFiller;

/**
 * A {@link Template} supporting the following tags.
 * <p>
 * Tags:
 * <ul>
 * <li>Count: <code>${count <i>iterable-key</i>}</code> replaces the tag with
 * the number of iterations.</li>
 * </ul>
 * Block tags for keys:
 * <ul>
 * <li>If Is: <code>${if <i>key</i>=<i>value</i>}</code> processes the block
 * only if the key equals the value.</li>
 * <li>If Is Not: <code>${if <i>key</i>!=<i>value</i>}</code> processes the
 * block only if the key <i>doesn't</i> equals the value.</li>
 * <li>If Exists: <code>${if <i>key</i>=}</code> processes the block only if the
 * key has a value.</li>
 * <li>If Doesn't Exist: <code>${if-none <i>key</i>=}</code> processes the block
 * only if the key has no value.</li>
 * </ul>
 * Block tags for iterable keys:
 * <ul>
 * <li>For Each: <code>${for-each <i>iterable-key</i>}</code> processes the
 * block once per iteration. A segment of the iteration can be specified as
 * <code>${for-each <i>iterable-key</i>;<i>start</i>;<i>limit</i>}</code>, where
 * <code>start</code> is which iteration to start at, and <code>limit</code> is
 * the maximum number of iterations to go through from there. They can be
 * literal numbers or keys. See below for special codes that can be used within
 * the for-each block.</li>
 * <li>If Has: <code>${if <i>iterable-key</i>}</code> processes the block once
 * <i>only</i> if there are iterations. If there are no iterations, then it does
 * not process the block.</li>
 * <li>If Has One: <code>${if-one <i>iterable-key</i>}</code> processes the
 * block once <i>only</i> if there is <i>exactly one</i> iteration. If there are
 * no iterations, or more than one iteration, then it does not process the
 * block.</li>
 * <li>If Has Many: <code>${if-many <i>iterable-key</i>}</code> processes the
 * block once <i>only</i> if there are <i>more than one</i> iterations. If there
 * are no iterations, or only one iteration, then it does not process the block.
 * </li>
 * <li>If Doesn't Have: <code>${if-none <i>iterable-key</i>}</code> processes
 * the block once <i>only</i> if there are no iterations. If there is even one
 * iteration, then it does not process the block.</li>
 * </ul>
 * Block tags for use within for-each blocks:
 * <ul>
 * <li>If First: <code>${if-first}</code> processes the block <i>only</i> if
 * this is the first iteration of the loop.</li>
 * <li>If Not First: <code>${if-not-first}</code> processes the block
 * <i>only</i> if this is not the first iteration of the loop.</li>
 * <li>If Has More: <code>${if-more}</code> processes the block <i>only</i> if
 * the outlying block has more iterations.</li>
 * <li>Index, 0-based: <code>${index-from-0}</code> replaces the tag with the
 * 0-based number of the current iteration of the outlying loop.</li>
 * <li>Index, 1-based: <code>${index-from-1}</code> replaces the tag with the
 * 1-based number of the current iteration of the outlying loop.</li>
 * </ul>
 * 
 * @author Tal Liron
 */
public class RichTemplate extends BasicTemplate
{
	//
	// Construction
	//

	public RichTemplate( String content, TemplateSource importTemplateSource ) throws ParseException, TemplateSourceException
	{
		super( content, importTemplateSource );
	}

	// //////////////////////////////////////////////////////////////////////////
	// Protected

	@Override
	protected Chunk parseTag( String tag ) throws ParseException
	{
		if( tag.startsWith( Count.MARK ) )
		{
			String[] using = Chunk.parseUsing( tag.substring( Count.MARK_LENGTH ) );
			return new Count( using[0], using[1] );
		}
		else if( tag.equals( Index0.MARK ) )
			return new Index0();
		else if( tag.equals( Index1.MARK ) )
			return new Index1();
		else if( tag.startsWith( ForEach.MARK ) )
		{
			String[] using = Chunk.parseUsing( tag.substring( ForEach.MARK_LENGTH ) );
			int sep = using[0].indexOf( ';' );
			if( sep != -1 )
			{
				String range = using[0].substring( sep + 1 );
				using[0] = using[0].substring( 0, sep );
				String[] ranges = range.split( ";" );
				if( ( ranges == null ) || ( ranges.length != 2 ) )
					return null;
				return new ForEach( using[0], using[1], ranges[0], ranges[1] );
			}
			else
				return new ForEach( using[0], using[1], null, null );
		}
		else if( tag.equals( IfFirst.MARK ) )
			return new IfFirst();
		else if( tag.startsWith( IfHasOne.MARK ) )
		{
			String[] using = Chunk.parseUsing( tag.substring( IfHasOne.MARK_LENGTH ) );
			return new IfHasOne( using[0], using[1] );
		}
		else if( tag.startsWith( IfHasMany.MARK ) )
		{
			String[] using = Chunk.parseUsing( tag.substring( IfHasMany.MARK_LENGTH ) );
			return new IfHasMany( using[0], using[1] );
		}
		else if( tag.equals( IfNotFirst.MARK ) )
			return new IfNotFirst();
		// IfHasNone must be under IfNotFirst
		else if( tag.startsWith( IfHasNone.MARK ) )
		{
			String string1 = tag.substring( IfHasNone.MARK_LENGTH );
			int equalIndex = string1.indexOf( '=' );
			if( equalIndex != -1 )
			{
				string1 = string1.substring( 0, equalIndex );
				String[] using = Chunk.parseUsing( string1 );
				return new IfHasNone( using[0], using[1], true );
			}
			else
			{
				String[] using = Chunk.parseUsing( string1 );
				return new IfHasNone( using[0], using[1], false );
			}
		}
		else if( tag.equals( IfHasMore.MARK ) )
			return new IfHasMore();
		else if( tag.startsWith( IfHas.MARK ) )
		{
			String string1 = tag.substring( IfHas.MARK_LENGTH );
			String[] using = Chunk.parseUsing( string1 );
			return new IfHas( using[0], using[1] );
		}
		// If must be under all other if's
		else if( tag.startsWith( If.MARK ) )
		{
			String string1 = tag.substring( If.MARK_LENGTH );
			int equalIndex = string1.indexOf( '=' );
			if( equalIndex != -1 )
			{
				string1 = string1.substring( 0, equalIndex );
				boolean equals = true;
				if( string1.endsWith( "!" ) )
				{
					string1 = string1.substring( 0, string1.length() - 1 );
					equals = false;
				}
				String string2 = tag.substring( If.MARK_LENGTH + equalIndex + 1 );
				String[] using = Chunk.parseUsing( string1 );
				return new If( using[0], using[1], equals, string2 );
			}
			else
				throw new ParseException( "\"If\" tag does not include \"=\" or \"!=\": " + tag );
		}
		else
			return super.parseTag( tag );
	}

	@Override
	protected void cast( Chunk chunk, Filler filler, Appendable out, boolean soft ) throws CastException, IOException
	{
		try
		{
			if( chunk instanceof Count )
			{
				Iterable<? extends Filler> fillers = filler.getFillers( chunk.string );
				int count = fillers != null ? TemplateUtil.count( fillers ) : 0;
				out.append( Integer.toString( count ) );
			}
			else if( chunk instanceof ForEach )
			{
				try
				{
					Iterable<? extends Filler> fillers = chunk.getUsing( filler ).getFillers( chunk.string );
					if( fillers != null )
					{
						ForEach forEach = (ForEach) chunk;
						int index = 0;
						int start = getInt( forEach.start, 0, forEach, filler );
						int limit = getInt( forEach.limit, -1, forEach, filler );

						Filler nestedFiller;
						for( Iterator<? extends Filler> i = fillers.iterator(); i.hasNext(); )
						{
							nestedFiller = i.next();
							if( start == 0 )
							{
								if( limit != 0 )
								{
									for( Chunk nestedChunk : forEach.chunks )
									{
										if( nestedChunk instanceof IfFirst )
										{
											if( index != 0 )
												continue;
										}
										else if( nestedChunk instanceof IfNotFirst )
										{
											if( index == 0 )
												continue;
										}
										else if( nestedChunk instanceof IfHasMore )
										{
											if( !i.hasNext() )
												continue;
										}
										else if( nestedChunk instanceof Index0 )
										{
											out.append( Integer.toString( index ) );
											continue;
										}
										else if( nestedChunk instanceof Index1 )
										{
											out.append( Integer.toString( index + 1 ) );
											continue;
										}

										cast( nestedChunk, new NestedFiller( nestedFiller, filler ), out, soft );
									}

									if( limit > 0 )
										if( --limit == 0 )
											break;
								}
							}
							else
								start--;

							index++;
						}
					}
				}
				catch( CastException x )
				{
				}
			}
			else if( chunk instanceof If )
			{
				If theIf = (If) chunk;
				if( !theIf.equals )
				{
					// If doesn't equal
					if( !chunk.getUsing( filler ).getValue( theIf.string ).toString().equals( theIf.string2 ) )
					{
						for( Chunk nestedChunk : ( (Block) chunk ).chunks )
							cast( nestedChunk, filler, out, soft );
					}
				}
				else if( theIf.string2.length() == 0 )
				{
					// If exists
					try
					{
						if( chunk.getUsing( filler ).getValue( theIf.string ) != null )
						{
							for( Chunk nestedChunk : ( (Block) chunk ).chunks )
								cast( nestedChunk, filler, out, soft );
						}
					}
					catch( CastException x )
					{
					}
				}
				else if( chunk.getUsing( filler ).getValue( theIf.string ).toString().equals( theIf.string2 ) )
				{
					// If equals
					for( Chunk nestedChunk : ( (Block) chunk ).chunks )
						cast( nestedChunk, filler, out, soft );
				}
			}
			else if( chunk instanceof IfHas )
			{
				// If has
				Iterable<? extends Filler> fillers = chunk.getUsing( filler ).getFillers( chunk.string );
				if( fillers != null )
				{
					if( fillers.iterator().hasNext() )
					{
						for( Chunk nestedChunk : ( (Block) chunk ).chunks )
							cast( nestedChunk, filler, out, soft );
					}
				}
			}
			else if( chunk instanceof IfHasNone )
			{
				IfHasNone ifNot = (IfHasNone) chunk;
				if( ifNot.doesntExist )
				{
					// If doesn't exist
					try
					{
						if( chunk.getUsing( filler ).getValue( ifNot.string ) == null )
							throw new CastException();
					}
					catch( CastException x )
					{
						for( Chunk nestedChunk : ( (Block) chunk ).chunks )
							cast( nestedChunk, filler, out, soft );
					}
				}
				else
				{
					// If doesn't have
					Iterable<? extends Filler> fillers = chunk.getUsing( filler ).getFillers( chunk.string );
					if( ( fillers == null ) || !fillers.iterator().hasNext() )
					{
						for( Chunk nestedChunk : ( (Block) chunk ).chunks )
							cast( nestedChunk, filler, out, soft );
					}
				}
			}
			else if( chunk instanceof IfHasOne )
			{
				Iterable<? extends Filler> fillers = chunk.getUsing( filler ).getFillers( chunk.string );
				if( fillers != null )
				{
					Iterator<? extends Filler> i = fillers.iterator();
					if( i.hasNext() )
					{
						Filler nestedTemplateFiller = i.next();
						if( !i.hasNext() )
						{
							for( Chunk nestedChunk : ( (Block) chunk ).chunks )
								cast( nestedChunk, new NestedFiller( nestedTemplateFiller, filler ), out, soft );
						}
					}
				}
			}
			else if( chunk instanceof IfHasMany )
			{
				Iterable<? extends Filler> fillers = chunk.getUsing( filler ).getFillers( chunk.string );
				if( fillers != null )
				{
					Iterator<? extends Filler> i = fillers.iterator();
					if( i.hasNext() )
					{
						i.next();
						if( i.hasNext() )
						{
							for( Chunk nestedChunk : ( (Block) chunk ).chunks )
								cast( nestedChunk, filler, out, soft );
						}
					}
				}
			}
			else if( ( chunk instanceof IfFirst ) || ( chunk instanceof IfNotFirst ) || ( chunk instanceof IfHasMore ) )
			{
				// If we got here, then we're not in an enclosing block, so
				// simply ignore these
				for( Chunk nestedChunk : ( (Block) chunk ).chunks )
					cast( nestedChunk, filler, out, soft );
			}
			else
				super.cast( chunk, filler, out, soft );
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

	private static int getInt( String string, int def, Chunk chunk, Filler filler ) throws CastException
	{
		if( string != null )
		{
			try
			{
				return Integer.parseInt( string );
			}
			catch( NumberFormatException x )
			{
				try
				{
					return Integer.parseInt( chunk.getUsing( filler ).getValue( string ).toString() );
				}
				catch( NumberFormatException xx )
				{
				}
			}
		}
		return def;
	}
}
