package javax.wireless.messaging;

import java.io.IOException;
import java.io.InterruptedIOException;
// ~--- non-JDK imports --------------------------------------------------------
import javax.microedition.midlet.MidletUtils;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class MessageConnectionImpl implements MessageConnection {

  private String address = null;
  private MessageListener messageListener = null;
  private boolean isConnClosed;

  public MessageConnectionImpl(final String address) {
    this.address = address;
    isConnClosed = false;
  }

  @Override
  public Message newMessage(final String type) {
    return newMessage(type, address);
  }

  @Override
  public Message newMessage(final String type, final String address) {
    if (type.equals("text")) { return new TextMessageImpl(address); }

    if (type.equals("binary")) { return new BinaryMessageImpl(address); }

    return null;
  }

  @Override
  public void send(final Message message) throws IOException, InterruptedIOException {
    MidletUtils.getInstance().getMidletListener().sendMessage(message, address);
  }

  @Override
  public Message receive() throws IOException, InterruptedIOException {
    final Message receivedMessage = MidletUtils.getInstance().getMidletListener().receiveMessage(address);

    return receivedMessage;
  }

  @Override
  public void setMessageListener(final MessageListener messageListener) throws IOException {
    this.messageListener = messageListener;
  }

  @Override
  public int numberOfSegments(final Message message) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void close() {
    isConnClosed = true;
  }

}
