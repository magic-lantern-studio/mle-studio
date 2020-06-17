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
package com.wizzer.mle.studio.framework.ui;

// Import standard Java packages.
import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;

// Import Eclipse packages.
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.domain.Table;
import com.wizzer.mle.studio.framework.ui.AttributeTreeViewer;
import com.wizzer.mle.studio.framework.ui.GuiUtilities;


/**
 * This class implements a <code>PluginFrame</code> that supports a single
 * domain table.
 * 
 * @author Mark S. Millard
 */
public abstract class SingleTablePluginFrame extends PluginFrame
{
    private String m_mainTitle;
    protected AttributeTreeViewer m_tree;
    protected Table m_table;
    private File m_file = null;

    /**
     * Creates new form <code>Composite</code> for the application using
     * the <code>PluginFrame</code>.
     * 
     * @param parent The parent <code>Composite</code> widget..
     * @param mainTitle The name of the applet using this <code>PluginFrame</code>.
     * @param table The domain table for this applet.
     */
    public SingleTablePluginFrame(Composite parent, String mainTitle, Table table)
    {
        super(parent, SWT.RESIZE);
        
        // Initialize fields.
        m_pluginName = mainTitle;
        m_mainTitle = mainTitle;
        m_table = table;
                
        // Initialize GUI components.
        initGUI(this);
        
		// Build the Attribute Tree.
		this.buildTree(this);
    }
    
    /**
     * Get the Attribute Tree Viewer.
     * 
     * @return An <code>AttributeTreeViewer</code> is returned for this plug-in.
     */
    public AttributeTreeViewer getAttributeTreeViewer()
    {
        return m_tree;
    }

    /**
     * Reset the table.
     * <p>
     * Subclasses must provide this method.
     * </p>
     * 
     * @param table The table to reset the plug-in with. If <b>null</b>, then a default
     * instance of the sub-classed <code>Table</code> will be created.
     */
    abstract protected void resetTable(Table table);

    /**
     * This method is called from within the constructor to initialize the form.
     */
    protected void initGUI(Composite parent)
    {
    	// Do nothing for now.
    }

	// Build the Attribute Tree.
	private void buildTree(Composite parent)
	{
		// Remove all items from the TabFolder.
		TabItem[] tabItems = m_tabFolder.getItems();
		if (tabItems != null) {
			for (int i = 0; i < tabItems.length; i++) {
				TabItem item = tabItems[i];
				item.dispose();
			}
		}
		
		// Construct a tree from the domain table.
		m_tree = new AttributeTreeViewer(m_tabFolder,SWT.NONE,m_table);
        
		// Add an item to the tab folder.
		TabItem item = new TabItem(m_tabFolder,SWT.RESIZE);
		item.setText("Table Editor");
		item.setControl(m_tree.getControl());
        
		// Notify Observers of these changes.
		m_table.notifyObservers();
	}

    public void handleFileSaveAsAction(Event e)
    {
        this.saveFile(true);
        
        // Mark as undirty.
        m_isDirty = false;
    }

    public void handleFileSaveAction(Event e)
    {
        this.saveFile(false);
        
		// Mark as undirty.
		m_isDirty = false;
    }

    private void updateTitle()
    {
        String fileName = (m_file == null) ? "" : " [" + m_file.getName() + "]";
        this.getShell().setText(m_mainTitle + fileName);
    }

    public void saveFile(boolean saveAs)
    {
        if (m_file == null || saveAs) // if we need to select a file
        {
            m_file = GuiUtilities.saveFile(this);
            if (null == m_file)
            {
                return;
            }
        }
        
        try {
			m_table.save(m_file);
        }  catch (Exception ex)
        {
            GuiUtilities.handleError("Error ocurred while saving the file: " + ex.getMessage(), ex, this);
        }

        this.updateTitle();
    }

    public void handleFileNewAction(Event e)
    {
        if (GuiUtilities.askYesNo("Do you really want to start a new table?", this))
        {
            this.resetTable(null);
        }
    }

    public void handleFileExitAction(Event e)
    {
        if (GuiUtilities.askYesNo("Do you really want to exit?", this))
        {
        	// XXX - Should dispose resources first.
            System.exit(0);
        }
    }

    // Load data from the table file.
    private void actuallyOpenFile()
    {
        try {
            // Set "LOADING" to true as an optimization to avoid tons of redraws
            // while reading the file:
            Attribute.LOADING = true;

			// Open the file and create a new Attribute Tree.
			m_table.open(m_file);
			
			// Reconstruct the GUI Attribute Tree and its corresponding model.
			buildTree(this);
			
			// Mark dirty.
			m_isDirty = true;

			// Update the title.
            this.updateTitle();

            // Turn it back off to allow proper updates again.
            Attribute.LOADING = false;
            
        } catch (Exception ex)
        {
            GuiUtilities.handleError("Error ocurred while reading file", ex, this);
        }
        //this.acceptNewVCT();
    }

    public void handleFileOpenAction(Event e)
    {
        m_file = GuiUtilities.openFile(this);
        if (m_file != null)
            this.actuallyOpenFile();
    }

    public void handleFileDumpToConsoleAction(Event e)
    {
    	if (m_console.isOpen())
    	    this.dumpToStream(m_console.asStream());
    	else
            this.dumpToStream(System.out);
    }

    private void dumpToStream(PrintStream out)
    {
        out.println();
        out.println("Displaying Table...");
        out.println();
		m_table.dump(out);
    }

    public void handleFileDumpToFileAction(Event e)
    {
		File file = GuiUtilities.saveFile(this);
		if (file != null) {
			try {
			    PrintStream ps = new PrintStream(new FileOutputStream(file), true);
                this.dumpToStream(ps);
                ps.close();
			} catch (Exception ex) {
				GuiUtilities.handleError("Error ocurred while saving file: " + ex.getMessage(), ex, this);
			}
		}
    }

}




