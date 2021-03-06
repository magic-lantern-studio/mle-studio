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
package com.wizzer.mle.studio.framework.ui;

// Import Eclipse packages.
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.HexStringAttribute;
import com.wizzer.mle.studio.framework.attribute.IAttribute;


/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributetreeViewer</code> for a <code>HexStringAttribute</code>.
 *
 * @author Mark S. Millard
 */
public class HexStringContextMenuHandler implements IAttributeContextMenuHandler
{
	// The AttributeTreeViewer.
	AttributeTreeViewer m_viewer = null;
	// The menu.
	private Menu m_popupChangeDisplay = null;
	// A menu item.
	private MenuItem m_popupChangeHexStringDisplay = null;

	/**
	 * The default constructor.
	 */
	public HexStringContextMenuHandler()
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

		// Create a context menu for deleting elements from the Attribute tree.
		m_popupChangeDisplay = new Menu(viewer.getControl().getShell(),SWT.POP_UP);
		m_popupChangeHexStringDisplay = new MenuItem(m_popupChangeDisplay,SWT.PUSH);
		m_popupChangeHexStringDisplay.setText("Change Display");
		m_popupChangeHexStringDisplay.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupChangeDisplayActionPerformed(e);
				}
			});
	}

	/**
	 * Process the "Change Display" action.
	 * 
	 * @param event The selection event that caused this handler to be invoked.
	 */
	public void popupChangeDisplayActionPerformed(Event event)
	{
		// Get the current display type.
		HexStringAttribute hsa = (HexStringAttribute) m_viewer.getSelectedAttribute();
		if ( hsa.isHexStringDisplay() )
		{
			hsa.setCharacterStringDisplay();
		}
		else
		{
			hsa.setHexStringDisplay();
		}
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
		
		if (type == Attribute.TYPE_HEX_STRING)
		{
			init(viewer);
			popup = m_popupChangeDisplay;

			HexStringAttribute hsa = (HexStringAttribute)attribute;
			if ( hsa.isHexStringDisplay() )
			{
				m_popupChangeHexStringDisplay.setText("Change Display to Characters");
			}
			else
			{
				m_popupChangeHexStringDisplay.setText("Change Display to Hexadecimal");
			}
		}
		
		return popup;
	}
}
