package javax.microedition.io;

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

  String Url = null;
  URL httpURL = null;
  HttpURLConnection urlconn = null;

  public HttpConnectionImplementation(final String name) {
    Url = name;
    try {
      httpURL = new URL(name);
      urlconn = (HttpURLConnection)httpURL.openConnection();
      urlconn.setDoInput(true);
      urlconn.setDoOutput(true);
    }
    catch (final Exception ex) {
      Logger.getLogger(HttpConnectionImplementation.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public String getURL() {
    return Url;
  }

  @Override
  public String getProtocol() {
    return httpURL.getProtocol();
  }

  @Override
  public String getHost() {
    return httpURL.getHost();
  }

  @Override
  public String getFile() {
    return httpURL.getFile();
  }

  @Override
  public String getRef() {
    return httpURL.getRef();
  }

  @Override
  public String getQuery() {
    return httpURL.getQuery();
  }

  @Override
  public int getPort() {
    return httpURL.getPort();
  }

  @Override
  public String getRequestMethod() {
    return urlconn.getRequestMethod();
  }

  @Override
  public void setRequestMethod(final String arg0) throws IOException {
    urlconn.setRequestMethod(arg0);
  }

  @Override
  public String getRequestProperty(final String GET) {
    return urlconn.getRequestProperty(GET);
  }

  @Override
  public void setRequestProperty(final String arg0, final String arg1) throws IOException {
    urlconn.setRequestProperty(arg0, arg1);
  }

  @Override
  public int getResponseCode() throws IOException {
    return urlconn.getResponseCode();
  }

  @Override
  public String getResponseMessage() throws IOException {
    return urlconn.getResponseMessage();
  }

  @Override
  public long getExpiration() throws IOException {
    return urlconn.getExpiration();
  }

  @Override
  public long getDate() throws IOException {
    return urlconn.getDate();
  }

  @Override
  public long getLastModified() throws IOException {
    return urlconn.getLastModified();
  }

  @Override
  public String getHeaderField(final String arg0) throws IOException {
    return urlconn.getHeaderField(arg0);
  }

  @Override
  public int getHeaderFieldInt(final String arg0, final int arg1) throws IOException {
    return urlconn.getHeaderFieldInt(arg0, arg1);
  }

  @Override
  public long getHeaderFieldDate(final String arg0, final long arg1) throws IOException {
    return urlconn.getHeaderFieldDate(arg0, arg1);
  }

  @Override
  public String getHeaderField(final int arg0) throws IOException {
    return urlconn.getHeaderField(arg0);
  }

  @Override
  public String getHeaderFieldKey(final int arg0) throws IOException {
    return urlconn.getHeaderFieldKey(arg0);
  }

  @Override
  public String getEncoding() {
    return urlconn.getContentEncoding();
  }

  @Override
  public String getType() {
    return urlconn.getContentType();
  }

  @Override
  public long getLength() {
    if (urlconn != null) {
      try {
        return urlconn.getInputStream().available();
      }
      catch (final IOException ex) {
        Logger.getLogger(HttpConnectionImplementation.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    return 0;
  }

  @Override
  public InputStream openInputStream() throws IOException {
    if (urlconn != null) {
      return urlconn.getInputStream();
    }
    else {
      System.out.println("Urlconnection exception ");

      return null;
    }
  }

  @Override
  public DataInputStream openDataInputStream() throws IOException {
    if (urlconn != null) {
      return new DataInputStream(urlconn.getInputStream());
    }
    else {
      System.out.println("Urlconnection exception ");

      return null;
    }
  }

  @Override
  public void close() {
    urlconn.disconnect();
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    if (urlconn != null) {
      return urlconn.getOutputStream();
    }
    else {
      System.out.println(" Connection Exception ");

      return null;
    }
  }

  @Override
  public DataOutputStream openDataOutputStream() throws IOException {
    if (urlconn != null) {
      return new DataOutputStream(urlconn.getOutputStream());
    }
    else {
      System.out.println(" Connection Exception ");

      return null;
    }
  }

}
