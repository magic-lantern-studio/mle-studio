// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2000-2020 Wizzer Works
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.framework.attribute;

// Import standard Java packages.
import java.io.PrintStream;
import java.util.Observer;

/**
 * This interface defines an Attribute of a domain Table.
 * 
 * @author Mark S. Millard
 */
public interface IAttribute
{
	/** Attribute type unknown. */
	public static final String TYPE_UNKNOWN = "com.wizzer.mle.studio.framework.attribute.UNKNOWN";

	/**
	 * Represent the <code>Attribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String toString();

	/**
	 * Get the value of the <code>Attribute</code> as a bit string.
	 * 
	 * @return  A <code>String</code> is returned.
	 */
	public String getBitString();

	/**
	 * Load the value of the <code>Attribute</code> from a bit string.
	 * 
	 * @param buffer The bit string to load from.
	 * @param offset An offset into the specified bit string indicating where to start
	 * loading from.
	 * 
	 * @return The new offset is returned.
	 */
	public int loadFromBitString(String buffer, int offset);

	/**
	 * Get a copy the contents of the <code>Attribute</code>.
	 * 
	 * @return A shallow copy of the <code>Attribute</code> is returned.
	 * The children of the <code>Attribute</code> are <b>not</b> copied.
	 */
	public Attribute copy();

	/**
	 * Dump the contents of the <b>Attribute</b> to the specified print stream.
	 * 
	 * @param ps The <code>PrintStream</code> to dump to.
	 * @param indent A <code>String</code> specifying an indentation format.
	 */
	public void dump(PrintStream ps, String indent);

	/**
	 * Test to see if the specified object is different from this <code>Attribute</code>.
	 * 
	 * @param obj The <code>Object</code> to test against.
	 * 
	 * @return <b>true</b> will be returned if the specified <code>Object</code> is different
	 * from this <code>Attribute</code>. If they are not different, then <b>false</b> will be
	 * returned.
	 */
	public boolean isDifferent(Object obj);
    
	/**
	 * Validate the value of the <code>Attribute</code>.
	 * 
	 * @return <b>true</b> will be returned if the value of the attribute is valid.
	 * Otherwise, b>false</b> will be returned if the value is invalid.
	 */
	public boolean validate();

	/**
	 * Set the value of the <code>Attribute</code>.
	 * 
	 * @param v The value of an <code>Attribute</code> must always be an
	 * <b>Object</b>.
	 */
	public void setValue(Object v);

	/**
	 * Get the value of the <code>Attribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String</code> is returned representing the value of the <code>Attribute</code>.
	 */
	public String getStringValue();

	/**
	 * Add a child <code>Attribute</code> to this one.
	 * 
	 * @param child The child <b>Attribute</b> node.
	 * @param o An <code>Observer</code> that will be monitoring the child
	 * <code>Attribute</code>.
	 * 
	 * @return A reference to the added child is returned.
	 */
	public Attribute addChild(Attribute child, Observer o);

	/**
	 * Deletes a specific child <code>Attribute</code>.
	 * 
	 * @param attr The child <code>Attribute</code> to remove.
	 */
	public void deleteChild(Attribute attr);

	/**
	 * Deletes a specific child <code>Attribute</code> by index.
	 * 
	 * @param index The location of the child <code>Attribute</code> to remove.
	 */
	public void deleteChild(int index);

	/**
	 * Delete all children of this <code>Attribute</code>.
	 */
	public void deleteAllChildren();

	/**
	 * Retrieve a child <code>Attribute</code> by name.
	 * 
	 * @param name The name of the child to retrieve.
	 * 
	 * @return If the child is found, then a reference to that child's
	 * <code>Attribute</code> node is returned. If the child is not found, then
	 * <b>null</b> will be returned.
	 */
	public Attribute getChildByName(String name);

	/**
	 * Get the number of children parented by this <code>Attribute</code>.
	 * 
	 * @return The number of children is returned.
	 */
	public int getChildCount();

	/**
	 * Get the child attribute specified by the given index.
	 * 
	 * @param index The location of the child <code>Attribute</code> to retrieve.
	 * 
	 * @return The reference to the child <code>Attribute</code> is returned.
	 */
	public Attribute getChild(int index);

	/**
	 * Set the specified child <code>Attribute</code> at the given location.
	 * 
	 * @param index The location of the child <code>Attribute</code> to set.
	 * @param child The child <code>Attribute</code> being set.
	 */
	public void setChild(int index, Attribute child);
 
	/**
	 * Replace the specified child <code>Attribute</code> at the given location.
	 * 
	 * @param index The location of the child <code>Attribute</code> to replace.
	 * @param child The new child <code>Attribute</code> being set.
	 * 
	 * @return The old child <code>Attribute</code> that was replaced.
	 */
	public Attribute replaceChild(int index, Attribute child);
   
	/**
	 * Get the index of the specified child.
	 * 
	 * @param child The child <code>Attribute</code> in which to retrieve the
	 * index for.
	 * 
	 * @return The index of the specified child <code>Attribute</code> is returned.
	 */
	public int getChildLocation(Attribute child);

	/**
	 * Get the <code>Attribute</code> type.
	 * 
	 * @return By default, <b>TYPE_UNKNOWN</b> should be returned when the type is not specified.
	 */
	public String getType();

	/**
	 * Get the name of the <code>Attribute<code>.
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getName();

	/**
	 * Set the name of the <code>Attribute</code>.
	 * 
	 * @param name The name to set.
	 */
	public void setName(String name);

	/**
	 * Test whether the <code>Attribute</code> is read-only.
	 * 
	 * @return If the attribute is read-only, then <b>true</b> will be returned.
	 * Otherwise, <b>false</b> will be returned.
	 */
	public boolean isReadOnly();

	/**
	 * Set the <code>Attribute</code> to read-only status.
	 * 
	 * @param readOnly If <b>true</b>, then the <code>Attribute</code> will be set read-only.
	 * Otherwise, <b>false</b> will enable the <code>Attribute</code> for editting.
	 */
	public void setReadOnly(boolean readOnly);
    
	/**
	 * Get the <code>Attribute</code> read-only status. This method does not take
	 * into account if the <code>Attribute</code> is in dangerous mode.
	 * 
	 * @return <b>true</b> is returned if the <code>Attribute</code> is read-only.
	 * Otherwise <b>false</b> will be returned.
	 */
	public boolean getReadOnly();
	
	/**
	 * Get the number of bits representing the <code>Attribute</code> value.
	 * 
	 * @return The number of bits is returned.
	 */
	public int getBits();
	
	/**
	  * Set the number of bits representing the <code>Attribute</code> value.
	  * 
	  * @param bits The number of bits.
	  */
	 public void setBits(int bits);

	 /**
	  * Get the <b>Attribute</b> value as an array of bytes.
	  * <p>
	  * This is how attributes are serialized (for saving or transmission). They can easily
	  * be converted into bit strings OR byte arrays.
	  * </p>
	  * 
	  * @return An array of bytes is returned containing the value of the <b>Attribute</b>.
	  */

	 public byte[] getBytes();

	/**
	 * Get the parent <b>Attribute</b>.
	 * 
	 * @return The parent of this attribute is returned. This may be <b>null</b> if this
	 * <b>Attribute</b> is the root of an attribute tree.
	 */
	public Attribute getParent();

	/**
	 * Set the parent <b>Attribute</b>.
	 * 
	 * @param parent The parent <b>Attribute</b> to set.
	 */
	public void setParent(Attribute parent);
}
