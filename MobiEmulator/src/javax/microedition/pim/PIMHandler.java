package javax.microedition.pim;

public abstract class PIMHandler {
    private static PIMHandler instance;
    public PIMHandler() {}

    public static PIMHandler getInstance() {
        if (instance == null) {
            try {
                instance = (PIMHandler) Class.forName("javax.microedition.pim.DefaultPIMHandler").newInstance();
            } catch (Exception e) {
                e.printStackTrace();

                throw new Error("PIM handler could not be initialized.");
            }
        }

        return instance;
    }
    public abstract int[] getSupportedFields(int i);
    public abstract boolean isSupportedField(int i, int j);
    public abstract boolean hasDefaultValue(int i, int j);
    public abstract int getFieldDataType(int i, int j);
    public abstract String getFieldLabel(int i, int j);
    public abstract int getDefaultIntValue(int i, int j);
    public abstract String getDefaultStringValue(int i, int j);
    public abstract String[] getDefaultStringArrayValue(int i, int j);
    public abstract long getDefaultDateValue(int i, int j);
    public abstract byte[] getDefaultBinaryValue(int i, int j);
    public abstract boolean getDefaultBooleanValue(int i, int j);
    public abstract int[] getSupportedAttributes(int i, int j);
    public abstract int getSupportedAttributesMask(int i, int j);
    public abstract String getAttributeLabel(int i, int j);
    public abstract boolean isSupportedAttribute(int i, int j, int k);
    public abstract int getStringArraySize(int i, int j);
    public abstract int[] getSupportedArrayElements(int i, int j);
    public abstract String getArrayElementLabel(int i, int j, int k);
    public abstract boolean isSupportedArrayElement(int i, int j, int k);
    public abstract int getMaximumValues(int i, int j);
    public abstract String[] getListNames(int i);
    public abstract String getDefaultListName(int i);
    public abstract Object[] getListKeys(int i, String s);
    public abstract byte[] getListElement(int i, String s, Object obj);
    public abstract Object commitListElement(int i, String s, Object obj, byte abyte0[]);
    public abstract String[] getCategories(int i, String s);
    public abstract void setCategories(int i, String s, String as[]);
    public abstract long parseDate(String s);
    public abstract String composeDate(long l);
    public abstract long parseDateTime(String s);
    public abstract String composeDateTime(long l);
}
