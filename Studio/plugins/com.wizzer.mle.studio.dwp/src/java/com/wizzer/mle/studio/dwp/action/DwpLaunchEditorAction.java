/*
 * DwpLaunchEditorAction.java
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
package com.wizzer.mle.studio.dwp.action;

// Import standaard Java packages.
import java.util.ArrayList;
import java.util.Iterator;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

// Import Eclipse packages.
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.ui.ide.IDE;

// Import Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpLog;
import com.wizzer.mle.studio.dwp.editor.DwpEditorInput;
import com.wizzer.mle.studio.dwp.editor.DwpResource;
import com.wizzer.mle.studio.dwp.editor.DwpTableEditor;
import com.wizzer.mle.studio.dwp.editor.IDwpResource;

/**
 * This class is an object action that is contributed into a popup menu for a view or editor.
 * It implements <code>IObjectActionDelegate</code> and is used to launch the Auetur
 * Magic Lantern DWP editors.
 * <p>
 * The Magic Lantern DWP editors include:
 * <ul>
 * <li>Digital Workprint Table Editor</li>
 * </ul>
 * 
 * @author Mark S. Millard
 */
public class DwpLaunchEditorAction implements IObjectActionDelegate
{
	// The magic indicating whether a file is an ASCII DWP or not.
	private static String DWP_HEADER_MAGIC = "#DWP 1.0 ascii";
	
	// The current Workbench part.
	private IWorkbenchPart m_currentPart = null;
	// The selected files.
	private IFile[] m_selectedFiles = null;
	
	/**
	 * The default constructor.
	 */
	public DwpLaunchEditorAction()
	{
		super();
	}

	/**
	 * Sets the active part for the delegate. The active part is commonly used to get a working
	 * context for the action, such as the shell for any dialog which is needed.
	 * <p>
	 * This method will be called every time the action appears in a popup menu.
	 * The <code>targetPart</code> may change with each invocation.
	 * </p>

	 * @param action The action proxy that handles presentation portion of the action.
	 * @param targetPart The new part target.
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		//DwpLog.getInstance().debug("DWPLaunchEditorAction: Setting active part.");
		
		// Save the new target Workbench part. We'll use it to open the editor when
		// the action is executed.
		m_currentPart = targetPart;
	}

	/**
	 * Performs this action.
	 * <p> 
	 * This method is called by the proxy action when the action has been triggered.
	 * </p>
	 * 
	 * @param action The action proxy that handles the presentation portion of the action.
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		//DwpLog.getInstance().debug("DwpLaunchEditorAction: running.");
		
		if (m_selectedFiles != null)
		{
			for (int i = 0; i < m_selectedFiles.length; i++)
			{
				// XXX - Make sure the selected file has the DWP property set before
				// launching this editor.
				activateEditor(m_selectedFiles[i]);
			}
		}
	}
	
	// Activate the editor for the specified file.
	private boolean activateEditor(IFile file)
	{
		boolean status = false;
		
		DwpLog.logInfo("Launching DWP editor for " + file.getName());
		
		try {
			// Make sure the file can be editted with a DWP Table tool.
			QualifiedName key = new QualifiedName(IDwpResource.DWP_QUALIFIER,
				IDwpResource.DWP_EDITOR_PROPERTY);
			String value = file.getPersistentProperty(key);
			if (value != null)
			{
				if (value.equals(DwpResource.DWP_EDITOR_VALUE))
				{
					// Open the editor for the DWP.
					DwpEditorInput input = new DwpEditorInput(file);
					IEditorPart editor = IDE.openEditor(m_currentPart.getSite().getPage(),
					    input,DwpTableEditor.getID(),true);
					//IEditorPart editor = m_currentPart.getSite().getPage().openEditor(input,DwpTableEditor.getID(),true);
            
					if (editor != null)
						status = true;
				}
			}
		} catch (PartInitException ex)
		{
			DwpLog.logError(ex,"Unable to show DWP Table Editor for " + file.getName());
		} catch (CoreException ex)
		{
			DwpLog.logError(ex,"Unable to show DWP TAble Editor for " + file.getName());
		}
		
		return status;
	}

	/**
	 * Notifies this action delegate that the selection in the workbench has changed.
	 * 
	 * @param action The action proxy that handles presentation portion of the action.
	 * @param selection The current selection, or null if there is no selection.

	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		//DwpLog.getInstance().debug("DwpLaunchEditorAction: Processing selection changed.");
		
		ArrayList newFiles = new ArrayList();
		IFile file = null;
		
		// Process the selection list.
		if ((selection != null) && (selection instanceof IStructuredSelection))
		{
			IStructuredSelection ss = (IStructuredSelection) selection;
			for (Iterator iter = ss.iterator(); iter.hasNext(); )
			{
				Object obj = iter.next();
				
				if (obj instanceof IFile)
				{
					file = (IFile) obj;
				}
				else if (obj instanceof IAdaptable)
				{
					IAdaptable a = (IAdaptable) obj;
					IResource res = (IResource) a.getAdapter(IResource.class);
					if (res instanceof IFile)
						file = (IFile) res;
				}
				
				if ((file != null) && (file.isSynchronized(IResource.DEPTH_ZERO)))
				{
					try
					{
						// Select the file only if it has the qualified property value.
						QualifiedName key = new QualifiedName(IDwpResource.DWP_QUALIFIER,
							IDwpResource.DWP_EDITOR_PROPERTY);
						String value = file.getPersistentProperty(key);
						if (value != null)
						{
							if (value.equals(DwpResource.DWP_EDITOR_VALUE))
							{
								// Add the file to the list to edit.
								newFiles.add(file);
							}
						} else if (isDwpASCII(file))
						{
							// The file is a DWP in ASCII format. Add the missing
							// qualification key/value pair. Note that the file was
							// probably added to the project externally, and not through
							// a Magic Lantern tool.
							file.setPersistentProperty(key,DwpResource.DWP_EDITOR_VALUE);
							
							// Add the file to the list to edit.
							newFiles.add(file);
							
						}
					} catch (CoreException ex)
					{
						DwpLog.logError(ex,"Unable to obtain property on " + file.getName());
					}
				}
			}
		}
		
		// Create the final list of selected files. This list will be processed when
		// action is actually executed.
		if (newFiles.isEmpty())
		{
			m_selectedFiles = null;
		}
		else
		{
			m_selectedFiles = (IFile[]) newFiles.toArray(new IFile[newFiles.size()]);
		}
	}

	// Test to see if the file resource is a Digital Workprint in ASCII format.
    private boolean isDwpASCII(IFile file)
    {
    	boolean retValue = false;
    	
		if ((file != null) && (file.isSynchronized(IResource.DEPTH_ZERO)))
		{
			try
			{
				InputStream istream = file.getContents();
				BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
				
				// Get the first line in the file.
				String line = reader.readLine();
				
				if (line.equals(DWP_HEADER_MAGIC))
				    retValue = true;
			} catch (CoreException ex)
			{
				DwpLog.logError(ex,"Unable to determine if file is a Digital Workprint.");
			} catch (IOException ex)
			{
				DwpLog.logError(ex,"Unable to determine if file is a Digital Workprint.");
			}
		}
		
	   	return retValue;
    }
}
