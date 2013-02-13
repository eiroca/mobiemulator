/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.io.file.FileConnectionImpl;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Connector {
    public static final int  READ       = 1;
    public static final int  READ_WRITE = 3;
    public static final int  WRITE      = 2;
    public static Connection connection;
    public static Connection open(String name) throws IOException {
        return Connector.open(name, READ_WRITE, false);
    }
    public static Connection open(String name, int mode) throws IOException {
        return Connector.open(name, mode, false);
    }
    public static Connection open(String name, int mode, boolean timeouts) throws IOException {
        if (name.startsWith("http://")) {
            // HttpConnectionImplementation con=;
            connection = new HttpConnectionImplementation(name);
            // System.out.println("getLength is "+con.getLength()+" encoding "+con.getEncoding());
        }
        if (name.startsWith("file://")) {
            connection = new FileConnectionImpl(name);
        }

        return connection;
    }
    public static DataInputStream openDataInputStream(String name) throws IOException {
        return null;
    }
    public static DataOutputStream openDataOutputStream(String name) throws IOException {
        return null;
    }
    public static InputStream openInputStream(String name) throws IOException {
        return null;
    }
    public static OutputStream openOutputStream(String name) throws IOException {
        return null;
    }
}
