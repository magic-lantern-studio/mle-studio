/*
 * DwpPackageAttribute.java
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
 * Package item.
 * 
 * @author Mark S. Millard
 */
public class DwpPackageAttribute extends DwpNameAttribute
{
	/**
	 * A constructor that initializes the value of the DWP Package Attribute.
	 * 
	 * @param name The name of the DWP package.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpPackageAttribute(String name, boolean isReadOnly)
	{
		super("Package", name, isReadOnly);
	}

	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_PACKAGE</b> is always returned.
	 */
	public String getType()
	{
		return DwpItemAttribute.TYPE_DWP_PACKAGE;
	}

	/**
	 * Get a copy the contents of the <code>DwpPackageAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpPackageAttribute</code> is returned.
	 * The children of the <code>DwpPackageAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpPackageAttribute attr = new DwpPackageAttribute((String)m_value,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

}
