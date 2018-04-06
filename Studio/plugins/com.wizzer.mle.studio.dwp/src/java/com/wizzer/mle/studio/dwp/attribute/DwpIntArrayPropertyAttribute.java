/*
 * DwpIntArrayPropertyAttribute.java
 * Created on Jul 7, 2004
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
package com.wizzer.mle.studio.dwp.attribute;

// Import standard Java classes.
import java.util.Vector;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;


/**
 * This class implements an <code>Attribute</code> for a Magic Lantern Digital Workprint
 * item representing an <i>IntArray</i> property.
 * <p>
 * An <i>IntArray</i> property is a multi-dimensional array of <i>int</i>s and is represented
 * here as a multi-dimensional array of integer values.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class DwpIntArrayPropertyAttribute extends DwpPropertyAttribute
{
	/** DWP Property attribute type. */
	public static final String TYPE_DWP_INTARRAYPROPERTY = "com.wizzer.mle.studio.dwp.attribute.INTARRAYPROPERTY";

	/**
	 * A constructor that initializes the value of the DWP IntArray property Attribute.
	 * 
	 * @param name The name of the DWP IntArray property as a <code>String</code>.
	 * @param type The type for the DWP IntArray property as a <code>String</code>.
	 * @param value The value for the DWP IntArray property as a multi-dimensional array
	 * of <code>Integer</code>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpIntArrayPropertyAttribute(String name, String type, Integer[] value, boolean isReadOnly)
	{
		super(name,type,value,isReadOnly);
	}
	
	/**
	 * Generate the <code>Object</code> representation of the value from the
	 * specified <code>String</code>. The object will be a multi-dimenstional array of
	 * <code>Integer</code> extracted from the space-delimitted tokens in the <i>valueStr</i>
	 * argument.
	 * 
	 * @param valueStr The value represented as a <code>String</code>.
	 * 
	 * @return An <code>Object</code> is returned.
	 */
	protected Object loadValue(String valueStr)
	{
		Vector values = new Vector();
	    String[] tokens = valueStr.split(" ");
		for (int i = 0; i < tokens.length; i++)
		{
		    if ((! tokens[i].equals("[")) &&
		        (! tokens[i].equals("]")) &&
		        (! tokens[i].equals(",")))
		        values.add(tokens[i]);
		}
		
		Integer[] value = new Integer[values.size()];
		for (int i = 0; i < values.size(); i++)
		{
		    String intValue = (String)values.elementAt(i);
		    intValue = intValue.trim();
			value[i] = new Integer(intValue);
		}
		return value;
	}

	/**
	 * Represent the <code>DwpIntArrayPropertyAttribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String</code> is returned in the form
	 * <i>( Property name IntArray [ val1, val2, ..., valn ] )</i>.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		StringBuffer str = new StringBuffer(getName());
		str.append(" ");
		String tagString = getTagsString();
		if ((tagString != null) && (! tagString.equals("")))
		{
		    str.append(tagString);
		    str.append(" ");
		}
		str.append(value.m_name);
		str.append(" ");
		str.append(value.m_type);
		str.append(" [");
		Integer[] intValue = (Integer[])value.m_value;
		for (int i = 0; i < intValue.length; i++)
		{
			str.append(" ");
			str.append(intValue[i]);
			if (i != (intValue.length - 1))
				str.append(" ,");
		}
		str.append(" ]");
		
		return str.toString();
	}

	/**
	 * Get a copy the contents of the <code>DwpIntArrayPropertyAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpIntArrayPropertyAttribute</code> is returned.
	 * The children of the <code>DwpIntArrayPropertyAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		DwpIntArrayPropertyAttribute attr = new DwpIntArrayPropertyAttribute(value.m_name,value.m_type,(Integer[])value.m_value,getReadOnly());
		this.copyTags(attr);
		return attr;
	}

	/**
	 * Get the <code>DwpIntArrayAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the vaule of the <code>DwpIntArrayAttribute</code>
	 * is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getStringValue()
	 */
	public String getStringValue()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		StringBuffer str = new StringBuffer(value.m_name);
		str.append(" ");
		str.append(value.m_type);
		str.append(" [");
		Integer[] intValue = (Integer[])value.m_value;
		for (int i = 0; i < intValue.length; i++)
		{
			str.append(" ");
			str.append(intValue[i]);
			if (i != (intValue.length - 1))
				str.append(" ,");
		}
		str.append(" ]");
		
		return str.toString();
	}
	
	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_INTARRAYPROPERTY</b> is always returned.
	 */
	public String getType()
	{
		return TYPE_DWP_INTARRAYPROPERTY;
	}

}
