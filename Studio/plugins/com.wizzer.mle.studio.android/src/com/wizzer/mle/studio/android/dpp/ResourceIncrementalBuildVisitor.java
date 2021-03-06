/*
 * ResourceIncrementalBuildVisitor.java
 * Created on Feb 5, 2008
 */

// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2008  Wizzer Works
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
package com.wizzer.mle.studio.android.dpp;

// Import Eclipse classes.

// Import Eclipse classes.
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

// Import Digital Playprint classes.
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;


/**
 * This class is used to execute the Magic Lantern mastering step
 * to manipulate Android resources.
 * 
 * @author Mark S. Millard
 */
public class ResourceIncrementalBuildVisitor implements IResourceDeltaVisitor
{
    // The progress monitor.
    IProgressMonitor m_monitor = null;

    /**
     * The constructor that specifies a progress monitor.
     * 
     * @param monitor The progress monitor.
     */
    public ResourceIncrementalBuildVisitor(IProgressMonitor monitor)
    {
        super();
        m_monitor = monitor;
    }

    /**
     * Visits the given resource delta.
     * 
     * @param delta The resource delta being visited.
     * 
     * @return <b>true</b> is returned if the resource delta's children should be visited;
     * <b>false></b> is returned if they should be skipped.
     * 
     * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
     */
    public boolean visit(IResourceDelta delta) throws CoreException
    {
        boolean retValue = true;
        IResource resource = null;
        
        // Determine what the delta is.
        switch (delta.getKind())
        {
            case IResourceDelta.CHANGED:
                // Handle modified resource.
                resource = delta.getResource();
                break;
        }
        
        if ((resource != null) && (resource instanceof IFile) &&
        	(resource.getFileExtension() != null) &&
        	(resource.getFileExtension().equals("dpp")))
        {
            IProject project = resource.getProject();
            
            if (MasteringProjectManager.getInstance().isDppProject(project))
            {
                // Begin monitoring.
                m_monitor.beginTask("Copying Digital Playprint.", 2);
                
                // Copy the playprint into the raw resource directory.
                IPath dest = project.getFullPath();
                dest = dest.append("/");
                dest = dest.append(ResourceBuilder.PLAYPRINT_RESOURCE_DIR);
                dest = dest.append("/");
                dest = dest.append(ResourceBuilder.PLAYPRINT_FILENAME);
                
                // Make sure the resource we are attempting to copy is not itself.
                if (! resource.getFullPath().equals(dest))
                {
                    // Remove existing resource if it currently exists.
	                IFile playprint = project.getFile(ResourceBuilder.PLAYPRINT_RESOURCE_DIR +
	                	"/" + ResourceBuilder.PLAYPRINT_FILENAME);
	                if (playprint.exists())
	                	playprint.delete(true, null);
	                
	                int updateFlags = IResource.DERIVED;
	                resource.copy(dest, updateFlags, m_monitor);
                }

                if (m_monitor.isCanceled())
                    return false;
                m_monitor.worked(1);
                
                // Refresh the project so the generated source shows up
                // in the workspace.
                project.refreshLocal(IResource.DEPTH_INFINITE,m_monitor);

                if (m_monitor.isCanceled())
                    return false;
                m_monitor.worked(1);
                
                // End monitoring.
                m_monitor.done();
                
                retValue = true;
            }
        }

        return retValue;
    }
}
