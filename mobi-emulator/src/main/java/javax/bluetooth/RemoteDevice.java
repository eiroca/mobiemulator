package javax.bluetooth;

import java.io.IOException;

// ~--- JDK imports ------------------------------------------------------------

import javax.microedition.io.Connection;

public class RemoteDevice {

  private long l_address;
  private final String s_address;

  protected RemoteDevice(String address) {
    if (address == null) { throw new NullPointerException("null address"); }

    final String errorMsg = "Malformed address: " + address;
    if (address.length() != 12) { throw new IllegalArgumentException(errorMsg); }

    if (address.startsWith("-")) { throw new IllegalArgumentException(errorMsg); }

    try {
      l_address = Long.parseLong(address, 16);
    }
    catch (final NumberFormatException e) {
      throw new IllegalArgumentException(errorMsg);
    }

    address = address.toUpperCase();

    try {
      final String lAddr = LocalDevice.getLocalDevice().getBluetoothAddress();
      if (address.equals(lAddr)) { throw new IllegalArgumentException("can't use the local address."); }
    }
    catch (final BluetoothStateException e) {
      throw new RuntimeException("Can't initialize bluetooth support");
    }

    s_address = address;
  }

  public boolean isTrustedDevice() {
    throw new RuntimeException("Not Implemented! Used to compile Code");
  }

  public String getFriendlyName(final boolean alwaysAsk) throws IOException {
    return null;
  }

  public final String getBluetoothAddress() {
    return s_address;
  }

  @Override
  public boolean equals(final Object obj) {
    return ((obj instanceof RemoteDevice)) && (l_address == ((RemoteDevice)obj).l_address);
  }

  @Override
  public int hashCode() {
    return (int)((l_address >>> 24) ^ (l_address & 0xFFFFFF));
  }

  public static RemoteDevice getRemoteDevice(final Connection conn) throws IOException {
    return null; // RemoteDeviceImpl.getRemoteDevice(conn);
  }

  public boolean authenticate() throws IOException {
    throw new RuntimeException("Not Implemented! Used to compile Code");
  }

  public boolean authorize(final Connection conn) throws IOException {
    throw new RuntimeException("Not Implemented! Used to compile Code");
  }

  public boolean encrypt(final Connection conn, final boolean on) throws IOException {
    throw new RuntimeException("Not Implemented! Used to compile Code");
  }

  public boolean isAuthenticated() {
    throw new RuntimeException("Not Implemented! Used to compile Code");
  }

  public boolean isAuthorized(final Connection conn) throws IOException {
    throw new RuntimeException("Not Implemented! Used to compile Code");
  }

  public boolean isEncrypted() {
    throw new RuntimeException("Not Implemented! Used to compile Code");
  }
}
