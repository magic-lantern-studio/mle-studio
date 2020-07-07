// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2020 Wizzer Works
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
package com.wizzer.mle.studio.properties;

// Import Eclipse packages.
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

// Import Magic Lantern packages.
import com.wizzer.mle.studio.MleLog;

/**
 * MasterTargetPropertyTester is used to test properties related to Magic Lantern
 * mastering tools and technology.
 * 
 * @author Mark Millard
 */
public class MasterTargetPropertyTester extends PropertyTester {

	/**
	 * The default constructor.
	 */
	public MasterTargetPropertyTester() {}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window == null) {
        	MleLog.logWarning("MasterTargetPropertyTester: window is null.");
        	return false;
        }
		
		if (window.equals(PlatformUI.getWorkbench().getWorkbenchWindows()[0])) {
			IPerspectiveDescriptor perspective = window.getActivePage().getPerspective();
			String perspectiveId = perspective.getId();
			String checkId = perspectiveId;
			if (receiver instanceof MPerspective) {
				MPerspective mPerspective = (MPerspective) receiver;
				checkId = mPerspective.getElementId();
			}
			if (expectedValue != null) {
			    if (checkId.startsWith((String)expectedValue)) {
				    return true;
			    }
			}
		}
			
		return false;
	}

}
