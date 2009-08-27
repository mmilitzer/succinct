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

import java.io.IOException;

import com.threecrickets.scripturian.ScriptSource;
import com.threecrickets.succinct.TemplateSource;
import com.threecrickets.succinct.TemplateSourceException;

public class ScripturianTemplateSource<S> implements TemplateSource
{
	//
	// Construction
	//

	public ScripturianTemplateSource( ScriptSource<S> scriptSource )
	{
		this.scriptSource = scriptSource;
	}

	//
	// TemplateSource
	//

	public String getTemplate( String name ) throws TemplateSourceException
	{
		try
		{
			return scriptSource.getScriptDescriptor( name ).getText();
		}
		catch( IOException x )
		{
			throw new TemplateSourceException( x );
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final ScriptSource<S> scriptSource;
}
