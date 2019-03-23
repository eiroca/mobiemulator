package javax.microedition.midlet;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ActionsDeniedPermission {

  public ActionsDeniedPermission() {
  }

  public boolean implies(final java.security.Permission permission) {
    return true;
  }

}
