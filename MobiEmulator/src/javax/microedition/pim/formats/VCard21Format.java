package javax.microedition.pim.formats;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.Writer;

//Referenced classes of package javax.microedition.pim.formats:
//           VCardFormat, VCardSupport
public class VCard21Format extends VCardFormat {
    private static final String PROPERTY_CATEGORY = "X-J2MEWTK-CATEGORY";
    private static final String PROPERTY_CLASS    = "X-J2MEWTK-CLASS";
    public VCard21Format() {}

    protected void writeAttributes(Writer w, int attributes) throws IOException {
        for (int i = 0;i < 32;i++) {
            long mask = 1L << i;
            if (((long) attributes & mask) == 0L) {
                continue;
            }
            String attributeLabel = VCardSupport.getAttributeLabel((int) mask);
            if (attributeLabel != null) {
                w.write(59);
                w.write(attributeLabel);
            }
        }
    }
    protected int parseAttributes(String attributes[]) {
        int code = 0;
        for (int i = 0;i < attributes.length;i++) {
            code |= VCardSupport.getAttributeCode(attributes[i], 0);
        }

        return code;
    }
    protected String getBinaryEncodingName() {
        return "BASE64";
    }
    protected String getCategoryProperty() {
        return "X-J2MEWTK-CATEGORY";
    }
    protected String getClassProperty() {
        return "X-J2MEWTK-CLASS";
    }
    protected String getVersion() {
        return "2.1";
    }
}
