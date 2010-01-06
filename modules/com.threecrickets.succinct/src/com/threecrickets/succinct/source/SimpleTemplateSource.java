/**
 * Copyright 2009-2010 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct.source;

import java.util.HashMap;
import java.util.Map;

import com.threecrickets.succinct.TemplateSource;
import com.threecrickets.succinct.TemplateSourceException;

public class SimpleTemplateSource implements TemplateSource
{
	//
	// Construction
	//

	public SimpleTemplateSource()
	{
		this( new HashMap<String, String>() );
	}

	public SimpleTemplateSource( Map<String, String> templateMap )
	{
		this.templateMap = templateMap;
	}

	//
	// Attributes
	//

	public Map<String, String> getTemplateMap()
	{
		return templateMap;
	}

	//
	// TemplateSource
	//

	public String getTemplate( String name ) throws TemplateSourceException
	{
		return templateMap.get( name );
	}

	public Iterable<String> getImportPrefixes()
	{
		return null;
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final Map<String, String> templateMap;
}
