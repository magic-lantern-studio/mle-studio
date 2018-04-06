/*
 * DwpRoleSetMappingAttribute.java
 * Created on Jun 4, 2004
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
 * Delegate/Role and Forum/Set Mapping item.
 * 
 * @author Mark S. Millard
 */
public class DwpRoleSetMappingAttribute extends DwpNameTypeAttribute
{
	/**
	 * A constructor that initializes the value of the DWP RoleSetMapping Attribute.
	 * 
	 * @param role The Role's class name.
	 * @param set The name for the Set.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpRoleSetMappingAttribute(String role, String set, boolean isReadOnly)
	{
		super("RoleSetMapping", null, isReadOnly);
		
		// Create a helper object to hold the Set value.
		Object value = new DwpNameTypeAttributeValue(role,set);
		setValue(value);
		
		// Set the number of significant bits.
		this.setBits(getValue().length() * 16);
	}
	
	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_ROLESETMAPPING</b> is always returned.
	 */
	public String getType()
	{
		return DwpItemAttribute.TYPE_DWP_ROLESETMAPPING;
	}

	/**
	 * Get a copy the contents of the <code>DwpRoleSetMappingAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpRoleSetMappingAttribute</code> is returned.
	 * The children of the <code>DwpRoleSetMappingAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpNameTypeAttributeValue value = (DwpNameTypeAttributeValue)m_value;
		DwpRoleSetMappingAttribute attr = new DwpRoleSetMappingAttribute(value.m_name,value.m_type,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

}
