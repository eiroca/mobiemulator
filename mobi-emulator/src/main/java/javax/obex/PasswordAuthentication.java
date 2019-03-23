package javax.obex;

public class PasswordAuthentication {

  private final byte[] password;
  private final byte[] userName;

  public PasswordAuthentication(final byte[] userName, final byte[] password) {
    if (password == null) { throw new NullPointerException("null password"); }

    this.password = password;
    this.userName = userName;
  }

  public byte[] getUserName() {
    return userName;
  }

  public byte[] getPassword() {
    return password;
  }
}
