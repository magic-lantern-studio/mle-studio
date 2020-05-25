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
import java.util.Vector;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.Utilities;


/**
 * <code>Attribute</code> is the base class for all attributes of a domain model supported by
 * the Wizzer Works Tool Framework.
 * <p>
 * It maintains a set of type constants for its subclasses to use to identify themselves.
 * In general, this technique breaks encapsulation, but it's better than using RTTI.
 * </p><p>
 * Every Attribute has exactly one value stored as a generic <code>Object</code>.
 * Each subclass should define its own <code>getValue()</code> method that returns
 * an appropriately typed value. Attributes are inherently tree-structured
 * (this class maintains a <code>Vector</code> of children).
 * Children always know who their parents are.
 * </p>
 */
public abstract class Attribute extends Observable implements IAttribute
{
    // The children of this Attribute.
    private Vector m_children = new Vector();
    // The parent of this Attribute.
    private Attribute m_parent = null;
    // The name of this Attribute.
    private String m_name;
    // Flag indicating that the Attribute is read only and can't be set.
    private boolean m_readOnly;
    // Additional data associated with this Attribute.
    private Object m_ancillaryData = null;

    // If dangerous is true, read-only attributes may be edited. Be careful. 
    private static boolean g_dangerous = false;

	/** The value of the attribute. */
	protected Object m_value;
	/** A reference to the child <code>Attribute</code>s by name. */
    protected Hashtable m_childrenByName = new Hashtable();
    /** The number of bits representing the <code>Attribute</code>. */
    protected int m_bits = 6; // XXX - why 6?

	/** String attribute type. */
    public static final String TYPE_STRING        = "com.wizzer.mle.studio.framework.attribute.STRING";
	/** Boolean attribute type. */
    public static final String TYPE_BOOLEAN       = "com.wizzer.mle.studio.framework.attribute.BOOLEAN";
	/** Integer attribute type. */
    public static final String TYPE_INTEGER       = "com.wizzer.mle.studio.framework.attribute.INTEGER";
	/** Choice attribute type. */
    public static final String TYPE_CHOICE        = "com.wizzer.mle.studio.framework.attribute.CHOICE";
	/** Variable List attribute type. */
    public static final String TYPE_VARIABLE_LIST = "com.wizzer.mle.studio.framework.attribute.VARIABLELIST";
	/** File attribute type. */
    public static final String TYPE_FILE          = "com.wizzer.mle.studio.framework.attribute.FILE";
	/** HEX String attribute type. */
    public static final String TYPE_HEX_STRING    = "com.wizzer.mle.studio.framework.attribute.HEXSTRING";
    
	/** Boolean indicating that the attribute is being loaded. */
    public static boolean LOADING              = false;

    // Abstract member functions:

    /**
     * Represent the <code>Attribute</code> as a <code>String</code>.
     * 
     * @return A <code>String</code> is returned.
     */
    public abstract String toString();

    /**
     * Get the value of the <code>Attribute</code> as a bit string.
     * 
     * @return  A <code>String</code> is returned.
     */
    public abstract String getBitString();

    /**
     * Load the value of the <code>Attribute</code> from a bit string.
     * 
     * @param buffer The bit string to load from.
     * @param offset An offset into the specified bit string indicating where to start
     * loading from.
     * 
     * @return The new offset is returned.
     */
    public abstract int loadFromBitString(String buffer, int offset);

	/**
	 * Get a copy the contents of the <code>Attribute</code>.
	 * 
	 * @return A shallow copy of the <code>Attribute</code> is returned.
	 * The children of the <code>Attribute</code> are <b>not</b> copied.
	 */
	public abstract Attribute copy();

    /**
     * Dump the contents of the <b>Attribute</b> to the specified print stream.
     * 
     * @param ps The <code>PrintStream</code> to dump to.
     * @param indent A <code>String</code> specifying an indentation format.
     */
    public abstract void dump(PrintStream ps, String indent);

    /**
     * Test to see if the specified object is different from this <code>Attribute</code>.
     * 
     * @param obj The <code>Object</code> to test against.
     * 
     * @return <b>true</b> will be returned if the specified <code>Object</code> is different
     * from this <code>Attribute</code>. If they are not different, then <b>false</b> will be
     * returned.
     */
    public abstract boolean isDifferent(Object obj);
    
    /**
     * Validate the value of the <code>Attribute</code>.
     * 
     * @return <b>true</b> will be returned if the value of the attribute is valid.
     * Otherwise, b>false</b> will be returned if the value is invalid.
     */
    public abstract boolean validate();

    /**
     * Set the value of the <code>Attribute</code>.
     * 
     * @param v The value of an <code>Attribute</code> must always be an
     * <b>Object</b>.
     */
    public void setValue(Object v)
    {
        if (v == null || !this.isDifferent(v))
        {
            return;
        }

        m_value = v;
        this.setChanged();
        if (!LOADING)
        {
            this.notifyObservers(this);
        }
    }

	/**
	 * Add a child <code>Attribute</code> to this one.
	 * 
	 * @param child The child <b>Attribute</b> node.
	 * @param o An <code>Observer</code> that will be monitoring the child
	 * <code>Attribute</code>.
	 * 
	 * @return A reference to the added child is returned.
	 */
    public Attribute addChild(Attribute child, Observer o)
    {
    	// Add the child.
        m_children.add(child);
        child.setParent(this);
        m_childrenByName.put(child.getName(), child);

        // Add the observer to the child.
        child.addObserver(o);
        
        // Notify this attributes observers that a new child was added.
        this.setChanged();
        this.notifyObservers(this);

        return child;
    }

    /**
     * Retrieve a child <code>Attribute</code> by name.
     * 
     * @param name The name of the child to retrieve.
     * 
     * @return If the child is found, then a reference to that child's
     * <code>Attribute</code> node is returned. If the child is not found, then
     * <b>null</b> will be returned.
     */
    public Attribute getChildByName(String name)
    {
        Attribute a = (Attribute)m_childrenByName.get(name);
        return a;
    }

    /**
     * Delete all children of this <code>Attribute</code>.
     */
    public void deleteAllChildren()
    {
    	// Remove the children.
        m_children.removeAllElements();
        m_childrenByName.clear();
        
        // Notify all observers that the children were deleted.
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Deletes a specific child <code>Attribute</code>.
     * 
     * @param attr The child <code>Attribute</code> to remove.
     */
    public void deleteChild(Attribute attr)
    {
    	// XXX - test to see if this attribute exists and throw an exception if it doesn't?
    	
    	// Remove the specified child.
        m_children.remove(attr);
        m_childrenByName.remove(attr.getName());
        
        // Notify all observers that the child was removed.
        this.setChanged();
        this.notifyObservers();
    }

	/**
	 * Deletes a specific child <code>Attribute</code> by index.
	 * 
	 * @param index The location of the child <code>Attribute</code> to remove.
	 */
    public void deleteChild(int index)
    {
    	// XXX - test to see if the index is valid and throw an exception if it isn't?
    	
		// Remove the specified child.
		Attribute attr = (Attribute)m_children.get(index);
        m_children.remove(index);
        m_childrenByName.remove(attr.getName());
        
		// Notify all observers that the child was removed.
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Get the number of children parented by this <code>Attribute</code>.
     * 
     * @return The number of children is returned.
     */
    public int getChildCount()
    {
        return m_children.size();
    }

    /**
     * Get the child attribute specified by the given index.
     * 
	 * @param index The location of the child <code>Attribute</code> to retrieve.
	 * 
     * @return The reference to the child <code>Attribute</code> is returned.
     */
    public Attribute getChild(int index)
    {
        return (Attribute)m_children.elementAt(index);
    }

    /**
     * Set the specified child <code>Attribute</code> at the given location.
     * 
     * @param index The location of the child <code>Attribute</code> to set.
     * @param child The child <code>Attribute</code> being set.
     */
    public void setChild(int index, Attribute child)
    {
        m_children.set(index, child);
    }
 
	/**
	 * Replace the specified child <code>Attribute</code> at the given location.
	 * 
	 * @param index The location of the child <code>Attribute</code> to replace.
	 * @param child The new child <code>Attribute</code> being set.
	 * 
	 * @return The old child <code>Attribute</code> that was replaced.
	 */
	public Attribute replaceChild(int index, Attribute child)
	{
		Attribute prevAttr = (Attribute)m_children.get(index);
		
		child.setParent(prevAttr.getParent());
		
		m_children.set(index, child);
		
		m_childrenByName.remove(prevAttr.getName());
		m_childrenByName.put(child.getName(),child);
		
		setChanged();
		notifyObservers(this);
		
		return prevAttr;
	}
   
    /**
     * Get the index of the specified child.
     * 
     * @param child The child <code>Attribute</code> in which to retrieve the
     * index for.
     * 
     * @return The index of the specified child <code>Attribute</code> is returned.
     */
    public int getChildLocation(Attribute child)
    {
    	return m_children.indexOf(child);
    }

    /**
     * Get the <code>Attribute</code> type.
     * <p>
     * Subclass implementations should return one of the following:
     * <ul>
     * <li>TYPE_BOOLEAN</li>
     * <li>TYPE_INTEGER</li>
     * <li>TYPE_STRING</li>
     * <li>TYPE_CHOICE</li>
     * <li>TYPE_FILE</li>
     * <li>TYPE_VARIABLE_LIST</li>
     * <li>TYPE_HEX_STRING</li>
     * <li>TYPE_UNKNOWN</li>
     * </ul>
     * </p>
     * 
     * @return By default, <b>TYPE_UNKNOWN</b> is always returned.
     */
    public String getType()
    {
        return Attribute.TYPE_UNKNOWN;
    }

    /**
     * Get the name of the <code>Attribute<code>.
     * 
     * @return A <code>String</code> is returned.
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Get the value of the <code>Attribute</code> as a <code>String</code>.
     * 
     * @return A <code>String</code> is returned representing the value of the <code>Attribute</code>.
     */
    public abstract String getStringValue();

    /**
     * Set the name of the <code>Attribute</code>.
     * 
     * @param name The name to set.
     */
    public void setName(String name)
    {
        m_name = name;
    }

    /**
     * Test whether the <code>Attribute</code> is read-only.
     * 
     * @return If the attribute is read-only, then <b>true</b> will be returned.
     * Otherwise, <b>false</b> will be returned.
     */
    public boolean isReadOnly()
    {
        return m_readOnly && !g_dangerous;
    }

    /**
     * Set the <code>Attribute</code> to read-only status.
     * 
     * @param readOnly If <b>true</b>, then the <code>Attribute</code> will be set read-only.
     * Otherwise, <b>false</b> will enable the <code>Attribute</code> for editting.
     */
    public void setReadOnly(boolean readOnly)
    {
        m_readOnly = readOnly;
    }
    
	/**
	 * Get the <code>Attribute</code> read-only status. This method does not take
	 * into account if the <code>Attribute</code> is in dangerous mode.
	 * 
	 * @return <b>true</b> is returned if the <code>Attribute</code> is read-only.
	 * Otherwise <b>false</b> will be returned.
	 */
    public boolean getReadOnly()
    {
    	return m_readOnly;
    }

    /**
     * Get the number of bits representing the <code>Attribute</code> value.
     * 
     * @return The number of bits is returned.
     */
    public int getBits()
    {
        return m_bits;
    }

    /**
     * Set the number of bits representing the <code>Attribute</code> value.
     * 
     * @param bits The number of bits.
     */
    public void setBits(int bits)
    {
        m_bits = bits;
    }

    /**
     * Get the <b>Attribute</b> value as an array of bytes.
     * <p>
     * This is how attributes are serialized (for saving or transmission). They can easily
     * be converted into bit strings OR byte arrays.
     * </p>
     * 
     * @return An array of bytes is returned containing the value of the <b>Attribute</b>.
     */

    public byte[] getBytes()
    {
        return Utilities.bitStringToByteRepresentation(this.getRecursiveBitString());
    }

	/**
	 * Pad the <code>input</code> string with the supplied <code>padding</code> string.
	 * 
	 * @param input The input <code>String</code> to pad.
	 * @param left A flag indicating whether the padding should be left-justified or not.
	 * If <b>true</b>, then padding will occur to the left of the input. If <b>false</b>,
	 * then padding will occur to the right of the input.
	 * @param padding The <code>String</code> to use for padding.
	 * @param The desired length for the padded result (in bits).
	 * 
	 * @return The padded <code>String</code> is returned.
	 */
    public static String Pad(String input, boolean left, String padding, long desiredLength)
    {
        String result = input;
        
        // Make sure it's long enough!
        while (desiredLength > result.length())
        {
            if (left)
            {
                result = padding + result;
            }
            else
            {
                result = result + padding;
            }
        }
        return result;
    }

    /**
     * Pad the <code>input</code> string with the supplied <code>padding</code> string.
     * 
     * @param input The input <code>String</code> to pad.
     * @param left A flag indicating whether the padding should be left-justified or not.
     * If <b>true</b>, then padding will occur to the left of the input. If <b>false</b>,
     * then padding will occur to the right of the input.
     * @param padding The <code>String</code> to use for padding.
     * 
     * @return The padded <code>String</code> is returned.
     */
    protected String Pad(String input, boolean left, String padding)
    {
        return Attribute.Pad(input, left, padding, m_bits);
    }

	/**
	 * Recursively get the bits representing the <code>Attribute</code> value
	 * and its children.
	 * 
	 * @return The <code>String</code> of bits is returned.
	 */
    public String getRecursiveBitString()
    {
        String result = this.getBitString();
        //System.out.println("getBitString: " + this.getName() + "... Length: " + result.length() + "[" + this.value + "].");
        for (int i = 0; i < this.getChildCount(); i++)
        {
            Attribute a = this.getChild(i);
            result += a.getRecursiveBitString();
        }
        return result;
    }

    /**
     * Test whether read-only attributes may be editted.
     * <p>
     * Dangerous refers to the properties file setting that allows users to edit data that would otherwise
     * be read-only. Note: this is a static property, so all <code>Attribute</code>s are either dangerous or
     * they all are not. Change this to non-static if they should be settable individually.
     * In this case, there's no obvious way to inform the system via properties files, though.
     * 
     * @return <b>true</b> will be returned if read-only attributes may be editted.
     * Otherwise <b>false</b> will be returned.
     */
    public static boolean isDangerous()
    {
        return g_dangerous;
    }

    /**
     * Enable editting of read-only attributes.
     * 
     * @param dangerous If <b>true</b>, all read-only attributes will become editable.
     * Turn off editting by passing in <b>false</b>.
     */
    public static void setDangerous(boolean dangerous)
    {
        Attribute.g_dangerous = dangerous;
    }

    /**
     * Get the parent <b>Attribute</b>.
     * 
     * @return The parent of this attribute is returned. This may be <b>null</b> if this
     * <b>Attribute</b> is the root of an attribute tree.
     */
    public Attribute getParent()
    {
        return m_parent;
    }

    /**
     * Set the parent <b>Attribute</b>.
     * 
     * @param parent The parent <b>Attribute</b> to set.
     */
    public void setParent(Attribute parent)
    {
        m_parent = parent;
    }
    
    /**
     * Get the ancillary data that may have been associated with this
     * <code>Attribute</code>.
     * 
     * @return An <code>Object</code> is returned. <b>null</b> will be returned
     * if there is none.
     */
    public Object getAncillaryData()
    {
    	return m_ancillaryData;
    }
    
	/**
	 * Set the ancillary data to associate with this <code>Attribute</code>.
	 * 
	 * @oaram data An <code>Object</code> managing the ancillary data.
	 */
    public void setAncillaryData(Object data)
    {
		m_ancillaryData = data;
    }

}
