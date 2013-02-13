/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

//~--- JDK imports ------------------------------------------------------------

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
