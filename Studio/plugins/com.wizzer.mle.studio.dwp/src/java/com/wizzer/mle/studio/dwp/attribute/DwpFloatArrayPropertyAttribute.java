/*
 * DwpFloatArrayPropertyAttribute.java
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

// Import standard Java classes.
import java.util.Vector;

//Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;


/**
 * This class implements an <code>Attribute</code> for a Magic Lantern Digital Workprint
 * item representing an <i>FloatArray</i> property.
 * <p>
 * A <i>floatArray</i> property is a multi-dimensional array of <i>float</i>s and is represented
 * here as a multi-dimensional array of floating-point values.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class DwpFloatArrayPropertyAttribute extends DwpPropertyAttribute
{
	/** DWP Property attribute type. */
	public static final String TYPE_DWP_FLOATARRAYPROPERTY = "com.wizzer.mle.studio.dwp.attribute.FLOATARRAYPROPERTY";

	/**
	 * A constructor that initializes the value of the DWP FloatArray property Attribute.
	 * 
	 * @param name The name of the DWP FloatArray property as a <code>String</code>.
	 * @param type The type for the DWP FloatArray property as a <code>String</code>.
	 * @param value The value for the DWP FloatArray property as a multi-dimensional array
	 * of <code>Float</code>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpFloatArrayPropertyAttribute(String name, String type, Float[] value, boolean isReadOnly)
	{
		super(name,type,value,isReadOnly);
	}
	
	/**
	 * Generate the <code>Object</code> representation of the value from the
	 * specified <code>String</code>. The object will be a multi-dimenstional array of
	 * <code>Float</code> extracted from the space-delimitted tokens in the <i>valueStr</i>
	 * argument.
	 * 
	 * @param valueStr The value represented as a <code>String</code>.
	 * 
	 * @return An <code>Object</code> is returned.
	 */
	protected Object loadValue(String valueStr)
	{
		Vector values = new Vector();
	    String[] tokens = valueStr.split(" ");
		for (int i = 0; i < tokens.length; i++)
		{
		    if ((! tokens[i].equals("[")) &&
		        (! tokens[i].equals("]")) &&
		        (! tokens[i].equals(",")))
		        values.add(tokens[i]);
		}
		
		Float[] value = new Float[values.size()];
		for (int i = 0; i < values.size(); i++)
		{
		    String floatValue = (String)values.elementAt(i);
		    floatValue = floatValue.trim();
			value[i] = new Float(floatValue);
		}
		return value;
	}

	/**
	 * Represent the <code>DwpFloatArrayPropertyAttribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String</code> is returned in the form
	 * <i>( Property name FloatArray [ val1, val2, ..., valn ] )</i>.
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
		str.append(" [");
		Float[] floatValue = (Float[])value.m_value;
		for (int i = 0; i < floatValue.length; i++)
		{
			str.append(" ");
			str.append(floatValue[i]);
			if (i != (floatValue.length - 1))
				str.append(" ,");
		}
		str.append(" ]");
		
		return str.toString();
	}

	/**
	 * Get a copy the contents of the <code>DwpFloatArrayPropertyAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpFloatArrayPropertyAttribute</code> is returned.
	 * The children of the <code>DwpFloatArrayPropertyAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		DwpFloatArrayPropertyAttribute attr = new DwpFloatArrayPropertyAttribute(value.m_name,value.m_type,(Float[])value.m_value,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

	/**
	 * Get the <code>DwpFloatArrayAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the vaule of the <code>DwpFloatArrayAttribute</code>
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
		str.append(" [");
		Float[] floatValue = (Float[])value.m_value;
		for (int i = 0; i < floatValue.length; i++)
		{
			str.append(" ");
			str.append(floatValue[i]);
			if (i != (floatValue.length - 1))
				str.append(" ,");
		}
		str.append(" ]");
		
		return str.toString();
	}
	
	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_FLOATARRAYPROPERTY</b> is always returned.
	 */
	public String getType()
	{
		return TYPE_DWP_FLOATARRAYPROPERTY;
	}

}
