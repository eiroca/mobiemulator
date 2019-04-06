package javax.microedition.pim.formats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactImpl;
import javax.microedition.pim.LineReader;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMFormat;
import javax.microedition.pim.PIMHandler;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;
import javax.microedition.pim.UnsupportedPIMFormatException;

// Referenced classes of package javax.microedition.pim.formats:
// EndMatcher, FormatSupport, VCardSupport, Base64Encoding
public abstract class VCardFormat extends EndMatcher implements PIMFormat {

  public VCardFormat() {
    super("VCARD");
  }

  protected abstract String getVersion();

  protected abstract String getCategoryProperty();

  protected abstract String getClassProperty();

  protected abstract int parseAttributes(String as[]);

  protected abstract String getBinaryEncodingName();

  @Override
  public String getName() {
    return "VCARD/" + getVersion();
  }

  @Override
  public boolean isTypeSupported(final int pimListType) {
    return pimListType == 1;
  }

  @Override
  public void encode(final OutputStream out, final String encoding, final PIMItem pimItem) throws IOException {
    final Writer w = new OutputStreamWriter(out, encoding);
    w.write("BEGIN:VCARD\r\n");
    w.write("VERSION:");
    w.write(getVersion());
    w.write("\r\n");
    final int fields[] = pimItem.getFields();
    FormatSupport.sort(fields);
    pimItem.getPIMList();
    for (final int field : fields) {
      final int valueCount = pimItem.countValues(field);
      for (int j = 0; j < valueCount; j++) {
        writeValue(w, pimItem, field, j);
      }
    }
    final String categories = FormatSupport.join(pimItem.getCategories(), ",");
    if (categories.length() > 0) {
      w.write(getCategoryProperty());
      w.write(":");
      w.write(categories);
      w.write("\r\n");
    }
    w.write("END:VCARD\r\n");
    w.flush();
  }

  protected void writeValue(final Writer w, final PIMItem item, final int field, final int index) throws IOException {
    final String label = VCardSupport.getFieldLabel(field);
    switch (field) {
      default:
        break;
      case 103: // 'g'
      case 104: // 'h'
      case 105: // 'i'
      case 107: // 'k'
      case 108: // 'l'
      case 109: // 'm'
      case 111: // 'o'
      case 113: // 'q'
      case 115: // 's'
      case 116: // 't'
      case 117: // 'u'
      case 118: // 'v'
      {
        final String sValue = item.getString(field, index);
        if (sValue != null) {
          w.write(label);
          writeAttributes(w, item.getAttributes(field, index));
          w.write(":");
          w.write(sValue);
          w.write("\r\n");
        }

        break;
      }
      case 100: // 'd'
      case 106: // 'j'
      {
        final String aValue[] = item.getStringArray(field, index);
        if (aValue != null) {
          w.write(label);
          writeAttributes(w, item.getAttributes(field, index));
          w.write(":");
          writeStringArray(w, aValue);
          w.write("\r\n");
        }

        break;
      }
      case 110: // 'n'
      case 112: // 'p'
      {
        final byte bValue[] = item.getBinary(field, index);
        if (bValue != null) {
          w.write(label);
          w.write(";ENCODING=");
          w.write(getBinaryEncodingName());
          writeAttributes(w, item.getAttributes(field, index));
          w.write(":\r\n    ");
          w.write(Base64Encoding.toBase64(bValue, 76, 4));
          w.write("\r\n");
        }

        break;
      }
      case 101: // 'e'
      case 114: // 'r'
      {
        w.write(label);
        writeAttributes(w, item.getAttributes(field, index));
        w.write(":");
        writeDate(w, item.getDate(field, index));
        w.write("\r\n");

        break;
      }
      case 102: // 'f'
      {
        final int iValue = item.getInt(field, index);
        final String sValue = VCardSupport.getClassType(iValue);
        if (sValue != null) {
          w.write(getClassProperty());
          writeAttributes(w, item.getAttributes(field, index));
          w.write(":");
          w.write(sValue);
          w.write("\r\n");
        }

        break;
      }
    }
  }

  protected void writeStringArray(final Writer w, final String data[]) throws IOException {
    for (int i = 0; i < data.length; i++) {
      if (data[i] != null) {
        w.write(data[i]);
      }
      if (i != (data.length - 1)) {
        w.write(59);
      }
    }
  }

  protected void writeDate(final Writer w, final long date) throws IOException {
    w.write(PIMHandler.getInstance().composeDate(date));
  }

  protected abstract void writeAttributes(Writer writer, int i) throws IOException;

  @Override
  public PIMItem[] decode(final InputStream in, final String encoding, final PIMList list) throws IOException {
    final LineReader r = new LineReader(in, encoding, this);
    final ContactImpl contact = decode(r);
    if (contact == null) {
      return null;
    }
    else {
      return (new ContactImpl[] {
          contact
      });
    }
  }

  private ContactImpl decode(final LineReader in) throws IOException {
    String line = in.readLine();
    if (line == null) { return null; }
    if (!line.toUpperCase().equals("BEGIN:VCARD")) { throw new UnsupportedPIMFormatException("Not a vCard :'" + line + "'"); }
    final String categoryProperty = getCategoryProperty();
    final ContactImpl contact = new ContactImpl(null);
    do {
      if ((line = in.readLine()) == null) {
        break;
      }
      final FormatSupport.DataElement element = FormatSupport.parseObjectLine(line);
      if (element.propertyName.equals("END")) { return contact; }
      if (element.propertyName.equals("VERSION")) {
        if (!element.data.equals(getVersion())) { throw new UnsupportedPIMFormatException("Version " + element.data + " not supported"); }
      }
      else if (element.propertyName.equals(categoryProperty)) {
        final String categories[] = FormatSupport.split(element.data, ',', 0);
        int j = 0;
        while (j < categories.length) {
          try {
            contact.addToCategory(categories[j]);
          }
          catch (final PIMException e) {
          }
          j++;
        }
      }
      else {
        importData(contact, element.propertyName, element.attributes, element.data);
      }
    }
    while (true);

    throw new IOException("Unterminated vCard");
  }

  private void importData(final Contact contact, final String prefix, final String attributes[], final String data) {
    final int attr = parseAttributes(attributes);
    int field = VCardSupport.getFieldCode(prefix);
    if ((field == -1) && prefix.equals(getClassProperty())) {
      field = 102;
    }
    if (!PIMHandler.getInstance().isSupportedField(1, field)) { return; }
    switch (field) {
      case 104: // 'h'
      case 111: // 'o'
      case 113: // 'q'
      default:
        break;
      case 103: // 'g'
      case 105: // 'i'
      case 107: // 'k'
      case 108: // 'l'
      case 109: // 'm'
      case 115: // 's'
      case 116: // 't'
      case 117: // 'u'
      case 118: // 'v'
        final String sdata = FormatSupport.parseString(attributes, data);
        contact.addString(field, attr, sdata);

        break;
      case 100: // 'd'
      case 106: // 'j'
        String elements[] = FormatSupport.parseStringArray(attributes, data);
        final int elementCount = PIMHandler.getInstance().getStringArraySize(1, field);
        if (elements.length != elementCount) {
          final String a[] = new String[elementCount];
          System.arraycopy(elements, 0, a, 0, Math.min(elements.length, elementCount));
          elements = a;
        }
        contact.addStringArray(field, attr, elements);

        break;
      case 101: // 'e'
      case 114: // 'r'
        final long date = PIMHandler.getInstance().parseDate(data);
        contact.addDate(field, attr, date);

        break;
      case 110: // 'n'
        final String valueType = FormatSupport.getAttributeValue(attributes, "VALUE=", null);
        if (valueType == null) {
          final byte bdata[] = FormatSupport.parseBinary(attributes, data);
          if (bdata.length != 0) {
            contact.addBinary(110, attr, bdata, 0, bdata.length);
          }

          break;
        }
        if (valueType.equals("URL")) {
          final String sdataurl = FormatSupport.parseString(attributes, data);
          contact.addString(111, attr, sdataurl);
        }

        break;
      case 112: // 'p'
        final String encoding = FormatSupport.getEncoding(attributes);
        if (encoding.equals("PLAIN_TEXT")) {
          final String sdatatext = FormatSupport.parseString(attributes, data);
          contact.addString(113, attr, sdatatext);

          break;
        }
        final byte bdata[] = FormatSupport.parseBinary(attributes, data);
        if (bdata.length != 0) {
          contact.addBinary(112, attr, bdata, 0, bdata.length);
        }

        break;
      case 102: // 'f'
        final int i = VCardSupport.getClassCode(data);
        if (i != -1) {
          contact.addInt(102, 0, i);
        }

        break;
    }
  }
}
