/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.bluetooth;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */

import javax.microedition.io.Connection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class L2CAPConnectionImpl implements L2CAPConnection {
    Socket       a;
    InputStream  b;
    OutputStream c;

    protected L2CAPConnectionImpl(Socket paramSocket) throws IOException {
        this.a = paramSocket;
        this.b = paramSocket.getInputStream();
        this.c = paramSocket.getOutputStream();
        paramSocket.setReceiveBufferSize(672);
        paramSocket.setSendBufferSize(672);
    }

    public int getTransmitMTU() throws IOException {
        return this.a.getSendBufferSize();
    }

    public int getReceiveMTU() throws IOException {
        return this.a.getReceiveBufferSize();
    }

    public void send(byte[] paramArrayOfByte) throws IOException {
        this.c.write(paramArrayOfByte, 0, Math.min(672, paramArrayOfByte.length));
    }

    public int receive(byte[] paramArrayOfByte) throws IOException {
        return this.b.read(paramArrayOfByte, 0, Math.min(672, paramArrayOfByte.length));
    }

    public boolean ready() throws IOException {
        return this.b.available() > 0;
    }

    public void close() {
        try {
            this.a.close();
        } catch (IOException ex) {
            Logger.getLogger(L2CAPConnectionImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection open(String paramString) throws IOException {
        String str1 = paramString.substring(0, paramString.indexOf("://"));
        int    i    = paramString.indexOf("://") + 3;
        String str2 = paramString.substring(i, paramString.indexOf(":", i));
        int    j    = paramString.indexOf(";");
        String str3 = paramString.substring(paramString.indexOf(":", i) + 1, (j >= 0) ? j : paramString.length());
        String str4 = (j >= 0) ? paramString.substring(j) : "";
        System.out.println("L2CAPConnection:");
        System.out.println("procotol=" + str1);
        System.out.println("host    =" + str2);
        System.out.println("uuid    =" + str3);
        System.out.println("options =" + str4);

        // if (paramString.indexOf("//localhost") >= 0)
        // return new a(str3);
        return new L2CAPConnectionImpl(new Socket(str2, Integer.parseInt(str3)));
    }
}
