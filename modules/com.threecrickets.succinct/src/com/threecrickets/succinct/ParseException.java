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

package com.threecrickets.succinct;

public class ParseException extends Exception
{
	//
	// Construction
	//

	public ParseException( String... keys )
	{
		super();
		this.keys = keys;
	}

	public ParseException( Throwable cause, String... keys )
	{
		super( cause );
		this.keys = keys;
	}

	//
	// Exception
	//

	@Override
	public String getMessage()
	{
		StringBuilder message = new StringBuilder( "Could not cast keys: " );
		for( int i = 0; i < keys.length; i++ )
		{
			message.append( keys[i] );
			if( i < keys.length - 1 )
				message.append( ", " );
		}
		return message.toString();
	}

	//
	// Serializable
	//

	public static final long serialVersionUID = 1;

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private final String[] keys;
}