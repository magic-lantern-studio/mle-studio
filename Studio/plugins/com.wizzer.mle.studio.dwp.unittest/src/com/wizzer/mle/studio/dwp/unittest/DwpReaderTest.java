/*
 * @file DwpReaderTest.java
 * Created on May 12, 2004
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

// Declare packages.
package com.wizzer.mle.studio.dwp.unittest;

// Import standard Java packages.
import java.io.FileOutputStream;
import java.util.Vector;

// Import JUnit packages.
import junit.framework.TestCase;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.domain.TableIterator;

// Import Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpReader;
import com.wizzer.mle.studio.dwp.attribute.DwpIncludeAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpDSOFileAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpHeaderFileAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSourceFileAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpActorDefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpPropertyDefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSetAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpGroupAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpActorAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpRoleBindingAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpRoleAttachmentAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpIntPropertyAttribute;
import com.wizzer.mle.studio.dwp.domain.DwpTable;


/**
 * This class is a unit test for com.wizzer.mle.studio.dwp.DwpReader.
 * 
 * @author Mark S. Millard
 */
public class DwpReaderTest extends TestCase
{
	static private String DIGITAL_WORKPRINT_TESTFILE = "test.dwp";
	static private FileOutputStream m_file = null;
	static private StringBuffer m_buffer = null;

	/**
	 * Constructor for DwpReaderTest.
	 * 
	 * @param name The name of the unit test.
	 */
	public DwpReaderTest(String name)
	{
		super(name);
	}

	/**
	 * Executes the test runner.
	 * 
	 * @param args Command line arguements.
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(DwpReaderTest.class);
	}

	/**
	 * Set up the test case.
	 * 
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		System.out.println("Setting up test case for DwpReader.");
		super.setUp();
		
		// Create the file.
		m_file = new FileOutputStream(DIGITAL_WORKPRINT_TESTFILE);
		if (m_file != null)
		{
			// Construct a valid Digital Workprint.
			m_buffer = new StringBuffer();
			m_buffer.append("#DWP 1.0 ascii\n");
			m_buffer.append("( Set set0 MlBasicRenderSet )\n");
			m_buffer.append("( Include asdf.dwp )\n");
			m_buffer.append("( ActorDef MyActor\n");
			m_buffer.append("\t( DSOFile MyActor.so )\n");
			m_buffer.append("\t( HeaderFile MyActor.h )\n");
			m_buffer.append("\t( SourceFile MyActor.cxx )\n");
			m_buffer.append("\t( PropertyDef x int )\n");
			m_buffer.append("\t( PropertyDef y int )\n");
			m_buffer.append("\t( PropertyDef z int )\n");
			m_buffer.append("\t( RoleBinding BRenderRole NoSet )\n");
			m_buffer.append(")\n");
			m_buffer.append("( Group g0 MlGroup\n");
			m_buffer.append("\t( HeaderFile mle/group.h )\n");
			m_buffer.append("\t( Actor actor0 MyActor\n");
			m_buffer.append("\t\t( RoleBinding BRenderRole set0 )\n");
			m_buffer.append("\t\t( Property x int 10 )\n");
			m_buffer.append("\t)\n");
			m_buffer.append("\t( Actor actor1 MyActor\n");
			m_buffer.append("\t\t( Property x int 4 )\n");
			m_buffer.append("\t\t( RoleBinding BRenderRole set0 )\n");
			m_buffer.append("\t)\n");
			m_buffer.append("\t( RoleAttachment actor0 actor1 )\n");
			m_buffer.append("\t( bleah more random stuff\n");
			m_buffer.append("\t\t  and a second line\n");
			m_buffer.append("\t\t)\n");
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
		System.out.println("Tearing down test case for DwpReader.");
		super.tearDown();
		
		// Close the file.
		m_file.close();
	}

	/**
	 * Test the constructor.
	 */
	 public void testDwpReader()
	{
		DwpReader reader = new DwpReader(DIGITAL_WORKPRINT_TESTFILE);
		String filename = reader.getFilename();
		if (filename.equals(DIGITAL_WORKPRINT_TESTFILE))
		{
			TestCase.assertEquals(true,true);
		} else
		{
			TestCase.assertEquals(true,false);
		}
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
			
			// Look for the Include item.
			DwpIncludeAttribute includeAttr = (DwpIncludeAttribute) iter.findAttribute("Include",table.getTopAttribute());
			TestCase.assertEquals("Include asdf.dwp", includeAttr.toString());
			TestCase.assertEquals("asdf.dwp", includeAttr.getStringValue());
			
			// Look for HeaderFile items.
			Vector attrs = iter.findAttributes("HeaderFile",table.getTopAttribute());
			TestCase.assertEquals(2,attrs.size());
			DwpHeaderFileAttribute headerFileAttr = (DwpHeaderFileAttribute) attrs.elementAt(0);
			TestCase.assertEquals("HeaderFile MyActor.h", headerFileAttr.toString());
			TestCase.assertEquals("MyActor.h", headerFileAttr.getStringValue());
			headerFileAttr = (DwpHeaderFileAttribute) attrs.elementAt(1);
			TestCase.assertEquals("HeaderFile mle/group.h", headerFileAttr.toString());
			TestCase.assertEquals("mle/group.h", headerFileAttr.getStringValue());
			
			// Look for the SourceFile item.
			DwpSourceFileAttribute sourceFileAttr = (DwpSourceFileAttribute) iter.findAttribute("SourceFile",table.getTopAttribute());
			TestCase.assertEquals("SourceFile MyActor.cxx", sourceFileAttr.toString());
			TestCase.assertEquals("MyActor.cxx", sourceFileAttr.getStringValue());
			
			// Look for the DSOFile item.
			DwpDSOFileAttribute dsoFileAttr = (DwpDSOFileAttribute) iter.findAttribute("DSOFile",table.getTopAttribute());
			TestCase.assertEquals("DSOFile MyActor.so", dsoFileAttr.toString());
			TestCase.assertEquals("MyActor.so", dsoFileAttr.getStringValue());

			// Look for the ActorDef item.
			DwpActorDefAttribute actorDefAttr = (DwpActorDefAttribute) iter.findAttribute("ActorDef",table.getTopAttribute());
			TestCase.assertEquals("ActorDef MyActor", actorDefAttr.toString());
			TestCase.assertEquals("MyActor", actorDefAttr.getStringValue());
			
			// Look for the Forum/Set item.
			DwpSetAttribute setAttr = (DwpSetAttribute) iter.findAttribute("Set",table.getTopAttribute());
			TestCase.assertEquals("Set set0 MlBasicRenderSet", setAttr.toString());
			TestCase.assertEquals("set0 MlBasicRenderSet", setAttr.getStringValue());

			// Look for DelegateBinding/RoleBinding items
			attrs = iter.findAttributes("RoleBinding",table.getTopAttribute());
			TestCase.assertEquals(3,attrs.size());
			DwpRoleBindingAttribute roleBindingAttr = (DwpRoleBindingAttribute) attrs.elementAt(0);
			TestCase.assertEquals("RoleBinding BRenderRole NoSet",roleBindingAttr.toString());
			TestCase.assertEquals("BRenderRole NoSet",roleBindingAttr.getStringValue());
			roleBindingAttr = (DwpRoleBindingAttribute) attrs.elementAt(1);
			TestCase.assertEquals("RoleBinding BRenderRole set0",roleBindingAttr.toString());
			TestCase.assertEquals("BRenderRole set0",roleBindingAttr.getStringValue());
			roleBindingAttr = (DwpRoleBindingAttribute) attrs.elementAt(2);
			TestCase.assertEquals("RoleBinding BRenderRole set0",roleBindingAttr.toString());
			TestCase.assertEquals("BRenderRole set0",roleBindingAttr.getStringValue());

			// Look for Actor items.
			attrs = iter.findAttributes("Actor",table.getTopAttribute());
			TestCase.assertEquals(2,attrs.size());
			DwpActorAttribute actorAttr = (DwpActorAttribute) attrs.elementAt(0);
			TestCase.assertEquals("Actor actor0 MyActor",actorAttr.toString());
			TestCase.assertEquals("actor0 MyActor",actorAttr.getStringValue());
			actorAttr = (DwpActorAttribute) attrs.elementAt(1);
			TestCase.assertEquals("Actor actor1 MyActor",actorAttr.toString());
			TestCase.assertEquals("actor1 MyActor",actorAttr.getStringValue());

			// Look for the Group item.
			DwpGroupAttribute groupAttr = (DwpGroupAttribute) iter.findAttribute("Group",table.getTopAttribute());
			TestCase.assertEquals("Group g0 MlGroup", groupAttr.toString());
			TestCase.assertEquals("g0 MlGroup", groupAttr.getStringValue());

			// Look for DelegateAttachment/RoleAttachment item.
			DwpRoleAttachmentAttribute roleAttachmentAttr = (DwpRoleAttachmentAttribute) iter.findAttribute("RoleAttachment",table.getTopAttribute());
			TestCase.assertEquals("RoleAttachment actor0 actor1", roleAttachmentAttr.toString());
			TestCase.assertEquals("actor0 actor1", roleAttachmentAttr.getStringValue());
			
			// Look for PropertyDef items.
			attrs = iter.findAttributes("PropertyDef",table.getTopAttribute());
			TestCase.assertEquals(3,attrs.size());
			DwpPropertyDefAttribute propertyDefAttr = (DwpPropertyDefAttribute) attrs.elementAt(0);
			TestCase.assertEquals("PropertyDef x int",propertyDefAttr.toString());
			TestCase.assertEquals("x int",propertyDefAttr.getStringValue());
			propertyDefAttr = (DwpPropertyDefAttribute) attrs.elementAt(1);
			TestCase.assertEquals("PropertyDef y int",propertyDefAttr.toString());
			TestCase.assertEquals("y int",propertyDefAttr.getStringValue());
			propertyDefAttr = (DwpPropertyDefAttribute) attrs.elementAt(2);
			TestCase.assertEquals("PropertyDef z int",propertyDefAttr.toString());
			TestCase.assertEquals("z int",propertyDefAttr.getStringValue());
			
			// Look for Property items.
			attrs = iter.findAttributes("Property",table.getTopAttribute());
			TestCase.assertEquals(2,attrs.size());
			DwpIntPropertyAttribute propertyAttr = (DwpIntPropertyAttribute) attrs.elementAt(0);
			TestCase.assertEquals("Property x int 10",propertyAttr.toString());
			TestCase.assertEquals("x int 10",propertyAttr.getStringValue());
			propertyAttr = (DwpIntPropertyAttribute) attrs.elementAt(1);
			TestCase.assertEquals("Property x int 4",propertyAttr.toString());
			TestCase.assertEquals("x int 4",propertyAttr.getStringValue());

		}
	}

	/**
	 * Test the dump() method.
	 */
	public void testDump()
	{
		// Test for success.
		DwpReader reader = new DwpReader(DIGITAL_WORKPRINT_TESTFILE);
		boolean status = reader.dump();
		TestCase.assertEquals(true,status);
	}
	
	// Load the required DLL.
	static
	{
		System.loadLibrary("DwpReader");
	}

}
