/*
 * DwpRoleAttachmentAttribute.java
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
 * Delegate/Role Attachment item.
 * 
 * @author Mark S. Millard
 */
public class DwpRoleAttachmentAttribute extends DwpNameTypeAttribute
{
	/**
	 * A constructor that initializes the value of the DWP Role Attachment Attribute.
	 * 
	 * @param parent The name of the parent DWP Actor.
	 * @param child The name for the child DWP Actor.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpRoleAttachmentAttribute(String parent, String child, boolean isReadOnly)
	{
		super("RoleAttachment", null, isReadOnly);
		
		// Create a helper object to hold the Set value.
		Object value = new DwpNameTypeAttributeValue(parent,child);
		setValue(value);
		
		// Set the number of significant bits.
		this.setBits(getValue().length() * 16);
	}
	
	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_ROLEATTACHMENT</b> is always returned.
	 */
	public String getType()
	{
		return DwpItemAttribute.TYPE_DWP_ROLEATTACHMENT;
	}

	/**
	 * Get a copy the contents of the <code>DwpRoleAttachmentAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpRoleAttachmentAttribute</code> is returned.
	 * The children of the <code>DwpRoleAttachmentAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpNameTypeAttributeValue value = (DwpNameTypeAttributeValue)m_value;
		DwpRoleAttachmentAttribute attr = new DwpRoleAttachmentAttribute(value.m_name,value.m_type,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

}
