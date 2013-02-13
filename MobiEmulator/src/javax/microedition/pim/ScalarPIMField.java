package javax.microedition.pim;

//Referenced classes of package javax.microedition.pim:
//           PIMField
class ScalarPIMField implements PIMField {
    private int    attributes;
    private Object value;
    ScalarPIMField() {}

    public void addValue(int attributes, Object value) {
        this.value      = value;
        this.attributes = attributes;
    }
    public Object getValue(int index) {
        return value;
    }
    public void setValue(int attributes, Object value, int index) {
        this.value      = value;
        this.attributes = attributes;
    }
    public int getAttributes(int index) {
        checkIndex(index);

        return attributes;
    }
    public boolean containsData() {
        return true;
    }
    public int getValueCount() {
        return 1;
    }
    public void removeValue(int i) {}
    public boolean isScalar() {
        return true;
    }
    private void checkIndex(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        } else {
            return;
        }
    }
}
