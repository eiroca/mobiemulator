package javax.bluetooth;

public class UUID {

  private static final long BASE_UUID_HIGHT = 4096L;
  private static final long BASE_UUID_LOW = -9223371485494954757L;
  private static final char[] digits = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  };
  long highBits;
  long lowBits;

  public UUID(final long uuidValue) {
    if ((uuidValue < 0L) || (uuidValue > 4294967295L)) { throw new IllegalArgumentException("The 'uuidValue' is out of [0, 2^32 - 1] range: " + uuidValue); }

    highBits = ((uuidValue << 32) | 0x1000);
    lowBits = -9223371485494954757L;
  }

  public UUID(final String uuidValue, final boolean shortUUID) {
    if (uuidValue == null) { throw new NullPointerException("Specified 'uuidValue' is null"); }

    if ((uuidValue.length() == 0) || ((shortUUID) && (uuidValue.length() > 8)) || (uuidValue.length() > 32)) { throw new IllegalArgumentException("Invalid length of specified 'uuidValue': " + uuidValue.length()); }

    if (uuidValue.indexOf('-') != -1) { throw new NumberFormatException("The '-' character is not allowed: " + uuidValue); }

    if (shortUUID) {
      final long val = Long.parseLong(uuidValue, 16);
      highBits = ((val << 32) | 0x1000);
      lowBits = -9223371485494954757L;

      return;
    }

    highBits = 0L;

    if (uuidValue.length() < 16) {
      lowBits = Long.parseLong(uuidValue, 16);

      return;
    }

    final int l = uuidValue.length();
    lowBits = Long.parseLong(uuidValue.substring(l - 8), 16);
    lowBits |= Long.parseLong(uuidValue.substring(l - 16, l - 8), 16) << 32;

    if (l == 16) { return; }

    if (l <= 24) {
      highBits = Long.parseLong(uuidValue.substring(0, l - 16), 16);
    }
    else {
      highBits = Long.parseLong(uuidValue.substring(l - 24, l - 16), 16);
      highBits |= Long.parseLong(uuidValue.substring(0, l - 24), 16) << 32;
    }
  }

  @Override
  public String toString() {
    final int[] ints = {
        (int)(lowBits & 0xFFFFFFFF), (int)((lowBits >>> 32) & 0xFFFFFFFF),
        (int)(highBits & 0xFFFFFFFF), (int)((highBits >>> 32) & 0xFFFFFFFF)
    };
    int charPos = 32;
    final char[] buf = new char[charPos];
    final int shift = 4;
    final int mask = 15;
    int needZerosIndex = -1;
    for (int i = 3; i >= 0; i--) {
      if (ints[i] != 0) {
        needZerosIndex = i - 1;

        break;
      }
    }

    for (int i = 0; i < ints.length; i++) {
      if ((ints[i] == 0) && (needZerosIndex < i) && (i != 0)) {
        continue;
      }

      if (needZerosIndex >= i) {
        for (int j = 0; j < 8; j++) {
          charPos--;
          buf[charPos] = UUID.digits[(ints[i] & mask)];
          ints[i] >>>= shift;
        }
      }
      else {
        do {
          charPos--;
          buf[charPos] = UUID.digits[(ints[i] & mask)];
          ints[i] >>>= shift;
        }
        while (ints[i] != 0);
      }
    }

    return new String(buf, charPos, 32 - charPos);
  }

  @Override
  public boolean equals(final Object value) {
    return ((value instanceof UUID)) && (lowBits == ((UUID)value).lowBits)
        && (highBits == ((UUID)value).highBits);
  }

  @Override
  public int hashCode() {
    return (int)(highBits ^ (highBits >> 32) ^ lowBits ^ (lowBits >> 32));
  }
}
