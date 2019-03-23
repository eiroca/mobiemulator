package javax.microedition.io;

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
