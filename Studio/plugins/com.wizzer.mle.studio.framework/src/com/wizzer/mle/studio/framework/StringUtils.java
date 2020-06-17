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
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This class provides various utility for manipulating <code>String</code>s.
 * 
 * @author Mark S. Millard
 * @created Dec 9, 2003
 */
public class StringUtils
{
   /**
    * Returns a <code>String</code> with all occurrences of <code>from</code>
    * within <code>orig</code> replaced with <code>to</code>. If
    * <code>orig</code> contains no occurrences of
    * <code>from</code>, or if <code>from</code> is equal to
    * <code>to</code>, <code>orig</code> itself is returned rather
    * than a copy being made.
    *
    * @param orig The original <code>String</code>. Must not be null.
    * @param from The <code>String</code> to replace within <code>orig</code>.
    * Must not be null.
    * @param to The <code>String</code> to replace <code>from</code> with. Must
    * not be null.
    *
    * @returns A version of <code>orig</code> with all occurrences
    * of <code>from</code> being replaced with <code>to</code>.
    *
    * @throws IllegalArgumentException This exception is thrown if <code>from</code> is empty.
    */
   public static String replace(String orig, String from, String to)
   {
      int fromLength = from.length();

      if ( fromLength==0 )
         throw new IllegalArgumentException("String to be replaced must not be empty");

      int start = orig.indexOf (from);
      if ( start==-1 )
      {
         return orig;
      }

      boolean greaterLength = (to.length() >= fromLength);

      StringBuffer buffer;
      // If the "to" parameter is longer than (or as long as) "from", the final length will
      // be at least as large.
      if ( greaterLength )
      {
         if ( from.equals (to) )
            return orig;
         buffer = new StringBuffer(orig.length());
      }
      else
      {
         buffer = new StringBuffer();
      }

      char [] origChars = orig.toCharArray();

      int copyFrom=0;
      while ( start != -1 )
      {
         buffer.append (origChars, copyFrom, start-copyFrom);
         buffer.append (to);
         copyFrom=start+fromLength;
         start = orig.indexOf (from, copyFrom);
      }
      buffer.append (origChars, copyFrom, origChars.length-copyFrom);

      //System.out.println("changed String: " + buffer);

      return buffer.toString();
   }


   /**
    * Converts a char to a HEX value, two character string. ie char:0 = "00" char:255 = "FF".
    * 
    * @param c The character to convert.
    * 
    * @return A two character string is returned in HEX format.
    */
   public static String getHexStringCHAR(char c)
   {
      String str = new String();

      // Handle the case of -1.
      if ( c == -1 )
      {
         str = "FF";
         return str;
      }

      if ( c < 16 )
      {
         // Pad out string to make it look nice.
         str = "0";
      }

      str += (Integer.toHexString((int)c)).toUpperCase();
      return str;
   }

   /**
    * Converts a byte to a HEX value, two character string. ie byte:0 = "00" byte:255 = "FF".
    * 
    * @param b The byte to convert.
    * 
    * @return A two character string is returned in HEX format.
    */
   public static String getHexStringBYTE(byte b)
   {
      String str = new String();

      // Handle the case of -1.
      if ( b == -1 )
      {
         str = "FF";
         return str;
      }

      int value = (((int)b) & 0xFF);

      if ( value < 16 )
      {
         // Pad out string to make it look nice.
         str = "0";
      }

      str += (Integer.toHexString(value)).toUpperCase();
      return str;
   }

   /**
    * Converts an int to a HEX value, four character string. ie int:0 = "0000" int:65356 = "FFFF".
    * 
    * @param w The int to convert.
    * 
    * @return A four character string is returned in HEX format.
    */
   public static String getHexStringWORD(short w)
   {
      String str = new String();

      // Handle the case of -1.
      if ( w == -1 )
      {
         str = "FFFF";
         return str;
      }

      int value = (((int)w) & 0xFFFF);

      if ( value < 16 )
      {
         // Pad out string to make it look nice.
         str = "000";
      }
      else if ( value < 256 )
      {
         // Pad out string to make it look nice.
         str = "00";
      }
      else if ( value < 4096 )
      {
         // Pad out string to make it look nice.
         str = "0";
      }

      str += (Integer.toHexString(value)).toUpperCase();
      return str;
   }

   /**
    * Converts a int to a HEX long, two-byte character string. ie 0=="00000000" -1=="FFFFFFFF".
    * 
    * @param dw The int to convert.
    * 
    * @return A two-byte character string is returned in HEX format.
    */
   public static String getHexStringDWORD(int dw)
   {
      String str = new String();

      // Handle the case of -1.
      if ( dw == -1 )
      {
         str = "FFFFFFFF";
         return str;
      }

      long value = (((long)dw) & 0xFFFFFFFFL);
      if ( value < 0 )
      {
         System.out.println("failed!!!:" + value);
      }

      if ( value < 16 )
      {
         // Pad out string to make it look nice.
         str = "0000000";
      }
      else if ( value < 256 )
      {
         // Pad out string to make it look nice.
         str = "000000";
      }
      else if ( value < 4096 )
      {
         // Pad out string to make it look nice.
         str = "00000";
      }
      else if ( value < 65536 )
      {
         // Pad out string to make it look nice.
         str = "0000";
      }
      else if ( value < 1048576 )
      {
         // Pad out string to make it look nice.
         str = "000";
      }
      else if ( value < 16777216 )
      {
         // Pad out string to make it look nice.
         str = "00";
      }
      else if ( value < 268435456 )
      {
         // Pad out string to make it look nice.
         str = "0";
      }

      str += (Long.toHexString(value)).toUpperCase();
      return str;
   }

   /**
	* Converts a byte array to a <code>String</code> in HEX format.
	* 
	* @param data The byte array to convert.
	* 
	* @return A character string is returned in HEX format.
	*/
   public static String getHexStringBYTEArray(byte[] data)
   {
      if (data == null)
      {
         return new String("");
      }

      return getHexStringBYTEArray(data, 0, data.length);
   }

   /**
    * Returns a <code>String</code> from the byte array.
	* 
	* @param data The byte array to convert.
	* 
	* @return The <code>String</code> representation of the specified byte array is returned.
    */
   public static String getStringBYTEArray(byte[] data)
   {
		return getStringBYTEArrayOffset(data, 0);
   }

   /**
    * Returns a <code>String</code> from the byte array beginning at the specifed <code>offset</code>.
	* 
	* @param data The byte array to convert.
	* @param offset An offset into the byte array.
	* 
	* @return The <code>String</code> representation of the specified byte array is returned.
    */
   public static String getStringBYTEArrayOffset(byte[] data, int offset)
   {
      if (data == null)
      {
         return new String("");
      }

      String str = new String();
      for (int x=0+offset; x<data.length; x++)
      {
			if (data[x] == 0)
			{
				return str;
			}
         str += (char)data[x];
      }
      return str;
   }

   /**
    * Returns a <code>String</code> from the byte array.
	* 
	* @param data The byte array to convert.
	* 
	* @return The <code>String</code> representation of the specified byte array is returned.
    */
   public static byte[] getBYTEArrayString(String str)
   {
      if (str == null)
      {
         return new byte[0];
      }

      byte[] data = new byte[str.length()];
      for (int x=0; x<str.length(); x++)
      {
         data[x] = (byte)str.charAt(x);
      }
      return data;
   }

   /**
    * Get the HEX character string from the specified byte array beginning at the specifed <b>offset</b>.
    * 
    * @param data The byte array to convert
    * @param offset An offset into the byte array.
    * @param length The number of characters in the <code>String</code> to convert.
    * 
    * @return A character string is returned in HEX format.
    */
   public static String getHexStringBYTEArray(byte[] data, int offset, int length)
   {
      if (data == null)
      {
         return new String("");
      }

      StringBuffer buff = new StringBuffer(4096);
      //String str = new String();
      for ( int i=offset; i<offset+length; i++ )
      {
         int val = data[i];
         //str += getHexStringBYTE(data[i]);
         buff.append(getHexStringBYTE(data[i]));
         if ( i != data.length - 1 )
         {
            buff.append(" ");
         }
      }
      return buff.toString();
   }

   // Traverse the specified String looking for tokens delimitted by whitespace (e.g. ' ', \t').
   // Place each token in the array of bytes as a unique element. The number of tokens will be
   // returned.
   private static int traverseString(String hexString, byte[] data)
   {
      int ct = 0;

      // Make a copy of the string.
      String str = new String(hexString);
      str = str.trim();
      //System.out.println("traverseString: '" + str + "'");

      while ( str.equals("") == false )
      {
         int i = 0;
         String numStr = "";

         // Try to build up value.
         try
         {
            char c = str.charAt(i);
            i++;
            //System.out.println("c:" + c);
            while ( c != ' ' && c != '\t' )
            {
               numStr += "" + c;
               c = str.charAt(i);
               i++;
               //System.out.println("c:" + c);
            }
         }
         catch ( Exception e )
         {
            // This is okay. really...
         }

         int val = Integer.parseInt(numStr, 16);

         if ( data != null )
         {
            try
            {
               data[ct] = (byte) val;
            }
            catch ( Exception e )
            {
               // Failed to save value into data array.
               // Most likely, the array isn't big enough.
               return -1;
            }
         }

         str = str.substring(i, str.length());
         str = str.trim();
         //System.out.println("ct: " + ct + " numStr: '" + numStr + "' val:" + val + "   str:'" + str + "'");
         ct++;
      }

      return ct;
   }

   /**
    * Converts a <code>String</code> of the format "00 11 FF" to a byte array: { 00, 17 , 255 }.
    * 
    * @param hexString The <code>String</code> to convert.
    * 
    * @return A byte array containg the parsed HEX values is returned.
    */
   public static byte[] getBYTEArrayFromHexString(String hexString)
   {
      // Call it first to determine the size the array should be.
      int dataCt = traverseString(hexString, null);
      byte[] data = new byte[dataCt];
      // Now do it again putting the data into the array.
      traverseString(hexString, data);

      return data;
   }

  /**
   * Parse a line into fields separated by a specified delimeter.
   * 
   * @param aLine A String containing the line to parse.
   * @param delimiter The delimeter denoting the separation of fields.
   * 
   * @return The parsed field array is returned.
   */
  public static String[] parseStringLine(String aLine, String delimiter)
  {
      StringTokenizer st = new StringTokenizer(aLine, delimiter, true);
     // System.out.println("GuideDataRecord: size:"+st.countTokens()+" delimiter:<"+delimiter+">");
      Vector rc = new Vector();
      String lastToken = delimiter;
      while(st.hasMoreTokens()){
          String aToken = (String)st.nextToken();
          if(aToken == null) rc.addElement("");
          else if(aToken.equals(delimiter)){
            if(lastToken.equals(delimiter))
              rc.addElement("");
          }
          else
            rc.addElement(aToken.trim());
          lastToken = aToken;
          // System.out.println("A Token: "+rc[count]);
      }
      return (String[]) rc.toArray(new String[rc.size()]);
  }

}
