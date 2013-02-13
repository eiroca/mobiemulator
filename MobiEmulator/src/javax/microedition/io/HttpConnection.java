/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface HttpConnection extends ContentConnection {
    public static final String GET                    = "GET";
    public static final String HEAD                   = "HEAD";
    public static final int    HTTP_ACCEPTED          = 202;
    public static final int    HTTP_BAD_GATEWAY       = 502;
    public static final int    HTTP_BAD_METHOD        = 405;
    public static final int    HTTP_BAD_REQUEST       = 400;
    public static final int    HTTP_CLIENT_TIMEOUT    = 408;
    public static final int    HTTP_CONFLICT          = 409;
    public static final int    HTTP_CREATED           = 201;
    public static final int    HTTP_ENTITY_TOO_LARGE  = 413;
    public static final int    HTTP_EXPECT_FAILED     = 417;
    public static final int    HTTP_FORBIDDEN         = 403;
    public static final int    HTTP_GATEWAY_TIMEOUT   = 504;
    public static final int    HTTP_GONE              = 410;
    public static final int    HTTP_INTERNAL_ERROR    = 500;
    public static final int    HTTP_LENGTH_REQUIRED   = 411;
    public static final int    HTTP_MOVED_PERM        = 301;
    public static final int    HTTP_MOVED_TEMP        = 302;
    public static final int    HTTP_MULT_CHOICE       = 300;
    public static final int    HTTP_NOT_ACCEPTABLE    = 406;
    public static final int    HTTP_NOT_AUTHORITATIVE = 203;
    public static final int    HTTP_NOT_FOUND         = 404;
    public static final int    HTTP_NOT_IMPLEMENTED   = 501;
    public static final int    HTTP_NOT_MODIFIED      = 304;
    public static final int    HTTP_NO_CONTENT        = 204;
    public static final int    HTTP_OK                = 200;
    public static final int    HTTP_PARTIAL           = 206;
    public static final int    HTTP_PAYMENT_REQUIRED  = 402;
    public static final int    HTTP_PRECON_FAILED     = 412;
    public static final int    HTTP_PROXY_AUTH        = 407;
    public static final int    HTTP_REQ_TOO_LONG      = 414;
    public static final int    HTTP_RESET             = 205;
    public static final int    HTTP_SEE_OTHER         = 303;
    public static final int    HTTP_TEMP_REDIRECT     = 307;
    public static final int    HTTP_UNAUTHORIZED      = 401;
    public static final int    HTTP_UNAVAILABLE       = 503;
    public static final int    HTTP_UNSUPPORTED_RANGE = 416;
    public static final int    HTTP_UNSUPPORTED_TYPE  = 415;
    public static final int    HTTP_USE_PROXY         = 305;
    public static final int    HTTP_VERSION           = 505;
    public static final String POST                   = "POST";
    public abstract String getURL();
    public abstract String getProtocol();
    public abstract String getHost();
    public abstract String getFile();
    public abstract String getRef();
    public abstract String getQuery();
    public abstract int getPort();
    public abstract String getRequestMethod();
    public abstract void setRequestMethod(String arg0) throws IOException;
    public abstract String getRequestProperty(String arg0);
    public abstract void setRequestProperty(String arg0, String arg1) throws IOException;
    public abstract int getResponseCode() throws IOException;
    public abstract String getResponseMessage() throws IOException;
    public abstract long getExpiration() throws IOException;
    public abstract long getDate() throws IOException;
    public abstract long getLastModified() throws IOException;
    public abstract String getHeaderField(String arg0) throws IOException;
    public abstract int getHeaderFieldInt(String arg0, int arg1) throws IOException;
    public abstract long getHeaderFieldDate(String arg0, long arg1) throws IOException;
    public abstract String getHeaderField(int arg0) throws IOException;
    public abstract String getHeaderFieldKey(int arg0) throws IOException;
}
