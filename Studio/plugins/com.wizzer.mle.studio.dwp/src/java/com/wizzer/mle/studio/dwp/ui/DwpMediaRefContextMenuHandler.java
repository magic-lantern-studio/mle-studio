/*
 * DwpMediaRefContextMenuHandler.java
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
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.ui.AttributeTreeViewer;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaRefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaRefSourceAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaRefTargetAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpPackageAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSceneAttribute;

/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributetreeViewer</code> for a <code>DwpMediaRefAttribute</code>.
 *
 * @author Mark S. Millard
 */
public class DwpMediaRefContextMenuHandler extends AbstractItemContextMenuHandler
{
	// A menu item for adding a Package DWP item.
	private MenuItem m_popupAddPackageItem = null;
	// A menu item for adding a MediaRef Source DWP item.
	private MenuItem m_popupAddMediaRefSourceItem = null;
	// A menu item for adding an MediaRef Target DWP item.
	private MenuItem m_popupAddMediaRefTargetItem = null;
	// The current count of MediaRef Targets.
	private int m_mrefTargetCount = 0;

	/**
     * The default constructor.
     */
    public DwpMediaRefContextMenuHandler()
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

    /**
     * Create the context menu.
     */
	protected void createContextMenu()
	{
		// Create a context menu for adding elements to the Attribute tree.
		m_popupMenu = new Menu(m_viewer.getControl().getShell(),SWT.POP_UP);

		// Add menu item for adding Package DWP item.
		m_popupAddPackageItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddPackageItem.setText("Add DWP Package Item");
		m_popupAddPackageItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PACKAGE);
				}
			});
		
		// Add menu item for adding MediaRef Source DWP item.
		m_popupAddMediaRefSourceItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddMediaRefSourceItem.setText("Add DWP MediaRef Source Item");
		m_popupAddMediaRefSourceItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_MEDIAREFSOURCE);
				}
			});
		
		// Add menu item for adding Group DWP item.
		m_popupAddMediaRefTargetItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddMediaRefTargetItem.setText("Add DWP MediaRef Target Item");
		m_popupAddMediaRefTargetItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_MEDIAREFTARGET);
				}
			});

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
		DwpMediaRefAttribute mref = (DwpMediaRefAttribute)m_viewer.getSelectedAttribute();
		
		if (type == DwpItemAttribute.TYPE_DWP_MEDIAREFSOURCE)
		{
		    DwpMediaRefSourceAttribute source = new DwpMediaRefSourceAttribute("0",
		        false);
		    mref.addChild(source,m_viewer.getTable());
		} else if (type == DwpItemAttribute.TYPE_DWP_MEDIAREFTARGET)
		{
		    String flags = "0";
		    DwpMediaRefTargetAttribute target = new DwpMediaRefTargetAttribute(
		        flags, false);
		    mref.addChild(target,m_viewer.getTable());
		} else if (type == DwpItemAttribute.TYPE_DWP_PACKAGE)
		{
		    DwpPackageAttribute attr = new DwpPackageAttribute("org", false);
		    mref.addChild(attr,m_viewer.getTable());
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
		
		if (type == DwpSceneAttribute.TYPE_DWP_MEDIAREF)
		{
			init(viewer);
			
			Attribute source = ((DwpMediaRefAttribute)attribute).getChildByName("MediaRefSource");
			if (source != null)
			{
			    // Already have a source, disable menu entry.
			    m_popupAddMediaRefSourceItem.setEnabled(false);
			}
			
			Attribute packageAttr = ((DwpMediaRefAttribute)attribute).getChildByName("Package");
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
