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

import java.io.Serializable;
import java.util.Iterator;

import com.threecrickets.succinct.source.ClassLoaderTemplateSource;

/**
 * A template is a text containing special tags that are replaced with actual
 * text when it is "cast" with a {@link Filler}. Template fillers can also case
 * groups of values, by providing iterations of fillings for processing block
 * tags. Note that templates are parsed upon initialization, so that later
 * casting will be as quick as possible.
 * <p>
 * Templates can be loaded from objects that implement {@link TemplateSource} .
 * By default, {@link ClassLoaderTemplateSource} is used.
 * 
 * @author Tal Liron
 * @see RichTemplate
 */
public interface Template extends Serializable
{
	public void setExternalHandler( ExternalHandler externalHandler );

	public ExternalHandler getExternalHandler();

	public void setFormatter( Formatter formatter );

	public Formatter getFormatter();

	public void cast( Filler filler, Appendable out ) throws CastException;

	public void softCast( Filler filler, Appendable out );

	public Iterator<Object> streamCast( Filler filler );

	public void transform( Filler filler ) throws ParseException, TemplateSourceException;

	public String[] getRequiredTags();
}
