/*
 * DwpMediaAttribute.java
 * Created on Jul 8, 2004
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
 * Media item.
 * 
 * @author Mark S. Millard
 */
public class DwpMediaAttribute extends DwpItemAttribute
{
	/**
	 * This class contains the value for a <code>DwpMediaAttribute</code>.
	 * 
	 * @author Mark S. Millard
	 */
	protected class DwpMediaAttributeValue
	{
		/** The Media label. */
		public String m_label = null;
		/** The Media URL. */
		public String m_url = null;
		/** The Media flags. */
		public Integer m_flags = null;
		
		/**
		 * A constructor that initializes the flags, label and url of the
		 * Media Attribute value.
		 * 
		 * @param flags The flags of the value as an <code>Integer</code>.
		 * @param label The label for the value as a <code>String</code>.
		 * @param url The URL of the value as a <code>String</code>.
		 */
		public DwpMediaAttributeValue(Integer flags, String label, String url)
		{
			m_flags = flags;
			m_label = label;
			m_url = url;
		}
	}
	
	/**
	 * A constructor that initializes the <code>DwpMediaAttribute</code>..
	 * <p>
	 * Note that the name of the <code>Attribute</code> is mapped to the DWP item type
	 * (i.e. "Media").
	 * </p>
	 * 
	 * @param flags The flags for the Media as an <code>Integer</code>.
	 * @param label The label for the Media as a <code>String</code>.
	 * @param url The URL for the Media as a <code>String</code>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpMediaAttribute(Integer flags, String label, String url, boolean isReadOnly)
	{
		super("Media", null, isReadOnly);
		
		// Create a helper object to hold the Property value.
		Object attrValue = new DwpMediaAttributeValue(flags,label,url);
		setValue(attrValue);
		
		// Set the number of significant bits.
		this.setBits(getValue().length() * 16);
	}

	/**
	 * Represent the <code>DwpMediaAttribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String</code> is returned in the form <i>( Media flags label url )</i>.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		DwpMediaAttributeValue value = (DwpMediaAttributeValue)m_value;
		StringBuffer str = new StringBuffer(getName());
		str.append(" ");
		String tagString = getTagsString();
		if ((tagString != null) && (! tagString.equals("")))
		{
		    str.append(tagString);
		    str.append(" ");
		}
		str.append(value.m_flags);
		str.append(" ");
		str.append(value.m_label);
		str.append(" ");
		str.append(value.m_url);
		
		return str.toString();
	}

	/**
	 * Get the value of the <code>DwpMediaAttribute</code> as a bit string.
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
	 * Load the value of the <code>DwpMediaAttribute</code> from a bit string.
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
		Integer flags = new Integer(tokens[2]);
		String label = tokens[3];
		String url = tokens[4];
		DwpMediaAttributeValue attrValue = new DwpMediaAttributeValue(flags,label,url);
		this.setValue(attrValue);
		
		//System.out.println ("Loaded String: " +  getName() + " " + bits + " [" + offset + ", " + getBits() + "] " + toString());

		return offset + this.getBits();

	}

	/**
	 * Dump the contents of the <b>DwpMediaAttribute</b> to the specified print stream.
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
	 * Test to see if the specified object is different from this <code>DwpMediaAttribute</code>.
	 * 
	 * @param obj The <code>Object</code> to test against.
	 * 
	 * @return <b>true</b> will be returned if the specified <code>Object</code> is different
	 * from this <code>DwpMediaAttribute</code>. If they are not different, then <b>false</b> will be
	 * returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#isDifferent(Object)
	 */
	public boolean isDifferent (Object v)
	{
		return ((this.getValue() == null) || (this.getValue().compareTo(v.toString()) != 0));
	}

	/**
	 * Validate the value of the <code>DwpMediaAttribute</code>.
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
			DwpMediaAttributeValue attrValue = (DwpMediaAttributeValue)m_value;
			if (attrValue.m_flags == null) rval = false;
			else if (attrValue.m_label == null) rval = false;
			else if (attrValue.m_url == null) rval = false;
			else rval = true;
		}
		
		return rval;
	}

	/**
	 * Get the <code>DwpMediaAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the vaule of the <code>DwpMediaAttribute</code>
	 * is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getStringValue()
	 */
	public String getStringValue()
	{
		//return toString();
		DwpMediaAttributeValue value = (DwpMediaAttributeValue)m_value;
		StringBuffer str = new StringBuffer(value.m_flags.toString());
		str.append(" ");
		str.append(value.m_label);
		str.append(" ");
		str.append(value.m_url);
		
		return str.toString();
	}

	/**
	 * Get the value of the <code>DwpMediaAttribute</code>.
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getValue()
	{
		if (m_value != null)
			//return toString();
			return getStringValue();
		else
			return null;
	}

	/**
	 * Get a copy the contents of the <code>DwpIntPropertyAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>DwpIntPropertyAttribute</code> is returned.
	 * The children of the <code>DwpIntPropertyAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		DwpMediaAttributeValue value = (DwpMediaAttributeValue)m_value;
		DwpMediaAttribute attr = new DwpMediaAttribute(value.m_flags,value.m_label,value.m_url,getReadOnly());
		this.copyTags(attr);
		return attr;
	}
	
	/**
	 * Get the atribute's flags.
	 * 
	 * @return An <code>Integer</code> will be returned.
	 */
	public Integer getValueFlags()
	{
	    if (m_value != null)
	    {
	        DwpMediaAttributeValue value = (DwpMediaAttributeValue)m_value;
	        return value.m_flags;
	    }
	    
	    return null;
	}

	/**
	 * Get the atribute's label.
	 * 
	 * @return Aa <code>String</code> will be returned.
	 */
	public String getValueLabel()
	{
	    if (m_value != null)
	    {
	        DwpMediaAttributeValue value = (DwpMediaAttributeValue)m_value;
	        return value.m_label;
	    }
	    
	    return null;
	}
	
	/**
	 * Get the atribute's URL.
	 * 
	 * @return A <code>String</code> will be returned.
	 */
	public String getValueURL()
	{
	    if (m_value != null)
	    {
	        DwpMediaAttributeValue value = (DwpMediaAttributeValue)m_value;
	        return value.m_url;
	    }
	    
	    return null;
	}
	
	/**
	 * Set the value of the attribute.
	 * 
	 * @param flags The DWP Media flags.
	 * @param label The DWP Media label.
	 * @param url The DWP Media URL.
	 */
	public void setValue(Integer flags, String label, String url)
	{
	    if (m_value == null)
	    {
			// Create a helper object to hold the Property value.
			Object attrValue = new DwpMediaAttributeValue(flags,label,url);
			setValue(attrValue);
	    } else
	    {
	        DwpMediaAttributeValue value = (DwpMediaAttributeValue)m_value;
	        value.m_flags = flags;
	        value.m_label = label;
	        value.m_url = url;
	        setValue(value);
	    }
	}

	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return <b>TYPE_DWP_MEDIA</b> is always returned.
	 */
	public String getType()
	{
		return DwpItemAttribute.TYPE_DWP_MEDIA;
	}

}
