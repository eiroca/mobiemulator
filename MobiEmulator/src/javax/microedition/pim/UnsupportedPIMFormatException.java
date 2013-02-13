package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

public class UnsupportedPIMFormatException extends IOException {
    public UnsupportedPIMFormatException() {}

    public UnsupportedPIMFormatException(String message) {
        super(message);
    }
}
