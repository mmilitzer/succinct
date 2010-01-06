/**
 * Copyright 2009-2010 Three Crickets LLC.
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
 * A base class for a {@link Filter} that replaces specific text with other
 * specific text.
 * 
 * @author Tal Liron
 */
public abstract class TokenFilter implements Filter
{
	//
	// Filter
	//

	public String filter( String source )
	{
		StringBuilder work = new StringBuilder( source );
		replace( work );
		return work.toString();
	}

	// //////////////////////////////////////////////////////////////////////////
	// Protected

	protected final Collection<Token> tokens = new ArrayList<Token>();

	protected void replace( StringBuilder work )
	{
		for( Token token : tokens )
			replace( token.token, token.value, work );
	}

	protected static class Token
	{
		Token( String token, String value )
		{
			this.token = token;
			this.value = value;
		}

		final String token;

		final String value;
	}

	protected void addToken( String token, String value )
	{
		tokens.add( new Token( token, value ) );
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static void replace( String string, String replacement, StringBuilder work )
	{
		int length = string.length();
		int replacementLength = replacement.length();
		for( int i = work.indexOf( string ); i != -1; i = work.indexOf( string, i ) )
		{
			work.replace( i, i + length, replacement );
			i += replacementLength;
		}
	}
}