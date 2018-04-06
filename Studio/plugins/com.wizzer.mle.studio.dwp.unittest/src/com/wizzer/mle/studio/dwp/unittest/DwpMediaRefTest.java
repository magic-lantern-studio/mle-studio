/*
 * DwpMediaRefTest.java
 * Created on Jul 8, 2004
 */

// COPYRIGHT_BEGIN

// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2007  Wizzer Works
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
//
//  Copyright (C) 2000-2007  Wizzer Works
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
package com.wizzer.mle.studio.dwp.unittest;

// Import standard Java packages.
import java.io.FileOutputStream;
import java.util.Vector;

//Import JUnit packages.
import junit.framework.TestCase;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.domain.TableIterator;

// Import Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpReader;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaRefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaRefSourceAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaRefTargetAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaRefClassAttribute;
import com.wizzer.mle.studio.dwp.domain.DwpTable;

/**
 * This class is a unit test for the Magic Lantern digital Workprint Media Reference
 * items.
 * 
 * @author Mark S. Millard
 */
public class DwpMediaRefTest extends TestCase
{
	static private String DIGITAL_WORKPRINT_TESTFILE = "test.dwp";
	static private FileOutputStream m_file = null;
	static private StringBuffer m_buffer = null;

	/**
	 * Constructor for DwpMediaRefTest.
	 * 
	 * @param name The name of the unit test.
	 */
	public DwpMediaRefTest(String name)
	{
		super(name);
	}

	/**
	 * Executes the test runner.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(DwpMediaRefTest.class);
	}

	/**
	 * Set up the test case.
	 * 
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		System.out.println("Setting up test case for DWP MEdia Reference.");
		super.setUp();
		
		// Create the file.
		m_file = new FileOutputStream(DIGITAL_WORKPRINT_TESTFILE);
		if (m_file != null)
		{
			// Construct a valid Digital Workprint.
			m_buffer = new StringBuffer();
			m_buffer.append("#DWP 1.0 ascii\n");
			m_buffer.append("( ActorDef MyActor\n");
			m_buffer.append("\t( PropertyDef name string\n");
			m_buffer.append("\t\t( MediaRefClass image MyFace )\n");
			m_buffer.append("\t)\n");
			m_buffer.append(")\n");
			m_buffer.append("( MediaRef movie MyHomeMovies\n");
			m_buffer.append("\t( MediaRefSource +mpg 0\n");
			m_buffer.append("\t\t( Media 0 movie file://../media/home.mpg )\n");
			m_buffer.append("\t)\n");
			m_buffer.append("\t( MediaRefTarget +win32 0\n");
			m_buffer.append("\t\t( Media 0 movie file://../media/home.avi )\n");
			m_buffer.append("\t)\n");
			m_buffer.append(")\n");
			
			// Write the expected contents.
			m_file.write(m_buffer.toString().getBytes());
			
			// Flush the output.
			m_file.flush();
		}
	}

	/**
	 * Tear down the test case.
	 * 
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		System.out.println("Tearing down test case for DWP Media Reference.");
		super.tearDown();
		
		// Close the file.
		m_file.close();
	}
	
	/**
	 * Test the read() method.
	 */
	public void testRead()
	{
		// Test for success.
		DwpReader reader = new DwpReader(DIGITAL_WORKPRINT_TESTFILE);
		DwpTable table = reader.read();
		TestCase.assertNotNull(table);
		
		// Test that a valid DwpTable was created.
		if (table != null)
		{
			TableIterator iter = new TableIterator(table);
			
			System.out.println("Testing generated DWP domain table contents.");

			// Look for MediaRef items.
			Vector attrs = iter.findAttributes("MediaRef",table.getTopAttribute());
			TestCase.assertEquals(1,attrs.size());
			DwpMediaRefAttribute mrefProperty = (DwpMediaRefAttribute) attrs.elementAt(0);
			TestCase.assertEquals("MediaRef movie MyHomeMovies",mrefProperty.toString());
			TestCase.assertEquals("movie MyHomeMovies",mrefProperty.getStringValue());
			// Look for MediaRefSource items.
			attrs = iter.findAttributes("MediaRefSource",table.getTopAttribute());
			TestCase.assertEquals(1,attrs.size());
			DwpMediaRefSourceAttribute mrefSrcProperty = (DwpMediaRefSourceAttribute) attrs.elementAt(0);
			TestCase.assertEquals("MediaRefSource +mpg 0",mrefSrcProperty.toString());
			TestCase.assertEquals("0",mrefSrcProperty.getStringValue());
			// Look for MediaRefTarget items.
			attrs = iter.findAttributes("MediaRefTarget",table.getTopAttribute());
			TestCase.assertEquals(1,attrs.size());
			DwpMediaRefTargetAttribute mrefTargetProperty = (DwpMediaRefTargetAttribute) attrs.elementAt(0);
			TestCase.assertEquals("MediaRefTarget +win32 0",mrefTargetProperty.toString());
			TestCase.assertEquals("0",mrefTargetProperty.getStringValue());
			// Look for Media items.
			attrs = iter.findAttributes("Media",table.getTopAttribute());
			TestCase.assertEquals(2,attrs.size());
			DwpMediaAttribute mediaProperty = (DwpMediaAttribute) attrs.elementAt(0);
			TestCase.assertEquals("Media 0 movie file://../media/home.mpg",mediaProperty.toString());
			TestCase.assertEquals("0 movie file://../media/home.mpg",mediaProperty.getStringValue());
			mediaProperty = (DwpMediaAttribute) attrs.elementAt(1);
			TestCase.assertEquals("Media 0 movie file://../media/home.avi",mediaProperty.toString());
			TestCase.assertEquals("0 movie file://../media/home.avi",mediaProperty.getStringValue());
			// Look for MediaRefClass items.
			attrs = iter.findAttributes("MediaRefClass",table.getTopAttribute());
			TestCase.assertEquals(1,attrs.size());
			DwpMediaRefClassAttribute mrefclassProperty = (DwpMediaRefClassAttribute) attrs.elementAt(0);
			TestCase.assertEquals("MediaRefClass image MyFace",mrefclassProperty.toString());
			TestCase.assertEquals("image MyFace",mrefclassProperty.getStringValue());
		}
	}
	
	// Load the required DLL.
	static
	{
		System.loadLibrary("DwpReader");
	}

}
