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
package com.wizzer.mle.studio.framework.domain;

// Import standard Java packages.
import java.util.Vector;

// Import Tool Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.attribute.IAttributeCallback;


/**
 * This class is used to iterate over the <code>Attribute</code> tree captured by a
 * domain table.
 * 
 * @author Mark S. Millard
 * @created Nov 4, 2003
 */
public class TableIterator
{
	// The table to iterate over.
	private Table m_table = null;
	
	/**
	 * A constructor that will initialize which table to iterate over.
	 * 
	 * @param table The domain table.
	 */
	public TableIterator(Table table)
	{
		m_table = table;
	}
	
	/**
	 * Get an array of all child <code>Attribute</code>s of the specified node.
	 * 
	 * @param node The <code>Attribute</code> node from which to retrieve the children.
	 * 
	 * @return A array containing all child <code>Attribute</code>s.
	 */
	public Attribute[] getChildren(Attribute node)
	{
		Attribute[] children = null;
    	
		if (node.getChildCount() > 0) {
			children = new Attribute[node.getChildCount()];
			for (int i = 0; i < node.getChildCount(); i++) {
				children[i] = node.getChild(i);
			}
		}

		return children == null ? new Attribute[0] : children;
	}
	
	/**
	 * Find the first <code>Attribute</code> with the specified name.
	 * 
	 * @param name The name of the <code>Attribute</code> to match.
	 * @param node The node in the <code>Attribute</code> tree from which to begin searching.
	 * If <b>null</b>, then the search will begin at the top of the tree.
	 * 
	 * @return If an <code>Attribute</code> is found matching the specified name,
	 * then it will be returned.
	 * Otherwise, <b>null</b> will be returned if no match is found.
	 */
	public Attribute findAttribute(String name, Attribute node)
	{
		Attribute attr = null;
		Attribute[] children;
		
		// Start from the top of the Attribute Tree if node is <b>null</b>.
		if (node == null)
			node = m_table.getTopAttribute();
		
		attr = node.getChildByName(name);
		if (attr == null) {
		    // Recursively	walk the Attribute Tree looking for a match.
		    children = getChildren(node);
		    for (int i = 0; i < children.length; i++)
		    {
				attr = findAttribute(name,children[i]);
			    if (attr != null)
				    break;
		    }
		}
		
		return attr;
	}

	/**
	 * Find all <code>Attribute</code>s with the specified name.
	 * 
	 * @param name The name of the <code>Attribute</code> to match.
	 * @param node The node in the <code>Attribute</code> tree from which to begin searching.
	 * If <b>null</b>, then the search will begin at the top of the tree.
	 * 
	 * @return A <code>Vector</code> is reuturned containing references to all
	 * <code>Attribute</code>s that match the specified name throughout the
	 * <code>Attribute</code> tree hierarchy.
	 */
	public Vector findAttributes(String name, Attribute node)
	{
		Attribute attr = null;
		Attribute[] children;
		Vector foundAttr = new Vector();
		
		// Start from the top of the Attribute tree if node is <b>null</b>.
		if (node == null)
			node = m_table.getTopAttribute();
		
		// Add this node if name matches.
		if (node.getName().equals(name))
		    foundAttr.add(node);

		// Search children for matches.
		for (int i = 0; i < node.getChildCount(); i++)
		{
			Attribute childNode = node.getChild(i);
			Vector attrs = findAttributes(name,childNode);
			
			// Add the children nodes that were found.
			if (attrs.size() > 0)
			    foundAttr.addAll(attrs);
		}
		
		return foundAttr;
	}
	
	/**
	 * Iterate over the table, applying the callback to each node in the
	 * <code>Attribute</code> tree.
	 * 
	 * @param node The node in the <code>Attribute</code> tree from which to begin searching.
	 * If <b>null</b>, then the search will begin at the top of the tree.
	 * @param cb The <code>Attribute</code> callback that will be applied as
	 * the <code>Attribute</code> tree is traversed.
	 */
	public void apply(IAttribute node, IAttributeCallback cb)
	{
		// Start from the top of the Attribute tree if node is <b>null</b>.
		if (node == null)
			node = m_table.getTopAttribute();
		
		// Call setup for callback.
		cb.prefix(node,cb.getData());

		// Apply the callback.
		cb.callback(node,cb.getData());

		// Traverse the Attribute tree, applying the callback.
		for (int i = 0; i < node.getChildCount(); i++)
		{
			IAttribute childNode = node.getChild(i);
			apply(childNode,cb);
		}
		
		// Call teardown for callback.
		cb.postfix(node,cb.getData());
	}

}
