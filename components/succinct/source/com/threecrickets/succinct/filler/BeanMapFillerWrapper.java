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
import java.util.Map;

import com.threecrickets.succinct.CastException;
import com.threecrickets.succinct.Filler;

/**
 * A {@link Filler} that supports getting values from a map of plain-old Java
 * objects (POJOs) via the Java bean property mechanism.
 * <p>
 * The dot convention is used to recursively delve into properties. For example,
 * the key {@code obj1.prop1.prop2.prop3} would be identical to the Java call
 * {@code obj.getProp1().getProp2().getProp3()}.
 * 
 * @author Tal Liron
 */
public class BeanMapFillerWrapper implements Filler
{
	//
	// Construction
	//

	public BeanMapFillerWrapper( Map<String, Object> baseValues )
	{
		this.baseValues = baseValues;
	}

	//
	// Filler
	//

	public Object getValue( String key ) throws CastException
	{
		String[] split = key.split( "\\." );
		if( split.length > 0 )
		{
			String ourKey = split[0];
			Object value = baseValues.get( ourKey );
			if( split.length == 1 )
				// It's us!
				return value;
			else if( value != null )
			{
				// Delegate to property
				try
				{
					return new BeanFillerWrapper( ourKey, value ).getValue( key );
				}
				catch( IntrospectionException x )
				{
					throw new CastException( x, key );
				}
			}
		}

		throw new CastException( key );
	}

	public Iterable<? extends Filler> getFillers( String iteratorKey ) throws CastException
	{
		Object value = baseValues.get( iteratorKey );
		if( value instanceof Iterable<?> )
			return new BeanFillerWrappingIterable( iteratorKey, (Iterable<?>) value );

		throw new CastException( iteratorKey );
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final Map<String, Object> baseValues;
}
