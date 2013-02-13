package javax.wireless.messaging;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface TextMessage extends Message {
    public abstract String getPayloadText();

    public abstract void setPayloadText(String paramString);
}
