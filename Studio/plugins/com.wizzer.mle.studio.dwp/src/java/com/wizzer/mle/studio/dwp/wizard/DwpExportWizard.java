/*
 * DwpExportWizard.java
 * Created on Jun 13, 2005
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
package com.wizzer.mle.studio.dwp.wizard;

// Import standard Java classes.
import java.util.List;
import java.net.URL;
import java.net.MalformedURLException;

// Import Eclipse classes.
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.IDE;

// Import Magic Lantern Digital Workprint classes.
import com.wizzer.mle.studio.dwp.DwpPlugin;

/**
 * This class implements a Wizard for exporting a Digital Workprint
 * to a file using the binary format.
 * 
 * @author Mark S. Millard
 */
public class DwpExportWizard extends Wizard implements IExportWizard
{
    // The main page.
    private DwpExportWizardPage m_mainPage = null;
    // The current selection.
    private IStructuredSelection m_selection = null;
    
    /**
     * The default constructor.
     */
    public DwpExportWizard()
    {
        super();
        
        // Establish a location for saving dialog settings between invokations.
    	IDialogSettings workbenchSettings = DwpPlugin.getDefault().getDialogSettings();
    	IDialogSettings section = workbenchSettings.getSection("DwpExportWizard");
    	if (section == null)
    		section = workbenchSettings.addNewSection("DwpExportWizard");
    	setDialogSettings(section);
    }

    /**
     * Performs any actions appropriate in response to the user having pressed
     * the Finish button, or refuse if finishing now is not permitted.
     * 
     * @return <b>true</b> is returned to indicate the finish request was accepted,
     * and <b>false</b> is returned to indicate that the finish request was refused.
     * 
     * @see org.eclipse.jface.wizard.IWizard#performFinish()
     */
    public boolean performFinish()
    {
        return m_mainPage.finish();
    }

    /**
     * Initializes this export wizard using the passed workbench and object selection.
     * <p>
     * This method is called after the no argument constructor and before other methods
     * are called.
     * </p>
     * 
     * @param workbench The current workbench.
     * @param selection The current selection.
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
    	m_selection = selection;
    	List selectedResources = IDE.computeSelectedResources(selection);
    	if (! selectedResources.isEmpty())
    	{
    		m_selection = new StructuredSelection(selectedResources);
    	}

    	// Look it up if current selection (after resource adapting) is empty.
    	if (m_selection.isEmpty() && workbench.getActiveWorkbenchWindow() != null)
    	{
    		IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
    		if ( page != null)
    		{
    			IEditorPart currentEditor = page.getActiveEditor();
    			if (currentEditor != null)
    			{
    				Object selectedResource = currentEditor.getEditorInput().getAdapter(IResource.class);
    				if (selectedResource != null)
    				{
    					m_selection = new StructuredSelection(selectedResource);
    				}
    			}
    		}
    	}

    	setWindowTitle(DwpExportWizardMessages.getString("DwpExportWizard.export"));
    	setDefaultPageImageDescriptor(getImageDescriptor("wizban/exportdir_wiz.gif"));
    	setNeedsProgressMonitor(true);
    }

    /*
     * Get the image descriptor with the given relative path.
     */
    private ImageDescriptor getImageDescriptor(String relativePath)
    {
    	String iconPath = "icons/full/";	
    	try
    	{
    		URL url = new URL(DwpPlugin.getDefault().getBundle().getEntry("/"), iconPath + relativePath);
    		return ImageDescriptor.createFromURL(url);
    	} catch (MalformedURLException ex)
    	{
    		// Should not happen.
    		return null;
    	}
    }

    /**
     * Add pages to the wizard.
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages()
    {
        super.addPages();
        m_mainPage = new DwpExportWizardPage(
            DwpExportWizardMessages.getString("DwpExportWizard.export.pageName"),
            m_selection);
        addPage(m_mainPage);
    }
}
