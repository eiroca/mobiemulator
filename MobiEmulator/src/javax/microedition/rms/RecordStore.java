/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.rms;

//~--- non-JDK imports --------------------------------------------------------

import Emulator.Main;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class RecordStore {
    // static RecordStore rs=null;
    public static final int  AUTHMODE_ANY      = 1;
    public static final int  AUTHMODE_PRIVATE  = 0;
    private static final int RECORD_ADDED      = 0;
    private static final int RECORD_CHANGED    = 1;
    private static final int RECORD_DELETED    = 2;
    // public static String recordStorePath = "C:/";
    public static String     RMSDirectory      = Main.getRmsDir();
    public static boolean    isRMSDebugEnabled = false;
    public static int        maxRecords        = 0;
    public static int        maxRecordsStores  = 0;
    public static int        mode              = AUTHMODE_PRIVATE;
    private static String    RecordStoreName;
    public static int        recordSize;
    public static int        sizeAvailable;
    private int              recordID        = 1;
    private int              recordStoreSize = 0;
    private Vector           listeners       = new Vector();
    public RecordEnumeration RE;
    private File             RMSDir;
    private byte[]           recordData;
    private File             recordStoreFile;
    private int              recordStoreVersion;
    public RecordStore(String recordStoreName, boolean createIfNecessary) throws RecordStoreNotFoundException {
        RecordStore.RecordStoreName = "#" + recordStoreName;
        if (!(RMSDir = new File(RMSDirectory)).exists() &&!RMSDir.isDirectory()) {
            RMSDir.mkdir();
        }
        recordID = getNumRecords();
        try {
            if (createIfNecessary) {
                if (!(recordStoreFile = new File(RMSDirectory + "/" + RecordStoreName)).exists()
                        &&!recordStoreFile.isDirectory()) {
                    if (isRMSDebugEnabled) {
                        System.out.println("Creating recordstore " + recordStoreName);
                    }
                    recordStoreFile.mkdir();
                }
            } else {
                return;
            }
        } catch (Exception e) {
            throw new RecordStoreNotFoundException(recordStoreName);
        }
    }

    public static void deleteRecordStore(String RecordStoreName) {
        // rs=new RecordStore();
        File f = new File(RMSDirectory + "/" + RecordStoreName);
        // System.out.println("..........delete recordstore " +
        // RecordStoreName);
        if (f.exists()) {
            if (f.isDirectory()) {
                File files[] = f.listFiles();
                for (File file : files) {
                    if (isRMSDebugEnabled) {
                        System.out.println("record deleted " + file.getName());
                    }
                    file.delete();
                }
            }
            if (isRMSDebugEnabled) {
                System.out.println("" + RecordStoreName + " recordstore deleted ");
            }
            f.delete();
            f = null;
        }
//      else {
//          //new RecordStoreNotFoundException();
//      }
    }
    public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary)
            throws RecordStoreNotFoundException {
        return openRecordStore(recordStoreName, createIfNecessary, 0, true);
    }
    public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary, int authmode,
            boolean writable) {
        mode = authmode;
        if (recordStoreName.startsWith("/")) {
            recordStoreName = recordStoreName.substring(1);
        }
        if (createIfNecessary || isRecordStoreAvailable(recordStoreName)) {
            try {
                return new RecordStore(recordStoreName, createIfNecessary);
            } catch (RecordStoreNotFoundException ex) {}
        }

        return null;
        // throw new RecordStoreNotFoundException(recordStoreName);
    }
    public static RecordStore openRecordStore(String recordStoreName, String vendorName, String suiteName)
            throws RecordStoreNotFoundException {
        return openRecordStore(recordStoreName, false);
    }
    public static boolean isRecordStoreAvailable(String recordStoreName) {
        return (new File(RMSDirectory + "/" + RecordStoreName)).exists();
    }
    public void setMode(int authmode, boolean writable) {}
    public void closeRecordStore() {
        if (isRMSDebugEnabled) {
            System.out.println("closing RecordStore " + RecordStoreName);
            // RecordStoreName = null;
        }
    }
    public static String[] listRecordStores() {
        File f = new File(RMSDirectory);
        if ((f = new File(RMSDirectory)).exists() && f.isDirectory()) {
            File   recordStores[] = f.listFiles(new recordStoreNameFilter());
            String names[]        = new String[recordStores.length];
            for (int i = 0;i < recordStores.length;i++) {
                names[i] = recordStores[i].getName();
            }
            recordStores = null;
            f            = null;

            return names;
        }

        return null;
    }
    public String getName() {
        return RecordStoreName;
    }
    public int getVersion() {
        return recordStoreVersion;
    }
    public int getSize() {
        return recordStoreSize;
    }
    public int getSizeAvailable() {
        File f    = new File(RMSDirectory + "/" + RecordStoreName);
        int  size = 0;
        if ((f = new File(RMSDirectory + "/" + RecordStoreName)).exists() && f.isDirectory()) {
            File recordList[] = f.listFiles();
            for (File aRecordList : recordList) {
                if (aRecordList.getName().endsWith(".rms")) {
                    size += aRecordList.length();
                }
            }
            recordList    = null;
            f             = null;
            sizeAvailable = size;

            return size;
        }

        return 0;
    }
    public int getNumRecords() {
        File f = new File(RMSDirectory + "/" + RecordStoreName);
        if (f.isDirectory()) {
            int    len           = f.listFiles().length;
            String recordsList[] = f.list();
            int    count         = 0;
            for (int i = 0;i < len;i++) {
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
        File f = new File(RMSDirectory + "/" + RecordStoreName);
        if (f.exists()) {
            long modifiedtime = f.lastModified();
            f = null;

            return modifiedtime;
        }

        return 0;
    }
    public void addRecordListener(RecordListener listener) {
        if (!listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }
    public void removeRecordListener(RecordListener listener) {
        listeners.removeElement(listener);
    }
    public int getNextRecordID() {
        return recordID + 1;
    }
    public int addRecord(byte[] data, int offset, int numBytes) throws IOException, RecordStoreException {
        if (RecordStoreName == null) {
            new RecordStoreException("RecordStore closed");

            return 0;
        }
        if ((getNumRecords() > maxRecords) && (maxRecords > 0)) {
            System.out.println("Max No Of Records Reached Not Adding Record");

            return recordID;
        }
        if (isRMSDebugEnabled) {
            System.out.println("Adding Record to " + RecordStoreName + "of length " + data.length + " nobytes "
                               + numBytes + " data is " + new String(data));
        }
        File recordStore;
        try {
            if (!(recordStore = new File(RMSDirectory + "/" + RecordStoreName + "/")).exists()
                    &&!recordStore.isDirectory()) {
                recordStore.mkdir();
            }
            ++recordID;
            FileOutputStream fo = new FileOutputStream(getRecordPath(recordID));
            if (data != null) {
                fo.write(data, offset, numBytes);
            }
            fo.close();
            fo   = null;
            data = null;
            if (isRMSDebugEnabled) {
                System.out.println("written bytes " + numBytes + " to record" + RecordStoreName + " " + recordID);
            }
            recordID++;
        } catch (Exception e) {
            throw new RecordStoreException();
        }
        recordStore = null;
        listenerNotify(this, recordID, RECORD_ADDED);

        return recordID;
    }
    public void deleteRecord(int recordId) {
        File f = new File(getRecordPath(recordId));
        if (f.exists()) {
            f.delete();
            f = null;
        } else {
            new RecordStoreException("Record" + recordId + "Not Found");
        }
        if (isRMSDebugEnabled) {
            System.out.println("deleted Record " + recordId);
        }
        listenerNotify(this, recordId, RECORD_DELETED);
    }
    public int getRecordSize(int recordId) {
        File f;
        if ((f = new File(getRecordPath(recordId))).exists()) {
            return (int) f.length();
        }

        return recordStoreSize;
    }
    public int getRecord(int recordId, byte[] buffer, int offset)
            throws RecordStoreNotFoundException, RecordStoreException {
        if (RecordStoreName == null) {
            new RecordStoreException("RecordStore closed");

            return 0;
        }
        DataInputStream di;
        String          recordPath = getRecordPath(recordId);
        if (!new File(recordPath).exists()) {
            new InvalidRecordIDException();
        }
        try {
            File recordFile = new File(recordPath);
            di         = new DataInputStream(new FileInputStream(recordFile));
            recordData = new byte[(int) recordFile.length()];
            if (buffer.length > recordData.length) {
                di.readFully(buffer, offset, (int) recordFile.length());
            } else {
                throw new ArrayIndexOutOfBoundsException();
                // this is causing array index out of bounds if buffer size >
                // recordData
                // System.arraycopy(recordData, offset, buffer, 0,
                // buffer.length);
            }
            if (isRMSDebugEnabled) {
                System.out.println("data readed is " + new String(buffer, "UTF-8"));
            }
            di.close();
            di = null;
            int len = recordData.length;
            recordData = null;

            return len;
        } catch (FileNotFoundException ex) {
            throw new RecordStoreNotFoundException("Record " + recordId + " Not Found");
        } catch (IOException ex) {
            throw new RecordStoreException();
        }
    }
    public byte[] getRecord(int recordId) throws InvalidRecordIDException, RecordStoreException {
        if (RecordStoreName == null) {
            new RecordStoreException("RecordStore closed ");

            return null;
        }
        if (isRMSDebugEnabled) {
            System.out.println("Getting Record " + recordId + " from RecordStore " + RecordStoreName);
        }
        String recordPath = getRecordPath(recordId);
        if ((RecordStoreName != null) &&!new File(recordPath).exists()) {
            throw new InvalidRecordIDException();
        }
        try {
            File recordFile;
            if ((recordFile = new File(recordPath)).length() > 0L) {
                DataInputStream di;
                di         = new DataInputStream(new FileInputStream(recordFile));
                recordData = new byte[(int) recordFile.length()];
                di.readFully(recordData);
                if (isRMSDebugEnabled) {
                    System.out.println("data of record is " + new String(recordData, "UTF-8"));
                }
                di.close();
                di = null;

                return recordData;
            }

            return null;
        } catch (Exception ex) {
            throw new RecordStoreException();
        }
    }
    public void setRecord(int recordId, byte[] newData, int offset, int numBytes) throws RecordStoreException {
        FileOutputStream fo;
        try {
            fo = new FileOutputStream(getRecordPath(recordId));
            fo.write(newData, offset, numBytes);
            fo.flush();
            fo.close();
            fo = null;
        } catch (Exception ex) {
            throw new RecordStoreException("ERROR While Creating Record with recordID" + recordId);
        }
        listenerNotify(this, recordId, RECORD_CHANGED);
    }
    public RecordEnumeration enumerateRecords(RecordFilter filter, RecordComparator comparator, boolean keepUpdated) {
        return new RecordEnumerationImplementation(this);
    }
    private void listenerNotify(RecordStore rs, int recId, int operation) {
        for (int i = 0;i < this.listeners.size();i++) {
            if (operation == RECORD_ADDED) {
                ((RecordListener) this.listeners.elementAt(i)).recordAdded(this, recId);
            } else if (operation == RECORD_CHANGED) {
                ((RecordListener) this.listeners.elementAt(i)).recordChanged(this, recId);
            } else if (operation == RECORD_DELETED) {
                ((RecordListener) this.listeners.elementAt(i)).recordDeleted(this, recId);
            }
        }
    }
    private String getRecordPath(int recID) {
        return RMSDirectory + "/" + RecordStore.RecordStoreName + "/" + RecordStore.RecordStoreName + "_" + recID
               + ".rms";
    }
    // MIDP 3.0

    public static RecordStore importRecordStore(java.io.InputStream is, java.lang.String importPassword,
            java.lang.String internalPassword) {
        return null;
    }
    public static void exportRecordStore(java.io.OutputStream os, java.lang.String recordStoreName,
            java.lang.String internalPassword, java.lang.String exportPassword)
            throws java.io.IOException, RecordStoreException, java.lang.IllegalArgumentException,
                   RecordStoreNotFoundException, SecureRecordStoreException {}
}

class recordStoreNameFilter implements FilenameFilter {
    public final boolean accept(File paramFile, String paramString) {
        return paramString.startsWith("#");
    }
}
