package javax.wireless.messaging;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MessagePart {
    static final int  BUFFER_SIZE                = 2048;
    static int        MAX_PART_SIZE_BYTES        = 30720;
    static final char US_ASCII_LOWEST_VALID_CHAR = ' ';
    static final char US_ASCII_VALID_BIT_MASK    = '';
    byte[]            content;
    String            contentID;
    String            contentLocation;
    String            encoding;
    String            mimeType;

    public MessagePart(byte[] contents, String mimeType, String contentId, String contentLocation, String enc)
            throws SizeExceededException {
        construct(contents, 0, (contents == null) ? 0 : contents.length, mimeType, contentId, contentLocation, enc);
    }

    public MessagePart(InputStream is, String mimeType, String contentId, String contentLocation, String enc)
            throws IOException, SizeExceededException {
        byte[] bytes = new byte[0];
        if (is != null) {
            ByteArrayOutputStream accumulator = new ByteArrayOutputStream();
            byte[]                buffer      = new byte[2048];
            int                   readBytes   = 0;
            while ((readBytes = is.read(buffer)) != -1) {
                accumulator.write(buffer, 0, readBytes);
            }

            bytes = accumulator.toByteArray();
        }

        construct(bytes, 0, bytes.length, mimeType, contentId, contentLocation, enc);
    }

    public MessagePart(byte[] contents, int offset, int length, String mimeType, String contentId,
                       String contentLocation, String enc)
            throws SizeExceededException {
        construct(contents, offset, length, mimeType, contentId, contentLocation, enc);
    }

    void construct(byte[] contents, int offset, int length, String mimeType, String contentId, String contentLocation,
                   String enc)
            throws SizeExceededException {
        if (length > MAX_PART_SIZE_BYTES) {
            throw new SizeExceededException("InputStream data exceeds " + MAX_PART_SIZE_BYTES
                                            + " byte MessagePart size limit");
        }

        if (mimeType == null) {
            throw new IllegalArgumentException("mimeType must be specified");
        }

        checkContentID(contentId);
        checkContentLocation(contentLocation);

        if (length < 0) {
            throw new IllegalArgumentException("length must be >= 0");
        }

        if ((contents != null) && (offset + length > contents.length)) {
            throw new IllegalArgumentException("offset + length exceeds contents length");
        }

        if (offset < 0) {
            throw new IllegalArgumentException("offset must be >= 0");
        }

        checkEncodingScheme(enc);

        if (contents != null) {
            this.content = new byte[length];
            System.arraycopy(contents, offset, this.content, 0, length);
        }

        this.mimeType        = mimeType;
        this.contentID       = contentId;
        this.contentLocation = contentLocation;
        this.encoding        = enc;
    }

    public byte[] getContent() {
        if (this.content == null) {
            return null;
        }

        byte[] copyOfContent = new byte[this.content.length];
        System.arraycopy(this.content, 0, copyOfContent, 0, this.content.length);

        return copyOfContent;
    }

    public InputStream getContentAsStream() {
        if (this.content == null) {
            return new ByteArrayInputStream(new byte[0]);
        }

        return new ByteArrayInputStream(this.content);
    }

    public String getContentID() {
        return this.contentID;
    }

    public String getContentLocation() {
        return this.contentLocation;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public int getLength() {
        return (this.content == null) ? 0 : this.content.length;
    }

    public String getMIMEType() {
        return this.mimeType;
    }

    static void checkContentID(String contentId) throws IllegalArgumentException {
        if (contentId == null) {
            throw new IllegalArgumentException("contentId must be specified");
        }

        if (contentId.length() > 100) {
            throw new IllegalArgumentException("contentId exceeds 100 char limit");
        }

        if (containsNonUSASCII(contentId)) {
            throw new IllegalArgumentException("contentId must not contain non-US-ASCII characters");
        }
    }

    static void checkContentLocation(String contentLoc) throws IllegalArgumentException {
        if (contentLoc != null) {
            if (containsNonUSASCII(contentLoc)) {
                throw new IllegalArgumentException("contentLocation must not contain non-US-ASCII characters");
            }

            if (contentLoc.length() > 100) {
                throw new IllegalArgumentException("contentLocation exceeds 100 char limit");
            }
        }
    }

    static void checkEncodingScheme(String encoding) throws IllegalArgumentException {}

    static boolean containsNonUSASCII(String str) {
        int numChars = str.length();
        for (int i = 0;i < numChars;i++) {
            char thisChar = str.charAt(i);
            if ((thisChar < ' ') || (thisChar != (thisChar & 0x7F))) {
                return true;
            }
        }

        return false;
    }
}
