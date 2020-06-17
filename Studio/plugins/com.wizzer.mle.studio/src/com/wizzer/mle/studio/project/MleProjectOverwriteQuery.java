/*
 * MleProjectOverwriteQuery.java
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
