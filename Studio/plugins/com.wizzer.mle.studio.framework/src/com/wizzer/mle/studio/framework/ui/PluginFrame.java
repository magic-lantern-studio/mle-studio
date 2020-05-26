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
package com.wizzer.mle.studio.framework.ui;

// Import standard Java packages.
import java.util.Observer;
import java.util.Properties;

// Import Eclipse packages.
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

// Import Wizzer Workds Framework packages.
import com.wizzer.mle.studio.framework.IMessageQueueProxy;
import com.wizzer.mle.studio.framework.Message;
import com.wizzer.mle.studio.framework.MessageProtocol;
import com.wizzer.mle.studio.framework.MessageQueue;
import com.wizzer.mle.studio.framework.ui.GuiUtilities;
import com.wizzer.mle.studio.framework.ui.IToolConsole;


/**
 * A Plugin Frame for the Wizzer Works Framework.
 * <p>
 * Every plugin should derive directly or indirectly from this class. It takes care of
 * providing a <b>Composite</b> cointrol for the plugin along with default menus
 * (File, Network, and View), file handling mechanisms, etc.
 * All of the default menus are initialized here, so
 * much of the work of each plug-in is avoided in descendants.
 * </p><p>
 * Because it is abstract, anyone implementing a concrete (instantiable) subclass
 * will be forced to provide implementations for certain methods, noted below.
 * 
 * @author Mark S. Millard
 */
public abstract class PluginFrame extends Composite implements Observer,IMessageQueueProxy
{
	static public int DEFAULT_WIDTH = 500;            // The default width of the PluginFrame.
	static public int DEFAULT_HEIGHT = 535;           // The height of the PluginFrame.
	static public int DEFAULT_TABFOLDER_WIDTH = 490;  // The default width of the TabFolder.
	static public int DEFAULT_TABFOLDER_HEIGHT = 490; // The default height of the TabFolder.

    protected IToolConsole m_console = null;       // XXX - Must be set by a sub-class implementation.
    protected String m_pluginName;
    protected boolean m_topLevelShell = false;
    protected MessageQueue m_messages = null;
    protected boolean m_isDirty = false;

    protected Label m_statusLabel                     = null;
    protected Label m_status                          = null;
    protected TabFolder m_tabFolder                   = null;
    protected Properties m_properties                 = new Properties();
    protected boolean m_dangerous                     = false;
    
    abstract public void addActions(Composite parent);

    abstract public void handleFileSaveAsAction(Event event);

    abstract public void handleFileSaveAction(Event event);

    abstract public void handleFileNewAction(Event event);

    abstract public void handleFileOpenAction(Event event);

    abstract public void handleFileDumpToConsoleAction(Event event);

    abstract public void handleFileDumpToFileAction(Event event);

	abstract public void handleViewConsoleAction(Event event);

    public PluginFrame(Composite parent,int style)
    {
        super(parent,style);
        
        // Initialize the GUI.
        initGUI(this);
        
        // Add a Message Queue for processing status.
        m_messages = new MessageQueue(64);
        m_messages.addObserver(this);
    }

    // Initialize the GUI layout.
    private void initGUI(Composite parent)
    {
		// Create a Layout manager.
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		parent.setLayout(gridLayout);
		        
        // Set the size of the PluginFrame Composite widget.
		parent.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		// Add the actions that will handle the menu items.
		addActions(parent);
        
		// Create a TabFolder to be used by the subclass.
		m_tabFolder = new TabFolder(parent,SWT.RESIZE);
		
		GridData tabFolderData = new GridData();
		tabFolderData.horizontalAlignment = GridData.FILL;
		tabFolderData.verticalAlignment = GridData.FILL;
		tabFolderData.grabExcessHorizontalSpace = true;
		tabFolderData.grabExcessVerticalSpace = true;
		tabFolderData.heightHint = DEFAULT_TABFOLDER_HEIGHT;
		tabFolderData.widthHint = DEFAULT_TABFOLDER_WIDTH;
		tabFolderData.horizontalSpan = 2;
		m_tabFolder.setLayoutData(tabFolderData);
		
        // Add a status label.
		m_statusLabel = new Label(parent,SWT.NONE);
		
		m_statusLabel.setAlignment(SWT.LEFT);
		m_statusLabel.setText("Status: ");
		m_statusLabel.setToolTipText("The status area.");
		//m_statusLabel.setFont(new Font(parent.getDisplay(),"Comic Sans MS", 14, SWT.BOLD));
		Font statusFont = m_statusLabel.getFont();
		FontData[] statusFontData = statusFont.getFontData();
		for (int i = 0; i < statusFontData.length; i++) {
			statusFontData[i].setStyle(SWT.BOLD);
		}
		m_statusLabel.setFont(new Font(parent.getDisplay(),statusFontData));
 
		GridData statusLabelData = new GridData();
		statusLabelData.horizontalAlignment = GridData.FILL;
		statusLabelData.verticalAlignment = GridData.FILL;
		m_statusLabel.setLayoutData(statusLabelData);
         
        // Add a status area.
		m_status = new Label(parent,SWT.BORDER | SWT.HORIZONTAL | SWT.SHADOW_IN | SWT.CENTER | SWT.RESIZE);
                 
		m_status.setAlignment(SWT.LEFT);
		m_status.setText("Status messages will be displayed here.");
		m_status.setToolTipText("The status area.");
		//m_status.setFont(new Font(parent.getDisplay(),"Comic Sans MS", 14, SWT.BOLD));
		/*
		statusFont = m_status.getFont();
		statusFontData = statusFont.getFontData();
		for (int i = 0; i < statusFontData.length; i++) {
			statusFontData[i].setStyle(SWT.BOLD);
		}
		m_status.setFont(new Font(parent.getDisplay(),statusFontData));
		*/

        GridData statusData = new GridData();
        statusData.horizontalAlignment = GridData.FILL;
        statusData.verticalAlignment = GridData.FILL;
        statusData.grabExcessHorizontalSpace = true;
        m_status.setLayoutData(statusData);
        
        // Update the layout.
		parent.layout();
    }
    
    public void topLevelShell(boolean top)
    {
    	m_topLevelShell = top;
    }
    
    public boolean isTopLevelShell()
    {
    	return m_topLevelShell;
    }
    
    public String getPluginName()
    {
    	return m_pluginName;
    }

    public void handleFileExitAction(Event event)
    {
        if (GuiUtilities.askYesNo("Do you really want to exit?", this))
        {
        	// XXX - Should not exit without disposing.
            System.exit(0);
        }
    }
    
    /**
     * Process all messages in the <code>MessageQueue</code>.
	 *
	 * @see com.wizzer.mle.studio.framework.IMessageQueueProxy#processMessages()
     */
    public void processMessages()
    {
    	Message msg;
    	while ((msg = m_messages.peek(MessageProtocol.PLUGIN_STATUS_MSGTYPE)) != null) {
			String status = new String(msg.getMessageData(),0,msg.getMessageLength());
			m_status.setText(status);
			if (m_console.isOpen())
				m_console.println(status);
			m_messages.pop(msg);
    	}
    }
    
    /**
     * Get the message queue managed by this plug-in.
     * 
     * @return A reference to the <code>MessageQueue</code> os returned.
	 *
	 * @see com.wizzer.mle.studio.framework.IMessageQueueProxy#getMessageQueue(java.lang.String)
     */
    public MessageQueue getMessageQueue()
    {
    	return m_messages;
    }

    /**
     * Determine if modifications have been made on the data managed by this frame.
     * 
     * @return <b>true</b> will be returned if the data has been changed.
     * Otherwise, <b>false</b> will be returned.
     */
    public boolean isDirty()
    {
    	return m_isDirty;
    }

}




