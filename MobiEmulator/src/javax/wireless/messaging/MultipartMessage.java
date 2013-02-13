package javax.wireless.messaging;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface MultipartMessage extends Message {
    public abstract boolean addAddress(String paramString1, String paramString2);

    public abstract void addMessagePart(MessagePart paramMessagePart) throws SizeExceededException;

    public abstract String getAddress();

    public abstract String[] getAddresses(String paramString);

    public abstract String getHeader(String paramString);

    public abstract MessagePart getMessagePart(String paramString);

    public abstract MessagePart[] getMessageParts();

    public abstract String getStartContentId();

    public abstract String getSubject();

    public abstract boolean removeAddress(String paramString1, String paramString2);

    public abstract void removeAddresses();

    public abstract void removeAddresses(String paramString);

    public abstract boolean removeMessagePart(MessagePart paramMessagePart);

    public abstract boolean removeMessagePartId(String paramString);

    public abstract boolean removeMessagePartLocation(String paramString);

    public abstract void setAddress(String paramString);

    public abstract void setHeader(String paramString1, String paramString2);

    public abstract void setStartContentId(String paramString);

    public abstract void setSubject(String paramString);
}
