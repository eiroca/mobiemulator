/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.media.protocol;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ContentDescriptor {
    String contentType;
    public ContentDescriptor(java.lang.String contentType) {
        this.contentType = contentType;
    }

    public java.lang.String getContentType() {
        return contentType;
    }
}
