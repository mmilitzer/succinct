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

package com.threecrickets.succinct;

/**
 * A filling supplies the text to replace tags when a {@link Template} is
 * "cast". The {@link #getValue(String)} method can return any object that
 * supports {@link Object#toString()} or a {@link CompositeValue}, which is
 * handled specially by {@link Template}. Note that these methods should
 * <i>never</i> return a null! Always throw an exception if a tag is unknown or
 * if there is an error.
 * 
 * @author Tal Liron
 */
public interface Filler
{
	public Object getValue( String key ) throws CastException;

	public Iterable<? extends Filler> getFillers( String iteratorKey ) throws CastException;
}
