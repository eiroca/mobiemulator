/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.midlet.MIDletIdentity;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface IMCConnection {
    public java.lang.String getServerName();
    public java.lang.String getRequestedServerVersion();
    public MIDletIdentity getRemoteIdentity();
}
