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
import org.eclipse.jface.window.ApplicationWindow;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.ChoiceAttribute;
import com.wizzer.mle.studio.framework.attribute.NumberAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;
import com.wizzer.mle.studio.framework.ui.ChoiceCellEditor;
import com.wizzer.mle.studio.framework.ui.GuiConstants;

/**
 * This class is a unit test for com.wizzer.mle.studio.framework.ui.ChoiceCellEditor.
 * 
 * @author Mark S. Millard
 */
public class ChoiceCellEditorTest extends TestCase
{

	/**
	 * Constructor for ChoiceCellEditorTest.
	 * 
	 * @param name
	 */
	public ChoiceCellEditorTest(String name)
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
		junit.textui.TestRunner.run(ChoiceCellEditorTest.class);
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
	 * Test the <code>ChoiceCellEditor</code>.
	 */
	public void testChoiceCellEditor()
	{
		class MyChoiceAttribute extends ChoiceAttribute implements Observer
		{
			MyChoiceAttribute(String name, Object[] choices, int index, int bits, boolean isReadOnly)
			{ super(name,choices,index,bits,isReadOnly); }

			public void update(Observable o, Object arg) {} // Do nothing for now.
		}
    	
		class AttributeEditor extends ApplicationWindow
		{
			AttributeEditor() { super(null); }
        	
			protected Control createContents(Composite parent)
			{
				getShell().setText("ChoiceCellEditor Unit Test");
		
				// Create some Attributes.
				int numAttributes = 5;
				VariableListAttribute root = new VariableListAttribute("Test",
					new NumberAttribute("Count",numAttributes,32,10,false),"testChoiceCellEditor",
					null,null);
				for (int i = 0; i < numAttributes; i++) {
					String name = new String("Attribute_" + i);
					String[] value = new String[3];
					for (int k = 0; k < 3; k++)
						value[k] = new String("Choice_" + k);
					MyChoiceAttribute attr = new MyChoiceAttribute(name,value,1,value[1].length(),false);
					root.addChild(attr,attr);
				}

				// Create the choice Cell Editor
				ChoiceCellEditor editor = new ChoiceCellEditor(parent,SWT.NONE);
				editor.setAttribute((ChoiceAttribute)root.getChild(0));
				editor.hideLabel();
				editor.activate();
 				
				//editor.getControl().pack();
 
				return editor.getControl();
			}
		}
        		
		AttributeEditor editor = new AttributeEditor();
		GuiConstants.setConstants(13,2,100,600,12);
		editor.setBlockOnOpen(true);
		editor.open();
		Display.getCurrent().dispose();
	}
}
