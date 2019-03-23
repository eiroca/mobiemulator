/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class RecordStoreNotOpenException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = -7628958986442454833L;

  public RecordStoreNotOpenException() {
    final Throwable t = new Throwable();
    t.printStackTrace();
  }

  public RecordStoreNotOpenException(final String message) {
    System.out.println(message);
    final Throwable t = new Throwable();
    t.printStackTrace();
  }
}
