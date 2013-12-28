/**
 * Copyright 2009-2014 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.gnu.org/copyleft/lesser.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct.chunk.block;

import com.threecrickets.succinct.Syntax;
import com.threecrickets.succinct.chunk.Block;

public class Flatten extends Block
{
	//
	// Constants
	//

	public static final String MARK = Syntax.get( "Flatten" );

	public static final int MARK_LENGTH = MARK.length();

	//
	// Construction
	//

	public Flatten()
	{
		super();
	}

	//
	// Block
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