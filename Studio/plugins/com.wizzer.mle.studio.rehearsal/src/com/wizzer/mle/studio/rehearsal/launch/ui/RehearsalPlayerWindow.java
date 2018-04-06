/*
 * RehearsalPlayerWindow.java
 * Created on Jun 13, 2006
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
package com.wizzer.mle.studio.rehearsal.launch.ui;

// Import Eclipse classes.
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.Variant;

// Import Magic Lantern Studio framework classes.
import com.wizzer.mle.studio.framework.ext.IControlSite;
import com.wizzer.mle.studio.framework.ext.ControlSiteFactory;
import com.wizzer.mle.studio.framework.ext.IControlSiteAdapter;

// Import Magic Lantern DWP classes.
import com.wizzer.mle.studio.dwp.DwpPath;

/**
 * This class provides a window containing the Digital Workprint Rehearsal Player
 * ActiveX components.
 * 
 * @author Mark S. Millard
 */
public class RehearsalPlayerWindow implements DisposeListener
{
    // A singleton instance of the window.
    private static RehearsalPlayerWindow g_window = null;
    
    /** The player OLE Frame component. */
    OleFrame m_playerFrame = null;
    
    /** The player OLE Automation component. */
    public OleAutomation m_playerOleAutomation = null;

    /** The player component's OLE Control Site. */
    OleClientSite m_playerControlSite = null;

    /** Flag indicating player component is activated. */
    boolean m_playerActivated = false;

	// Hide the default constructor.
	private RehearsalPlayerWindow() {}

    /**
     * A constructor that specifies the parent <code>Composite</code> widget for the
     * Rehearsal Player window.
     * 
     * @param parent The parent <code>Composite</code> widget.
     */
    public RehearsalPlayerWindow(Composite parent)
    {
        GridLayout pLayout = new GridLayout();
        parent.setLayout(pLayout);

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FillLayout());

        GridData data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        //data.widthHint = 848;
        composite.setLayoutData(data);
 
        m_playerFrame = createPlayerWindow(composite);
    }

    /**
	 * Dispose of widget resources.
	 * 
	 * @param event The dispose event.
	 * 
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent event)
	{
		// Clean up Rehearsal Player ActiveX component.
        if (m_playerActivated)
        {
			m_playerControlSite.deactivateInPlaceClient();
			m_playerActivated = false;

        }
		if (m_playerOleAutomation != null)
		{
		    m_playerOleAutomation.dispose();
		    m_playerOleAutomation = null;
		}
		if (m_playerFrame != null)
		{
			m_playerFrame.dispose();
			m_playerFrame = null;
		}
	}
	
	/**
	 * Set the Digital Workprint that the Rehearsal Player should play.
	 * 
	 * <pre>
	 *	Arguments:
	 *		dwp [filename]
	 *
	 *	Any filename containing spaces must be quoted.
	 *
	 *  Example:
	 *		<code>"\"C:\\Program Files\\Wizzer Works\\Magic Lantern\\sample\\mtea\\mtea.dwp\""</code>
	 * </pre>
	 *
	 * @param dwp The digital workprint.
	 * 
	 * @return
	 */
	public boolean setDigitalWorkprint(String dwp)
	{
		Variant vReturn = null;
		
		// Make sure that the DWP is in its canonical form.
		String path = DwpPath.canonicalPath(dwp,true);
		
		try
		{
			int[] rgdispid = m_playerOleAutomation.getIDsOfNames(new String[]{"setDigitalWorkprint", "dwp"}); 
			int dispIdMember = rgdispid[0];
			Variant[] rgvarg = new Variant[1];
			rgvarg[0] = new Variant(path);
			int[] rgdispidNamedArgs = new int[1];
			rgdispidNamedArgs[0] = rgdispid[1];
			vReturn = m_playerOleAutomation.invoke(dispIdMember, rgvarg, rgdispidNamedArgs);
		}
		catch (Exception ex)
		{
			System.out.println("Unable to set Digital Workprint: " + ex.getMessage());
		}
		
		return vReturn != null;
	}

	/**
	 * Initialize the Rehearsal Player.
	 *
	 * <pre>
	 *	Arguments:
	 *		-cwd [current working directory]
	 *		-config [filename]
	 *		-properties [filename]
	 *		-log [filename]
	 *
	 *	Any filename containing spaces must be quoted.
	 *
	 *  Example:
	 *		<code>"-config \"C:\\Program Files\\Wizzer Works\\Magic Lantern\\config.ini\""</code>
	 * </pre>
	 * 
	 * @param commandLine String of command line arguments separated by spaces.
	 *
	 * @return <b>true</b> is returned if the operation was successful. Otherwise,
	 * <b>false</b> will be returned.
	 */
	public boolean initPlayer(String commandLine)
	{
		Variant vReturn = null;
		
		try
		{
			int[] rgdispid = m_playerOleAutomation.getIDsOfNames(new String[]{"init", "cmdline"}); 
			int dispIdMember = rgdispid[0];
			Variant[] rgvarg = new Variant[1];
			rgvarg[0] = new Variant(commandLine);
			int[] rgdispidNamedArgs = new int[1];
			rgdispidNamedArgs[0] = rgdispid[1];
			vReturn = m_playerOleAutomation.invoke(dispIdMember, rgvarg, rgdispidNamedArgs);

			//int[] rgdispid = m_playerOleAutomation.getIDsOfNames(new String[]{"init"}); 
			//int dispIdMember = rgdispid[0];
			//vReturn = m_playerOleAutomation.invoke(dispIdMember);			
		}
		catch (Exception ex)
		{
			System.out.println("Unable to initialize Rehearsal Player: " + ex.getMessage());
		}
		
		return vReturn != null;
	}
	
	/**
	 * Execute one cycle of the player execution.
	 * 
	 * @return <b>true</b> is returned if the execution was successful.
	 * Otherwise <b>false</b> is returned.
	 */
	public boolean execPlayerCycle()
	{
		Variant vReturn = null;
		
		try
		{
			int[] rgdispid = m_playerOleAutomation.getIDsOfNames(new String[]{"run"}); 
			int dispIdMember = rgdispid[0];
			vReturn = m_playerOleAutomation.invoke(dispIdMember);
		}
		catch (Exception ex)
		{
			System.out.println("Unable to execute Rehearsal Player: " + ex.getMessage());
		}
		
		return vReturn != null;
	}
	
	// Create the Rehearsal Player widget.
	private OleFrame createPlayerWindow(Composite parent)
	{
	    // Create the ActiveX control.
	    OleFrame playerFrame = new OleFrame(parent, SWT.BORDER);
	    
	    IControlSite controlSite = ControlSiteFactory.getInstance().createControlSite(
	    	IControlSiteAdapter.OLE_CONTROL_SITE, playerFrame, SWT.NONE, "RehearsalPlayer.AuStage");
	    m_playerControlSite = (OleClientSite) controlSite;
		//m_playerControlSite =
		//    new OleControlSiteEx(playerFrame, SWT.NONE, "RehearsalPlayer.AuStage");
		playerFrame.setSize(640, 480);
		playerFrame.setFocus();
		
		m_playerOleAutomation = new OleAutomation(m_playerControlSite);

		// Activate the ActiveX control "in place".
		m_playerActivated = (m_playerControlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE) == OLE.S_OK);

		return playerFrame;

	}
}
