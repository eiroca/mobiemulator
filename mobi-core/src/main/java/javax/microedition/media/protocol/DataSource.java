package javax.microedition.media.protocol;

import javax.microedition.media.Control;
import javax.microedition.media.Controllable;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class DataSource implements Controllable {

  private final String locator;

  public DataSource(final java.lang.String locator) {
    this.locator = locator;
  }

  @Override
  public Control getControl(final String controlType) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
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
