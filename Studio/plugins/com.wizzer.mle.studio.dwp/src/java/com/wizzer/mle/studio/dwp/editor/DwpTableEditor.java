/*
 * DwpTableEditor.java
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
package com.wizzer.mle.studio.dwp.editor;

// Import standard Java packages.
import java.util.Observable;
import java.util.Observer;

// Import Eclipse packages.
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.domain.TableChangedListener;
import com.wizzer.mle.studio.framework.ProcessMessageQueue;

// Import Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpLog;


/**
 * This class is an Eclipse IDE Editor used to present the Magic Lantern
 * Digital Worprint.
 * 
 * @author Mark S. Millard
 */
public class DwpTableEditor extends EditorPart implements Observer
{
	// The unique identifer for the DWP Table Editor.
	static final private String DWP_TABLE_EDITOR = "com.wizzer.mle.studio.dwp.editor.DwpTableEditor";
	static final private String DWP_TABLE_NAME = "Digital Workprint Table";
	
	// The page for the Outline View.
	private DwpTableEditorContentOutlinePage m_outlinePage = null;

	/**
	 * Get the unique identifier for the DWP Table Editor.
	 * 
	 * @return A String is returned representing the ID of the Eclipse IDE editor.
	 */
	static public String getID()
	{
		return DWP_TABLE_EDITOR;
	}

	// The DWP Table tool.
	private DwpTableIdeFrame m_tool = null;
	private ProcessMessageQueue m_msgQueueHandler = null;
	
	/**
	 * The default constructor.
	 */
	public DwpTableEditor()
	{
		super();
	}

	/**
	 * Saves the contents of the Digital Workprint Table. 
	 * <p>
	 * If the save is successful, the part will fire a property changed event reflecting
	 * the new dirty state (<b>PROP_DIRTY</b> property).
	 * </p><p>
	 * If the save is cancelled through user action, or for any other reason, the part
	 * will invoke <code>setCancelled</code> on the <code>IProgressMonitor</code> to inform the caller.
	 * </p><p>
	 * This method is long-running; progress and cancellation are provided by the given
	 * progress monitor.
	 * </p>
	 * 
	 * @param monitor The progress monitor.
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor)
	{
		boolean status = false;
		
		DwpLog.getInstance().debug("Saving Digital Workprint Table.");
		
		// Save the DWP.
		DwpEditorInput input = (DwpEditorInput)getEditorInput();
		status = input.marshall();
		
		// If data was saved successfully, fire the dirty property.
		if (status) {
			// Mark as no longer dirty.
			m_tool.setIsDirty(false);
        	
			// and fire the property.
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}
	
	/**
	 * This method asks the user for the workspace path of a file resource
	 * and saves the document there.
	 * 
	 * @param progressMonitor The progress monitor to be used.
	 */
	protected boolean performSaveAs(IProgressMonitor progressMonitor)
	{
		Shell shell = getSite().getShell();
		IEditorInput input = getEditorInput();
		
		// Create a new "Save As..." dialog.
		SaveAsDialog dialog = new SaveAsDialog(shell);
		
		// Set the original file as initial selection.
		IFile original = (input instanceof IFileEditorInput) ? ((IFileEditorInput) input).getFile() : null;
		if (original != null)
			dialog.setOriginalFile(original);
		
		dialog.create();
		
		// Open the dialog.
		if (dialog.open() == Window.CANCEL)
		{
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return false;
		}
		
		// And retrieve the specified path.
		IPath filePath = dialog.getResult();
		if (filePath == null)
		{
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return false;
		}

		// Update editor input from the specified file.
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IFile file = workspace.getRoot().getFile(filePath);
		final DwpEditorInput newInput = new DwpEditorInput(file);
		newInput.init(newInput.getName(),m_tool.getTable(),false);
		
		// Determine if the file exists; if it doesn't, create it.
		if (! file.exists())
		{
		    IContainer parent = file.getParent();
		    
			// Write the resource to the file system.
		    if (parent.getType() == IResource.FOLDER)
		        newInput.getResource().createFileInFolder((IFolder)parent);
		    else if (parent.getType() == IResource.PROJECT)
		        newInput.getResource().createFileInProject((IProject)parent);
		    else
		    {
				if (progressMonitor != null)
					progressMonitor.setCanceled(true);
				return false;		        
		    }
		} else
		{
		    // XXX - determine if the user really wants to override an existing file.
		}
		
		// Save the file.
		if (! newInput.marshall())
		{
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return false;
		}
		
		// Reset the input to the editor.
		setInput(newInput);
		setPartName(newInput.getName());
		
		// Fire the property changed event for input modifications.
		firePropertyChange(IEditorPart.PROP_INPUT);

		// Shut down the progress monitor.
		if (progressMonitor != null)
			progressMonitor.setCanceled(false);
		
		return true;
	}

	/**
	 * Saves the contents of the Digital Workprint Table to another object.
	 * <p>
	 * This method will open a "Save As" dialog where the user will be able to select a new
	 * name for the contents. After the selection is made, the contents will be saved to that
	 * new name. During this operation a <code>IProgressMonitor</code> should be used to
	 * indicate progress.
	 * </p><p>
	 * If the save is successful, the part fires a property changed event reflecting the
	 * new dirty state (<b>PROP_DIRTY</b> property).
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs()
	{
		DwpLog.getInstance().debug("Attempting to save DWP as...");
		
		// Save the file first.
		boolean status = performSaveAs(new NullProgressMonitor());

		if (status)
		{
			// Mark as no longer dirty.
			m_tool.setIsDirty(false);
	    	
			// Fire the property changed event for dirty modifications.
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	/**
	 * Sets the cursor and selection state for this editor to the passage defined by the given marker.
	 * Not used yet.
	 * 
	 * @param marker The marker.
	 * 
	 * @see org.eclipse.ui.IEditorPart#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker(IMarker marker)
	{
		// Do nothing for now.
	}

	/**
	 * Initializes this editor with the given editor site and input.
	 * <p>
	 * This method is automatically called shortly after part construction;
	 * it marks the start of the part's lifecycle. The <code>IWorkbenchPart.dispose</code> method
	 * will be called automically at the end of the lifecycle. Clients must not call this method.
	 * </p>
	 * 
	 * @param site The editor site.
	 * @param input The editor input. This should be an instance of the <code>DwpEditorInput</code>
	 * class. If it isn't, then a <code>PartInitException</code> is thrown.
	 * 
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input)
		throws PartInitException
	{
		// Test first to determine that editor input is valid.
		if (input instanceof DwpEditorInput)
		{
			setSite(site);
			setInput(input);
			setPartName(input.getName());
		}
		else
		{
			// Throw an exception.
			throw new PartInitException("Invalid input type: " + input.getName());
		}
	}

	/**
	 * Returns whether the contents of this part have changed since the last save operation.
	 * <p>
	 * If this value changes the part will fire a property listener event with PROP_DIRTY.
	 * </p>
	 * 
	 * @return <b>true</b> is returned if the contents have been modified and need saving.
	 * <b>false</b> is returned if they have not changed since the last save.
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	public boolean isDirty()
	{
		// Make sure that the tool exists before delegating.
		if (m_tool != null)
			return m_tool.isDirty();
		else
			return false;
	}

	/**
	 * Returns whether the "Save As" operation is supported by this part. 
	 * 
	 * @return <b>true</b> is returned if "Save As" is supported, and
	 * <b>false</b> will be returned if not supported.
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed()
	{
		return true;
	}

	/**
	 * Creates the DWP controls for this workbench part.
	 * <p>
	 * Clients should not call this method (the workbench calls this method when it needs to,
	 * which may be never).
	 * </p>
	 * 
	 * @param parent The parent <code>Control</code>.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent)
	{
		// Create a new DwpIdeFrame that is associated with an Eclipse IDE View.
		m_tool = new DwpTableIdeFrame(parent, SWT.BORDER);
		
		// Initialize the consoles.
		m_tool.getConsole().setPart(this);
		
		// Set up message handling
		m_msgQueueHandler = new ProcessMessageQueue(m_tool);
		m_tool.setMsgQueueHandler(m_msgQueueHandler);
		
		// Add the Menu Manager Actions.
		m_tool.addMenuActions(this);
		
		// Initialize the editor's input. This needs to be done here since we want
		// to associate the DWP domain table with the input (for marshalling/unmarshalling).
		DwpEditorInput input = (DwpEditorInput)getEditorInput();
		input.init(input.getName(),m_tool.getTable(),true);
		
		// Need to listen for modifications made to the underlying domain table.
		TableChangedListener m_listener = new TableChangedListener(this);
		m_tool.addTableChangedListener(m_listener);
		m_listener.addObserver(this);
		
		// Set the selection provider.
		getSite().setSelectionProvider(m_tool.getAttributeTreeViewer().getTreeViewer());
		
		// Mark the tool as being intialized.
		m_tool.setIsInitialized(true);
	}

	/**
	 * Set the focus on the <code>Viewer</code>.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus()
	{
		m_tool.setFocus();
	}
	
	/**
	 * Get the Tool associated with this editor.
	 * 
	 * @return A reference to a <code>DwpTableIdeFrame</code> is returned.
	 */
	public DwpTableIdeFrame getTool()
	{
		return m_tool;
	}
	
	/**
	 * Clean up and dispose of resources.
	 */
	public void dispose()
	{
	    DwpLog.getInstance().debug("Shutting down DWP Table Editor.");
		if (m_outlinePage != null)
		    m_outlinePage.dispose();
		m_tool.hideConsole();
	}
	
	/**
	 * Get the Object associated with the specified <i>adapter</i> class.
	 * 
	 * @param apater The required adapter class.
	 * 
	 * @return The associated Object is returned. If no association exists,
	 * then <b>null</b> will be returned.
	 */
	public Object getAdapter(Class adapter)
	{
	    if (IContentOutlinePage.class.equals(adapter))
	    {
	        if (m_outlinePage == null)
	        {
	            m_outlinePage = new DwpTableEditorContentOutlinePage(this);
	        }
	        return m_outlinePage;
	    }
	    return super.getAdapter(adapter);
	}

	/**
	 * This method is called whenever the observed object is changed.
	 * <p>
	 * The <code>DwpTableEditor</code> observes modifications made to the
	 * <code>DwpTableIdeFrame</code> via a <code>TableChangedListener</code>.
	 * </p>
	 * 
	 * @param obs The oberservable object.
	 * @param obj An argument passed by the observed object when
	 * <code>notifyObservers</code> is called.
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable obs, Object obj)
	{
		if (obs instanceof TableChangedListener)
		{
			if (obj instanceof Boolean)
			{
				boolean flag = ((Boolean)obj).booleanValue();
				if (flag)
				{
				    // Refresh the OutlineView
				    if (m_outlinePage != null)
				    {
				        m_outlinePage.refresh();
				    }
				    
				    // Fire the Property changed event.
					firePropertyChange(IEditorPart.PROP_DIRTY);
				}
			}
		}
	}

}
