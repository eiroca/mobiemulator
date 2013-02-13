package javax.microedition.pim.formats;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.pim.UnsupportedPIMFormatException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

//Referenced classes of package javax.microedition.pim.formats:
//           QuotedPrintableEncoding, Base64Encoding
public class FormatSupport {
    public static final String BASE64           = "BASE64";
    public static final String PLAIN_TEXT       = "PLAIN_TEXT";
    public static final String QUOTED_PRINTABLE = "QUOTED_PRINTABLE";
    public static final String UTF8             = "UTF-8";
    public FormatSupport() {}

    public static String getCharSet(String attributes[]) {
        String charset = getAttributeValue(attributes, "CHARSET=", "UTF-8");
        try {
            "".getBytes(charset);

            return charset;
        } catch (UnsupportedEncodingException e) {}

        return "UTF-8";
    }
    public static String getAttributeValue(String attributes[], String key, String defaultValue) {
        for (int i = 0;i < attributes.length;i++) {
            if (attributes[i].startsWith(key)) {
                return attributes[i].substring(key.length());
            }
        }

        return defaultValue;
    }
    public static String getEncoding(String attributes[]) {
        for (int i = 0;i < attributes.length;i++) {
            String s = attributes[i].toUpperCase();
            if (attributes[i].equals("ENCODING=QUOTED-PRINTABLE") || attributes[i].equals("QUOTED-PRINTABLE")) {
                return "QUOTED_PRINTABLE";
            }
            if (attributes[i].equals("ENCODING=BASE64") || attributes[i].equals("BASE64")
                    || attributes[i].equals("ENCODING=B")) {
                return "BASE64";
            }
        }

        return "PLAIN_TEXT";
    }
    public static String convertString(String data, String encoding, String charset) {
        if (encoding.equals("QUOTED_PRINTABLE")) {
            byte[] b = QuotedPrintableEncoding.fromQuotedPrintable(data);
            try {
                return new String(b, charset);
            } catch (UnsupportedEncodingException e) {
                return new String(b);
            }
        }
        if (encoding.equals("BASE64")) {
            byte[] b = Base64Encoding.fromBase64(data);
            try {
                return new String(b, charset);
            } catch (UnsupportedEncodingException e) {
                return new String(b);
            }
        }
        if (charset.equals("UTF-8")) {
            return data;
        }
        try {
            return new String(data.getBytes("UTF-8"), charset);
        } catch (UnsupportedEncodingException e) {}

        throw new Error("UTF-8 encoding not available");
    }
    public static String[] split(String data, char separatorChar, int startingPoint) {
        if (startingPoint == data.length()) {
            return new String[0];
        }
        Vector elementList    = new Vector();
        int    startSearchAt  = startingPoint;
        int    startOfElement = startingPoint;
        int    i;
        while ((i = data.indexOf(separatorChar, startSearchAt)) != -1) {
            if ((i != 0) && (data.charAt(i - 1) == '\\')) {
                startSearchAt = i + 1;
            } else {
                String element = data.substring(startOfElement, i);
                elementList.addElement(element);
                startSearchAt = startOfElement = i + 1;
            }
        }
        if (elementList.size() == 0) {
            return (new String[]{ data.substring(startOfElement) });
        }
        elementList.addElement(data.substring(startOfElement));
        String elements[] = new String[elementList.size()];
        for (int j = 0;j < elements.length;j++) {
            elements[j] = (String) elementList.elementAt(j);
        }

        return elements;
    }
    public static String join(String elements[], String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i < elements.length;i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(elements[i]);
        }

        return sb.toString();
    }
    public static void sort(int a[]) {
        for (int j = 1;j < a.length;j++) {
            int v = a[j];
            int i;
            for (i = j - 1;(i >= 0) && (a[i] > v);i--) {
                a[i + 1] = a[i];
            }
            a[i + 1] = v;
        }
    }
    public static boolean contains(int a[], int value) {
        int lowerBound = 0;
        for (int upperBound = a.length - 1;upperBound - lowerBound >= 0;) {
            int i = lowerBound + (upperBound - lowerBound) / 2;
            int v = a[i];
            if (v > value) {
                upperBound = i - 1;
            } else if (v < value) {
                lowerBound = i + 1;
            } else {
                return true;
            }
        }

        return false;
    }
    public static DataElement parseObjectLine(String line) throws UnsupportedPIMFormatException {
        int i = line.indexOf(':');
        if ((i == -1) || (i == 0)) {
            throw new UnsupportedPIMFormatException("Invalid line: '" + line + "'");
        }
        DataElement element = new DataElement();
        element.data = line.substring(i + 1).trim();
        String prefix = line.substring(0, i).trim();
        i = prefix.indexOf(';');
        if (i == -1) {
            element.propertyName = prefix.toUpperCase();
            element.attributes   = new String[0];
        } else {
            element.propertyName = prefix.substring(0, i).toUpperCase();
            element.attributes   = split(prefix, ';', i + 1);
            for (int j = 0;j < element.attributes.length;j++) {
                element.attributes[j] = element.attributes[j].toUpperCase();
            }
        }
        i = element.propertyName.lastIndexOf('.');
        if (i != -1) {
            element.propertyName = element.propertyName.substring(i + 1);
        }

        return element;
    }
    public static String parseString(String attributes[], String data) {
        String charset  = getCharSet(attributes);
        String encoding = getEncoding(attributes);

        return convertString(data, encoding, charset);
    }
    public static String[] parseStringArray(String attributes[], String data) {
        String charset    = getCharSet(attributes);
        String encoding   = getEncoding(attributes);
        String elements[] = split(data, ';', 0);
        for (int i = 0;i < elements.length;i++) {
            elements[i] = convertString(elements[i], encoding, charset);
        }

        return elements;
    }
    public static byte[] parseBinary(String attributes[], String data) {
        String encoding = getEncoding(attributes);
        if (encoding.equals("QUOTED_PRINTABLE")) {
            return QuotedPrintableEncoding.fromQuotedPrintable(data);
        }
        if (encoding.equals("BASE64")) {
            return Base64Encoding.fromBase64(data);
        } else {
            return data.getBytes();
        }
    }
    public static class DataElement {
        String attributes[];
        String data;
        String propertyName;
        public DataElement() {}
    }
}
