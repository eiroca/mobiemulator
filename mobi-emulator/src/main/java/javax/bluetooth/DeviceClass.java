package javax.bluetooth;

public class DeviceClass {

  private static final int MASK_MAJOR = 7936;
  private static final int MASK_MINOR = 252;
  private static final int MASK_OVERFLOW = -16777216;
  private static final int MASK_SERVICE = 16769024;
  private final int record;

  public DeviceClass(final int record) {
    if ((record & 0xFF000000) != 0) { throw new IllegalArgumentException("The 'record' bits out of (0-23) range."); }

    this.record = record;
  }

  public int getServiceClasses() {
    return record & 0xFFE000;
  }

  public int getMajorDeviceClass() {
    return record & 0x1F00;
  }

  public int getMinorDeviceClass() {
    return record & 0xFC;
  }
}
