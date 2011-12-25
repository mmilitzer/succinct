/**
 * Copyright 2009-2011 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct.chunk.tag.rich;

import com.threecrickets.succinct.Syntax;
import com.threecrickets.succinct.chunk.Tag;

public class Index1 extends Tag
{
	//
	// Constants
	//

	public static final String MARK = Syntax.get( "Index1" );

	public static final int MARK_LENGTH = MARK.length();

	//
	// Construction
	//

	public Index1()
	{
		super();
	}

	//
	// Tag
	//

	@Override
	public String asTag()
	{
		return MARK;
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static final long serialVersionUID = 1L;
}