package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

//Referenced classes of package javax.microedition.pim:
//           PIMException, PIMList, PIMItem
public abstract class PIM {
    public static final int CONTACT_LIST = 1;
    public static final int EVENT_LIST   = 2;
    public static final int READ_ONLY    = 1;
    public static final int READ_WRITE   = 3;
    public static final int TODO_LIST    = 3;
    public static final int WRITE_ONLY   = 2;
    private static PIM      instance;
    protected PIM() {}

    public static PIM getInstance() {
        synchronized (PIM.class) {
            if (instance == null) {
                try {
                    instance = (PIM) Class.forName("javax.microedition.pim.PIMImpl").newInstance();
                } catch (Exception e) {
                    e.printStackTrace();

                    throw new Error("PIM implementation " + "javax.microedition.pim.PIMImpl"
                                    + " could not be initialized.");
                }
            }

            return instance;
        }
    }
    public abstract PIMList openPIMList(int i, int j) throws PIMException;
    public abstract PIMList openPIMList(int i, int j, String s) throws PIMException;
    public abstract String[] listPIMLists(int i);
    public abstract PIMItem[] fromSerialFormat(InputStream inputstream, String s)
            throws PIMException, UnsupportedEncodingException;
    public abstract void toSerialFormat(PIMItem pimitem, OutputStream outputstream, String s, String s1)
            throws PIMException, UnsupportedEncodingException;
    public abstract String[] supportedSerialFormats(int i);
}
