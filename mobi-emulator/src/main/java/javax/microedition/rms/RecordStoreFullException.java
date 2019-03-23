/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class RecordStoreFullException extends RecordStoreException {

  /**
   *
   */
  private static final long serialVersionUID = 4221790506313809648L;

  public RecordStoreFullException() {
    final Throwable t = new Throwable();
    t.printStackTrace();
  }

  public RecordStoreFullException(final String message) {
    System.out.println(message);
    final Throwable t = new Throwable();
    t.printStackTrace();
  }
}
