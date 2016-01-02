/**
 * Copyright 2009-2016 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.gnu.org/copyleft/lesser.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct.filler;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import com.threecrickets.succinct.CastException;
import com.threecrickets.succinct.Filler;

/**
 * A {@link Filler} that supports getting values from a plain-old Java object
 * (POJO) via the Java bean property mechanism.
 * <p>
 * The dot convention is used to recursively delve into properties. For example,
 * the key {@code obj1.prop1.prop2.prop3} would be identical to the Java call
 * {@code obj.getProp1().getProp2().getProp3()}.
 * 
 * @author Tal Liron
 */
public class BeanFillerWrapper implements Filler
{
	//
	// Construction
	//

	public BeanFillerWrapper( String keyPrefix, Object bean ) throws IntrospectionException
	{
		this( keyPrefix, bean, Introspector.getBeanInfo( bean.getClass() ) );
	}

	public BeanFillerWrapper( String keyPrefix, Object bean, BeanInfo beanInfo )
	{
		this.keyPrefix = keyPrefix + ".";
		this.bean = bean;
		this.beanInfo = beanInfo;
	}

	//
	// Filler
	//

	public Object getValue( String key ) throws CastException
	{
		if( key.startsWith( keyPrefix ) )
		{
			String subKey = key.substring( keyPrefix.length() );
			String[] split = subKey.split( "\\." );
			if( split.length > 0 )
			{
				String ourKey = split[0];
				for( PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors() )
				{
					if( propertyDescriptor.getName().equals( ourKey ) )
					{
						try
						{
							Object value = propertyDescriptor.getReadMethod().invoke( bean );
							if( split.length == 1 )
								// It's us!
								return value;
							else
								// Delegate to property
								return new BeanFillerWrapper( keyPrefix + ourKey, value ).getValue( key );
						}
						catch( IllegalArgumentException x )
						{
							throw new CastException( x, key );
						}
						catch( IllegalAccessException x )
						{
							throw new CastException( x, key );
						}
						catch( InvocationTargetException x )
						{
							throw new CastException( x, key );
						}
						catch( IntrospectionException x )
						{
							throw new CastException( x, key );
						}
					}
				}
			}
		}

		throw new CastException( key );
	}

	public Iterable<? extends Filler> getFillers( String iteratorKey ) throws CastException
	{
		if( iteratorKey.startsWith( keyPrefix ) )
		{
			String subKey = iteratorKey.substring( keyPrefix.length() );
			for( PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors() )
			{
				if( propertyDescriptor.getName().equals( subKey ) )
				{
					try
					{
						Object subBean = propertyDescriptor.getReadMethod().invoke( bean );
						if( subBean instanceof Iterable<?> )
							return new BeanFillerWrappingIterable( subKey, (Iterable<?>) bean );
					}
					catch( IllegalArgumentException x )
					{
						throw new CastException( x, iteratorKey );
					}
					catch( IllegalAccessException x )
					{
						throw new CastException( x, iteratorKey );
					}
					catch( InvocationTargetException x )
					{
						throw new CastException( x, iteratorKey );
					}
				}
			}
		}

		throw new CastException( iteratorKey );
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final String keyPrefix;

	private final Object bean;

	private final BeanInfo beanInfo;
}
