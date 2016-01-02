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

package com.threecrickets.succinct.filter;

import com.threecrickets.succinct.Filter;

/**
 * A {@link Filter} that replaces line separators with paragraph breaks.
 * 
 * @author Tal Liron
 */
public class ParagraphFilter extends TokenFilter
{
	//
	// Construction
	//

	public ParagraphFilter()
	{
		super();
		addToken( "\r\n", "<p>" );
		addToken( "\n\r", "<p>" );
		addToken( "\r", "<p>" );
		addToken( "\n", "<p>" );
		addToken( "<p><p>", "<p>" );
		// addToken( "<br><br>", "<p>" );
		// addToken( "\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" );
		// addToken( "  ", "&nbsp;&nbsp;" );
	}

	//
	// Filter
	//

	@Override
	public String filter( String source )
	{
		StringBuilder work = new StringBuilder( source );
		replace( work );
		work.insert( 0, "<p>" );
		return work.toString();
	}
}