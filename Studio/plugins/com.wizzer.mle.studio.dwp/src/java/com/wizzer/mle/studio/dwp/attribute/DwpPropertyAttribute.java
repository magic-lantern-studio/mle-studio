/*
 * DwpPropertyAttribute.java
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
