/*
 * AbstractItemContextMenuHandler.java
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
