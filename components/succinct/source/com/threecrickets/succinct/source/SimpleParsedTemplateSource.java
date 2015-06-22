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

package com.threecrickets.succinct.source;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.threecrickets.succinct.ParsedTemplateSource;
import com.threecrickets.succinct.RichTemplate;
import com.threecrickets.succinct.Template;
import com.threecrickets.succinct.TemplateSource;
import com.threecrickets.succinct.TemplateSourceException;

/**
 * Note: Uses the TemplateSource for imports.
 * 
 * @author Tal Liron
 */
public class SimpleParsedTemplateSource implements ParsedTemplateSource
{
	//
	// Construction
	//

	public SimpleParsedTemplateSource( TemplateSource templateSource ) throws TemplateSourceException
	{
		this( templateSource, RichTemplate.class );
	}

	public SimpleParsedTemplateSource( TemplateSource templateSource, Class<? extends Template> templateClass ) throws TemplateSourceException
	{
		this.templateSource = templateSource;
		try
		{
			constructor = templateClass.getConstructor( String.class, TemplateSource.class );
		}
		catch( SecurityException x )
		{
			throw new TemplateSourceException( x );
		}
		catch( NoSuchMethodException x )
		{
			throw new TemplateSourceException( x );
		}
	}

	//
	// ParsedTemplateSource
	//

	public Template getParsedTemplate( String name ) throws TemplateSourceException
	{
		Template template = cache.get( name );
		if( template != null )
			return template;
		else
		{
			String source = templateSource.getTemplate( name );
			try
			{
				// Note: Uses the TemplateSource for imports
				template = constructor.newInstance( source, templateSource );
				cache.putIfAbsent( name, template );
				return template;
			}
			catch( IllegalArgumentException x )
			{
				throw new TemplateSourceException( x );
			}
			catch( InstantiationException x )
			{
				throw new TemplateSourceException( x );
			}
			catch( IllegalAccessException x )
			{
				throw new TemplateSourceException( x );
			}
			catch( InvocationTargetException x )
			{
				throw new TemplateSourceException( x );
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final Constructor<? extends Template> constructor;

	private final TemplateSource templateSource;

	private final ConcurrentMap<String, Template> cache = new ConcurrentHashMap<String, Template>();
}
