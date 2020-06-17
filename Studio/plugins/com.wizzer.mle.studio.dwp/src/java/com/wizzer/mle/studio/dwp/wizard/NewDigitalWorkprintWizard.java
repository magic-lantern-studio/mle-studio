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
package com.wizzer.mle.studio.dwp.wizard;

// Import Eclipse classes.
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

// Import Magic Lantern Studio classes.
import com.wizzer.mle.studio.dwp.DwpException;
import com.wizzer.mle.studio.dwp.DwpWriter;
import com.wizzer.mle.studio.dwp.editor.DwpTableMainFrame;

public class NewDigitalWorkprintWizard extends BasicNewResourceWizard
{
	// The Digital Workprint editor page.
	private DigitalWorkprintPage m_dwpPage;
	// The DWP save page.
	private DwpSaveFileWizardPage m_saveFilePage;
	
	public NewDigitalWorkprintWizard()
	{
		super();
		setWindowTitle(NewDigitalWorkprintWizardMessages.getString(
			"NewDigitalWorkprintWizard.title"));
	}
	
	public void addPages()
	{
		super.addPages();
		
		m_dwpPage = new DigitalWorkprintPage("Digital Workprint");
		addPage(m_dwpPage);
		
		m_saveFilePage = new DwpSaveFileWizardPage("Save Digital Workprint", selection);
		addPage(m_saveFilePage);
	}

	public boolean performFinish()
	{
		boolean status = false;
		
		// Retrieve the file name.
		String filename = m_saveFilePage.getFilename();
		if ((filename == null) || filename.isEmpty())
			return status;
		
		// Retrieve the Digital Workprint.
		DwpTableMainFrame dwp = m_dwpPage.getDwpTable();
		
		// Save the Digital Workprint.
		DwpWriter writer = new DwpWriter(dwp.getTable());
		try {
			writer.write(filename);
			status = true;
		} catch (DwpException ex) {
			ex.printStackTrace();
		}
		
		return true;
	}

}
