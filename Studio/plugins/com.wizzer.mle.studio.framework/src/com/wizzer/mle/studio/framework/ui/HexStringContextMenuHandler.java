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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.HexStringAttribute;
import com.wizzer.mle.studio.framework.attribute.IAttribute;


/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributetreeViewer</code> for a <code>HexStringAttribute</code>.
 *
 * @author Mark S. Millard
 */
public class HexStringContextMenuHandler implements IAttributeContextMenuHandler
{
	// The AttributeTreeViewer.
	AttributeTreeViewer m_viewer = null;
	// The menu.
	private Menu m_popupChangeDisplay = null;
	// A menu item.
	private MenuItem m_popupChangeHexStringDisplay = null;

	/**
	 * The default constructor.
	 */
	public HexStringContextMenuHandler()
	{
		super();
	}
	
	/**
	 * Initialize the handler.
	 * <p>
	 * This must be called prior to getting the context menu.
	 * </p>
	 * 
	 * @param viewer The <code>AttributeTreeViewer</code> that will use the
	 * context menu.
	 */
	public void init(AttributeTreeViewer viewer)
	{
		createContextMenu(viewer);
	}

	// Create the context menu.
	private void createContextMenu(AttributeTreeViewer viewer)
	{
		m_viewer = viewer;

		// Create a context menu for deleting elements from the Attribute tree.
		m_popupChangeDisplay = new Menu(viewer.getControl().getShell(),SWT.POP_UP);
		m_popupChangeHexStringDisplay = new MenuItem(m_popupChangeDisplay,SWT.PUSH);
		m_popupChangeHexStringDisplay.setText("Change Display");
		m_popupChangeHexStringDisplay.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupChangeDisplayActionPerformed(e);
				}
			});
	}

	/**
	 * Process the "Change Display" action.
	 * 
	 * @param event The selection event that caused this handler to be invoked.
	 */
	public void popupChangeDisplayActionPerformed(Event event)
	{
		// Get the current display type.
		HexStringAttribute hsa = (HexStringAttribute) m_viewer.getSelectedAttribute();
		if ( hsa.isHexStringDisplay() )
		{
			hsa.setCharacterStringDisplay();
		}
		else
		{
			hsa.setHexStringDisplay();
		}
	}
		
	/**
	 * Get a context <code>Menu</code> for the specified <code>IAttribute</code>.
	 * 
	 * @param attribute The <code>IAttribute</code> to create a context menu for.
	 * 
	 * @return A reference to a <code>Menu</code> is returned.
	 */
	public Menu getContextMenu(IAttribute attribute,AttributeTreeViewer viewer)
	{
		Menu popup = null;
		
		String type = attribute.getType();
		IAttribute parent = attribute.getParent();
		String parentType = parent.getType();
		
		if (type == Attribute.TYPE_HEX_STRING)
		{
			init(viewer);
			popup = m_popupChangeDisplay;

			HexStringAttribute hsa = (HexStringAttribute)attribute;
			if ( hsa.isHexStringDisplay() )
			{
				m_popupChangeHexStringDisplay.setText("Change Display to Characters");
			}
			else
			{
				m_popupChangeHexStringDisplay.setText("Change Display to Hexadecimal");
			}
		}
		
		return popup;
	}
}
