/*
 * DwpWriterTest.java
 * Created on Aug 9, 2004
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

// Import JUnit packages.
import junit.framework.TestCase;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;

// Import Magic Lantern Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpWriter;
import com.wizzer.mle.studio.dwp.DwpException;
import com.wizzer.mle.studio.dwp.domain.DwpTable;
import com.wizzer.mle.studio.dwp.attribute.DwpSetAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpIncludeAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpActorDefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpDSOFileAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpHeaderFileAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSourceFileAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpPropertyDefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpRoleBindingAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpGroupAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpActorAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpIntPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpRoleAttachmentAttribute;


/**
 * This class is a unit test for com.wizzer.mle.studio.dwp.DwpWriter.
 * 
 * @author Mark S. Millard
 */
public class DwpWriterTest extends TestCase
{
	// The DWP table that is being tested.
	DwpTestTable m_table = null;

	// This class is used to build a DWP Table to test against.
	private class DwpTestTable extends DwpTable
	{
		/**
		 * Build the default instance of the Digital Workprint Test Table.
		 * 
		 * @return The root of the <code>Attribute</code> tree is returned.
		 * 
		 * @see com.wizzer.mle.studio.framework.domain.Table#buildDefaultInstance()
		 */
		protected Attribute buildDefaultInstance()
		{
			Attribute root = super.buildDefaultInstance();
			
			m_elements.addChild(new DwpSetAttribute("set0","MlBasicRenderSet",false),this);
			m_elements.addChild(new DwpIncludeAttribute("asdf.dwp",false),this);
			DwpActorDefAttribute actorDef = new DwpActorDefAttribute("MyActor",false);
			m_elements.addChild(actorDef,this);
			actorDef.addChild(new DwpDSOFileAttribute("MyActor.so",false),this);
			actorDef.addChild(new DwpHeaderFileAttribute("MyActor.h",false),this);
			actorDef.addChild(new DwpSourceFileAttribute("MyActor.cxx",false),this);
			actorDef.addChild(new DwpPropertyDefAttribute("x","int",false),this);
			actorDef.addChild(new DwpPropertyDefAttribute("y","int",false),this);
			actorDef.addChild(new DwpPropertyDefAttribute("z","int",false),this);
			actorDef.addChild(new DwpRoleBindingAttribute("BRenderRole","NoSet",false),this);
			DwpGroupAttribute group = new DwpGroupAttribute("g0","MlGroup",false);
			m_elements.addChild(group,this);
			group.addChild(new DwpHeaderFileAttribute("mle/group.h",false),this);
			DwpActorAttribute actor0 = new DwpActorAttribute("actor0","MyActor",false);
			group.addChild(actor0,this);
			actor0.addChild(new DwpRoleBindingAttribute("BRenderRole","set0",false),this);
			actor0.addChild(new DwpIntPropertyAttribute("x","int",new Integer(10),false),this);
			DwpActorAttribute actor1 = new DwpActorAttribute("actor1","MyActor",false);
			group.addChild(actor1,this);
			actor1.addChild(new DwpIntPropertyAttribute("x","int",new Integer(4),false),this);
			actor1.addChild(new DwpRoleBindingAttribute("BRenderRole","set0",false),this);
			group.addChild(new DwpRoleAttachmentAttribute("actor0","actor1",false),this);
			
			return root;
		}
	}
	
	/**
	 * Constructor for DwpWriterTest.
	 * 
	 * @param name The name of the unit test.
	 */
	public DwpWriterTest(String name)
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
		junit.textui.TestRunner.run(DwpWriterTest.class);
	}

	/**
	 * Set up the test case.
	 * 
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		System.out.println("Setting up test case for DwpWriter.");
		super.setUp();
		
		if (m_table == null)
		    m_table = new DwpTestTable();
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
	}

	/**
	 * Test the write() method.
	 */
	public void testWrite()
	{
		byte[] data = null;

		// Test for success.
		DwpWriter writer = new DwpWriter(m_table);
		try
		{
		   data = writer.write();
		} catch (DwpException ex) {}
		TestCase.assertNotNull(data);
		
		dump(data);
	}
	
	/**
	 * Dump the specifed data to system out.
	 * 
	 * @param data The data to dump.
	 */
	protected void dump(byte[] data)
	{
		String output = new String(data);
		System.out.println(output);
	}
}
