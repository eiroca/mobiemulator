/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface RecordComparator {
    public static final int EQUIVALENT = 0;
    public static final int FOLLOWS    = 1;
    public static final int PRECEDES   = -1;
    public abstract int compare(byte[] data1, byte[] data2);
}
