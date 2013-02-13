package javax.microedition.pim;

//Referenced classes of package javax.microedition.pim:
//           PIMField
class EmptyPIMField implements PIMField {
    EmptyPIMField() {}

    public void addValue(int i, Object obj) {}
    public Object getValue(int index) {
        return null;
    }
    public void setValue(int i, Object obj, int j) {}
    public int getAttributes(int index) {
        throw new IndexOutOfBoundsException("No data in field");
    }
    public boolean containsData() {
        return false;
    }
    public int getValueCount() {
        return 0;
    }
    public void removeValue(int i) {}
    public boolean isScalar() {
        return true;
    }
}
