// COPYRIGHT_BEGIN
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

    public IProject getProject()
    {
        return m_project;
    }

    public void setProject(final IProject project)
    {
        m_project = project;
    }

}
