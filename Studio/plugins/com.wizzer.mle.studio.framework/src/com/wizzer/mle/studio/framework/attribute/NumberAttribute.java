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

// Import Tool Framework packages.
import com.wizzer.mle.studio.framework.FrameworkLog;

/**
 * This class implements a <code>Number</code> attribute.
 * 
 * @author Mark S. Millard
 */
public class NumberAttribute extends Attribute
{
	/**
	 * A constructor that initializes the <code>NumberAttribute</code>.
	 * 
	 * @param name The name of the attribute.
	 * @param value The value of the attribute.
	 * @param characters The number of bits that are valid as part of the <code>NumberAttribute</code>
	 * value.
	 * @param radix The radix for the number. For example, <b>10</b> is would be used for a decimal
	 * numeric system and <b>2</b> would be used for a binary numeric system.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
    public NumberAttribute(String name, long value, int bits, int radix, boolean isReadOnly)
    {
        this.setName(name);
        this.setValue(new Long (value));
        this.setBits(bits);
        m_radix = radix;
        this.setReadOnly(isReadOnly);
    }

	/**
	 * Get the value of the <code>NumberAttribute</code>.
	 * 
	 * @return A <code>long</code> is returned. <b>-1</b> will be returned if the value is not set.
	 */
    public long getValue()
    {
        if (m_value == null)
        {
            return -1;
        }
        Long l = (Long) m_value;
        return l.longValue();
    }

    /**
     * Increment the value of the number.
     * 
     * @return The bumped value is returned.
     */
    public long bump()
    {
        long newValue = this.getValue() + 1;

        // XXX - This should wrap.
        long upper = (long)((long)1 << m_bits);
        if (newValue >= upper)
        {
            newValue = 0;
        }

        this.setValue(new Long (newValue));

        return newValue;
    }

    /**
     * Set the value of the number.
     * 
     * @param str The value of the number to set in <b>String</b> format.
     */
    public void setValue(String str)
    {
        this.setValue(new Long (Long.parseLong(str, m_radix)));
    }

    private int m_min;   // The minimal range the value may have.
    private int m_max;   // The maximum range the value may have.
    private int m_radix; // The base of the number.

	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return By default, <b>TYPE_NUMBER</b> is always returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getType()
	 */
    public String getType()
    {
        return Attribute.TYPE_INTEGER;
    }

    /**
     * Get the minimal value that the <code>NumberAttribute</code> can have.
     * 
     * @return The minimal value of the valid range of values is returned.
     */
    public int getMin()
    {
        return m_min;
    }

	/**
	 * Set the minimal value that the <code>NumberAttribute</code> can have.
	 * 
	 * @param min The minimal value of the valid range of values.
	 */
    public void setMin(int min)
    {
        m_min = min;
    }

	/**
	 * Get the maximum value that the <code>NumberAttribute</code> can have.
	 * 
	 * @return The maximum value of the valid range of values is returned.
	 */
    public int getMax()
    {
        return m_max;
    }

	/**
	 * Set the maximum value that the <code>NumberAttribute</code> can have.
	 * 
	 * @param max The maximum value of the valid range of values.
	 */
    public void setMax(int max)
    {
        m_max = max;
    }

    /**
     * Get the base or radix of the <code>NumberAttribute</code>.
     * 
     * @return The radix is returned.
     */
    public int getRadix()
    {
        return m_radix;
    }

	/**
	 * Set the base or radix of the <code>NumberAttribute</code>.
	 * 
	 * @parm radix The radix.
	 */
    public void setRadix(int radix)
    {
        m_radix = radix;
    }

	/**
	 * Format the <code>NumberAttribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing this <code>NumberAttribute</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#toString()
	 */
    public String toString()
    {
        return this.getName() + ": " + this.toValueString();
    }

	/**
	 * Get the <code>NumberAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the value of the <code>NumberAttribute</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getStringValue()
	 */
    public String getStringValue()
    {
        return this.toValueString();
    }

	/**
	 * Format the <code>NumberAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the vaule of the <code>NumberAttribute</code> is returned.
	 */
    public String toValueString()
    {
        return Long.toString(((Long)m_value).longValue(), m_radix);
    }

	/**
	 * Get the value of the <code>NumberAttribute</code> as a bit string.
	 * 
	 * @return  A <code>String</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getBitString()
	 */
    public String getBitString()
    {
        String result = Long.toBinaryString(this.getValue());
        if (m_bits < result.length())
        {
            result = result.substring(result.length() - (int)m_bits);
        }
        result = this.Pad(result, true, "0");
        //System.out.println ("Number getBitString(): " + getName() + " " + result);
        return result;
    }

	/**
	 * Load the value of the <code>NumberAttribute</code> from a bit string.
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
        String bits = "";
        try
        {
        	bits = buffer.substring((int)offset, (int)offset + (int)this.getBits());
        }
        catch (StringIndexOutOfBoundsException e)
        {
			String message = new String("String error in NumberAttribute: " + this.getName());
			FrameworkLog.logWarning(message);
			
            // XXX - is this safe?
            return offset;
        }

        this.setValue(new Long(Long.parseLong(bits, 2)));

        //System.out.println ("Loaded Number: " +  getName() + " " + bits + " [" + offset + ", " + getBits() + "] " + toValueString());

        return offset + this.getBits();
    }

	/**
	 * Dump the contents of the <b>NumberAttribute</b> to the specified print stream.
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
        ps.println(indent + " " + this.getName() + " (Number) " +  this.getValue() + " " +  this.getBits() + " " +  this.getBitString());
        for (int i = 0; i < this.getChildCount(); i++)
        {
            Attribute a = this.getChild(i);
            a.dump(ps, indent + "|---");
        }
    }

	/**
	 * Test to see if the specified object is different from this <code>Number</code>.
	 * 
	 * @param obj The <code>Object</code> to test against.
	 * 
	 * @return <b>true</b> will be returned if the specified <code>Object</code> is different
	 * from this <code>NumberAttribute</code>. If they are not different, then <b>false</b> will be
	 * returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#isDifferent(Object)
	 */
    public boolean isDifferent (Object v)
    {
        return this.getValue() != ((Long) v).longValue();
    }
    
	/**
	 * Get a copy of the contents of this <code>NumberAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>NumberAttribute</code> is returned.
	 * The children of the <code>NumberAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		NumberAttribute attr = new NumberAttribute(getName(),getValue(),getBits(),getRadix(),getReadOnly());
		return attr;
	}
    
	/**
	 * Validate the value of the <code>NumberAttribute</code>.
	 * 
	 * @return <b>true</b> will be returned if the value of the attribute is valid.
	 * Otherwise, b>false</b> will be returned if the value is invalid.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#validate()
	 */
    public boolean validate()
    {
    	// Check against the valid range of values.
    	if (((Long)m_value).longValue() < m_min )
    		return false;
		if (((Long)m_value).longValue() > m_max )
				return false;
   		
    	return true;
    }
}
