/*
 * DwpTableIdeFrame.java
 * Created on Jul 22, 2004
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
package com.wizzer.mle.studio.dwp.editor;

// Import standard Java packages.
import java.util.Observable;

// Import Eclipse packages.
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.actions.PartEventAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.Message;
import com.wizzer.mle.studio.framework.MessageQueue;
import com.wizzer.mle.studio.framework.MessageProtocol;
import com.wizzer.mle.studio.framework.ProcessMessageQueue;
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.ui.ToolConsole;
import com.wizzer.mle.studio.framework.ui.AttributeContextMenuHandlerRegistry;

// Import Magic Lantern Digital Workprint packages.
//import com.wizzer.mle.studio.dwp.DwpLog;
import com.wizzer.mle.studio.dwp.DwpPlugin;
import com.wizzer.mle.studio.dwp.action.DwpFileDumpToConsoleAction;
import com.wizzer.mle.studio.dwp.action.DwpFileDumpToFileAction;
import com.wizzer.mle.studio.dwp.action.DwpFileOpenAction;
import com.wizzer.mle.studio.dwp.action.DwpFileSaveAction;
import com.wizzer.mle.studio.dwp.action.DwpFileSaveAsAction;
import com.wizzer.mle.studio.dwp.action.DwpViewConsoleAction;
import com.wizzer.mle.studio.dwp.domain.DwpTable;
import com.wizzer.mle.studio.dwp.view.DwpConsoleView;

/**
 * This class is used to integrate the <code>DwpTableMainFrame</code> with the Eclipse IDE.
 * It takes care of the Actions that need to be added to the <code>DwpTableEditor</code>.
 * 
 * @author Mark S. Millard
 */
public class DwpTableIdeFrame extends DwpTableMainFrame
{
	// The ttile for the tool.
	static private final String MAIN_TITLE = "Digital Workprint Table Tool";

	/** The view console Action. */
	protected PartEventAction m_consoleAction = null;

	/** The current Attribute selection. */
	protected IAttribute m_currentSelection = null;

	// Actions
	//private DwpFileNewAction m_newAction = null;
	private DwpFileOpenAction m_openAction = null;
	private DwpFileSaveAction m_saveAction = null;
	private DwpFileSaveAsAction m_saveAsAction = null;
	private DwpFileDumpToConsoleAction m_dumpToConsoleAction = null;
	private DwpFileDumpToFileAction m_dumpToFileAction = null;

	// Message Queue Handler
	private ProcessMessageQueue m_msgQueueHandler;
	
	// The background color for clearing the highlight.
	private Color m_backgroundColor = null;
	// The background color for highlighting selections.
	private Color m_highlightColor = null;


	/**
	 * A constructor that specifies the parent <code>Composite</code> and style.
	 * 
	 * @param parent The parent <code>Composite</code> widget.
	 * @param style The SWT style.
	 */
	public DwpTableIdeFrame(Composite parent, int style)
	{
		super(parent, MAIN_TITLE, new DwpTable());
		m_pluginName = MAIN_TITLE;
		
		// Create the required Consoles. Note that the View that will be in communication with
		// these consoles will be set later, while the DwpTableEditor is being initialized.
		m_console = new ToolConsole(null,DwpConsoleView.getID());
		
		// Establish the colors that will be used for highlighting/clearing selections.
		m_backgroundColor = getAttributeTreeViewer().getTreeViewer().getTree().getBackground();
		m_highlightColor = new Color(DwpPlugin.getWorkbenchWindow().getShell().getDisplay(),
                224,223,227);
		
		// Add the menu management actions.
		//addActions(parent);
		
		// Add listeners.
		addListeners();
	}

	/**
	 * Add actions to the parent composite widget.
	 * 
	 * @param parent The parent <code>Composite</code>.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.PluginFrame#addActions(org.eclipse.swt.widgets.Composite)
	 */
	public void addActions(Composite parent)
	{
		// Do nothing for now.
	}

	// Add listeners to the table viewer.
	private void addListeners()
	{
	   m_tree.getTreeViewer().addSelectionChangedListener(
		   new ISelectionChangedListener()
		   {
				public void selectionChanged(SelectionChangedEvent event)
				{
					handleSelectionChanged(event);
				}
		   });

	    m_tree.getTreeViewer().getTree().addKeyListener(
	        new KeyListener()
	        {
	            public void keyPressed(KeyEvent event)
	            {
	                //System.out.println("Key Pressed: " + event.keyCode);
	            }
	            public void keyReleased(KeyEvent event)
	            {
	                if (event.keyCode == SWT.DEL)
	                    handleDeleteKeyPressed(event);
	                else if (event.keyCode == SWT.INSERT)
	                    handleInsertKeyPressed(event);
	            }
	        });
	}

	/**
	 * Add actions to the specifed part's menu.
	 * 
	 * @param part The editor part to use for the menu.
	 */
	public void addMenuActions(IEditorPart part)
	{
		IMenuManager menuMgr = part.getEditorSite().getActionBars().getMenuManager();
		IToolBarManager toolBarMgr = part.getEditorSite().getActionBars().getToolBarManager();
		
		/*
		IContributionItem[] items = menuMgr.getItems();
		for (int i = 0; i < items.length; i++) {
			System.out.println("Contribution Item: " + items[i].getId());
		}
		*/
		
		// Get the submenu for the MPEG BRM Tools.
		IMenuManager dwpTableMenu = menuMgr.findMenuUsingPath("com.wizzer.mle.studio.dwp.editor.DwpTableMenu");
		
		// The New/Open/Save/SaveAs items.
	
		//m_newAction = new DwpFileNewAction(part);
		//dwpTableMenu(m_newAction);

		//m_openAction = new DwpFileOpenAction(part);
		//dwpTableMenu.add(m_openAction);

		//m_saveAction = new DwpFileSaveAction(part);
		//dwpTableMenu.add(m_saveAction);

		//m_saveAsAction = new DwpFileSaveAsAction(part);
		//dwpTableMenu.add(m_saveAsAction);
	
		//dwpTableMenu.add(new Separator());
		
		// The Console items.
		
		m_consoleAction = new DwpViewConsoleAction(part);
		dwpTableMenu.add(m_consoleAction);
		DwpPlugin.getWorkbenchPage().addPartListener(m_consoleAction);
		
		dwpTableMenu.add(new Separator());
		
		m_dumpToConsoleAction = new DwpFileDumpToConsoleAction(part);
		dwpTableMenu.add(m_dumpToConsoleAction);

		m_dumpToFileAction = new DwpFileDumpToFileAction(part);
		dwpTableMenu.add(m_dumpToFileAction);

		dwpTableMenu.add(new Separator());
	}

	/**
	 * Process the View Console Action.
	 * 
	 * @param event The <code>MenuItem</code> event that caused this handler to be invoked.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.PluginFrame#handleViewConsoleAction(org.eclipse.swt.widgets.Event)
	 */
	public void handleViewConsoleAction(Event event)
	{
		//DwpLog.logInfo("Got View Console Action Event: " + event.toString());
		if (m_consoleAction.isChecked())
		{
			// Show the Console.
			m_console.setVisible(true);
			m_console.setName(m_pluginName, false);
			//m_consoleAction.setChecked(false);
		} else
		{
			// Hide the Console.
			m_console.setVisible(false);
			//m_consoleAction.setChecked(true);
		 }
	}

	/**
	 * Get the <code>ToolConsole</code> associated with this frame.
	 * 
	 * @return The <code>ToolConsole</code> is returned.
	 */
	public ToolConsole getConsole()
	{
		return (ToolConsole)m_console;
	}
	
	/**
	 * Hide the console.
	 */
	public void hideConsole()
	{
	    m_console.setVisible(false);
	    m_consoleAction.setChecked(false);
	}
	
	/**
	 * Set the handler for the message queue.
	 * 
	 * @param handler The <code>ProcessMessageQueue</code> handler.
	 */
	public void setMsgQueueHandler(ProcessMessageQueue handler)
	{
		m_msgQueueHandler = handler;
	}
	
	/**
	 * Get the handler for the message queue.
	 * 
	 * @retrun The <code>ProcessMessageQueue</code> handler is returned.
	 */
	public ProcessMessageQueue getMsgQueueHandler()
	{
		return m_msgQueueHandler;
	}


	/*
	 * Retrieve the selected <code>IAttribute</code>.
	 */
	private IAttribute getSelectedAttribute()
	{
		IStructuredSelection selectedItems = (IStructuredSelection)m_tree.getTreeViewer().getSelection();
    	
		// Check to see if any items were selected.
		if (selectedItems.isEmpty())
		{
			return null;
		}
        
		return (IAttribute)selectedItems.getFirstElement();
	}
	
	// Highlight the selected items in the TreeViewer.
	private void highlightSelectedItems(Color color)
	{
        TreeItem[] items = getSelectedItems();
        for (int i = 0; i < items.length; i++)
        	if (! items[i].isDisposed())
        		items[i].setBackground(color);
	}

	/**
	 * Handle the Selection Changed event on the TableTreeViewer.
	 * 
	 * @param event The selection event that caused this handler to be invoked.
	 */
	public void handleSelectionChanged(SelectionChangedEvent event)
	{
		IAttribute attr = getSelectedAttribute();
		//System.out.println("Attribute selected: " + attr.getName());
		
		// Clear the the highlight with the established background color.
		if (m_currentSelection != null)
		{
            highlightSelectedItems(m_backgroundColor);
		}
		
		// Remember the currently selected Attribute(s). Used by the Property View
		// to determine whether properties may be modified or not.
		m_currentSelection = attr;
		setSelectedItems();
		
		// Highlight the new selection.
		highlightSelectedItems(m_highlightColor);
	}
	
	/**
	 * Handle the key event for deletion.
	 * 
	 * @param event The key event that caused this handler to be invoked.
	 */
	public void handleDeleteKeyPressed(KeyEvent event)
	{
	    //System.out.println("Delete Key Released: " + event.keyCode);
	    
	    // Delete the current selection.
	    if (m_currentSelection != null)
	    {
		    Attribute attr = (Attribute)m_currentSelection;
		    Attribute parent = attr.getParent();
		    parent.deleteChild(attr);
	    }
	}
	
	/**
	 * Handle the key event for insertion.
	 * 
	 * @param event The key event that caused this handler to be invoked.
	 */
	public void handleInsertKeyPressed(KeyEvent event)
	{
	    //System.out.println("Insert Key Released: " + event.keyCode);
	    
	    if (m_currentSelection != null)
	    {
		    Attribute attr = (Attribute)m_currentSelection;
		    Menu popup = AttributeContextMenuHandlerRegistry.getInstance().getContextMenu(
	        	attr,m_tree);
		    popup.setVisible(true);
	    }
	}

	/**
	 * This method is called whenever the observed object is changed.
	 * <p>
	 * The <code>DwpTableIdeFrame</code> observes modifications made to its
	 * <code>MessageQueue</code>.
	 * </p>
	 * 
	 * @param obs The oberservable object.
	 * @param obj An argument passed by the observed object when
	 * <code>notifyObservers</code> is called.
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable obs, Object obj)
	{
		// Attempt to handle messages here first.
		if (obs.getClass().getName().equals("com.wizzer.mle.studio.framework.MessageQueue"))
		{
			// Process the message queue for updating the status area.
			MessageQueue queue = (MessageQueue)obs;
			Message msg = (Message)obj;
				
			if ((msg != null) && (msg.getMessageType() == MessageProtocol.PLUGIN_STATUS_MSGTYPE))
			{
				if (m_msgQueueHandler != null) {
					Display.getDefault().asyncExec(m_msgQueueHandler);
					return;
				}
			}
		}
		
		// Can't handle it here, so let the parent class try.
		super.update(obs,obj);
	}
}
