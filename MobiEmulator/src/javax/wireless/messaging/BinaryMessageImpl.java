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
class BinaryMessageImpl implements BinaryMessage {
    private String address;
    private byte[] data;
    private long   timestamp;

    public BinaryMessageImpl(String address) {
        this.address = address;
    }

    public byte[] getPayloadData() {
        return this.data;
    }

    public void setPayloadData(byte[] data) {
        this.data = data;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getTimestamp() {
        return new Date(timestamp);
    }
}
