package javax.microedition.pim;

abstract interface PIMField {
    public abstract void addValue(int i, Object obj);
    public abstract Object getValue(int i);
    public abstract void setValue(int i, Object obj, int j);
    public abstract int getAttributes(int i);
    public abstract boolean containsData();
    public abstract int getValueCount();
    public abstract void removeValue(int i);
    public abstract boolean isScalar();
}
