/*
 * DwpRotationPropertyAttribute.java
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
 * item representing a <i>MlRotation</i> property.
 * <p>
 * A <i>MlRotation</i> property is a 4-dimensional array of <i>MlScalar</i>s and is represented
 * here as a 4-dimensional array of floating-point values.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class DwpRotationPropertyAttribute extends DwpPropertyAttribute
{
	/** DWP Property attribute type. */
	public static final String TYPE_DWP_ROTATIONPROPERTY = "com.wizzer.mle.studio.dwp.attribute.ROTATIONPROPERTY";

	/**
	 * A constructor that initializes the value of the DWP MlRotation property Attribute.
	 * 
	 * @param name The name of the DWP MlRotation property as a <code>String</code>.
	 * @param type The type for the DWP MlRotation property as a <code>String</code>.
	 * @param value The value for the DWP MlRotation property as a 4-dimensional array
	 * of <code>Float</code>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpRotationPropertyAttribute(String name, String type, Float[] value, boolean isReadOnly)
	{
		super(name,type,value,isReadOnly);
	}
	
	/**
	 * Generate the <code>Object</code> representation of the value from the
	 * specified <code>String</code>. The object will be a 4-dimenstional array of
	 * <code>Float</code> extracted from the first and second tokens in the <i>valueStr</i>
	 * argument.
	 * 
	 * @param valueStr The value represented as a <code>String</code>.
	 * 
	 * @return An <code>Object</code> is returned.
	 */
	protected Object loadValue(String valueStr)
	{
		String[] tokens = valueStr.split(" ");
		Float[] value = new Float[4];
		value[0] = new Float(tokens[0]);
		value[1] = new Float(tokens[1]);
		value[2] = new Float(tokens[2]);
		value[3] = new Float(tokens[3]);
		return value;
	}

	/**
	 * Represent the <code>DwpRotationPropertyAttribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String</code> is returned in the form
	 * <i>( Property name MlRotation value0 value1 value2 value4 )</i>.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		StringBuffer str = new StringBuffer(getName());
		str.append(" ");
		String tagString = getTagsString();
		if ((tagString != null) && (! tagString.equals("")))
		{
		    str.append(tagString);
		    str.append(" ");
		}
		str.append(value.m_name);
		str.append(" ");
		str.append(value.m_type);
		str.append(" ");
		Float[] floatValue = (Float[])value.m_value;
		str.append(floatValue[0]);
		str.append(" ");
		str.append(floatValue[1]);
		str.append(" ");
		str.append(floatValue[2]);
		str.append(" ");
		str.append(floatValue[3]);
		
		return str.toString();
	}

	/**
	 * Get a copy the contents of the <code>DwpRotationPropertyAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpRotationPropertyAttribute</code> is returned.
	 * The children of the <code>DwpRotationPropertyAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		DwpRotationPropertyAttribute attr = new DwpRotationPropertyAttribute(value.m_name,value.m_type,(Float[])value.m_value,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

	/**
	 * Get the <code>DwpRotationAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the vaule of the <code>DwpRotationAttribute</code>
	 * is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getStringValue()
	 */
	public String getStringValue()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		StringBuffer str = new StringBuffer(value.m_name);
		str.append(" ");
		str.append(value.m_type);
		str.append(" ");
		Float[] floatValue = (Float[])value.m_value;
		str.append(floatValue[0]);
		str.append(" ");
		str.append(floatValue[1]);
		str.append(" ");
		str.append(floatValue[2]);
		str.append(" ");
		str.append(floatValue[3]);
		
		return str.toString();
	}
	
	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_ROTATIONPROPERTY</b> is always returned.
	 */
	public String getType()
	{
		return TYPE_DWP_ROTATIONPROPERTY;
	}

}
