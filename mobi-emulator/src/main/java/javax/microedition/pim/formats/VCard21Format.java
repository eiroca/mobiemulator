package javax.microedition.pim.formats;

// ~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.Writer;

// Referenced classes of package javax.microedition.pim.formats:
// VCardFormat, VCardSupport
public class VCard21Format extends VCardFormat {

  private static final String PROPERTY_CATEGORY = "X-J2MEWTK-CATEGORY";
  private static final String PROPERTY_CLASS = "X-J2MEWTK-CLASS";

  public VCard21Format() {
  }

  @Override
  protected void writeAttributes(final Writer w, final int attributes) throws IOException {
    for (int i = 0; i < 32; i++) {
      final long mask = 1L << i;
      if ((attributes & mask) == 0L) {
        continue;
      }
      final String attributeLabel = VCardSupport.getAttributeLabel((int)mask);
      if (attributeLabel != null) {
        w.write(59);
        w.write(attributeLabel);
      }
    }
  }

  @Override
  protected int parseAttributes(final String attributes[]) {
    int code = 0;
    for (final String attribute : attributes) {
      code |= VCardSupport.getAttributeCode(attribute, 0);
    }

    return code;
  }

  @Override
  protected String getBinaryEncodingName() {
    return "BASE64";
  }

  @Override
  protected String getCategoryProperty() {
    return "X-J2MEWTK-CATEGORY";
  }

  @Override
  protected String getClassProperty() {
    return "X-J2MEWTK-CLASS";
  }

  @Override
  protected String getVersion() {
    return "2.1";
  }
}
