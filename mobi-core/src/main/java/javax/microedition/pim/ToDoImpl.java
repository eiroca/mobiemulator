package javax.microedition.pim;

// ~--- JDK imports ------------------------------------------------------------

import javax.microedition.pim.formats.VCalendar10Format;

// Referenced classes of package javax.microedition.pim:
// AbstractPIMItem, ToDoListImpl, PIMFormat
public class ToDoImpl extends AbstractPIMItem implements ToDo {

  public ToDoImpl(final ToDoListImpl list) {
    super(list, 3);
  }

  ToDoImpl(final ToDoListImpl list, final ToDo base) {
    super(list, base);
  }

  @Override
  PIMFormat getEncodingFormat() {
    return new VCalendar10Format();
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
        return true;
    }

    return false;
  }

  @Override
  public void addInt(final int field, final int attributes, final int value) {
    switch (field) {
      case 100: // 'd'
        validateClass(value);

        break;
      case 105: // 'i'
        validatePriority(value);

        break;
    }
    super.addInt(field, attributes, value);
  }

  @Override
  public void setInt(final int field, final int index, final int attributes, final int value) {
    switch (field) {
      case 100: // 'd'
        validateClass(value);

        break;
      case 105: // 'i'
        validatePriority(value);

        break;
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

  private void validatePriority(final int value) {
    if ((value < 0) || (value > 9)) {
      throw new IllegalArgumentException("Invalid PRIORITY value: " + value);
    }
    else {
      return;
    }
  }

  @Override
  protected int getRevisionField() {
    return 106;
  }

  @Override
  protected int getUIDField() {
    return 108;
  }

  @Override
  protected String toDisplayableString() {
    return "ToDo[" + formatData() + "]";
  }
}
