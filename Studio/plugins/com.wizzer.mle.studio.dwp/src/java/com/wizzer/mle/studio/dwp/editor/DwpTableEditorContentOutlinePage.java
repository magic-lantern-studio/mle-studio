/*
 * DwpTableEditorContentOutlinePage.java
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

// Import Eclipse classes.
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
//import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.wizzer.mle.studio.framework.attribute.Attribute;

/**
 * This class provides a <code>ContentOutlinePage</code> for the <code>DwpTableEditor</code>.
 * 
 * @author Mark S. Millard
 */
public class DwpTableEditorContentOutlinePage extends ContentOutlinePage
{
    // The DWP Table Editor.
    private DwpTableEditor m_editor = null;
    // Flag indicating whether to update the editor selection.
    private boolean m_updateEditor = true;
    
    /**
     * A constructor that initializes the DWP Table Editor.
     * 
     * @param editor The DWP Table Editor to associcate with this
     * <code>ContentOutlinePage</code>.
     */
    public DwpTableEditorContentOutlinePage(DwpTableEditor editor)
    {
        super();
        m_editor = editor;
    }

    /**
     * Create the control for the <code>TabaleViewer</code>.
     * 
     * @param The parent <code>Composite</code> widget.
     */
    public void createControl(Composite parent)
    {
    	super.createControl(parent);

    	// Get the viewer for the OutlineView.
    	TreeViewer viewer = getTreeViewer();
    	// Set the content provider.
    	viewer.setContentProvider(new DwpItemTreeModel(m_editor.getTool().getTable()));
    	// Set the label provider.
    	viewer.setLabelProvider(new DwpItemTreeLabelProvider());
    	// Add a listener for selections made in the OutlineView.
    	viewer.addSelectionChangedListener(this);
    	
    	// Register a selection changed listener for selections made by
    	// the editor.
    	TreeViewer editorViewer = m_editor.getTool().getAttributeTreeViewer().getTreeViewer();
    	editorViewer.addSelectionChangedListener(
	        new ISelectionChangedListener()
	        {
	            public void selectionChanged(SelectionChangedEvent event)
	            {
	                handleSelectionChanged(event);
	            }
	        });


    	// Tell the Tree Viewer where the top of the model is.
    	viewer.setInput(m_editor.getTool().getTable().getTopAttribute());
    	
    	// Expand the tree.
    	viewer.expandAll();
    }

	/**
	 * Handle the selection changed event caused by the DwpTableEditor.
	 * 
	 * @param event The event causing this handler to be invoked.
	 */
	protected void handleSelectionChanged(SelectionChangedEvent event)
	{
	    ISelection selection = event.getSelection();
        if (! selection.isEmpty())
        {
            System.out.println("Handling selection changed event from table editor.");
            
            // Open the editor viewer on the selected attribute.
            if (selection instanceof StructuredSelection)
            {
                // Get the selected Attribute.
                Attribute attr = (Attribute)((StructuredSelection)selection).getFirstElement();
                // Expand the viewer to the selected Attribute.
                m_updateEditor = false;
                getTreeViewer().setSelection(selection);
                m_updateEditor = true;
            }
        }
	}
	
    /**
     * Handle the selection changed event caused by the OutlineView.
     * 
     * @param event The event causing this handler to be invoked.
     */
	public void selectionChanged(SelectionChangedEvent event)
    {
        if (! m_updateEditor)
            return;
        
        super.selectionChanged(event);
        
        ISelection selection= event.getSelection();
        if (! selection.isEmpty())
        {
            //System.out.println("Setting highlight on table editor.");
            
            // Get the editor's associated TableTreeViewer.
            TreeViewer viewer = m_editor.getTool().getAttributeTreeViewer().getTreeViewer();
            // Select the Attribute in the TableTreeViewer. By default,
            // the tree will be expanded to show the new selection.
            viewer.setSelection(selection,true);

        }
    }
	
	/**
	 * Refresh the viewer.
	 */
	public void refresh()
	{
	    getTreeViewer().refresh();
	}
}
