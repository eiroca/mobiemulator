package javax.microedition.pim;

// ~--- JDK imports ------------------------------------------------------------

import java.util.Vector;

// Referenced classes of package javax.microedition.pim:
// PIMField
class VectorPIMField implements PIMField {

  private final Vector values = new Vector();
  private final Vector attributes = new Vector();

  VectorPIMField() {
  }

  @Override
  public void addValue(final int attributes, final Object value) {
    values.addElement(value);
    this.attributes.addElement(new Integer(attributes));
  }

  @Override
  public Object getValue(final int index) {
    return values.elementAt(index);
  }

  @Override
  public void setValue(final int attributes, final Object value, final int index) {
    values.setElementAt(value, index);
    this.attributes.setElementAt(new Integer(attributes), index);
  }

  @Override
  public int getAttributes(final int index) {
    return ((Integer)attributes.elementAt(index)).intValue();
  }

  @Override
  public boolean containsData() {
    return values.size() > 0;
  }

  @Override
  public int getValueCount() {
    return values.size();
  }

  @Override
  public void removeValue(final int index) {
    values.removeElementAt(index);
    attributes.removeElementAt(index);
  }

  @Override
  public boolean isScalar() {
    return false;
  }
}
