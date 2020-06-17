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
