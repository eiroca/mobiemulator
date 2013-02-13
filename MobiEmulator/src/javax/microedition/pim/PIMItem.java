package javax.microedition.pim;

//Referenced classes of package javax.microedition.pim:
//           PIMException, PIMList
public interface PIMItem {
    public static final int ATTR_NONE                    = 0;
    public static final int BINARY                       = 0;
    public static final int BOOLEAN                      = 1;
    public static final int DATE                         = 2;
    public static final int EXTENDED_ATTRIBUTE_MIN_VALUE = 0x1000000;
    public static final int EXTENDED_FIELD_MIN_VALUE     = 0x1000000;
    public static final int INT                          = 3;
    public static final int STRING                       = 4;
    public static final int STRING_ARRAY                 = 5;
    public abstract PIMList getPIMList();
    public abstract void commit() throws PIMException;
    public abstract boolean isModified();
    public abstract int[] getFields();
    public abstract byte[] getBinary(int i, int j);
    public abstract void addBinary(int i, int j, byte abyte0[], int k, int l);
    public abstract void setBinary(int i, int j, int k, byte abyte0[], int l, int i1);
    public abstract long getDate(int i, int j);
    public abstract void addDate(int i, int j, long l);
    public abstract void setDate(int i, int j, int k, long l);
    public abstract int getInt(int i, int j);
    public abstract void addInt(int i, int j, int k);
    public abstract void setInt(int i, int j, int k, int l);
    public abstract String getString(int i, int j);
    public abstract void addString(int i, int j, String s);
    public abstract void setString(int i, int j, int k, String s);
    public abstract boolean getBoolean(int i, int j);
    public abstract void addBoolean(int i, int j, boolean flag);
    public abstract void setBoolean(int i, int j, int k, boolean flag);
    public abstract String[] getStringArray(int i, int j);
    public abstract void addStringArray(int i, int j, String as[]);
    public abstract void setStringArray(int i, int j, int k, String as[]);
    public abstract int countValues(int i);
    public abstract void removeValue(int i, int j);
    public abstract int getAttributes(int i, int j);
    public abstract void addToCategory(String s) throws PIMException;
    public abstract void removeFromCategory(String s);
    public abstract String[] getCategories();
    public abstract int maxCategories();
}
