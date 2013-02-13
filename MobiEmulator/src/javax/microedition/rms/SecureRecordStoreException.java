/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class SecureRecordStoreException extends Exception {
    public SecureRecordStoreException() {
        Throwable t = new Throwable();
        t.printStackTrace();
    }

    public SecureRecordStoreException(String message) {
        System.out.println(message);
        Throwable t = new Throwable();
        t.printStackTrace();
    }
}
