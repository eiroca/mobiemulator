/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.media;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Manager {
    public static final String TONE_DEVICE_LOCATOR = "device://tone";
    static String[]            protocols           = { "audio/midi", "x-wav", };
    public static Player createPlayer(InputStream stream, String type) throws IOException, MediaException {
        if (stream == null) {
            System.out.println("stream is null");
        }

        return new PlayerImplementation(stream, type);
    }
    public static String[] getSupportedContentTypes(String protocol) {
        return protocols;
    }
    public static String[] getSupportedProtocols(String content_type) {
        return protocols;
    }
    public static Player createPlayer(String locator) throws IOException, MediaException {
        try {
            return new PlayerImplementation(locator);
        } catch (Exception ex) {
            new Exception("Unsupported File Type");
        }

        return null;
    }
    public static void playTone(int note, int duration, int volume) throws MediaException {}
}
