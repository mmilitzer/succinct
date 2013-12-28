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

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.threecrickets.succinct.CastException;
import com.threecrickets.succinct.Filler;

/**
 * A {@link Filler} wrapper that caches all values, including nested fillers.
 * Can improve performance, thought obviously does not allow for dymanically
 * changing fillers.
 * 
 * @author Tal Liron
 */
public class CachingFillerWrapper implements Filler
{
	public CachingFillerWrapper( Filler filler )
	{
		this.filler = filler;
	}

	public Object getValue( String key ) throws CastException
	{
		Object value = valueCache.get( key );
		if( value == null )
		{
			value = filler.getValue( key );
			if( value == null )
				value = Void.TYPE;
			Object existing = valueCache.putIfAbsent( key, value );
			if( existing != null )
				value = existing;
		}

		if( value != Void.TYPE )
			return value;
		else
			throw new CastException( key );
	}

	@SuppressWarnings("unchecked")
	public Iterable<? extends Filler> getFillers( String iteratorKey ) throws CastException
	{
		Object wrappedFillers = fillersCache.get( iteratorKey );
		if( wrappedFillers == null )
		{
			Iterable<? extends Filler> fillers = filler.getFillers( iteratorKey );
			if( fillers == null )
				wrappedFillers = Void.TYPE;
			else
			{
				wrappedFillers = new ArrayList<CachingFillerWrapper>();
				for( Filler filler : fillers )
					( (ArrayList<CachingFillerWrapper>) wrappedFillers ).add( new CachingFillerWrapper( filler ) );
			}
			Iterable<? extends Filler> existing = (Iterable<? extends Filler>) fillersCache.putIfAbsent( iteratorKey, wrappedFillers );
			if( existing != null )
				wrappedFillers = existing;
		}

		if( wrappedFillers != Void.TYPE )
			return (Iterable<? extends Filler>) wrappedFillers;
		else
			throw new CastException( iteratorKey );
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final Filler filler;

	private final ConcurrentMap<String, Object> valueCache = new ConcurrentHashMap<String, Object>();

	private final ConcurrentMap<String, Object> fillersCache = new ConcurrentHashMap<String, Object>();
}
