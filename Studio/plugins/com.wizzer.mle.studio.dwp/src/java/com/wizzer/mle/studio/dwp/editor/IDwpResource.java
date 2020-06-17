/*
 * IDwpResource.java
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
