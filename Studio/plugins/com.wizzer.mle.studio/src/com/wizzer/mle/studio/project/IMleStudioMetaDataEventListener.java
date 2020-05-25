// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.project;

// Import standard Java classes.
import java.util.EventListener;

/**
 * This interface specifies a listener for meta-data events.
 * 
 * @author Mark S. Millard
 */
public interface IMleStudioMetaDataEventListener extends EventListener
{
    /**
     * Handle the meta-data event.
     * 
     * @param event The meta-data event.
     */
    public void handleMetaDataEvent(MleStudioMetaDataEvent event);
}
