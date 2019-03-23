package javax.microedition.pim.formats;

// ~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.Writer;

// Referenced classes of package javax.microedition.pim.formats:
// VCardFormat, VCardSupport, FormatSupport
public class VCard30Format extends VCardFormat {

  public VCard30Format() {
  }

  @Override
  protected void writeAttributes(final Writer w, final int attributes) throws IOException {
    boolean writtenData = false;
    for (int i = 0; i < 32; i++) {
      final long mask = 1L << i;
      if ((attributes & mask) == 0L) {
        continue;
      }
      final String attributeLabel = VCardSupport.getAttributeLabel((int)mask);
      if (attributeLabel == null) {
        continue;
      }
      if (writtenData) {
        w.write(",");
      }
      else {
        w.write(";TYPE=");
        writtenData = true;
      }
      w.write(attributeLabel);
    }
  }

  @Override
  protected int parseAttributes(final String attributes[]) {
    int code = 0;
    for (final String attribute : attributes) {
      if (attribute.startsWith("TYPE=")) {
        final String s[] = FormatSupport.split(attribute, ',', 5);
        for (final String element : s) {
          code |= VCardSupport.getAttributeCode(element, 0);
        }
      }
      else {
        code |= VCardSupport.getAttributeCode(attribute, 0);
      }
    }

    return code;
  }

  @Override
  protected String getBinaryEncodingName() {
    return "B";
  }

  @Override
  protected String getCategoryProperty() {
    return "CATEGORY";
  }

  @Override
  protected String getClassProperty() {
    return "CLASS";
  }

  @Override
  protected String getVersion() {
    return "3.0";
  }
}
