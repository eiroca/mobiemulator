/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.rms;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class RecordEnumerationImplementation implements RecordEnumeration {
    private int         curRecordIndex = 1;
    private int         totalNoRecords = 0;
    private RecordStore recordstore;
    public RecordEnumerationImplementation(RecordStore rs) {
        this.recordstore = rs;
        totalNoRecords   = rs.getNumRecords();
        curRecordIndex   = 1;
    }

    public boolean isKeptUpdated() {
        return false;
    }
    public void keepUpdated(boolean keepUpdated) {}
    public void rebuild() {}
    public void reset() {
        curRecordIndex = 1;
    }
    public boolean hasPreviousElement() {
        return curRecordIndex > 1;
    }
    public boolean hasNextElement() {
        return curRecordIndex < numRecords();
    }
    public int previousRecordId() {
        int i = curRecordIndex;
        --curRecordIndex;

        return i;
    }
    public byte[] previousRecord() {
        if (curRecordIndex < 1) {
            new InvalidRecordIDException("Invalid Record Id ");
        }
        byte[] b = null;
        try {
            b = recordstore.getRecord(curRecordIndex);
        } catch (InvalidRecordIDException ex) {
            Logger.getLogger(RecordEnumerationImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RecordStoreException ex) {
            Logger.getLogger(RecordEnumerationImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        --curRecordIndex;

        return b;
    }
    public int numRecords() {
        return recordstore.getNumRecords();
    }
    public byte[] nextRecord() {
        if (curRecordIndex > numRecords()) {
            new InvalidRecordIDException("Invalid Record Id ");
        }
        byte[] b = null;
        try {
            b = recordstore.getRecord(curRecordIndex);
        } catch (InvalidRecordIDException ex) {
            Logger.getLogger(RecordEnumerationImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RecordStoreException ex) {
            Logger.getLogger(RecordEnumerationImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        ++curRecordIndex;

        return b;
    }
    public int nextRecordId() {
        int i = curRecordIndex;
        ++curRecordIndex;

        return i;
    }
    public void destroy() {
        curRecordIndex = 1;
        totalNoRecords = 0;
        recordstore    = null;
    }
}
