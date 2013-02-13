/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class RecordStoreInfo {
    public int getAuthMode() {
        return RecordStore.mode;
    }
    public boolean isWriteable() {
        return true;
    }
    public boolean isEncrypted() {
        return false;
    }
    public long getSize() {
        return RecordStore.recordSize;
    }
    public long getSizeAvailable() {
        return Integer.MAX_VALUE;
    }
}
