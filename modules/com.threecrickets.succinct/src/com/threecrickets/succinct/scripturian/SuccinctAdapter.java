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

import javax.script.ScriptEngine;

import com.threecrickets.scripturian.Executable;
import com.threecrickets.scripturian.LanguageAdapter;
import com.threecrickets.scripturian.adapter.Jsr223LanguageAdapter;
import com.threecrickets.scripturian.adapter.ScriptEngines;
import com.threecrickets.scripturian.exception.LanguageInitializationException;
import com.threecrickets.succinct.BasicTemplate;
import com.threecrickets.succinct.chunk.Tag;

/**
 * An {@link LanguageAdapter} that supports Succinct templates.
 * 
 * @author Tal Liron
 */
@ScriptEngines("succinct")
public class SuccinctAdapter extends Jsr223LanguageAdapter
{
	//
	// ScriptletHelper
	//

	public SuccinctAdapter() throws LanguageInitializationException
	{
		super();
	}

	@Override
	public String getTextAsProgram( Executable document, ScriptEngine engine, String content )
	{
		return content;
	}

	@Override
	public String getExpressionAsProgram( Executable document, ScriptEngine engine, String content )
	{
		return Tag.BEGIN + content + Tag.END;
	}

	@Override
	public String getExpressionAsInclude( Executable document, ScriptEngine engine, String content )
	{
		return BasicTemplate.TAG_IMPORT_BEGIN + content + Tag.END;
	}

	@Override
	public String getInvocationAsProgram( Executable document, ScriptEngine engine, String content )
	{
		return "";
	}
}
