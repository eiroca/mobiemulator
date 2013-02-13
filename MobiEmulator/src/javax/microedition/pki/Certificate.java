/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.pki;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface Certificate {
    public abstract String getSubject();
    public abstract String getIssuer();
    public abstract String getType();
    public abstract String getVersion();
    public abstract String getSigAlgName();
    public abstract long getNotBefore();
    public abstract long getNotAfter();
    public abstract String getSerialNumber();
}
