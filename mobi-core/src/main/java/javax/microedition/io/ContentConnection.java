package javax.microedition.io;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface ContentConnection extends StreamConnection {

  public String getEncoding();

  public String getType();

  public long getLength();

}
