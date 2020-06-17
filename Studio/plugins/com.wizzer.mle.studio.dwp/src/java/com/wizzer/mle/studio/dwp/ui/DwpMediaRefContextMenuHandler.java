/*
 * DwpMediaRefContextMenuHandler.java
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
