package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface RecordFilter {

  public abstract boolean matches(byte[] data);

}
