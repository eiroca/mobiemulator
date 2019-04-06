/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class SecureRecordStoreException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = -1657584684523205655L;

  public SecureRecordStoreException() {
    final Throwable t = new Throwable();
    t.printStackTrace();
  }

  public SecureRecordStoreException(final String message) {
    System.out.println(message);
    final Throwable t = new Throwable();
    t.printStackTrace();
  }
}
