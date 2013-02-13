package javax.bluetooth;

public class DiscoveryAgent {
    public static final int CACHED           = 0;
    public static final int GIAC             = 10390323;
    public static final int LIAC             = 10390272;
    public static final int NOT_DISCOVERABLE = 0;
    public static final int PREKNOWN         = 1;
    // private DiscoveryAgentImpl discoveryAgentImpl;

    DiscoveryAgent() {
        // this.discoveryAgentImpl = new DiscoveryAgentImpl(this);
    }

    public RemoteDevice[] retrieveDevices(int option) {
        return null;    // this.discoveryAgentImpl.retrieveDevices(option);
    }

    public boolean startInquiry(int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        return false;    // this.discoveryAgentImpl.startInquiry(accessCode, listener);
    }

    public boolean cancelInquiry(DiscoveryListener listener) {
        return false;    // this.discoveryAgentImpl.cancelInquiry(listener);
    }

    public int searchServices(int[] attrSet, UUID[] uuidSet, RemoteDevice btDev, DiscoveryListener discListener)
            throws BluetoothStateException {
        return 0;    // this.discoveryAgentImpl.searchServices(attrSet, uuidSet, btDev, discListener);
    }

    public boolean cancelServiceSearch(int transID) {
        return false;    // this.discoveryAgentImpl.cancelServiceSearch(transID);
    }

    public String selectService(UUID uuid, int security, boolean master) throws BluetoothStateException {
        return "";    // this.discoveryAgentImpl.selectService(uuid, security, master);
    }
}
