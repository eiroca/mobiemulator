package javax.microedition.pim.formats;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.Writer;

//Referenced classes of package javax.microedition.pim.formats:
//           VCardFormat, VCardSupport, FormatSupport
public class VCard30Format extends VCardFormat {
    public VCard30Format() {}

    protected void writeAttributes(Writer w, int attributes) throws IOException {
        boolean writtenData = false;
        for (int i = 0;i < 32;i++) {
            long mask = 1L << i;
            if (((long) attributes & mask) == 0L) {
                continue;
            }
            String attributeLabel = VCardSupport.getAttributeLabel((int) mask);
            if (attributeLabel == null) {
                continue;
            }
            if (writtenData) {
                w.write(",");
            } else {
                w.write(";TYPE=");
                writtenData = true;
            }
            w.write(attributeLabel);
        }
    }
    protected int parseAttributes(String attributes[]) {
        int code = 0;
        for (int i = 0;i < attributes.length;i++) {
            if (attributes[i].startsWith("TYPE=")) {
                String s[] = FormatSupport.split(attributes[i], ',', 5);
                for (int j = 0;j < s.length;j++) {
                    code |= VCardSupport.getAttributeCode(s[j], 0);
                }
            } else {
                code |= VCardSupport.getAttributeCode(attributes[i], 0);
            }
        }

        return code;
    }
    protected String getBinaryEncodingName() {
        return "B";
    }
    protected String getCategoryProperty() {
        return "CATEGORY";
    }
    protected String getClassProperty() {
        return "CLASS";
    }
    protected String getVersion() {
        return "3.0";
    }
}
