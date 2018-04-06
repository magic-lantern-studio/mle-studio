/*
 * AbstractItemContextMenuHandler.java
 * Created on May 14, 2005
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
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

// Import Magic Lantern Tool Framework classes.
import com.wizzer.mle.studio.framework.ui.AttributeTreeViewer;
import com.wizzer.mle.studio.framework.ui.IAttributeContextMenuHandler;

// Import Magic Lantern DWP classes.
import com.wizzer.mle.studio.dwp.DwpPlugin;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;

/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributeTreeViewer</code>.
 * <p>
 * A menu entry is provided for creating a discriminator tag to a
 * Digital Workprint item.
 * </p>
 * 
 * @author Mark S. Millard
 */
public abstract class AbstractItemContextMenuHandler implements
	IAttributeContextMenuHandler
{    
	// The AttributeTreeViewer.
    protected AttributeTreeViewer m_viewer = null;
	// The menu.
    protected Menu m_popupMenu = null;
	// A menu item for adding a tag.
    protected MenuItem m_popupAddTag = null;

    /**
     * The default constructor.
     */
    public AbstractItemContextMenuHandler()
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
	    m_viewer = viewer;
	}

    /**
     * Create the context menu.
     * <p>
     * A menu item is created for adding a tag to a DWP item.
     * </p>
     */
	protected void createContextMenu()
	{
		// Create a context menu for adding elements to the Attribute tree.
	    if (m_popupMenu == null)
	        m_popupMenu = new Menu(m_viewer.getControl().getShell(),SWT.POP_UP);

		// Add menu item for adding a DWP tag.
		m_popupAddTag = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddTag.setText("Add DWP tag");
		m_popupAddTag.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddTagActionPerformed(e);
				}
			});
	}

	/**
	 * Process the "Add tag" action.
	 * 
	 * @param event The selection event that caused this handler to be invoked.
	 */
	public void popupAddTagActionPerformed(Event event)
	{
	    // Get selected attribute.
	    DwpItemAttribute attr = (DwpItemAttribute)m_viewer.getSelectedAttribute();
	    //System.out.println("DWP Attribute selected: " + attr.getName())
	    
	    // Add a placeholder for the tag.
	    attr.addTag("tagProxy");
	    
	    // Update the Property View.
	    IViewPart props = DwpPlugin.getWorkbenchWindow().getActivePage().findView("org.eclipse.ui.views.PropertySheet");
	    if (props != null)
	    {
	        PropertySheetPage page = (PropertySheetPage)((PropertySheet)props).getCurrentPage();
	        page.refresh();
	    }
	}
}
