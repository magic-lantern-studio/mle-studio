/*
 * DwpMediaRefSourceAttribute.java
 * Created on Jul 8, 2004
 */

// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2007  Wizzer Works
//
//  Wizzer Works makes available all content in this file ("Content").
//  Unless otherwise indicated below, the Content is provided to you
//  under the terms and conditions of the Common Public License Version 1.0
//  ("CPL"). A copy of the CPL is available at
//
//      http://opensource.org/licenses/cpl1.0.php
//
//  For purposes of the CPL, "Program" will mean the Content.
//
//  For information concerning this Makefile, contact Mark S. Millard,
//  of Wizzer Works at msm@wizzerworks.com.
//
//  More information concerning Wizzer Works may be found at
//
//      http://www.wizzerworks.com
//
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.dwp.attribute;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;

/**
 * This class implements an <code>Attribute</code> for a Magic Lantern Digital Workprint
 * MediaRefSource item.
 *
 * @author Mark S. Millard
 */
public class DwpMediaRefSourceAttribute extends DwpNameAttribute
{
	/**
	 * A constructor that initializes the value of the DWP MediaRefSource Attribute.
	 * 
	 * @param flags The flags of the DWP MediaRef Target.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpMediaRefSourceAttribute(String flags, boolean isReadOnly)
	{
		super("MediaRefSource", flags, isReadOnly);
	}

	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_MEDIAREFSOURCE</b> is always returned.
	 */
	public String getType()
	{
		return DwpItemAttribute.TYPE_DWP_MEDIAREFSOURCE;
	}

	/**
	 * Get a copy the contents of the <code>DwpMediaRefSourceAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpMediaRefSourceAttribute</code> is returned.
	 * The children of the <code>DwpMediaRefSourceAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpMediaRefSourceAttribute attr = new DwpMediaRefSourceAttribute((String)m_value,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

}
