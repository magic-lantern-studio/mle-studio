/*
 * DwpSceneContextMenuHandler.java
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
import com.wizzer.mle.studio.framework.ui.IAttributeContextMenuHandler;
import com.wizzer.mle.studio.dwp.attribute.DwpGroupAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpGroupRefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpHeaderFileAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpPackageAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSceneAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSourceFileAttribute;

/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributetreeViewer</code> for a <code>DwpSceneAttribute</code>.
 *
 * @author Mark S. Millard
 */
public class DwpSceneContextMenuHandler implements IAttributeContextMenuHandler
{
	// The AttributeTreeViewer.
	AttributeTreeViewer m_viewer = null;
	// The menu.
	private Menu m_popupMenu = null;
	// A menu item for adding a HeaderFile item.
	private MenuItem m_popupAddHeaderFileItem = null;
	// A menu item for adding a SourceFile item.
	private MenuItem m_popupAddSourceFileItem = null;
	// A menu item for adding a Package item.
	private MenuItem m_popupAddPackageItem = null;
	// A menu item for adding an Actor DWP item.
	private MenuItem m_popupAddGroupItem = null;
	// A menu item for adding a Role Attachement DWP item.
	private MenuItem m_popupAddGroupRefItem = null;
	// The current count of actors.
	private int m_groupCount = 0;

	/**
     * The default constructor.
     */
    public DwpSceneContextMenuHandler()
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

		// Create a context menu for adding elements to the Attribute tree.
		m_popupMenu = new Menu(m_viewer.getControl().getShell(),SWT.POP_UP);
		
		// Add menu item for adding HeaderFile DWP item.
		m_popupAddHeaderFileItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddHeaderFileItem.setText("Add DWP HeaderFile Item");
		m_popupAddHeaderFileItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_HEADERFILE);
				}
			});

		// Add menu item for adding SourceFile DWP item.
		m_popupAddSourceFileItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddSourceFileItem.setText("Add DWP SourceFile Item");
		m_popupAddSourceFileItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_SOURCEFILE);
				}
			});

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

		// Add menu item for adding Group DWP item.
		m_popupAddGroupItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddGroupItem.setText("Add DWP Group Item");
		m_popupAddGroupItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_GROUP);
				}
			});
		

		// Add menu item for adding GroupRef DWP item.
		m_popupAddGroupRefItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddGroupRefItem.setText("Add DWP GroupRef Item");
		m_popupAddGroupRefItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_GROUPREF);
				}
			});

	}

	/**
	 * Process the "Add List Element" action.
	 * 
	 * @param event The selection event that caused this handler to be invoked.
	 * @param type The type of <code>Attribute</code> to add.
	 */
	public void popupAddListElementActionPerformed(Event event, String type)
	{
		DwpSceneAttribute scene = (DwpSceneAttribute)m_viewer.getSelectedAttribute();
		
		if (type == DwpItemAttribute.TYPE_DWP_GROUP)
		{
		    DwpGroupAttribute group = new DwpGroupAttribute("group" + m_groupCount++,
		        "MyGroup", false);
		    scene.addChild(group,m_viewer.getTable());
		} if (type == DwpItemAttribute.TYPE_DWP_HEADERFILE)
		{
		    String headerFile = scene.getValueType() + ".h";
		    DwpHeaderFileAttribute header = new DwpHeaderFileAttribute(headerFile, false);
		    scene.addChild(header,m_viewer.getTable());
		} if (type == DwpItemAttribute.TYPE_DWP_SOURCEFILE)
		{
		    String sourceFile = scene.getValueType() + ".cpp";
		    DwpSourceFileAttribute source = new DwpSourceFileAttribute(sourceFile, false);
		    scene.addChild(source,m_viewer.getTable());
		} else if (type == DwpItemAttribute.TYPE_DWP_PACKAGE)
		{
		    DwpPackageAttribute attr = new DwpPackageAttribute("org", false);
		    scene.addChild(attr,m_viewer.getTable());
		} if (type == DwpItemAttribute.TYPE_DWP_GROUPREF)
		{
		    DwpGroupRefAttribute groupRef = new DwpGroupRefAttribute(
		        "group", false);
		    scene.addChild(groupRef,m_viewer.getTable());
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
		
		if (type == DwpSceneAttribute.TYPE_DWP_SCENE)
		{
			init(viewer);
			
			Attribute headerFile = ((DwpSceneAttribute)attribute).getChildByName("HeaderFile");
			if (headerFile != null)
			{
			    // Already have a header file, disable menu entry.
			    m_popupAddHeaderFileItem.setEnabled(false);
			}

			Attribute sourceFile = ((DwpSceneAttribute)attribute).getChildByName("SourceFile");
			if (sourceFile != null)
			{
			    // Already have a source file, disable menu entry.
			    m_popupAddSourceFileItem.setEnabled(false);
			}
			
			Attribute packageAttr = ((DwpSceneAttribute)attribute).getChildByName("Package");
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
