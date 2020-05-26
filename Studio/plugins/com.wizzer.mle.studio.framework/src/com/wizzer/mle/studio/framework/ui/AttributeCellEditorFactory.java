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

// Import the Eclipse packages.
import org.eclipse.swt.widgets.Composite;


/**
 * This class is used to generate <code>AttributeCellEditor</code>s that have been
 * registered with this factory for a specific <code>Attribute</code> type.
 * 
 * @author Mark S. Millard
 */
public class AttributeCellEditorFactory extends Observable
{
	// The singleton instance.
	private static AttributeCellEditorFactory g_theFactory = null;
	// The registered factories.
	private Hashtable m_registry = null;
	
    // Hide default constructor.
    private AttributeCellEditorFactory()
    {
    	m_registry = new Hashtable(16);
    }
    
    /**
     * Get the Singleton instance of the <code>AttributeCellEditorFactory</code>.
     * 
     * @return The single instance of the factory is returned.
     */
    public static AttributeCellEditorFactory getInstance()
    {
    	if (g_theFactory == null)
    	    g_theFactory = new AttributeCellEditorFactory();
    	    
    	return g_theFactory;
    }
    
    /**
     * Register the specified factory for the specifed <code>Attribute</code> type.
     * 
     * @param type The <code>Attribute</code> type.
     * @param factory The factory used to create <code>AttributeCellEditor</code>
     * for the specified type.
     * 
     * @return The previous factory of the specified type is returned.
     * <b>null</b> is returned if there was no previous registration of this type.
     */
    public Object addFactory(String type,IAttributeCellEditorFactory factory)
    {
    	Object retValue = null;
    	
    	// Add the factory.
		retValue = m_registry.put(type,factory);
		
		// Notify observers that the meta-factory has changed.
		this.setChanged();
		this.notifyObservers();
    	
    	return retValue;
    }
    
	/**
	 * Unregister the specified factory for the specifed <code>Attribute</code> type.
	 * 
	 * @param type The <code>Attribute</code> type.
	 * 
	 * @return The factory that was registered is returned.
	 * <b>null</b> is returned if there was no previous registration of this type.
	 */
    public Object removeFactory(String type)
    {
		Object retValue = null;
    	
    	// Remove the factory.
		retValue = m_registry.remove(type);

		// Notify observers that the meta-factory has changed.
		this.setChanged();
		this.notifyObservers();
    	
		return retValue;
    }
   
    /**
     * Check whether a factory has been registered for the specified type.
     * 
     * @param type The <code>Attribute</code> type.
     * 
     * @return <b>true</b> is returned if a factory has been registered for the
     * specified type. Otherwise, <b>false</b> is returned.
     */
    public boolean hasFactory(String type)
    {
    	boolean retValue = m_registry.containsKey(type);
   	    return retValue;
    }
   
    /**
     * Create an instance of the <code>AttributeCellEditor</code> for the specified type.
     * 
     * @param type The <code>Attribute</code> type.
     * 
     * @return An instance of an <code>AttributeCellEditor</code> for the specified
     * type of <code>Attribute</code> is returned.
     * If an instance could not be created, then <b>null</b> will be returned.
     */
    public AttributeCellEditor createInstance(String type,Composite parent)
    {
        AttributeCellEditor retValue = null;
       
        // Make sure there is a factory of the registered type.
        if (m_registry.containsKey(type))
        {
        	// Create the AttributeCellEditor for the specified type.
            IAttributeCellEditorFactory factory = (IAttributeCellEditorFactory)m_registry.get(type);
            retValue = factory.createInstance(parent);
        }
       
       return retValue;
    }
}
