package javax.microedition.pim.formats;

import java.io.UnsupportedEncodingException;
import java.util.Vector;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.pim.UnsupportedPIMFormatException;

// Referenced classes of package javax.microedition.pim.formats:
// QuotedPrintableEncoding, Base64Encoding
public class FormatSupport {

  public static final String BASE64 = "BASE64";
  public static final String PLAIN_TEXT = "PLAIN_TEXT";
  public static final String QUOTED_PRINTABLE = "QUOTED_PRINTABLE";
  public static final String UTF8 = "UTF-8";

  public FormatSupport() {
  }

  public static String getCharSet(final String attributes[]) {
    final String charset = FormatSupport.getAttributeValue(attributes, "CHARSET=", "UTF-8");
    try {
      "".getBytes(charset);

      return charset;
    }
    catch (final UnsupportedEncodingException e) {
    }

    return "UTF-8";
  }

  public static String getAttributeValue(final String attributes[], final String key, final String defaultValue) {
    for (final String attribute : attributes) {
      if (attribute.startsWith(key)) { return attribute.substring(key.length()); }
    }

    return defaultValue;
  }

  public static String getEncoding(final String attributes[]) {
    for (final String attribute : attributes) {
      attribute.toUpperCase();
      if (attribute.equals("ENCODING=QUOTED-PRINTABLE") || attribute.equals("QUOTED-PRINTABLE")) { return "QUOTED_PRINTABLE"; }
      if (attribute.equals("ENCODING=BASE64") || attribute.equals("BASE64")
          || attribute.equals("ENCODING=B")) { return "BASE64"; }
    }

    return "PLAIN_TEXT";
  }

  public static String convertString(final String data, final String encoding, final String charset) {
    if (encoding.equals("QUOTED_PRINTABLE")) {
      final byte[] b = QuotedPrintableEncoding.fromQuotedPrintable(data);
      try {
        return new String(b, charset);
      }
      catch (final UnsupportedEncodingException e) {
        return new String(b);
      }
    }
    if (encoding.equals("BASE64")) {
      final byte[] b = Base64Encoding.fromBase64(data);
      try {
        return new String(b, charset);
      }
      catch (final UnsupportedEncodingException e) {
        return new String(b);
      }
    }
    if (charset.equals("UTF-8")) { return data; }
    try {
      return new String(data.getBytes("UTF-8"), charset);
    }
    catch (final UnsupportedEncodingException e) {
    }

    throw new Error("UTF-8 encoding not available");
  }

  public static String[] split(final String data, final char separatorChar, final int startingPoint) {
    if (startingPoint == data.length()) { return new String[0]; }
    final Vector elementList = new Vector();
    int startSearchAt = startingPoint;
    int startOfElement = startingPoint;
    int i;
    while ((i = data.indexOf(separatorChar, startSearchAt)) != -1) {
      if ((i != 0) && (data.charAt(i - 1) == '\\')) {
        startSearchAt = i + 1;
      }
      else {
        final String element = data.substring(startOfElement, i);
        elementList.addElement(element);
        startSearchAt = startOfElement = i + 1;
      }
    }
    if (elementList.size() == 0) {
      return (new String[] {
          data.substring(startOfElement)
      });
    }
    elementList.addElement(data.substring(startOfElement));
    final String elements[] = new String[elementList.size()];
    for (int j = 0; j < elements.length; j++) {
      elements[j] = (String)elementList.elementAt(j);
    }

    return elements;
  }

  public static String join(final String elements[], final String separator) {
    final StringBuffer sb = new StringBuffer();
    for (int i = 0; i < elements.length; i++) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(elements[i]);
    }

    return sb.toString();
  }

  public static void sort(final int a[]) {
    for (int j = 1; j < a.length; j++) {
      final int v = a[j];
      int i;
      for (i = j - 1; (i >= 0) && (a[i] > v); i--) {
        a[i + 1] = a[i];
      }
      a[i + 1] = v;
    }
  }

  public static boolean contains(final int a[], final int value) {
    int lowerBound = 0;
    for (int upperBound = a.length - 1; (upperBound - lowerBound) >= 0;) {
      final int i = lowerBound + ((upperBound - lowerBound) / 2);
      final int v = a[i];
      if (v > value) {
        upperBound = i - 1;
      }
      else if (v < value) {
        lowerBound = i + 1;
      }
      else {
        return true;
      }
    }

    return false;
  }

  public static DataElement parseObjectLine(final String line) throws UnsupportedPIMFormatException {
    int i = line.indexOf(':');
    if ((i == -1) || (i == 0)) { throw new UnsupportedPIMFormatException("Invalid line: '" + line + "'"); }
    final DataElement element = new DataElement();
    element.data = line.substring(i + 1).trim();
    final String prefix = line.substring(0, i).trim();
    i = prefix.indexOf(';');
    if (i == -1) {
      element.propertyName = prefix.toUpperCase();
      element.attributes = new String[0];
    }
    else {
      element.propertyName = prefix.substring(0, i).toUpperCase();
      element.attributes = FormatSupport.split(prefix, ';', i + 1);
      for (int j = 0; j < element.attributes.length; j++) {
        element.attributes[j] = element.attributes[j].toUpperCase();
      }
    }
    i = element.propertyName.lastIndexOf('.');
    if (i != -1) {
      element.propertyName = element.propertyName.substring(i + 1);
    }

    return element;
  }

  public static String parseString(final String attributes[], final String data) {
    final String charset = FormatSupport.getCharSet(attributes);
    final String encoding = FormatSupport.getEncoding(attributes);

    return FormatSupport.convertString(data, encoding, charset);
  }

  public static String[] parseStringArray(final String attributes[], final String data) {
    final String charset = FormatSupport.getCharSet(attributes);
    final String encoding = FormatSupport.getEncoding(attributes);
    final String elements[] = FormatSupport.split(data, ';', 0);
    for (int i = 0; i < elements.length; i++) {
      elements[i] = FormatSupport.convertString(elements[i], encoding, charset);
    }

    return elements;
  }

  public static byte[] parseBinary(final String attributes[], final String data) {
    final String encoding = FormatSupport.getEncoding(attributes);
    if (encoding.equals("QUOTED_PRINTABLE")) { return QuotedPrintableEncoding.fromQuotedPrintable(data); }
    if (encoding.equals("BASE64")) {
      return Base64Encoding.fromBase64(data);
    }
    else {
      return data.getBytes();
    }
  }

  public static class DataElement {

    String attributes[];
    String data;
    String propertyName;

    public DataElement() {
    }
  }
}
