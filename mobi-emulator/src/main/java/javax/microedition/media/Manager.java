package javax.microedition.media;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Manager {

  public static final String TONE_DEVICE_LOCATOR = "device://tone";
  static String[] protocols = {
      "audio/midi", "x-wav",
  };

  public static Player createPlayer(final InputStream stream, final String type) throws IOException, MediaException {
    if (stream == null) {
      System.out.println("stream is null");
    }

    return new PlayerImplementation(stream, type);
  }

  public static String[] getSupportedContentTypes(final String protocol) {
    return Manager.protocols;
  }

  public static String[] getSupportedProtocols(final String content_type) {
    return Manager.protocols;
  }

  public static Player createPlayer(final String locator) throws IOException, MediaException {
    try {
      return new PlayerImplementation(locator);
    }
    catch (final Exception ex) {
      new Exception("Unsupported File Type");
    }

    return null;
  }

  public static void playTone(final int note, final int duration, final int volume) throws MediaException {
  }

}
