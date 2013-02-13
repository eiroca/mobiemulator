/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface ContentConnection extends StreamConnection {
    public String getEncoding();
    public String getType();
    public long getLength();
}
