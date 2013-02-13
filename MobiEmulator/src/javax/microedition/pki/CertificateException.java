/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.pki;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class CertificateException extends IOException {
    public static final byte BAD_EXTENSIONS               = 1;
    public static final byte BROKEN_CHAIN                 = 11;
    public static final byte CERTIFICATE_CHAIN_TOO_LONG   = 2;
    public static final byte EXPIRED                      = 3;
    public static final byte INAPPROPRIATE_KEY_USAGE      = 10;
    public static final byte MISSING_SIGNATURE            = 5;
    public static final byte NOT_YET_VALID                = 6;
    public static final byte ROOT_CA_EXPIRED              = 12;
    public static final byte SITENAME_MISMATCH            = 7;
    public static final byte UNAUTHORIZED_INTERMEDIATE_CA = 4;
    public static final byte UNRECOGNIZED_ISSUER          = 8;
    public static final byte UNSUPPORTED_PUBLIC_KEY_TYPE  = 13;
    public static final byte UNSUPPORTED_SIGALG           = 9;
    public static final byte VERIFICATION_FAILED          = 14;
    private Certificate      certificate                  = null;
    private byte             reason                       = 0;
    public CertificateException(Certificate certificate, byte status) {
        this.certificate = certificate;
        this.reason      = status;
        System.out.println(" Exception " + this.certificate.getSubject() + " reason " + status);
        Throwable t = new Throwable();
        t.printStackTrace();
    }

    public CertificateException(String message, Certificate certificate, byte status) {
        this.certificate = certificate;
        this.reason      = status;
        System.out.println(" Exception " + message + " " + this.certificate.getSubject() + " reason " + status);
        Throwable t = new Throwable();
        t.printStackTrace();
    }

    public Certificate getCertificate() {
        return certificate;
    }
    public byte getReason() {
        return reason;
    }
}
