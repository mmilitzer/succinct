/**
 * Copyright 2009-2016 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.gnu.org/copyleft/lesser.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct.source;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.threecrickets.succinct.TemplateSource;
import com.threecrickets.succinct.TemplateSourceException;
import com.threecrickets.succinct.TemplateUtil;

/**
 * A {@link TemplateSource} that uses the class loader to load templates.
 * {@code .template} is added to all template names.
 * 
 * @author Tal Liron
 */
public class ClassLoaderTemplateSource implements TemplateSource
{
	//
	// Construction
	//

	public ClassLoaderTemplateSource()
	{
		this( (String) null );
	}

	public ClassLoaderTemplateSource( String extension )
	{
		this( null, ".template" );
	}

	public ClassLoaderTemplateSource( Iterable<String> templatePrefixes )
	{
		this( templatePrefixes, ".template" );
	}

	public ClassLoaderTemplateSource( Iterable<String> templatePrefixes, String extension )
	{
		this.templatePrefixes = templatePrefixes;
		this.extension = extension;
	}

	//
	// TemplateSource
	//

	public String getTemplate( String name ) throws TemplateSourceException
	{
		String template = cache.get( name );
		if( template != null )
			return template;
		else
		{
			try
			{
				ClassLoader loader = getClass().getClassLoader();
				InputStream stream = loader.getResourceAsStream( name + extension );

				if( stream == null )
				{
					// Try loading with prefixes
					for( String prefix : templatePrefixes )
					{
						stream = loader.getResourceAsStream( prefix + name + extension );
						if( stream != null )
							break;
					}
				}

				if( stream == null )
					throw new TemplateSourceException( "Cannot load template: " + name );

				template = TemplateUtil.getString( stream );
				cache.putIfAbsent( name, template );
				return template;
			}
			catch( IOException x )
			{
				throw new TemplateSourceException( x );
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final String extension;

	private Iterable<String> templatePrefixes;

	private final ConcurrentMap<String, String> cache = new ConcurrentHashMap<String, String>();
}
