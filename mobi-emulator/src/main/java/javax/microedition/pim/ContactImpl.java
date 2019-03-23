package javax.microedition.pim;

// ~--- JDK imports ------------------------------------------------------------

import javax.microedition.pim.formats.VCard21Format;

// Referenced classes of package javax.microedition.pim:
// AbstractPIMItem, ContactListImpl, PIMFormat

public class ContactImpl extends AbstractPIMItem implements Contact {

  public ContactImpl(final ContactListImpl list) {
    super(list, 1);
  }

  ContactImpl(final ContactListImpl list, final Contact base) {
    super(list, base);
  }

  @Override
  public int getPreferredIndex(final int field) {
    final int indices = countValues(field);
    for (int i = 0; i < indices; i++) {
      final int attributes = getAttributes(field, i);
      if ((attributes & 0x80) != 0) { return i; }
    }

    return -1;
  }

  @Override
  PIMFormat getEncodingFormat() {
    return new VCard21Format();
  }

  static boolean isValidPIMField(final int field) {
    switch (field) {
      case 100: // 'd'
      case 101: // 'e'
      case 102: // 'f'
      case 103: // 'g'
      case 104: // 'h'
      case 105: // 'i'
      case 106: // 'j'
      case 107: // 'k'
      case 108: // 'l'
      case 109: // 'm'
      case 110: // 'n'
      case 111: // 'o'
      case 112: // 'p'
      case 113: // 'q'
      case 114: // 'r'
      case 115: // 's'
      case 116: // 't'
      case 117: // 'u'
      case 118: // 'v'
        return true;
    }

    return false;
  }

  @Override
  public void addBinary(final int field, final int attributes, final byte value[], final int offset, final int length) {
    super.addBinary(field, attributes, value, offset, length);
    if (field == 112) {
      for (; countValues(113) > 0; removeValue(113, 0)) {
      }
    }
  }

  @Override
  public void addString(final int field, final int attributes, final String value) {
    super.addString(field, attributes, value);
    if (field == 113) {
      for (; countValues(112) > 0; removeValue(112, 0)) {
      }
    }
  }

  @Override
  public void addInt(final int field, final int attributes, final int value) {
    if (field == 102) {
      validateClass(value);
    }
    super.addInt(field, attributes, value);
  }

  @Override
  public void setInt(final int field, final int index, final int attributes, final int value) {
    if (field == 102) {
      validateClass(value);
    }
    super.setInt(field, index, attributes, value);
  }

  private void validateClass(final int value) {
    switch (value) {
      case 200:
      case 201:
      case 202:
        return;
    }

    throw new IllegalArgumentException("Invalid CLASS value: " + value);
  }

  @Override
  protected int getRevisionField() {
    return 114;
  }

  @Override
  protected int getUIDField() {
    return 117;
  }

  @Override
  protected String toDisplayableString() {
    return "Contact[" + formatData() + "]";
  }
}
