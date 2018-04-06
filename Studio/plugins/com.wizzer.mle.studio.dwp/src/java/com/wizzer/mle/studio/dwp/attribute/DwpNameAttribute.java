/*
 * DwpNameAttribute.java
 * Created on Jun 3, 2004
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

// Import Magic Lantern Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;


/**
 * This class implements an <code>Attribute</code> for Magic Lantern Digital Workprint
 * Name-based attributes.
 * <p>
 * Name-based Attributes support the following DWP items:
 * <ul>
 * <li>Include</li>
 * <li>DSOFile</li>
 * <li>HeaderFile</li>
 * <li>SourceFile</li>
 * <li>ActorDef</li>
 * </ul>
 * </p>
 * 
 * @author Mark S. Millard
 */
public abstract class DwpNameAttribute extends DwpItemAttribute
{
	/**
	 * A constructor that initializes the <code>DwpNameAttribute</code>.
	 * <p>
	 * Note that the name of the <code>Attribute</code> is mapped to the DWP item type
	 * (i.e. "ActorDef", "DSOFile", "HeaderFile" ...).
	 * </p>
	 * 
	 * @param type The type of the DWP item.
	 * @param value The contents of the DWP item. This parameter may be <b>null</b>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpNameAttribute(String type, Object value, boolean isReadOnly)
	{
		super(type, value, isReadOnly);
		
		// Set the number of significant bits.
		this.setBits(getValue().length() * 16);
	}

	/**
	 * Format the <code>DwpNameAttribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String</code> is returned in the form <i>( dwptype name )</i>.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer str = new StringBuffer(getName());
		str.append(" ");
		String tagString = getTagsString();
		if ((tagString != null) && (! tagString.equals("")))
		{
		    str.append(tagString);
		    str.append(" ");
		}
		str.append(m_value);
		
		return str.toString();
	}

	/**
	 * Get the value of the <code>DwpNameAttribute</code> as a bit string.
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
	 * Load the value of the <code>DwpNameAttribute</code> from a bit string.
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
		this.setValue("");
		String loadedStr = "";

		//System.out.println ("Trying this string: " + getName());
		long charCount = this.getBits() / 16;
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
		String value = name;
		this.setValue(value);

		//System.out.println ("Loaded String: " +  getName() + " " + bits + " [" + offset + ", " + getBits() + "] " + toString());

		return offset + this.getBits();
	}

	/**
	 * Dump the contents of the <b>DwpNameAttribute</b> to the specified print stream.
	 * <p>
	 * The contents of any child attributes will also be dumped.
	 * </p>
	 * 
	 * @param ps The <code>PrintStream</code> to dump to.
	 * @param indent A <code>String</code> specifying an indentation format.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#dump(PrintStream, java.lang.String)
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
	 * Test to see if the specified object is different from this <code>DwpNameAttribute</code>.
	 * 
	 * @param obj The <code>Object</code> to test against.
	 * 
	 * @return <b>true</b> will be returned if the specified <code>Object</code> is different
	 * from this <code>StringAttribute</code>. If they are not different, then <b>false</b> will be
	 * returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#isDifferent(Object)
	 */
	public boolean isDifferent (Object v)
	{
		return this.getValue() == null || this.getValue().compareTo((String) v) != 0;
	}
    
	/**
	 * Validate the value of the <code>DwpNameAttribute</code>.
	 * 
	 * @return <b>true</b> will be returned if the value of the attribute is valid.
	 * Otherwise, b>false</b> will be returned if the value is invalid.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#validate()
	 */
	public boolean validate()
	{
		// XXX - Do something more useful here.
		return true;
	}

	/**
	 * Get the <code>DwpNameAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the value of the <code>DwpNameAttribute</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getStringValue()
	 */
	public String getStringValue()
	{
		//return toString();
		StringBuffer str = new StringBuffer((String)m_value);
		
		return str.toString();
	}

	/**
	 * Get the value of the <code>DwpNameAttribute</code>.
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

}
