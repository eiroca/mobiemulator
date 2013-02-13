/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.wireless.messaging;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface BinaryMessage extends Message {
    public abstract byte[] getPayloadData();

    public abstract void setPayloadData(byte[] paramArrayOfByte);
}
