/*
 * GenmediaBuildVisitor.java
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
	            resource.setDerived(true,null);

        } catch (CoreException ex)
        {
            throw new DppException("Unable to set derived asset.",ex);
        }
    }

}
