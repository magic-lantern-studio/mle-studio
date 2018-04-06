/*
 * RehearsalPlayerView.java
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
package com.wizzer.mle.studio.rehearsal.view;

// Import Eclipse classes.
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.graphics.Point;

// Import Digital Workprint classes.
import com.wizzer.mle.studio.dwp.DwpPath;
import com.wizzer.mle.studio.framework.ext.ControlSiteFactory;
import com.wizzer.mle.studio.framework.ext.IControlSite;
import com.wizzer.mle.studio.framework.ext.IControlSiteAdapter;

// Import Rehearsal Player classes.
import com.wizzer.mle.studio.rehearsal.RehearsalLog;
import com.wizzer.mle.studio.rehearsal.RehearsalPlugin;


/**
 * This class implements an Eclipse view for the Rehearsal Player.
 * 
 * @author Mark S. Millard
 */
public class RehearsalPlayerView extends ViewPart
{
	// The unique identifier for the Rehearsal Player View.
	static final private String REHEARSALPLAYER_MAIN_VIEW = "com.wizzer.mle.studio.rehearsal.view.RehearsalPlayerView";

	// The top widget.
	private ScrolledComposite m_scrolledComposite;
	// The OLE frame for the Rehearsal Player ActiveX component.
	private OleFrame m_frame;
	// The OLE control site for the Rehearsal Player ActiveX component.
	private OleClientSite m_controlSite;
	// The OLE automation for the Rehearsal Player ActiveX component.
	private OleAutomation m_oleAutomation;
	// Flag indicating activation state.
	private boolean m_activated;

	/**
	 * The default constructor.
	 */
	public RehearsalPlayerView()
	{
		super();
		// Do nothing extra.
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 * 
	 * @param parent The parent <code>Composite</code> widget.
	 */
	public void createPartControl(Composite parent)
	{
		try
		{
			// Create scrolling window.
			m_scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
			
			// Create the ActiveX control.
			m_frame = new OleFrame(m_scrolledComposite, SWT.NONE);
			
			IControlSite controlSite = ControlSiteFactory.getInstance().createControlSite(
			    IControlSiteAdapter.OLE_CONTROL_SITE, m_frame, SWT.NONE, "RehearsalPlayer.AuStage");
			m_controlSite = (OleClientSite) controlSite;
			//m_controlSite = new OleControlSiteEx(m_frame, SWT.NONE, "RehearsalPlayer.AuStage");
			m_oleAutomation = new OleAutomation(m_controlSite);
						
			// In-place activate the ActiveX control.
			m_activated = (m_controlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE) == OLE.S_OK);
			
			// Set frame and size.
			Point point = controlSite.getExtent();
			point.x = 640;
			point.y = 480;
			m_frame.setSize(point.x, point.y);
			m_scrolledComposite.setContent(m_frame);
			m_frame.setFocus();

		} catch (Exception ex)
		{
			RehearsalLog.logError(ex,"Unable to create RehearsalPlayer.AuStage.");
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()
	{
		m_frame.setFocus();
	}
	
	/**
	 * Dispose of Rehearsal Player components.
	 */
    public void dispose()
    {
		if (m_activated)
		{
			m_controlSite.deactivateInPlaceClient();
			m_activated = false;
		}
		if (m_oleAutomation != null)
		{
			m_oleAutomation.dispose();
			m_oleAutomation = null;
		}
    	super.dispose();
    	
    	// Close all Rehearsal Player views if this view is closing.
		RehearsalPlugin.getDefault().closeViews();
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
			int[] rgdispid = m_oleAutomation.getIDsOfNames(new String[]{"setDigitalWorkprint", "dwp"}); 
			int dispIdMember = rgdispid[0];
			Variant[] rgvarg = new Variant[1];
			rgvarg[0] = new Variant(path);
			int[] rgdispidNamedArgs = new int[1];
			rgdispidNamedArgs[0] = rgdispid[1];
			vReturn = m_oleAutomation.invoke(dispIdMember, rgvarg, rgdispidNamedArgs);
		}
		catch (Exception ex)
		{
			RehearsalLog.logError(ex,"Unable to set Digital Workprint.");
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
			int[] rgdispid = m_oleAutomation.getIDsOfNames(new String[]{"init", "cmdline"}); 
			int dispIdMember = rgdispid[0];
			Variant[] rgvarg = new Variant[1];
			rgvarg[0] = new Variant(commandLine);
			int[] rgdispidNamedArgs = new int[1];
			rgdispidNamedArgs[0] = rgdispid[1];
			vReturn = m_oleAutomation.invoke(dispIdMember, rgvarg, rgdispidNamedArgs);
		}
		catch (Exception ex)
		{
			RehearsalLog.logError(ex,"Unable to initialize Rehearsal Player.");
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
			int[] rgdispid = m_oleAutomation.getIDsOfNames(new String[]{"run"}); 
			int dispIdMember = rgdispid[0];
			vReturn = m_oleAutomation.invoke(dispIdMember);
		}
		catch (Exception ex)
		{
			RehearsalLog.logError(ex,"Unable to execute Rehearsal Player.");
		}
		
		return vReturn != null;
	}
    
	public Control getControl() 
	{
		return m_frame;
	}
	
	/**
	 * Determine whether the Rehearsal Player ActiveX control is activated.
	 * 
	 * @return <b>true</b> is returned if the Rehearsal Player ActiveX control
	 * is activated. Otherwise <b>false</b> will be returned.
	 */
	public boolean isActivated()
	{
		return m_activated;
	}
    
	/**
	 * Get the unique identifier for the Rehearsal Player Main View.
	 * 
	 * @return A String is returned representing the ID of the Eclipse IDE view.
	 */
	static public String getID()
	{
		return REHEARSALPLAYER_MAIN_VIEW;
	}
}
