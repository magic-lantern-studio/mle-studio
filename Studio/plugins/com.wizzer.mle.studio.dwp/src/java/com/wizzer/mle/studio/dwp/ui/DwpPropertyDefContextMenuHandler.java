/*
 * DwpPropertyDefContextMenuHandler.java
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

// Import Magic Lantern Tool Framework classes.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.ui.AttributeTreeViewer;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaRefClassAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpPropertyDefAttribute;

/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributetreeViewer</code> for a <code>DwpPropertyDefAttribute</code>.
 *
 * @author Mark S. Millard
 */
public class DwpPropertyDefContextMenuHandler extends AbstractItemContextMenuHandler
{
	// A menu item for adding an MediaRefClass DWP item.
	private MenuItem m_popupAddMediaRefClassItem = null;

	/**
     * The default constructor.
     */
    public DwpPropertyDefContextMenuHandler()
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

		// Add menu item for adding MediaRefClass DWP item.
		m_popupAddMediaRefClassItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddMediaRefClassItem.setText("Add DWP MediaRefClass Item");
		m_popupAddMediaRefClassItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_MEDIAREFCLASS);
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
		DwpPropertyDefAttribute propDef = (DwpPropertyDefAttribute)m_viewer.getSelectedAttribute();
		
		if (type == DwpItemAttribute.TYPE_DWP_MEDIAREFCLASS)
		{
		    DwpMediaRefClassAttribute attr = new DwpMediaRefClassAttribute("mref0","MyMediaRefClass", false);
		    propDef.addChild(attr,m_viewer.getTable());
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
		
		if (type == DwpPropertyDefAttribute.TYPE_DWP_PROPERTYDEF)
		{
			init(viewer);
			
			Attribute mrefClassAttr = ((DwpPropertyDefAttribute)attribute).getChildByName("MediaRefClass");
			if (mrefClassAttr != null)
			{
			    // Already have a MediaRef Class, disable menu entry.
			    m_popupAddMediaRefClassItem.setEnabled(false);
			}
			
			String value = ((DwpPropertyDefAttribute)attribute).getValueType();
			if (! value.equals("MediaRef"))
			{
			    // Don't want to add a MediaRefClass to a property definition
			    // that isn't a MediaRef.
			    m_popupAddMediaRefClassItem.setEnabled(false);
			}

			popup = m_popupMenu;
		}
		
		return popup;
    }

}
