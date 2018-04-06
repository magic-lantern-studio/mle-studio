/*
 * GengroupBuildVisitor.java
 * Created on Jun 20, 2005
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

// Import Eclipse classes.
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IStatus;

// Import Digital Workprint classes.
import com.wizzer.mle.studio.dwp.DwpUtilities;

// Import Digital Playprint classes.
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.DppGengroup;
import com.wizzer.mle.studio.dpp.DppLog;
import com.wizzer.mle.studio.dpp.DppPlugin;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GengroupPropertyManager;

/**
 * This class is used to execute the Magic Lantern mastering command
 * <i>gengroup</i> on resources that are Digital Workprints.
 * 
 * @author Mark S. Millard
 */
public class GengroupBuildVisitor implements IResourceVisitor
{
    // The gengroup command.
    private DppGengroup m_command = null;
    // The progress monitor.
    IProgressMonitor m_monitor = null;
    
    /**
     * The constructor that specifies a progress monitor.
     * 
     * @param monitor The progress monitor.
     */
    public GengroupBuildVisitor(IProgressMonitor monitor)
    {
        super();
        m_monitor = monitor;
    }

    /**
     * Visits the given resource and runs <b>gengroup</b> on it if it is a
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
	                    m_monitor.beginTask("Executing gengroup.", 3);
	                    
	                    // Create a gengroup command.
		                m_command = new GengroupBuildCommand(resource).getCommand();

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
		                	    "Gengroup builder failed.",null));
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
            DppLog.logError(ex,"Unable to run gengroup on " + resource.getName() + ".");
        }

        return retValue;
    }
    
    // A utility for filtering chunk files.
    private class ChunkFileFilter implements FilenameFilter
    {
        public boolean accept(File dir, String name)
        {
            if (name.endsWith(".chk"))
            	return true;
            else
                return false;
        }
    }
    
    // Mark generated assets as derived elements in the project.
    private void setDerivedAssets(IResource dwp) throws DppException
    {
        IProject project = dwp.getProject();
        
        try
        {
	        // Process common mastering assets.
            String outputDir = MasteringProjectManager.getInstance().getDestinationDirValue(dwp);
            
	        // Process gengroup assets.
	        String actoridFile = GengroupPropertyManager.getInstance().getActorIdValue(dwp);
	        String member = new String(outputDir + "/" + actoridFile);
	        IResource resource = project.findMember(member);
	        if (resource != null)
	            resource.setDerived(true);

	        String groupidFile = GengroupPropertyManager.getInstance().getGroupIdValue(dwp);
	        member = new String(outputDir + "/" + groupidFile);
	        resource = project.findMember(member);
	        if (resource != null)
	            resource.setDerived(true);
	        
	        // Process chunk files.
	        File projectPath = project.getLocation().toFile();
	        File dir = new File(projectPath.getAbsolutePath() + "/" + outputDir);
	        String[] chunkFiles = dir.list(new ChunkFileFilter());
	        for (int i = 0; i < chunkFiles.length; i++)
	        {
	            member = new String(outputDir + "/" + chunkFiles[i]);
	            resource = project.findMember(member);
		        if (resource != null)
		            resource.setDerived(true);
	        }

        } catch (CoreException ex)
        {
            throw new DppException("Unable to set derived asset.",ex);
        }
    }

}
