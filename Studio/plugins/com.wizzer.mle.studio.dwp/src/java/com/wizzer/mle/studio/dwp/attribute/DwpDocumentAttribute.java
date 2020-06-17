/*
 * DwpDocumentAttribute.java
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
package com.wizzer.mle.studio.dwp.attribute;

// Import Magic Lantern Framework classes.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.attribute.NumberAttribute;
import com.wizzer.mle.studio.framework.attribute.StringAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;

/**
 * This class implements an <code>Attribute</code> that represents a Digital Workprint
 * document.
 * 
 * @author Mark S. Millard
 */
public class DwpDocumentAttribute extends VariableListAttribute
{
	/** DWP Document attribute type. */
	public static final String TYPE_DWP_DOCUMENT = "com.wizzer.mle.studio.dwp.attribute.DOCUMENT";

	/**
	 * A constructor for initializing the <code>DwpDocumentAttribute</code>.
	 * <p>
	 * It provides a function name which is to be called when an element is
	 * added or removed. The function is called back on the <code>Object</code> which
	 * is specified as the caller.
	 * </p>
	 * 
	 * @param displayName A <code>String</code> that can be displayed in a GUI.
	 * @param countAttribute A <code>NumberAttribute</code> identifying the number of attributes in the variable
	 * list.
	 * @param callback The name of a method that can be called back when an element
	 * is added to the variable list.
	 * @param callbackData User data to associate with the callback.
	 * @param theCaller The <code>Object</code> that is called back when an element is added or removed from
	 * the variable list.
	 */
   public DwpDocumentAttribute(String displayName,
        NumberAttribute countAttribute, String callback,
        Object callbackData, Object theCaller)
    {
        super(displayName, countAttribute, callback, callbackData, theCaller);
    }

   /**
    * A constructor for initializing the <code>DwpDocumentAttribute</code>.
    * <p>
    * It provides a function name which is to be called when an element is
    * added or removed. The function is called back on the <code>Object</code> which
	 * is specified as the caller.
    * </p><p>
    * The multiplier and base are provided so that the associated count attribute can be
    * automatically maintained.
    * </p><p>
    * For N elements, the count is: <b>base + ( N * multiplier)</b>.
    * </p>
    * 
    * @param displayName A <code>String</code> that can be displayed in a GUI.
    * @param countAttribute A <code>NumberAttribute</code> identifying the number of attributes in the variable
    * list.
    * @param callback The name of a method that can be called back when an elemnt
    * is added to the variable list.
    * @param callbackData User data to associate with the callback.
    * @param theCaller The <code>Object</code> that is called back when an element is added or removed from
    * the variable list.
    * @param multiplier
    * @param base
    */
    public DwpDocumentAttribute(String displayName,
        NumberAttribute countAttribute, String callback,
        Object callbackData, Object theCaller, int multiplier, int base)
    {
        super(displayName, countAttribute, callback, callbackData, theCaller,
                multiplier, base);
    }

	/**
	 * Get the <code>DwpDocumentAttribute</code> type.
	 * 
	 * @return By default, <b>TYPE_DWP_DOCUMENT</b> is always returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.Attribute#getType()
	 */
    public String getType()
    {
        return TYPE_DWP_DOCUMENT;
    }

}
