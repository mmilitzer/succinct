/**
 * Copyright 2000-2017 Three Crickets LLC.
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
 * A wrapper that creates a child-parent link between two {@link Filler}
 * instances.
 * 
 * @author Tal Liron
 */
public class NestedFiller implements Filler
{
	//
	// Construction
	//

	public NestedFiller( Filler filler, Filler parentFiller )
	{
		this.filler = filler;
		this.parentFiller = parentFiller;
	}

	//
	// TemplateFiller
	//

	public Object getValue( String key ) throws CastException
	{
		try
		{
			return filler.getValue( key );
		}
		catch( CastException x )
		{
			return parentFiller.getValue( key );
		}
	}

	public Iterable<? extends Filler> getFillers( String iteratorKey ) throws CastException
	{
		try
		{
			return filler.getFillers( iteratorKey );
		}
		catch( CastException x )
		{
			return parentFiller.getFillers( iteratorKey );
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final Filler filler;

	private final Filler parentFiller;
}
