// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.rehearsal;

// Import Eclipse classes.
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.rehearsal.launch.RehearsalPlayerLaunchListener;
import com.wizzer.mle.studio.rehearsal.launch.ui.RehearsalPlayerWindow;
import com.wizzer.mle.studio.rehearsal.view.RehearsalPlayerView;


/**
 * The activator class controls the plug-in life cycle.
 */
public class RehearsalPlugin extends AbstractUIPlugin
{
	// The plug-in ID.
	public static final String PLUGIN_ID = "com.wizzer.mle.studio.rehearsal";

	/** View configuration uses Magic Lantern Perspective. */
	public static final int LAUNCH_PERSPECTIVE = 0;
	/** View configuration uses separate window. */
	public static final int LAUNCH_WINDOW = 1;

	// The shared instance.
	private static RehearsalPlugin m_plugin;
	// The launch listener.
	private RehearsalPlayerLaunchListener m_launchListener = null;
	// Flag indicating view configuration.
	private boolean m_usePerspective = false;
	// The SWT window for separate window configuration.
	private Shell m_rehearsalPlayerWindow = null;
	// Helper class for Rehearsal Player execution cycle.
	private PlayerMainLoop m_loop;

	/*
	 * This class implements a Runnable for executing the Rehearsal
	 * Player's main loop.
	 * 
	 * @author Mark S. Millard
	 */
	private class PlayerMainLoop implements Runnable
	{
		// The Rehearsal Player view.
		private RehearsalPlayerView m_player;
		
		/** A flag indicating whether to quit or not. */
		public boolean m_quit = false;
        
		// Hide the default constructor.
		private PlayerMainLoop() {}
		
		/**
		 * A constructor that specifies the Rehearsal Player.
		 * 
		 * @param player The Rehearsal Player.
		 */
        PlayerMainLoop(RehearsalPlayerView player)
        {
            this.m_player = player;
        }

        /**
         * Execute the main loop.
         */
        public void run()
        {
        	Display display = RehearsalPlugin.getDisplay();

        	while (! m_quit)
        	{
        		if (! display.readAndDispatch())
        			// Execute one cycle of execution.
        			m_player.execPlayerCycle();
        	}
        }
    }

	/**
	 * The constructor.
	 */
	public RehearsalPlugin()
	{
	}

	/**
	 * This method is called upon plug-in activation.
	 */
	public void start(BundleContext context) throws Exception
	{
	    // We use System.out.println here instead of DwpLog.logInfo because
	    // the plug-in activation will fail otherwise. This happens because
	    // the plug-in has not yet been completely constructed and initialized.
		System.out.println("Activating com.wizzer.mle.studio.rehearsal plug-in.");
		super.start(context);
		m_plugin = this;
		
		// Initialize the launch listener.
		m_launchListener = new RehearsalPlayerLaunchListener();
		
		// Get the launch manager from the Eclipse Debug Plugin and register our
		// launch listener with it.
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        manager.addLaunchListener(m_launchListener);
	}

	/**
	 * This method is called when the plug-in is stopped.
	 */
	public void stop(BundleContext context) throws Exception
	{
		System.out.println("Deactivating com.wizzer.mle.studio.rehearsal plug-in.");
		m_plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return The shared instance is returned.
	 */
	public static RehearsalPlugin getDefault()
	{
		return m_plugin;
	}
	
	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace()
	{
		return ResourcesPlugin.getWorkspace();
	}
	
	/**
	 * Get the active Workbench page.
	 * 
	 * @return A reference to <code>IWorkbenchPage</code> is returned.
	 */
	public static IWorkbenchPage getWorkbenchPage()
	{
	    return getWorkbenchWindow().getActivePage();
	}
	
	/**
	 * Get the active Workbench window.
	 * 
	 * @return A reference to <code>IWorkbenchWindow</code> is returned.
	 */
	public static IWorkbenchWindow getWorkbenchWindow()
	{
	    return m_plugin.getWorkbench().getActiveWorkbenchWindow();
	}
	
	/**
	 * Get the display.
	 * 
	 * @return The <code>Display</code> is returned.
	 */
	public static Display getDisplay()
	{
		Shell shell = getActiveWorkbenchShell();
		if (shell != null) {
			return shell.getDisplay();
		}
		
		Display display = Display.getCurrent();
		if (display == null) {
			display= Display.getDefault();
		}
		
		return display;
	}

	/**
	 * Get the active workbench shell.
	 * 
	 * @return A <code>Shell</code> is returned.
	 */
	public static Shell getActiveWorkbenchShell()
	{
		IWorkbenchWindow workBenchWindow = getActiveWorkbenchWindow();
		if (workBenchWindow == null)
			return null;
			
		return workBenchWindow.getShell();
	}

	/**
	 * Get the active workbench window.
	 * 
	 * @return The active workbench window is returned.
	 */
	static public IWorkbenchWindow getActiveWorkbenchWindow()
	{
		if (m_plugin == null)
			return null;

		IWorkbench workBench = m_plugin.getWorkbench();
		if (workBench == null)
			return null;

		return workBench.getActiveWorkbenchWindow();
	}

	/**
	 * Get the active page.
	 * 
	 * @return The active page is returned for the active workbench window.
	 */
	static public IWorkbenchPage getActivePage()
	{
		IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null)
			return null;
	
		return activeWorkbenchWindow.getActivePage();
	}
	
	/**
	 * Show all the Rehearsal Player ActiveX components in a separate window.
	 * 
	 * @param dwp The Digital Workprint.
	 * @param workingdir The working directory.
	 */
	public void showWindow(String dwp, String workingDir)
	{
        final Display display = RehearsalPlugin.getDisplay();
        final Shell shell = new Shell(display);
        
        final RehearsalPlayerWindow window = new RehearsalPlayerWindow(shell);
	    shell.addDisposeListener(window);
	    shell.addDisposeListener(new DisposeListener()
	    {
	        public void widgetDisposed(DisposeEvent event)
	        {
	            RehearsalPlayerLaunchListener.terminateCurrentLaunch();
	        }
	    });
	    window.setDigitalWorkprint(dwp);
	    window.initPlayer("-cwd " + "\"" + workingDir + "\"");
		shell.pack();
		shell.open();

		// Start an asynchronous thread to handle event dispatching.
	    display.asyncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					while (! shell.isDisposed())
					{
					    if (! display.readAndDispatch())
					    	//display.sleep();
					    	window.execPlayerCycle();
					}
				} catch (Exception ex)
				{
					// An exception is being thrown by display.readAndDispatch() after
					// the shell window has been closed but is not yet disposed. We will
					// ignore this exception for now.
					//System.out.println("***** " + ex.getMessage());
				}
			}
		});
	    
        shell.setText("Rehearsal Player");
        //shell.setImage(
        //    RehearsalPlugin.getDefault().getImageRegistry().getDescriptor("logo").createImage());

	    m_rehearsalPlayerWindow = shell;
	}
	
	/**
	 * Close down the separate window containing the Client Simulator
	 * ActiveX components.
	 */
	public void closeWindow()
	{
	    if ((! m_usePerspective) && (m_rehearsalPlayerWindow != null))
	    {
	    	final Display display = RehearsalPlugin.getDisplay();
	    	
	    	// Use a synchronous thread that is envoked by the user interface thread
	    	// to kill the shell window. This is required to workaround the case where
	    	// closeWindow() is called from a thread that is not the user interface thread.
	    	display.syncExec(new Runnable()
	    	{
	    		public void run()
	    		{
	    	    	m_rehearsalPlayerWindow.close();
	    		}
	    	});

	    	m_rehearsalPlayerWindow = null;
	    }
	}
	
	/*
	 * Activates the specified view in the active page.
	 * This call has no effect if the specified view is
	 * already activated.
	 * 
	 * @param id The id of the <code>ViewPart</code> to activate.
	 *
	 * @return <code>true</code> if the search result view could be activated
	 */
	private IViewPart showView(String id)
	{
		try
		{
			return (getActivePage().showView(id));
		} catch (PartInitException ex)
		{
			RehearsalLog.logError(ex, "Unable to show view " + id);
			return null;
		}	
	}
	
	// Hide the specified View.
	private void hideView(IViewPart part)
	{
		IWorkbenchPage page = getActivePage();
		if (page != null)
			page.hideView(part);
	}
	
	// Find the Rehearsal Player view.
	private RehearsalPlayerView findRehearsalPlayerViewInActivePage()
	{
		IWorkbenchPage page = getActivePage();		
		if (page == null)
			return null;

		return (RehearsalPlayerView) page.findView(RehearsalPlayerView.getID());
	}
	
	// Close all references to the Rehearsal Player Views.
	private void closeViewReferences()
	{
		IWorkbenchPage page = getActivePage();		
		if (page == null)
			return;

		IViewReference[] refs = page.getViewReferences();
		for (int i = 0; i < refs.length; i++)
		{
			String partID = refs[i].getId();
			if (partID.equalsIgnoreCase(RehearsalPlayerView.getID()))
			    hideView(refs[i].getView(false));
		}
	}
	
	/**
	 * Show all the Rehearsal Player ActiveX components in a perspective.
	 * 
	 * @param dwp The Digital Workprint.
	 * @param workingDir The working directory.
	 */
	public void showViews(String dwp, String workingDir)
	{
		// XXX - throw an exception if dwp and workingDir are null.

		// Get the id of the perspective in which to show the specified view.
		// XXX - Eventually this should be a preference obtained from the
		// global preferences.
		String defaultPerspectiveId = "com.wizzer.mle.studio.StudioPerspective";
		
		if (defaultPerspectiveId != null)
		{
			IWorkbenchWindow window = getActiveWorkbenchWindow();
			if (window != null && window.getShell() != null && !window.getShell().isDisposed())
			{
				try
				{
					PlatformUI.getWorkbench().showPerspective(defaultPerspectiveId, window);
				} catch (WorkbenchException ex)
				{
					// Show view in current perspective.
				}
			}
		}

		// Show the Rehearsal Player view if necessary.
		RehearsalPlayerView playerView = findRehearsalPlayerViewInActivePage();
		if (playerView == null)
			playerView = (RehearsalPlayerView)showView(RehearsalPlayerView.getID());

		if (playerView != null)
		{
			// Set the digital workprint.
			playerView.setDigitalWorkprint(dwp);
			
			// Initialize the player.
			playerView.initPlayer("-cwd " + "\"" + workingDir + "\"");
			
			// Start player execution.
			final Display display = RehearsalPlugin.getDisplay();
			
			playerView.execPlayerCycle();
			m_loop = new PlayerMainLoop(playerView);
			display.asyncExec(m_loop);
		}
	}
	
	/**
	 * Hide all Rehearsal Player views.
	 */
	public void hideViews()
	{
		// Make sure this runs in the UI thread.
		Display.getDefault().asyncExec(new Thread()
		{
			public void run()
			{
				// Remember current perspective so that we can return to it.
				IPerspectiveDescriptor currentDesc = null;
				IWorkbenchPage activePage = getActivePage();
				if (activePage != null)
					currentDesc = activePage.getPerspective();
		
				// Get the id of the perspective in which to hide the specified view.
				// XXX - Eventually this should be a preference obtained from the
				// global preferences
				String defaultPerspectiveId = "com.wizzer.mle.studio.StudioPerspective";
				
				if (defaultPerspectiveId != null)
				{
					IWorkbenchWindow window = getActiveWorkbenchWindow();
					if (window != null && window.getShell() != null && !window.getShell().isDisposed())
					{
						try
						{
							PlatformUI.getWorkbench().showPerspective(defaultPerspectiveId, window);
						} catch (WorkbenchException ex)
						{
							// Hide view in current perspective.
						}
					}
				}
				
				// Hide the Rehearsal Player view.
				RehearsalPlayerView playerView = findRehearsalPlayerViewInActivePage();
				if ((playerView != null))
					hideView(playerView);
				
				// Reset to the previous perspective.
				if (activePage != null)
					activePage.setPerspective(currentDesc);
			}
		});
	}

	/**
	 * Close down the views containing the Rehearsal Player
	 * ActiveX components.
	 */
	public void closeViews()
	{
		// Hide the Views. Note that the parts will be closed if they are no longer
		// referenced.
		hideViews();
		
		// Make sure all references to the Rehearsal Player Views are closed.
		// Sometimes they aren't closed because they are reactivated during the
		// call to hide the Views. Go figure.
		closeViewReferences();
		
		// Terminate any outstanding launches.
		if (m_launchListener != null)
		    RehearsalPlayerLaunchListener.terminateCurrentLaunch();

	}

	/**
	 * Set the view configuration.
	 * 
	 * @param config The type of view configuration for launching the ActiveX
	 * components of the Rehearsal Player.
	 */
	public void setViewConfiguration(int config)
	{
	    if (config == LAUNCH_PERSPECTIVE)
	        m_usePerspective = true;
	    else
	        m_usePerspective = false;
	}

	/**
	 * Shutdown the Rehearsal Player.
	 */
	public void closePlayer()
	{
	    if (m_usePerspective)
	    {
	    	if (m_loop != null)
	    		m_loop.m_quit = true;
	        closeViews();
	    } else
	        closeWindow();
	}
	
	/**
	 * Startup the Rehearsal Player.
	 * 
	 * @param dwp The Digital Workprint.
	 * @param workingDir The working directory.
	 */
	public void openPlayer(String dwp, String workingDir)
	{
	    if (m_usePerspective)
	    {
	    	if (m_loop != null)
	    		m_loop = null;
	        showViews(dwp, workingDir);
	    } else
	        showWindow(dwp, workingDir);
	}

	/**
	 * Get the plug-in identifier.
	 * 
	 * @return A <code>String</code> is returned identifying the plug-in.
	 */
	public static String getID()
	{
		return getDefault().getBundle().getSymbolicName();
	}

}
