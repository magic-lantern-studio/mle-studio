/*
 * DwpTable.java
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
package com.wizzer.mle.studio.dwp.domain;

// Import standard Java packages.
import java.util.Observable;

// Import Magic Lantern Studio Framework packages.
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.NumberAttribute;
import com.wizzer.mle.studio.framework.attribute.StringAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;
import com.wizzer.mle.studio.framework.domain.Table;

// Import DWP packages.
import com.wizzer.mle.studio.dwp.DwpException;
import com.wizzer.mle.studio.dwp.DwpLog;
import com.wizzer.mle.studio.dwp.attribute.DwpDocumentAttribute;

/**
 * This class implements a domain table for a Magic Lantern Digital Workprint.
 * 
 * @author Mark S. Millard
 */
public class DwpTable extends Table implements IDwpItemAttributeFactory
{
	/** The version of the DWP supported bu this domain table. */
	protected static String DWP_VERSION = "Digital_Workprint_v1.0";
	
	/** The elements in the Digital Workprint. */
	protected VariableListAttribute m_elements;
	/** The number of elements in the Digital Workprint. */
	protected NumberAttribute m_numElements;
	
	// The factory that will create DWP items for the domain table.
	private IDwpItemAttributeFactory m_dwpItemFactory;
	// The current DWP item type.
	private String m_dwpItemType = null;
	
	/**
	 * The default constructor.
	 */
	public DwpTable()
	{
		super();
		
		// Register this as the default DWP Attribute item factory.
		if (m_dwpItemFactory == null)
		{
		    m_dwpItemFactory = this;
		    m_elements.setTheCaller(m_dwpItemFactory);
		    m_elements.setCallbackData(m_dwpItemType);
		}
	}

	/**
	 * Build the default instance of the Digital Workprint.
	 * 
	 * @return The root of the <b>Attribute</b> tree is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.domain.Table#buildDefaultInstance()
	 */
	protected Attribute buildDefaultInstance()
	{
		// Add a header at the top of the DWP table.
		m_top = new StringAttribute(DWP_VERSION, "", 0, true);
		
		// Create an Attribute that can hold the number of elements in the DWP.
		// This is not part of the DWP format, but a mechanism by which to keep
		// track of the DWP Attributes being added to the domain table.
		m_numElements = new NumberAttribute("Number_Of_Elements", 0, 32, 10, true);
		
		// Create a Variable List Attribute to hold the remaining Dwp items.
		//m_elements = (VariableListAttribute)m_top.addChild(
		//     new VariableListAttribute("DWP Items", m_numElements, "createDwpItemAttribute",
		//         m_dwpItemType, m_dwpItemFactory), this);
		DwpDocumentAttribute top = new DwpDocumentAttribute("DWP Items",
		    m_numElements, "createDwpItemAttribute", m_dwpItemType, new DwpDocument());
		m_elements = (DwpDocumentAttribute)m_top.addChild(top, this);
		try {
		    registerDwpItemFactory(new DwpDocument(),top.getType());
		} catch (DwpException ex)
		{
		    DwpLog.logError(ex,"Unable to register DWP item factory.");
		}

		// Add the CRC 32-bit
		m_crcAttribute = (NumberAttribute)m_top.addChild(new NumberAttribute("CRC_32", 1234, 32, 16, true), this);
		this.updateCRC();

		// Mark as being modified.
		this.setChanged();
						
		return m_top;
	}

	/**
	 * Create a default instance of a Digital Workprint Item.
	 * <p>
	 * A <code>StringAttribute</code> is created as a proxy for DWP item.
	 * </p>
	 * 
	 * @param vla The Variable List Attribute managing the list of items.
	 * @param data User data associated with the callback. Ignored.
	 * 
	 * @return A reference to an Attribute Tree is returned containing the required fields.
	 */
	public IAttribute createDwpItemAttribute(VariableListAttribute vla, Object data)
	{
		long count = vla.getCount();

		// Create a proxy for a Digital Workprint item by default.
		Attribute result = new StringAttribute("DWP_Item_Proxy_" + count,"", 0, true);

		return result;
	}
	
	/**
	 * Register a factory by which Digital Workprint Items will be created.
	 * 
	 * @param factory The DWP Item factory.
	 * @param data The callback data that will be sent with the callback.
	 * 
	 * @throws DwpException This is exception is thrown if the factory
	 * argument is <b>null</b>.
	 */
	public void registerDwpItemFactory(IDwpItemAttributeFactory factory, Object data)
	    throws DwpException
	{
		if (factory == null)
		    throw new DwpException("DWP Item Factory must not be null.");
		    
		m_dwpItemFactory = factory;
		m_dwpItemType = (String)data;
	}

	/**
	 * Unregister the current Digital Workprint Item factory. This domain table
	 * will become the default factory and, by default, will generate proxys for any
	 * items that may attempt to be created.
	 */
	public void unregisterDwpItemFactory()
	{
		m_dwpItemFactory = this;
		m_dwpItemType = null;
	}
	
	/**
	 * Get the branch of the attribute tree where the DWP items are being stored.
	 * 
	 * @return A reference to the <code>VariableListAttribute</code> is returned.
	 */
	public VariableListAttribute getDwpElements()
	{
		return m_elements;
	}
    
	// Recursively copy the children of the specified node.
	private void copyChildren(Attribute toAttr, Attribute fromAttr)
	{
		if ((toAttr == null) || (fromAttr == null))
			return;

		// Recursively copy children.
		if (fromAttr.getChildCount() > 0)
		{
			for (int i = 0; i < fromAttr.getChildCount(); i++)
			{
				Attribute fromChild = fromAttr.getChild(i);
				Attribute toChild = toAttr.addChild(fromChild.copy(),this);
				copyChildren(toChild,fromChild);
				if (toAttr instanceof VariableListAttribute)
				{
					((VariableListAttribute)toAttr).getCountAttribute().bump();
				}
			}
		}
	}
	
	/**
	 * Copy the specifed table's DWP items to this one.
	 * 
	 * @param table The <code>DwpTable</code> to copy.
	 * 
	 * @return <b>true</b> is returned if the Attribute tree is successfully copied.
	 * Otherwise <b>false</b> will be returned.
	 */
	public boolean copy(DwpTable table)
	{
		boolean retValue = false;
    	
		// Remove the DWP items of this Table.
		purge(m_elements);
		m_numElements.setValue(new Long(0));
		
		// Copy the elements from the specified Table.
		copyChildren(m_elements,table.m_elements);
		
		retValue = true;
    	
		return retValue;
	}

	/**
	 * This method is called whenever the observed object is changed. 
	 * An application calls an Observable object's notifyObservers method to have all the object's observers
	 * notified of the change.
	 * <p>
	 * When the Observable object is an Attribute, the notfication most likely came from an update or
	 * modifcation to the Attribute Tree. This notification is then forwarded to all Observers of this
	 * domain table.
	 * </p>
	 * 
	 * @param obs The observable object.
	 * @param obj An argument passed to the notifyObservers method.
	 */
	public void update(Observable obs, Object obj)
	{
		// Update the CRC.
		this.updateCRC();
		this.setChanged();

		if (obs instanceof Attribute)
		{
			// Notification came from the Attribute Tree, pass it on,
			// specifically to the Attribute Tree Model.
			this.notifyObservers(obs);
		} else
		{
			this.notifyObservers();
		}
	}

}
