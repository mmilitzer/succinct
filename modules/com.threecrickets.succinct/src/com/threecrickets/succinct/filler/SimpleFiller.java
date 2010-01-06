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

package com.threecrickets.succinct.filler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.threecrickets.succinct.CastException;
import com.threecrickets.succinct.Filler;

/**
 * A serializable {@link Filler} for which tags can be added programmatically.
 * Note that if a tag already exists, the {@link #setValue(String, Object)}
 * treats it as a nested cast instead.
 * 
 * @author Tal Liron
 */
public class SimpleFiller implements Filler
{
	//
	// Construction
	//

	public SimpleFiller()
	{
		this( new HashMap<String, Object>() );
	}

	public SimpleFiller( Map<String, ?> castMap )
	{
		this( castMap, new HashMap<String, Iterable<? extends Filler>>() );
	}

	public SimpleFiller( Map<String, ?> castMap, Map<String, Iterable<? extends Filler>> nestedCastMap )
	{
		this.values = castMap;
		this.nestedValues = nestedCastMap;
	}

	//
	// Attributes
	//

	@SuppressWarnings("unchecked")
	public void setValue( String key, Object value )
	{
		( (Map<String, Object>) values ).put( key, value );
	}

	public void setIterableValue( String iteratorKey, Iterable<? extends Filler> values )
	{
		nestedValues.put( iteratorKey, values );
	}

	public void setIterableValue( String iteratorKey, Filler filler )
	{
		if( filler != null )
			nestedValues.put( iteratorKey, Collections.singleton( filler ) );
		else
			nestedValues.put( iteratorKey, null );
	}

	@SuppressWarnings("unchecked")
	public void addIterableValue( String tag, Object value )
	{
		List<Filler> list = (List<Filler>) nestedValues.get( tag );
		if( list == null )
		{
			list = new ArrayList<Filler>();
			nestedValues.put( tag, list );
		}

		if( value instanceof Filler )
			list.add( (Filler) value );
		else
			list.add( new FillerValueWrapper( value ) );
	}

	//
	// TemplateFiller
	//

	public Object getValue( String key ) throws CastException
	{
		if( values == null )
			throw new CastException( key );
		if( !values.containsKey( key ) )
			throw new CastException( key );
		return values.get( key );
	}

	public Iterable<? extends Filler> getFillers( String iteratorKey ) throws CastException
	{
		if( nestedValues == null )
			throw new CastException( iteratorKey );
		if( !nestedValues.containsKey( iteratorKey ) )
			throw new CastException( iteratorKey );
		return nestedValues.get( iteratorKey );
	}

	// //////////iteratorKey/////////////////////////////////////////////////
	// Private

	public final Map<String, ?> values;

	public final Map<String, Iterable<? extends Filler>> nestedValues;
}
