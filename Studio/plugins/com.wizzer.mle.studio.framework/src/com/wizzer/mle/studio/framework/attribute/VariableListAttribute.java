//COPYRIGHT_BEGIN
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
//COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.framework.attribute;


/**
 * This <code>Attribute</code> subclass maintains the root of a tree of attributes intended to
 * hold a variable set of sub-elements.
 * <p>
 * The GUI should provide some user interface mechanism for adding and removing
 * (and optionally reordering) the sub-elements at will.
 * </p>
 */
public class VariableListAttribute extends StringAttribute
{
	// A user friendly display name.
    private String m_displayName = ""; // e.g., "Channel"
    // The callback.
    private String m_callback = ""; // e.g., "buildDefaultChannelInstance"
    // The user callback data.
    private Object m_callbackData = null;
    // The number of Attributes in the list.
    private NumberAttribute m_countAttribute = null;
    private int m_multiplier;
    private int m_base;
    // The object to callback to.
    private Object m_theCaller;

	/**
	 * A constructor for initializing the <code>VariableListAttribute</code>.
	 * <p>
	 * It provides a function name which is to be called when an element is
	 * added or removed. The function is called back on the <code>Object</code> which
	 * is specified as the caller.
	 * </p>
	 * 
	 * @param displayName A <code>String</code> that can be displayed in a GUI.
	 * @param countAttribute A <code>NumberAttribute</code> identifying the number of attributes in the variable
	 * list.
	 * @param callback The name of a method that can be called back when an element
	 * is added to the variable list.
	 * @param callbackData User data to associate with the callback.
	 * @param theCaller The <code>Object</code> that is called back when an element is added or removed from
	 * the variable list.
	 */
    public VariableListAttribute(String displayName, NumberAttribute countAttribute,
    	String callback, Object callbackData, Object theCaller)
    {
        this(displayName, countAttribute, callback, callbackData, theCaller, 1, 0);
    }

    /**
     * A constructor for initializing the <code>VariableListAttribute</code>.
     * <p>
     * It provides a function name which is to be called when an element is
     * added or removed. The function is called back on the <code>Object</code> which
	 * is specified as the caller.
     * </p><p>
     * The multiplier and base are provided so that the associated count attribute can be
     * automatically maintained.
     * </p><p>
     * For N elements, the count is: <b>base + ( N * multiplier)</b>.
     * </p>
     * 
     * @param displayName A <code>String</code> that can be displayed in a GUI.
     * @param countAttribute A <code>NumberAttribute</code> identifying the number of attributes in the variable
     * list.
     * @param callback The name of a method that can be called back when an elemnt
     * is added to the variable list.
     * @param callbackData User data to associate with the callback.
     * @param theCaller The <code>Object</code> that is called back when an element is added or removed from
     * the variable list.
     * @param multiplier
     * @param base
     */
    public VariableListAttribute(String displayName, NumberAttribute countAttribute,
        String callback, Object callbackData, Object theCaller, int multiplier, int base)
    {
            // Make it plural:
            super(displayName, "", 0, true);
            
            m_countAttribute = countAttribute;
            m_displayName = displayName;
			m_callback = callback;
			m_callbackData = callbackData;
            m_multiplier = multiplier;
            m_theCaller = theCaller;
            m_base = base;
    }

	/**
	 * Get the <code>VariableListAttribute</code> type.
	 * 
	 * @return By default, <b>TYPE_VARIABLE_LIST</b> is always returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getType()
	 */
    public String getType()
    {
        return VariableListAttribute.TYPE_VARIABLE_LIST;
    }

    /**
     * Get a name that can be displayed by a GUI.
     * 
     * @return A <code>String</code> is returned.
     */
    public String getDisplayName()
    {
        return m_displayName;
    }

	/**
	 * Set a name that can be displayed by a GUI.
	 * 
	 * @param displayName A <code>String</code> is returned.
	 */
    public void setDisplayName(String displayName)
    {
        m_displayName = displayName;
    }

    /**
     * Get the number of attributes contained by the <code>VariableListAttribute</code>.
     * 
     * @return A <code>NumberAttribute</code> is returned that manages the count of attributes
     * in the <code>VariableListAttribute</code>.
     */
    public NumberAttribute getCountAttribute()
    {
        return m_countAttribute;
    }

	/**
	 * Set the number of attributes contained by the <code>VariableListAttribute</code>.
	 * 
	 * @return countAttribute A <code>NumberAttribute</code> used to manages the count of attributes
	 * in the <code>VariableListAttribute</code>.
	 */
    public void setCountAttribute(NumberAttribute countAttribute)
    {
        m_countAttribute = countAttribute;
    }

    /**
     * Get the method name that is called back when an element is added or removed from the
     * <code>VariableListAttribute</code>.
     * <p>
     * This method is called back on <code>theCaller</code> object that was specified during
     * construction.
     * </p>
     * 
     * @return The name of the method is returned as a <code>String</code>.
     */
    public String getCallback()
    {
        return m_callback;
    }

	/**
	 * Set the method name that is called back when an element is added or removed from the
	 * <code>VariableListAttribute</code>.
     * <p>
     * This method is called back on <code>theCaller</code> object that was specified during
     * construction.
     * </p>
	 */
    public void setCallback(String methodName)
    {
        m_callback = methodName;
    }

	/**
	 * Get the data associated with the callback method.
	 * 
	 * @return The user callback data is returned.
	 */
	public Object getCallbackData()
	{
		return m_callbackData;
	}

	/**
	 * Set the data associated with the callback mehtod.
	 * 
	 * @param data The user callback data.
	 */
	public void setCallbackData(Object data)
	{
		m_callbackData = data;
	}

    /**
     * Get the element count multiplier.
     * <p>
     * For N elements, the count is: <b>base + ( N * multiplier)</b>.
     * </p>
     * 
     * @return The multiplier value is returned.
     */
    public int getMultiplier()
    {
        return m_multiplier;
    }

	/**
	 * Set the element count multiplier.
     * <p>
     * For N elements, the count is: <b>base + ( N * multiplier)</b>.
     * </p>
	 * 
	 * @param multiplier The multiplier value.
	 */
    public void setMultiplier(int multiplier)
    {
        m_multiplier = multiplier;
    }

    /**
     * Get the <code>Object</code> that is called back when an element is added or removed from the
     * <code>VariableListAttribute</code>
     * 
     * @return A reference to the callback <code>Object</code> is returned.
     */
    public Object getTheCaller()
    {
        return m_theCaller;
    }

	/**
	 * GSt the <code>Object</code> that is called back when an element is added or removed from the
	 * <code>VariableListAttribute</code>
	 * 
	 * @parm theCaller A reference to the callback <code>Object</code>.
	 */
    public void setTheCaller(Object theCaller)
    {
        m_theCaller = theCaller;
    }

	/**
	 * Get the element count base.
     * <p>
     * For N elements, the count is: <b>base + ( N * multiplier)</b>.
     * </p>
	 * 
	 * @return The base value is returned.
	 */
    public int getBase()
    {
        return m_base;
    }

	/**
	 * Set the element count base.
     * <p>
     * For N elements, the count is: <b>base + ( N * multiplier)</b>.
     * </p>
	 * 
	 * @param base The base value.
	 */
    public void setBase(int base)
    {
        m_base = base;
    }

    /**
     * Get the number of attributes in the <code>VariableListAttribute</code>.
     * 
     * @return The number of attributes is returned.
     */
    public long getCount()
    {
        long val = this.getCountAttribute().getValue();
        return (val - this.getBase()) / this.getMultiplier();
    }

	/**
	 * Get a copy of the contents of this <code>VariableListAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>VariableListAttribute</code> is returned.
	 * The children of the <code>variableListAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		NumberAttribute numAttr = (NumberAttribute)getCountAttribute().copy();
		VariableListAttribute attr = new VariableListAttribute(getName(),numAttr,getCallback(),
		    getCallbackData(),getTheCaller(),getMultiplier(),getBase());
		attr.setDisplayName(getDisplayName());
		return attr;
	}

}




