/*
 * DwpSetDefContextMenuHandler.java
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
import com.wizzer.mle.studio.dwp.attribute.DwpHeaderFileAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpPackageAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpPropertyDefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSetAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSetDefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSourceFileAttribute;

/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributetreeViewer</code> for a <code>DwpSetDefAttribute</code>.
 *
 * @author Mark S. Millard
 */
public class DwpSetDefContextMenuHandler extends AbstractItemContextMenuHandler
{
	// A menu item for adding a HeaderFile item.
	private MenuItem m_popupAddHeaderFileItem = null;
	// A menu item for adding a SourceFile item.
	private MenuItem m_popupAddSourceFileItem = null;
	// A menu item for adding a Package item.
	private MenuItem m_popupAddPackageItem = null;
	// A menu item for adding an PropertyDef DWP item.
	private MenuItem m_popupAddPropertyDefItem = null;
	// The current count of property defs.
	private int m_propertyDefCount = 0;

	/**
     * The default constructor.
     */
    public DwpSetDefContextMenuHandler()
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

		// Add menu item for adding PropertyDef DWP item.
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
		DwpSetDefAttribute setDef = (DwpSetDefAttribute)m_viewer.getSelectedAttribute();
		
		if (type == DwpItemAttribute.TYPE_DWP_HEADERFILE)
		{
		    String headerFile = setDef.getValue() + ".h";
		    DwpHeaderFileAttribute header = new DwpHeaderFileAttribute(headerFile, false);
		    setDef.addChild(header,m_viewer.getTable());
		} else if (type == DwpItemAttribute.TYPE_DWP_SOURCEFILE)
		{
		    String sourceFile = setDef.getValue() + ".cpp";
		    DwpSourceFileAttribute source = new DwpSourceFileAttribute(sourceFile, false);
		    setDef.addChild(source,m_viewer.getTable());
		} else if (type == DwpItemAttribute.TYPE_DWP_PROPERTYDEF)
		{
		    DwpPropertyDefAttribute group = new DwpPropertyDefAttribute("prop" +
		        m_propertyDefCount++, "int", false);
		    setDef.addChild(group,m_viewer.getTable());
		} else if (type == DwpItemAttribute.TYPE_DWP_PACKAGE)
		{
		    DwpPackageAttribute attr = new DwpPackageAttribute("org", false);
		    setDef.addChild(attr,m_viewer.getTable());
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
		
		if (type == DwpSetDefAttribute.TYPE_DWP_SETDEF)
		{
			init(viewer);

			Attribute headerFileAttr = ((DwpSetDefAttribute)attribute).getChildByName("HeaderFile");
			if (headerFileAttr != null)
			{
			    // Already have a header file, disable menu entry.
			    m_popupAddHeaderFileItem.setEnabled(false);
			}
			
			Attribute sourceFileAttr = ((DwpSetDefAttribute)attribute).getChildByName("SourceFile");
			if (sourceFileAttr != null)
			{
			    // Already have a source file, disable menu entry.
			    m_popupAddSourceFileItem.setEnabled(false);
			}

			Attribute packageAttr = ((DwpSetDefAttribute)attribute).getChildByName("Package");
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
