/*
 * AbstractRehearsalPlayerConfigurationTab.java
 * Created on Jun 6, 2006
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
package com.wizzer.mle.studio.rehearsal.launch.ui;

// Import standard Java classes.

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
//import org.eclipse.cdt.core.model.ICProject;
//import org.eclipse.cdt.core.model.ICElement;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.rehearsal.RehearsalPlugin;
import com.wizzer.mle.studio.rehearsal.launch.IRehearsalPlayerLaunchConfigurationConstants;

/**
 * Common function for Rehearsal Player launch configuration tabs.
 * 
 * @author Mark S. Millard
 */
public abstract class AbstractRehearsalPlayerConfigurationTab
    extends AbstractLaunchConfigurationTab
{
	/** The mode the configuration is being built for. */
	protected String m_mode;

	// Hide the default constructor.
	private AbstractRehearsalPlayerConfigurationTab() {}
	
	/**
	 * A constructor that initializes the mode.
	 * 
	 * @param mode The run mode.
	 */
	public AbstractRehearsalPlayerConfigurationTab(String mode)
	{
		super();
		m_mode = mode;
	}
	
	/**
	 * Returns the current project context from which to initialize
	 * default settings, or <code>null</code> if none.
	 * 
	 * @return A project context is returned.
	 */
	protected IProject getContext()
	{
		IWorkbenchPage page = RehearsalPlugin.getActivePage();
		if (page != null)
		{
			ISelection selection = page.getSelection();
			if (selection instanceof IStructuredSelection)
			{
				IStructuredSelection ss = (IStructuredSelection)selection;
				if (! ss.isEmpty())
				{
					Object obj = ss.getFirstElement();
					if (obj instanceof IProject)
					{
						return (IProject)obj;
					}
					if (obj instanceof IJavaElement)
					{
						IJavaElement element = (IJavaElement)obj;
						IJavaProject jproject = element.getJavaProject();
						IProject project = jproject.getProject();
						return project;
					}
/* XXX - Leave this for CDT based Magic Lantern Tools (e.g. BRender)
					if (obj instanceof ICElement)
					{
						ICElement element = (ICElement)obj;
						ICProject cproject = element.getCProject();
						IProject project = cproject.getProject();
						return project;
					}
*/
					if (obj instanceof IResource)
					{
						IProject project = ((IResource)obj).getProject();
						return project;
					}
				}
			}
			
			IEditorPart part = page.getActiveEditor();
			if (part != null)
			{
				IEditorInput input = part.getEditorInput();
				return (IProject) input.getAdapter(IProject.class);
			}
		}
		
		return null;
	}

	/**
	 * Set the project attribute based on the specified project and launch
	 * configuration.
	 * 
	 * @param project The <code>IProject</code> to initialize for.
	 * @param config The launch configuration.
	 */
	protected void initializeProject(IProject project, ILaunchConfigurationWorkingCopy config)
	{
		if (project != null && project.exists())
		{
			String name = project.getName();
			config.setAttribute(IRehearsalPlayerLaunchConfigurationConstants.ATTR_PROJECT_NAME, name);
			IPath location = project.getLocation();
			config.setAttribute(IRehearsalPlayerLaunchConfigurationConstants.ATTR_PROJECT,location.toString());
		}
	}

}
