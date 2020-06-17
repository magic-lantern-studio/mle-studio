/*
 * DwpSetDefAttribute.java
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

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;


/**
 * This class implements an <code>Attribute</code> for a Magic Lantern Digital Workprint
 * SetDef item.
 * 
 * @author Mark S. Millard
 */
public class DwpSetDefAttribute extends DwpNameAttribute
{
	/**
	 * A constructor that initializes the value of the DWP SetDef Attribute.
	 * 
	 * @param name The name of the DWP Set Definition item.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpSetDefAttribute(String name, boolean isReadOnly)
	{
		super("SetDef", name, isReadOnly);
	}

	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_SETDEF</b> is always returned.
	 */
	public String getType()
	{
		return DwpItemAttribute.TYPE_DWP_SETDEF;
	}

	/**
	 * Get a copy the contents of the <code>DwpSetDefAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpSetDefAttribute</code> is returned.
	 * The children of the <code>DwpSetDefAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpSetDefAttribute attr = new DwpSetDefAttribute((String)m_value,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

}
