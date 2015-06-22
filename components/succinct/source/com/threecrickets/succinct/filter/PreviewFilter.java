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

package com.threecrickets.succinct.filter;

import com.threecrickets.succinct.Filter;

/**
 * A {@link Filter} that shows only the first line of text.
 * 
 * @author Tal Liron
 */
public class PreviewFilter implements Filter
{
	//
	// Construction
	//

	public PreviewFilter( int cutoff )
	{
		this.cutoff = cutoff;
	}

	//
	// Attributes
	//

	public boolean isTooLong( String source )
	{
		return source.length() > cutoff;
	}

	//
	// Filter
	//

	public String filter( String source )
	{
		source = breakFilter.filter( source );
		if( isTooLong( source ) )
			source = source.substring( 0, cutoff ) + CUTOFF_ELLIPSIS;
		return source;
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static final String CUTOFF_ELLIPSIS = " ...";

	private static final BreakFilter breakFilter = new BreakFilter();

	private final int cutoff;
}