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

package com.threecrickets.succinct.filler;

import java.util.Iterator;

import com.threecrickets.succinct.Filler;

/**
 * A wrapper for a {@link Filler} iterator that wraps each element in
 * turn with a {@link NestedFiller} wrapper.
 * 
 * @author Tal Liron
 */
public class NestedFillersChain implements Iterable<Filler>
{
	//
	// Construction
	//

	public NestedFillersChain( Iterable<? extends Filler> iterable, Filler parent )
	{
		this.iterable = iterable;
		this.parent = parent;
	}

	//
	// Iterable
	//

	public Iterator<Filler> iterator()
	{
		return new ChainParentIterator();
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final Iterable<? extends Filler> iterable;

	private final Filler parent;

	private class ChainParentIterator implements Iterator<Filler>
	{
		public boolean hasNext()
		{
			return iterator.hasNext();
		}

		public Filler next()
		{
			return new NestedFiller( iterator.next(), parent );
		}

		public void remove()
		{
			iterator.remove();
		}

		private Iterator<? extends Filler> iterator = iterable.iterator();
	}
}
