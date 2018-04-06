/*
 * DwpScalarPropertyAttribute.java
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
 * item representing a <i>MlScalar</i> property.
 * <p>
 * A <i>MlScalar</i> property is either a fixed or floating-point real number. It is
 * represeneted here as a floating-point value.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class DwpScalarPropertyAttribute extends DwpPropertyAttribute
{
	/**
	 * A constructor that initializes the value of the DWP MlScalar property Attribute.
	 * 
	 * @param name The name of the DWP MlScalar property as a <code>String</code>.
	 * @param type The type for the DWP MlScalar property as a <code>String</code>.
	 * @param value The value for the DWP MlScalar property as a <code>Float</code>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpScalarPropertyAttribute(String name, String type, Float value, boolean isReadOnly)
	{
		super(name,type,value,isReadOnly);
	}
	
	/**
	 * Generate the <code>Object</code> representation of the value from the
	 * specified <code>String</code>. The object will be a <code>Float</code>
	 * extracted from the first token in the <i>valueStr</i>
	 * argument.
	 * 
	 * @param valueStr The value represented as a <code>String</code>.
	 * 
	 * @return An <code>Object</code> is returned.
	 */
	protected Object loadValue(String valueStr)
	{
		String[] tokens = valueStr.split(" ");
		Float value = new Float(tokens[0]);
		return value;
	}

	/**
	 * Get a copy the contents of the <code>DwpScalarPropertyAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpScalarPropertyAttribute</code> is returned.
	 * The children of the <code>DwpScalarPropertyAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		DwpScalarPropertyAttribute attr = new DwpScalarPropertyAttribute(value.m_name,value.m_type,(Float)value.m_value,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

}
