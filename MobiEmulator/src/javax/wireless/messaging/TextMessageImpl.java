/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.wireless.messaging;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class TextMessageImpl implements TextMessage {
    private String address;
    private String data;
    private long   timeStamp;

    public TextMessageImpl(String address) {
        this.address = address;
    }

    public String getPayloadText() {
        return data;
    }

    public void setPayloadText(String data) {
        this.data = data;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getTimestamp() {
        return new Date(timeStamp);
    }
}
