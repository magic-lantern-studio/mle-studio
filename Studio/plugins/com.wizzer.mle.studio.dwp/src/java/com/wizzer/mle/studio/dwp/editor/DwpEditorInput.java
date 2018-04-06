/*
 * DwpEditorInput.java
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
import org.eclipse.ui.part.FileEditorInput;

// Import Digital Workprint packages.
import com.wizzer.mle.studio.dwp.domain.DwpTable;
import com.wizzer.mle.studio.dwp.domain.DwpTableHandlerFactory;
import com.wizzer.mle.studio.dwp.domain.IDwpTableHandler;
import com.wizzer.mle.studio.dwp.editor.DwpResource;

/**
 * Adapter for making a file resource a suitable input for the
 * Magic Lantern Digital Workprint Table editor.
 * 
 * @author Mark S. Millard
 */
public class DwpEditorInput extends FileEditorInput
{
	static final private String g_inputLabel = "Digital Workprint";
	static final private String g_toolTip = "DWP Table Input";
	
	// The associated resource.
	private DwpResource m_dwpResource = null;
	
	/**
	 * Creates an DWP editor input based on the given file resource.
	 * 
	 * @param file The file resource.
	 */
	public DwpEditorInput(IFile file)
	{
		super(file);
	}

	/**
	 * Initialize the editor input by creating a Digital Workprint Table and
	 * associating it with the file resource. The domain table is updated to
	 * reflect the contents of the file resource.
	 * <p>
	 * This method is normally called from the <code>DwpToolEditor</code> when
	 * the editor is intialized.
	 * </p>
	 * 
	 * @param name The name of the file resource.
	 * @param domainTable A reference to the <code>DwpTable</code>.
	 * @param retrieve If <b>true</b>, then the resource will be retrieved from
	 * the file resource. If <b>false</b>, then the resource will not be read.
	 */
	public void init(String name, DwpTable table, boolean retrieve)
	{
		IDwpTableHandler dwp = DwpTableHandlerFactory.getInstance().createTableHandler(table);
		m_dwpResource = new DwpResource(name,dwp);

		// Update domain table with unmarshalled data from the IFile resource.
		if (retrieve)
		    m_dwpResource.unmarshall(getFile());
	}
	
	/**
	 * Get the associated DWP resource.
	 * 
	 * @return The Digital Workprint resource is returned. This may be
	 * <b>null</b>.
	 */
	public DwpResource getResource()
	{
	    return m_dwpResource;
	}
	
	/**
	 * Unmarshall the Digital Workprint from the file.
	 * 
	 * @return If the file is successfully unmarshalled, then <b>true</b> will be returned.
	 * Otherwise, <b>false</b> will be returned.
	 */
	public boolean unmarshall()
	{
		boolean status = false;
		if (m_dwpResource != null)
			status = m_dwpResource.unmarshall(getFile());
		return status;
	}
    
	/**
	 * Marshall the Digital Workprint to the file.
	 * 
	 * @return If the file is successfully marshalled, then <b>true</b> will be returned.
	 * Otherwise, <b>false</b> will be returned.
	 */
	public boolean marshall()
	{
		boolean status = false;
		if (m_dwpResource != null)
			status = m_dwpResource.marshall(getFile());
		return status;
	}

}
