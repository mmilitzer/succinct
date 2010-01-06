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

package com.threecrickets.succinct.chunk;

import java.util.ArrayList;
import java.util.List;

public abstract class Block extends Chunk
{
	//
	// Construction
	//

	public Block()
	{
		super();
	}

	public Block( String string, String using )
	{
		super( string, using );
	}

	//
	// Attributes
	//

	public final List<Chunk> chunks = new ArrayList<Chunk>();

	public abstract String asTag();

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