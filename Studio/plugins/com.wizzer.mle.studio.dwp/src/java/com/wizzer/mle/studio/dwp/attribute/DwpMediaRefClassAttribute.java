/*
 * DwpMediaRefClassAttribute.java
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
 * MediaRefClass item.
 * 
 * @author Mark S. Millard
 */
public class DwpMediaRefClassAttribute extends DwpNameTypeAttribute
{
	/**
	 * A constructor that initializes the value of the DWP MediaRefClass Attribute.
	 * 
	 * @param name The name of the DWP MediaRefClass.
	 * @param clazz The class for the DWP MediaRef.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpMediaRefClassAttribute(String name, String clazz, boolean isReadOnly)
	{
		super("MediaRefClass", null, isReadOnly);
		
		// Create a helper object to hold the Set value.
		Object value = new DwpNameTypeAttributeValue(name,clazz);
		setValue(value);
		
		// Set the number of significant bits.
		this.setBits(getValue().length() * 16);
	}
	
	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_MEDIAREFCLASS</b> is always returned.
	 */
	public String getType()
	{
		return DwpItemAttribute.TYPE_DWP_MEDIAREFCLASS;
	}

	/**
	 * Get a copy the contents of the <code>DwpMediaRefClassAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpMediaRefClassAttribute</code> is returned.
	 * The children of the <code>DwpMediaRefClassAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpNameTypeAttributeValue value = (DwpNameTypeAttributeValue)m_value;
		DwpMediaRefClassAttribute attr = new DwpMediaRefClassAttribute(value.m_name,value.m_type,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

}
