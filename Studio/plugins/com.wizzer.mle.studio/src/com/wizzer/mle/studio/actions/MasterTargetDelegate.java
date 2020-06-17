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
package com.wizzer.mle.studio.actions;

// Import Eclipse classes.
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.wizzer.mle.studio.MleLog;
import com.wizzer.mle.studio.natures.MleStudioNature;

public class MasterTargetDelegate implements IWorkbenchWindowActionDelegate
{
	private IProject m_project = null;

	public void dispose()
	{
		// TODO Auto-generated method stub.
	}

	public void init(IWorkbenchWindow window)
	{
		// TODO Auto-generated method stub.	
	}

	public void run(IAction action)
	{
		MleLog.logInfo("Running action: " + action.getId());
		try
		{
			if ((m_project != null) && m_project.hasNature(MleStudioNature.NATURE_ID))
			{
				NullProgressMonitor monitor = new NullProgressMonitor();
				m_project.build(IncrementalProjectBuilder.FULL_BUILD , monitor);
			}
		} catch (CoreException ex)
		{
			MleLog.logError(ex, "Unable to master project: " + ex.getMessage());
		}
	}

	public void selectionChanged(IAction action, ISelection selection)
	{
		if ((selection != null) && (selection instanceof IStructuredSelection))
		{
			IResource resource = null;
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Iterator iter = structuredSelection.iterator();
			while (iter.hasNext())
			{
				Object next = iter.next();
				if (next instanceof IResource)
				{
					resource = (IResource) next;
				} else if (next instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable) next;
					resource = (IResource) adaptable.getAdapter(IResource.class);
				}
				if (resource != null)
				{
					IProject project = resource.getProject();
					try
					{
						if (project.hasNature(MleStudioNature.NATURE_ID))
						{
							// Valid Magic Lantern Project.
							m_project = project;
							action.setEnabled(true);
						} else
						{
							m_project = null;
							action.setEnabled(false);
						}
					} catch (CoreException ex)
					{
						action.setEnabled(false);
					}
				}
			}
		} else
			m_project = null;
	}

}
