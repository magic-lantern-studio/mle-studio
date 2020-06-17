/*
 * MleJavaCapabilityConfigurationPage.java
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
package com.wizzer.mle.studio.project;

// Import Eclipse packages.
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.launching.JavaRuntime;

// Import Magic Lantern packages.
import com.wizzer.mle.studio.MleLog;
import com.wizzer.mle.studio.MlePlugin;

/**
 * This class is used to configure a Magic Lantern Java project.
 * 
 * @author Mark S. Millard
 */
public class MleJavaCapabilityConfigurationPage
	extends JavaCapabilityConfigurationPage
{
    public static String MLE_ROOT_DEFAULT = "<Unknown MLE_ROOT>";

	// The main project resource.
	private NewJavaProjectWizardPageOne m_projectPage = null;
	// The configuration element.
	private IConfigurationElement m_configurationElement = null;
	
	/**
	 * The default constructor should not be called directly. It is required
	 * by the Eclipse Workbench construction.
	 */
	public MleJavaCapabilityConfigurationPage()
	{
		super();
		
		if (MlePlugin.isLinux()) {
			MLE_ROOT_DEFAULT = "/opt/MagicLantern";
		} else if (MlePlugin.isWindows()) {
			MLE_ROOT_DEFAULT = "C:\\Program Files\\Wizzer Works\\MagicLantern";
		} else
			MLE_ROOT_DEFAULT = "<Unknown MLE_ROOT>";
	}
	
	/**
	 * A constructor that associates the main project resource with this
	 * capability configuration page.
	 */
	public MleJavaCapabilityConfigurationPage(NewJavaProjectWizardPageOne page,IConfigurationElement element)
	{
		super();
		m_projectPage = page;
		m_configurationElement = element;
	}

	/**
	 * Sets the visibility of this dialog page.
	 * 
	 * @param visible <b>true</b> to make this page visible,
	 * and <b>false</b> to hide it.
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible)
	{
		// Need to override this method in order to react to changes on
		// the new project page.
		updatePage();
		super.setVisible(visible);
	}

	/**
	 * Update the JavaCapabilityConfigurationPage by initializing the output
	 * location for the compiled Magic Lantern classes. Also, set the 
	 * default classpath.
	 */
	public void updatePage()
	{
		try
		{
			IJavaProject jproject = this.getJavaProject();
			if (jproject == null)
			{
				IProject project = this.getProjectHandle();
				URI projectLocation = m_projectPage.getProjectLocationURI();
				JavaCapabilityConfigurationPage.createProject(project, projectLocation, null);
				jproject = this.getJavaProject();
			}
		
			// Specify the default output location.
			IPath outputLocation = jproject.getPath();
			outputLocation = outputLocation.append("/bin");
			
			// Specify the default source location.
			IPath sourceLocation = jproject.getPath();
			sourceLocation = sourceLocation.append("/src");
			IClasspathEntry sourceEntry = JavaCore.newSourceEntry(sourceLocation);
			
			// Specify a classpath variable.
			IPath toolrootPath = null;
			String toolrootEnv = System.getProperty("MLE_ROOT");
			if (toolrootEnv != null)
				toolrootPath = new Path(toolrootEnv);
			else
			    toolrootPath = new Path(MLE_ROOT_DEFAULT);
			
			JavaCore.setClasspathVariable("MLE_ROOT",toolrootPath,null);
			
			// Specify the required Magic Lantern libraries.
			String rtJarLocation = new String("MLE_ROOT");
			rtJarLocation = rtJarLocation + "/lib/java/runtime/mlert.jar";
			IPath runtimeJar = new Path(rtJarLocation);
			IClasspathEntry rtJarEntry = JavaCore.newVariableEntry(runtimeJar,null,null);
			
			String partsJarLocation = new String("MLE_ROOT");
			partsJarLocation = partsJarLocation + "/lib/java/runtime/parts.jar";
			IPath partsJar = new Path(partsJarLocation);
			IClasspathEntry partsJarEntry = JavaCore.newVariableEntry(partsJar,null,null);
			
			String actorsJarLocation = new String("MLE_ROOT");
			actorsJarLocation = actorsJarLocation + "/lib/java/runtime/actors.jar";
			IPath actorsJar = new Path(actorsJarLocation);
			IClasspathEntry actorsJarEntry = JavaCore.newVariableEntry(actorsJar,null,null);

			String rolesJarLocation = new String("MLE_ROOT");
			rolesJarLocation = rolesJarLocation + "/lib/java/runtime/roles.jar";
			IPath rolesJar = new Path(rolesJarLocation);
			IClasspathEntry rolesJarEntry = JavaCore.newVariableEntry(rolesJar,null,null);

			String propsJarLocation = new String("MLE_ROOT");
			propsJarLocation = propsJarLocation + "/lib/java/runtime/props.jar";
			IPath propsJar = new Path(propsJarLocation);
			IClasspathEntry propsJarEntry = JavaCore.newVariableEntry(propsJar,null,null);

			String mrefsJarLocation = new String("MLE_ROOT");
			mrefsJarLocation = mrefsJarLocation + "/lib/java/runtime/mrefs.jar";
			IPath mrefsJar = new Path(mrefsJarLocation);
			IClasspathEntry mrefsJarEntry = JavaCore.newVariableEntry(mrefsJar,null,null);

			String msetsJarLocation = new String("MLE_ROOT");
			msetsJarLocation = msetsJarLocation + "/lib/java/runtime/sets.jar";
			IPath setsJar = new Path(msetsJarLocation);
			IClasspathEntry setsJarEntry = JavaCore.newVariableEntry(setsJar,null,null);

			String stagesJarLocation = new String("MLE_ROOT");
			stagesJarLocation = stagesJarLocation + "/lib/java/runtime/stages.jar";
			IPath stagesJar = new Path(stagesJarLocation);
			IClasspathEntry stagesJarEntry = JavaCore.newVariableEntry(stagesJar,null,null);

			String mathJarLocation = new String("MLE_ROOT");
			mathJarLocation = mathJarLocation + "/lib/java/runtime/mlmath.jar";
			IPath mathJar = new Path(mathJarLocation);
			IClasspathEntry mathJarEntry = JavaCore.newVariableEntry(mathJar,null,null);
			
			// Add JRE libraries.
			IClasspathEntry jreEntry = JavaCore.newContainerEntry(
			    new Path(JavaRuntime.JRE_CONTAINER),false);
			
			// Build up the list of classpath entries.
			IClasspathEntry[] projectClasspath = new IClasspathEntry[11];
			projectClasspath[0] = sourceEntry;
			projectClasspath[1] = rtJarEntry;
			projectClasspath[2] = partsJarEntry;
			projectClasspath[3] = actorsJarEntry;
			projectClasspath[4] = rolesJarEntry;
			projectClasspath[5] = propsJarEntry;
			projectClasspath[6] = mrefsJarEntry;
			projectClasspath[7] = setsJarEntry;
			projectClasspath[8] = stagesJarEntry;
			projectClasspath[9] = mathJarEntry;
			projectClasspath[10] = jreEntry;
			
			// Initialize the Java project.
			init(jproject, outputLocation, projectClasspath, false);
		} catch (JavaModelException ex)
		{
			MleLog.logError(ex,"Unable to set Classpath variable MLE_ROOT.");
		} catch (CoreException ex)
		{
			MleLog.logError(ex,"Unable to create Java project.");
		}
	}

	/**
	 * Get the configuration element of this page.
	 * 
	 * @return Returns the <code>IConfigurationElement</code> that was set
	 * during construction.
	 */
	public IConfigurationElement getConfigurationElement()
	{
		return m_configurationElement;
	}
	
	/**
	 * Get the project handle.
	 * 
	 * @return A reference to the project handle resource is returned.
	 */
	private IProject getProjectHandle()
	{
		IWorkspace workspace = JavaPlugin.getWorkspace();
		String name = m_projectPage.getProjectName();
		IProject handle = workspace.getRoot().getProject(name);
		return handle;
	}

}
