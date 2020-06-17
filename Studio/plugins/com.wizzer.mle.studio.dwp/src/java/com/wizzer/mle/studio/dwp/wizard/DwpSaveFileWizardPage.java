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
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.dwp.DwpPlugin;

/**
 * This class provides a user interface used to save a Digital Workprint.
 * 
 * @author Mark Millard
 */
public class DwpSaveFileWizardPage extends WizardPage
{
	// Valid Digital Workprint extensions.
	private static final String[] WORKPRINT_EXTENSIONS = new String[] {"*.dwp", "*.adf", "*.wpf", "*.*"};
	// Digital Workprint extension names.
	private static final String[] WORKPRINT_NAMES = new String[] {
		"Digital Workprint (*.dwp)",
		"Actor Definition File (*.adf)",
		"Set Definition File (*.wpf)",
		"All Files (*.*)"
	};
	// Default name for DWP file.
	private static final String DEFAULT_WORKPRINT = "DigitalWorkprint.dwp";
	
	// The current selection.
    private IStructuredSelection m_selection;
    // The file text component.
    private Text m_fileText;
    // The browse button component.
    private Button m_browseButton;
    
	protected DwpSaveFileWizardPage(String pageName, IStructuredSelection selection)
	{
		super(pageName);
		
		setTitle(NewDigitalWorkprintWizardMessages.getString("DwpSaveFileWizardPage.title"));
		setDescription(NewDigitalWorkprintWizardMessages.getString("DwpSaveFileWizardPage.description"));
		ImageDescriptor image = DwpPlugin.getDefault().getImageDescriptor("icons/digitalworkprint_64x64_32bit.png");
		setImageDescriptor(image);

		m_selection = selection;
	}

	public void createControl(Composite parent)
	{
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
            | GridData.HORIZONTAL_ALIGN_FILL));
 
        Label label = new Label(composite, SWT.NONE);
        label.setText(NewDigitalWorkprintWizardMessages.getString("DwpSaveFileWizardPage.fileLabel"));
        
        m_fileText = new Text(composite, SWT.BORDER);
        GridData fileTextData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        fileTextData.grabExcessHorizontalSpace = true;
        m_fileText.setLayoutData(fileTextData);
        m_fileText.addModifyListener(new ModifyListener() {
        	public void modifyText(ModifyEvent event) {
        		handleTextModified(event);
        	}
        });
        
        m_browseButton = new Button(composite, SWT.NONE);
        m_browseButton.setText(NewDigitalWorkprintWizardMessages.getString("DwpSaveFileWizardPage.browseButton"));
        m_browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				handleButtonSelection(event);
			}
        });
        
        boolean useDefaultPath = true;
        StringBuffer buffer = new StringBuffer();
        if ((m_selection != null) && (! m_selection.isEmpty()))
        {
        	Object selection = m_selection.getFirstElement();
        	if (selection instanceof IResource)
        	{
        		IResource resource = (IResource) selection;
        		IPath path = null;
        		if (resource instanceof IContainer)
        		{
	        		path = resource.getLocation();
        		} else if (resource instanceof IFile)
        		{
        			path = ((IFile)resource).getParent().getLocation();
        		}
        		if (path != null)
        		{
        			buffer.append(path.toString());
        			buffer.append("/");
        			useDefaultPath = false;
        		} else
        			useDefaultPath = true;
        	}
        }
        if (useDefaultPath)
        {
        	String value = System.getProperty("MLE_ROOT");
        	buffer.append(value);
        	buffer.append("/include/workprints/");
        }
        buffer.append(DEFAULT_WORKPRINT);
        m_fileText.setText(buffer.toString());
        validatePage();

        setControl(composite);
	}
	
	public String getFilename()
	{
		return m_fileText.getText();
	}
	
	private void validatePage()
	{
		String text = m_fileText.getText();
		if ((text == null) || (text.isEmpty()))
		{
			setPageComplete(false);
			setErrorMessage(NewDigitalWorkprintWizardMessages.getString("DwpSaveFileWizardPage.validatePage"));
		} else
		{
			setPageComplete(true);
			setErrorMessage(null);
		}
	}

	protected void handleButtonSelection(SelectionEvent event)
	{
		FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
		dialog.setFilterExtensions(WORKPRINT_EXTENSIONS);
		dialog.setFilterNames(WORKPRINT_NAMES);
		dialog.setFilterIndex(0);
		
		// Open the dialog and retrieve the file to save.
		String filename = dialog.open();
		
		// Update the text field.
		m_fileText.setText(filename);
	}
	
	protected void handleTextModified(ModifyEvent event)
	{
		validatePage();
	}
}
