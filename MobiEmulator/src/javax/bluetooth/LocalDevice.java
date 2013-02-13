package javax.bluetooth;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.io.Connection;

public class LocalDevice {
    private static LocalDevice localDevice;
    // private static LocalDeviceImpl localDeviceImpl;
    private DiscoveryAgent discoveryAgent;

    public static LocalDevice getLocalDevice() throws BluetoothStateException {
        synchronized (LocalDevice.class) {
            if (localDevice == null) {
                try {
                    localDevice = new LocalDevice();

                    if (localDevice.discoveryAgent == null) {
                        localDevice.discoveryAgent = new DiscoveryAgent();
                    }
                    // localDeviceImpl = new LocalDeviceImpl(localDevice);
                } catch (Exception bse) {
                    localDevice = null;
                } catch (Throwable e) {
                    localDevice = null;

                    throw new BluetoothStateException(e.toString());
                }
            }
        }

        return localDevice;
    }

    public DiscoveryAgent getDiscoveryAgent() {
        return this.discoveryAgent;
    }

    public String getFriendlyName() {
        return "";    // localDeviceImpl.getFriendlyName();
    }

    public DeviceClass getDeviceClass() {
        return null;    // localDeviceImpl.getDeviceClass();
    }

    public static String getProperty(String property) {
        return null;    // localDevice != null ? localDeviceImpl.getProperty(property) : null;
    }

    public static boolean isPowerOn() {
        String res = System.getProperty("bluetooth.system.state");

        return (res != null) && (res.equals("on"));
    }

    public int getDiscoverable() {
        return 0;    // localDeviceImpl.getDiscoverable();
    }

    public String getBluetoothAddress() {
        return "";    // localDeviceImpl.getBluetoothAddress();
    }

    public boolean setDiscoverable(int mode) throws BluetoothStateException {
        return false;    // localDeviceImpl.setDiscoverable(mode);
    }

    public ServiceRecord getRecord(Connection notifier) {
        return null;    // localDeviceImpl.getRecord(notifier);
    }

    public void updateRecord(ServiceRecord srvRecord) throws ServiceRegistrationException {
        // localDeviceImpl.updateRecord(srvRecord);
    }
}
