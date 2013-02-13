package javax.obex;

public abstract interface Authenticator {
    public abstract PasswordAuthentication onAuthenticationChallenge(String paramString, boolean paramBoolean1,
            boolean paramBoolean2);

    public abstract byte[] onAuthenticationResponse(byte[] paramArrayOfByte);
}
