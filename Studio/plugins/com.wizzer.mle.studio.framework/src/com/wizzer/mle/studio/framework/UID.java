/*
 * Unique identifiers which are "published" in some way may need special treatment,
 * since the identifier may need to be difficult to guess or forge. A typical example
 * is the value of a cookie used as a browser identifier - simply using a series of
 * consecutive integers is generally unacceptable, since one user could easily impersonate
 * another by altering the value of the cookie to some nearby integer. 
 * 
 * Here is one way to generate such identifiers : 
 *
 *  1. upon startup, initialize SecureRandom (this is a lengthy operation, so needs to be done
 *     outside of normal operation) 
 *  2. when a new identifier is needed, generate a random number using SecureRandom 
 *  3. create a MessageDigest of the random number 
 *  4. encode the byte[] returned by the MessageDigest into some acceptable textual form 
 *  5. check if the result is already being used ; if it is not already taken, it is suitable
 *     as a unique identifier
 *
 * The MessageDigest class is suitable for generating a "one-way hash" of  arbitrary data.
 * (Note that hash values never uniquely identify their source data, since different source
 * data can produce the same hash value. The value of hashCode, for example, does not uniquely
 * identify its associated object.) A MessageDigest takes any input, and produces a String
 * which : 
 * 
 *  1. is of fixed length 
 *  2. does not allow the original input to be easily recovered (in fact, this is very hard) 
 *  3. does not uniquely identify the input ; however, similar input will produce dissimilar
 *   message digests 
 * 
 * MessageDigest is often used as a checksum, for verifying that data has not been altered
 * since its creation.
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
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * This class is used to generate an unique identifier.
 * 
 * @author Mark S. Millard
 */
public class UID
{
	// The random number generator.
	static private SecureRandom m_prng = null;

	/**
	 * Generate an unique identifier.
	 * 
	 * @return A <code>String</code> is returned containing a random number that
	 * has been encoded into a HEX representation of tha value's message digest.
	 */
	static public String generateUID()
	{
		String uid = null;
		
		try
		{
		    // Initialize SecureRandom, if necessary.
		    // This is a lengthy operation, to be done only upon
			// first invokation.
			if (m_prng == null)
			    m_prng = SecureRandom.getInstance("SHA1PRNG");

			// Generate a random number.
			String randomNum = new Integer( m_prng.nextInt() ).toString();

			// Get its digest.
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] result =  sha.digest( randomNum.getBytes() );

			uid = hexEncode(result);
			FrameworkLog.logInfo("Unique identifier generated: " + uid );
			
		} catch ( NoSuchAlgorithmException ex )
		{
			  FrameworkLog.logError(ex,"Unable to generate unique identifier.");
		}
		
		return uid;
	}
	
	/*
	 * The byte[] returned by <code>MessageDigest</code> does not have a nice
	 * textual representation, so some form of encoding is usually performed.
	 *
	 * This implementation follows the example of David Flanagan's book
	 * "Java In A Nutshell", and converts a byte array into a String
	 * of hex characters.
	 *
	 * Another popular alternative is to use a "Base64" encoding.
	 */
	static private String hexEncode(byte[] aInput)
	{
		StringBuffer result = new StringBuffer();
		char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
		
		for ( int idx = 0; idx < aInput.length; ++idx)
		{
		    byte b = aInput[idx];
		    result.append( digits[ (b&0xf0) >> 4 ] );
		    result.append( digits[ b&0x0f] );
		}
		
		return result.toString();
	} 

	// Hide default constructor.
	private UID() {}

}
