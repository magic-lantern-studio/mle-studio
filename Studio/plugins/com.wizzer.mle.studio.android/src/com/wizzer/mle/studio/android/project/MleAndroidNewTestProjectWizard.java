// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.android.project;

// Import Magic Lantern classes.
import org.eclipse.jface.wizard.Wizard;

import com.wizzer.mle.studio.android.internal.wizards.MleTargetState;
import com.wizzer.mle.studio.android.internal.wizards.NewProjectWizard;
import com.wizzer.mle.studio.android.internal.wizards.NewTestProjectWizard;
import com.wizzer.mle.studio.android.internal.wizards.NewProjectWizardState.Mode;

/**
 * A "New Test Android Project" Wizard.
 * <p/>
 * This is really the {@link NewProjectWizard} that only displays the "test project" pages.
 */
public class MleAndroidNewTestProjectWizard extends NewTestProjectWizard
{
	// The state of the target to be created.
	private MleTargetState m_targetState;

	/**
     * Creates a new wizard for creating an Android Test Project.
     */
    public MleAndroidNewTestProjectWizard()
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
