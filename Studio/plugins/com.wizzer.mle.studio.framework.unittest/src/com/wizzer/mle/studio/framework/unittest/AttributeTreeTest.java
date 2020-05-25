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
package com.wizzer.mle.studio.framework.unittest;

// Import standard Java packages.
import java.util.Observer;
import java.util.Observable;

// Import JUnit packages.
import junit.framework.TestCase;

// Import Eclipse packates.
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.FillLayout;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.domain.Table;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.StringAttribute;
import com.wizzer.mle.studio.framework.attribute.NumberAttribute;
import com.wizzer.mle.studio.framework.attribute.ChoiceAttribute;
import com.wizzer.mle.studio.framework.attribute.FileAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;
import com.wizzer.mle.studio.framework.ui.AttributeTreeViewer;
import com.wizzer.mle.studio.framework.ui.GuiConstants;

/**
 * This class is a unit test for com.wizzer.mle.studio.framework.ui.AttributeTreeViewer.
 *
 * @author Mark S. Millard
 */
public class AttributeTreeTest extends TestCase
{
	/**
	 * Constructor for AttributeTreeTest.
	 * 
	 * @param name
	 */
	public AttributeTreeTest(String name)
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
		junit.textui.TestRunner.run(AttributeTreeTest.class);
	}
	

	/**
	 * Set up the test case.
	 * 
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Tear down the test case.
	 * 
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
	
	/**
	 * Test the <code>AttributeTreeViewer</code>.
	 */
	public void testAttributeTree()
	{
		class MyTable extends Table
		{
			class MyTreeNodeAttribute extends VariableListAttribute implements Observer
			{
				MyTreeNodeAttribute(String menuName, NumberAttribute countAttribute, String functionName, Object theCaller)
				{ super(menuName,countAttribute,functionName,null,theCaller); }

				public void update(Observable o, Object arg) {} // Do nothing for now.
			}
    	
			class MyStringAttribute extends StringAttribute implements Observer
			{
				MyStringAttribute(String name, String value, int characters, boolean isReadOnly)
				{ super(name,value,characters,isReadOnly); }

				public void update(Observable o, Object arg) {} // Do nothing for now.
			}
    	
			class MyNumberAttribute extends NumberAttribute implements Observer
			{
				MyNumberAttribute(String name, long value, int bits, int radix, boolean isReadOnly)
				{ super(name,value,bits,radix,isReadOnly); }

				public void update(Observable o, Object arg) {} // Do nothing for now.
			}
    	
			class MyChoiceAttribute extends ChoiceAttribute implements Observer
			{
				MyChoiceAttribute(String name, Object[] choices, int index, int bits, boolean isReadOnly)
				{ super(name,choices,index,bits,isReadOnly); }

				public void update(Observable o, Object arg) {} // Do nothing for now.
			}
    	
			class MyFileAttribute extends FileAttribute implements Observer
			{
				MyFileAttribute(String name, String value, int characters, boolean isReadOnly)
				{ super(name,value,characters,isReadOnly); }

				public void update(Observable o, Object arg) {} // Do nothing for now.
			}
			
			class MyAttributeFactory
			{
				final public Attribute stringSubtreeAttributeTreeModel(MyTreeNodeAttribute parent)
				{
					System.out.println("In String Subtree Attribute Factory.");
					String name = new String("NewStringAttribute");
					String value = new String("A new string attribute.");
					MyStringAttribute attr = new MyStringAttribute(name,value,value.length(),false);
					return attr;
				}
				
				public Attribute numberSubtreeAttributeTreeModel(MyTreeNodeAttribute parent)
				{
					return null;
				}
				
				public Attribute choiceSubtreeAttributeTreeModel(MyTreeNodeAttribute parent)
				{
					return null;
				}
				
				public Attribute fileSubtreeAttributeTreeModel(MyTreeNodeAttribute parent)
				{
					return null;
				}
			}
			
			// The factory for creating new attributes.
			MyAttributeFactory m_attrFactory = null;

			// The default constructor.
			MyTable()
			{
				super();
			}
            
			protected Attribute buildDefaultInstance()
			{
				// Initialize the attribute factory.
				if (m_attrFactory == null)
					m_attrFactory = new MyAttributeFactory();
								
				// Create some Attributes.
				MyTreeNodeAttribute root = new MyTreeNodeAttribute("Root",
					new NumberAttribute("Count",6,32,10,false),"rootAttributeTreeModel",m_attrFactory);
				
				// Create a tree node full of String Attributes.
				MyTreeNodeAttribute stringSubTree = new MyTreeNodeAttribute("StringSubTree",
					new NumberAttribute("Count",4,32,10,false),"stringSubtreeAttributeTreeModel",m_attrFactory);
				for (int i = 0; i < 4; i++) {
					String name = new String("StringAttribute_" + i);
					String value = new String("Value_" + i);
					MyStringAttribute attr = new MyStringAttribute(name,value,value.length(),false);
					stringSubTree.addChild(attr,attr);
				}
				root.addChild(stringSubTree,stringSubTree);
				
				// Create a tree node full of Number Attributes.
				MyTreeNodeAttribute numberSubTree = new MyTreeNodeAttribute("NumberSubTree",
					new NumberAttribute("Count",10,32,10,false),"numberSubtreeAttributeTreeModel",m_attrFactory);
				for (int i = 0; i < 10; i++) {
					String name = new String("NumberAttribute_" + i);
					long value = i;
					MyNumberAttribute attr = new MyNumberAttribute(name,value,32,10,false);
					numberSubTree.addChild(attr,attr);
				}
				root.addChild(numberSubTree,numberSubTree);
				
				// Create a tree nopde full of Choice Attributes.
				MyTreeNodeAttribute choiceSubTree = new MyTreeNodeAttribute("ChoiceSubTree",
					new NumberAttribute("Count",5,32,10,false),"choiceSubtreeAttributeTreeModel",m_attrFactory);
				for (int i = 0; i < 5; i++) {
					String name = new String("ChoiceAttribute_" + i);
					String[] value = new String[7];
					for (int k = 0; k < 7; k++)
						value[k] = new String("Choice_" + k);
					MyChoiceAttribute attr = new MyChoiceAttribute(name,value,i,value[i].length(),false);
					choiceSubTree.addChild(attr,attr);
				}
				root.addChild(choiceSubTree,choiceSubTree);
				
				// Create a tree node full of File Attributes.
				MyTreeNodeAttribute fileSubTree = new MyTreeNodeAttribute("FileSubTree",
					new NumberAttribute("Count",2,32,10,false),"fileSubtreeAttributeTreeModel",m_attrFactory);
				for (int i = 0; i < 2; i++) {
					String name = new String("FileAttribute_" + i);
					String value = new String("File_" + i);
					MyFileAttribute attr = new MyFileAttribute(name,value,value.length(),false);
					fileSubTree.addChild(attr,attr);
				}
				root.addChild(fileSubTree,fileSubTree);
				
				// Create some read-only attributes.
				for (int i = 0; i < 5; i++) {
					String name = new String("ReadOnlyAttribute_" + i);
					String value = new String("Value_" + i);
					MyStringAttribute attr = new MyStringAttribute(name,value,value.length(),true);
					root.addChild(attr,attr);
				}
				
				// Set the top of tree and return it.
				m_top = root;
				return root;
			}
			
			public void update(Observable o, Object arg) {} // Do nothing for now.
		}
		
		Shell shell = new Shell();
		shell.setText("AttributeTreeViewer Unit Test");

		// Set layout for shell.
		shell.setLayout(new FillLayout());
		
		// Create a domain table.
		MyTable domainTable = new MyTable();

		GuiConstants.setConstants(13,2,200,600,12);
		
		// Create a composite to hold the children.
		final AttributeTreeViewer attrTreeUnitTest = new AttributeTreeViewer(shell,SWT.NONE,domainTable);
		
		attrTreeUnitTest.getControl().addDisposeListener(new DisposeListener()
			{
				public void widgetDisposed(DisposeEvent e) {
					attrTreeUnitTest.dispose();			
				}
			
			});

		// Ask the shell to display its content.
		shell.open();
		shell.pack();
		
		// Dispatch events.
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

}
