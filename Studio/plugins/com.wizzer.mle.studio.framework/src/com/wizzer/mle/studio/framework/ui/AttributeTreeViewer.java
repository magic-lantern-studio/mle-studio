// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
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
package com.wizzer.mle.studio.framework.ui;

//import javax.swing.JTree;

// Import standard Java packages.
import java.util.Arrays;

// Import Eclipse packages.
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Tree;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.FrameworkPlugin;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.domain.Table;
import com.wizzer.mle.studio.framework.ui.AttributeTreeCellEditor;

/**
 * This class manages the GUI for a domain table in the form of a <code>TableTreeViewer</code>.
 * 
 * @author Mark S. Millard
 */
public class AttributeTreeViewer extends Composite implements IAttributeViewer
{
    private Table m_table           = null;
    private Tree m_tree             = null;
	private TreeViewer m_treeViewer = null;
	
	// Set the table column property names.
	private final String ATTRIBUTE_NAME_COLUMN  = "name";
	private final String ATTRIBUTE_VALUE_COLUMN = "value";

	// Set column names.
	private String[] m_columnNames = new String[] {
		ATTRIBUTE_NAME_COLUMN,
		ATTRIBUTE_VALUE_COLUMN
		};

    /**
     * A constructor that associates a domain table with the GUI
     * attribute tree browser.
     * 
     * @param parent The parent <code>Composite</code> widget.
     * @param style The style to use for this widget.
     * @param table A domain table that will be displayed in this
     * attribute tree.
     */
    public AttributeTreeViewer(Composite parent, int style, Table table)
    {
        super(parent, style);
        
        // Initialize fields.
        m_table = table;
        
        // Load Attribute extensions, if necessary.
        FrameworkPlugin plugin = FrameworkPlugin.getDefault();
        if (plugin != null)
        {
            plugin.getAttributeCellEditors();
			plugin.getAttributeContextMenuHandlers();
        }
        
        // Initialize the tree GUI components.
        initGUI(this);
        
        // Update the attribute tree.
        this.updateTree(table);
        
        this.setVisible(true);
        this.layout();
    }

    /**
     * Update the <code>TableTreeViewer</code> based on the new domain <code>Table</code>.
     * 
     * @param table The new <code>Table</code> to update from.
     */
    public void updateTree(Table table)
    {
    	// Set the content provider.
        AttributeTreeModel treeModel = new AttributeTreeModel(table);
        m_treeViewer.setContentProvider(treeModel);
        
        // Add the viewer as a listener to the model so that the model can update it
        // when modifications are made to the model.
        treeModel.addChangeListener(this);
        
        // Tell the Tree Viewer where the top of the model is.
        m_treeViewer.setInput(table.getTopAttribute());
        
        // Hack to set height of row.
		TreeItem item = m_tree.getTopItem();
		if (item == null)
			item = new TreeItem(m_tree,SWT.NONE);
		Image nullImage = new Image(m_tree.getDisplay(),26,
			GuiConstants.PANEL_HEIGHT_EDIT + (GuiConstants.BORDER_SIZE_EDIT * 2) + 14);
		item.setImage(1,nullImage);
		nullImage.dispose();
		nullImage = null;
		item.setImage(1,nullImage);
    }

	/**
	 * Get the <code>Table</code>.
	 * 
	 * @return The <code>Table</code> is returned.
	 */
	public Table getTable()
	{
		return m_table;
	}

	/**
	 * Get the <code>Tree</code>.
	 * 
	 * @return The <code>Tree</code> is returned.
	 */
	public Tree getTree()
	{
		return m_tree;
	}

    /**
     * Get the <code>TreeViewer</code>.
     * 
     * @return The <code>TreeViewer</code> is returned.
     */
    public TreeViewer getTreeViewer()
    {
        return m_treeViewer;
    }
    
    /*
     * Initialize the GUI.
     * 
     * @param parent The parent <code>Composite</code> widget.
     */
    private void initGUI(Composite parent)
    {
		// Set numColumns to 3 for additional buttons.
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 4;
		parent.setLayout(layout);

		// Create the table tree laying out the columns.
		createTableTree(parent);
		
		// Create and setup the Table Tree Viewer.
		createTableTreeViewer();
 
        // Add additional listeners for managing the Popup Menus in context.  
        this.addListeners();
    }

	/*
	 * Create the TableTree.
	 */
	private void createTableTree(Composite parent)
	{
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
					SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		m_tree = new Tree(parent, style);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		//gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		m_tree.setLayoutData(gridData);		
					
		m_tree.setLinesVisible(true);
		m_tree.setHeaderVisible(true);

		// 1st column shows attribute hierarchy.
		TreeColumn column = new TreeColumn(m_tree, SWT.CENTER, 0);		
		column.setText("Attribute Name");
		column.setWidth(GuiConstants.LABEL_WIDTH_EDIT);
		
		// 2nd column shows attribute value.
		column = new TreeColumn(m_tree, SWT.LEFT, 1);
		column.setText("Attribute Value");
		column.setWidth(GuiConstants.EDIT_WIDTH_EDIT + 50);  // XXX - Why 50 pixels?

		// Add handler for sizing tree item's height
		m_tree.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.height = GuiConstants.PANEL_HEIGHT_EDIT + (GuiConstants.BORDER_SIZE_EDIT * 2) + 2;
			}
		});
	}

	/*
	 * Create the TreeViewer.
	 */
	private void createTableTreeViewer()
	{
		m_treeViewer = new TreeViewer(m_tree);
		m_treeViewer.setUseHashlookup(true);
		
		m_treeViewer.setColumnProperties(m_columnNames);

		// Create the cell editors.
		CellEditor[] editors = new CellEditor[m_columnNames.length];

		// No cell editor for Column 1.
		editors[0] = null;

		// Column 2 : A generic Attribute Cell Editor.
		editors[1] = new AttributeTreeCellEditor(m_tree,SWT.NONE);
						
		// Assign the cell editors to the viewer.
		m_treeViewer.setCellEditors(editors);
		
		// Set the cell modifier for the viewer.
		m_treeViewer.setCellModifier(new AttributeTreeCellModifier(this));

        // Set the cell label provider for the viewer.
        m_treeViewer.setLabelProvider(new AttributeTreeLabelProvider());

		// There is no default sorter or filter for the viewer.
	}

	/**
	 * Return the column names in a collection.
	 * 
	 * @return A <code>List</code> is returned containing the column names.
	 */
	public java.util.List getColumnNames()
	{
		return Arrays.asList(m_columnNames);
	}
	
	/**
	 * Release resources.
	 */
	public void dispose()
	{		
		// Tell the label provider to release its resources.
		m_treeViewer.getLabelProvider().dispose();
	}

    /*
     * Handle the Context Menu if the selected attribute has one registered.
     * 
     * @param e The Mouse Event that caused this handler to be called back.
     */
    private void handleAttributeContextMenu(MouseEvent e)
    {
        Menu popup = null;
        
        //System.out.println("Button Event: " + new Integer(e.button));
        
		// Ignore all buttons except for 3.
        if (e.button != 3)
        {
        	// Clear the context menu and then exit.
			m_treeViewer.getTree().setMenu(popup);
            return;
        }

		// Check if an Attribute has been selected.
        Attribute selectedAttribute = this.getSelectedAttribute();
        if (selectedAttribute == null)
        {
        	// Nothing selecteed, exit.
            return;
        }
        //System.out.println("Context menu for " + this.selectedAttribute.getName());
        
		// Check to see if an old context menu exists from a previous Attribute.
		popup = m_treeViewer.getTree().getMenu();
		if (popup != null)
		{
			popup.setVisible(false);
			m_treeViewer.getTree().setMenu(null);
		}

		// Using the Eclipse plugin, retrieve the context menu from registry.
		AttributeContextMenuHandlerRegistry registry = AttributeContextMenuHandlerRegistry.getInstance();
		if (registry.hasHandler(selectedAttribute.getType()))
		{
			// Retrieve the menu.
			popup = registry.getContextMenu(selectedAttribute,this);
		} else
		{
			// Clean up after check for previous context menu.
			popup = null;
		}
		
        // Process the context menu.
        if (popup != null)
        {
        	//System.out.println("Setting context menu for " + selectedAttribute.getName());
            m_treeViewer.getTree().setMenu(popup);
            popup.setVisible(true);
        }
    }
    
	/**
	 * Handle the Selection Changed event.
	 * 
	 * @param event The selection event that caused this handler to be invoked.
	 */
	public void handleSelectionChanged(SelectionChangedEvent event)
	{
		Attribute selectedAttribute = this.getSelectedAttribute();

		// Check if an Attribute has been selected.
		if (selectedAttribute == null)
		{
			return;
		}

        if (m_treeViewer.isCellEditorActive())
        {
        	// Note: we can't call m_tableTreeViewer.cancelEditing() here because it will cancel the
        	// selected Attribute Cell Editor which we want to be active upon selection of a cell.
        	
	        CellEditor[] editors = m_treeViewer.getCellEditors();
	        if (selectedAttribute != ((AttributeTreeCellEditor)editors[1]).getAttribute()) {
	            editors[1].deactivate();
	            // Note: Don't dispose the editor just yet, just deactivate it.
	        }
        }
	}
	
    /**
     * Handle the Mouse Released event.
     * 
     * @param event The mouse event that caused this handler to be invoked.
     */
    public void treeMouseReleased(MouseEvent event)
    {
        this.handleAttributeContextMenu(event);
    }

	/**
	 * Return the parent composite.
	 * 
	 * @return The parent <code>Control</code> is returned.
	 */
	public Control getControl()
	{
		return m_tree.getParent();
	}

    /**
     * Retrieve the selected <code>Attribute</code>.
     * 
     * @return The selected <code>Attribute</code> is returned. If the selection is empty,
     * then <b>null</b> will be returned.
     */
    public Attribute getSelectedAttribute()
    {
    	IStructuredSelection selectedItems = (IStructuredSelection)m_treeViewer.getSelection();
    	
        // Check to see if any items were selected.
        if (selectedItems.isEmpty())
        {
            return null;
        }
        
        return (Attribute)selectedItems.getFirstElement();
    }

    /**
     * Retrieve the index of the selectd <code>Attribute</code>, relative to its parent.
     * 
     * @return The index of the selected Attribute will be returned. If the Attribute is
     * not retrievable, then <b>-1</b> will be returned.
     */
    public int getSelectedSubIndex()
    {
    	// Get the selected Attribute.
        Attribute child = this.getSelectedAttribute();
        
        // Find the selected Attribute's parent. Should this be obtained via the GUI?
        Attribute parent = child.getParent();
        
        // Find the index of the child relative to its parent.
        for (int i = 0; i < parent.getChildCount(); i++)
        {
            if (parent.getChild(i) == child)
            {
                System.out.println("The index to be deleted is: " + i);
                return i;
            }
        }

        return -1;
    }

    /*
     * Add listeners for monitored events.
     */
    private void addListeners()
    {
        m_tree.addMouseListener(
            new MouseAdapter()
            {
                public void mouseUp(MouseEvent event)
                {
                    treeMouseReleased(event);
                }
            });
       
       m_treeViewer.addSelectionChangedListener(
           new ISelectionChangedListener()
           {
			    public void selectionChanged(SelectionChangedEvent event)
			    {
			    	handleSelectionChanged(event);
			    }
           });
           
		m_treeViewer.addTreeListener(
			new ITreeViewerListener()
			{
				public void treeCollapsed(TreeExpansionEvent event)
				{
					if (m_treeViewer.isCellEditorActive()) {
						m_treeViewer.cancelEditing();
					}
				}
				public void treeExpanded(TreeExpansionEvent event)
				{
					if (m_treeViewer.isCellEditorActive()) {
						m_treeViewer.cancelEditing();
					}
				}
			});
    }

	// Define methods from IAttributeViewer.
		
	/**
	 * Update the viewer to reflect the fact that an attribute was added 
	 * to the model.
	 * 
	 * @param attr The <code>Attribute</code> to add to the view.
	 */
	public void addAttribute(Attribute attr)
	{
		Object parent = null;
		m_treeViewer.add(parent,attr);
	}
	
	/**
	 * Update the viewer to reflect the fact that an attribute was removed 
	 * from the model.
	 * 
	 * @param attr The <code>Attribute</code> to remove from the view.
	 */
	public void removeAttribute(Attribute attr)
	{
		m_treeViewer.remove(attr);	
	}
	
	/**
	 * Update the viewer to reflect the fact that one of the attributes
	 * was modified.
	 * 
	 * @param attr The <code>Attribute</code> that was modified.
	 */
	public void updateAttribute(Attribute attr)
	{
		//m_treeViewer.update(attr,null);
		m_treeViewer.refresh(attr);
	}

}
