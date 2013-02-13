/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface RecordFilter {
    public abstract boolean matches(byte[] data);
}
