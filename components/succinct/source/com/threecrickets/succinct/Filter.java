/**
 * Copyright 2000-2017 Three Crickets LLC.
 * <p>
 * The contents of this file are subject to the terms of the LGPL version 3.0:
 * http://www.gnu.org/copyleft/lesser.html
 * <p>
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly from Three Crickets
 * at http://threecrickets.com
 */

package com.threecrickets.succinct;

/**
 * A filter performs manipulation on text.
 * 
 * @author Tal Liron
 */
public interface Filter
{
	public String filter( String source );
}