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

package com.threecrickets.succinct.source;

import java.util.Map;

import com.threecrickets.succinct.TemplateSource;
import com.threecrickets.succinct.Filler;

public interface SuccinctSource extends TemplateSource, Filler
{
	public Map<String, String> getText();

	public boolean isRightToLeft();
}
