/*
 * GenmediaBuildVisitor.java
 * Created on Jul 26, 2005
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
package com.wizzer.mle.studio.dpp.builder;

// Import standard Java classes.
import java.io.FileNotFoundException;
import java.io.IOException;

// Import Eclipse classes.
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

// Import Digital Workprint classes.
import com.wizzer.mle.studio.dwp.DwpUtilities;

// Import Digital Playprint classes.
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.DppGenmedia;
import com.wizzer.mle.studio.dpp.DppLog;
import com.wizzer.mle.studio.dpp.DppPlugin;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GenmediaPropertyManager;

/**
 * This class is used to execute the Magic Lantern mastering command
 * <i>genmedia</i> on resources that are Digital Workprints.
 * 
 * @author Mark S. Millard
 */
public class GenmediaBuildVisitor implements IResourceVisitor
{
    // The genmedia command.
    private DppGenmedia m_command = null;
    // The progress monitor.
    IProgressMonitor m_monitor = null;
    
    /**
     * The constructor that specifies a progress monitor.
     * 
     * @param monitor The progress monitor.
     */
    public GenmediaBuildVisitor(IProgressMonitor monitor)
    {
        super();
        m_monitor = monitor;
    }

    /**
     * Visits the given resource and runs <b>genmedia</b> on it if it is a
     * Digital Workprint.
     * 
     * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources.IResource)
     */
    public boolean visit(IResource resource) throws CoreException
    {
        boolean retValue = true;
        
        try
        {
	        if ((resource != null) && (resource instanceof IFile) &&
	        	(resource.getFileExtension() != null) &&
	        	(resource.getFileExtension().equals("dwp")))
	        {
	            if (DwpUtilities.isDigitalWorkprint((IFile)resource))
	            {
	                IProject project = resource.getProject();
	                
	                if (MasteringProjectManager.getInstance().isDppProject(project))
	                {
	                    // Begin monitoring.
	                    m_monitor.beginTask("Executing genmedia.", 3);
	                    
	                    // Create a gengroup command.
		                m_command = new GenmediaBuildCommand(resource).getCommand();
		                
		                if (m_monitor.isCanceled())
		                    return false;
		                m_monitor.worked(1);
		                
		                // Execute the command.
		                int result = m_command.exec();
		                if (result != 0)
		                {
		                	// Terminate the monitor.
		                	m_monitor.done();
		                	throw new CoreException(new Status(IStatus.ERROR,
		                	    DppPlugin.getID(),result,
		                	    "Genmedia builder failed.",null));
		                }

		                if (m_monitor.isCanceled())
		                    return false;
		                m_monitor.worked(1);
		                
		                // Refresh the project so the generated source shows up
		                // in the workspace.
		                project.refreshLocal(IResource.DEPTH_INFINITE,m_monitor);
		                setDerivedAssets(resource);

		                if (m_monitor.isCanceled())
		                    return false;
		                m_monitor.worked(1);
		                
		                // End monitoring.
		                m_monitor.done();
		                
		                retValue = true;
	                }
	            }
	        }
        } catch (FileNotFoundException ex)
        {
            DppLog.logError(ex,"File not found: " + resource.getName());
        } catch (IOException ex)
        {
            DppLog.logError(ex,"IO Error: " + resource.getName());
        } catch (DppException ex)
        {
            DppLog.logError(ex,"Unable to run genmedia on " + resource.getName() + ".");
        }

        return retValue;
    }
    
    // Mark generated assets as derived elements in the project.
    private void setDerivedAssets(IResource dwp) throws DppException
    {
        IProject project = dwp.getProject();
        
        try
        {
	        // Process common mastering assets.
            String outputDir = MasteringProjectManager.getInstance().getDestinationDirValue(dwp);
	        
	        // Process genmedia assets.
	        String bomFile = GenmediaPropertyManager.getInstance().getBomValue(dwp);
	        String member = new String(outputDir + "/" + bomFile);
	        IResource resource = project.findMember(member);
	        if (resource != null)
	            resource.setDerived(true);

        } catch (CoreException ex)
        {
            throw new DppException("Unable to set derived asset.",ex);
        }
    }

}
