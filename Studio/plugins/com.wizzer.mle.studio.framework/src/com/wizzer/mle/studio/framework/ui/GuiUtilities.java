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

// Import Eclipse packages.
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.DirectoryDialog;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.FrameworkLog;

/**
 * This class provides some useful GUI utility.
 * 
 * @author Mark S. Millard
 */
public class GuiUtilities
{
    static FileDialog m_fileChooser = null;
    static DirectoryDialog m_directoryChooser = null;
    static MessageDialog m_inputDialog= null;

    /**
     * Retrieve an existing file using a File Dialog chooser.
     * 
     * @param caller The <code>Control</code> required for launching the File Dialog widget.
     * 
     * @return A <code>File</code> is returned if a valid file was chosen via the GUI.
     * Otherwise <b>null</b> will be returned.
     */
    static public File openFile(Control caller)
    {
        File file = null;

        m_fileChooser = new FileDialog(caller.getShell());
        String filename =  m_fileChooser.open();
        if (filename != null)
        {
            file = new File(filename);
        } // If user selected a file.

        return file;
    }

    /**
     * Create or overwrite a file using a File Dialog chooser.
     * 
     * @param caller The <code>Control</code> required for launching the File Dialog widget.
     * 
     * @return A <code>File</code> is returned if a valid file was created via the GUI.
     * Otherwise <b>null</b> will be returned.
     */
    static public File saveFile(Control caller)
    {
        File file = null;

        m_fileChooser = new FileDialog(caller.getShell());
        m_fileChooser.setText("Save As");
        
        
        String filename =  m_fileChooser.open();
        if (filename != null)
        {
            // Create a new File object.
            file = new File(filename);
            
            // Check if it already exists.
            if (file.exists())
            {
                if (askYesNo("This file exists. Do you want to replace it?", caller))
                {
                    file.delete();
                }
                else
                {
                    file = null;
                    return null;
                }
            }
            
            // Create or overwrite the new file.
            try
            {
                file.createNewFile();
                return file;
            }
            catch (Exception ex)
            {
            	String message = new String("Error ocurred while creating a file: " + ex.getMessage());
            	
            	// Provide feedback to the user.
                popupMessage(message, caller);
                
                // Log an error.
                FrameworkLog.logError(ex,message);
                
                return null;
            }
        } // If user selected a file.

        // Should never reach here.
        return null;
    }

	/**
	 * Retrieve an existing directory using a Directory Dialog chooser.
	 * 
	 * @param caller The <code>Control</code> required for launching the Directory Dialog widget.
	 * 
	 * @return A <code>File</code> is returned if a valid directory was chosen via the GUI.
	 * Otherwise <b>null</b> will be returned.
	 */
    static public File openDirectory(Control caller, String message)
    {
        File file = null;

        m_directoryChooser = new DirectoryDialog(caller.getShell());
        m_directoryChooser.setMessage( message );
        String directory =  m_directoryChooser.open();
        if (directory != null)
        {
            // Create a new File object.
            file = new File(directory);

            return file;            
        } // If user selected a file.

        // Should never reach here.
        return null;
    }

   /**
    * Display an error that needs to be presented as a Dialog to the user.
    * <p>
    * The error will also be logged.
    * </p>
    * 
    * @param str The message to display.
    * @param ex The exception that was thrown when the error occurred.
    * @param caller  The <code>Control</code> required for launching the Dialog widget.
    */
   static public void handleError(String str, Exception ex, Control caller)
    {
    	String message = new String(str + ": " + ex.toString());
    	
    	// Provide user feedback.
        popupMessage(message, caller);
        
        // Log an error.
        FrameworkLog.logError(ex,message);
    }

	/**
	 * Ask a Yes/No question via a Dialog Panel.
	 * 
	 * @param question The question to display.
	 * @param caller  The <code>Control</code> required for launching the Dialog widget.
	 */
    static public boolean askYesNo(String question, Control caller)
    {
    	boolean result = MessageDialog.openQuestion(caller.getShell(),
    		"Magic Lantern Tool",question);
        return result;
    }

	/**
	 * Present an informative message via a Dialog Panel.
	 * 
	 * @param message The message to display.
	 * @param caller  The <code>Control</code> required for launching the Dialog widget.
	 */
    static public void popupMessage(String message, Control caller)
    {
    	boolean result = MessageDialog.openConfirm(caller.getShell(),
    		"Magic Lantern Tool", message);
    }

}




