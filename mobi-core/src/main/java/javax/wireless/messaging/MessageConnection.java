package javax.wireless.messaging;

import java.io.IOException;
import java.io.InterruptedIOException;
/**
 *
 * @author Ashok Kumar Gujarathi
 */
import javax.microedition.io.Connection;

public abstract interface MessageConnection extends Connection {

  public static final String BINARY_MESSAGE = "binary";
  public static final String MULTIPART_MESSAGE = "multipart";
  public static final String TEXT_MESSAGE = "text";

  public abstract Message newMessage(String paramString);

  public abstract Message newMessage(String paramString1, String paramString2);

  public abstract void send(Message paramMessage) throws IOException, InterruptedIOException;

  public abstract Message receive() throws IOException, InterruptedIOException;

  public abstract void setMessageListener(MessageListener paramMessageListener) throws IOException;

  public abstract int numberOfSegments(Message paramMessage);

}
