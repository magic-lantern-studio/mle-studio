/*
 * DwpWriter.java
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
package com.wizzer.mle.studio.dwp;

// Import standard Java packages.
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.attribute.IAttributeCallback;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.domain.TableIterator;

// Import Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpLog;
import com.wizzer.mle.studio.dwp.attribute.DwpIncludeAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;
import com.wizzer.mle.studio.dwp.domain.DwpTable;


/**
 * This class implements a writer for a Magic Lantern Digital Workprint.
 * 
 * @author Mark S. Millard
 */
public class DwpWriter
{
	// The DWP table to write.
	private DwpTable m_table = null;
	
	/**
	 * This class is a helper class for traversing the <code>Attribute</code> tree
	 * and building the DWP in it's ASCII format.
	 * 
	 * @author Mark S. Millard
	 */
	protected class DwpAsciiCallback implements IAttributeCallback
	{
		// The ancillary data.
		private Object m_data = null;
		// The ASCII format of the DWP.
		private StringBuffer m_ascii = new StringBuffer();
		
		/**
		 * The default constructor.
		 */
		public DwpAsciiCallback()
		{
			super();
			
			// Add the header.
			m_ascii.append("#DWP 1.0 ascii\n");
		}
		
		/**
		 * Invoke the callback method.
		 * 
		 * @param node An <code>IAttribute</code> in the Attribute tree which the callback
		 * is being associated with.
		 * @param data Anciliary data that can be used by the callback method.
		 * 
		 * @return If the callback is successful, <b>true</b> will be returned.
		 * Otherwise, <b>false</b> will be returned.
		 */
		public boolean callback(IAttribute node, Object data)
		{
			boolean retValue = true;
			
			// Skip node if not registered.
			if (! (node instanceof DwpItemAttribute))
			{
				return false;
			}

			// Skip writing contents of DwpInclude attribute.
			if (childOfInclude(node))
				return false;

			// Format the Attribute for DWP output.
			StringBuffer buffer = new StringBuffer(node.toString());
			if (node.getChildCount() > 0)
			    buffer.append("\n");

			// Append the formatted node to the accumulative output.
			m_ascii.append(buffer);
			
			return retValue;
		}
		
		// Calculate the level of indentation.
		private char[] calculateIndent(Integer level)
		{
			int lval = level.intValue();
			char[] indent = null;
			
			if (lval > 0)
			{
				indent = new char[lval * 2];
				for (int i = 0; i < (lval * 2); i++)
					indent[i] = ' ';
			}
			
			return indent;
		}
		
		/**
		 * Invoke the setup method. Formats and prepends indentation.
		 * 
		 * @param node An <code>IAttribute</code> in the Attribute tree which the callback
		 * is being associated with.
		 * @param data Anciliary data that can be used by the callback method.
		 * 
		 * @return If the setup is successful, <b>true</b> will be returned.
		 * Otherwise, <b>false</b> will be returned.
		 */
		public boolean prefix(IAttribute node, Object data)
		{
			char[] indent = null;
			
			// Skip node if not registered.
			if (! (node instanceof DwpItemAttribute))
			{
				return false;
			}
			
			// Skip writing contents of DwpInclude attribute.
			if (childOfInclude(node))
				return false;
			
			// Compute level information and indentation.
			if ((data != null) && (data instanceof Integer ))
			{
				indent = calculateIndent((Integer) data);
				if (node.getChildCount() > 0)
				{
					// Increment the level.
					int level = ((Integer)data).intValue();
					level++;
					Integer nextLevel = new Integer(level);
					setData(nextLevel);
				}
			}

			// Format the Attribute for DWP output.
			StringBuffer buffer = new StringBuffer();
			if (indent != null)
			    buffer.append(indent);
			buffer.append("( ");

			// Append the formatted node to the accumulative output.
			m_ascii.append(buffer);
			
			return true;
		}
		
		/**
		 * Invoke the teardown method. Cleans up indentation state.
		 * 
		 * @param node An <code>IAttribute</code> in the Attribute tree which the callback
		 * is being associated with.
		 * @param data Anciliary data that can be used by the callback method.
		 * 
		 * @return If the teardown is successful, <b>true</b> will be returned.
		 * Otherwise, <b>false</b> will be returned.
		 */
		public boolean postfix(IAttribute node, Object data)
		{
			char[] indent = null;
			
			// Skip node if not registered.
			if (! (node instanceof DwpItemAttribute))
			{
				return false;
			}
			
			// Skip writing contents of DwpInclude attribute.
			if (childOfInclude(node))
				return false;
			
			// Compute level information and indentation.
			if ((data != null) && (data instanceof Integer ))
			{
				Integer prevLevel = (Integer)data;
				if (node.getChildCount() > 0)
				{
					// Decrement the level.
					int level = ((Integer)data).intValue();
					level--;
					prevLevel = new Integer(level);
					setData(prevLevel);
				}
				//indent = calculateIndent((Integer) data);
				indent = calculateIndent(prevLevel);
			}

			StringBuffer buffer = new StringBuffer();			
			if (node.getChildCount() == 0)
			{
				buffer.append(" )\n");
			} else
			{
				if (indent != null)
				    buffer.append(indent);
				buffer.append(")\n");
			}

			// Append the formatted node to the accumulative output.
			m_ascii.append(buffer);

			return true;
		}

		/**
		 * Set the anciliary data.
		 * 
		 * @param data Additional data that is associated with the callback.
		 * This may be set to <b>null</b>.
		 */
		public void setData(Object data)
		{
			m_data = data;
		}
    
		/**
		 * Get the anciliary data.
		 * 
		 * @return Additional data that is associated with the callback
		 * is returned. This may be <b>null</b>.
		 */
		public Object getData()
		{
			return m_data;
		}
		
		/**
		 * Get the DWP ASCII representation as a <code>String</code>.
		 * 
		 * @return The DWP is returned as a <code>String</code>.
		 */
		public String getAsciiAsString()
		{
		    // Remove the last '\n';
		    StringBuffer tmp = m_ascii.deleteCharAt(m_ascii.length()-1);
		    
			return tmp.toString();
		}
		
		/**
		 * Get the DWP ASCII representation as a <code>byte</code> array.
		 * 
		 * @return The DWP is returned as an array of <code>byte</code>s.
		 */
		public byte[] getAsciiAsByteArray()
		{
			byte[] buffer = getAsciiAsString().getBytes();
			return buffer;
		}
		
	    // Determine if specified node is a child of DwpIncludeAttribute
	    private boolean childOfInclude(IAttribute node)
	    {
	    	IAttribute parent = node.getParent();

	    	if (parent == null)
	    		return false;
	    	else if (parent instanceof DwpIncludeAttribute)
	    		return true;
	    	else
	    		return childOfInclude(parent);
	    }
	}
	
	/**
	 * A constructor specifying the DWP table to write.
	 * 
	 * @param table The DWP table to write.
	 */
	public DwpWriter(DwpTable table)
	{
		super();
    	
    	m_table = table;
	}

	/**
	 * Write the DWP table to the specified file.
	 * 
	 * @param filename The name of the file to write to.
	 * 
	 * @throws DwpException This exception is thrown if the DWP table is not
	 * set (<b>null</b>), or the specifed file name is <b>null</b>.
	 * 
	 * @return The number of bytes written is returned.
	 */
	public int write(String filename) throws DwpException
	{
		int numBytes = 0;
		
		if (m_table == null)
        {
        	throw new DwpException("Unable to write Digital Workprint: DWP Table is null.");
        } else if (filename == null)
        {
			throw new DwpException("Unable to write Digital Workprint: file name is not specified.");
        }
        
        try
        {
        	// Create a new File Output Stream to write to.
	        File file = new File(filename);
	        FileOutputStream ostream = new FileOutputStream(file);
		
			// Retrieve the byte array in ASCII format.
			byte[] buffer = getAsciiOutput();

			// Write the buffer to the file.
	        ostream.write(buffer);
	        numBytes = buffer.length;
	        
	        // Close the Output Stream.
	        ostream.close();
        } catch (FileNotFoundException ex)
        {
        	DwpLog.logError(ex,"File " + filename + " not found, Exception thown.");
        } catch (IOException ex)
        {
			DwpLog.logError(ex,"File " + filename + " not written, Exception thrown.");
        }
		
		return numBytes;
	}

	/**
	 * Write the DWP table, returneing a byte array. The array will be allocated
	 * to the length required to output the Digital Workprint in its ASCII format.
	 * 
	 * @throws DwpException This exception is thrown if the DWP table is not
	 * set (<b>null</b>).
	 * 
	 * @return A byte array is returned.
	 */
	public byte[] write() throws DwpException
	{
		byte[] buffer;

		// Make sure the table exists.
		if (m_table == null)
		{
			throw new DwpException("Unable to write Digital Workprint: DWP Table is null.");
		}
		
		// Retrive the byte array in ASCII format.
		buffer = getAsciiOutput();

		// Return the byte array.
		return buffer;
	}
    
    // Create a byte array containing the DWP in ASCII format.
    private byte[] getAsciiOutput()
    {
    	byte[] dwpOutput = null;
    	
    	// Create a writer.
    	DwpAsciiCallback writer = new DwpAsciiCallback();
    	Integer level = new Integer(0);
    	writer.setData(level);
    	
    	// Find the top the DWP Attribute tree.
    	TableIterator iter = new TableIterator(m_table);
    	Attribute dwpTop = m_table.getDwpElements();
    	
    	// Generate the ASCII representation of the DwpTable.
    	iter.apply(dwpTop,writer);
    	
    	// Retrieve the byte[] representation of the DWP in ASCII format.
    	dwpOutput = writer.getAsciiAsByteArray();
    	
    	return dwpOutput;
    }

}
