/*
 * DwpPropertyTest.java
 * Created on Jul 6, 2004
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
import com.wizzer.mle.studio.dwp.attribute.DwpIntPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpFloatPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpStringPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpScalarPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpVector2PropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpVector3PropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpVector4PropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpIntArrayPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpFloatArrayPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpRotationPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpTransformPropertyAttribute;
import com.wizzer.mle.studio.dwp.domain.DwpTable;


/**
 * This class is a unit test for the Magic Lantern Digital Workprint properties.
 * 
 * @author Mark S. Millard
 */
public class DwpPropertyTest extends TestCase
{
	static private String DIGITAL_WORKPRINT_TESTFILE = "test.dwp";
	static private FileOutputStream m_file = null;
	static private StringBuffer m_buffer = null;

	/**
	 * Constructor for DwpPropertyTest.
	 * 
	 * @param name The name of the unit test.
	 */
	public DwpPropertyTest(String name)
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
		junit.textui.TestRunner.run(DwpPropertyTest.class);
	}

	/**
	 * Set up the test case.
	 * 
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		System.out.println("Setting up test case for DWP properties.");
		super.setUp();
		
		// Create the file.
		m_file = new FileOutputStream(DIGITAL_WORKPRINT_TESTFILE);
		if (m_file != null)
		{
			// Construct a valid Digital Workprint.
			m_buffer = new StringBuffer();
			m_buffer.append("#DWP 1.0 ascii\n");
			m_buffer.append("( ActorDef MyActor\n");
			m_buffer.append("\t( PropertyDef name string )\n");
			m_buffer.append("\t( PropertyDef age int )\n");
			m_buffer.append("\t( PropertyDef weight float )\n");
			m_buffer.append("\t( PropertyDef height MlScalar )\n");
			m_buffer.append("\t( PropertyDef eyesite MlVector2 )\n");
			m_buffer.append("\t( PropertyDef position MlVector3 )\n");
			m_buffer.append("\t( PropertyDef matrix MlVector4 )\n");
			m_buffer.append("\t( PropertyDef count IntArray )\n");
			m_buffer.append("\t( PropertyDef speed FloatArray )\n");
			m_buffer.append("\t( PropertyDef sitOnItAnd MlRotation )\n");
			m_buffer.append("\t( PropertyDef werewolf MlTransform )\n");
			m_buffer.append(")\n");
			m_buffer.append("( Actor actor0 MyActor\n");
			m_buffer.append("\t( Property name string \"Mark S. Millard\" )\n");
			m_buffer.append("\t( Property age int 43 )\n");
			m_buffer.append("\t( Property weight float 192.6 )\n");
			m_buffer.append("\t( Property height MlScalar 70.5 )\n");
			m_buffer.append("\t( Property eyesite MlVector2 20 20 )\n");
			m_buffer.append("\t( Property position MlVector3 0 123.75 99.999 )\n");
			m_buffer.append("\t( Property matrix MlVector4 1 2 3 4 )\n");
			m_buffer.append("\t( Property count IntArray [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ] )\n");
			m_buffer.append("\t( Property speed FloatArray [ 101, 202.75, 30.87, 403.456, 505, 666, 76.8 ] )\n");
			m_buffer.append("\t( Property sitOnItAnd MlRotation 1 2 3 4 )\n");
			m_buffer.append("\t( Property werewolf MlTransform 1 2 3 11 12 13 21 22 23 31 32 33 )\n");
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
		System.out.println("Tearing down test case for DWP properties.");
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

			// Look for Property items.
			Vector attrs = iter.findAttributes("Property",table.getTopAttribute());
			TestCase.assertEquals(11,attrs.size());
			DwpStringPropertyAttribute nameProperty = (DwpStringPropertyAttribute) attrs.elementAt(0);
			TestCase.assertEquals("Property name string \"Mark S. Millard\"",nameProperty.toString());
			TestCase.assertEquals("name string \"Mark S. Millard\"",nameProperty.getStringValue());
			DwpIntPropertyAttribute ageProperty = (DwpIntPropertyAttribute) attrs.elementAt(1);
			TestCase.assertEquals("Property age int 43",ageProperty.toString());
			TestCase.assertEquals("age int 43",ageProperty.getStringValue());
			DwpFloatPropertyAttribute weightProperty = (DwpFloatPropertyAttribute) attrs.elementAt(2);
			TestCase.assertEquals("Property weight float 192.6",weightProperty.toString());
			TestCase.assertEquals("weight float 192.6",weightProperty.getStringValue());
			DwpScalarPropertyAttribute heightProperty = (DwpScalarPropertyAttribute) attrs.elementAt(3);
			TestCase.assertEquals("Property height MlScalar 70.5",heightProperty.toString());
			TestCase.assertEquals("height MlScalar 70.5",heightProperty.getStringValue());
			DwpVector2PropertyAttribute eyesiteProperty = (DwpVector2PropertyAttribute) attrs.elementAt(4);
			TestCase.assertEquals("Property eyesite MlVector2 20.0 20.0",eyesiteProperty.toString());
			TestCase.assertEquals("eyesite MlVector2 20.0 20.0",eyesiteProperty.getStringValue());
			DwpVector3PropertyAttribute positionProperty = (DwpVector3PropertyAttribute) attrs.elementAt(5);
			TestCase.assertEquals("Property position MlVector3 0.0 123.75 99.999",positionProperty.toString());
			TestCase.assertEquals("position MlVector3 0.0 123.75 99.999",positionProperty.getStringValue());
			DwpVector4PropertyAttribute matrixProperty = (DwpVector4PropertyAttribute) attrs.elementAt(6);
			TestCase.assertEquals("Property matrix MlVector4 1.0 2.0 3.0 4.0",matrixProperty.toString());
			TestCase.assertEquals("matrix MlVector4 1.0 2.0 3.0 4.0",matrixProperty.getStringValue());
			DwpIntArrayPropertyAttribute countProperty = (DwpIntArrayPropertyAttribute) attrs.elementAt(7);
			TestCase.assertEquals("Property count IntArray [ 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 ]",countProperty.toString());
			TestCase.assertEquals("count IntArray [ 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 ]",countProperty.getStringValue());
			DwpFloatArrayPropertyAttribute speedProperty = (DwpFloatArrayPropertyAttribute) attrs.elementAt(8);
			TestCase.assertEquals("Property speed FloatArray [ 101.0 , 202.75 , 30.87 , 403.456 , 505.0 , 666.0 , 76.8 ]",speedProperty.toString());
			TestCase.assertEquals("speed FloatArray [ 101.0 , 202.75 , 30.87 , 403.456 , 505.0 , 666.0 , 76.8 ]",speedProperty.getStringValue());
			DwpRotationPropertyAttribute rotateProperty = (DwpRotationPropertyAttribute) attrs.elementAt(9);
			TestCase.assertEquals("Property sitOnItAnd MlRotation 1.0 2.0 3.0 4.0",rotateProperty.toString());
			TestCase.assertEquals("sitOnItAnd MlRotation 1.0 2.0 3.0 4.0",rotateProperty.getStringValue());
			DwpTransformPropertyAttribute transformProperty = (DwpTransformPropertyAttribute) attrs.elementAt(10);
			TestCase.assertEquals("Property werewolf MlTransform 1.0 2.0 3.0 11.0 12.0 13.0 21.0 22.0 23.0 31.0 32.0 33.0",transformProperty.toString());
			TestCase.assertEquals("werewolf MlTransform 1.0 2.0 3.0 11.0 12.0 13.0 21.0 22.0 23.0 31.0 32.0 33.0",transformProperty.getStringValue());
		}
	}
	
	// Load the required DLL.
	static
	{
		System.loadLibrary("DwpReader");
	}

}
