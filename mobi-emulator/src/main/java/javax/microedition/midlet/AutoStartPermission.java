package javax.microedition.midlet;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class AutoStartPermission {

  public AutoStartPermission() {
  }

  public boolean implies(final java.security.Permission permission) {
    return true;
  }

}
