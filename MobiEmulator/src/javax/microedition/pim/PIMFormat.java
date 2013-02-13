package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//Referenced classes of package javax.microedition.pim:
//           UnsupportedPIMFormatException
public interface PIMFormat {
    public abstract String getName();
    public abstract boolean isTypeSupported(int i);
    public abstract PIMItem[] decode(InputStream inputstream, String s, PIMList pimlist)
            throws UnsupportedPIMFormatException, IOException;
    public abstract void encode(OutputStream outputstream, String s, PIMItem pimitem) throws IOException;
}
