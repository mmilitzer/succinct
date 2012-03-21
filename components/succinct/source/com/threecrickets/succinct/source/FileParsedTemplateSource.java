/**
 * Copyright 2009-2012 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.gnu.org/copyleft/lesser.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct.source;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.threecrickets.succinct.Formatter;
import com.threecrickets.succinct.ParseException;
import com.threecrickets.succinct.ParsedTemplateSource;
import com.threecrickets.succinct.RichTemplate;
import com.threecrickets.succinct.Template;
import com.threecrickets.succinct.TemplateSource;
import com.threecrickets.succinct.TemplateSourceException;
import com.threecrickets.succinct.TemplateUtil;

/**
 * A {@link ParsedTemplateSource} that reads templates stored in files in a
 * directory. The templates are cached, and checked for validity according to
 * the files' modification timestamp.
 * 
 * @author Tal Liron
 */
public class FileParsedTemplateSource implements TemplateSource, ParsedTemplateSource
{
	//
	// Construction
	//

	public FileParsedTemplateSource( File basePath, Formatter templateFormatter )
	{
		this.basePath = basePath;
		this.templateFormatter = templateFormatter;
	}

	//
	// TemplateSource
	//

	public String getTemplate( String name ) throws TemplateSourceException
	{
		return getEntry( name ).source;
	}

	//
	// ParsedTemplateSource
	//

	public Template getParsedTemplate( String name ) throws ParseException, TemplateSourceException
	{
		return getEntry( name ).getTemplate( this, templateFormatter );
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final String extension = ".template";

	private final ConcurrentMap<String, Entry> entries = new ConcurrentHashMap<String, Entry>();

	private final File basePath;

	private final Formatter templateFormatter;

	private static class Entry
	{
		private Entry( File file ) throws TemplateSourceException
		{
			try
			{
				source = TemplateUtil.getString( file );
			}
			catch( IOException x )
			{
				throw new TemplateSourceException( x );
			}

			timestamp = file.lastModified();
		}

		private boolean isValid( File file )
		{
			long timestamp = file.lastModified();
			return timestamp <= this.timestamp;
		}

		private Template getTemplate( TemplateSource templateSource, Formatter templateFormatter ) throws ParseException, TemplateSourceException
		{
			if( template == null )
			{
				template = new RichTemplate( source, templateSource );
				if( templateFormatter != null )
					template.setFormatter( templateFormatter );
			}

			return template;
		}

		private final String source;

		private final long timestamp;

		private Template template;
	}

	private Entry getEntry( String name ) throws TemplateSourceException
	{
		File file = new File( basePath, name + extension );
		name = file.getPath();

		Entry entry = entries.get( name );

		if( entry != null )
		{
			if( !entry.isValid( file ) )
				entry = null;
		}

		if( entry == null )
		{
			entry = new Entry( file );
			Entry existing = entries.putIfAbsent( name, entry );
			if( existing != null )
				entry = existing;
		}

		return entry;
	}
}
