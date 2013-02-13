package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;

//Referenced classes of package javax.microedition.pim:
//           PIMException, PIMItem
public interface PIMList {
    public static final String UNCATEGORIZED = null;
    public abstract String getName();
    public abstract void close() throws PIMException;
    public abstract Enumeration items() throws PIMException;
    public abstract Enumeration items(PIMItem pimitem) throws PIMException;
    public abstract Enumeration items(String s) throws PIMException;
    public abstract Enumeration itemsByCategory(String s) throws PIMException;
    public abstract String[] getCategories() throws PIMException;
    public abstract boolean isCategory(String s) throws PIMException;
    public abstract void addCategory(String s) throws PIMException;
    public abstract void deleteCategory(String s, boolean flag) throws PIMException;
    public abstract void renameCategory(String s, String s1) throws PIMException;
    public abstract int maxCategories();
    public abstract boolean isSupportedField(int i);
    public abstract int[] getSupportedFields();
    public abstract boolean isSupportedAttribute(int i, int j);
    public abstract int[] getSupportedAttributes(int i);
    public abstract boolean isSupportedArrayElement(int i, int j);
    public abstract int[] getSupportedArrayElements(int i);
    public abstract int getFieldDataType(int i);
    public abstract String getFieldLabel(int i);
    public abstract String getAttributeLabel(int i);
    public abstract String getArrayElementLabel(int i, int j);
    public abstract int maxValues(int i);
    public abstract int stringArraySize(int i);
}
