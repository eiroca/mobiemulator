package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.util.Vector;

//Referenced classes of package javax.microedition.pim:
//           PIMField
class VectorPIMField implements PIMField {
    private final Vector values     = new Vector();
    private final Vector attributes = new Vector();
    VectorPIMField() {}

    public void addValue(int attributes, Object value) {
        values.addElement(value);
        this.attributes.addElement(new Integer(attributes));
    }
    public Object getValue(int index) {
        return values.elementAt(index);
    }
    public void setValue(int attributes, Object value, int index) {
        values.setElementAt(value, index);
        this.attributes.setElementAt(new Integer(attributes), index);
    }
    public int getAttributes(int index) {
        return ((Integer) attributes.elementAt(index)).intValue();
    }
    public boolean containsData() {
        return values.size() > 0;
    }
    public int getValueCount() {
        return values.size();
    }
    public void removeValue(int index) {
        values.removeElementAt(index);
        attributes.removeElementAt(index);
    }
    public boolean isScalar() {
        return false;
    }
}
