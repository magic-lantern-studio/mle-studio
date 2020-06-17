/*
 * DwpStringPropertyAttribute.java
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
