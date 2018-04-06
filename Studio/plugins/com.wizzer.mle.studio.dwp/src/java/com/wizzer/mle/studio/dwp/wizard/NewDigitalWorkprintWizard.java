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
