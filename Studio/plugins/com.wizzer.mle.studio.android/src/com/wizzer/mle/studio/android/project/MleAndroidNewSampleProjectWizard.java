// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.android.project;

// Import Magic Lantern classes.
import org.eclipse.jface.wizard.Wizard;

import com.wizzer.mle.studio.android.internal.wizards.MleTargetState;
import com.wizzer.mle.studio.android.internal.wizards.NewSampleProjectWizard;

/**
 * A "New Sample Android Project" Wizard.
 * <p/>
 * This displays the new project wizard pre-configured for samples only.
 */
public class MleAndroidNewSampleProjectWizard extends NewSampleProjectWizard
{
	// The state of the target to be created.
	private MleTargetState m_targetState;

	/**
     * Creates a new wizard for creating a sample Android project.
     */
    public MleAndroidNewSampleProjectWizard()
    {
    	super();

		m_targetState = new MleTargetState();
    }
    
    /**
	 * Add pages to the wizard before the wizard opens.
	 * 
	 * @see Wizard#addPages
	 */	
	public void addPages()
	{
		super.addPages();
		
		mValues.mleTarget = m_targetState;
	}
}
