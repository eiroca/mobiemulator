/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.wireless.messaging;

//~--- JDK imports ------------------------------------------------------------

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
