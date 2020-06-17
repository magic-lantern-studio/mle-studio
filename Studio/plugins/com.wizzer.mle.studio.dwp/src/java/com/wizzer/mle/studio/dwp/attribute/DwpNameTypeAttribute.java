/*
 * DwpNameTypeAttribute.java
 */

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
package com.wizzer.mle.studio.dwp.attribute;

//Import standard Java packages.
import java.io.PrintStream;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;


/**
 * This class implements an <code>Attribute</code> for Magic Lantern Digital Workprint
 * NameType-based attributes.
 * <p>
 * NameType-based Attributes support the following DWP items:
 * <ul>
 * <li>Set/Forum</li>
 * <li>PropertyDef</li>
 * <li>RoleBinding</li>
 * <li>RoleAttachment</li>
 * <li>Group</li>
 * <li>Actor</li>
 * </ul>
 * </p>
 * 
 * @author Mark S. Millard
 */
public abstract class DwpNameTypeAttribute extends DwpItemAttribute
{
	/**
	 * This class contains the value for a <code>DwpNameTypeAttribute</code>.
	 * 
	 * @author Mark S. Millard
	 */
	protected class DwpNameTypeAttributeValue
	{
		/** The name of the value */
		public String m_name = null;
		/** The type of the value */
		public String m_type = null;
		
		/**
		 * A constructor that initializes the name and type of the value.
		 * 
		 * @param name The name of the value.
		 * @param type The type for the value.
		 */
		public DwpNameTypeAttributeValue(String name, String type)
		{
			m_name = name;
			m_type = type;
		}
		
		/**
		 * Convert the value to a <code>String</code>.
		 * 
		 * @return A <code>String</code> is returned in the form of "<name> <type>".
		 */
		public String toString()
		{
			StringBuffer str = new StringBuffer(m_name);
			str.append(" ");
			str.append(m_type);

			return str.toString();
		}
	}
	
	/**
	 * A constructor that initializes the <code>DwpNameTypeAttribute</code>..
	 * <p>
	 * Note that the name of the <code>Attribute</code> is mapped to the DWP item type
	 * (i.e. "ActorDef", "DSOFile", "HeaderFile" ...).
	 * </p>
	 * 
	 * @param type The type of the DWP item.
	 * @param value The contents of the DWP item. This parameter may be <b>null</b>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpNameTypeAttribute(String type, String value, boolean isReadOnly)
	{
		super(type, value, isReadOnly);
	}

	/**
	 * Represent the <code>DwpNameTypeAttribute</code> as a <code>String</code>.
	 * 
	 * @return A <code>String</code> is returned in the form <i>( dwptype name type )</i>.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		DwpNameTypeAttributeValue value = (DwpNameTypeAttributeValue)m_value;
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
	 * Get the value of the <code>DwpNameTypeAttribute</code> as a bit string.
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
	 * Load the value of the <code>DwpNameTypeAttribute</code> from a bit string.
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
		DwpNameTypeAttributeValue attrValue = new DwpNameTypeAttributeValue(name,type);
		this.setValue(attrValue);
		
		//System.out.println ("Loaded String: " +  getName() + " " + bits + " [" + offset + ", " + getBits() + "] " + toString());

		return offset + this.getBits();

	}

	/**
	 * Dump the contents of the <b>DwpNameTypeAttribute</b> to the specified print stream.
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
	 * Test to see if the specified object is different from this <code>DwpNameTypeAttribute</code>.
	 * 
	 * @param obj The <code>Object</code> to test against.
	 * 
	 * @return <b>true</b> will be returned if the specified <code>Object</code> is different
	 * from this <code>DwpSetAttribute</code>. If they are not different, then <b>false</b> will be
	 * returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#isDifferent(Object)
	 */
	public boolean isDifferent (Object obj)
	{
		return ((this.getValue() == null) || (this.getValue().compareTo(obj.toString()) != 0));
	}

	/**
	 * Validate the value of the <code>DwpNameTypeAttribute</code>.
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
			DwpNameTypeAttributeValue value = (DwpNameTypeAttributeValue)m_value;
			if (value.m_name == null) rval = false;
			else if (value.m_type == null) rval = false;
			else rval = true;
		}
		
		return rval;
	}

	/**
	 * Get the <code>DwpNameTypeAttribute</code> value as a <code>String</code>.
	 * 
	 * @return A <code>String<code> representing the vaule of the <code>DwpNameTypeAttribute</code>
	 * is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getStringValue()
	 */
	public String getStringValue()
	{
		DwpNameTypeAttributeValue value = (DwpNameTypeAttributeValue)m_value;
        return value.toString();
	}

	/**
	 * Get the value of the <code>DwpNameTypeAttribute</code>.
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
		DwpNameTypeAttributeValue value = (DwpNameTypeAttributeValue)m_value;
		return value.m_name;
	}
	
	/**
	 * Get the type component of the value.
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getValueType()
	{
		DwpNameTypeAttributeValue value = (DwpNameTypeAttributeValue)m_value;
		return value.m_type;
	}

	/**
	 * Set the value from the specified parameters.
	 * 
	 * @param name The value name.
	 * @param type The value type.
	 *
	 */
	public void setValue(String name, String type)
	{
		// Create a helper object to hold the Actor value.
		Object value = new DwpNameTypeAttributeValue(name,type);
		setValue(value);
	}

}
