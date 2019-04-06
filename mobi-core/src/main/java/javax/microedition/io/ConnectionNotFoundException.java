package javax.microedition.io;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ConnectionNotFoundException extends Throwable {

  /**
   *
   */
  private static final long serialVersionUID = 3632439225598367552L;

  ConnectionNotFoundException() {
    this.printStackTrace();
  }

  ConnectionNotFoundException(final String Message) {
    System.out.println(Message);
    toString();
  }

}
