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

/**
 * This class is used to manage global constants required by the Wizzer Works
 * Framework.
 * 
 * @author Mark S. Millard
 */
public class GuiConstants
{
    // Change these to reconfigure the tree renderer.

    /** The height of a edit field. */
    public static int FIELD_HEIGHT_EDIT = 13;
    /** The size of the pixel border in an edit field. */
	public static int BORDER_SIZE_EDIT = 2;
	/** The width of the label in an edit field. */
	public static int LABEL_WIDTH_EDIT = 350;
	/** The width of the edit field. */
	public static int EDIT_WIDTH_EDIT = 600;
	/** The size of the font in an edit field. */
	public static int FONT_SIZE_EDIT = 12;

    // These are usually calculated based on the above...
    /** The width of a plug-in panel. */
    public static int PANEL_HEIGHT_EDIT = 17;
    /** The height of a plug-in panel. */
    public static int PANEL_WIDTH_EDIT = 956;

    /**
     * Set the global constants used to configure table tree editors.
     * 
     * @param fieldHeightEdit The edit field height.
     * @param borderSizeEdit The size of the pixel border.
     * @param labelWidthEdit The width of the label.
     * @param editWidthEdit The widht of the edit field.
     * @param fontSizeEdit The size of the edit field font.
     */
    public static void setConstants(
        int fieldHeightEdit,
        int borderSizeEdit,
        int labelWidthEdit,
        int editWidthEdit,
        int fontSizeEdit)
    {
        FIELD_HEIGHT_EDIT = fieldHeightEdit;
        BORDER_SIZE_EDIT = borderSizeEdit;
        LABEL_WIDTH_EDIT = labelWidthEdit;
        EDIT_WIDTH_EDIT = editWidthEdit;
        FONT_SIZE_EDIT = fontSizeEdit;

        // These are calculated based on the above...
        PANEL_HEIGHT_EDIT = FIELD_HEIGHT_EDIT + (2 * BORDER_SIZE_EDIT);
        PANEL_WIDTH_EDIT = LABEL_WIDTH_EDIT + EDIT_WIDTH_EDIT + (3 * BORDER_SIZE_EDIT);
    }

}

