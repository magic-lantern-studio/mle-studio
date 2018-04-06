/*
 * DwpActorContextMenuHandler.java
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
import com.wizzer.mle.studio.dwp.attribute.DwpActorAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpHeaderFileAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpPackageAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpRoleBindingAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSourceFileAttribute;

/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributetreeViewer</code> for a <code>DwpActorAttribute</code>.
 *
 * @author Mark S. Millard
 */
public class DwpActorContextMenuHandler extends AbstractPropertyContextMenuHandler
{    
	// A menu item for adding a HeaderFile item.
	//private MenuItem m_popupAddHeaderFileItem = null;
	// A menu item for adding a SourceFile item.
	//private MenuItem m_popupAddSourceFileItem = null;
	// A menu item for adding a Package item.
	//private MenuItem m_popupAddPackageItem = null;
	// A menu item for adding a Role Binding item.
	private MenuItem m_popupAddRoleBindingItem = null;

	/**
     * The default constructor.
     */
    public DwpActorContextMenuHandler()
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
     *  Create the context menu.
     */
	protected void createContextMenu()
	{
		// Create a context menu for adding elements to the Attribute tree.
		m_popupMenu = new Menu(m_viewer.getControl().getShell(),SWT.POP_UP);
		
		// Add menu item for adding HeaderFile DWP item.
		/*
		m_popupAddHeaderFileItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddHeaderFileItem.setText("Add DWP HeaderFile Item");
		m_popupAddHeaderFileItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_HEADERFILE, null);
				}
			});
		*/

		// Add menu item for adding SourceFile DWP item.
		/*
		m_popupAddSourceFileItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddSourceFileItem.setText("Add DWP SourceFile Item");
		m_popupAddSourceFileItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_SOURCEFILE, null);
				}
			});
		*/

		// Add menu item for adding Package DWP item.
		/*
		m_popupAddPackageItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddPackageItem.setText("Add DWP Package Item");
		m_popupAddPackageItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PACKAGE, null);
				}
			});
		*/

		// Add menu item for adding RoleBinding DWP item.
		m_popupAddRoleBindingItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddRoleBindingItem.setText("Add DWP RoleBinding Item");
		m_popupAddRoleBindingItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_ROLEBINDING, null);
				}
			});
		
		// Create the menu items for the Property DWP items.
		super.createContextMenu();
	}

	/**
	 * Process the "Add List Element" action.
	 * 
	 * @param event The selection event that caused this handler to be invoked.
	 * @param type The type of <code>Attribute</code> to add.
	 */
	public void popupAddListElementActionPerformed(Event event, String attrType,
	    String propType)
	{
		DwpActorAttribute actor = (DwpActorAttribute)m_viewer.getSelectedAttribute();
		
		if (attrType == DwpItemAttribute.TYPE_DWP_HEADERFILE)
		{
		    String headerFile = actor.getValueType() + ".h";
		    DwpHeaderFileAttribute header = new DwpHeaderFileAttribute(headerFile, false);
		    actor.addChild(header,m_viewer.getTable());
		} else if (attrType == DwpItemAttribute.TYPE_DWP_SOURCEFILE)
		{
		    String sourceFile = actor.getValueType() + ".cpp";
		    DwpSourceFileAttribute source = new DwpSourceFileAttribute(sourceFile, false);
		    actor.addChild(source,m_viewer.getTable());
		} else if (attrType == DwpItemAttribute.TYPE_DWP_PACKAGE)
		{
		    DwpPackageAttribute attr = new DwpPackageAttribute("org", false);
		    actor.addChild(attr,m_viewer.getTable());
		} else if (attrType == DwpItemAttribute.TYPE_DWP_ROLEBINDING)
		{
		    String role = actor.getValueType() + "Role";
		    String set = "MySet";
		    DwpRoleBindingAttribute binding = new DwpRoleBindingAttribute(
		        role, set, false);
		    actor.addChild(binding,m_viewer.getTable());
		} else
		{
		    // Check for the Property Attribute types inherited from
		    // AbstractPropertyContextMenuHandler.
		    super.popupAddListElementActionPerformed(event, attrType, propType);
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
		
		if (type == DwpActorAttribute.TYPE_DWP_ACTOR)
		{
			init(viewer);
			
			Attribute headerFileAttr = ((DwpActorAttribute)attribute).getChildByName("HeaderFile");
			if (headerFileAttr != null)
			{
			    // Already have a header file, disable menu entry.
			    //m_popupAddHeaderFileItem.setEnabled(false);
			}
			
			Attribute sourceFileAttr = ((DwpActorAttribute)attribute).getChildByName("SourceFile");
			if (sourceFileAttr != null)
			{
			    // Already have a source file, disable menu entry.
			    //m_popupAddSourceFileItem.setEnabled(false);
			}
			
			Attribute packageAttr = ((DwpActorAttribute)attribute).getChildByName("Package");
			if (packageAttr != null)
			{
			    // Already have a package, disable menu entry.
			    //m_popupAddPackageItem.setEnabled(false);
			}

			Attribute roleBindingAttr = ((DwpActorAttribute)attribute).getChildByName("RoleBinding");
			if (roleBindingAttr != null)
			{
			    // Already have a role binding, disable menu entry.
			    m_popupAddRoleBindingItem.setEnabled(false);
			}

			popup = m_popupMenu;
		}
		
		return popup;
    }

}
