/*
 * DwpPropertyAttribute.java
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

/**
 * This class is an abstract class, partially implementing an <code>Attribute</code>
 * for a Magic Lantern Digital Workprint Property item.
 * <p>
 * All Digital Workprint Property items represented as domain table Attributes are
 * subclassed from <code>DwpPropertyAttribute</code>.
 * </p>
 * 
 * @author Mark S. Millard
 */
public abstract class DwpPropertyAttribute extends DwpNameTypeValueAttribute
{
	/**
	 * A constructor that initializes the value of the DWP Property Attribute.
	 * 
	 * @param name The name of the DWP Property as a <code>String</code>.
	 * @param type The type for the DWP Property as a <code>String</code>.
	 * @param value The value for the DWP Property as an <code>Object</code>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpPropertyAttribute(String name, String type, Object value, boolean isReadOnly)
	{
		super("Property", null, isReadOnly);
		
		// Create a helper object to hold the Property value.
		Object attrValue = new DwpNameTypeValueAttributeValue(name,type,value);
		setValue(attrValue);
		
		// Set the number of significant bits.
		this.setBits(getValue().length() * 16);
	}
	
	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_PROPERTY</b> is always returned.
	 */
	public String getType()
	{
		return DwpItemAttribute.TYPE_DWP_PROPERTY;
	}

}
