/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

//~--- JDK imports ------------------------------------------------------------

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class HttpConnectionImplementation implements HttpConnection {
    String            Url     = null;
    URL               httpURL = null;
    HttpURLConnection urlconn = null;
    public HttpConnectionImplementation(String name) {
        Url = name;
        try {
            httpURL = new URL(name);
            urlconn = (HttpURLConnection) httpURL.openConnection();
            urlconn.setDoInput(true);
            urlconn.setDoOutput(true);
        } catch (Exception ex) {
            Logger.getLogger(HttpConnectionImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getURL() {
        return Url;
    }
    public String getProtocol() {
        return httpURL.getProtocol();
    }
    public String getHost() {
        return httpURL.getHost();
    }
    public String getFile() {
        return httpURL.getFile();
    }
    public String getRef() {
        return httpURL.getRef();
    }
    public String getQuery() {
        return httpURL.getQuery();
    }
    public int getPort() {
        return httpURL.getPort();
    }
    public String getRequestMethod() {
        return urlconn.getRequestMethod();
    }
    public void setRequestMethod(String arg0) throws IOException {
        urlconn.setRequestMethod(arg0);
    }
    public String getRequestProperty(String GET) {
        return urlconn.getRequestProperty(GET);
    }
    public void setRequestProperty(String arg0, String arg1) throws IOException {
        urlconn.setRequestProperty(arg0, arg1);
    }
    public int getResponseCode() throws IOException {
        return urlconn.getResponseCode();
    }
    public String getResponseMessage() throws IOException {
        return urlconn.getResponseMessage();
    }
    public long getExpiration() throws IOException {
        return urlconn.getExpiration();
    }
    public long getDate() throws IOException {
        return urlconn.getDate();
    }
    public long getLastModified() throws IOException {
        return urlconn.getLastModified();
    }
    public String getHeaderField(String arg0) throws IOException {
        return urlconn.getHeaderField(arg0);
    }
    public int getHeaderFieldInt(String arg0, int arg1) throws IOException {
        return urlconn.getHeaderFieldInt(arg0, arg1);
    }
    public long getHeaderFieldDate(String arg0, long arg1) throws IOException {
        return urlconn.getHeaderFieldDate(arg0, arg1);
    }
    public String getHeaderField(int arg0) throws IOException {
        return urlconn.getHeaderField(arg0);
    }
    public String getHeaderFieldKey(int arg0) throws IOException {
        return urlconn.getHeaderFieldKey(arg0);
    }
    public String getEncoding() {
        return urlconn.getContentEncoding();
    }
    public String getType() {
        return urlconn.getContentType();
    }
    public long getLength() {
        if (urlconn != null) {
            try {
                return urlconn.getInputStream().available();
            } catch (IOException ex) {
                Logger.getLogger(HttpConnectionImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }
    public InputStream openInputStream() throws IOException {
        if (urlconn != null) {
            return urlconn.getInputStream();
        } else {
            System.out.println("Urlconnection exception ");

            return null;
        }
    }
    public DataInputStream openDataInputStream() throws IOException {
        if (urlconn != null) {
            return new DataInputStream(urlconn.getInputStream());
        } else {
            System.out.println("Urlconnection exception ");

            return null;
        }
    }
    public void close() {
        urlconn.disconnect();
    }
    public OutputStream openOutputStream() throws IOException {
        if (urlconn != null) {
            return urlconn.getOutputStream();
        } else {
            System.out.println(" Connection Exception ");

            return null;
        }
    }
    public DataOutputStream openDataOutputStream() throws IOException {
        if (urlconn != null) {
            return new DataOutputStream(urlconn.getOutputStream());
        } else {
            System.out.println(" Connection Exception ");

            return null;
        }
    }
}
