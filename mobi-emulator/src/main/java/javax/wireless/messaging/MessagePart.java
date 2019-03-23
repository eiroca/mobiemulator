package javax.wireless.messaging;

// ~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MessagePart {

  static final int BUFFER_SIZE = 2048;
  static int MAX_PART_SIZE_BYTES = 30720;
  static final char US_ASCII_LOWEST_VALID_CHAR = ' ';
  static final char US_ASCII_VALID_BIT_MASK = '';
  byte[] content;
  String contentID;
  String contentLocation;
  String encoding;
  String mimeType;

  public MessagePart(final byte[] contents, final String mimeType, final String contentId, final String contentLocation, final String enc)
      throws SizeExceededException {
    construct(contents, 0, (contents == null) ? 0 : contents.length, mimeType, contentId, contentLocation, enc);
  }

  public MessagePart(final InputStream is, final String mimeType, final String contentId, final String contentLocation, final String enc)
      throws IOException, SizeExceededException {
    byte[] bytes = new byte[0];
    if (is != null) {
      final ByteArrayOutputStream accumulator = new ByteArrayOutputStream();
      final byte[] buffer = new byte[2048];
      int readBytes = 0;
      while ((readBytes = is.read(buffer)) != -1) {
        accumulator.write(buffer, 0, readBytes);
      }

      bytes = accumulator.toByteArray();
    }

    construct(bytes, 0, bytes.length, mimeType, contentId, contentLocation, enc);
  }

  public MessagePart(final byte[] contents, final int offset, final int length, final String mimeType, final String contentId, final String contentLocation, final String enc)
      throws SizeExceededException {
    construct(contents, offset, length, mimeType, contentId, contentLocation, enc);
  }

  void construct(final byte[] contents, final int offset, final int length, final String mimeType, final String contentId, final String contentLocation, final String enc)
      throws SizeExceededException {
    if (length > MessagePart.MAX_PART_SIZE_BYTES) { throw new SizeExceededException("InputStream data exceeds " + MessagePart.MAX_PART_SIZE_BYTES
        + " byte MessagePart size limit"); }

    if (mimeType == null) { throw new IllegalArgumentException("mimeType must be specified"); }

    MessagePart.checkContentID(contentId);
    MessagePart.checkContentLocation(contentLocation);

    if (length < 0) { throw new IllegalArgumentException("length must be >= 0"); }

    if ((contents != null) && ((offset + length) > contents.length)) { throw new IllegalArgumentException("offset + length exceeds contents length"); }

    if (offset < 0) { throw new IllegalArgumentException("offset must be >= 0"); }

    MessagePart.checkEncodingScheme(enc);

    if (contents != null) {
      content = new byte[length];
      System.arraycopy(contents, offset, content, 0, length);
    }

    this.mimeType = mimeType;
    contentID = contentId;
    this.contentLocation = contentLocation;
    encoding = enc;
  }

  public byte[] getContent() {
    if (content == null) { return null; }

    final byte[] copyOfContent = new byte[content.length];
    System.arraycopy(content, 0, copyOfContent, 0, content.length);

    return copyOfContent;
  }

  public InputStream getContentAsStream() {
    if (content == null) { return new ByteArrayInputStream(new byte[0]); }

    return new ByteArrayInputStream(content);
  }

  public String getContentID() {
    return contentID;
  }

  public String getContentLocation() {
    return contentLocation;
  }

  public String getEncoding() {
    return encoding;
  }

  public int getLength() {
    return (content == null) ? 0 : content.length;
  }

  public String getMIMEType() {
    return mimeType;
  }

  static void checkContentID(final String contentId) throws IllegalArgumentException {
    if (contentId == null) { throw new IllegalArgumentException("contentId must be specified"); }

    if (contentId.length() > 100) { throw new IllegalArgumentException("contentId exceeds 100 char limit"); }

    if (MessagePart.containsNonUSASCII(contentId)) { throw new IllegalArgumentException("contentId must not contain non-US-ASCII characters"); }
  }

  static void checkContentLocation(final String contentLoc) throws IllegalArgumentException {
    if (contentLoc != null) {
      if (MessagePart.containsNonUSASCII(contentLoc)) { throw new IllegalArgumentException("contentLocation must not contain non-US-ASCII characters"); }

      if (contentLoc.length() > 100) { throw new IllegalArgumentException("contentLocation exceeds 100 char limit"); }
    }
  }

  static void checkEncodingScheme(final String encoding) throws IllegalArgumentException {
  }

  static boolean containsNonUSASCII(final String str) {
    final int numChars = str.length();
    for (int i = 0; i < numChars; i++) {
      final char thisChar = str.charAt(i);
      if ((thisChar < ' ') || (thisChar != (thisChar & 0x7F))) { return true; }
    }

    return false;
  }
}
