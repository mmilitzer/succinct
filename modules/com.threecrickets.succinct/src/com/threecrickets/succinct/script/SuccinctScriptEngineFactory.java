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

package com.threecrickets.succinct.script;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import com.threecrickets.succinct.TemplateSource;

public class SuccinctScriptEngineFactory implements ScriptEngineFactory
{
	//
	// ScriptEngineFactory
	//

	public String getEngineName()
	{
		return ENGINE_NAME;
	}

	public String getEngineVersion()
	{
		return ENGINE_VERSION;
	}

	public String getLanguageName()
	{
		return LANGUAGE_NAME;
	}

	public String getLanguageVersion()
	{
		return LANGUAGE_VERSION;
	}

	public Object getParameter( String key )
	{
		if( key.equals( ScriptEngine.ENGINE ) )
			return getEngineName();
		else if( key.equals( ScriptEngine.ENGINE_VERSION ) )
			return getEngineVersion();
		if( key.equals( ScriptEngine.LANGUAGE ) )
			return getLanguageName();
		if( key.equals( ScriptEngine.LANGUAGE_VERSION ) )
			return getLanguageVersion();
		if( key.equals( ScriptEngine.NAME ) )
			return getNames().get( 0 );
		if( key.equals( "THREADING" ) )
			return "STATELESS";
		else
			return null;
	}

	public List<String> getNames()
	{
		return NAMES;
	}

	public List<String> getExtensions()
	{
		return EXTENSIONS;
	}

	public List<String> getMimeTypes()
	{
		return MIME_TYPES;
	}

	public String getProgram( String... statement )
	{
		StringWriter w = new StringWriter();
		for( String s : statement )
			w.append( s );
		return w.toString();
	}

	public String getOutputStatement( String text )
	{
		return text;
	}

	public String getMethodCallSyntax( String obj, String m, String... args )
	{
		return "";
	}

	public ScriptEngine getScriptEngine()
	{
		return new SuccinctScriptEngine( this );
	}

	//
	// Attributes
	//

	public TemplateSource getTemplateSource()
	{
		return templateSource;
	}

	public void setTemplateSource( TemplateSource templateSource )
	{
		this.templateSource = templateSource;
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static final String ENGINE_NAME = "Three Crickets Succinct";

	private static final String ENGINE_VERSION = "1.0";

	private static final String LANGUAGE_NAME = "Succinct";

	private static final String LANGUAGE_VERSION = "1.0";

	private static final List<String> NAMES = Arrays.asList( "succinct" );

	private static final List<String> EXTENSIONS = Arrays.asList( "succinct" );

	private static final List<String> MIME_TYPES = Collections.emptyList();

	private TemplateSource templateSource;
}
