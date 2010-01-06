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

package com.threecrickets.succinct.scripturian;

import java.util.Map;

import javax.script.ScriptContext;

import com.threecrickets.scripturian.ScriptletController;
import com.threecrickets.scripturian.exception.DocumentRunException;
import com.threecrickets.succinct.Caster;
import com.threecrickets.succinct.Filler;
import com.threecrickets.succinct.Formatter;
import com.threecrickets.succinct.TemplateSource;

public abstract class SuccinctScriptletController implements ScriptletController
{
	//
	// Constants
	//

	public static final String SOURCE = "templateSource";

	public static final String FORMATTER = "templateFormatter";

	public static final String FILLER = "templateFiller";

	public static final String CASTER = "templateCaster";

	public static final String CASTER_ATTRIBUTES = "templateCasterAttributes";

	//
	// Construction
	//

	public SuccinctScriptletController( TemplateSource templateSource, Formatter formatter, Filler filler )
	{
		this.templateSource = templateSource;
		this.formatter = formatter;
		this.filler = filler;
		caster = null;
	}

	public SuccinctScriptletController( TemplateSource templateSource, Formatter formatter, Caster<Map<String, String>> caster )
	{
		this.templateSource = templateSource;
		this.formatter = formatter;
		this.caster = caster;
		filler = null;
	}

	//
	// Attributes
	//

	public TemplateSource getTemplateSource()
	{
		return templateSource;
	}

	public Formatter getFormatter()
	{
		return formatter;
	}

	public Filler getFiller()
	{
		return filler;
	}

	public Caster<Map<String, String>> getCaster()
	{
		return caster;
	}

	public abstract Map<String, String> getCasterAttributes();

	//
	// ScriptletController
	//

	public void initialize( ScriptContext scriptContext ) throws DocumentRunException
	{
		scriptContext.setAttribute( SOURCE, templateSource, ScriptContext.ENGINE_SCOPE );
		if( formatter != null )
			scriptContext.setAttribute( FORMATTER, formatter, ScriptContext.ENGINE_SCOPE );
		scriptContext.setAttribute( SOURCE, templateSource, ScriptContext.ENGINE_SCOPE );
		if( filler != null )
			scriptContext.setAttribute( FILLER, filler, ScriptContext.ENGINE_SCOPE );
		else
		{
			scriptContext.setAttribute( CASTER, caster, ScriptContext.ENGINE_SCOPE );
			scriptContext.setAttribute( CASTER_ATTRIBUTES, getCasterAttributes(), ScriptContext.ENGINE_SCOPE );
		}
	}

	public void finalize( ScriptContext scriptContext )
	{
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final TemplateSource templateSource;

	private final Formatter formatter;

	private final Filler filler;

	private final Caster<Map<String, String>> caster;
}
