package javax.microedition.pim.formats;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QuotedPrintableEncoding {
    public QuotedPrintableEncoding() {}

    public static byte[] fromQuotedPrintable(String sdata) {
        ByteArrayOutputStream out                   = new ByteArrayOutputStream();
        ByteArrayOutputStream whitespaceAccumulator = new ByteArrayOutputStream();
        char                  data[]                = sdata.toCharArray();
        boolean               followingEqualsSign   = false;
        for (int i = 0;i < data.length;i++) {
            if (followingEqualsSign) {
                switch (data[i]) {
                default :
                    if (i < data.length - 1) {
                        String charCode = sdata.substring(i, i + 2);
                        i++;
                        try {
                            out.write(Integer.parseInt(charCode, 16));
                        } catch (NumberFormatException nfe) {
                            out.write(61);
                            out.write(charCode.charAt(0));
                            out.write(charCode.charAt(1));
                        }
                    } else {
                        out.write(61);
                        out.write(data[i]);
                    }
                // fall through
                case 10 :    // '\n'
                    followingEqualsSign = false;

                    break;
                }

                continue;
            }
            try {
label0:
                switch (data[i]) {
                case 13 :    // '\r'
                    break;
                case 61 :    // '='
                    out.write(whitespaceAccumulator.toByteArray());
                    whitespaceAccumulator.reset();
                    followingEqualsSign = true;
                    int j = i + 1;
                    do {
                        if (j >= data.length) {
                            break label0;
                        }
                        char c = data[j];
                        if ((c != ' ') && (c != '\t') && (c != '\r')) {
                            if (c == '\n') {
                                i = j - 1;
                            }

                            break label0;
                        }
                        j++;
                    } while (true);
                case 9 :     // '\t'
                case 32 :    // ' '
                    whitespaceAccumulator.write(data[i]);

                    break;
                case 10 :    // '\n'
                    whitespaceAccumulator.reset();
                    out.write(data[i]);

                    break;
                default :
                    out.write(whitespaceAccumulator.toByteArray());
                    whitespaceAccumulator.reset();
                    out.write(data[i]);

                    break;
                }
            } catch (IOException e) {}
        }
        try {
            out.write(whitespaceAccumulator.toByteArray());
        } catch (IOException e) {}

        return out.toByteArray();
    }
}
