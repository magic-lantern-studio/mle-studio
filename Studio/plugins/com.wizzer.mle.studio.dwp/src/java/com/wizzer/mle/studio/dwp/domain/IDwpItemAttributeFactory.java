/*
 * IDwpItemAttributeFactory.java
 * Created on Aug 10, 2004
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
package com.wizzer.mle.studio.dwp.domain;

// Import the Magic Lantern Studio Framework packages.
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;


/**
 * This interface is used to provide a contract for classes that can create
 * new Magic Lantern Digital Workprint <code>IAttribute</code> items.
 * 
 * @author Mark S. Millard
 */
public interface IDwpItemAttributeFactory
{
	/**
	 * Create an instance of a Digital Workprint <code>IAttribute</code> item.
	 * 
	 * @param vla The Variable List Attribute managing the list of items.
	 * @param type The type of DWP <code>IAttribute</code> item to create.
	 * 
	 * @return A reference to an <code>IAttribute</code> tree is returned containing
	 * the required fields.
	 */
	public IAttribute createDwpItemAttribute(VariableListAttribute vla, Object type);

}
