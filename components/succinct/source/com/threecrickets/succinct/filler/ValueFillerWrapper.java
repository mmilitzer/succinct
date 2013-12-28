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

import com.threecrickets.succinct.CastException;
import com.threecrickets.succinct.Filler;

/**
 * This wrapper allows any object to support the {@link Filler} interface by
 * casting itself as the {@code value} tag. No other cast tag and no subcasts
 * are supported. Note that the object should support {@link Object#toString()}
 * in order for templates to make use of the value.
 * 
 * @author Tal Liron
 */
public class ValueFillerWrapper implements Filler
{
	//
	// Static operations
	//

	public static Filler newTemplateFillerValueWrapper( Object object, Filler parentFiller )
	{
		return new ValueFillerWrapper( object, parentFiller );
	}

	//
	// Construction
	//

	public ValueFillerWrapper( Object object )
	{
		this.object = object;
		this.parentFiller = null;
	}

	public ValueFillerWrapper( Object object, Filler parentFiller )
	{
		this.object = object;
		this.parentFiller = parentFiller;
	}

	//
	// TemplateFiller
	//

	public Object getValue( String key ) throws CastException
	{
		if( key.equals( VALUE ) )
			return object;
		else if( parentFiller != null )
			return parentFiller.getValue( key );
		else
			throw new CastException( key );
	}

	public Iterable<? extends Filler> getFillers( String iteratorKey ) throws CastException
	{
		if( parentFiller != null )
			return parentFiller.getFillers( iteratorKey );
		else
			throw new CastException( iteratorKey );
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static final String VALUE = "value";

	private final Object object;

	private final Filler parentFiller;
}
