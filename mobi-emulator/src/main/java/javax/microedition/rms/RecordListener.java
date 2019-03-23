package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface RecordListener {

  public abstract void recordAdded(RecordStore recordStore, int recordId);

  public abstract void recordDeleted(RecordStore recordStore, int recordId);

  public abstract void recordChanged(RecordStore recordStore, int recordId);

}
