/*
 * DwpEditorInput.java
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
