// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.java2d.launch;

//Import Eclipse packages.
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

import com.wizzer.mle.studio.java2d.Java2DLog;

/**
 * DPPLaunchShortcut is a launch shortcut that is used to launch Magic Lantern
 * titles.
 * 
 * @author Mark Millard
 */
public class DPPLaunchShortcut implements ILaunchShortcut {

	// Extract a Resource element from the specified selection.
	private IResource extractSelection(ISelection sel) {
		// The selection must be structured.
	    if (! (sel instanceof IStructuredSelection))
	        return null;
	    
	    // Check if the first element in the selection is a Resource.
	    // If it is, then return it.
	    IStructuredSelection ss = (IStructuredSelection) sel;
	    Object element = ss.getFirstElement();
	    if (element instanceof IResource)
	        return (IResource) element;
	    
	    // Check if the first element is an Adaptable object.
	    // If it is, then return the adapter's resource.
	    if (! (element instanceof IAdaptable))
	        return null;
	    IAdaptable adaptable = (IAdaptable)element;
	    Object adapter = adaptable.getAdapter(IResource.class);
	    
	    return (IResource) adapter;
	}

	@Override
	public void launch(ISelection selection, String mode) {
		Java2DLog.logInfo("DPPLaunchShortcut: Launching Magic Lantern project selection in " + mode + " mode.");

		ILaunchConfiguration config = null;

		try {
			// Get launch configurations.
		    ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
		    ILaunchConfigurationType lct = mgr.getLaunchConfigurationType("com.wizzer.mle.studio.java2d.launch.Java2DRuntimeLaunchConfigurationType");
			ILaunchConfiguration[] lcs = mgr.getLaunchConfigurations(lct);
		
	    	// Get the project name for the selection.
		    IResource resource = extractSelection(selection);
		    if (resource == null) {
			    Java2DLog.logWarning("DPPLaunchShortcut: Unable to launch configuration for Magic Lantern project");
			    return;
		    }
		    IProject project = resource.getProject();
		    String projectName = project.getName();

			// Check if an existing launch configuration currently exists. If it does, then
		    // use it. The name of the configuration to look for will be based on the name
		    // of the enclosing project.
		    for (ILaunchConfiguration nextConfig : lcs) {
				if (projectName.equals(nextConfig.getName())) {
					config = nextConfig;
					break;
				}
		    }

		    // Create a new launch configuration if necessary.
		    if (config == null) {
			    IContainer container = null;
			    ILaunchConfigurationWorkingCopy wc = lct.newInstance(container, projectName);
			    
			    // Set launch configuration attributes here.
			    String main = projectName.toLowerCase();
			    main = main.concat(".title.");
			    main = main.concat(projectName);
			    String args = "src/gen/";
			    args = args.concat(projectName);
			    args = args.concat(".dpp");
			    wc.setAttribute(
			        IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, 
			    	projectName);
			    wc.setAttribute(
			        IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, 
			        main);
			    wc.setAttribute(
			    	IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
			    	args);
			    
			    // Save the new configuration.
			    config =  wc.doSave();
		    }
		    
			// Create a progress monitor for the launch.
			NullProgressMonitor monitor = new NullProgressMonitor();
			
			// Launch the configuration.
			if (mode.equals("run")) {
				config.launch(ILaunchManager.RUN_MODE, monitor);
			} else if (mode.equals("debug")) {
				config.launch(ILaunchManager.DEBUG_MODE, monitor);
			} else {
				Java2DLog.logWarning("DPPLaunchShortcut: Invalid launch configuration mode");
			}
		} catch (CoreException ex) {
			Java2DLog.logError(ex, "DPPLaunchShortcut: Unable to launch configuration for Magic Lantern project");
			ex.printStackTrace();
			return;
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		Java2DLog.logInfo("DPPLaunchShortcut: Launching Magic Lantern project editor in " + mode + " mode.");
		// TODO: Complete when a Magic Lantern Studio feature requires the shortcut.
	}

}
