// COPYRIGHT_BEGIN
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
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.framework.domain;

// Import standard Java packages.
import java.lang.Object;
import java.lang.reflect.Method;
import java.io.FileWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

// Import Tool Framework packages.
import com.wizzer.mle.studio.framework.FrameworkLog;
import com.wizzer.mle.studio.framework.Utilities;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.NumberAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;

/**
 * This class implements a domain model for a table.
 * <p>
 * All domain-specific tables derive from this class. Each <code>Table</code> has a single
 * <code>Attribute</code> that acts as the root of its full <code>Attribute</code> tree.
 * It also maintains a place-holder for a CRC <code>NumberAttribute</code> in case one is needed.
 */
public abstract class Table extends Observable implements Observer
{
	/** The top of a hierarchical <code>Attribute</code> tree. */
    protected Attribute m_top = null;
    /** The version <code>Attribute</code>. */
    protected NumberAttribute m_versionAttribute = null;

    // CRC stuff:
    
    /** The crc32 <code>Attribute</code>. */
    protected NumberAttribute m_crcAttribute;
    // The crc32 value for the table.
    private CRC32 m_crc = new CRC32();

    /**
     * Build a default instance of the <code>Table</code>.
     * 
     * Every concrete subclass of <code>Table</code> must provide an implementation for this function.
     * It will be automatically invoked when the table is created.
     */
    protected abstract Attribute buildDefaultInstance();

    /**
     * Indicate that the table has changed.
     */
    public void externalSetChanged()
    {
        this.setChanged();
    }

    /**
     * Handle the deletion of an <code>Attribute</code> from the table.
     * <p>
     * By default, it does nothing. However, subclasses should override this method if some
     * special processing must be done.
     * </p>
     * 
     * @param a The <code>Attribute</code> that was deleted from the table.
     */
    protected void handleDeletion(Attribute a)
    {
        // Don't do anything... but override in subclasses if you need
        // special processing.
    }

    /**
     * The default constructor.
     * <p>
     * The <code>buildDefaultInstance()</code> method is called so that a subclass implementation
     * may populate its table with useful data at the time of construction.
     * </p>
     */
    public Table()
    {
        this.buildDefaultInstance();
        this.setChanged();
    }

    /**
     * Boolean choices that many subclassed tables use to denote boolean values.
     */
    protected static Object[] g_booleanChoices =
        {
        	"Yes",
        	"No"
    };

    /**
     * Get the byte representation of the table data.
     * 
     * @param includeSubordinates A flag indicating whether the byte representation should include
     * data from subordinate tables. If <b>true</b>, then the data from the subordinate tables will be
     * included. If <b>false</b>, then the subordinate table data will not be included.
     * 
     * @return A byte array is returned containing the byte representation of the table.
     */
    public byte[] getByteRepresentation(boolean includeSubordinates)
    {
        String s = this.getBitString(includeSubordinates);
        return Utilities.bitStringToByteRepresentation(s);
    }

    /**
     * Dump the contents of the <code>Table</code> to a <code>PrintStream</code>.
     * 
     * @param ps The <code>PrintStream</code> to dump the contents to.
     */
    public void dump(PrintStream ps)
    {
        m_top.dump(ps, "");
    }

    /**
     * Bump the version of the <code>Table</code>.
     * <p>
     * This allows any subclass to update its version attribute
     * which should then wrap as necessary so as not to contain more bits than allowed.
     * </p>
     * 
     * @return The bumped value is returned. <b>-1</b> will be returned if the managing
     * <b>Attribute</b> is not set.
     */
    public long bumpVersion()
    {
        if (m_versionAttribute == null)
        {
            return -1;
        }
        
        // Notify observers of the table that the version has been bumped.
        this.setChanged();
        this.notifyObservers();
        
        return m_versionAttribute.bump();
    }

    /**
     * Delete an element from the specified <code>VariableListAttribute</code>.
     * 
     * @param vla The <code>VariableListAttribute</code> to delete the element from.
     * @param index The location of the element to delete.
     */
     public void deleteListElement(VariableListAttribute vla, int index)
    {
        String message = new String("Table.deleteListElement(vla, " + index + ")");
        FrameworkLog.logInfo(message);
        
        long count = vla.getCountAttribute().getValue();
        count -= vla.getMultiplier();
        vla.getCountAttribute().setValue(new Long(count));
        vla.deleteChild(index);
    }

	/**
	 * Add an element to the specified <code>VariableListAttribute</code>.
	 * <p>
     * This uses reflection to invoke the correct function when a list element is added.
     * The name of the function is specified as a parameter to the constructor for the
     * <code>VariableListAttribute</code> when it was created.
     * 
     * @param vla The <code>VariableListAttribute</code> to add the element to.
     */
    public void addListElement(VariableListAttribute vla)
    {
        try
        {
            String fn        = vla.getCallback();
            Object data      = vla.getCallbackData();
            Class c          = vla.getTheCaller().getClass();
            Class[] parmList = new Class[2];
            parmList[0] = vla.getClass();
            parmList[1] = Object.class;
            Method m = c.getMethod(fn, parmList);

            Object[] argList = new Object[2];
            argList[0] = vla;
            argList[1] = data;

            vla.addChild((Attribute)m.invoke(vla.getTheCaller(), argList), this);
            if (!Attribute.LOADING)
            {
                long count = vla.getCountAttribute().getValue();
                count += vla.getMultiplier();
                vla.getCountAttribute().setValue(new Long(count));
            }
        }
        catch (Exception ex)
        {
            String message = new String("Error in addListElement: " + ex.toString());
            FrameworkLog.logError(ex,message);
        }
        
        // Notify observes of the table that an element was added.
        this.setChanged();
        this.notifyObservers(vla);
    }

	/**
	 * Move an element in its <code>VariableListAttribute</code>.
	 * 
	 * @param up A boolean indicating which direction to move the <code>Attribute</code> element.
	 * If <b>true</b>, then the element will be moved toward the front of the list.
	 * If <b>false</b>, then the element is moved toward the end of the list.
	 * @param listElement The <code>Attribute</code> element to move.
	 * 
	 * @return <b>true</b> will be returned if the <code>Attribute</code> element is successfully moved.
	 * Otherwise, <b>false</b> will be returned.
	 */
	public boolean moveListElement(boolean up, Attribute listElement)
	{
		boolean result = false;

		VariableListAttribute vla = (VariableListAttribute)listElement.getParent();
		int n = vla.getChildCount();
		for (int i = 0; i < n; i++)
		{
			if (listElement == vla.getChild(i))
			{
				int otherIndex = i + ((up) ? -1 : 1);
				if (otherIndex < 0 || otherIndex >= n)
				{
					break;
				}

				// Swap 'em:
				Attribute other = vla.getChild(otherIndex);
				vla.setChild(otherIndex, listElement);
				vla.setChild(i, other);
				
				// Notify observers of the table that the list has changed.
				this.setChanged();
				this.notifyObservers();
				
				result = true;
				break;
			}
		}
		
		return result; // true -> I actually swapped elements.
	}

    /**
     * Get the top of the <code>Attribute</code> tree.
     * 
     * @return The root of the hierarchical <code>Attribute</code> tree is returned.
     */
    public Attribute getTopAttribute()
    {
        return m_top;
    }

    /**
     * Retrieve the data from the table as a bit string.
     * 
     * @param includeSubordinates A flag indicating whether the bit string should include
     * data from subordinate tables. If <b>true</b>, then the data from the subordinate tables will be
     * included. If <b>false</b>, then the subordinate table data will not be included.
     * 
     * @return The bit string is returned.
     */
    public String getBitString(boolean includeSubordinates)
    {
        String result = m_top.getRecursiveBitString();
        if (includeSubordinates)
        {
            result += getSubordinateBitString();
        }

        return result;
    }

    /**
     * Save the <code>Table</code> to the specified <code>File</code>.
     * 
     * @param file The file object to save the table to.
     * 
     * @return If the table is saved successfully, then <b>true</b> will be returned.
     * Otherwise <b>false</b> will be returned.
     * 
     * @throws Exception An exception is thrown if the file can not be successfully written
     * to.
     */
    public boolean save(File file) throws Exception
    {
        boolean result = true;
        String s = this.getBitString(true);
        //System.out.println("Writing these bits: " + s);
        int bitCount = s.length();
        int byteCount = bitCount / 8;
        // Create a buffer from the bit string:
        int whichByte                 = 0;
        int byteIndex                 = 0;
        FileWriter fo                 = null;
        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file));
        String eight                = "";
        
        for (int i = 0; i < bitCount; i += 8, byteIndex++)
        {
            try
            {
                eight = s.substring(i, i + 8);
            }
            catch (Exception e)
            {
                // In case it wasn't an even multiple of 16 bits:
                eight = s.substring(i, s.length());
            }
            byte b = (byte) Integer.parseInt(eight, 2);
            // System.out.println("" + charIndex + "Writing 16 bits: " + sixteen + " ... which = " + Integer.toString(c, 2) +
            // " = " + Integer.toString(c, 16));
            outputStream.write( b );
        }

        // Flush and clean up the file by closing it.
        outputStream.flush();
        outputStream.close();
        
        return result;
    }

    /**
     * Get the bit string for all subordinate tables.
     * <p>
     * The default implementation returns an empty string. This method should be
     * overloaded for tables with sub-tables (e.g., VCT has  PMT and PAT tables).
     * </p>
     * 
     * @return the bit string for all subordinate tables is returned. This may be an
     * empty string.
     */
    protected String getSubordinateBitString()
    {
        return "";
    }

    /**
     * Reset the table from the specified bit string.
     * 
     * @param str The bit string to reload the <code>Table</code> from.
     */
    public void resetFromBitString(String str)
    {
    	// Rebuild the default instance.
        m_top = this.buildDefaultInstance();
        
        // Rebuild the table.
        int offset = 0;
        offset = this.populateAttributeFromBitString(m_top, str, offset);
        
        // Rebuild the subordinate tables.
        this.loadSubordinateTables(m_top, str, offset);
    }

    /**
     * Open the specified <code>File</code> and load it into the <code>Table</code>.
     * 
     * @param file The file object to load the new table data from.
     * 
     * @throws Exception An exceptionis thrown if the specifed can not be opened
     * successfully.
     */
    public void open(File file) throws Exception
    {
        // Open and load the file.
        int bytesToRead = (int) file.length();
        DataInputStream in = new DataInputStream( new FileInputStream( file ) );
        byte[] fileData = new byte[ bytesToRead ];
        for (int bytesRead = 0; bytesRead < bytesToRead; ) 
        {
            bytesRead += in.read( fileData, bytesRead, bytesToRead - bytesRead );
        }
        
        // Create a bit string from the file data.
        String str = "";
        for (int i = 0; i < bytesToRead; i++)
        {
            String bitString = Integer.toString( ((short) fileData[i]) & 0xff, 2);
            // inefficient padding: fix this!!!
            while (bitString.length() < 8)
            {
                bitString = "0" + bitString;
            }
            str += bitString;
        }
        
        // Log an informative message.
        String message = new String("Read these " + str.length() + " bits: " + str);
        FrameworkLog.logInfo(message);

		// Reset Table from string of bits.
        resetFromBitString(str);
        
        // Notify Observers that the table was reset from the specified file.
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Load in all subordinate tables.
     * <p>
     * Some tables (like the VCT, for example) make use of subordinate
     * tables (PAT, PMT, for example). This method provides a generic mechanism to have these loaded. 
     * Subclasses should overload this method for tables with sub-tables.
     * </p>
     * 
     * @param top The top of the hierarchical <code>Attribute</code> tree.
     * @param str The bit string to load the subordinate tables from.
     * @param offset An offset into the bit string from which to begin loading.
     * 
     * @return A new offset is calculated and returned.
     */
    protected int loadSubordinateTables(Attribute top, String str, int offset)
    {
        return 0;
    }

	/**
	 * Load the specified <code>Attribute</code> from a bit string.
	 * 
	 * @param topAttribute The top of the hierarchical <code>Attribute</code> tree to load.
	 * @param str The bit string to load the subordinate tables from.
	 * @param offset An offset into the bit string from which to begin loading.
	 * 
	 * @return A new offset is calculated and returned.
	 */
    public int populateAttributeFromBitString(Attribute topAttribute, String str, int offset)
    {
        offset = topAttribute.loadFromBitString(str, offset);

        //if (topAttribute.getName().compareTo("num_channels_in_section") == 0)
        if (topAttribute.getType() == Attribute.TYPE_VARIABLE_LIST)
        {
            VariableListAttribute vla = (VariableListAttribute)topAttribute;
            //NumberAttribute na        = vla.getCountAttribute();
            //int multiplier            = vla.getMultiplier();
            int howManyItemsToCreate = (int)vla.getCount();
            for (int i = 0; i < howManyItemsToCreate; i++)
            {
                this.addListElement(vla);
            }
        }

        for (int i = 0; i < topAttribute.getChildCount(); i++)
        {
            Attribute a = topAttribute.getChild(i);
            offset = populateAttributeFromBitString(a, str, offset);
        }
        
        this.setChanged();
        //this.notifyObservers();

        return offset;
    }

    /**
     * Create a <code>Vector</code> that can be used to transmit the <code>Table</code>.
     * <p>
     * By defatult, an empty <code>Vector</code> is returned.
     * This method will typically be overridden by subclasses that know more about the data
     * they wish to transmit.
     * </p>
     * 
     * @return The transmission vector is returned.
     */
    public Vector createTransmissionVector()
    {
        // This is pretty bogus: it'll typically be overridden.
        return new Vector();
    }

    /**
     * Convert the specified byte array to a <code>String</code>.
     * 
     * @param bytes The byte array to convert.
     * @param len The number of bytes to convert.
     * 
     * @return The converted <code>String</code> is returned. The result will
     * be padded in a left-justified, byte alignment configuration.
     */
    public static String convertBytesToBitString(byte[] bytes, int len)
    {
        String result = "";
        for (int i = 0; i < len; i++)
        {
            byte b = bytes[i];
            String eight = Integer.toBinaryString(b);
            if (eight.length() > 8)
            {
                eight = eight.substring(eight.length() - 8, eight.length());
            }
            eight = Attribute.Pad(eight, true, "0", 8);
            result = result + eight;
        }
        return result;
    }
    
    // Recursively purge the children of the specified node.
    private void purgeChildren(Attribute node)
    {
    	if (node == null)
    		return;

		// Recursively delete children, if any.
		if (node.getChildCount() > 0)
		{
			for (int i = 0; i < node.getChildCount(); i++)
			{
				Attribute childNode = node.getChild(i);
				purgeChildren(childNode);
			}
			
			// Delete this node's children.
			node.deleteAllChildren();
		}
    }
    
	/**
	 * Convenience routine for purging the Table's Attribute tree.
	 * 
	 * @param node The <code>Attribute</code> node to begin purging.
	 */
    public void purge(Attribute node)
    {
    	Attribute top;

    	if (node == null) top = m_top;
    	else top = node;
 
    	if (top != null)
    	{
    		purgeChildren(top);
    	}
    	
    	this.updateCRC();
    }

    /**
     * Update the crc32 value for the <code>Table</code>.
     */
    protected void updateCRC()
    {
        m_crc.reset();
        byte bytes[] = this.getByteRepresentation(false);

        // Forget the last four bytes: these represent the CRC itself!!!
        m_crc.update(bytes, 0, bytes.length - 4);

        long crcValue = m_crc.getValue();
        m_crcAttribute.setValue(new Long(crcValue));
    }

}




