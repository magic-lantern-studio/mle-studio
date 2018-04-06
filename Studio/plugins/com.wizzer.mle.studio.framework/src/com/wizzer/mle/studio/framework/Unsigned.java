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
package com.wizzer.mle.studio.framework;

/**
 * This class provides a collection of utilities for reading/writing Unsigned data types
 * in a consistent fashion.
 * 
 * @author Mark S. Millard
 * @created Sep 9, 2003
 */
public class Unsigned
{
   /**
	* Converts a 6-byte MAC Address to network byte order and puts it into
	* the specified data array.
	* 
	* @param data The data arary that will contain the converted value. This is an ouput
	* parameter.
	* @param offset An offset into the data array identifying where the converted value
	* should be placed.
	* @param value The 6-byte value to convert.
	* 
	* @return The number of bytes converted is returned. This will always be <b>6</b>
	*/
   final public static int writeMAC(char[] data, int offset, long value)
   {
      value = value & 0x0000FFFFFFFFFFFFl;

      data[offset+0] = (char)((value >> 40) & 0xFF);
      data[offset+1] = (char)((value >> 32) & 0xFF);
      data[offset+2] = (char)((value >> 24) & 0xFF);
      data[offset+3] = (char)((value >> 16) & 0xFF);
      data[offset+4] = (char)((value >> 8) & 0xFF);
      data[offset+5] = (char)(value & 0xFF);

      String message = new String("writeMAC: " + value + "\t" +
          Integer.toHexString((char)data[offset+0]) + "  " + Integer.toHexString((char)data[offset+1]) + " " +
          Integer.toHexString((char)data[offset+2]) + " " + Integer.toHexString((char)data[offset+3]) + " " +
          Integer.toHexString((char)data[offset+4]) + " " + Integer.toHexString((char)data[offset+5]) + " ");
      FrameworkLog.logInfo(message);
                         
      return 6;
   }

   /**
	* Converts a 6-byte MAC Address to network byte order and puts it into
	* the specified data array.
	* 
	* @param data The data arary that will contain the converted value. This is an ouput
	* parameter.
	* @param offset An offset into the data array identifying where the converted value
	* should be placed.
	* @param value The 6-byte value to convert.
	* 
	* @return The number of bytes converted is returned. This will always be <b>6</b>
	*/
   final public static int writeMAC(byte[] data, int offset, long value)
   {
      value = value & 0x0000FFFFFFFFFFFFl;

      data[offset+0] = (byte)((value >> 40) & 0xFF);
      data[offset+1] = (byte)((value >> 32) & 0xFF);
      data[offset+2] = (byte)((value >> 24) & 0xFF);
      data[offset+3] = (byte)((value >> 16) & 0xFF);
      data[offset+4] = (byte)((value >> 8) & 0xFF);
      data[offset+5] = (byte)(value & 0xFF);

      String message = new String("writeMAC: " + value + "\t" +
          Integer.toHexString((char)data[offset+0]) + "  " + Integer.toHexString((char)data[offset+1]) + " " +
          Integer.toHexString((char)data[offset+2]) + " " + Integer.toHexString((char)data[offset+3]) + " " +
          Integer.toHexString((char)data[offset+4]) + " " + Integer.toHexString((char)data[offset+5]) + " ");
      FrameworkLog.logInfo(message);
          
      return 6;
   }

   /**
    * Converts a 2-byte integer to network byte order and puts it into
    * the specified data array.
	* 
	* @param data The data arary that will contain the converted value. This is an ouput
	* parameter.
	* @param offset An offset into the data array identifying where the converted value
	* should be placed.
	* @param value The 2-byte value to convert.
	* 
	* @return The number of bytes converted is returned. This will always be <b>2</b>
    */
   final public static int writeWORD(char[] data, int offset, int value)
   {
      data[offset+0] = (char)((value >> 8) & 0xFF);
      data[offset+1] = (char)(value & 0xFF);
      return 2;
   }

   /**
    * Adds a byte to the specified data array.
	* 
	* @param data The data arary that will contain the converted value. This is an ouput
	* parameter.
	* @param offset An offset into the data array identifying where the converted value
	* should be placed.
	* @param value The byte value to convert.
	* 
	* @return The number of bytes converted is returned. This will always be <b>1</b>
    */
   final public static int writeBYTE(char[] data, int offset, int value)
   {
      data[offset] = (char)(value & 0xFF);
      return 1;
   }

   /**
    * Converts a 8-byte long to network byte order and puts it into
    * the specified data array.
	* 
	* @param data The data arary that will contain the converted value. This is an ouput
	* parameter.
	* @param offset An offset into the data array identifying where the converted value
	* should be placed.
	* @param value The 8-byte value to convert.
	* 
	* @return The number of bytes converted is returned. This will always be <b>8</b>
    */
   final public static int writeQWORD(byte[] data, int offset, long value)
   {
      data[offset+0] = (byte)((value >> 56) & 0xFF);
      data[offset+1] = (byte)((value >> 48) & 0xFF);
      data[offset+2] = (byte)((value >> 40) & 0xFF);
      data[offset+3] = (byte)((value >> 32) & 0xFF);
      data[offset+4] = (byte)((value >> 24) & 0xFF);
      data[offset+5] = (byte)((value >> 16) & 0xFF);
      data[offset+6] = (byte)((value >>  8) & 0xFF);
      data[offset+7] = (byte)((value >>  0) & 0xFF);
      return 8;
   }
   
   /**
	* Converts a 4-byte integer to network byte order and puts it into
	* the specified data array.
	* 
	* @param data The data arary that will contain the converted value. This is an ouput
	* parameter.
	* @param offset An offset into the data array identifying where the converted value
	* should be placed.
	* @param value The 4-byte value to convert.
	* 
	* @return The number of bytes converted is returned. This will always be <b>4</b>
	*/
   final public static int writeDWORD(char[] data, int offset, long value)
   {
	  data[offset+0] = (char)((value >> 24) & 0xFF);
	  data[offset+1] = (char)((value >> 16) & 0xFF);
	  data[offset+2] = (char)((value >> 8) & 0xFF);
	  data[offset+3] = (char)(value & 0xFF);
	  return 4;
   }

   /**
    * Converts a 4-byte integer to network byte order and puts it into
    * the specified data array.
	* 
	* @param data The data arary that will contain the converted value. This is an ouput
	* parameter.
	* @param offset An offset into the data array identifying where the converted value
	* should be placed.
	* @param value The 4-byte value to convert.
	* 
	* @return The number of bytes converted is returned. This will always be <b>4</b>
    */
   final public static int writeDWORD(byte[] data, int offset, long value)
   {
      data[offset+0] = (byte)((value >> 24) & 0xFF);
      data[offset+1] = (byte)((value >> 16) & 0xFF);
      data[offset+2] = (byte)((value >> 8) & 0xFF);
      data[offset+3] = (byte)(value & 0xFF);
      return 4;
   }

   /**
    * Converts a 2-byte integer to network byte order and puts it into
    * the specified data array.
	* 
	* @param data The data arary that will contain the converted value. This is an ouput
	* parameter.
	* @param offset An offset into the data array identifying where the converted value
	* should be placed.
	* @param value The 2-byte value to convert.
	* 
	* @return The number of bytes converted is returned. This will always be <b>2</b>
    */
   final public static int writeWORD(byte[] data, int offset, int value)
   {
      data[offset+0] = (byte)((value >> 8) & 0xFF);
      data[offset+1] = (byte)(value & 0xFF);
      return 2;
   }

   /**
    * Adds a byte to the data array.
	* 
	* @param data The data arary that will contain the converted value. This is an ouput
	* parameter.
	* @param offset An offset into the data array identifying where the converted value
	* should be placed.
	* @param value The byte value to convert.
	* 
	* @return The number of bytes converted is returned. This will always be <b>1</b>
    */
   final public static int writeBYTE(byte[] data, int offset, int value)
   {
      data[offset] = (byte)(value & 0xFF);
      return 1;
   }

   /**
    * Read a 1-byte integer value from the specified data array.
    * 
    * @param data The data array to read from.
    * @param offset  An offset into the data array identifying where to begin reading from.
    * 
    * @return A byte value is returned.
    */
   final public static short readBYTE(byte[] data, int offset)
   {
      short value = (short)(data[offset] & 0xFF);

      return value;
   }

   /**
	* Read a 2-byte integer value from the specified data array.
	* 
	* @param data The data array to read from.
	* @param offset  An offset into the data array identifying where to begin reading from.
	* 
	* @return A 2-byte integer value is returned.
	*/
   final public static int readWORD(byte[] data, int offset)
   {
      int  i = ((data[offset] & 0xFF) << 8) |
               (data[offset+1]  & 0xFF);
      return i;
   }

   /**
	* Read a 4-byte integer value from the specified data array.
	* 
	* @param data The data array to read from.
	* @param offset  An offset into the data array identifying where to begin reading from.
	* 
	* @return A 4-byte integer value is returned.
	*/
   final public static long readDWORD(byte[] data, int offset)
   {
      long l =  (((long)data[offset] & 0xFF)  << 24) |
                ((data[offset+1] & 0xFF) << 16) |
                ((data[offset+2] & 0xFF) << 8)  |
                 (data[offset+3] & 0xFF);
      return l;
   }

   /**
	* Read a 8-byte integer value from the specified data array.
	* 
	* @param data The data array to read from.
	* @param offset  An offset into the data array identifying where to begin reading from.
	* 
	* @return A 8-byte integer value is returned.
	*/
   final public static long readQWORD(byte[] data, int offset)
   {
      long l =  (((long)data[offset+0] & 0xFF) << 56) |
                (((long)data[offset+1] & 0xFF) << 48) |
                (((long)data[offset+2] & 0xFF) << 40) |
                (((long)data[offset+3] & 0xFF) << 32) |
                (((long)data[offset+4] & 0xFF) << 24) |
                (((long)data[offset+5] & 0xFF) << 16) |
                (((long)data[offset+6] & 0xFF) <<  8) |
                (((long)data[offset+7] & 0xFF) <<  0);
      return l;
   }

   /**
    * Read a MAC Address value from the specified data array.
    * 
    * @param data The data array to read from.
    * @param offset  An offset into the data array identifying where to begin reading from.
    * 
    * @return A 6-byte MAC Address is returned.
    */
    final public static long readMAC(byte[] data, int offset)
   {
      //System.out.println("Called readMac with offset of " + offset);

      //System.out.println("readMAC " + StringUtils.getHexStringBYTEArray(data,offset, 6));

      long low = (((long)data[offset+2] & 0xFF)  << 24) |
                 ((data[offset+3] & 0xFF) << 16) |
                 ((data[offset+4] & 0xFF) << 8)  |
                 (data[offset+5] & 0xFF);

      long hi =  (((long)data[offset+0] & 0xFF) << 8) |
                 ((data[offset+1] & 0xFF));

      long l = ((hi << 32) | low);

      return l;
   }

}

