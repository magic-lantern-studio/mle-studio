/*
 * IDwpResource.java
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
package com.wizzer.mle.studio.dwp.editor;

// Import Eclipse packages.
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;


/**
 * This interface is used to manage a Digital Workprint Table resource
 * associated with a Magic Lantern project.
 * 
 * @author Mark S. Millard
 */
public interface IDwpResource
{
	/** Qualifier for DWP Resources. */
	static public String DWP_QUALIFIER = "com.wizzer.mle.studio.dwp";
	/** The property for a DWP editor. */
	static public String DWP_EDITOR_PROPERTY = "DWP.EDITOR.RESOURCE";
	
	/**
	 * Get the resource <code>IFolder</code> for the Digital Workprint Table.
	 * 
	 * @return A <code>IFolder</code> resource is returned indicating the location of
	 * the Digital Workprint table within the context of a Magic Lantern project.
	 */
	public IFolder getFolder();
	
	/**
	 * Set the <code>IFolder</code> resource for the Digital Workprint Table.
	 * 
	 * @param folder A <code>IFolder</code> resource indicating the location of
	 * the Digital Workprint table.
	 */
	public void setFolder(IFolder folder);
	
	/**
	 * Validate the <code>IFolder</code> resource for the Digital Workprint
	 * table.
	 * 
	 * @return <b>true</b> will be returned if the resource is valid. Otherwise
	 * <b>false</b> shall be returned.
	 */
	public boolean validateFolder();
}
