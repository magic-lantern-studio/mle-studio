/*
 * GenmediaBuilder.java
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
import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

// Import Eclipse classes.
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

// Import Magic Lantern Digital Workprint classes.
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.DppLog;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GenmediaPropertyManager;

/**
 * This class is an incremental project builder for the Magic Lantern
 * mastering step, <i>genmedia</i>.
 * 
 * @author Mark S. Millard
 */
public class GenmediaBuilder extends IncrementalProjectBuilder
{
    /**
     * The default constructor.
     */
    public GenmediaBuilder()
    {
        super();
    }

    /**
     * Build using the Magic Lantern <i>genmedia</i> command utiltity.
     * 
     * @param kind The kind of build being requested. Valid values are:
     * <ul>
     * <li>FULL_BUILD- indicates a full build.</li>
     * <li>INCREMENTAL_BUILD- indicates an incremental build.</li>
     * <li>AUTO_BUILD- indicates an automatically triggered incremental build (auto-building on).</li>
     * </ul>
     * @param args A table of builder-specific arguments keyed by argument name
     * (key type: <code>String</code>, value type: <code>String</code>);
     * <b>null</b> is equivalent to an empty map.
     * @param monitor A progress monitor, or <b>null</b> if progress reporting and cancellation are not desired.
     * 
     * @return The list of projects for which this builder would like deltas
     * the next time it is run or <b>null</b> if none.
     */
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
        throws CoreException
    {
        if (kind == IncrementalProjectBuilder.FULL_BUILD)
        {
            fullBuild(monitor);
        } else
        {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null)
            {
                fullBuild(monitor);
            } else
            {
                incrementalBuild(delta, monitor);
            }
        }
        return null;
    }

    /**
     * Perform a full build on the project.
     * 
     * @param monitor A progress monitor to use during the build.
     * 
     * @throws CoreException This exception is thrown if an error occurs
     * while running <i>genmedia</i>.
     */
    protected void fullBuild(final IProgressMonitor monitor) throws CoreException
    {
        try
        {
            getProject().accept(new GenmediaBuildVisitor(monitor));
        } catch (CoreException ex)
        {
            DppLog.logError(ex,"Error occurred while performing a full build.");
        }
    }

    /**
     * Perform an incremental build on the project.
     * 
     * @param delta The delta of resources.
     * @param monitor A progress monitor to use during the build.
     * 
     * @throws CoreException This exception is thrown if an error occurs
     * while running <i>genmedia</i>.
     */
    protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor)
        throws CoreException
    {
         // The visitor does the work.
         delta.accept(new GenmediaIncrementalBuildVisitor(monitor));
    }

    // A utility for filtering chunk files.
    // XXX - Should limit itself to just MediaRef chunks derived from
    // Digital Workprint.
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

    /**
     * Remove the derived assets generated by <i>genmedia</i>.
     *
     * @param monitor A progress monitor to use during the build.
     */
    protected void clean(IProgressMonitor monitor)
    {
    	IProject project = getProject();
    	
    	String message = "Cleaning genmedia artifacts.\n";
    	DppLog.logConsole(message);
    	DppLog.logInfo(message);
    	
    	try
    	{
    	    String destDir = MasteringProjectManager.getInstance().getDestinationDirValue(project);
    	    
    	    // Remove the MediaRef chunks.
    	    IResource resource;
    	    String member;
	        File projectPath = project.getLocation().toFile();
	        File dir = new File(projectPath.getAbsolutePath() + "/" + destDir);
	        String[] chunkFiles = dir.list(new ChunkFileFilter());
	        if (chunkFiles != null)
	        {
		        for (int i = 0; i < chunkFiles.length; i++)
		        {
		            member = new String(destDir + "/" + chunkFiles[i]);
		            resource = project.findMember(member);
			        if (resource != null)
			            resource.delete(true,monitor);
		        }
	        }

    	    // Remove the generated BOM file.
    	    String bom = GenmediaPropertyManager.getInstance().getBomValue(project);
    	    String filename = new String(destDir + "/" + bom);
    	    IFile file = project.getFile(filename);
    	    file.delete(true,false,monitor);
    	} catch (DppException ex)
    	{
    		DppLog.logError(ex,"Error occurred while performing a clean.");
    	} catch (CoreException ex)
    	{
    		DppLog.logError(ex,"Error occurred while deleting genmedia artifacts.");
    	}
     }

}
