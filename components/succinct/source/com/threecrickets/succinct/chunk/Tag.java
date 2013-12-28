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

package com.threecrickets.succinct.chunk;

import com.threecrickets.succinct.Syntax;

public abstract class Tag extends Chunk
{
	//
	// Constants
	//

	public static final String BEGIN = Syntax.get( "Tag.begin" );

	public static final int BEGIN_LENGTH = BEGIN.length();

	public static final String END = Syntax.get( "Tag.end" );

	public static final int END_LENGTH = END.length();

	//
	// Construction
	//

	public Tag()
	{
		super();
	}

	public Tag( String tag, String using )
	{
		super( tag, using );
	}

	//
	// Attributes
	//

	public abstract String asTag();

	//
	// Chunk
	//

	@Override
	public String[] getRequiredTags()
	{
		if( string == null )
			return super.getRequiredTags();
		else
			return new String[]
			{
				string
			};
	}

	//
	// Object
	//

	@Override
	public String toString()
	{
		return Tag.BEGIN + asTag() + ( using == null ? "" : Chunk.USING_BEGIN + using + Chunk.USING_END ) + ( string == null ? "" : string ) + Tag.END;
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static final long serialVersionUID = 1L;
}