/*
 * DwpBootAttribute.java
 * Created on Jul 16, 2004
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

//Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;


/**
 * This class implements an <code>Attribute</code> for a Magic Lantern Digital Workprint
 * Boot item.
 * 
 * @author Mark S. Millard
 */
public class DwpBootAttribute extends DwpNameAttribute
{
	/**
	 * A constructor that initializes the value of the DWP Boot Attribute.
	 * 
	 * @param name The name of the DWP Boot Group.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpBootAttribute(String name, boolean isReadOnly)
	{
		super("Boot", name, isReadOnly);
	}

	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_BOOT</b> is always returned.
	 */
	public String getType()
	{
		return DwpItemAttribute.TYPE_DWP_BOOT;
	}

	/**
	 * Get a copy the contents of the <code>DwpBootAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpBootAttribute</code> is returned.
	 * The children of the <code>DwpBootAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpBootAttribute attr = new DwpBootAttribute((String)m_value,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

}
