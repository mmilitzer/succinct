/**
 * Copyright 2009 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://www.threecrickets.com/
 */

package com.threecrickets.succinct.filter;

import com.threecrickets.succinct.Filter;

/**
 * A {@link com.threecrickets.succinct.Filter} that adds "in bed" to everything.
 * Hilarious!
 * 
 * @author Tal Liron
 */
public class InBedFilter implements Filter
{
	//
	// Filter
	//

	public String filter( String source )
	{
		return source + " in bed";
	}
}