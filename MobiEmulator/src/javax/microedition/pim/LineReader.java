package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

//Referenced classes of package javax.microedition.pim:
//           MarkableInputStream
public class LineReader extends InputStreamReader {
    private final InputStream in;
    private final Matcher     matcher;
    public LineReader(InputStream in, String encoding, Matcher matcher) throws UnsupportedEncodingException {
        this(new MarkableInputStream(in), encoding, matcher);
    }

    private LineReader(MarkableInputStream in, String encoding, Matcher matcher) throws UnsupportedEncodingException {
        super(in, encoding);
        this.in      = in;
        this.matcher = matcher;
    }

    public String readLine() throws IOException {
        StringBuffer sb                   = new StringBuffer();
        boolean      lineIsOnlyWhiteSpace = true;
        boolean      done                 = false;
        int          i                    = read();
        while ((i != -1) &&!done) {
            switch (i) {
            case 13 :        // '\r'
                i = read();
                if (i != 10) {
                    throw new IOException("Bad line terminator");
                }
            // fall through
            case 10 :        // '\n'
                if (lineIsOnlyWhiteSpace) {
                    sb.setLength(0);
                }
                mark(1);
                i = read();
                reset();
                switch (i) {
                case 9 :     // '\t'
                case 32 :    // ' '
                    skip(1L);

                    break;
                default :
                    if (!lineIsOnlyWhiteSpace) {
                        done = true;

                        continue;
                    }
                    skip(1L);
                }

                break;
            case 9 :         // '\t'
            case 32 :        // ' '
                sb.append((char) i);
                i = read();

                break;
            default :
                sb.append((char) i);
                if (matcher.match(sb)) {
                    return sb.toString().trim();
                }
                i                    = read();
                lineIsOnlyWhiteSpace = false;
            }
            if (lineIsOnlyWhiteSpace) {
                return null;
            }
        }

        return sb.toString().trim();
    }
    public void mark(int lookahead) throws IOException {
        in.mark(lookahead);
    }
    public boolean markSupported() {
        return true;
    }
    public void reset() throws IOException {
        in.reset();
    }
    public int read() throws IOException {
        return this.in.read();
    }
    public static interface Matcher {
        public abstract boolean match(StringBuffer stringbuffer);
    }
}
