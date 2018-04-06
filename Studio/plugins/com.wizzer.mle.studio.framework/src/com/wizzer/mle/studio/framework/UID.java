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
