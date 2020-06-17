/*
 * DwpTableMainFrame.java
 */

// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2000-2020 Wizzer Works
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.dwp.editor;

// Import standard Java packages.
import java.util.Observable;

// Import Eclipse packages.
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TreeItem;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.domain.Table;
import com.wizzer.mle.studio.framework.domain.TableChangedListener;
import com.wizzer.mle.studio.framework.ui.AttributeTreeViewer;
import com.wizzer.mle.studio.framework.MessageQueue;
import com.wizzer.mle.studio.framework.Message;
import com.wizzer.mle.studio.framework.MessageProtocol;
import com.wizzer.mle.studio.framework.ui.SingleTablePluginFrame;

// Import Digital Workprint packages.
import com.wizzer.mle.studio.dwp.domain.DwpTable;

/**
 * This class is used to configure the generic <code>SingleTablePluginFrame</code> for
 * use as a Digital Workprint Table editor.
 * 
 * @author Mark S. Millard
 */
public abstract class DwpTableMainFrame extends SingleTablePluginFrame
{
	/** Flag indicating that the Plugin has been initialized. */
	protected boolean m_isInitialized = false;
	/** A listener for table changed events. */
	protected TableChangedListener m_tableListener = null;
	
	// Items that are currently selected.
	private TreeItem[] m_selectedItems = null;

	/**
	 * Creates new form <code>Composite</code> for the application using
	 * the <code>SingleTablePluginFrame</code>.
	 * 
	 * @param parent The parent <code>Composite</code> widget.
	 * @param mainTitle The name of the applet using this <code>SingleTablePluginFrame</code>.
	 * @param table The domain table for this applet.
	 */
	public DwpTableMainFrame(Composite parent,String mainTitle,Table table)
	{
		super(parent, mainTitle, table);
		
		// Need to listen for modifications made to the table so that we
		// can mark it as dirty when necessary.
		table.addObserver(this);
	}
	
	/**
	 * Get the DWP domain table.
	 * 
	 * @return The <code>DwpTable</code> is returned.
	 */
	public DwpTable getTable()
	{
		return (DwpTable)m_table;
	}

	/**
	 * Reset the DWP Table.
	 * 
	 * @param table the DWP Table to reset to.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.SingleTablePluginFrame#resetTable()
	 */
	protected void resetTable(Table table)
	{
		// Create a new DWP Table.
		if (table == null)
		    m_table = new DwpTable();
		else
		    m_table = table;
        
		// Create the Attribute Tree Viewer for the new table.
		m_tree = new AttributeTreeViewer(m_tabFolder,SWT.NONE,m_table);
		m_selectedItems = null;
        
		// Add an item to the tab folder.
		TabItem item = new TabItem(m_tabFolder,SWT.RESIZE);
		item.setText("Digital Workprint Table");
		item.setControl(m_tree.getControl());
		
		// Mark dirty.
		m_isDirty = true;
        
		// Notify all observers of the table update.
		m_table.notifyObservers();
	}

	/**
	 * Get the initialization value for the table.
	 * 
	 * @return <b>true</b> will be returned if the table has been initialized.
	 * Ptherwise <b>false</b> will be returned.
	 */	
	public boolean getIsInitialized()
	{
		return m_isInitialized;
	}
	
	/**
	 * Set the initialization value for the table.
	 * 
	 * @param flag <b>true</b> should be used to set the table as initialized.
	 * Otherwise <b>false</b> should be used to indicate that the table is not initialized.
	 */
	public void setIsInitialized(boolean flag)
	{
		m_isInitialized = flag;
	}
	
	/**
	 * Determine if the table has been initialized.
	 * 
	 * @return <b>true</b> will be returned if the table has been initialized.
	 * Otherwise <b>false</b> will be returned.
	 */
	public boolean isInitialized()
	{
		return m_isInitialized;
	}
	
	/**
	 * Add a table changed listener to the table.
	 * 
	 * @param listener The <code>TableChangedListener</code> to add.
	 */
	public void addTableChangedListener(TableChangedListener listener)
	{
		m_tableListener = listener;
	}
	
	/**
	 * Indicate that the table has been modified.
	 * 
	 * @param flag <b>true</b> should be used to indicate that the table has been modified.
	 * Otherwise <b>false</b> should be used to indicate that the table has not changed.
	 */
	public void setIsDirty(boolean flag)
	{
		m_isDirty = flag;
	}
	
	/**
	 * Get the modification flag for the table.
	 * 
	 * @return <b>true</b> will be returned if the table has been modified.
	 * Otherwise <b>false</b> will be returned.
	 */
	public boolean getIsDirty()
	{
		return m_isDirty;
	}
	
	/**
	 * Get the <code>Item</code>s in the AttributeTreeViewer that are currently selected.
	 * 
	 * @return An array of <code>TreeItem</code> is returned. If no items are currently
	 * selected, then <b>null</b> will be returned.
	 */
	protected TreeItem[] getSelectedItems()
	{
	    return m_selectedItems;
	}
	
	/**
	 * Set the cache of the currently selected <code>Item</code>s in the AttributeTreeViewer.
	 */
	protected void setSelectedItems()
	{
	    m_selectedItems = m_tree.getTree().getSelection();;
	}

	/**
	 * This method is called whenever the observed object is changed.
	 * <p>
	 * The <code>DwpTableMainFrame</code> observes modifications made to the
	 * <code>DwpTable</code>.
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
		//String className = obs.getClass().getName();
		//System.out.println("Observable update() being called by " + className);
		if (obs.getClass().getName().equals("com.wizzer.mle.studio.framework.MessageQueue"))
		{
			// Process the message queue for updating the status area.
			MessageQueue queue = (MessageQueue)obs;
			Message msg = (Message)obj;
				
			if ((msg != null) && (msg.getMessageType() == MessageProtocol.PLUGIN_STATUS_MSGTYPE))
			{
				//System.out.println("Status message sent.");
				Display display = Display.getDefault();
				display.wake();
			}
		}
		else if ((obs instanceof DwpTable) && (obj != null))
		{
			//DwpLog.getInstance().debug("Obervable is DwpTable with input: " +
			//    obj.getClass().getName());
			
			// We want to ignore all updates until AFTER the table has been initialized.
			if (m_isInitialized)
			{
				m_isDirty = true;
				if (m_tableListener != null)
				{
					// Mark modified and notify all Observers.
					m_tableListener.setModified();
					m_tableListener.notifyObservers(new Boolean(m_isDirty));
				}
			}
		}
	}
}
