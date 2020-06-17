/*
 * AbstractRuntimePlayerConfigurationTab.java
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
package com.wizzer.mle.studio.dpp.launch.ui;

// Import standard Java classes.

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
//import org.eclipse.cdt.core.model.ICProject;
//import org.eclipse.cdt.core.model.ICElement;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.dpp.DppPlugin;
import com.wizzer.mle.studio.dpp.launch.IDppLaunchConfigurationConstants;

/**
 * Common function for Runtime Player launch configuration tabs.
 * 
 * @author Mark S. Millard
 */
public abstract class AbstractRuntimePlayerConfigurationTab
    extends AbstractLaunchConfigurationTab
{
	/** The mode the configuration is being built for. */
	protected String m_mode;

	// Hide the default constructor.
	private AbstractRuntimePlayerConfigurationTab() {}
	
	/**
	 * A constructor that initializes the mode.
	 * 
	 * @param mode The run mode.
	 */
	public AbstractRuntimePlayerConfigurationTab(String mode)
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
		IWorkbenchPage page = DppPlugin.getActivePage();
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
/*
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
			config.setAttribute(IDppLaunchConfigurationConstants.ATTR_PROJECT_NAME, name);
			IPath location = project.getLocation();
			config.setAttribute(IDppLaunchConfigurationConstants.ATTR_PROJECT,location.toString());
		}
	}

}
