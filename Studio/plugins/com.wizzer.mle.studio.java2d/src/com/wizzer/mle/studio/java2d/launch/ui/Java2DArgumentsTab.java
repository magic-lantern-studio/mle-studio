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
package com.wizzer.mle.studio.java2d.launch.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
//Import Eclipse packages.
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Mark Millard
 */
public class Java2DArgumentsTab extends JavaArgumentsTab {
	
	private IProject getCurrentProject() {
	    IProject project = null;
	    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    if (window != null) {
	        ISelection iselection = window.getSelectionService().getSelection();
	        IStructuredSelection selection = (IStructuredSelection) iselection;
	        if (selection == null) {
	            return null;
	        }

	        Object firstElement = selection.getFirstElement();
	        if (firstElement instanceof IResource) {
	            project = ((IResource) firstElement).getProject();
	        } else if (firstElement instanceof PackageFragmentRoot) {
	            IJavaProject jProject = ((PackageFragmentRoot) firstElement)
	                    .getJavaProject();
	            project = jProject.getProject();
	        } else if (firstElement instanceof IJavaElement) {
	            IJavaProject jProject = ((IJavaElement) firstElement)
	                    .getJavaProject();
	            project = jProject.getProject();
	        }
	    }
	    return project;
	}

	@Override
    public void setDefaults(ILaunchConfigurationWorkingCopy config) {
        // Start with the normal defaults for this tab.
        super.setDefaults(config);
        
        IProject project = getCurrentProject();
        
        String projectName = project.getName();
        String args = "src/gen/";
        args = args.concat(projectName);
        args = args.concat(".dpp");
        
        // Set/override them with name of Digital Playprint.
        config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, args);
    }
}
