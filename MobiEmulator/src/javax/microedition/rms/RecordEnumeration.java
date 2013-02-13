/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface RecordEnumeration {
    public boolean isKeptUpdated();
    public void keepUpdated(boolean keepUpdated);
    public void rebuild();
    public void reset();
    public boolean hasPreviousElement();
    public boolean hasNextElement();
    public int previousRecordId();
    public byte[] previousRecord();
    public int numRecords();
    public byte[] nextRecord();
    public int nextRecordId();
    public void destroy();
}
