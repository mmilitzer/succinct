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

package com.threecrickets.succinct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Iterator;

public class TemplateUtil
{
	public static String cast( Template template, Filler filling ) throws CastException
	{
		StringBuilder buffer = new StringBuilder();
		template.cast( filling, buffer );
		return buffer.toString();
	}

	public static String softCast( Template template, Filler filling )
	{
		StringBuilder buffer = new StringBuilder();
		template.softCast( filling, buffer );
		return buffer.toString();
	}

	public static int count( Iterable<?> i )
	{
		int count = 0;
		Iterator<?> ii = i.iterator();
		while( ii.hasNext() )
		{
			ii.next();
			count++;
		}
		return count;
	}

	public static String removeWhiteSpace( String string )
	{
		return string.replaceAll( "[\\n\\r\\t]", "" );
	}

	public static String getString( InputStream input ) throws IOException
	{
		BufferedReader reader = new BufferedReader( new InputStreamReader( input ), BUFFER_SIZE );
		String string = getString( reader );
		input.close();
		return string;
	}

	public static String getString( Reader reader ) throws IOException
	{
		StringWriter writer = new StringWriter();
		int c;
		while( true )
		{
			c = reader.read();
			if( c == -1 )
				break;

			writer.write( c );
		}

		return writer.toString();
	}

	public static String getString( File file ) throws IOException
	{
		BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( file ) ), BUFFER_SIZE );
		return getString( reader );
	}

	private static final int BUFFER_SIZE = 1024 * 1024;
}
