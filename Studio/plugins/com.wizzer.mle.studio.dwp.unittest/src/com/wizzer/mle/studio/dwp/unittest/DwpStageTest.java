/*
 * DwpStageTest.java
 * Created on Jul 9, 2004
 */

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
import com.wizzer.mle.studio.dwp.attribute.DwpStageDefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpStageAttribute;
import com.wizzer.mle.studio.dwp.domain.DwpTable;

/**
 * This class is a unit test for the Magic Lantern Digital Workprint Stage items.
 * 
 * @author Mark S. Millard
 */
public class DwpStageTest extends TestCase
{
	static private String DIGITAL_WORKPRINT_TESTFILE = "test.dwp";
	static private FileOutputStream m_file = null;
	static private StringBuffer m_buffer = null;

	/**
	 * Constructor for DwpStageTest.
	 * 
	 * @param name The name of the unit test.
	 */
	public DwpStageTest(String name)
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
		junit.textui.TestRunner.run(DwpStageTest.class);
	}

	/**
	 * Set up the test case.
	 * 
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		System.out.println("Setting up test case for DWP Stage.");
		super.setUp();
		
		// Create the file.
		m_file = new FileOutputStream(DIGITAL_WORKPRINT_TESTFILE);
		if (m_file != null)
		{
			// Construct a valid Digital Workprint.
			m_buffer = new StringBuffer();
			m_buffer.append("#DWP 1.0 ascii\n");
			m_buffer.append("( StageDef MyGlobe\n");
			m_buffer.append("\t( PropertyDef name string )\n");
			m_buffer.append("\t( PropertyDef capacity int )\n");
			m_buffer.append(")\n");
			m_buffer.append("( Stage theatre MyGlobe\n");
			
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
		System.out.println("Tearing down test case for Stage.");
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

			// Look for StageDef items.
			Vector attrs = iter.findAttributes("StageDef",table.getTopAttribute());
			TestCase.assertEquals(1,attrs.size());
			DwpStageDefAttribute stagedefProperty = (DwpStageDefAttribute) attrs.elementAt(0);
			TestCase.assertEquals("StageDef MyGlobe",stagedefProperty.toString());
			TestCase.assertEquals("MyGlobe",stagedefProperty.getStringValue());
			attrs = iter.findAttributes("Stage",table.getTopAttribute());
			TestCase.assertEquals(1,attrs.size());
			DwpStageAttribute stageProperty = (DwpStageAttribute) attrs.elementAt(0);
			TestCase.assertEquals("Stage theatre MyGlobe",stageProperty.toString());
			TestCase.assertEquals("theatre MyGlobe",stageProperty.getStringValue());

		}		
	}
	
	// Load the required DLL.
	static
	{
		System.loadLibrary("DwpReader");
	}

}
