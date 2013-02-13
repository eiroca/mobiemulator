package javax.microedition.pim.formats;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;

public class Base64Encoding {
    private static byte BASE64_BYTES[] = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
        60, 61, -1, -1, -1, 0, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
        22, 23, 24, 25, 26, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
        44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1
    };
    private static char BASE64_CHARS[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
        'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };
    public Base64Encoding() {}

    public static byte[] fromBase64(String sdata) {
        if ((sdata == null) || (sdata.length() < 2)) {
            return new byte[0];
        }
        ByteArrayOutputStream out    = new ByteArrayOutputStream();
        int                   length = sdata.length();
        char                  data[] = new char[length + 2];
        sdata.getChars(0, length, data, 0);
        data[length]     = '=';
        data[length + 1] = '=';
        int i      = nextCharIndex(data, 0);
        int offset = 0;
        do {
            if (i >= data.length) {
                break;
            }
            int char0 = data[i = nextCharIndex(data, i)];
            if (char0 == 61) {
                break;
            }
            int char1 = data[i = nextCharIndex(data, i + 1)];
            if (char1 == 61) {
                break;
            }
            int char2 = data[i = nextCharIndex(data, i + 1)];
            int char3 = data[i = nextCharIndex(data, i + 1)];
            i = nextCharIndex(data, i + 1);
            out.write(BASE64_BYTES[char0] << 2 | BASE64_BYTES[char1] >> 4);
            if (char2 != 61) {
                int value = BASE64_BYTES[char1] << 4 | BASE64_BYTES[char2] >> 2;
                out.write(value & 0xff);
                if (char3 != 61) {
                    value = BASE64_BYTES[char2] << 6 | BASE64_BYTES[char3];
                    out.write(value & 0xff);
                }
            }
        } while (true);

        return out.toByteArray();
    }
    private static int nextCharIndex(char data[], int i) {
        for (;(i < data.length) && ((data[i] > '\177') || (BASE64_BYTES[data[i]] == -1));i++) {}

        return i;
    }
    public static String toBase64(byte data[], int lineLength, int indent) {
        StringBuffer sb          = new StringBuffer();
        int          i           = 0;
        int          charsInLine = 0;
        do {
            if (i >= data.length) {
                break;
            }
            int byte0 = data[i++] & 0xff;
            int byte1 = (i >= data.length) ? 256
                                           : data[i++] & 0xff;
            int byte2 = (i >= data.length) ? 256
                                           : data[i++] & 0xff;
            sb.append(BASE64_CHARS[byte0 >> 2]);
            if (byte1 == 256) {
                sb.append(BASE64_CHARS[byte0 << 4 & 0x30]);
                sb.append("==");
            } else {
                sb.append(BASE64_CHARS[(byte0 << 4 | byte1 >> 4) & 0x3f]);
                if (byte2 == 256) {
                    sb.append(BASE64_CHARS[byte1 << 2 & 0x3f]);
                    sb.append('=');
                } else {
                    sb.append(BASE64_CHARS[(byte1 << 2 | byte2 >> 6) & 0x3f]);
                    sb.append(BASE64_CHARS[byte2 & 0x3f]);
                }
            }
            if ((charsInLine += 4) + 4 > lineLength && (i < data.length)) {
                sb.append("\r\n");
                int j = 0;
                while (j < indent) {
                    sb.append(' ');
                    j++;
                }
            }
        } while (true);

        return sb.toString();
    }
}
