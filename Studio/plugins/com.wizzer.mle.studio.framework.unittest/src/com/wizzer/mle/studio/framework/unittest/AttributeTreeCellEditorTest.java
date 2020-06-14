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
import org.eclipse.swt.layout.*;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

// Import Magic Lantern Framework packages.
import com.wizzer.mle.studio.framework.attribute.StringAttribute;
import com.wizzer.mle.studio.framework.attribute.NumberAttribute;
import com.wizzer.mle.studio.framework.attribute.ChoiceAttribute;
import com.wizzer.mle.studio.framework.attribute.FileAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;
import com.wizzer.mle.studio.framework.ui.AttributeTreeCellEditor;
import com.wizzer.mle.studio.framework.ui.GuiConstants;

/**
 * This class is a unit test for com.wizzer.mle.studio.framework.ui.AttributeTreeCellEditor.
 * 
 * @author Mark S. Millard
 */
public class AttributeTreeCellEditorTest extends TestCase
{
	/**
	 * Constructor for AttributeTreeCellEditorTest.
	 * @param name
	 */
	public AttributeTreeCellEditorTest(String name)
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
		junit.textui.TestRunner.run(AttributeTreeCellEditorTest.class);
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
	 * Test the <code>AttributeTreeCellEditor</code>.
	 */
	public void testAttributeTreeCellEditor()
	{
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
    	
		class AttributeEditor extends ApplicationWindow
		{
			Combo m_selectEditor;             // A Combo for selecting the current Cell Editor.
			AttributeTreeCellEditor m_editor; // The Attribute Cell Editor.
			VariableListAttribute m_root;     // The list of attributes.
			Composite m_composite;            // A holder for children widgets.
								
			AttributeEditor() { super(null); }
        	
			protected Control createContents(Composite parent)
			{
				getShell().setText("AttributeTreeCellEditor Unit Test");
				int minWidth = 480;
				int minHeight = 175;
				getShell().setMinimumSize(minWidth, minHeight);
		
				// Create some Attributes.
				int numAttributes = 4;
				m_root = new VariableListAttribute("Test",
					new NumberAttribute("Count",numAttributes,32,10,false),"testAttributeTreeCellEditor",
					null, null);
					
				String strValue = new String("String Value");
				MyStringAttribute strAttr = new MyStringAttribute(new String("String Attribute"),
					strValue,strValue.length(),false);
				m_root.addChild(strAttr,strAttr);
				
				MyNumberAttribute numAttr = new MyNumberAttribute(new String("Number Attribute"),
					100,32,10,false);
				m_root.addChild(numAttr,numAttr);
				
				String[] choiceValue = new String[3];
				for (int k = 0; k < 3; k++)
					choiceValue[k] = new String("Choice_" + k);
				MyChoiceAttribute choiceAttr = new MyChoiceAttribute(new String("Choice Attribute"),
					choiceValue,1,choiceValue[1].length(),false);
				m_root.addChild(choiceAttr,choiceAttr);

				String fileValue = new String("Readme.txt");
				MyFileAttribute fileAttr = new MyFileAttribute(new String("File Attribute"),
					fileValue,fileValue.length(),false);
				m_root.addChild(fileAttr,fileAttr);

				// Create a parent composite for easier layout.
				m_composite = new Composite(parent,SWT.RESIZE);
                
				GridLayout layout = new GridLayout();
				layout.numColumns = 1;
				m_composite.setLayout(layout);
                
				// Add a button to switch between editors.
				m_selectEditor = new Combo(m_composite,SWT.DROP_DOWN);
				m_selectEditor.add("String Attribute");
				m_selectEditor.add("Number Attribute");
				m_selectEditor.add("Choice Attribute");
				m_selectEditor.add("File Attribute");
				m_selectEditor.select(0);
				m_selectEditor.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e)
					{
						int index = m_selectEditor.getSelectionIndex();
						
						// Clean up the old editor.
						m_editor.deactivate();
						m_editor.dispose();
												
						// Setup and activate the new editor.
						AttributeTreeCellEditor.setAttributeType(m_root.getChild(index));
						m_editor = new AttributeTreeCellEditor(m_composite,SWT.RESIZE);
						m_editor.setAttribute(m_root.getChild(index));
						m_editor.activate();
						
						// Force layout.
						m_composite.layout();
						m_composite.getShell().pack(true);						
					}
				});
				
				GridData selectEditorData = new GridData();
				selectEditorData.widthHint = 100;
				m_selectEditor.setLayoutData(selectEditorData);

				// Create the Attribute tree Cell Editor.
				AttributeTreeCellEditor.setAttributeType(m_root.getChild(0));
				m_editor = new AttributeTreeCellEditor(m_composite,SWT.RESIZE);
				m_editor.setAttribute(m_root.getChild(0));
				m_editor.activate();
				 				
				return m_editor.getControl();
			}
		}
        		
		AttributeEditor editor = new AttributeEditor();
		GuiConstants.setConstants(13,2,125,550,12);
		editor.setBlockOnOpen(true);
		editor.open();
		Display.getCurrent().dispose();
	}
	
}
