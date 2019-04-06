package javax.wireless.messaging;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
import java.util.Date;

public abstract interface Message {

  public abstract String getAddress();

  public abstract void setAddress(String paramString);

  public abstract Date getTimestamp();

}
