// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
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
package com.wizzer.mle.studio.framework.ui;

// Import standard Java packages.
import java.util.Observable;
import java.util.Hashtable;

// Import Eclipse packages.
import org.eclipse.swt.widgets.Menu;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.IAttribute;


/**
 * This class is used to manage <code>IAttributeContextMenuHandler</code>s that have been
 * registered for a specific <code>IAttribute</code> type.
 * 
 * @author Mark S. Millard
 */
public class AttributeContextMenuHandlerRegistry extends Observable
{
	// The singleton instance.
	private static AttributeContextMenuHandlerRegistry g_theRegistry = null;
	// The registered handlers.
	private Hashtable m_registry = null;
	
	// Hide default constructor.
	private AttributeContextMenuHandlerRegistry()
	{
		m_registry = new Hashtable(16);
	}
    
	/**
	 * Get the Singleton instance of the <code>AttributeContextMenuHandlerRegistry</code>.
	 * 
	 * @return The single instance of the registry is returned.
	 */
	public static AttributeContextMenuHandlerRegistry getInstance()
	{
		if (g_theRegistry == null)
			g_theRegistry = new AttributeContextMenuHandlerRegistry();
    	    
		return g_theRegistry;
	}
    
	/**
	 * Register the specified handler for the specifed <code>IAttribute</code> type.
	 * 
	 * @param type The <code>IAttribute</code> type.
	 * @param handler The handler for the specified type.
	 * 
	 * @return The previous handler of the specified type is returned.
	 * <b>null</b> is returned if there was no previous registration of this type.
	 */
	public Object addHandler(String type,IAttributeContextMenuHandler handler)
	{
		Object retValue = null;
    	
		// Add the handler.
		retValue = m_registry.put(type,handler);
		
		// Notify observers that the registry has changed.
		this.setChanged();
		this.notifyObservers();
    	
		return retValue;
	}
    
	/**
	 * Unregister the specified handler for the specifed <code>IAttribute</code> type.
	 * 
	 * @param type The <code>IAttribute</code> type.
	 * 
	 * @return The handler that was registered is returned.
	 * <b>null</b> is returned if there was no previous registration of this type.
	 */
	public Object removeHandler(String type)
	{
		Object retValue = null;
    	
		// Remove the handler.
		retValue = m_registry.remove(type);

		// Notify observers that the registry has changed.
		this.setChanged();
		this.notifyObservers();
    	
		return retValue;
	}
   
	/**
	 * Check whether a handler has been registered for the specified type.
	 * 
	 * @param type The <code>IAttribute</code> type.
	 * 
	 * @return <b>true</b> is returned if a factory has been registered for the
	 * specified type. Otherwise, <b>false</b> is returned.
	 */
	public boolean hasHandler(String type)
	{
		boolean retValue = m_registry.containsKey(type);
		return retValue;
	}

	/**
	 * Get a <code>Menu</code> for the specified attribute.
	 * 
	 * @param attribute The <code>IAttribute</code> to get the context menu for.
	 * 
	 * @return An instance of a <code>Menu</code> is returned for the specified
	 * type of <code>IAttribute</code>.
	 * If an instance could not be retrieved, then <b>null</b> will be returned.
	 */
	public Menu getContextMenu(IAttribute attribute,AttributeTreeViewer viewer)
	{
		Menu retValue = null;
		String type = attribute.getType();
       
		// Make sure there is a factory of the registered type.
		if (m_registry.containsKey(type))
		{
			// Create the Menu for the specified type.
			IAttributeContextMenuHandler handler = (IAttributeContextMenuHandler)m_registry.get(type);
			retValue = handler.getContextMenu(attribute,viewer);
		}
       
	   return retValue;
	}

}
