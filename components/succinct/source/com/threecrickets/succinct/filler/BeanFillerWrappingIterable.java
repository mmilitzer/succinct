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

package com.threecrickets.succinct.filler;

import java.util.Iterator;

/**
 * @author Tal Liron
 */
public class BeanFillerWrappingIterable implements Iterable<BeanFillerWrapper>
{
	//
	// Construction
	//

	public BeanFillerWrappingIterable( String keyPrefix, Iterable<?> iterable )
	{
		this.keyPrefix = keyPrefix;
		this.iterable = iterable;
	}

	//
	// Iterable
	//

	public Iterator<BeanFillerWrapper> iterator()
	{
		return new BeanFillerWrappingIterator( keyPrefix, iterable.iterator() );
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final String keyPrefix;

	private final Iterable<?> iterable;
}