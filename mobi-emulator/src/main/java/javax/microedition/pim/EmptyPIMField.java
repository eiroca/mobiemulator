package javax.microedition.pim;

// Referenced classes of package javax.microedition.pim:
// PIMField
class EmptyPIMField implements PIMField {

  EmptyPIMField() {
  }

  @Override
  public void addValue(final int i, final Object obj) {
  }

  @Override
  public Object getValue(final int index) {
    return null;
  }

  @Override
  public void setValue(final int i, final Object obj, final int j) {
  }

  @Override
  public int getAttributes(final int index) {
    throw new IndexOutOfBoundsException("No data in field");
  }

  @Override
  public boolean containsData() {
    return false;
  }

  @Override
  public int getValueCount() {
    return 0;
  }

  @Override
  public void removeValue(final int i) {
  }

  @Override
  public boolean isScalar() {
    return true;
  }
}
