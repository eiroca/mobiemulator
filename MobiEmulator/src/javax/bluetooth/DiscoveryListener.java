package javax.bluetooth;

public abstract interface DiscoveryListener {
    public static final int INQUIRY_COMPLETED                   = 0;
    public static final int INQUIRY_ERROR                       = 7;
    public static final int INQUIRY_TERMINATED                  = 5;
    public static final int SERVICE_SEARCH_COMPLETED            = 1;
    public static final int SERVICE_SEARCH_DEVICE_NOT_REACHABLE = 6;
    public static final int SERVICE_SEARCH_ERROR                = 3;
    public static final int SERVICE_SEARCH_NO_RECORDS           = 4;
    public static final int SERVICE_SEARCH_TERMINATED           = 2;

    public abstract void deviceDiscovered(RemoteDevice paramRemoteDevice, DeviceClass paramDeviceClass);

    public abstract void servicesDiscovered(int paramInt, ServiceRecord[] paramArrayOfServiceRecord);

    public abstract void serviceSearchCompleted(int paramInt1, int paramInt2);

    public abstract void inquiryCompleted(int paramInt);
}
