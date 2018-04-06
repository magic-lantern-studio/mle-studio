/*
 * DwpStringPropertyAttribute.java
 * Created on Jul 7, 2004
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
 * item representing a <i>string</i> property.
 * 
 * @author Mark S. Millard
 */
public class DwpStringPropertyAttribute extends DwpPropertyAttribute
{
	/**
	 * A constructor that initializes the value of the DWP string property Attribute.
	 * 
	 * @param name The name of the DWP string property as a <code>String</code>.
	 * @param type The type for the DWP string property as a <code>String</code>.
	 * @param value The value for the DWP string property as an <code>String</code>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpStringPropertyAttribute(String name, String type, String value, boolean isReadOnly)
	{
		super(name,type,value,isReadOnly);

		// Add back the quotation marks if necessary. The quotation marks will be
		// added if there is a space in the value argument. Ignore if the string is already quoted.
		if ((value.indexOf(' ') > -1) && (! value.startsWith("\"")))
		{
			DwpNameTypeValueAttributeValue attrValue = (DwpNameTypeValueAttributeValue) m_value;
			attrValue.m_value = loadValue(value);
			m_value = attrValue;
		}
	}
	
	/**
	 * Generate the <code>Object</code> representation of the value from the
	 * specified <code>String</code>. The object will be a <code>String</code>
	 * extracted from the <i>valueStr</i> argument. The string will be quoted
	 * if necessary.
	 * 
	 * @param valueStr The value represented as a <code>String</code>.
	 * 
	 * @return An <code>Object</code> is returned.
	 */
	protected Object loadValue(String valueStr)
	{
		String value = new String("\"" + valueStr + "\"");
		return value;
	}

	/**
	 * Get a copy the contents of the <code>DwpStringPropertyAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpStringPropertyAttribute</code> is returned.
	 * The children of the <code>DwpStringPropertyAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		DwpStringPropertyAttribute attr = new DwpStringPropertyAttribute(value.m_name,value.m_type,(String)value.m_value,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

	
}
