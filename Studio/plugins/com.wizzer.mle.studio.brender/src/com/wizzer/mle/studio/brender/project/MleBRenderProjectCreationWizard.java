/*
 * MleBRenderProjectCreationWizard.java
 * Created on Aug 4, 2005
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
package com.wizzer.mle.studio.brender.project;

// Import standard Java classes.
import java.lang.reflect.InvocationTargetException;

// Import Eclipse classes.
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.operation.IRunnableWithProgress;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.make.internal.ui.MakeUIPlugin;
import org.eclipse.cdt.make.ui.wizards.MakeProjectWizardOptionPage;
import org.eclipse.cdt.make.ui.wizards.NewMakeProjectWizard;
import org.eclipse.cdt.ui.wizards.NewCProjectWizardPage;

// Import Magic Lantern DWP classes.

// Import Magic Lantern DPP classes.

// Import Magic Lantern classes.
import com.wizzer.mle.studio.MleLog;
import com.wizzer.mle.studio.MlePlugin;
import com.wizzer.mle.studio.project.MleTargetConfigurationPage;
import com.wizzer.mle.studio.project.MleTargetCreationOperation;
import com.wizzer.mle.studio.project.MleProjectOverwriteQuery;


/**
 * This class implements a wizard for creating a new Magic Lantern
 * BRender Project.
 * 
 * @author Mark S. Millard
 */
public class MleBRenderProjectCreationWizard extends NewMakeProjectWizard implements IExecutableExtension
{
    // The wizard title and description.
	private static final String WZ_TITLE = "MleBRenderProjectCreationWizard.title";
	private static final String WZ_DESC = "MleBRenderProjectCreationWizard.description";

	private static final String WZ_SETTINGS_TITLE = "MakeCCWizard.title";
	private static final String WZ_SETTINGS_DESC = "MakeCCWizard.description";

	// The configuration page for the BRender platform.
	private MleTargetConfigurationPage m_brenderConfigPage;
	// The configuration element.
	private IConfigurationElement m_configElement;

	/**
	 * The default constructor.
	 */
	public MleBRenderProjectCreationWizard()
	{
		super(MleProjectMessages.getString(WZ_TITLE), MleProjectMessages.getString(WZ_DESC));
	}

	/**
	 * Add pages to the wizard before the wizard opens.
	 * 
	 * @see Wizard#addPages
	 */
	public void addPages()
	{
		super.addPages();

		// Create a configuration page for the BRender target platform.
		m_brenderConfigPage = new MleTargetConfigurationPage("MleBRenderTargetConfigurationWizardPage");
		m_brenderConfigPage.setTitle(MleProjectMessages.getString("MleBRenderConfigurationWizardPage.pagetitle"));
		m_brenderConfigPage.setDescription(MleProjectMessages.getString("MleBRenderConfigurationWizardPage.pagedescription"));
		addPage(m_brenderConfigPage);
		
		// Create a configuration page for the C++/make environment.
		addPage(
			fOptionPage =
				new MleMakeProjectOptionPage(
					MakeUIPlugin.getResourceString(WZ_SETTINGS_TITLE),
					MakeUIPlugin.getResourceString(WZ_SETTINGS_DESC),
					m_configElement));
	}

	/**
	 * Stores the configuration element for the wizard.  The configuration element will be
	 * used in <code>performFinish</code> to set the result perspective.
	 * 
	 * @param cfig The configuration element used to trigger this execution.
	 * It can be queried by the executable extension for specific configuration properties.
	 * @param propertyName The name of an attribute of the configuration element used on
	 * the createExecutableExtension(String) call. This argument can be used in the
	 * cases where a single configuration element is used to define multiple executable
	 * extensions.
	 * @param data Data Adapter data in the form of a <code>String</code>,
	 * a <code>Hashtable</code>, or <b>null</b>.
	 * 
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data)
	{
		// Remember the configuration element.
		m_configElement = cfig;
	}

	/**
	 * Execute the project initialization and creation.
	 * 
	 * @param monitor The progress monitor.
	 * 
	 * @throws CoreException This exception is thrown if the BRender project
	 * can not be successfully created and initialized.
	 */
	protected void doRun(IProgressMonitor monitor) throws CoreException
	{
	    // Create a monitor, if necessary.
		if (monitor == null)
		{
			monitor = new NullProgressMonitor();
		}
		
		// Create and initialize the project.
		monitor.beginTask(MakeUIPlugin.getResourceString("MakeCCWizard.task_name"), 10);
		super.doRun(new SubProgressMonitor(monitor, 9));
		
		// Configure new project.
		if (newProject != null)
		{
			// Add C++ Nature to the newly created project.
			CCorePlugin.getDefault().convertProjectFromCtoCC(newProject, new SubProgressMonitor(monitor, 1));
		
			// Configure the new Cpp project.
			MleTargetCreationOperation runnable = new MleCppProjectCreationOperation(
		        newProject, fMainPage, m_brenderConfigPage, (MleMakeProjectOptionPage)fOptionPage,
		        new MleProjectOverwriteQuery(getShell()));
			
			IRunnableWithProgress op = new WorkspaceModifyDelegatingOperation(runnable);
			try
			{
				getContainer().run(false, true, op);
			} catch (InvocationTargetException ex)
			{
				return;
			} catch (InterruptedException ex)
			{
				return;
			}
			
			// Update the resources in the final perspective.
			BasicNewProjectResourceWizard.updatePerspective(m_configElement);
			IResource[] resources = runnable.getElementsToOpen();
			if (resources != null)
			{
				for (int i = 0; i < resources.length; i++)
				    // Open the MLE project resource.
				   openResource(resources[i]);
			}
		}

		monitor.done();
	}
	
	protected void doRunEpilogue(IProgressMonitor monitor)
	{
        // Retrieve the active Workbench window.
		IWorkbenchWindow window = MlePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		if (window != null)
		{
            // Retrieve the active page.
			final IWorkbenchPage activePage = window.getActivePage();
			
            // Select and reveal the new MLE project.
			BasicNewResourceWizard.selectAndReveal(newProject, activePage.getWorkbenchWindow());
			
			//newProject.close(monitor);
		}
	}
	
	/*
	 * Open the project resource.
	 * 
	 * @param resource The project resource to open.
	 */
	protected void openResource(final IResource resource)
	{
		// Make sure the resource is of the appropriate FILE type.
		if (resource.getType() != IResource.FILE)
			return;
		
		// Retrieve the active Workbench window.
		IWorkbenchWindow window = MlePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return;
		
		// Retrieve the active page.
		final IWorkbenchPage activePage = window.getActivePage();
		if (activePage != null) {
			final Display display = getShell().getDisplay();
			display.asyncExec(new Runnable()
			{
				public void run()
				{
					try
					{
						//activePage.openEditor((IFile)resource);
						IDE.openEditor(activePage,(IFile)resource);
					} catch (PartInitException e)
					{
						MleLog.logError(e,"Unable to open resource " + resource.getName());
					}
				}
			});
			
			// Select and reveal the new MLE project.
			BasicNewResourceWizard.selectAndReveal(resource, activePage.getWorkbenchWindow());
		}
	}
    
	// Set the default DWP and DPP file names.
	private void setDefaultFilenames(MleTargetConfigurationPage page)
	{
	    if (! page.isDwpSpecified())
	        page.setDwpFilename(fMainPage.getProjectName() + ".dwp");
        if (! page.isDppSpecified())
            page.setDppFilename(fMainPage.getProjectName() + ".dpp");
	}

	/**
	 * Get the successor of the given page.
	 * 
	 * @param page The reference page to use.
	 * 
	 * @return The next page is returned, or <b>null</b> if there is none.
	 */
	public IWizardPage getNextPage(IWizardPage page)
	{
		if (page instanceof NewCProjectWizardPage)
		{
            setDefaultFilenames(m_brenderConfigPage);
        	return m_brenderConfigPage;
		}
		
		return super.getNextPage(page);
	}

}
