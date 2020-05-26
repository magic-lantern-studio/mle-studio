/*
 * StudioPerspective.java
 * Created on Nov 6, 2006
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
package com.wizzer.mle.studio.perspective;

// Import standard Java packages.
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

// Import Eclipse classes.
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

// Include Magic Lantern classes.
import com.wizzer.mle.studio.MleLog;


/**
 * This class implements an Eclipse IDE Perspective for the Wizzer Works
 * Magic Lantern Studio tool suite.
 */
public class StudioPerspective implements IPerspectiveFactory
{
	// The resource bundle name.
	private static final String RESOURCE_BUNDLE= StudioPerspective.class.getName();
	// The resource bundle.
	private static ResourceBundle g_ResourceBundle= ResourceBundle.getBundle(RESOURCE_BUNDLE);

	/** Identifier for JDT Package Explorer. */
	public static final String ID_JAVA_PACKAGE_EXPLORER = "org.eclipse.jdt.ui.PackageExplorer";
	/** Identifier for JDT Java Hierarchy Perspective. */
	public static final String ID_JAVA_PERSPECTIVE = "org.eclipse.jdt.ui.JavaHierarchyPerspective";

	/**
	 * The default constructor.
	 */
	public StudioPerspective()
	{
		super();
	}

	/**
	 * The initial layout of this perspective will have no editor area. It will 
	 * contain a folder with the Resource Navigator view.
	 * There is a shortcut to the dialogs solution and the Java perspective.
	 * *
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout)
	{
		// Get the editor area.
		String editorArea = layout.getEditorArea();

		// Top left: Project and Package Explorers.
		IFolderLayout topLeft =
			layout.createFolder("topLeft", IPageLayout.LEFT, 0.20f, editorArea);
		topLeft.addView(ID_JAVA_PACKAGE_EXPLORER);
		topLeft.addView(IPageLayout.ID_PROJECT_EXPLORER);
		//topLeft.addPlaceholder(IPageLayout.ID_BOOKMARKS);

		// Bottom right: Property Sheet View.
		IFolderLayout bottomRight =
			layout.createFolder("bottomRight", IPageLayout.BOTTOM, 0.80f, editorArea);
		bottomRight.addView(IPageLayout.ID_PROP_SHEET);

		// Top right: Outline View
		IFolderLayout topRight =
			layout.createFolder("topRight", IPageLayout.RIGHT, 0.75f, editorArea);
		topRight.addView(IPageLayout.ID_OUTLINE);
	
		// Hide editor area.
		layout.setEditorAreaVisible(false);
		//layout.addNewWizardShortcut(ID_BASIC_WIZARD);
		layout.addPerspectiveShortcut(ID_JAVA_PERSPECTIVE);
	}

	/**
	 * Gets a string from the resource bundle.
	 * 
	 * @param key The string used to get the bundle value, must not be <b>null</b>.
	 * 
	 * @return The value for the specified key is returned as a string.
	 */
	public static String getString(String key)
	{
		try
		{
			return g_ResourceBundle.getString(key);
		} catch (MissingResourceException e)
		{
			MleLog.logWarning("Missing resource string: " + key);
			return null;
		}
	}
	
	/**
	 * Gets a string from the resource bundle and formats it with the argument.
	 * 
	 * @param key The string used to get the bundle value, must not be <b>null</b>.
	 * @param arg The formatting argument.
	 * 
	 * @return The value for the specified key is returned as a formatted string.
	 */
	public static String getFormattedString(String key, Object arg)
	{
		return MessageFormat.format(getString(key), new Object[] { arg });
	}

	/**
	 * Gets a string from the resource bundle and formats it with arguments.
	 * 
	 * @param key The string used to get the bundle value, must not be <b>null</b>.
	 * @param arg The formatting arguments.
	 * 
	 * @return The value for the specified key is returned as a formatted string.
	 */	
	public static String getFormattedString(String key, Object[] args)
	{
		return MessageFormat.format(getString(key), args);
	}

}
