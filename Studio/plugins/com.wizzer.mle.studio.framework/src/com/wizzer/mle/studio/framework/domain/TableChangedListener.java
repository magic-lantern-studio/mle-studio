// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
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
package com.wizzer.mle.studio.framework.domain;

// Import standard Java packages.
import java.util.Observable;

/**
 * This class is used to notify collections of <code>Observers</code> on a
 * domain <code>Table</code>.
 * 
 * @author Mark S. Millard
 */
public class TableChangedListener extends Observable
{
	private Object m_owner = null;
	
	/**
	 * A constructor that initializes the owner of this
	 * <code>Observable</code>.
	 */
	public TableChangedListener(Object owner)
	{
		super();
		m_owner = owner;
	}
	
	public void setModified()
	{
		setChanged();
	}
	
	public boolean isModified()
	{
		return hasChanged();
	}
}
