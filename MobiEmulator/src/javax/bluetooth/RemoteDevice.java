package javax.bluetooth;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.io.Connection;
import java.io.IOException;

public class RemoteDevice {
    private long   l_address;
    private String s_address;

    protected RemoteDevice(String address) {
        if (address == null) {
            throw new NullPointerException("null address");
        }

        String errorMsg = "Malformed address: " + address;
        if (address.length() != 12) {
            throw new IllegalArgumentException(errorMsg);
        }

        if (address.startsWith("-")) {
            throw new IllegalArgumentException(errorMsg);
        }

        try {
            this.l_address = Long.parseLong(address, 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMsg);
        }

        address = address.toUpperCase();

        try {
            String lAddr = LocalDevice.getLocalDevice().getBluetoothAddress();
            if (address.equals(lAddr)) {
                throw new IllegalArgumentException("can't use the local address.");
            }
        } catch (BluetoothStateException e) {
            throw new RuntimeException("Can't initialize bluetooth support");
        }

        this.s_address = address;
    }

    public boolean isTrustedDevice() {
        throw new RuntimeException("Not Implemented! Used to compile Code");
    }

    public String getFriendlyName(boolean alwaysAsk) throws IOException {
        return null;
    }

    public final String getBluetoothAddress() {
        return this.s_address;
    }

    public boolean equals(Object obj) {
        return ((obj instanceof RemoteDevice)) && (this.l_address == ((RemoteDevice) obj).l_address);
    }

    public int hashCode() {
        return (int) (this.l_address >>> 24 ^ this.l_address & 0xFFFFFF);
    }

    public static RemoteDevice getRemoteDevice(Connection conn) throws IOException {
        return null;    // RemoteDeviceImpl.getRemoteDevice(conn);
    }

    public boolean authenticate() throws IOException {
        throw new RuntimeException("Not Implemented! Used to compile Code");
    }

    public boolean authorize(Connection conn) throws IOException {
        throw new RuntimeException("Not Implemented! Used to compile Code");
    }

    public boolean encrypt(Connection conn, boolean on) throws IOException {
        throw new RuntimeException("Not Implemented! Used to compile Code");
    }

    public boolean isAuthenticated() {
        throw new RuntimeException("Not Implemented! Used to compile Code");
    }

    public boolean isAuthorized(Connection conn) throws IOException {
        throw new RuntimeException("Not Implemented! Used to compile Code");
    }

    public boolean isEncrypted() {
        throw new RuntimeException("Not Implemented! Used to compile Code");
    }
}
