/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.wireless.messaging;

//~--- non-JDK imports --------------------------------------------------------

import javax.microedition.midlet.MidletUtils;
import java.io.IOException;
import java.io.InterruptedIOException;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class MessageConnectionImpl implements MessageConnection {
    private String          address         = null;
    private MessageListener messageListener = null;
    private boolean         isConnClosed;

    public MessageConnectionImpl(String address) {
        this.address = address;
        isConnClosed = false;
    }

    public Message newMessage(String type) {
        return newMessage(type, this.address);
    }

    public Message newMessage(String type, String address) {
        if (type.equals("text")) {
            return new TextMessageImpl(address);
        }

        if (type.equals("binary")) {
            return new BinaryMessageImpl(address);
        }

        return null;
    }

    public void send(Message message) throws IOException, InterruptedIOException {
        MidletUtils.getInstance().getMidletListener().sendMessage(message, this.address);
    }

    public Message receive() throws IOException, InterruptedIOException {
        Message receivedMessage = MidletUtils.getInstance().getMidletListener().receiveMessage(address);

        return receivedMessage;
    }

    public void setMessageListener(MessageListener messageListener) throws IOException {
        this.messageListener = messageListener;
    }

    public int numberOfSegments(Message message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void close() {
        isConnClosed = true;
    }
}
