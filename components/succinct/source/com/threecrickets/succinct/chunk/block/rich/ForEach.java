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

package com.threecrickets.succinct.chunk.block.rich;

import com.threecrickets.succinct.Syntax;
import com.threecrickets.succinct.chunk.Block;
import com.threecrickets.succinct.chunk.Chunk;
import com.threecrickets.succinct.chunk.Tag;

public class ForEach extends Block
{
	//
	// Constants
	//

	public static final String MARK = Syntax.get( "ForEach" );

	public static final int MARK_LENGTH = MARK.length();

	//
	// Construction
	//

	public ForEach( String string, String using, String start, String limit )
	{
		super( string, using );
		this.start = start;
		this.limit = limit;
	}

	//
	// Attributes
	//

	public final String start;

	public final String limit;

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