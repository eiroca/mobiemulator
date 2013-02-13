/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.midlet;

//~--- non-JDK imports --------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class MIDletIdentity {
    public static final java.lang.String IDENTIFIED_THIRD_PARTY   = "IdentifiedThirdParty";
    public static final java.lang.String MANUFACTURER             = "Manufacturer";
    public static final java.lang.String OPERATOR                 = "Operator";
    public static final java.lang.String UNIDENTIFIED_THIRD_PARTY = "UnidentifiedThirdParty";
    public java.lang.String getName() {
        return  MidletUtils.getInstance().getMidletListener().getMidletClassName();
    }
    public java.lang.String getVersion() {
        return System.getProperty("MIDlet-Version");
    }
    public java.lang.String getVendor() {
        return System.getProperty("MIDlet-Vendor");
    }
    public boolean isAuthorized() {
        return true;
    }
    public java.lang.String getSecurityDomain() {
        return IDENTIFIED_THIRD_PARTY;
    }
    public java.lang.String toString() {
        return "";
    }
}
