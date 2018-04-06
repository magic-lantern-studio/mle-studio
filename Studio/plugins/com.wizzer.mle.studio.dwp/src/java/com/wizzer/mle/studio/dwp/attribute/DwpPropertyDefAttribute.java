/*
 * DwpPropertyDefAttribute.java
 * Created on Jun 4, 2004
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
 * PropertyDef item.
 * 
 * @author Mark S. Millard
 */
public class DwpPropertyDefAttribute extends DwpNameTypeAttribute
{
	/**
	 * A constructor that initializes the value of the DWP PropertyDef Attribute.
	 * 
	 * @param name The name of the DWP Property Definition.
	 * @param type The type for the DWP Property Definition.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpPropertyDefAttribute(String name, String type, boolean isReadOnly)
	{
		super("PropertyDef", null, isReadOnly);
		
		// Create a helper object to hold the Set value.
		Object value = new DwpNameTypeAttributeValue(name,type);
		setValue(value);
		
		// Set the number of significant bits.
		this.setBits(getValue().length() * 16);
	}
	
	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_PROPERTYDEF</b> is always returned.
	 */
	public String getType()
	{
		return DwpItemAttribute.TYPE_DWP_PROPERTYDEF;
	}

	/**
	 * Get a copy the contents of the <code>DwpPropertyDefAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpPropertyDefAttribute</code> is returned.
	 * The children of the <code>DwpPropertyDefAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpNameTypeAttributeValue value = (DwpNameTypeAttributeValue)m_value;
		DwpPropertyDefAttribute attr = new DwpPropertyDefAttribute(value.m_name,value.m_type,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

}
