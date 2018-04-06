// Declare package.
package com.wizzer.mle.studio.dwp.wizard;

// Import Eclipse classes.
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.dwp.DwpPlugin;
import com.wizzer.mle.studio.dwp.domain.DwpTable;
import com.wizzer.mle.studio.dwp.editor.DwpTableMainFrame;
import com.wizzer.mle.studio.framework.domain.Table;

public class DigitalWorkprintPage extends WizardPage
{
    // The Digital Workprint table.
	private DigitalWorkprintTable m_dwpTable;
	
	// Table title.
	private static final String DWP_TABLE_TITLE = "Digital Workprint";
	
	public class DigitalWorkprintTable extends DwpTableMainFrame
	{

		public DigitalWorkprintTable(Composite parent, String mainTitle, Table table)
		{
			super(parent, mainTitle, table);
			// TODO Auto-generated constructor stub
		}

		public void addActions(Composite parent)
		{
			// TODO Auto-generated method stub
		}

		public void handleViewConsoleAction(Event event)
		{
			// TODO Auto-generated method stub
		}
		
	}

	protected DigitalWorkprintPage(String pageName)
	{
		super(pageName);
		setTitle(NewDigitalWorkprintWizardMessages.getString("DigitalWorkprintPage.title"));
		setDescription(NewDigitalWorkprintWizardMessages.getString("DigitalWorkprintPage.description"));
		ImageDescriptor image = DwpPlugin.getDefault().getImageDescriptor("icons/digitalworkprint_64x64_32bit.png");
		setImageDescriptor(image);
	}

	public void createControl(Composite parent)
	{
		// Create the Digital Workprint table.
		m_dwpTable = new DigitalWorkprintTable(parent, DWP_TABLE_TITLE, new DwpTable());

		// Set the composite as the control for this page.
		setControl(m_dwpTable);
	}
	
	public DwpTableMainFrame getDwpTable()
	{
		return m_dwpTable;
	}

}
