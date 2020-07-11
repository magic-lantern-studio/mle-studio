/*
 * MleProjectCreationWizard.java
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
package com.wizzer.mle.studio.project;

// Import standard Java classes.

// Import Eclipse classes.
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ErrorDialog;

import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.dialogs.IOverwriteQuery;

import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import org.eclipse.swt.widgets.Display;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.MleLog;


/**
 * This class implements a wizard for creating a new Magic Lantern
 * Project.
 * 
 * @author Mark S. Millard
 */
public abstract class MleProjectCreationWizard extends Wizard implements IExecutableExtension, INewWizard
{
    /** Unique identifier for Mastering Nature. */
    public static final String MASTERING_NATURE_ID = "com.wizzer.mle.studio.dpp.MasteringNature";

	// The workbench.
	protected IWorkbench m_workbench;
	// The selected element.
	protected IStructuredSelection m_selection;
	
	// Cache of newly-created project
	protected IProject m_newProject;
	// Cache of newly-create DWP.
	protected IFile m_dwp = null;

	// The configuration element.
	protected IConfigurationElement m_configElement;
	// The resource to open.
	protected IResource m_elementToOpen;
	// Flag indicating whether to query on overwrite.
	protected IOverwriteQuery m_overwriteQuery;

	/**
     * The default constructor.
     */
    public MleProjectCreationWizard()
    {
        super();
        
		// Set the wizard window title.
		setWindowTitle(MleProjectMessages.getString("MleProjectCreationWizard.title"));
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
	 * Initializes this creation wizard using the passed workbench and object selection.
	 * <p>
	 * This method is called after the no argument constructor and before other methods are called.
	 * </p>
	 * 
	 * @param workbench The current workbench.
	 * @param selection The current object selection.
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		// Save the workbench.
		m_workbench = workbench;
		// Save the selection.
		m_selection = selection; 
	}
	
	// Handle exceptions in a common way. Messages are extracted as resources.
	protected void handleException(Throwable target)
	{
		String title = MleProjectMessages.getString("MleProjectCreationWizard.op_error.title");
		String message = MleProjectMessages.getString("MleProjectCreationWizard.op_error.message");
		if (target instanceof CoreException)
		{
			IStatus status = ((CoreException)target).getStatus();
			ErrorDialog.openError(getShell(), title, message, status);
			MleLog.logError((CoreException)target,message);
		} else
		{
			MessageDialog.openError(getShell(), title, target.getMessage());
			MleLog.logWarning(target.toString());
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
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
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

}
