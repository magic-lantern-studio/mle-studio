/*
 * MleProjectOverwriteQuery.java
 * Created on Aug 5, 2005
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
package com.wizzer.mle.studio.project;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.dialogs.IOverwriteQuery;

/**
 * @author Mark S. Millard
 */
public class MleProjectOverwriteQuery implements IOverwriteQuery
{
    Shell m_shell = null;
    
    // Hide the default constructor.
    private MleProjectOverwriteQuery() {}
    
    /**
     * 
     */
    public MleProjectOverwriteQuery(Shell shell)
    {
        super();
        m_shell = shell;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.dialogs.IOverwriteQuery#queryOverwrite(java.lang.String)
     */
    public String queryOverwrite(String pathString)
    {
		/*
		String[] returnCodes = { YES, NO, ALL, CANCEL};
		int returnVal = openDialog(file);
		return returnVal < 0 ? CANCEL : returnCodes[returnVal];
		*/
		
		// Should alwyas return ALL since we are creating a new MLE project.
		return IOverwriteQuery.ALL;
    }

	// Open the dialog panel
	private int openDialog(final String file)
	{
		final int[] result = { IDialogConstants.CANCEL_ID };
		m_shell.getDisplay().syncExec(new Runnable()
		{
			public void run()
			{
				String title = MleProjectMessages.getString("MleProjectCreationWizard.overwritequery.title");
				String msg = MleProjectMessages.getFormattedString("MleProjectCreationWizard.overwritequery.message", file);
				String[] options = {IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.YES_TO_ALL_LABEL, IDialogConstants.CANCEL_LABEL};
				MessageDialog dialog = new MessageDialog(m_shell, title, null, msg, MessageDialog.QUESTION, options, 0);
				result[0] = dialog.open();
			}
		});
		return result[0];
	}

}
