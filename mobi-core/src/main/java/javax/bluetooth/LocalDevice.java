package javax.bluetooth;

// ~--- JDK imports ------------------------------------------------------------

import javax.microedition.io.Connection;

public class LocalDevice {

  private static LocalDevice localDevice;
  // private static LocalDeviceImpl localDeviceImpl;
  private DiscoveryAgent discoveryAgent;

  public static LocalDevice getLocalDevice() throws BluetoothStateException {
    synchronized (LocalDevice.class) {
      if (LocalDevice.localDevice == null) {
        try {
          LocalDevice.localDevice = new LocalDevice();

          if (LocalDevice.localDevice.discoveryAgent == null) {
            LocalDevice.localDevice.discoveryAgent = new DiscoveryAgent();
          }
          // localDeviceImpl = new LocalDeviceImpl(localDevice);
        }
        catch (final Exception bse) {
          LocalDevice.localDevice = null;
        }
        catch (final Throwable e) {
          LocalDevice.localDevice = null;

          throw new BluetoothStateException(e.toString());
        }
      }
    }

    return LocalDevice.localDevice;
  }

  public DiscoveryAgent getDiscoveryAgent() {
    return discoveryAgent;
  }

  public String getFriendlyName() {
    return ""; // localDeviceImpl.getFriendlyName();
  }

  public DeviceClass getDeviceClass() {
    return null; // localDeviceImpl.getDeviceClass();
  }

  public static String getProperty(final String property) {
    return null; // localDevice != null ? localDeviceImpl.getProperty(property) : null;
  }

  public static boolean isPowerOn() {
    final String res = System.getProperty("bluetooth.system.state");

    return (res != null) && (res.equals("on"));
  }

  public int getDiscoverable() {
    return 0; // localDeviceImpl.getDiscoverable();
  }

  public String getBluetoothAddress() {
    return ""; // localDeviceImpl.getBluetoothAddress();
  }

  public boolean setDiscoverable(final int mode) throws BluetoothStateException {
    return false; // localDeviceImpl.setDiscoverable(mode);
  }

  public ServiceRecord getRecord(final Connection notifier) {
    return null; // localDeviceImpl.getRecord(notifier);
  }

  public void updateRecord(final ServiceRecord srvRecord) throws ServiceRegistrationException {
    // localDeviceImpl.updateRecord(srvRecord);
  }
}
