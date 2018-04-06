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
import java.util.Observable;

// Import JUnit packages.
import junit.framework.TestCase;

// Import Eclipse packates.
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.FillLayout;

// Import Magic Lantern Framework packages.
import com.wizzer.mle.studio.framework.ui.PluginFrame;
import com.wizzer.mle.studio.framework.Message;
import com.wizzer.mle.studio.framework.MessageQueue;
import com.wizzer.mle.studio.framework.MessageProtocol;
import com.wizzer.mle.studio.framework.ui.GuiUtilities;
import com.wizzer.mle.studio.framework.ui.Console;

/**
 * This class is a unit test for com.wizzer.mle.studio.framework.ui.PluginFrame.
 * 
 * @author Mark S. Millard
 */
public class PluginFrameTest extends TestCase
{
	/**
	 * Constructor for PluginFrameTest.
	 * 
	 * @param name
	 */
	public PluginFrameTest(String name)
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
		junit.textui.TestRunner.run(PluginFrameTest.class);
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
	 * Test the <code>PluginFrame</code>.
	 */
	public void testPluginFrame()
	{
		class MyPluginFrame extends PluginFrame
		{
			MyPluginFrame(Composite parent,int style)
			{
				super(parent,style);

				m_console = new Console();
				m_pluginName = new String("PluginFrame Unit Test");
				m_console.setName(m_pluginName,false);
				// Enable timestamps on the console.
				m_console.enableTimestamp(true);
			}
			
			public void update(Observable obs,Object obj)
			{
				if (obs.getClass().getName().equals("com.wizzer.mle.studio.framework.MessageQueue"))
				{
					MessageQueue queue = (MessageQueue)obs;
					Message msg = (Message)obj;
				
					if (msg.getMessageType() == MessageProtocol.PLUGIN_STATUS_MSGTYPE)
					{
						//System.out.println("Status message sent.");
						Display display = Display.getDefault();
						display.wake();
					}
				}
			};
			
			public void addActions(Composite parent)
			{
				// Do nothing for now.
			}
			
			public boolean sendMessage(Message msg)
			{
				return m_messages.push(msg);
			}
			
			public void handleFileSaveAsAction(Event event) {};
			public void handleFileSaveAction(Event event) {};
			public void handleFileNewAction(Event event) {};
			public void handleFileOpenAction(Event event) { GuiUtilities.openFile(this.getShell());};
			public void handleFileDumpToConsoleAction(Event event) {};
			public void handleFileDumpToFileAction(Event event) {};
			public void handleNetworkSendAction(Event event) {};
			public void handleNetworkStopSendingAction(Event event) {};
			public void handleNetworkUpdateAction(Event event) {};
			public void handleViewConsoleAction(Event event) {};
		}
    	
		Display display = new Display();
		Shell shell = new Shell(display);
		
		shell.setLayout(new FillLayout());

		MyPluginFrame frame = new MyPluginFrame(shell, SWT.BORDER);
        
		// Put a status update in the Message Queue.
		byte[] data = new String("Testing status update.").getBytes();
		Message msg = new Message(MessageProtocol.PLUGIN_STATUS_MSGTYPE,(short)data.length,data);
		frame.sendMessage(msg);
        
		shell.setText("PluginFrame Unit Test");
		shell.pack();
		shell.open();
 
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch()) {
				display.sleep();
				frame.processMessages();
			}
		}

		display.dispose();	}

}
