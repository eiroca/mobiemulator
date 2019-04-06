package javax.microedition.midlet;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class MIDletIdentity {

  public static final java.lang.String IDENTIFIED_THIRD_PARTY = "IdentifiedThirdParty";
  public static final java.lang.String MANUFACTURER = "Manufacturer";
  public static final java.lang.String OPERATOR = "Operator";
  public static final java.lang.String UNIDENTIFIED_THIRD_PARTY = "UnidentifiedThirdParty";

  public String getName() {
    return MidletUtils.getInstance().getMidletListener().getMidletClassName();
  }

  public String getVersion() {
    return System.getProperty("MIDlet-Version");
  }

  public String getVendor() {
    return System.getProperty("MIDlet-Vendor");
  }

  public boolean isAuthorized() {
    return true;
  }

  public String getSecurityDomain() {
    return MIDletIdentity.IDENTIFIED_THIRD_PARTY;
  }

  @Override
  public String toString() {
    return "";
  }

}
