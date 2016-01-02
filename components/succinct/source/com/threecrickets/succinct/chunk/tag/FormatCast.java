/**
 * Copyright 2009-2016 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.gnu.org/copyleft/lesser.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct.chunk.tag;

import com.threecrickets.succinct.Syntax;

public class FormatCast extends Cast
{
	//
	// Constants
	//

	public static final String MARK = Syntax.get( "FormatCast" );

	public static final int MARK_LENGTH = MARK.length();

	//
	// Construction
	//

	public FormatCast( String tag, String using, String format )
	{
		super( tag, using );
		this.format = format;
	}

	//
	// Attributes
	//

	public final String format;

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static final long serialVersionUID = 1L;
}