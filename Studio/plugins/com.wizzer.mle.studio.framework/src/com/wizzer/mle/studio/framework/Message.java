/**
 * @file Message.java
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
package com.wizzer.mle.studio.framework;

// Import standard Java packages.
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * A container for a message.
 * <p>
 * The <code>Message</code> class encapsulates the format of a
 * specific message which may be transported in a variety of ways.
 * </p><p>
 * The format for the message is:
 * <ul>
 * <li>1 byte for Message Type</li>
 * <li>2 bytes for Message Length</li>
 * <li>n bytes for Message Data</li>
 * </ul>
 * </p>
 * 
 * @author Mark S. Millard
 */
public class Message 
{
  	// The attributes of this class follow the typical T-L-V
  	// Type-Length-Value convention.

  	private byte m_messageType = (byte)0;        // Type of Message.
  	private short m_messageLength = (short)0;    // Message length.
  	private byte[] m_messageData = new byte[0];  // The actual payload.

   	/**
   	 *  A constructor that builds up a message.
   	 * 
   	 *  @param type Type of Message.
   	 *  @param length Message length.
   	 *  @param data The actual payload.
   	 */
  	public Message(byte type, short length, byte[] data)
  	{
    	m_messageType = type;
    	m_messageLength = length;
    	m_messageData = data;
  	}

   	/**
   	 * Set the Message Type.
   	 * 
   	 * @param byte Message type.
   	 */
  	public void setMessageType(byte type)
  	{
    	m_messageType = type;
  	}

  	/**
   	 * Set the Message Length.
   	 * 
  	 * @param short Message length.
  	 */
  	public void setMessageLength(short length)
  	{
   		m_messageLength = length;
  	}

  	/**
  	 * Set the Message Data.
  	 * 
  	 * @param data The Message data.
  	 */
  	public void setMessageData(byte[] data)
  	{
    	m_messageData = data;
  	}

  	/**
  	 * Get the Message Type.
  	 * 
  	 * @return The Message type is returned as a byte.
  	 */
  	public byte getMessageType()
  	{
   		return m_messageType;
  	}

  	/**
  	 * Get the Message Length.
  	 * 
  	 * @return The Message length is returned as a short.
  	 */
  	public short getMessageLength()
  	{
    	return m_messageLength;
  	}

  	/**
  	 * Get the Message Data.
  	 * 
  	 * @return The Message payload is returned as a byte array. This may be <b>null</b>.
  	 */
  	public byte[] getMessageData()
  	{
    	return m_messageData;
 	}

    /**
     * Format a byte array into a Message.
     * 
     * @param data The byte array to parse and format.
     * 
     * @return A Message is returned containing the information passed in through
     * the byte arra.
     * 
     * @throws Exception This exception is thrown if the data contents is inconsistent
     * with the Message format.
     */
  	public static Message formatMessage(byte[] data)
  		throws Exception
  	{
      	// 1 byte of messageType
      	// 2 bytes of messageLength
      	// <messageLength> bytes of data
      	Message passThruData = null;

      	if ( data.length < 3 )
      	{
        	throw new Exception("Data has to be at least 3 bytes in length.");
      	}
      	byte mType = (byte)com.wizzer.mle.studio.framework.Unsigned.readBYTE(data, 0);
      	short mLength = (short)com.wizzer.mle.studio.framework.Unsigned.readWORD(data, 1);
      	byte[] mData = new byte[data.length - 3];

      	if ( mLength != (data.length - 3) )
     	{
        	throw new Exception("Inconsistent Data: Message Length != Data Length.");
      	}

      	System.arraycopy(data,3,mData,0,(data.length - 3));
      	passThruData = new Message(mType,mLength,mData);
      	
      	return passThruData;
  	}

  	/**
   	 * Check if the message is correctly formatted.
  	 * 
  	 * @return A boolean value is returned indicating the validity of the message.
  	 * <b>true</b> is returned if the message is valid. Otherwise, <b>false</b> is returned.
   	 */
  	public boolean isValid()
  		throws Exception
  	{
     	if ( (m_messageLength != 0) && (m_messageData == null) )
     	{
        	throw new Exception("Message Data is required.");
     	}
     	else if ( m_messageLength != m_messageData.length)
     	{
        	throw new Exception("Message Data length != Message Length");
     	}
     	
     	return true;
  	}

  	/**
  	 * Returns the byte array of the message.
  	 * 
  	 * @return The byte array of the message is returned.
  	 */
  	public byte[] getBytes()
  	{
    	byte[] retValue = null;

    	try
    	{
      		ByteArrayOutputStream baos = new ByteArrayOutputStream();
      		DataOutputStream dos = new DataOutputStream(baos);

      		// Write the message to the ouput stream.
      		dos.writeByte(getMessageType());
      		dos.writeShort(getMessageLength());
      		dos.write(getMessageData());
      		
      		// Converty the output stream to a byte array.
      		retValue = baos.toByteArray();

            // Clean up.
      		dos.close();
      		baos.close();
    	}
    	catch(Exception ex)
    	{
			String message = "Unable to read message byte array: Type " + getMessageType() + ": Size "
			   + getMessageLength();
			FrameworkLog.logError(ex,message);
    	}
    	
    	return retValue;
  	}
}
