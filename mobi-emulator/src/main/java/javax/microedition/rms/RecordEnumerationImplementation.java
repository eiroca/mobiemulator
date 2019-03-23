package javax.microedition.rms;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class RecordEnumerationImplementation implements RecordEnumeration {

  private int curRecordIndex = 1;
  private int totalNoRecords = 0;
  private RecordStore recordstore;

  public RecordEnumerationImplementation(final RecordStore rs) {
    recordstore = rs;
    totalNoRecords = rs.getNumRecords();
    curRecordIndex = 1;
  }

  @Override
  public boolean isKeptUpdated() {
    return false;
  }

  @Override
  public void keepUpdated(final boolean keepUpdated) {
  }

  @Override
  public void rebuild() {
  }

  @Override
  public void reset() {
    curRecordIndex = 1;
  }

  @Override
  public boolean hasPreviousElement() {
    return curRecordIndex > 1;
  }

  @Override
  public boolean hasNextElement() {
    return curRecordIndex < numRecords();
  }

  @Override
  public int previousRecordId() {
    final int i = curRecordIndex;
    --curRecordIndex;

    return i;
  }

  @Override
  public byte[] previousRecord() {
    if (curRecordIndex < 1) {
      new InvalidRecordIDException("Invalid Record Id ");
    }
    byte[] b = null;
    try {
      b = recordstore.getRecord(curRecordIndex);
    }
    catch (final InvalidRecordIDException ex) {
      Logger.getLogger(RecordEnumerationImplementation.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (final RecordStoreException ex) {
      Logger.getLogger(RecordEnumerationImplementation.class.getName()).log(Level.SEVERE, null, ex);
    }
    --curRecordIndex;

    return b;
  }

  @Override
  public int numRecords() {
    return recordstore.getNumRecords();
  }

  @Override
  public byte[] nextRecord() {
    if (curRecordIndex > numRecords()) {
      new InvalidRecordIDException("Invalid Record Id ");
    }
    byte[] b = null;
    try {
      b = recordstore.getRecord(curRecordIndex);
    }
    catch (final InvalidRecordIDException ex) {
      Logger.getLogger(RecordEnumerationImplementation.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (final RecordStoreException ex) {
      Logger.getLogger(RecordEnumerationImplementation.class.getName()).log(Level.SEVERE, null, ex);
    }
    ++curRecordIndex;

    return b;
  }

  @Override
  public int nextRecordId() {
    final int i = curRecordIndex;
    ++curRecordIndex;

    return i;
  }

  @Override
  public void destroy() {
    curRecordIndex = 1;
    totalNoRecords = 0;
    recordstore = null;
  }

}
