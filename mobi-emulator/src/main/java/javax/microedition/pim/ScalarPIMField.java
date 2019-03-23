package javax.microedition.pim;

// Referenced classes of package javax.microedition.pim:
// PIMField
class ScalarPIMField implements PIMField {

  private int attributes;
  private Object value;

  ScalarPIMField() {
  }

  @Override
  public void addValue(final int attributes, final Object value) {
    this.value = value;
    this.attributes = attributes;
  }

  @Override
  public Object getValue(final int index) {
    return value;
  }

  @Override
  public void setValue(final int attributes, final Object value, final int index) {
    this.value = value;
    this.attributes = attributes;
  }

  @Override
  public int getAttributes(final int index) {
    checkIndex(index);

    return attributes;
  }

  @Override
  public boolean containsData() {
    return true;
  }

  @Override
  public int getValueCount() {
    return 1;
  }

  @Override
  public void removeValue(final int i) {
  }

  @Override
  public boolean isScalar() {
    return true;
  }

  private void checkIndex(final int index) {
    if (index != 0) {
      throw new IndexOutOfBoundsException(String.valueOf(index));
    }
    else {
      return;
    }
  }
}
