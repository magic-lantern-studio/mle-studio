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

