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
package com.wizzer.mle.studio.natures;

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

// Import Magic Lantern plugins.
import com.wizzer.mle.studio.MlePlugin;

/**
 * The Magic Lantern project Eclipse nature.
 * <p>
 * Note that this should not conflict with other frameworks.
 * </p>
 */
public class MleStudioNature implements IProjectNature
{
	// The associated project.
    private IProject m_project;
    
    /** The nature id. */
    public static final String NATURE_ID = MlePlugin.getID() + ".natures.MleStudioNature";

    /**
     * @see org.eclipse.core.resources.IProjectNature#configure()
     */
    public void configure() throws CoreException
    {
       // Using java builder, at least for now.
    }

    /**
     * @see org.eclipse.core.resources.IProjectNature#deconfigure()
     */
    public void deconfigure() throws CoreException
    {
        // Currently nothing to do. If an associated builder is added,
        // this is where it would be removed.
    }

    /**
     * Get the current, associated project.
     * 
     * @return A reference to an <code>IProject</code> is returned.
     */
    public IProject getProject()
    {
        return m_project;
    }

    /**
     * Set the associated project.
     * 
     * @param project The project to associate.
     */
    public void setProject(final IProject project)
    {
        m_project = project;
    }

}
