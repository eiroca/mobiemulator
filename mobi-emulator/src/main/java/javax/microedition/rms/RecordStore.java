package javax.microedition.rms;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
// ~--- non-JDK imports --------------------------------------------------------
import Emulator.Main;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class RecordStore {

  // static RecordStore rs=null;
  public static final int AUTHMODE_ANY = 1;
  public static final int AUTHMODE_PRIVATE = 0;
  private static final int RECORD_ADDED = 0;
  private static final int RECORD_CHANGED = 1;
  private static final int RECORD_DELETED = 2;
  // public static String recordStorePath = "C:/";
  public static String RMSDirectory = Main.getRmsDir();
  public static boolean isRMSDebugEnabled = false;
  public static int maxRecords = 0;
  public static int maxRecordsStores = 0;
  public static int mode = RecordStore.AUTHMODE_PRIVATE;
  private static String RecordStoreName;
  public static int recordSize;
  public static int sizeAvailable;
  private int recordID = 1;
  private final int recordStoreSize = 0;
  private final Vector listeners = new Vector();
  public RecordEnumeration RE;
  private File RMSDir;
  private byte[] recordData;
  private File recordStoreFile;
  private int recordStoreVersion;

  public RecordStore(final String recordStoreName, final boolean createIfNecessary) throws RecordStoreNotFoundException {
    RecordStore.RecordStoreName = "#" + recordStoreName;
    if (!(RMSDir = new File(RecordStore.RMSDirectory)).exists() && !RMSDir.isDirectory()) {
      RMSDir.mkdir();
    }
    recordID = getNumRecords();
    try {
      if (createIfNecessary) {
        if (!(recordStoreFile = new File(RecordStore.RMSDirectory + "/" + RecordStore.RecordStoreName)).exists()
            && !recordStoreFile.isDirectory()) {
          if (RecordStore.isRMSDebugEnabled) {
            System.out.println("Creating recordstore " + recordStoreName);
          }
          recordStoreFile.mkdir();
        }
      }
      else {
        return;
      }
    }
    catch (final Exception e) {
      throw new RecordStoreNotFoundException(recordStoreName);
    }
  }

  public static void deleteRecordStore(final String RecordStoreName) {
    // rs=new RecordStore();
    File f = new File(RecordStore.RMSDirectory + "/" + RecordStoreName);
    // System.out.println("..........delete recordstore " +
    // RecordStoreName);
    if (f.exists()) {
      if (f.isDirectory()) {
        final File files[] = f.listFiles();
        for (final File file : files) {
          if (RecordStore.isRMSDebugEnabled) {
            System.out.println("record deleted " + file.getName());
          }
          file.delete();
        }
      }
      if (RecordStore.isRMSDebugEnabled) {
        System.out.println("" + RecordStoreName + " recordstore deleted ");
      }
      f.delete();
      f = null;
    }
    //      else {
    //          //new RecordStoreNotFoundException();
    //      }
  }

  public static RecordStore openRecordStore(final String recordStoreName, final boolean createIfNecessary)
      throws RecordStoreNotFoundException {
    return RecordStore.openRecordStore(recordStoreName, createIfNecessary, 0, true);
  }

  public static RecordStore openRecordStore(String recordStoreName, final boolean createIfNecessary, final int authmode, final boolean writable) {
    RecordStore.mode = authmode;
    if (recordStoreName.startsWith("/")) {
      recordStoreName = recordStoreName.substring(1);
    }
    if (createIfNecessary || RecordStore.isRecordStoreAvailable(recordStoreName)) {
      try {
        return new RecordStore(recordStoreName, createIfNecessary);
      }
      catch (final RecordStoreNotFoundException ex) {
      }
    }

    return null;
    // throw new RecordStoreNotFoundException(recordStoreName);
  }

  public static RecordStore openRecordStore(final String recordStoreName, final String vendorName, final String suiteName) throws RecordStoreNotFoundException {
    return RecordStore.openRecordStore(recordStoreName, false);
  }

  public static boolean isRecordStoreAvailable(final String recordStoreName) {
    return (new File(RecordStore.RMSDirectory + "/" + RecordStore.RecordStoreName)).exists();
  }

  public void setMode(final int authmode, final boolean writable) {
  }

  public void closeRecordStore() {
    if (RecordStore.isRMSDebugEnabled) {
      System.out.println("closing RecordStore " + RecordStore.RecordStoreName);
      // RecordStoreName = null;
    }
  }

  public static String[] listRecordStores() {
    File f = new File(RecordStore.RMSDirectory);
    if ((f = new File(RecordStore.RMSDirectory)).exists() && f.isDirectory()) {
      File recordStores[] = f.listFiles(new recordStoreNameFilter());
      final String names[] = new String[recordStores.length];
      for (int i = 0; i < recordStores.length; i++) {
        names[i] = recordStores[i].getName();
      }
      recordStores = null;
      f = null;
      return names;
    }
    return null;
  }

  public String getName() {
    return RecordStore.RecordStoreName;
  }

  public int getVersion() {
    return recordStoreVersion;
  }

  public int getSize() {
    return recordStoreSize;
  }

  public int getSizeAvailable() {
    File f = new File(RecordStore.RMSDirectory + "/" + RecordStore.RecordStoreName);
    int size = 0;
    if ((f = new File(RecordStore.RMSDirectory + "/" + RecordStore.RecordStoreName)).exists() && f.isDirectory()) {
      File recordList[] = f.listFiles();
      for (final File aRecordList : recordList) {
        if (aRecordList.getName().endsWith(".rms")) {
          size += aRecordList.length();
        }
      }
      recordList = null;
      f = null;
      RecordStore.sizeAvailable = size;
      return size;
    }
    return 0;
  }

  public int getNumRecords() {
    File f = new File(RecordStore.RMSDirectory + "/" + RecordStore.RecordStoreName);
    if (f.isDirectory()) {
      final int len = f.listFiles().length;
      final String recordsList[] = f.list();
      int count = 0;
      for (int i = 0; i < len; i++) {
        if (recordsList[i].endsWith(".rms")) {
          count++;
        }
      }
      f = null;
      return count;
    }

    return 0;
  }

  public long getLastModified() {
    File f = new File(RecordStore.RMSDirectory + "/" + RecordStore.RecordStoreName);
    if (f.exists()) {
      final long modifiedtime = f.lastModified();
      f = null;
      return modifiedtime;
    }
    return 0;
  }

  public void addRecordListener(final RecordListener listener) {
    if (!listeners.contains(listener)) {
      listeners.addElement(listener);
    }
  }

  public void removeRecordListener(final RecordListener listener) {
    listeners.removeElement(listener);
  }

  public int getNextRecordID() {
    return recordID + 1;
  }

  public int addRecord(byte[] data, final int offset, final int numBytes) throws IOException, RecordStoreException {
    if (RecordStore.RecordStoreName == null) {
      new RecordStoreException("RecordStore closed");
      return 0;
    }
    if ((getNumRecords() > RecordStore.maxRecords) && (RecordStore.maxRecords > 0)) {
      System.out.println("Max No Of Records Reached Not Adding Record");
      return recordID;
    }
    if (RecordStore.isRMSDebugEnabled) {
      System.out.println("Adding Record to " + RecordStore.RecordStoreName + "of length " + data.length + " nobytes " + numBytes + " data is " + new String(data));
    }
    File recordStore;
    try {
      if (!(recordStore = new File(RecordStore.RMSDirectory + "/" + RecordStore.RecordStoreName + "/")).exists() && !recordStore.isDirectory()) {
        recordStore.mkdir();
      }
      ++recordID;
      FileOutputStream fo = new FileOutputStream(getRecordPath(recordID));
      if (data != null) {
        fo.write(data, offset, numBytes);
      }
      fo.close();
      fo = null;
      data = null;
      if (RecordStore.isRMSDebugEnabled) {
        System.out.println("written bytes " + numBytes + " to record" + RecordStore.RecordStoreName + " " + recordID);
      }
      recordID++;
    }
    catch (final Exception e) {
      throw new RecordStoreException();
    }
    recordStore = null;
    listenerNotify(this, recordID, RecordStore.RECORD_ADDED);
    return recordID;
  }

  public void deleteRecord(final int recordId) {
    File f = new File(getRecordPath(recordId));
    if (f.exists()) {
      f.delete();
      f = null;
    }
    else {
      new RecordStoreException("Record" + recordId + "Not Found");
    }
    if (RecordStore.isRMSDebugEnabled) {
      System.out.println("deleted Record " + recordId);
    }
    listenerNotify(this, recordId, RecordStore.RECORD_DELETED);
  }

  public int getRecordSize(final int recordId) {
    File f;
    if ((f = new File(getRecordPath(recordId))).exists()) { return (int)f.length(); }
    return recordStoreSize;
  }

  public int getRecord(final int recordId, final byte[] buffer, final int offset) throws RecordStoreNotFoundException, RecordStoreException {
    if (RecordStore.RecordStoreName == null) {
      new RecordStoreException("RecordStore closed");
      return 0;
    }
    DataInputStream di;
    final String recordPath = getRecordPath(recordId);
    if (!new File(recordPath).exists()) {
      new InvalidRecordIDException();
    }
    try {
      final File recordFile = new File(recordPath);
      di = new DataInputStream(new FileInputStream(recordFile));
      recordData = new byte[(int)recordFile.length()];
      if (buffer.length > recordData.length) {
        di.readFully(buffer, offset, (int)recordFile.length());
      }
      else {
        throw new ArrayIndexOutOfBoundsException();
        // this is causing array index out of bounds if buffer size >
        // recordData
        // System.arraycopy(recordData, offset, buffer, 0,
        // buffer.length);
      }
      if (RecordStore.isRMSDebugEnabled) {
        System.out.println("data readed is " + new String(buffer, "UTF-8"));
      }
      di.close();
      di = null;
      final int len = recordData.length;
      recordData = null;
      return len;
    }
    catch (final FileNotFoundException ex) {
      throw new RecordStoreNotFoundException("Record " + recordId + " Not Found");
    }
    catch (final IOException ex) {
      throw new RecordStoreException();
    }
  }

  public byte[] getRecord(final int recordId) throws InvalidRecordIDException, RecordStoreException {
    if (RecordStore.RecordStoreName == null) {
      new RecordStoreException("RecordStore closed ");
      return null;
    }
    if (RecordStore.isRMSDebugEnabled) {
      System.out.println("Getting Record " + recordId + " from RecordStore " + RecordStore.RecordStoreName);
    }
    final String recordPath = getRecordPath(recordId);
    if ((RecordStore.RecordStoreName != null) && !new File(recordPath).exists()) { throw new InvalidRecordIDException(); }
    try {
      File recordFile;
      if ((recordFile = new File(recordPath)).length() > 0L) {
        DataInputStream di;
        di = new DataInputStream(new FileInputStream(recordFile));
        recordData = new byte[(int)recordFile.length()];
        di.readFully(recordData);
        if (RecordStore.isRMSDebugEnabled) {
          System.out.println("data of record is " + new String(recordData, "UTF-8"));
        }
        di.close();
        di = null;
        return recordData;
      }
      return null;
    }
    catch (final Exception ex) {
      throw new RecordStoreException();
    }
  }

  public void setRecord(final int recordId, final byte[] newData, final int offset, final int numBytes) throws RecordStoreException {
    FileOutputStream fo;
    try {
      fo = new FileOutputStream(getRecordPath(recordId));
      fo.write(newData, offset, numBytes);
      fo.flush();
      fo.close();
      fo = null;
    }
    catch (final Exception ex) {
      throw new RecordStoreException("ERROR While Creating Record with recordID" + recordId);
    }
    listenerNotify(this, recordId, RecordStore.RECORD_CHANGED);
  }

  public RecordEnumeration enumerateRecords(final RecordFilter filter, final RecordComparator comparator, final boolean keepUpdated) {
    return new RecordEnumerationImplementation(this);
  }

  private void listenerNotify(final RecordStore rs, final int recId, final int operation) {
    for (int i = 0; i < listeners.size(); i++) {
      if (operation == RecordStore.RECORD_ADDED) {
        ((RecordListener)listeners.elementAt(i)).recordAdded(this, recId);
      }
      else if (operation == RecordStore.RECORD_CHANGED) {
        ((RecordListener)listeners.elementAt(i)).recordChanged(this, recId);
      }
      else if (operation == RecordStore.RECORD_DELETED) {
        ((RecordListener)listeners.elementAt(i)).recordDeleted(this, recId);
      }
    }
  }

  private String getRecordPath(final int recID) {
    return RecordStore.RMSDirectory + "/" + RecordStore.RecordStoreName + "/" + RecordStore.RecordStoreName + "_" + recID + ".rms";
  }
  // MIDP 3.0

  public static RecordStore importRecordStore(final java.io.InputStream is, final String importPassword, final String internalPassword) {
    return null;
  }

  public static void exportRecordStore(final OutputStream os, final String recordStoreName, final String internalPassword, final String exportPassword) throws IOException, RecordStoreException, IllegalArgumentException, RecordStoreNotFoundException, SecureRecordStoreException {
  }

}

class recordStoreNameFilter implements FilenameFilter {

  @Override
  public final boolean accept(final File paramFile, final String paramString) {
    return paramString.startsWith("#");
  }

}
