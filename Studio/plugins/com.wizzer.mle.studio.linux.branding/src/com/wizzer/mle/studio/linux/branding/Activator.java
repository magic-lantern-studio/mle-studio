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
package com.wizzer.mle.studio.linux.branding;

// Import Eclipse packages.
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID.
	public static final String PLUGIN_ID = "com.wizzer.mle.studio.linux.branding"; //$NON-NLS-1$

	// The shared instance.
	private static Activator m_plugin;
	
	/**
	 * The constructor.
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		// We use System.out.println here instead of DppLog.logInfo because
	    // the plug-in activation will fail otherwise. This happens because
	    // the plug-in has not yet been completely constructed and initialized.
	    System.out.println("Activating com.wizzer.mle.studio.branding plug-in.");
		super.start(context);
		m_plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Deactivating com.wizzer.mle.studio.branding plug-in.");
		m_plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return The shared instance is returned.
	 */
	public static Activator getDefault() {
		return m_plugin;
	}

}
