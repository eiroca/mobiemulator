/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class RecordStoreNotFoundException extends RecordStoreException {
    public RecordStoreNotFoundException() {
        Throwable t = new Throwable();
        t.printStackTrace();
    }

    public RecordStoreNotFoundException(String message) {
        System.out.println(message);
        Throwable t = new Throwable();
        t.printStackTrace();
    }
}
