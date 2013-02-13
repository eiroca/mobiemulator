package javax.bluetooth;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

public abstract interface ServiceRecord {
    public static final int AUTHENTICATE_ENCRYPT     = 2;
    public static final int AUTHENTICATE_NOENCRYPT   = 1;
    public static final int NOAUTHENTICATE_NOENCRYPT = 0;

    public abstract DataElement getAttributeValue(int paramInt);

    public abstract RemoteDevice getHostDevice();

    public abstract int[] getAttributeIDs();

    public abstract boolean populateRecord(int[] paramArrayOfInt) throws IOException;

    public abstract String getConnectionURL(int paramInt, boolean paramBoolean);

    public abstract void setDeviceServiceClasses(int paramInt);

    public abstract boolean setAttributeValue(int paramInt, DataElement paramDataElement);
}
