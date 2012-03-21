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

import java.util.ArrayList;
import java.util.Collection;

import com.threecrickets.succinct.Filter;

/**
 * A {@link Filter} that filters out all HTML tags except a specific (safe) set.
 * 
 * @author Tal Liron
 */
public class HTMLFilter implements Filter
{
	//
	// Construction
	//

	public HTMLFilter( boolean allowSome )
	{
		if( allowSome )
		{
			allowed.add( "b" );
			allowed.add( "i" );
			allowed.add( "u" );
			allowed.add( "a" );
		}
	}

	//
	// Filter
	//

	public String filter( String source )
	{
		StringBuilder work = new StringBuilder( source );
		int tagEnd, start, end;
		String tag;
		for( int i = work.indexOf( TAG_START ); i != -1; i = work.indexOf( TAG_START, i + 1 ) )
		{
			tagEnd = work.indexOf( TAG_END, i );
			if( tagEnd != -1 )
			{
				end = work.indexOf( " ", i );
				if( ( end == -1 ) || ( end > tagEnd ) )
				{
					end = tagEnd;
				}
				start = i + ( work.charAt( i + 1 ) == '/' ? 2 : 1 );
				tag = work.substring( start, end ).trim().toLowerCase();
				if( isAllowed( tag ) )
				{
					i = tagEnd;
				}
				else
				{
					work.replace( i, tagEnd + 1, "" );
					i--;
				}
			}
		}
		return work.toString();
	}

	// //////////////////////////////////////////////////////////////////////////
	// Protected

	protected static final String TAG_START = "<";

	protected static final String TAG_END = ">";

	protected final Collection<String> allowed = new ArrayList<String>();

	protected boolean isAllowed( String tag )
	{
		return allowed.contains( tag );
	}
}