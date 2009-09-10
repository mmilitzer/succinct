/**
 * Copyright 2009 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://www.threecrickets.com/
 */

package com.threecrickets.succinct.scripturian;

import javax.script.ScriptEngine;

import com.threecrickets.scripturian.Document;
import com.threecrickets.scripturian.ScriptEngines;
import com.threecrickets.scripturian.ScriptletParsingHelper;
import com.threecrickets.succinct.BasicTemplate;
import com.threecrickets.succinct.chunk.Tag;

/**
 * An {@link ScriptletParsingHelper} that supports Succinct templates.
 * 
 * @author Tal Liron
 */
@ScriptEngines("succinct")
public class SuccinctScriptletParsingHelper implements ScriptletParsingHelper
{
	//
	// ScriptletParsingHelper
	//

	public boolean isPrintOnEval()
	{
		return false;
	}

	public String getScriptletHeader( Document document, ScriptEngine engine )
	{
		return "";
	}

	public String getScriptletFooter( Document document, ScriptEngine engine )
	{
		return "";
	}

	public String getTextAsProgram( Document document, ScriptEngine engine, String content )
	{
		return content;
	}

	public String getExpressionAsProgram( Document document, ScriptEngine engine, String content )
	{
		return Tag.BEGIN + content + Tag.END;
	}

	public String getExpressionAsInclude( Document document, ScriptEngine engine, String content )
	{
		return BasicTemplate.TAG_IMPORT_BEGIN + content + Tag.END;
	}

	public String getInvocationAsProgram( Document document, ScriptEngine engine, String content )
	{
		return "";
	}
}
