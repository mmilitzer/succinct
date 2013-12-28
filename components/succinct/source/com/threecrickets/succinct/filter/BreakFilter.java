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

package com.threecrickets.succinct.filter;

import com.threecrickets.succinct.Filter;

/**
 * A {@link Filter} that removes all line breaks.
 * 
 * @author Tal Liron
 */
public class BreakFilter extends TokenFilter
{
	//
	// Construction
	//

	public BreakFilter()
	{
		super();
		addToken( "\r", " " );
		addToken( "\n", " " );
		addToken( "<p>", " " );
		addToken( "</p>", " " );
		addToken( "<P>", " " );
		addToken( "</P>", " " );
		addToken( "<br>", " " );
		addToken( "<BR>", " " );
		addToken( "<br/>", " " );
		addToken( "<BR/>", " " );
	}
}