/*
 * DwpTableEditorContentOutlinePage.java
 * Created on May 18, 2005
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
