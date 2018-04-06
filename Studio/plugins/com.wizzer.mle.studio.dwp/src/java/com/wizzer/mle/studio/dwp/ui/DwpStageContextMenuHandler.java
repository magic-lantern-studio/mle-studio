/*
 * DwpStageContextMenuHandler.java
 * Created on Apr 14, 2005
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
package com.wizzer.mle.studio.dwp.ui;

// Import Eclipse classes.
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

// Import Magic Lantern Tool Framework classes.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.ui.AttributeTreeViewer;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpPackageAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSetAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpStageAttribute;

/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributetreeViewer</code> for a <code>DwpStageAttribute</code>.
 *
 * @author Mark S. Millard
 */
public class DwpStageContextMenuHandler extends AbstractItemContextMenuHandler
{
	// A menu item for adding a Package item.
	private MenuItem m_popupAddPackageItem = null;
	// A menu item for adding an Set DWP item.
	private MenuItem m_popupAddSetItem = null;
	// The current count of property defs.
	private int m_setCount = 0;

	/**
     * The default constructor.
     */
    public DwpStageContextMenuHandler()
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
	    super.init(viewer);
		this.createContextMenu();
	}

    /** Create the context menu. */
	protected void createContextMenu()
	{
		// Create a context menu for adding elements to the Attribute tree.
		m_popupMenu = new Menu(m_viewer.getControl().getShell(),SWT.POP_UP);

		// Add menu item for adding Set DWP item.
		m_popupAddSetItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddSetItem.setText("Add DWP Set Item");
		m_popupAddSetItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_SET);
				}
			});

		// Add menu item for adding PropertyDef DWP item.
		/*
		m_popupAddPropertyDefItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddPropertyDefItem.setText("Add DWP PropertyDef Item");
		m_popupAddPropertyDefItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTYDEF);
				}
			});
		*/

		super.createContextMenu();
	}

	/**
	 * Process the "Add List Element" action.
	 * 
	 * @param event The selection event that caused this handler to be invoked.
	 * @param type The type of <code>Attribute</code> to add.
	 */
	public void popupAddListElementActionPerformed(Event event, String type)
	{
		DwpStageAttribute stageDef = (DwpStageAttribute)m_viewer.getSelectedAttribute();
		
		if (type == DwpItemAttribute.TYPE_DWP_SET)
		{
		    DwpSetAttribute set = new DwpSetAttribute("set" +
		        m_setCount++, "MySet", false);
		    stageDef.addChild(set,m_viewer.getTable());
		} if (type == DwpItemAttribute.TYPE_DWP_PACKAGE)
		{
		    DwpPackageAttribute attr = new DwpPackageAttribute("org", false);
		    stageDef.addChild(attr,m_viewer.getTable());
		}
	}

	/**
	 * Get a context <code>Menu</code> for the specified <code>IAttribute</code>.
	 * 
	 * @param attribute The <code>IAttribute</code> to create a context menu for.
	 * 
	 * @return A reference to a <code>Menu</code> is returned.
	 */
    public Menu getContextMenu(IAttribute attribute, AttributeTreeViewer viewer)
    {
		Menu popup = null;
		
		String type = attribute.getType();
		IAttribute parent = attribute.getParent();
		String parentType = parent.getType();
		
		if (type == DwpStageAttribute.TYPE_DWP_STAGE)
		{
			init(viewer);
			
			Attribute packageAttr = ((DwpStageAttribute)attribute).getChildByName("Package");
			if (packageAttr != null)
			{
			    // Already have a package, disable menu entry.
			    m_popupAddPackageItem.setEnabled(false);
			}

			popup = m_popupMenu;
		}
		
		return popup;
    }

}
