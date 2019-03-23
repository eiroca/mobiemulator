package javax.microedition.media.protocol;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ContentDescriptor {

  String contentType;

  public ContentDescriptor(final java.lang.String contentType) {
    this.contentType = contentType;
  }

  public java.lang.String getContentType() {
    return contentType;
  }

}
