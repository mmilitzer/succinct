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

import java.beans.IntrospectionException;
import java.util.Iterator;

/**
 * @author Tal Liron
 */
public class BeanFillerWrappingIterator implements Iterator<BeanFillerWrapper>
{
	//
	// Construction
	//

	public BeanFillerWrappingIterator( String keyPrefix, Iterator<?> iterator )
	{
		this.keyPrefix = keyPrefix;
		this.iterator = iterator;
	}

	//
	// Iterator
	//

	public boolean hasNext()
	{
		return iterator.hasNext();
	}

	public BeanFillerWrapper next()
	{
		Object next = iterator.next();
		if( next == null )
			return null;
		else
		{
			try
			{
				return new BeanFillerWrapper( keyPrefix, next );
			}
			catch( IntrospectionException x )
			{
				return null;
			}
		}
	}

	public void remove()
	{
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final String keyPrefix;

	private final Iterator<?> iterator;
}