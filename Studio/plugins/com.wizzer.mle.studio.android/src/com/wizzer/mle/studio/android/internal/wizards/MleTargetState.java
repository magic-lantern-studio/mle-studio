package com.wizzer.mle.studio.android.internal.wizards;

import java.io.File;

import org.eclipse.core.runtime.IConfigurationElement;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.wizzer.mle.studio.framework.PathUtil;

public class MleTargetState
{
	/** The location for the Magic Lantern Android Studio sample applications. */
	public static final int SAMPLES = 1;
	
	/** The name of the Digital Workprint file. */
	public String dwpFilename;
	/** The name of the Digital Playprint file. */
	public String dppFilename;
	
	/** Flag indicating GenCast Builder is selected. */
    public boolean gencastSelected;
	/** Flag indicating GenScene Builder is selected. */
    public boolean gensceneSelected;
	/** Flag indicating GenMediaRef Builder is selected. */
    public boolean genmrefSelected;
	/** Flag indicating GenTables Builder is selected. */
    public boolean gentablesSelected;
	/** Flag indicating GenDPPScript Builder is selected. */
    public boolean gendppscriptSelected;
	/** Flag indicating GenDPP Builder is selected. */
    public boolean gendppSelected;
    
    /** Flag indicating Rehearsal Player is selected. */
    public boolean rehearsalPlayerSelected;
    
    /** Application Template type. */
    public int templateType;
    
    /** Flag indicating whether to use an application template. */
    public boolean useTemplate;
    /** Configuration elements used to define application template information. */
    public IConfigurationElement[] applicationImports;
    
    /**
     * Get the Android API level as a <code>String</code>.
     * 
     * @param target The Android target.
     * 
     * @return The API level is returned as a <code>String</code>.
     */
    public static String getAndroidApi(IAndroidTarget target)
    {
    	AndroidVersion version = target.getVersion();
    	String targetDir;
		switch (version.getApiLevel())
		{
		    case 3:	targetDir = "android-3"; break;
		    case 4: targetDir = "android-4"; break;
		    case 5:	targetDir = "android-5"; break;
		    case 6:	targetDir = "android-6"; break;
		    case 7:	targetDir = "android-7"; break;
		    case 8:	targetDir = "android-8"; break;
		    case 9:	targetDir = "android-9"; break;
		    case 10: targetDir = "android-10"; break;
		    case 11: targetDir = "android-11"; break;
		    case 12: targetDir = "android-12"; break;
		    case 13: targetDir = "android-13"; break;
		    case 14: targetDir = "android-14"; break;
		    case 15: targetDir = "android-15"; break;
			default:
				return null;
		}
		return new String(targetDir);
    }
    
    /**
     * Get the path to the location identified by the key. The location should
     * be relative to $MLE_HOME.
     * 
     * @param target An Android target.
     * @param key The Magic Lantern location to retrieve.
     * 
     * @return A fully expanded path is returned.
     */
    public String getPath(IAndroidTarget target, int key)
    {
    	// Return the path for $MLE_HOME/lib/<android-target>/<location>.
    	String path = null;
    	String location = null;
    	
		
		String targetDir = getAndroidApi(target);
		
		if (key == SAMPLES) location = "samples";
		else return null;
		
		String mleHome = System.getenv("MLE_HOME");
		String dirname = mleHome + File.separator +
			"lib" + File.separator +
			targetDir + File.separator +
			location;
		
		path = PathUtil.filenameExpand(dirname);
		
    	return path;
    }
}
