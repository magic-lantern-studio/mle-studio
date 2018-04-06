/*
 * DwpVector3PropertyCellEditorFctory.java
 * Created on Aug 6, 2004
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
package com.wizzer.mle.studio.dwp.ui;

// Import Eclipse packages.
import org.eclipse.swt.widgets.Composite;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.ui.AttributeCellEditor;
import com.wizzer.mle.studio.framework.ui.IAttributeCellEditorFactory;


/**
 * This class implements an <code>IAttributeCellEditorFactory</code> for a
 * Magic Lantern Digital Workprint <code>DwpVector3PropertyAttribute</code>.
 * 
 * @author Mark S. Millard
 */
public class DwpVector3PropertyCellEditorFactory implements IAttributeCellEditorFactory 
{
	/**
	 * The default constructor.
	 */
	public DwpVector3PropertyCellEditorFactory()
	{
		super();
	}

	/**
	 * Create an instance of the <code>DwpVector3PropertyCellEditor</code>.
	 * 
	 * @param parent The parent <code>Composite</code> widget of the Cell Editor.
	 * 
	 * @return An instance of a <code>DwpVector3PropertyCellEditor</code> is returned.
	 * If one could not be created, then <b>null</b> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.IAttributeCellEditorFactory#createInstance()
	 */
	public AttributeCellEditor createInstance(Composite parent)
	{
		return new DwpVector3PropertyCellEditor(parent);
	}

}
