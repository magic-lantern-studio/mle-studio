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

// Import standard java packages.
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class provides some useful utility for managing directory path names.
 * 
 * @author Mark S. Millard
 */
public class PathUtil
{
	/*
	 * Various constants that might be found in pathnames.
	 */
	static private char TILDE = '~';
	static private char SLASH = '/';
	static private char DOLLAR = '$';
	static private char NIL = '\0';
	static private char PERCENT = '%';
	static private String SPECIALS = "_";
	static private char LBRACE = '{';
	static private char RBRACE = '}';

    /* The regex for one or more spaces. */
	static final private String ONE_OR_MORE_SPACES = "\\s+";

	// Some predefined, known environment variables.
	static private Hashtable m_knownEnvVars = new Hashtable();
	// A convenience value for appending paths with incremental values.
	static private int m_filenameIndex = 0;

    // Register the known environment variables.
    static
    {
    	String var = new String("MLE_HOME");
    	String value = new String("MleDefaultHomePath");
    	m_knownEnvVars.put(var,value);
    }
	
    static public String filenameExpand(String filename)
    {
    	StringBuffer path = new StringBuffer(filename);
		StringBuffer userName =  null;
		StringBuffer tildeName = null;
		char curPtr;
		StringBuffer varName, val;
		int curIndex = 0;
		int newIndex = 0;
		StringBuffer retString = new StringBuffer(filename.length());
		int retIndex = 0;

		// Return an empty String if nothing to expand.
		
		if (filename == null) 
		{
			return new String("");
		}
    
		// Quick check for no work; if path doesn't start with '~' or contain a '$' or '%' character.
		
		if ((filename.charAt(0) != TILDE) &&
			(filename.indexOf(DOLLAR) == -1) &&
			(filename.indexOf(PERCENT) == -1))
		{
			return new String(filename);
		}

		// If the first character is a tilde, process it.  Note that embedded
		// tildes are not allowed, as per C-shell semantics. 

		curPtr = path.charAt(curIndex);
		if (curPtr == TILDE)
		{
			curIndex++;
			if (curIndex < path.length())
			{
				curPtr = path.charAt(curIndex);
			}
			
			if ((curIndex == path.length())  || curPtr == SLASH)
			{
				tildeName = getHomeOfMe();
			} else
			{

				if ((newIndex = path.toString().indexOf(SLASH)) == -1)
				{
					newIndex = path.length();
				}
				userName = new StringBuffer(path.substring(curIndex,newIndex));
				tildeName = getHomeOfName(userName);
				curIndex = newIndex;
			}
			retString.insert(retIndex,tildeName.toString());
			retIndex += tildeName.toString().length();
			//retString.insert(retIndex,SLASH);
			//retIndex++;
		}

		// curIndex now points to the current scan point in the remaining input.
		// Check for environment variables. 

		while (curIndex < path.length())
		{
			// Get the next character.
			curPtr = path.charAt(curIndex);
			
			if (curPtr == DOLLAR)
			{
				varName = new StringBuffer(path.length() - curIndex);
				curIndex++;
				curPtr = path.charAt(curIndex);
				if (curPtr == LBRACE)
				{
					newIndex = 0;
					curIndex++;
					curPtr = path.charAt(curIndex);
					while ((curPtr != NIL) && (curPtr != RBRACE))
					{
						varName.insert(newIndex,curPtr);
						newIndex++;
						curIndex++;
						curPtr = path.charAt(curIndex);
					}
					if (curPtr != NIL)
					{
						curIndex++;
					}
				} else
				{
					newIndex = 0;
					while ((curIndex < path.length()) && (isAlnum(curPtr) ||
						   (SPECIALS.indexOf(curPtr) != -1)))
					{
						curPtr = path.charAt(curIndex);
						varName.insert(newIndex,curPtr);
						newIndex++;
						curIndex++;
					}
				}

                // Terminate the variable name.
				//varName.insert(newIndex,NIL);
				
				// Get the system environment variable.
				if ((val = getEnv(varName)) == null)
				{
					// If the undefined environment variable is in the known table,
					// substitute the default value.
					
					if (m_knownEnvVars.containsKey(varName.toString()))
					{
						val = new StringBuffer((String)m_knownEnvVars.get(varName.toString()));
						retString.insert(retIndex,val.toString());
						retIndex += val.toString().length();
						//retString.insert(retIndex,SLASH);
						//retIndex++;
					}
				} else
				{
					retString.insert(retIndex,val.toString());
					retIndex += val.toString().length();
					//retString.insert(retIndex,SLASH);
					//retIndex++;
				}

			} else if ( curPtr == PERCENT )
			{
				char[] fmt = new char[3];
				String buf;

				curPtr = path.charAt(curIndex);
				fmt[0] = curPtr; curIndex++;
				curPtr = path.charAt(curIndex);
				fmt[1] = curPtr; curIndex++;
				fmt[2] = 0;
				if ( fmt[1] == 'n' )
				{
					String value = String.valueOf(m_filenameIndex);
					buf = new String(value);
				} else
				{
					GregorianCalendar clock = new GregorianCalendar();
					String timestamp = new String(
					    new Integer(clock.get(Calendar.DAY_OF_MONTH)).toString() +
						new Integer(clock.get(Calendar.MONTH)).toString() +
						new Integer(clock.get(Calendar.YEAR)).toString() +
						new Integer(clock.get(Calendar.HOUR)).toString() +
						new Integer(clock.get(Calendar.MINUTE)).toString() +
						new Integer(clock.get(Calendar.SECOND)).toString()
					);
					buf = new String(timestamp);
				}
				retString.insert(retIndex,buf);
				retIndex += buf.length();

			} else
			{
				retString.insert(retIndex,curPtr);
				retIndex++;
				curIndex++;
			}
		}

        // Terminate the return string.
		//retString.insert(retIndex,NIL);
		
		// Return the expanded file name.
		return retString.toString();
    }
        
    // Get the system environment variable.
    static private StringBuffer getEnv(StringBuffer var)
    {
    	String varName = new String(var);
    	String value = System.getProperty(varName);
    	if (value != null)
    	    return new StringBuffer(value);
    	else
    	    return null;
    }
    
    // Get the home path for the user.
    static private StringBuffer getHomeOfMe()
    {
    	String home = System.getProperty("HOME");
    	if (home != null)
    	    return new StringBuffer(home);
    	
        StringBuffer path = new StringBuffer(1);
        path.insert(0,TILDE);
    	return path;
    }
    
    // Get the home path for the specified user.
    // XXX - not yet supported.
    static private StringBuffer getHomeOfName(StringBuffer user)
    {
    	int len = 1 + user.length() + 1;
		StringBuffer path = new StringBuffer(len);
		path.insert(0,TILDE);
		path.insert(1,user);
		return path;
    }

	static public boolean isAlpha(char ch) 
	{
		return Character.isLetter(ch);
	}

	static public boolean isAlnum(char ch) 
	{
		return Character.isLetterOrDigit(ch);
	}

	static public boolean isLowerCase(char ch) 
	{
		return Character.isLowerCase(ch);
	}

	static public char toLowerCase(char ch) 
	{
		return (isUpperCase(ch) ? Character.toLowerCase(ch) : ch);
	}

	static public boolean isUpperCase(char ch) 
	{
		return Character.isUpperCase(ch);
	}

	static public char toUpperCase(char ch) 
	{
		return (isLowerCase(ch) ? Character.toUpperCase(ch) : ch);
	}
    
	static public boolean isSpace(char ch) 
	{
		return Character.isSpace(ch);
	}

	static public boolean isDigit(char ch) 
	{
		return Character.isDigit(ch);
	}
	
	static public void setFilenameIndexSet(int x)
	{
		m_filenameIndex = x;
	}

	static void incFilenameIndex()
	{
		m_filenameIndex++;
	}

	/**
	 * Converts a url to a file object.
	 * 
	 * @param url The url of the resource.
	 * @return The corresponding file.
	 */
	static public File urlToFile(URL url)
	{
	    final String noSpaces = url.toString().replaceAll(ONE_OR_MORE_SPACES, "%20");
	    File result = null;
	    try {
	        final URI uri = new URI(noSpaces);
	        result = new File(uri);
	    } catch (final URISyntaxException e) {
	        e.printStackTrace();
	    }
	    return result;
	}

	static public void main(String[] args)
    {
    	// Test 1: simple path.
    	String filename = "C:/Users/msm/test/for/filename/expansion.txt";
    	String result = PathUtil.filenameExpand(filename);
    	System.out.println("Test 1: " + result);
    	// Test 2: expand tilde with path.
		filename = "~/test/for/filename/expansion.txt";
		result = PathUtil.filenameExpand(filename);
		System.out.println("Test 2: " + result);
		// Test 3: expand just environemnt variable.
		filename = "$HOME";
		result = PathUtil.filenameExpand(filename);
		System.out.println("Test 3: " + result);
		// Test 4: expand environemnt variable with path.
		filename = "${HOME}/test/for/filename/expansion.txt";
		result = PathUtil.filenameExpand(filename);
		System.out.println("Test 4: " + result);
		// Test 5: expand just tilde.
		filename = "~";
		result = PathUtil.filenameExpand(filename);
		System.out.println("Test 5: " + result);
		// Test 6: test for known default values.
		filename = "$MLE_HOME";
		result = PathUtil.filenameExpand(filename);
		System.out.println("Test 6: " + result);
		// Test 7: test for incremental filename generation.
		for (int i = 0; i < 10; i++)
		{
			PathUtil.incFilenameIndex();
			filename = "${MLE_HOME}/animation/file%n";
			result = PathUtil.filenameExpand(filename);
			System.out.println("Test 7: " + result);
		}
		// Test 8: test for timestamp filename generation.
		filename = "${HOME}/animation/file%d";
		result = PathUtil.filenameExpand(filename);
		System.out.println("Test 8: " + result);
    }
    
}
