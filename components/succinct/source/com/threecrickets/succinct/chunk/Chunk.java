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

package com.threecrickets.succinct.chunk;

import java.io.Serializable;
import java.util.Iterator;

import com.threecrickets.succinct.CastException;
import com.threecrickets.succinct.Syntax;
import com.threecrickets.succinct.Filler;
import com.threecrickets.succinct.filler.NestedFiller;

public abstract class Chunk implements Serializable
{
	//
	// Constants
	//

	public static final String USING_BEGIN = Syntax.get( "Using.begin" );

	public static final int USING_BEGIN_LENGTH = USING_BEGIN.length();

	public static final String USING_END = Syntax.get( "Using.end" );

	public static final int USING_END_LENGTH = USING_END.length();

	//
	// Static operations
	//

	public static String[] parseUsing( String string )
	{
		if( string != null )
		{
			if( string.startsWith( USING_BEGIN ) )
			{
				int usingEnd = string.indexOf( USING_END, USING_BEGIN_LENGTH );
				if( usingEnd != -1 )
				{
					return new String[]
					{
						string.substring( usingEnd + USING_END_LENGTH ), string.substring( USING_BEGIN_LENGTH, usingEnd )
					};
				}
			}
		}

		return new String[]
		{
			string, null
		};
	}

	//
	// Construction
	//

	public Chunk()
	{
		this( null, null );
	}

	public Chunk( String string, String using )
	{
		this.string = string;
		this.using = using;
	}

	//
	// Attributes
	//

	public final String string;

	public final String using;

	public String[] getRequiredTags()
	{
		return new String[0];
	}

	public Filler getUsing( Filler templateFiller )
	{
		if( using != null )
		{
			try
			{
				Iterable<? extends Filler> templateFillers = templateFiller.getFillers( using );
				if( templateFillers != null )
				{
					Iterator<? extends Filler> i = templateFillers.iterator();
					if( i.hasNext() )
						return new NestedFiller( i.next(), templateFiller );
				}
			}
			catch( CastException x )
			{
			}
		}

		return templateFiller;
	}

	//
	// Object
	//

	@Override
	public String toString()
	{
		if( string == null )
			return "";
		else
			return string;
	}

	// //////////////////////////////////////////////////////////////////////////
	// Private

	private static final long serialVersionUID = 1L;
}