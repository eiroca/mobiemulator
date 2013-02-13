package javax.bluetooth;

public class UUID {
    private static final long   BASE_UUID_HIGHT = 4096L;
    private static final long   BASE_UUID_LOW   = -9223371485494954757L;
    private static final char[] digits          = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    long                        highBits;
    long                        lowBits;

    public UUID(long uuidValue) {
        if ((uuidValue < 0L) || (uuidValue > 4294967295L)) {
            throw new IllegalArgumentException("The 'uuidValue' is out of [0, 2^32 - 1] range: " + uuidValue);
        }

        this.highBits = (uuidValue << 32 | 0x1000);
        this.lowBits  = -9223371485494954757L;
    }

    public UUID(String uuidValue, boolean shortUUID) {
        if (uuidValue == null) {
            throw new NullPointerException("Specified 'uuidValue' is null");
        }

        if ((uuidValue.length() == 0) || ((shortUUID) && (uuidValue.length() > 8)) || (uuidValue.length() > 32)) {
            throw new IllegalArgumentException("Invalid length of specified 'uuidValue': " + uuidValue.length());
        }

        if (uuidValue.indexOf('-') != -1) {
            throw new NumberFormatException("The '-' character is not allowed: " + uuidValue);
        }

        if (shortUUID) {
            long val = Long.parseLong(uuidValue, 16);
            this.highBits = (val << 32 | 0x1000);
            this.lowBits  = -9223371485494954757L;

            return;
        }

        this.highBits = 0L;

        if (uuidValue.length() < 16) {
            this.lowBits = Long.parseLong(uuidValue, 16);

            return;
        }

        int l = uuidValue.length();
        this.lowBits = Long.parseLong(uuidValue.substring(l - 8), 16);
        this.lowBits |= Long.parseLong(uuidValue.substring(l - 16, l - 8), 16) << 32;

        if (l == 16) {
            return;
        }

        if (l <= 24) {
            this.highBits = Long.parseLong(uuidValue.substring(0, l - 16), 16);
        } else {
            this.highBits = Long.parseLong(uuidValue.substring(l - 24, l - 16), 16);
            this.highBits |= Long.parseLong(uuidValue.substring(0, l - 24), 16) << 32;
        }
    }

    public String toString() {
        int[] ints = { (int) (this.lowBits & 0xFFFFFFFF), (int) (this.lowBits >>> 32 & 0xFFFFFFFF),
                       (int) (this.highBits & 0xFFFFFFFF), (int) (this.highBits >>> 32 & 0xFFFFFFFF) };
        int    charPos        = 32;
        char[] buf            = new char[charPos];
        int    shift          = 4;
        int    radix          = 16;
        int    mask           = 15;
        int    needZerosIndex = -1;
        for (int i = 3;i >= 0;i--) {
            if (ints[i] != 0) {
                needZerosIndex = i - 1;

                break;
            }
        }

        for (int i = 0;i < ints.length;i++) {
            if ((ints[i] == 0) && (needZerosIndex < i) && (i != 0)) {
                continue;
            }

            if (needZerosIndex >= i) {
                for (int j = 0;j < 8;j++) {
                    charPos--;
                    buf[charPos] = digits[(ints[i] & mask)];
                    ints[i]      >>>= shift;
                }
            } else {
                do {
                    charPos--;
                    buf[charPos] = digits[(ints[i] & mask)];
                    ints[i]      >>>= shift;
                } while (ints[i] != 0);
            }
        }

        return new String(buf, charPos, 32 - charPos);
    }

    public boolean equals(Object value) {
        return ((value instanceof UUID)) && (this.lowBits == ((UUID) value).lowBits)
               && (this.highBits == ((UUID) value).highBits);
    }

    public int hashCode() {
        return (int) (this.highBits ^ this.highBits >> 32 ^ this.lowBits ^ this.lowBits >> 32);
    }
}
