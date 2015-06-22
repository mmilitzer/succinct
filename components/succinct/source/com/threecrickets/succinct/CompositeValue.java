/**
 * Copyright 2009-2015 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.gnu.org/copyleft/lesser.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A {@link Filler#getValue(String)} can return this value to mark that parts of
 * the value should be handled by a {@link ExternalHandler}.
 * 
 * @author Tal Liron
 */
public class CompositeValue implements Iterable<Object>, Serializable
{
	//
	// Types
	//

	public static class External implements Serializable
	{
		private External( String extenalKey )
		{
			this.externalKey = extenalKey;
		}

		public final String externalKey;

		private static final long serialVersionUID = 1L;
	}

	//
	// Attributes
	//

	public void add( Object object )
	{
		objects.add( object );
	}

	public void addExternal( String externalKey )
	{
		objects.add( new External( externalKey ) );
	}

	//
	// Iterable
	//

	public Iterator<Object> iterator()
	{
		return objects.iterator();
	}

	//
	// Object
	//

	@Override
	public String toString()
	{
		// Ignores externals
		StringBuilder buffer = new StringBuilder( 512 );
		for( Object cast : objects )
			if( !( cast instanceof External ) )
				buffer.append( cast );
		return buffer.toString();
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static final long serialVersionUID = 1L;

	private final List<Object> objects = new ArrayList<Object>();
}
