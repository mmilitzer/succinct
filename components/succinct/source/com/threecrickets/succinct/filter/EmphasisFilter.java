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

package com.threecrickets.succinct.filter;

/**
 * A {@link com.threecrickets.succinct.Filter} that wraps certain words in HTML
 * bold tags.
 * 
 * @author Tal Liron
 */
public class EmphasisFilter extends TokenFilter
{
	//
	// Construction
	//

	public EmphasisFilter()
	{
		super();
		addPhrase( "Shreds and Patches" );
	}

	// //////////////////////////////////////////////////////////////////////////
	// Protected

	protected void addPhrase( String phrase )
	{
		addToken( phrase, "<b>" + phrase + "</b>" );
	}
}