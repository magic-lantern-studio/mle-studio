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

/**
 * This class implements a <code>String</code> attribute using a HEX format.
 * 
 * @author Jim Campanel
 */
public class HexStringAttribute extends StringAttribute
{
	/**
	 * A constructor that initializes the <code>HexStringAttribute</code>.
	 * 
	 * @param name The name of the attribute.
	 * @param value The value of the attribute.
	 * @param characters The number of bytes that are valid as part of the <code>HexStringAttribute</code>
	 * value.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
    public HexStringAttribute(String name, String value, int bytes, boolean isReadOnly)
    {
        // This is a string that is up to twice the number of valid byte characters
        // (two hex characters per byte).
        super(name, value, bytes * 2, isReadOnly );
        
        // Assume hex display type to start
        m_displayType = HEX_STRING;
    }
    
	/**
	 * A constructor that initializes the <code>HexStringAttribute</code>.
	 * 
	 * @param name The name of the attribute.
	 * @param value The value of the attribute as a byte array.
	 * @param characters The number of bytes that are valid as part of the <code>HexStringAttribute</code>
	 * value.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
    public HexStringAttribute(String name, byte[] value, int bytes, boolean isReadOnly)
    {
        super( name, new String(), bytes * 2, isReadOnly );
        
        // Convert the hex data to a string.
        StringBuffer stringValue = new StringBuffer();
        for (int i = 0; i < value.length; i++)
        {
            stringValue.append( m_hexCharacter[ (value[i] >> 4) & 0x0f ] );
            stringValue.append( m_hexCharacter[ value[i] & 0x0f ] );
        }
        
        setValue( stringValue.toString() );
        
        // Assume hex display type to start
        m_displayType = HEX_STRING;
    }
    
	/**
	 * Get the <code>HexStringAttribute</code> type.
	 * 
	 * @return By default, <b>TYPE_HEX_STRING</b> is always returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getType()
	 */
    public String getType()
    {
        return Attribute.TYPE_HEX_STRING;
    }

    /**
     * Get the value of the <code>HexStringAttribute</code> as an byte array representation.
     * 
     * @param getSubordinates Not used.
     * 
     * @return The representation of the <code>HexStringAttribute</code> is returned as a byte array.
     */
    public byte[] getByteRepresentation( boolean getSubordinates )
    {
        StringBuffer text;
        
        // Get the current string in hex.
        if ( CHARACTER_STRING == m_displayType )
        {
            // Need to convert the character data to hex
            text = new StringBuffer( getHexFromString( getStringValue() ) );
        }
        else
        {
            // Already in hex
            text = new StringBuffer( getStringValue() );
        }

        // Tack on a training zero in case we have an odd number of hex characters.
        text.append( "0" );
        
        // This will drop the trailing zero from our processing if we have an even number
        // of characters to start.
        int byteCnt = text.length() / 2;
        
        // Create the array to be returned.
        byte[] bytes = new byte[ byteCnt ];
        
        // Convert the 2 character bytes to binary.
        for (int i = 0; i < byteCnt; i++)
        {
            bytes[i] = (byte) Integer.parseInt( text.substring( i * 2, i * 2 + 2 ), 16 );
        }
    
        return bytes;
    }

	/**
	 * Get the value of the <code>HexStringAttribute</code> as a bit string.
	 * 
	 * @return  A <code>String</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getBitString()
	 */    
    public String getBitString()
    {

        String result = "";
        byte[] bytes = getByteRepresentation( false );

        for (int i = 0; i < bytes.length; i++)
        {
            byte b = bytes[ i ];
            String charString = Integer.toString(b & 0xff, 2);
            charString = StringAttribute.Pad(charString, true, "0", 8);
            result = result + charString;
        }

        result = this.Pad(result, false, "0");

        return result;
    }

	/**
	 * Validate the value of the <code>HexStringAttribute</code>.
	 * 
	 * @return <b>true</b> will be returned if the value of the attribute is valid.
	 * Otherwise, b>false</b> will be returned if the value is invalid.
	 * 
	 * @see com.wizzer.mle.studio.framework.Attribute#validate()
	 */
    public boolean validate()
    {
        // May be different depending on the display type set
        if ( HEX_STRING == m_displayType )
        {
            // Must be any number of valid hex characters
            return getStringValue().matches("[0-9,a-f,A-F]*");
        }
        else
        {
            // Character strings take any value.
            return true;
        }
    }
    
    private String getStringFromHex( String hexString )
    {
        StringBuffer newStringValue = new StringBuffer();
        
        // Update the internal data to the proper representation
        StringBuffer oldStringValue = new StringBuffer( hexString );

        // If an odd number of digits, add a zero to the end.
        if ( ( oldStringValue.length() % 2 ) == 1 )
        {
            oldStringValue.append( "0" );
        }
        
        // Convert the hex to string data
        for (int i = 0; i < oldStringValue.length(); i += 2)
        {
            int value = Integer.parseInt( oldStringValue.substring( i, i + 2 ), 16 );
            char valueChar = (char) value;
            // Are we less than space or greater than tilde on the ascii value?
            // If so, it's a non-printable value, so escape it (follows HTML convention).
            // Also, escape an ampersand so it can't be seen as an escape sequence when converted back.
            if ( ( ' ' > valueChar ) || ( '~' < valueChar ) || ( '&' == valueChar ) )
            {
                newStringValue.append( "&#" );
                newStringValue.append( m_hexCharacter[ (value >> 4) & 0xf ] );
                newStringValue.append( m_hexCharacter[ value & 0xf ] );
                newStringValue.append( ';' ); 
            }
            else
            {
                // Normal character.  Just display it.
                newStringValue.append( valueChar  );
            }
        }
        
        return newStringValue.toString();
    }
    
    private boolean isHexDigit( char hexDigit )
    {
        // Check the numric digits
        if ( ( '0' <= hexDigit ) && ( '9' >= hexDigit ) )
        {
            return true;
        }

        // Check a-f, lowercase        
        if ( ( 'a' <= hexDigit ) && ( 'f' >= hexDigit ) )
        {
            return true;
        }
        
        // Check a-f, uppercase        
        if ( ( 'A' <= hexDigit ) && ( 'F' >= hexDigit ) )
        {
            return true;
        }
        
        return false;
    }
    
    private String getHexFromString( String charString )
    {
        StringBuffer newStringValue = new StringBuffer();
        
        // Update the internal data to the proper representation
        StringBuffer oldStringValue = new StringBuffer( charString );
            
        for (int i = 0; i < oldStringValue.length(); ++i)
        {
            int value = oldStringValue.charAt( i );
            boolean isEscape = false;
            
            if ( ( '&' == (char) value ) && ( '#' == oldStringValue.charAt( i + 1 ) ) )
            {
                int hexValue = 0;
                
                // It OK as long as we have no more than 2 hex digits followed by a semi-colon.
                int j;
                for (j = i + 2; ( j < oldStringValue.length() ) && 
                                isHexDigit( oldStringValue.charAt( j ) ) &&
                                ( j < i + 4 ); j++ )
                {
                }
                
                if ( ( j < oldStringValue.length() ) && ( ';' == oldStringValue.charAt( j ) ) )
                {
                    // Convert the escaped hex digits to a value.
                    value = Integer.parseInt( oldStringValue.substring( i + 2, j ), 16 ) & 0xff;
                    
                    // Skip the escape sequence
                    i += j - i;
                }
            }
            
            // Convert the value to a string of 2 hex digits.
            newStringValue.append( m_hexCharacter[ (value >> 4) & 0xf ] );
            newStringValue.append( m_hexCharacter[ value & 0xf ] );
        }
        
        return newStringValue.toString();
    }

    /**
     *  Set the display/edit mode to a character string.
     * <p>
     * This routine is usually used by a GUI for setting the display mode.
     * </p>
     */
    public void setCharacterStringDisplay()
    {
        // Ignore the request if we're already there.
        if ( CHARACTER_STRING != m_displayType )
        {
            // Set the display type
            m_displayType = CHARACTER_STRING;
            
            // Convert the data.
            setValue( getStringFromHex( getValue() ) );
        } // End of if we need to convert the internal data
    }

	/**
	 *  Set the display/edit mode to a HEX string.
	 * <p>
	 * This routine is usually used by a GUI for setting the display mode.
	 * </p>
	 */
    public void setHexStringDisplay()
    {
        // Ignore the request if we're already there.
        if ( HEX_STRING != m_displayType )
        {
            // Set the display type
            m_displayType = HEX_STRING;
            
            // Convert the data.
            setValue( getHexFromString( getValue() ) );
        } // End of if we need to convert the internal data
    }
    
    /**
     * Check the current format for the display string.
     * 
     * @return If <b>true</b> is returned, then the display string format is in HEX mode.
     * If <b>false</b> is returned, then the string format is in character display mode.
     */
    public boolean isHexStringDisplay()
    {
        if (HEX_STRING == m_displayType)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    static final String CHARACTER_STRING = "com.wizzer.mle.studio.framework.attribute.HEXSTRING";
    static final String HEX_STRING = "com.wizzer.mle.studio.framework.attribute.HEXSTRING";
    private String m_displayType;
    static final char[] m_hexCharacter = 
        { '0', '1', '2', '3', '4', '5', '6', '7', 
          '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'  };
}
