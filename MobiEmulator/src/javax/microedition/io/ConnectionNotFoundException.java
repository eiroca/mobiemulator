/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ConnectionNotFoundException extends Throwable {
    ConnectionNotFoundException() {
        this.printStackTrace();
    }

    ConnectionNotFoundException(final String Message) {
        System.out.println(Message);
        this.toString();
    }
}
