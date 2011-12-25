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

package com.threecrickets.succinct.chunk.block.rich;

import com.threecrickets.succinct.Syntax;
import com.threecrickets.succinct.chunk.Block;
import com.threecrickets.succinct.chunk.Chunk;
import com.threecrickets.succinct.chunk.Tag;

public class If extends Block
{
	//
	// Constants
	//

	public static final String MARK = Syntax.get( "If" );

	public static final int MARK_LENGTH = MARK.length();

	//
	// Construction
	//

	public If( String string1, String using, boolean equals, String string2 )
	{
		super( string1, using );
		this.equals = equals;
		this.string2 = string2;
	}

	//
	// Attributes
	//

	public final String string2;

	public final boolean equals;

	//
	// Block
	//

	@Override
	public String asTag()
	{
		return MARK;
	}

	//
	// Object
	//

	@Override
	public String toString()
	{
		StringBuilder buffer = new StringBuilder( 512 );
		buffer.append( Tag.BEGIN );
		buffer.append( asTag() );
		if( string != null )
			buffer.append( string );
		if( string2 != null )
		{
			buffer.append( equals ? "=" : "!=" );
			buffer.append( string2 );
		}
		buffer.append( Tag.END );
		for( Chunk subChunk : chunks )
			buffer.append( subChunk );
		buffer.append( Tag.BEGIN );
		buffer.append( Tag.END );
		return buffer.toString();
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static final long serialVersionUID = 1L;
}