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

package com.threecrickets.succinct;

import java.io.IOException;
import java.util.Properties;

public class Syntax
{
	public static String get( String name )
	{
		if( properties == null )
		{
			properties = new Properties();
			try
			{
				properties.load( ClassLoader.getSystemResourceAsStream( "META-INF/com.threecrickets.succinct.Syntax" ) );
			}
			catch( IOException x )
			{
				x.printStackTrace();
			}
		}

		return properties.getProperty( name );
	}

	private static Properties properties;
}
