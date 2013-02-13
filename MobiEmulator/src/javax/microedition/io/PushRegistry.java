/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
import java.io.IOException;

public class PushRegistry {
    public static void registerConnection(String paramString1, String paramString2, String paramString3)
            throws ClassNotFoundException, IOException {}
    public static boolean unregisterConnection(String paramString) {
        return false;
    }
    public static String[] listConnections(boolean paramBoolean) {
        return null;
    }
    public static String getMIDlet(String paramString) {
        return null;
    }
    public static String getFilter(String paramString) {
        return null;
    }
    public static long registerAlarm(String paramString, long paramLong)
            throws ClassNotFoundException, ConnectionNotFoundException {
        return 0;
    }
}
