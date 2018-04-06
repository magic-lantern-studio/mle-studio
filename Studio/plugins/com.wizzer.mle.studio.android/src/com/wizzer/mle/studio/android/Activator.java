// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2012  Wizzer Works
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
package com.wizzer.mle.studio.android;

// Import standard Java classes.
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

// Import Eclipse classes.
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

// Include ADT classes.
import com.android.ide.eclipse.adt.AdtConstants;

/**
 * The class is the main Eclipse Plug-in class for the com.wizzer.mle.studio.android
 * plug-in from Wizzer Works.
 */
public class Activator extends AbstractUIPlugin
{
	// The plug-in ID.
	public static final String PLUGIN_ID = "com.wizzer.mle.studio.android";

	// The shared instance of the plugin.
	private static Activator g_plugin;
	
	/**
	 * The constructor.
	 */
	public Activator()
	{
		g_plugin = this;
	}

	/**
	 * This method is called upon plug-in activation.
	 */
	public void start(BundleContext context) throws Exception
	{
		// We use System.out.println here instead of MleLog.logInfo because
	    // the plug-in activation will fail otherwise. This happens because
	    // the plug-in has not yet been completely constructed and initialized.
	    System.out.println("Activating com.wizzer.mle.studio.android plug-in.");
		super.start(context);
	}

	/**
	 * This method is called upon plug-in deactivation.
	 */
	public void stop(BundleContext context) throws Exception
	{
		System.out.println("Deactivating com.wizzer.mle.studio.android plug-in.");
		g_plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return The shared instance of the plug-in.
	 */
	public static Activator getDefault()
	{
		return g_plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace()
	{
		return ResourcesPlugin.getWorkspace();
	}
	
	/**
	 * Get the plug-in identifier.
	 * 
	 * @return A <code>String</code> is returned identifying the plug-in.
	 */
	public static String getID()
	{
		return getDefault().getBundle().getSymbolicName();
	}
	
	/**
     * Returns the URL of a binary file embedded in the plugin jar file.
     * 
     * @param filepath The file path to the text file.
     * 
     * @return <b>null</b> is returned if the file was not found.
     */
    public static URL getEmbeddedFileUrl(String filepath)
    {
        Bundle bundle = null;
        synchronized (Activator.class) {
            if (g_plugin != null) {
                bundle = g_plugin.getBundle();
            } else {
                AndroidLog.logWarning(getID() + " Plugin is missing.");    //$NON-NLS-1$
                return null;
            }
        }

        // Attempt to get a file to one of the template.
        String path = filepath;
        if (! path.startsWith(AdtConstants.WS_SEP)) {
            path = AdtConstants.WS_SEP + path;
        }

        URL url = bundle.getEntry(path);

        if (url == null) {
            AndroidLog.logInfo("Bundle file URL not found at path " + path); //$NON-NLS-1$
        }

        return url;
    }
	
	/**
     * Reads and returns the content of a binary file embedded in the plugin jar
     * file.
     * 
     * @param filepath The file path to the text file.
     * 
     * @return <b>null</b> is returned if the file could not be read.
     */
    public static InputStream readEmbeddedFileAsStream(String filepath)
    {
        // Attempt to read an embedded file.
        try {
            URL url = getEmbeddedFileUrl(AdtConstants.WS_SEP + filepath);
            if (url != null) {
                return url.openStream();
            }
        } catch (MalformedURLException ex) {
            // We'll just return null.
            AndroidLog.logError(ex, "Failed to read stream " + filepath);  //$NON-NLS-1$
        } catch (IOException ex) {
            // We'll just return null;.
        	AndroidLog.logError(ex, "Failed to read stream " + filepath);  //$NON-NLS-1$
        }

        return null;
    }
    
    /**
     * Reads and returns the content of a binary file embedded in the plugin jar
     * file.
     * 
     * @param filepath The file path to the text file.
     * 
     * @return <b>null</b> will be returned if the file could not be read.
     */
    public static byte[] readEmbeddedFile(String filepath)
    {
        try {
            InputStream is = readEmbeddedFileAsStream(filepath);
            if (is != null) {
                // Create a buffered reader to facilitate reading.
                BufferedInputStream stream = new BufferedInputStream(is);

                // Get the size to read.
                int avail = stream.available();

                // Create the buffer and reads it.
                byte[] buffer = new byte[avail];
                stream.read(buffer);

                // And return.
                return buffer;
            }
        } catch (IOException e) {
            // We'll just return null.
        	AndroidLog.logInfo("Failed to read binary file " + filepath);  //$NON-NLS-1$
        }

        return null;
    }
    
    /**
     * Reads and returns the content of a text file embedded in the plugin jar
     * file.
     * 
     * @param filepath The file path to the text file.
     * 
     * @return <b>null>/b> is returned if the file could not be read.
     */
    public static String readEmbeddedTextFile(String filepath)
    {
        try {
            InputStream is = readEmbeddedFileAsStream(filepath);
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuilder total = new StringBuilder(reader.readLine());
                while ((line = reader.readLine()) != null) {
                    total.append('\n');
                    total.append(line);
                }

                return total.toString();
            }
        } catch (IOException ex) {
            // We'll just return null.
        	AndroidLog.logError(ex, "Failed to read text file " + filepath);  //$NON-NLS-1$
        }

        return null;
    }
}
