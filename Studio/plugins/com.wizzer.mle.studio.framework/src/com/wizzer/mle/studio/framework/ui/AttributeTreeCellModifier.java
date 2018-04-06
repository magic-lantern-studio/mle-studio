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

// Import Eclipse packages.
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.CellEditor;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.ChoiceAttribute;
import com.wizzer.mle.studio.framework.attribute.FileAttribute;
import com.wizzer.mle.studio.framework.attribute.NumberAttribute;
import com.wizzer.mle.studio.framework.attribute.StringAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;

/**
 * This class implements an <code>ICellModifier</code> for an <code>AttributeTreeViewer</code>.
 * <p>
 * An <code>ICellModifier</code> is called when the user modifes a cell in a 
 * <code>AttributeTreeViewer</code> viewer.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class AttributeTreeCellModifier implements ICellModifier
{
	// A reference to the AttributeTreeViewer.
	private AttributeTreeViewer m_attributeTree;
	
	/**
	 * A constructor that associates the <code>AttributeTreeViewer</code> to monitor.
	 * 
	 * @param attributeTree An instance of the <code>AttributeTreeViewer</code>.
	 */
	public AttributeTreeCellModifier(AttributeTreeViewer attributeTree)
	{
		super();
		m_attributeTree = attributeTree;
	}
	
	// Prepare the Cell Editor for the specified attribute.
	private void prepCellEditor(Attribute attr)
	{
		// Prepare the Cell Editor for the specifed Attribute.
		CellEditor[] editors = m_attributeTree.getTreeViewer().getCellEditors();
		    	
		// Deactivate the previous Cell Editor.
		if (editors[1].isActivated()) {
			editors[1].deactivate();
			editors[1].dispose();
		}
		    	
		// Create a new Cell Editor with the appropriate underlying Cell Editor.
		AttributeTreeCellEditor.setAttributeType(attr);
		Composite parent = m_attributeTree.getTreeViewer().getTree();
		AttributeTreeCellEditor newEditor = new AttributeTreeCellEditor(parent);
		newEditor.setAttribute(attr);
		newEditor.hideLabel();
		editors[1] = newEditor;

	}

	/**
	 * Checks whether the given property of the given element can be modified
	 * 
	 * @param element An <code>Attribute</code> from the <code>AttributeTreeViewer</code>.
	 * @param property A property of the <code>AttributeTreeViewer</code>.
	 * 
	 * @return If the property of the specified element can be modified, then <>true<> will be
	 * returned. Otherwise <>false<> will be returned.
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property)
	{		
		boolean result = true;

		// Find the index of the column.
		int columnIndex = m_attributeTree.getColumnNames().indexOf(property);
		
		Attribute attr = (Attribute) element;
		
		// Can't edit the attribute name column.
		if (columnIndex == 0) {
		    result = false;
		} else {
			// Don't edit the tree node if it is read only.
			if (attr.isReadOnly())
			{
			    result = false;
			} else
			{
				// Prepare a new cell editor for the selected Attribute.
				prepCellEditor(attr);
			}
		}
		
		return result;
	}

	/**
	 * Get the value for the given property of the given element.
	 * 
	 * @param element An <code>Attribute</code> from the <code>AttributeTreeViewer</code>.
	 * @param property A property of the <code>AttributeTreeViewer</code>.
	 * 
	 * @return The value for the given property of the given element is returned.
	 * Returns <b>null</b> if the element does not have the given property.
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property)
	{
		// Find the index of the column.
		int columnIndex = m_attributeTree.getColumnNames().indexOf(property);

		Object result = null;
		Attribute attr = (Attribute) element;

		switch (columnIndex) {
			case 0 : // ATTRIBUTE_NAME_COLUMN
			    result = new String(attr.getName());
			    break;
			case 1 : // ATTRIBUTE_VALUE_COLUMN
			    String attrType = attr.getType();
			    if ((attrType == Attribute.TYPE_STRING) || (attrType == Attribute.TYPE_HEX_STRING))
			    {
					result = ((StringAttribute)attr).getValue();
			    } else if (attrType == Attribute.TYPE_INTEGER)
			    {
					result = new Long(((NumberAttribute)attr).getValue());
			    } else if (attrType == Attribute.TYPE_FILE)
			    {
					result = ((FileAttribute)attr).getValue();
			    } else if (attrType == Attribute.TYPE_CHOICE)
			    {
					// Returns the index of the current choice.
					result = new Integer(((ChoiceAttribute)attr).getValue());
			    } else if (attrType == Attribute.TYPE_VARIABLE_LIST)
			    {
					result = ((VariableListAttribute)attr).getValue();
			    }
			    
				break;
			default :
				result = "";
		}
		
		return result;	
	}

	/**
	 * Modifies the value for the given property of the given element.
	 * <p>
	 * Has no effect if the element does not have the given property, or if the property cannot be modified.
	 * <p> 
	 * Note that it is possible for an SWT Item to be passed instead of the model element.
	 * To handle this case in a safe way, use:
	 * 
     * <code>
     * if (element instanceof Item) {
     *     element = ((Item) element).getData();
     * }
     * // modify the element's property here
     * </code>
     * </p>
     *
	 * @param element An <code>Attribute</code> from the <code>AttributeTreeViewer</code>.
	 * @param property A property of the <code>AttributeTreeViewer</code>.
	 * @param value The new value.
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value)
	{
		// Find the index of the column.
		int columnIndex	= m_attributeTree.getColumnNames().indexOf(property);
			
		TreeItem item = (TreeItem) element;
		Attribute attr = (Attribute) item.getData();

		switch (columnIndex) {
			case 1 : // ATTRIBUTE_VALUE_COLUMN
				String attrType = attr.getType();
				if ((attrType == Attribute.TYPE_STRING) || (attrType == Attribute.TYPE_HEX_STRING))
				{
					((StringAttribute)attr).setValue(value);
				} else if (attrType == Attribute.TYPE_INTEGER)
				{
					((NumberAttribute)attr).setValue(value);
				} else if (attrType == Attribute.TYPE_FILE)
				{
					((FileAttribute)attr).setValue(value);
				} else if (attrType == Attribute.TYPE_CHOICE)
				{
					// Requires the index of the current choice.
					((ChoiceAttribute)attr).setValue(value);
				} else if (attrType == Attribute.TYPE_VARIABLE_LIST)
				{
					((VariableListAttribute)attr).setValue(value);
				}

				break;
			default :
		}
		
		// Notify the model change listeners that the attribute was modified.
		AttributeTreeModel model = (AttributeTreeModel)m_attributeTree.getTreeViewer().getContentProvider();	
		model.attributeChanged(attr);
	}
}
