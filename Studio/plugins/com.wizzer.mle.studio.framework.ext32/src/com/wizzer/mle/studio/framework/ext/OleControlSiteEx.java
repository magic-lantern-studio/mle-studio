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
package com.wizzer.mle.studio.framework.ext;

// Import Eclipse classes.
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.SIZE;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Point;

/**
 * This class extends <code>OleClientSite</code> for the creation of ActiveX controls,
 * in or out-of-process.
 *
 * @author Mark S. Millard
 *
 * Uses CoCreateInstance to create a COM object (instead of OleCreate);
 * this allows the creation of an out-of-process server.
 */
public class OleControlSiteEx extends OleClientSite implements IControlSite
{
	/**
	 * A constructor that specifies the parent <code>OleFrame</code> in which to embed
	 * the COM object.
	 * 
	 * @param parent A composite widget; must be an OleFrame.
	 * @param style The bitwise OR'ing of widget styles.
	 * @param progId The unique program identifier of an OLE Document application;
	 * the value of the ProgID key or the value of the VersionIndependentProgID key
	 * specified in the registry for the desired OLE Document (for example,
	 * the VersionIndependentProgID for Word is Word.Document) program identifier.
	 */
	public OleControlSiteEx(Composite parent, int style, String progId)
	{
		super(parent, style);
		try
		{
			// Get the class identifier.
			appClsid = getClassID(progId);
			if (appClsid == null)
			{
				OLE.error(OLE.ERROR_INVALID_CLASSID);
			} 

			// Create the object.
			long[] ppvObject = new long[1];
			int CLSCTX_ALL = COM.CLSCTX_INPROC_SERVER | COM.CLSCTX_LOCAL_SERVER | COM.CLSCTX_INPROC_HANDLER;
			int hr = COM.CoCreateInstance(appClsid, 0, CLSCTX_ALL, COM.IIDIUnknown, ppvObject);
			if (hr != COM.S_OK)
			{
				OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, hr);
			}
	
			// Set the IUnknown pointer.
			objIUnknown = new IUnknown(ppvObject[0]);

			// Initialize sinks.
			addObjectReferences();
		}
		catch(SWTError ex)
		{
			this.dispose();
			disposeCOMInterfaces();
			throw ex;
		}			
	}
	
	/**
	 * Get the extent of the embedded OLE native object.
	 * 
	 * @return a <>Point<> is returned containing the x and y dimensions of the
	 * embedded OLE native object.
	 */
	public Point getExtent()
	{
		SIZE sizel = new SIZE();
		// Get the current size of the embedded OLENatives object.
		if (objIOleObject != null)
		{
			if (objIViewObject2 != null && !COM.OleIsRunning(objIOleObject.getAddress()))
			{
				objIViewObject2.GetExtent(COM.DVASPECT_CONTENT, -1, 0, sizel);
			} else
			{
				objIOleObject.GetExtent(COM.DVASPECT_CONTENT, sizel);
			}
		}
		return new Point(sizel.cx, sizel.cy);
	}	

};


