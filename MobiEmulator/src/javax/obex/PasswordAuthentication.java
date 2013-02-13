package javax.obex;

public class PasswordAuthentication {
    private byte[] password;
    private byte[] userName;

    public PasswordAuthentication(byte[] userName, byte[] password) {
        if (password == null) {
            throw new NullPointerException("null password");
        }

        this.password = password;
        this.userName = userName;
    }

    public byte[] getUserName() {
        return this.userName;
    }

    public byte[] getPassword() {
        return this.password;
    }
}
