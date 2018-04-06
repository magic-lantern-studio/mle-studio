/*
 * IDwpTableHandlerFactory.java
 * Created on Jul 21, 2004
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

// Import Magic Lantern Framework packages.
import com.wizzer.mle.studio.framework.domain.Table;


/**
 * This interface is used to create a handle for Digital Workprint tables
 * as defined by the Wizzer Works Digital Workprint specification.
 * 
 * @author Mark S. Millard
 */
public interface IDwpTableHandlerFactory
{
	/**
	 * Create a handler for a Digital Workprint table.
	 * 
	 * @return A reference to the new Service Information table is returned.
	 */
	public IDwpTableHandler createTableHandler(Table domainTable);

}
