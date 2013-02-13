/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.rms;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class RecordStoreNotOpenException extends Exception {
    public RecordStoreNotOpenException() {
        Throwable t = new Throwable();
        t.printStackTrace();
    }

    public RecordStoreNotOpenException(String message) {
        System.out.println(message);
        Throwable t = new Throwable();
        t.printStackTrace();
    }
}
