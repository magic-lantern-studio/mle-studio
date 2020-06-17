/*
 * DwpItemTreeModel.java
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

// Import standard Java classes.
import java.util.HashSet;
import java.util.Set;

// Import Eclipse classes.
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

// Import Magic Lantern Tool Framework classes.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.domain.Table;
import com.wizzer.mle.studio.framework.ui.IAttributeViewer;

/**
 * This class implements a <code>ITreeContentProvider</code> for a
 * <code>TreeViewer</code> that displays the items in a Digital Workprint.
 * 
 * @author Mark S. Millard
 */
public class DwpItemTreeModel implements ITreeContentProvider
{
	/** The domain table. */
    protected Table m_domainTable;
    /** The set of listeners for change events. */
	protected Set m_changeListeners = new HashSet();

    /**
     * A constructor that assigns the domain table for the DWP Item Tree.
     * 
     * @param table The domain table.
     */ 
    public DwpItemTreeModel(Table table)
    {
        super();
        m_domainTable = table;
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
     * Disposes of this <code>DwpItemTreeModel</code> content provider.
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
     * Notifies this <code>DwpITemTreeModel</code> content provider that the given viewer's input has been
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
        //if (newInput != null)
        //    System.out.println("New input: " + newInput.toString());
        //if (oldInput != null)
        //    System.out.println("Old input: " + oldInput.toString());
    }

}
