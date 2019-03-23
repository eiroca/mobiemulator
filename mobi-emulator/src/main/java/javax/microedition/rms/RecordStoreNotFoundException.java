/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class RecordStoreNotFoundException extends RecordStoreException {

  /**
   *
   */
  private static final long serialVersionUID = -4594147613901196247L;

  public RecordStoreNotFoundException() {
    final Throwable t = new Throwable();
    t.printStackTrace();
  }

  public RecordStoreNotFoundException(final String message) {
    System.out.println(message);
    final Throwable t = new Throwable();
    t.printStackTrace();
  }
}
