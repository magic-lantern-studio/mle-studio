// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2020 Wizzer Works
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
package com.wizzer.mle.studio.handlers;

// Import Java packages.
import java.util.Iterator;

// Import Eclipse packages.
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

// Import Magic Lantern Studio packages.
import com.wizzer.mle.studio.MleLog;
import com.wizzer.mle.studio.natures.MleStudioNature;


/**
 * MasterTargetHandler is used to generate a Magic Lantern Digital Playprint when
 * the com.wizzer.mle.studio.commands.mastertarget command is invoked.
 * 
 * @author Mark S. Millard
 */
public class MasterTargetHandler extends AbstractHandler {
	
	private IProject m_project = null;

	/**
	 * Build the title mastering artifacts and Digital Playprint for the selected
	 * project.
	 */
	public void run()
	{
		MleLog.logInfo("Running command: " + "Master Target");
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
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);

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
							// Valid Magic Lantern project.
							m_project = project;
							run();
						} else
						{
							// Not a Magic Lantern project.
							m_project = null;
						}
					} catch (CoreException ex)
					{
						MleLog.logError(ex, "MasterTargetHandler: Unable to execute.");
					}
				}
			}
		} else
			m_project = null;

		return null;
	}
}
