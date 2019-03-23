package javax.bluetooth;

// ~--- JDK imports ------------------------------------------------------------

import java.util.Vector;

public class DataElement {

  public static final int BOOL = 40;
  public static final int DATALT = 56;
  public static final int DATSEQ = 48;
  public static final int INT_1 = 16;
  public static final int INT_16 = 20;
  public static final int INT_2 = 17;
  public static final int INT_4 = 18;
  public static final int INT_8 = 19;
  public static final int NULL = 0;
  public static final int STRING = 32;
  public static final int URL = 64;
  public static final int UUID = 24;
  public static final int U_INT_1 = 8;
  public static final int U_INT_16 = 12;
  public static final int U_INT_2 = 9;
  public static final int U_INT_4 = 10;
  public static final int U_INT_8 = 11;
  private boolean booleanValue;
  private long longValue;
  private Object miscValue;
  private final int valueType;

  public DataElement(final boolean bool) {
    valueType = 40;
    booleanValue = bool;
  }

  public DataElement(final int valueType) {
    switch (valueType) {
      case 0:
        break;
      case 48:
      case 56:
        miscValue = new Vector();

        break;
      default:
        throw new IllegalArgumentException("Invalid valueType for this constructor: " + valueType);
    }

    this.valueType = valueType;
  }

  public DataElement(final int valueType, final long value) {
    long min = 0L;
    long max = 0L;
    switch (valueType) {
      case 8:
        max = 255L;

        break;
      case 9:
        max = 65535L;

        break;
      case 10:
        max = 4294967295L;

        break;
      case 16:
        min = -128L;
        max = 127L;

        break;
      case 17:
        min = -32768L;
        max = 32767L;

        break;
      case 18:
        min = -2147483648L;
        max = 2147483647L;

        break;
      case 19:
        min = -9223372036854775808L;
        max = 9223372036854775807L;

        break;
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      default:
        throw new IllegalArgumentException("Invalid 'valueType' for this constructor: " + valueType);
    }

    if ((value < min) || (value > max)) { throw new IllegalArgumentException("Invalid 'value' for specified type: " + value); }

    this.valueType = valueType;
    longValue = value;
  }

  public DataElement(final int valueType, final Object value) {
    boolean isCorrectValue = true;
    switch (valueType) {
      case 32:
      case 64:
        isCorrectValue = value instanceof String;

        break;
      case 24:
        isCorrectValue = value instanceof UUID;

        break;
      case 12:
      case 20:
        isCorrectValue = ((value instanceof byte[])) && (((byte[])value).length == 16);

        break;
      case 11:
        isCorrectValue = ((value instanceof byte[])) && (((byte[])value).length == 8);

        break;
      default:
        throw new IllegalArgumentException("Invalid 'valueType' for this constructor: " + valueType);
    }

    if (!isCorrectValue) { throw new IllegalArgumentException("Invalid 'value' for specified type: " + value); }

    this.valueType = valueType;
    miscValue = value;
  }

  public synchronized void addElement(final DataElement elem) {
    if ((valueType != 48) && (valueType != 56)) { throw new ClassCastException("Invalid element type for this method: " + valueType); }

    if (elem == null) { throw new NullPointerException("Specified element is null"); }

    ((Vector)miscValue).addElement(elem);
  }

  public synchronized void insertElementAt(final DataElement elem, final int index) {
    if ((valueType != 48) && (valueType != 56)) { throw new ClassCastException("Invalid element type for this method: " + valueType); }

    if (elem == null) { throw new NullPointerException("Specified element is null"); }

    if ((index < 0) || (index > ((Vector)miscValue).size())) { throw new IndexOutOfBoundsException("Specified index is out of range"); }

    ((Vector)miscValue).insertElementAt(elem, index);
  }

  public synchronized int getSize() {
    if ((valueType != 48) && (valueType != 56)) { throw new ClassCastException("Invalid element type for this method: " + valueType); }

    return ((Vector)miscValue).size();
  }

  public boolean removeElement(final DataElement elem) {
    if ((valueType != 48) && (valueType != 56)) { throw new ClassCastException("Invalid element type for this method: " + valueType); }

    if (elem == null) { throw new NullPointerException("Specified element is null"); }

    return ((Vector)miscValue).removeElement(elem);
  }

  public int getDataType() {
    return valueType;
  }

  public long getLong() {
    switch (valueType) {
      case 8:
      case 9:
      case 10:
      case 16:
      case 17:
      case 18:
      case 19:
        break;
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      default:
        throw new ClassCastException("Invalid element type for this method: " + valueType);
    }

    return longValue;
  }

  public boolean getBoolean() {
    if (valueType != 40) { throw new ClassCastException("Invalid element type for this method: " + valueType); }

    return booleanValue;
  }

  public synchronized Object getValue() {
    Object retValue = miscValue;
    switch (valueType) {
      case 24:
      case 32:
      case 64:
        break;
      case 48:
      case 56:
        retValue = ((Vector)miscValue).elements();

        break;
      case 11:
      case 12:
      case 20:
        final int length = ((byte[])miscValue).length;
        retValue = new byte[length];
        System.arraycopy(miscValue, 0, retValue, 0, length);

        break;
      default:
        throw new ClassCastException("Invalid element type for this method: " + valueType);
    }

    return retValue;
  }
}
