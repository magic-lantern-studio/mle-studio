/*
 * DwpVector3PropertyAttribute.java
 */

// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2000-2020 Wizzer Works
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.dwp.attribute;

//Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;


/**
 * This class implements an <code>Attribute</code> for a Magic Lantern Digital Workprint
 * item representing a <i>MlVector3</i> property.
 * <p>
 * A <i>MlVector3</i> property is a 1-dimensional array of <i>MlScalar</i>s and is represented
 * here as a three floating-point values.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class DwpVector3PropertyAttribute extends DwpPropertyAttribute
{
	/** DWP Property attribute type. */
	public static final String TYPE_DWP_VECTOR3PROPERTY = "com.wizzer.mle.studio.dwp.attribute.VECTOR3PROPERTY";

	/**
	 * A constructor that initializes the value of the DWP MlVector3 property Attribute.
	 * 
	 * @param name The name of the DWP MlVector3 property as a <code>String</code>.
	 * @param type The type for the DWP MlVector3 property as a <code>String</code>.
	 * @param value The value for the DWP MlVector3 property as a 1-dimensional array
	 * of <code>Float</code>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpVector3PropertyAttribute(String name, String type, Float[] value, boolean isReadOnly)
	{
		super(name,type,value,isReadOnly);
	}
	
	/**
	 * Generate the <code>Object</code> representation of the value from the
	 * specified <code>String</code>. The object will be a 1-dimensional array of
	 * <code>Float</code> extracted from the first three tokens in the <i>valueStr</i>
	 * argument.
	 * 
	 * @param valueStr The value represented as a <code>String</code>.
	 * 
	 * @return An <code>Object</code> is returned.
	 */
	protected Object loadValue(String valueStr)
	{
		String[] tokens = valueStr.split(" ");
		Float[] value = new Float[3];
		value[0] = new Float(tokens[0]);
		value[1] = new Float(tokens[1]);
		value[2] = new Float(tokens[2]);
		return value;
	}

	/**
	 * Represent the <code>DwpVector3PropertyAttribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String</code> is returned in the form
	 * <i>( Property name MlVector3 value0 value1 value2 )</i>.
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
		
		return str.toString();
	}

	/**
	 * Get a copy the contents of the <code>DwpVector3PropertyAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpVector3PropertyAttribute</code> is returned.
	 * The children of the <code>DwpVector3PropertyAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		DwpVector3PropertyAttribute attr = new DwpVector3PropertyAttribute(value.m_name,value.m_type,(Float[])value.m_value,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

	/**
	 * Get the <code>DwpVector3Attribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the vaule of the <code>DwpVector3Attribute</code>
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
		
		return str.toString();
	}
	
	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_VECTOR3PROPERTY</b> is always returned.
	 */
	public String getType()
	{
		return TYPE_DWP_VECTOR3PROPERTY;
	}
}
