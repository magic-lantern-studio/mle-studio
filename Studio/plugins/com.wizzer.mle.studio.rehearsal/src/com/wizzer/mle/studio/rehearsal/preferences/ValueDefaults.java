/*
 * ValueDefaults.java
 * Created on Jun 5, 2006
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
package com.wizzer.mle.studio.rehearsal.preferences;

// Import Eclipse classes.
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.rehearsal.RehearsalPlugin;

/**
 * This class is used to initialize the default values for the preferences
 * established by the com.wizzer.mle.studio.dwp plug-in.
 * <p>
 * Preferences that are set by this initializer include:
 * <li>
 * <ul>Rehearsal Player View Configuration</ul>
 * <li>
 *  
 * @author Mark S. Millard
 */
public class ValueDefaults extends AbstractPreferenceInitializer
{    
    /** The preference key for specifying how to launch the Rehearsal Player views. */
    public static final String VIEW_CONFIGURATION_KEY = "mle.rehearsalplayer.view.configuration";
    /** Launch the Rehearsal Player views in the Magic Lantern Perspective. */
    public static final int VIEW_CONFIGURATION_PERSPECTIVE = 0;
    /** Launch the Rehearsal Player views in a separate window. */
    public static final int VIEW_CONFIGURATION_WINDOW = 1;
    
    /**
     * The default constructor.
     */
    public ValueDefaults()
    {
        super();
    }

    /**
     * Intialize the settings for the plug-in's preferences.
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences()
    {
        IPreferenceStore store = RehearsalPlugin.getDefault().getPreferenceStore();
        store.setDefault(VIEW_CONFIGURATION_KEY,VIEW_CONFIGURATION_WINDOW);
    }

}
