/*
 * DwpItemContextMenuHandler.java
 * Created on May 17, 2005
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
import org.eclipse.swt.widgets.Menu;

// Import Magic Lantern Tool Framework classes.
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.ui.AttributeTreeViewer;

/**
 * This class is the default Context Menu Handler for DWP items.
 * 
 * @author Mark S. Millard
 */
public class DwpItemContextMenuHandler extends AbstractItemContextMenuHandler
{
    // Initialization flag.
    boolean m_isInitialized = false;

	/**
     * The default constructor.
     */
    public DwpItemContextMenuHandler()
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
		super.createContextMenu();
		m_isInitialized = true;
	}

	/**
	 * Get a context <code>Menu</code> for the specified <code>IAttribute</code>.
	 * 
	 * @param attribute The <code>IAttribute</code> to create a context menu for.
	 * 
	 * @return A reference to a <code>Menu</code> is returned.
	 * 
     * @see com.wizzer.mle.studio.framework.ui.IAttributeContextMenuHandler#getContextMenu(com.wizzer.mle.studio.framework.attribute.IAttribute, com.wizzer.mle.studio.framework.ui.AttributeTreeViewer)
     */
    public Menu getContextMenu(IAttribute attribute, AttributeTreeViewer viewer)
    {
        // Initialize the menu, if necessary.
        if (! m_isInitialized)
            init(viewer);
        
        // Return the context menu.
        return m_popupMenu;
    }

}
