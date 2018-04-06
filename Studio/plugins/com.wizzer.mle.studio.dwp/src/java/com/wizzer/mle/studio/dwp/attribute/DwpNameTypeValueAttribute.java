/*
 * DwpNameTypeValueAttribute.java
 * Created on Jun 4, 2004
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

//Import standard Java packages.
import java.io.PrintStream;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;

/**
 * This class implements an <code>Attribute</code> for Magic Lantern Digital Workprint
 * NameTypeValue-based attributes.
 * <p>
 * NameTypeValue-based Attributes support the following DWP items:
 * <ul>
 * <li>Property</li>
 * </ul>
 * </p>
 * 
 * @author Mark S. Millard
 */
public abstract class DwpNameTypeValueAttribute extends DwpItemAttribute
{
	/**
	 * This class contains the value for a <code>DwpNameTypeValueAttribute</code>.
	 * 
	 * @author Mark S. Millard
	 */
	protected class DwpNameTypeValueAttributeValue
	{
		/** The name of the Attribute value. */
		public String m_name = null;
		/** The type of the Attribute value. */
		public String m_type = null;
		/** The value of the Attribute value. */
		public Object m_value = null;
		
		/**
		 * A constructor that initializes the name, type and value of the
		 * Attribute value.
		 * 
		 * @param name The name of the value as a <code>String</code>.
		 * @param type The type for the value as a <code>String</code>.
		 * @param value The value of the value as an <code>Object</code>.
		 */
		public DwpNameTypeValueAttributeValue(String name, String type, Object value)
		{
			m_name = name;
			m_type = type;
			m_value = value;
		}
		
		/**
		 * Convert the value to a <code>String</code>.
		 * 
		 * @return A <code>String</code> is returned in the form of "<name> <type> <value>".
		 */
		public String toString()
		{
			StringBuffer str = new StringBuffer(m_name);
			str.append(" ");
			str.append(m_type);
			str.append(" ");
			str.append(m_value.toString());

			return str.toString();
		}
	}
	
	/**
	 * Generate the <code>Object</code> representation of the value from the
	 * specified <code>String</code>.
	 * 
	 * @param valueStr The value represented as a <code>String</code>.
	 * 
	 * @return An <code>Object</code> is returned.
	 */
	protected abstract Object loadValue(String valueStr);
	
	/**
	 * A constructor that initializes the <code>DwpNameTypeValueAttribute</code>..
	 * <p>
	 * Note that the name of the <code>Attribute</code> is mapped to the DWP item type
	 * (i.e. "ActorDef", "DSOFile", "HeaderFile" ...).
	 * </p>
	 * 
	 * @param type The type of the DWP item.
	 * @param value The contents of the DWP item. This parameter may be <b>null</b>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpNameTypeValueAttribute(String type, String value, boolean isReadOnly)
	{
		super(type, value, isReadOnly);
	}

	/**
	 * Represent the <code>DwpNameTypeValueAttribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String</code> is returned in the form <i>( dwptype name type value )</i>.
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
		str.append(value.toString());
		
		return str.toString();
	}

	/**
	 * Get the value of the <code>DwpNameTypeValueAttribute</code> as a bit string.
	 * 
	 * @return  A <code>String</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getBitString()
	 */
	public String getBitString()
	{
		String result = "";
		String val = toString();

		for (int i = 0; i < val.length(); i++)
		{
			char c = val.charAt(i);
			String charString = Integer.toString(c, 2);
			charString = Attribute.Pad(charString, true, "0", 16);
			//System.out.println ("Creating a bit string for: " + getName() + " " + charString);
			result = result + charString;
		}

		if (m_bits < result.length())
		{
			//System.out.println("Error: Number is too long: " + result);
		}
		result = this.Pad(result, false, "0");
        
		//System.out.println ("String getBitString(): " + getName() + " " + result);
		return result;
	}

	/**
	 * Load the value of the <code>DwpNameTypeValueAttribute</code> from a bit string.
	 * 
	 * @param buffer The bit string to load from.
	 * @param offset An offset into the specified bit string indicating where to start
	 * loading from.
	 * 
	 * @return A new offset is calculated and returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#loadFromBitString(java.lang.String, int)
	 */
	public int loadFromBitString(String buffer, int offset)
	{
		this.setValue(null);
		String loadedStr = "";
		
		//System.out.println ("Trying this string: " + getName());
		int charCount = this.getBits() / 16;
		for (int i = offset; i < offset + this.getBits(); i += 16)
		{
			String bitString = buffer.substring(i, i + 16);
			char c = (char) Integer.parseInt(bitString, 2);
			if (c == 0) continue;
			//System.out.println ("Char: " +  c);
			loadedStr = loadedStr + c;
		}
		
		String[] tokens = loadedStr.split(" ");
		String name = tokens[2];
		String type = tokens[3];
		// The remaining tokens, except for the last one, are part of the value.
		String valueStr = tokens[4];
		for (int i = 5; i < tokens.length; i++)
		{
			valueStr += " ";
			valueStr += tokens[i];
		}
		Object value = loadValue(valueStr);
		DwpNameTypeValueAttributeValue attrValue = new DwpNameTypeValueAttributeValue(name,type,value);
		this.setValue(attrValue);
		
		//System.out.println ("Loaded String: " +  getName() + " " + bits + " [" + offset + ", " + getBits() + "] " + toString());

		return offset + this.getBits();

	}

	/**
	 * Dump the contents of the <b>DwpNameTypeValueAttribute</b> to the specified print stream.
	 * <p>
	 * The contents of any child attributes will also be dumped.
	 * </p>
	 * 
	 * @param ps The <code>PrintStream</code> to dump to.
	 * @param indent A <code>String</code> specifying an indentation format.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#dump(java.io.PrintStream, java.lang.String)
	 */
	public void dump(PrintStream ps, String indent)
	{
		ps.println(indent + " " + this.getName() + " (DWP Item) " +  this.getValue() + " " +  this.getBits() + " " +  this.getBitString());
		for (int i = 0; i < this.getChildCount(); i++)
		{
			Attribute a = this.getChild(i);
			a.dump(ps, indent + "|---");
		}
	}

	/**
	 * Test to see if the specified object is different from this <code>DwpNameTypeValueAttribute</code>.
	 * 
	 * @param obj The <code>Object</code> to test against.
	 * 
	 * @return <b>true</b> will be returned if the specified <code>Object</code> is different
	 * from this <code>DwpNameTypeValueAttribute</code>. If they are not different, then <b>false</b>
	 * will be returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#isDifferent(Object)
	 */
	public boolean isDifferent (Object obj)
	{
		return ((this.getValue() == null) || (this.getValue().compareTo(obj.toString()) != 0));
	}

	/**
	 * Validate the value of the <code>DwpNameTypeValueAttribute</code>.
	 * 
	 * @return <b>true</b> will be returned if the value of the attribute is valid.
	 * Otherwise, b>false</b> will be returned if the value is invalid.
	 *
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#validate()
	 */
	public boolean validate()
	{
		boolean rval;
		
		if (m_value == null)
		{
			rval = false;
		} else
		{
			DwpNameTypeValueAttributeValue attrValue = (DwpNameTypeValueAttributeValue)m_value;
			if (attrValue.m_name == null) rval = false;
			else if (attrValue.m_type == null) rval = false;
			else if (attrValue.m_value == null) rval = false;
			else rval = true;
		}
		
		return rval;
	}

	/**
	 * Get the <code>DwpNameTypeValueAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the vaule of the <code>DwpNameTypeValueAttribute</code>
	 * is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getStringValue()
	 */
	public String getStringValue()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;		
		return value.toString();
	}

	/**
	 * Get the value of the <code>DwpNameTypeValueAttribute</code>.
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getValue()
	{
		if (m_value != null)
			return getStringValue();
		else
			return null;
	}
	
	/**
	 * Get the name component of the value.
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getValueName()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		return value.m_name;
	}
	
	/**
	 * Get the type component of the value.
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getValueType()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		return value.m_type;
	}
	
	/**
	 * Get the value component of the value.
	 * 
	 * @return An <code>Object</code> is returned.
	 */
	public Object getValueValue()
	{
		DwpNameTypeValueAttributeValue value = (DwpNameTypeValueAttributeValue)m_value;
		//return value.m_value.toString();
		return value.m_value;
	}

	/**
	 * Set the value from the specified parameters.
	 * 
	 * @param name The value name.
	 * @param type The value type.
	 * @param value The value value.
	 *
	 */
	public void setValue(String name, String type, String value)
	{
		Object obj = loadValue(value);
		
		// Create a helper object to hold the DWP item's value.
		Object helper = new DwpNameTypeValueAttributeValue(name,type,obj);
		setValue(helper);
	}

}
