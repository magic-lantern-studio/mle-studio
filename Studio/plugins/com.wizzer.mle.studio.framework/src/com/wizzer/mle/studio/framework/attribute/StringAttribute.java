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

// Import standard Java packages.
import java.io.PrintStream;

/**
 * This class implements a <code>String</code> attribute.
 * 
 * @author Mark S. Millard
 */
public class StringAttribute extends Attribute
{
    /**
     * A constructor that initializes the <code>StringAttribute</code>.
     * 
     * @param name The name of the attribute.
     * @param value The value of the attribute.
     * @param characters The number of characters that are valid as part of the <code>StringAttribute</code>
     * value.
     * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
     */
    public StringAttribute(String name, String value, int characters, boolean isReadOnly)
    {
        this.setName(name);
        this.setValue(value);
        this.setReadOnly(isReadOnly);
        this.setBits(characters * 16);
    }

    /**
     * Format the <code>StringAttribute</code> as a <code>String</code>.
     * 
     * @return A <code>String<code> representing this <code>StringAttribute</code> is returned.
     * 
     * @see com.wizzer.mle.studio.framework.attribute.Attribute#toString()
     */
    public String toString()
    {
        return (String) m_value;
    }

	/**
	 * Get the <code>StringAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the value of the <code>StringAttribute</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getStringValue()
	 */
    public String getStringValue()
    {
        return (String) m_value;
    }

	/**
	 * Get the <code>StringAttribute</code> type.
	 * 
	 * @return By default, <b>TYPE_STRING</b> is always returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getType()
	 */
    public String getType()
    {
        return Attribute.TYPE_STRING;
    }

    /**
     * Get the value of the <code>StringAttribute</code>.
     * 
     * @return A <code>String</code> is returned.
     */
    public String getValue()
    {
        return (String) m_value;
    }

	/**
	 * Get the value of the <code>StringAttribute</code> as a bit string.
	 * 
	 * @return  A <code>String</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getBitString()
	 */
    public String getBitString()
    {
        String result = "";
        String val = (String) m_value;

        for (int i = 0; i < val.length(); i++)
        {
            char c = val.charAt(i);
            String charString = Integer.toString(c, 2);
            charString = StringAttribute.Pad(charString, true, "0", 16);
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
	 * Load the value of the <code>StringAttribute</code> from a bit string.
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
        //System.out.println ("Trying this string: " + getName());
        long charCount = this.getBits() / 16;
        for (int i = offset; i < offset + this.getBits(); i += 16)
        {
        	String bitString = buffer.substring(i, i + 16);
            char c = (char) Integer.parseInt(bitString, 2);
            if (c == 0) continue;
            //System.out.println ("Char: " +  c);
            this.setValue(((String) m_value) + c);
        }

        //System.out.println ("Loaded String: " +  getName() + " " + bits + " [" + offset + ", " + getBits() + "] " + toString());

        return offset + this.getBits();
    }

	/**
	 * Dump the contents of the <b>StringAttribute</b> to the specified print stream.
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
        ps.println(indent + " " + this.getName() + " (String) " +  this.getValue() + " " +  this.getBits() + " " +  this.getBitString());
        for (int i = 0; i < this.getChildCount(); i++)
        {
            Attribute a = this.getChild(i);
            a.dump(ps, indent + "|---");
        }
    }

	/**
	 * Test to see if the specified object is different from this <code>StringAttribute</code>.
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
	 * Get a copy of the contents of this <code>StringAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>StringAttribute</code> is returned.
	 * The children of the <code>StringAttribute</code> are <b>not</b> copied.
	 */
    public Attribute copy()
    {
    	StringAttribute attr = new StringAttribute(getName(),getValue(),getBits() / 16,getReadOnly());
    	return attr;
    }
    
	/**
     * Validate the value of the <code>StringAttribute</code>.
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
}
