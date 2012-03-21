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

import java.util.Map;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.threecrickets.succinct.CastException;
import com.threecrickets.succinct.Caster;
import com.threecrickets.succinct.Filler;
import com.threecrickets.succinct.Formatter;
import com.threecrickets.succinct.Template;

public class SuccinctCompiledScript extends CompiledScript
{
	//
	// Construction
	//

	public SuccinctCompiledScript( ScriptEngine scriptEngine, Template template )
	{
		this.scriptEngine = scriptEngine;
		this.template = template;
	}

	//
	// CompiledScript
	//

	@SuppressWarnings("unchecked")
	@Override
	public Object eval( ScriptContext scriptContext ) throws ScriptException
	{
		Formatter formatter = (Formatter) scriptContext.getAttribute( SuccinctScriptEngine.FORMATTER );
		template.setFormatter( formatter );

		Filler filler = (Filler) scriptContext.getAttribute( SuccinctScriptEngine.FILLER );
		if( filler != null )
		{
			try
			{
				template.cast( filler, scriptContext.getWriter() );
				return null;
			}
			catch( CastException x )
			{
				throw new ScriptException( x );
			}
		}

		Caster<Map<String, String>> caster = (Caster<Map<String, String>>) scriptContext.getAttribute( SuccinctScriptEngine.CASTER );
		if( caster == null )
			throw new ScriptException( "Script context must contain either a \"" + SuccinctScriptEngine.FILLER + "\" or a \"" + SuccinctScriptEngine.CASTER + "\" attribute" );

		Map<String, String> casterAttributes = (Map<String, String>) scriptContext.getAttribute( SuccinctScriptEngine.CASTER_ATTRIBUTES );

		try
		{
			caster.cast( template, null, scriptContext.getWriter(), casterAttributes );
			return null;
		}
		catch( CastException x )
		{
			throw new ScriptException( x );
		}
	}

	@Override
	public ScriptEngine getEngine()
	{
		return scriptEngine;
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final ScriptEngine scriptEngine;

	private final Template template;
}
