package javax.microedition.io;

import javax.microedition.pki.Certificate;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface SecurityInfo {

  public abstract String getCipherSuite();

  public abstract String getProtocolName();

  public abstract String getProtocolVersion();

  public abstract Certificate getServerCertificate();

}
