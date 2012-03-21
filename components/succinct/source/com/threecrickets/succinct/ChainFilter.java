/**
 * Copyright 2009-2012 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.gnu.org/copyleft/lesser.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct;

import java.util.ArrayList;
import java.util.Collection;

public class ChainFilter implements Filter
{
	//
	// Construction
	//

	public ChainFilter()
	{
	}

	public ChainFilter( Filter... filters )
	{
		for( Filter filter : filters )
			this.filters.add( filter );
	}

	//
	// Operations
	//

	public void addFilter( Filter filter )
	{
		filters.add( filter );
	}

	//
	// Filter
	//

	public String filter( String source )
	{
		for( Filter filter : filters )
			source = filter.filter( source );
		return source;
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final Collection<Filter> filters = new ArrayList<Filter>();
}
