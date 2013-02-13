/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class RecordStoreFullException extends RecordStoreException {
    public RecordStoreFullException() {
        Throwable t = new Throwable();
        t.printStackTrace();
    }

    public RecordStoreFullException(String message) {
        System.out.println(message);
        Throwable t = new Throwable();
        t.printStackTrace();
    }
}
