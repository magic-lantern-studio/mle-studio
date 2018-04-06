/*
 * GendppBuilder.java
 * Created on Jul 27, 2005
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
import java.util.Map;

// Import Eclipse classes.
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

// Import Magic Lantern Digital Workprint classes.
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.DppLog;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GenppscriptPropertyManager;

/**
 * This class is an incremental project builder for the Magic Lantern
 * mastering step, <i>gendpp</i>.
 * 
 * @author Mark S. Millard
 */
public class GendppBuilder extends IncrementalProjectBuilder
{
    /**
     * The default constructor.
     */
    public GendppBuilder()
    {
        super();
    }

    /**
     * Build using the Magic Lantern <i>gendpp</i> command utiltity.
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
     * while running <i>gendpp</i>.
     */
    protected void fullBuild(final IProgressMonitor monitor) throws CoreException
    {
        try
        {
            getProject().accept(new GendppBuildVisitor(monitor));
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
     * while running <i>gendpp</i>.
     */
    protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor)
        throws CoreException
    {
         // The visitor does the work.
         delta.accept(new GendppIncrementalBuildVisitor(monitor));
    }

    /**
     * Remove the derived assets generated by <i>genppscript</i>.
     *
     * @param monitor A progress monitor to use during the build.
     */
    protected void clean(IProgressMonitor monitor)
    {
    	IProject project = getProject();
    	
    	try
    	{
    	    String destDir = MasteringProjectManager.getInstance().getDestinationDirValue(project);
    	    
    	    // Remove the generated DPP file.
    	    String dpp = GenppscriptPropertyManager.getInstance().getDppValue(project);
    	    String filename = new String(destDir + "/" + dpp);
    	    IFile file = project.getFile(filename);
    	    file.delete(true,false,monitor);
    	} catch (DppException ex)
    	{
    		DppLog.logError(ex,"Error occurred while performing a clean.");
    	} catch (CoreException ex)
    	{
    		DppLog.logError(ex,"Error occurred while deleting genppscript artifacts.");
    	}
     }

}
