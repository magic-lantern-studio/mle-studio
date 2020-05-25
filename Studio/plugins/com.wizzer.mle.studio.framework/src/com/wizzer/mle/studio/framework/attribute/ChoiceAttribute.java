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
 * This class implements a <code>Choice</code> attribute.
 * It maintains its value as an index into a set of choices.
 */
public class ChoiceAttribute extends Attribute
{
    /*
     * These represent the array of legal choice strings that should be displayed
     * by the GUI.
     */
    private Object[] m_choices;

	/**
	 * A constructor that initializes the <code>ChoiceAttribute</code>.
	 * 
	 * @param name The name of the attribute.
	 * @param choices An array of possible choices.
	 * @param index The current choice.
	 * @param bits The number of bits that are valid as part of the <code>ChoiceAttribute</code>
	 * value.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
    public ChoiceAttribute(String name, Object[] choices, int index, int bits, boolean isReadOnly)
    {
        this.setName(name);
        m_value = new Integer(index);
        m_choices = choices;
        this.setReadOnly(isReadOnly);
        this.setBits(bits);
    }

    /**
     * Get the array of available choices.
     * 
     * @return An array of <b>Object</b>s is returned.
     */
    public Object[] getChoices()
    {
        return m_choices;
    }

    /**
     * Set the array of available chocies.
     * 
     * @param choices An array of possible <b>Object</b>s that may be selected.
     */
    public void setChoices(Object[] choices)
    {
        m_choices = choices;
    }

	/**
	 * Format the current choice value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the vaule of the current choice is returned.
	 */
    public String toValueString()
    {
        return m_choices[this.getIndex()].toString();
    }

    /**
     * Get the index of the current choice.
     * 
     * @return The index is returned.
     */
    public int getIndex()
    {
        Integer i = (Integer) m_value;
        return i.intValue();
    }

	/**
	 * Set the index of the current choice.
	 * 
	 * @param index The index for the current choice
	 */
    public void setIndex(int index)
    {
        this.setValue(new Integer (index));
    }

	/**
	 * Get the <code>ChoiceAttribute</code> type.
	 * 
	 * @return By default, <b>TYPE_CHOICE</b> is always returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getType()
	 */
    public String getType()
    {
        return Attribute.TYPE_CHOICE;
    }

	/**
	 * Get the <code>ChoiceAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the value of the <code>ChoiceAttribute</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.Attribute#getStringValue()
	 */
    public String getStringValue()
    {
        return this.toValueString();
    }

	/**
	 * Format the <code>ChoiceAttribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing this <code>ChoiceAttribute</code> is returned.
	 * Currently, <b>null</b> is always returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#toString()
	 */
    public String toString()
    {
        // XXX - Do something useful here.
        return null;
    }

	/**
	 * Get the value of the <code>ChoiceAttribute</code> as a bit string.
	 * 
	 * @return  A <code>String</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getBitString()
	 */
    public String getBitString()
    {
        String result = Integer.toString(this.getIndex(), 2);
        if (m_bits < result.length())
        {
            //System.out.println("Error: Number is too long: " + result);
        }
        result = this.Pad(result, true, "0");

        //System.out.println ("Choice getBitString(): " + getName() + " " + result);

        return result;
    }

	/**
	 * Load the value of the <code>ChoiceAttribute</code> from a bit string.
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
        String bits = buffer.substring(offset, offset + this.getBits());

        //System.out.println ("Loaded Choice: " +  getName() + " " + bits + " [" + offset + ", " + getBits() + "]");

        this.setValue(new Integer(Integer.parseInt(bits, 2)));

        return offset + this.getBits();
    }

	/**
	 * Get the value of the <code>ChoiceAttribute</code>.
	 * 
	 * @return An <code>int</code> is returned representing the current choice.
	 */
    public int getValue ()
    {
        return this.getIndex();
    }

	/**
	 * Dump the contents of the <b>ChoiceAttribute</b> to the specified print stream.
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
        ps.println(indent + " " + this.getName() + " (Choice) " +  this.getValue() + " " +  this.getBits() + " " +  this.getBitString());
        for (int i = 0; i < this.getChildCount(); i++)
        {
            Attribute a = this.getChild(i);
            a.dump(ps, indent + "|---");
        }
    }

	/**
	 * Test to see if the specified object is different from this <code>ChoiceAttribute</code>.
	 * 
	 * @param obj The <code>Object</code> to test against.
	 * 
	 * @return <b>true</b> will be returned if the specified <code>Object</code> is different
	 * from this <code>ChoiceAttribute</code>. If they are not different, then <b>false</b> will be
	 * returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#isDifferent(Object)
	 */
    public boolean isDifferent (Object v)
    {
        return this.getValue() != ((Integer) v).intValue();
    }
    
	/**
	 * Get a copy of the contents of this <code>ChoiceAttribute</code>.
	 * 
	 * @return A shallow copy of the <code>ChoiceAttribute</code> is returned.
	 * The children of the <code>ChoiceAttribute</code> are <b>not</b> copied.
	 */
	public Attribute copy()
	{
		ChoiceAttribute attr = new ChoiceAttribute(getName(),getChoices(),getIndex(),getBits(),getReadOnly());
		return attr;
	}
    
	/**
	 * Validate the value of the <code>ChoiceAttribute</code>.
	 * 
	 * @return <b>true</b> will be returned if the value of the attribute is valid.
	 * Otherwise, b>false</b> will be returned if the value is invalid.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#validate()
	 */
	public boolean validate()
	{
		// Check for a valid index value.
		if (getIndex() < 0)
			return false;
		if (getIndex() >= m_choices.length)
			return false;
		
		return true;
	}
}
