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

// Import standard Java packages.
import java.util.Observer;
import java.util.Observable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// Import Eclipse packages.
import org.eclipse.jface.viewers.ITreeContentProvider;

// Import Eclipse packages.
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.viewers.Viewer;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.NumberAttribute;
import com.wizzer.mle.studio.framework.attribute.StringAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;
import com.wizzer.mle.studio.framework.domain.Table;

/**
 * This class implements a <code>ITreeContentProvider</code> for a
 * <code>TreeViewer</code>
 * 
 * @author Mark S. Millard
 */
public class AttributeTreeModel implements ITreeContentProvider,Observer
{
	/** The domain table. */
    protected Table m_domainTable;
    /** The set of listeners for change events. */
	protected Set m_changeListeners = new HashSet();

    /**
     * A constructor that assigns the domain table for the Attribute Tree.
     * 
     * @param table The domain table.
     */ 
    public AttributeTreeModel(Table table)
    {
        m_domainTable = table;
        table.addObserver(this);
    }
    
    /**
     * Get the Attribute Tree root.
     * 
     * @return An <code>Attribute</code> representing the root of the tree is returned.
     */
    public Attribute getRoot()
    {
    	return m_domainTable.getTopAttribute();
    }

    // Define methods from ITreeContentProvider.

    /**
     * Returns the child elements of the given parent element. 
     * <p>
     * The difference between this method and <code>IStructuredContentProvider.getElements()</code> is that
     * <code>getElements()</code> is called to obtain the tree viewer's root elements,
     * whereas <code>getChildren()</code> is used to obtain the children
     * of a given parent element in the tree (including a root). 
     * </p><p>
     * The result is not modified by the viewer. 
     * </p>
     * 
     * @param element The parent element.
     * 
     * @return An array of child elements.
     */
    public Object[] getChildren(Object element)
    {
    	Object[] children = null;
    	Attribute node = (Attribute) element;
    	
    	if (node.getChildCount() > 0) {
    	    children = new Object[node.getChildCount()];
    	    for (int i = 0; i < node.getChildCount(); i++) {
    		    children[i] = node.getChild(i);
    	    }
    	}

    	return children == null ? new Object[0] : children;
    }
    
    /**
     * Returns the elements to display in the viewer when its input is set to the given element.
     * <p>
     * These elements can be presented as rows in a table, items in a list, etc.
     * The result is not modified by the viewer.
     * </p>
     * 
     * @param element The input element.
     * 
     * @return The array of elements to display in the viewer is returned.
     */
    public Object[] getElements(Object element)
    {
    	return this.getChildren(element);
    }
    
    /**
     * Returns whether the given element has children.
     * <p>
     * Intended as an optimization for when the viewer does not need the actual children.
     * Clients may be able to implement this more efficiently than <code>getChildren()</code>.
     * </p>
     * 
     * @param element The element to test for the existance of children.
     * 
     * @return <b>true</b> is returned if the given element has children, and
     * <b>false</b> is returned if it has no children.
     */
    public boolean hasChildren(Object element)
    {
    	return ((Attribute) element).getChildCount() > 0;
    }
    
    /**
     * Returns the parent for the given element, or null indicating that the parent can't be computed.
     * <p>
     * If null is returned, then the tree-structured viewer can't expand a given node correctly if requested.
     * </p>
     * 
     * @param element The element to retrieve the parent of.
     * 
     * @return The parent element, or null if it has none or if the parent cannot be computed.
     */
    public Object getParent(Object element)
    {
    	return ((Attribute) element).getParent();
    }
    
    /**
     * Disposes of this <code>AttributeTreeModel</code> content provider.
     * <p>
     * This is called by the viewer when it is disposed. 
     * The viewer should not be updated during this call, as it is in the process of being disposed. 
     * </p>
     */
    public void dispose()
    {
    	// Does nothing for now.
    }
    
    /**
     * Notifies this <code>AttributeTreeModel</code> content provider that the given viewer's input has been
     * switched to a different element. 
     * <p>
     * A typical use for this method is registering the content provider as a listener to changes
     * on the new input (using model-specific means), and deregistering the viewer from the old input.
     * In response to these change notifications, the content provider should update the viewer
     * (see the add, remove, update and refresh methods on the viewers). 
     * </p><p>
     * The viewer should not updated during this call, as it might be in the process of being disposed.
     * 
     * @param viewer The viewer.
     * @param oldInput The old input element, or null if the viewer did not previously have an input.
     * @param newInput The new input element, or null if the viewer does not have an input.
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
/*
		if (newInput != null)
			//((Attribute) newInput).addChangeListener(this);
		    addAttribute((Attribute) newInput);
		if (oldInput != null)
			//((Attribute) oldInput).removeChangeListener(this);
            removeAttribute((Attribute) oldInput);
*/
    }

	/**
	 * Notify the change listeners that the specified attribute was changed.
	 * 
	 * @param attr The <code>Attribute</code> that caused this change notification.
	 */
	public void attributeChanged(Attribute attr)
	{
		Iterator iterator = m_changeListeners.iterator();
		while (iterator.hasNext())
			((IAttributeViewer) iterator.next()).updateAttribute(attr);
	}

	/**
	 * Remove the specified Attribute Viewer.
	 * 
	 * @param viewer The change listener to remove.
	 */
	public void removeChangeListener(IAttributeViewer viewer)
	{
		m_changeListeners.remove(viewer);
	}

	/**
	 * Add the specified Attribute Viewer.
	 * 
	 * @param viewer The change listener to add.
	 */
	public void addChangeListener(IAttributeViewer viewer)
	{
		m_changeListeners.add(viewer);
	}

	/**
	 * Update the Attribute Model if necessary.
	 * 
	 * @param obs The <code>Observable</code> object, in this case the Simulator
	 * domain table.
	 * @param obj The object being observed, in this case is should be an Attribute.
	 */
	public void update(Observable obs, Object obj)
	{
		Table table = (Table) obs;

		// If this is a NEW attribute root, update the tree.
		if (getRoot() != table.getTopAttribute())
		{
			//this.updateTree(table);
			attributeChanged(table.getTopAttribute());
		} else if (obj != null) {
			Attribute attr = (Attribute) obj;
			attributeChanged(attr);
		}
        
		// XXX - need to update the display here somehow.
		//m_tableTree.layout();
	}

    // A unit test for AttributeTreeModel.
    static public void main(String[] args)
    {
    	class MyTable extends Table
    	{    	
			class MyTreeNodeAttribute extends VariableListAttribute implements Observer
			{
				MyTreeNodeAttribute(String menuName, NumberAttribute countAttribute, String functionName, Object theCaller)
				{ super(menuName,countAttribute,functionName,null,theCaller); }

				public void update(Observable o, Object arg) {} // Do nothing for now.
    		
				public String toString() { return getName();}
			}
    	
			class MyStringAttribute extends StringAttribute implements Observer
			{
				MyStringAttribute(String name, String value, int characters, boolean isReadOnly)
				{ super(name,value,characters,isReadOnly); }

				public void update(Observable o, Object arg) {} // Do nothing for now.
    		
				public String toString() { return getName();}
			}

    		public Attribute buildDefaultInstance()
    		{
				// Create some Attributes.
				MyTreeNodeAttribute root = new MyTreeNodeAttribute("Root",
					new NumberAttribute("Count",6,32,10,false),"rootAttributeTreeModel",null);
				MyTreeNodeAttribute subTree = new MyTreeNodeAttribute("SubTree",
					new NumberAttribute("Count",4,32,10,false),"subtreeAttributeTreeModel",null);
				for (int i = 0; i < 4; i++) {
					String name = new String("SubAttribute_" + i);
					String value = new String("Value_" + i);
					MyStringAttribute attr = new MyStringAttribute(name,value,value.length(),false);
					subTree.addChild(attr,attr);
				}
				root.addChild(subTree,subTree);
				for (int i = 0; i < 5; i++) {
					String name = new String("Attribute_" + i);
					String value = new String("Value_" + i);
					MyStringAttribute attr = new MyStringAttribute(name,value,value.length(),false);
					root.addChild(attr,attr);
				}
				
				// Set the top of the Attribute Tree.
				m_top = root;
				
				// And return the root of the Attribute Tree.
				return root;
    		}

			public void update(Observable o, Object arg) {} // Do nothing for now.
    	}
    	
    	class AttributeViewer extends ApplicationWindow
        {
        	AttributeViewer() { super(null); }
        	
        	protected Control createContents(Composite parent)
        	{
				getShell().setText("AttributeTreeModel Unit Test");

				// Create a new Domain Model.
				MyTable table = new MyTable();

				// Create an Attribute Tree Model
				AttributeTreeModel treeModel = new AttributeTreeModel(table);
		
				// Create the Tree Viewer
				TreeViewer tv = new TreeViewer(parent);
				tv.setContentProvider(treeModel);
				tv.setInput(table.getTopAttribute());
       		
       		    return tv.getTree();
        	}
        }
        		
		AttributeViewer viewer = new AttributeViewer();
		viewer.setBlockOnOpen(true);
		viewer.open();
		Display.getCurrent().dispose();
	}
}

