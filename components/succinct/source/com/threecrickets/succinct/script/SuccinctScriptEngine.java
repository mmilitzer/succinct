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

package com.threecrickets.succinct.script;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.threecrickets.succinct.ParseException;
import com.threecrickets.succinct.RichTemplate;
import com.threecrickets.succinct.TemplateSource;
import com.threecrickets.succinct.TemplateSourceException;
import com.threecrickets.succinct.TemplateUtil;

public class SuccinctScriptEngine implements ScriptEngine, Compilable
{
	public static final String SOURCE = "templateSource";

	public static final String FORMATTER = "templateFormatter";

	public static final String FILLER = "templateFiller";

	public static final String CASTER = "templateCaster";

	public static final String CASTER_ATTRIBUTES = "templateCasterAttributes";

	//
	// ScriptEngine
	//

	public ScriptEngineFactory getFactory()
	{
		return factory;
	}

	public ScriptContext getContext()
	{
		return scriptContext;
	}

	public void setContext( ScriptContext scriptContext )
	{
		this.scriptContext = scriptContext;
	}

	public Bindings getBindings( int scope )
	{
		Bindings bindings = this.bindings.get( scope );
		if( ( bindings == null ) && ( ( scope == ScriptContext.ENGINE_SCOPE ) || ( scope == ScriptContext.GLOBAL_SCOPE ) ) )
		{
			bindings = createBindings();
			setBindings( bindings, scope );
		}
		return bindings;
	}

	public void setBindings( Bindings bindings, int scope )
	{
		this.bindings.put( scope, bindings );
	}

	public Bindings createBindings()
	{
		return new SimpleBindings();
	}

	public Object get( String key )
	{
		return getBindings( ScriptContext.ENGINE_SCOPE ).get( key );
	}

	public void put( String key, Object value )
	{
		getBindings( ScriptContext.ENGINE_SCOPE ).put( key, value );
	}

	public Object eval( String script ) throws ScriptException
	{
		return eval( script, scriptContext );
	}

	public Object eval( Reader reader ) throws ScriptException
	{
		try
		{
			return eval( TemplateUtil.getString( reader ) );
		}
		catch( IOException x )
		{
			throw new ScriptException( x );
		}
	}

	/*
	 * Override this, if you must
	 */
	public Object eval( String script, ScriptContext scriptContext ) throws ScriptException
	{
		return compile( script ).eval( scriptContext );
	}

	public Object eval( Reader reader, ScriptContext scriptContext ) throws ScriptException
	{
		try
		{
			return eval( TemplateUtil.getString( reader ), scriptContext );
		}
		catch( IOException x )
		{
			throw new ScriptException( x );
		}
	}

	/*
	 * Override this, if you must
	 */
	public Object eval( String script, Bindings bindings ) throws ScriptException
	{
		return compile( script ).eval( bindings );
	}

	public Object eval( Reader reader, Bindings bindings ) throws ScriptException
	{
		try
		{
			return eval( TemplateUtil.getString( reader ), bindings );
		}
		catch( IOException x )
		{
			throw new ScriptException( x );
		}
	}

	//
	// Compilable
	//

	public CompiledScript compile( Reader reader ) throws ScriptException
	{
		try
		{
			return compile( TemplateUtil.getString( reader ) );
		}
		catch( IOException x )
		{
			throw new ScriptException( x );
		}
	}

	public CompiledScript compile( String script ) throws ScriptException
	{
		TemplateSource rawTemplateSource = null;

		ScriptContext scriptContext = getContext();
		if( scriptContext != null )
			rawTemplateSource = (TemplateSource) scriptContext.getAttribute( SOURCE, ScriptContext.ENGINE_SCOPE );

		if( rawTemplateSource == null )
			rawTemplateSource = ( (SuccinctScriptEngineFactory) getFactory() ).getTemplateSource();

		try
		{
			return new SuccinctCompiledScript( this, new RichTemplate( script, rawTemplateSource ) );
		}
		catch( ParseException x )
		{
			throw new ScriptException( x );
		}
		catch( TemplateSourceException x )
		{
			throw new ScriptException( x );
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final ScriptEngineFactory factory;

	private final Map<Integer, Bindings> bindings = new HashMap<Integer, Bindings>();

	private ScriptContext scriptContext;

	// //////////////////////////////////////////////////////////////////////////
	// Protected

	protected SuccinctScriptEngine( SuccinctScriptEngineFactory factory )
	{
		this.factory = factory;
	}
}
