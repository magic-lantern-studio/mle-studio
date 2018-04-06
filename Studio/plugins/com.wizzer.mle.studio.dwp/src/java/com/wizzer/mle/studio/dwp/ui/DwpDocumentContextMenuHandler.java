/*
 * DwpDocumentContextMenuHandler.java
 * Created on Aug 10, 2004
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

// Import Eclipse packages.
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

// Import Wizzer Works Framework classes.
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.ui.IAttributeContextMenuHandler;
import com.wizzer.mle.studio.framework.ui.AttributeTreeViewer;

// Import Digital Workprint classes.
import com.wizzer.mle.studio.dwp.attribute.DwpDocumentAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;


/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributetreeViewer</code> for a <code>DwpDocumentAttribute</code>.
 *
 * @author Mark S. Millard
 */
public class DwpDocumentContextMenuHandler implements IAttributeContextMenuHandler
{
	// The AttributeTreeViewer.
	AttributeTreeViewer m_viewer = null;
	// The menu.
	private Menu m_popupMenu = null;
	// A menu item for adding an Include DWP item.
	private MenuItem m_popupAddIncludeItem = null;
	// A menu item for adding a Stage DWP item.
	private MenuItem m_popupAddStageItem = null;
	// A menu item for adding a Scene DWP item.
	private MenuItem m_popupAddSceneItem = null;
	// A menu item for adding a Group DWP item.
	private MenuItem m_popupAddGroupItem = null;
	// A menu item for adding a Media Reference DWP item.
	private MenuItem m_popupAddMediaRefItem = null;
	// A menu item for adding a ActorDef DWP item.
	private MenuItem m_popupAddActorDefItem = null;
	// A menu item for adding a RoleDef DWP item.
	private MenuItem m_popupAddRoleDefItem = null;
	// A menu item for adding a SetDef DWP item.
	private MenuItem m_popupAddSetDefItem = null;
	// A menu item for adding a Boot Scene DWP item.
	private MenuItem m_popupAddBootItem = null;



	/**
	 * The default constructor.
	 */
	public DwpDocumentContextMenuHandler()
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
		
		// Add menu item for adding Include DWP item.
		m_popupAddIncludeItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddIncludeItem.setText("Add DWP Include Item");
		m_popupAddIncludeItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_INCLUDE);
				}
			});

		// Add menu item for adding ActorDef DWP item.
		m_popupAddSetDefItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddSetDefItem.setText("Add DWP SetDef Item");
		m_popupAddSetDefItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_SETDEF);
				}
			});

		// Add menu item for adding ActorDef DWP item.
		m_popupAddActorDefItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddActorDefItem.setText("Add DWP ActorDef Item");
		m_popupAddActorDefItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_ACTORDEF);
				}
			});

		// Add menu item for adding RoleDef DWP item.
		m_popupAddRoleDefItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddRoleDefItem.setText("Add DWP RoleDef Item");
		m_popupAddRoleDefItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_ROLEDEF);
				}
			});
		
		// Add menu item for adding Stage DWP item.
		m_popupAddStageItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddStageItem.setText("Add DWP Stage Item");
		m_popupAddStageItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_STAGE);
				}
			});
		
		// Add menu item for adding Scene DWP item.
		m_popupAddSceneItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddSceneItem.setText("Add DWP Scene Item");
		m_popupAddSceneItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_SCENE);
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

		// Add menu item for adding MediaRef DWP item.
		m_popupAddMediaRefItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddMediaRefItem.setText("Add DWP MediaRef Item");
		m_popupAddMediaRefItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_MEDIAREF);
				}
			});

		// Add menu item for adding Boot DWP item.
		m_popupAddBootItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddBootItem.setText("Add DWP Boot Item");
		m_popupAddBootItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_BOOT);
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
		DwpDocumentAttribute vla = (DwpDocumentAttribute)m_viewer.getSelectedAttribute();
		
		// Add the callback data.
		vla.setCallbackData(type);

        // Add a new element to the the selected VariableList Attribute.
		m_viewer.getTable().addListElement(vla);
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
		
		if (type == DwpDocumentAttribute.TYPE_DWP_DOCUMENT)
		{
			init(viewer);
			popup = m_popupMenu;
		}
		
		return popup;
	}

}
