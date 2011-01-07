/**
 * Copyright 2009-2011 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.opensource.org/licenses/lgpl-3.0.html
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
 * A {@link Filter} that identifies hyperlinks and wraps them in HTML anchor
 * tags.
 * 
 * @author Tal Liron
 */
public class HyperlinkFilter implements Filter
{
	//
	// Construction
	//

	public HyperlinkFilter()
	{
		description = "link";

		prefixes.add( "http://" );
		prefixes.add( "https://" );
		prefixes.add( "www." );
		prefixes.add( "ftp://" );
		prefixes.add( "mailto:" );
		prefixes.add( "news:" );
	}

	//
	// Filter
	//

	public String filter( String source )
	{
		StringBuilder work = new StringBuilder( source );
		for( String prefix : prefixes )
		{
			convert( prefix, work );
		}
		return work.toString();
	}

	// //////////////////////////////////////////////////////////////////////////
	// Protected

	protected static final String TAG_START1 = "[<a title=\"Link\" href=\"";

	protected static final String TAG_START2 = "\">";

	protected static final String TAG_END = "</a>]";

	protected static final String DEFAULT_PREFIX = "http://";

	protected static final int EXTRA_LENGTH = TAG_START1.length() + TAG_START2.length() + TAG_END.length();

	protected static final String LEGAL_CHARS = "_-!.~\'()*,;:$&+=?/[]@%";

	protected final Collection<String> prefixes = new ArrayList<String>();

	protected final String description;

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private void convert( String prefix, StringBuilder work )
	{
		int prefixLength = prefix.length();
		int hyperlinkLength, hyperlinkLength2, descriptionLength, start;
		String hyperlink, description, before;
		for( int i = work.indexOf( prefix ); i != -1; i = work.indexOf( prefix, i + 1 ) )
		{
			// Ignore link if we are inside an href contstruction
			start = i;
			if( prefix.indexOf( ":" ) == -1 )
			{
				start -= DEFAULT_PREFIX.length();
			}
			if( start >= 5 )
			{
				before = work.substring( start - 5, start ).toLowerCase();
				if( isHref( before ) )
					continue;
			}
			if( start >= 6 )
			{
				before = work.substring( start - 6, start ).toLowerCase();
				if( isHref( before ) )
					continue;
			}

			hyperlink = work.substring( i, endOfHyperlink( work, i + prefixLength + 1 ) );
			hyperlinkLength = hyperlink.length();

			if( this.description != null )
			{
				description = this.description;
				descriptionLength = description.length();
			}
			else
			{
				description = hyperlink;
				descriptionLength = hyperlinkLength;
			}

			if( prefix.indexOf( ":" ) == -1 )
			{
				hyperlink = DEFAULT_PREFIX + hyperlink;
				hyperlinkLength2 = hyperlink.length();
			}
			else
			{
				hyperlinkLength2 = hyperlinkLength;
			}

			work.replace( i, i + hyperlinkLength, TAG_START1 + hyperlink + TAG_START2 + description + TAG_END );
			i += hyperlinkLength2 + descriptionLength + EXTRA_LENGTH;
		}
	}

	private static int endOfHyperlink( StringBuilder work, int i )
	{
		int length = work.length();
		for( ; i < length; i++ )
		{
			if( !isLegal( work.charAt( i ) ) )
			{
				return i;
			}
		}
		return i;
	}

	private static boolean isLegal( char c )
	{
		return ( ( c >= 'A' ) && ( c <= 'Z' ) ) || // alpha
			( ( c >= 'a' ) && ( c <= 'z' ) ) || // alpha
			( ( c >= '0' ) && ( c <= '9' ) ) || // digit
			( LEGAL_CHARS.indexOf( c ) != -1 ) || // unreserved
			( ( c > 127 ) && !Character.isISOControl( c ) && !Character.isSpaceChar( c ) ); // other
	}

	private static boolean isHref( String string )
	{
		return string.equals( "href=" ) || string.equals( "href=\"" ) || string.equals( "href='" );
	}
}