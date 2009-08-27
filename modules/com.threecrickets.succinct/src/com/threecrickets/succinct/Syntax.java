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
