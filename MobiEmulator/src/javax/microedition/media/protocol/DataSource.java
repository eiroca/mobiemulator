/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.media.protocol;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.media.Control;
import javax.microedition.media.Controllable;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class DataSource implements Controllable {
    private String locator;
    public DataSource(java.lang.String locator) {
        this.locator = locator;
    }

    public Control getControl(String controlType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public Control[] getControls() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public java.lang.String getLocator() {
        return locator;
    }
    public abstract java.lang.String getContentType();
    public abstract void connect() throws java.io.IOException;
    public abstract void disconnect();
    public abstract void start() throws java.io.IOException;
    public abstract void stop() throws java.io.IOException;
    public abstract SourceStream[] getStreams();
}
