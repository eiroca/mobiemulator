/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details.
 */



package javax.microedition.midlet;

//~--- non-JDK imports --------------------------------------------------------

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Display;

//~--- JDK imports ------------------------------------------------------------

public abstract class MIDlet {


    /**
     * Protected constructor for subclasses.
     */
    protected MIDlet() {}

    /**
     * Signals the MIDlet to start and enter the Active state.
     */
    protected abstract void startApp() throws MIDletStateChangeException;

    /**
     * Signals the MIDlet to stop and enter the Paused state.
     */
    protected abstract void pauseApp();

    /**
     * Signals the MIDlet to terminate and enter the Destroyed state.
     */
    protected abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;

    /**
     * Public method that allows the midlet manager to call the protected
     * startApp on a midlet
     */
    public void amsStartApp(MidletListener midletListener) {
        try {
            MidletUtils.getInstance().setMidletListener(midletListener);
            startApp();
            System.out.println("StartApp called");
        } catch (MIDletStateChangeException ex) {}
    }

    /**
     * Public method that allows the midlet manager to call the protected
     * pauseApp on a midlet
     */
    public void amsPauseApp() {
        pauseApp();
    }

    /**
     * Public method that allows the midlet manager to call the protected
     * destroyApp on a midlet
     */
    public void amsDestroyApp(boolean unconditional) {
        try {
            destroyApp(unconditional);
        } catch (MIDletStateChangeException ex) {}
    }

    /**
     * Used by an MIDlet to notify the application management software that it
     * has entered into the Destroyed state.
     */
    public final void notifyDestroyed() {
        System.out.println("notifyDestroyed called");
        Display.getDisplay(MidletUtils.getInstance().getMidletListener().getCurrentMIDlet()).setCurrent(null);
        MidletUtils.getInstance().getMidletListener().unLoadCurrentJar();
    }

    /**
     * Notifies the application management software that the MIDlet does not
     * want to be active and has entered the Paused state.
     */
    public final void notifyPaused() {}

    /**
     * Provides a MIDlet with a mechanism to indicate that it is interested in
     * entering the Active state.
     */
    public final void resumeRequest() {}
    public final boolean platformRequest(String URL) throws ConnectionNotFoundException{
//        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
//        java.net.URI     url     = null;
//        try {
//            url = new java.net.URI(URL);
//            if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
//                System.err.println("Desktop doesn't support the browse action (fatal)");
//
//                return false;
//            }
//            desktop.browse(url);
//
//            return true;
//        } catch (Exception ex) {
//            System.out.println("URL Not Proper");
//        }
//
//        return false;
        
                try
            {
              String str = "rundll32 url.dll,FileProtocolHandler " + URL;
              Runtime.getRuntime().exec(str);
              return false;
            }
            catch (Exception localException)
            {
               
            }
            return true;  
            //return false;
            }

    /**
     * Provides a MIDlet with a mechanism to retrieve named properties from the
     * application management software.
     */
    public final String getAppProperty(String key) {
        return MidletUtils.getInstance().getMidletListener().getAppProperty(key);
    }
}
