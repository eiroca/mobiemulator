package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

//Referenced classes of package javax.microedition.pim:
//           AbstractPIMItem, PIMHandler, PIMField
public abstract class AbstractPIMList implements PIMList {
    private Vector       items;
    private int          mode;
    private final String name;
    private boolean      open;
    private final int    type;
    AbstractPIMList(int type, String name, int mode) {
        items     = new Vector();
        open      = true;
        this.type = type;
        this.name = name;
        this.mode = mode;
    }

    public void addCategory(String category) throws PIMException {
        checkWritePermission();
        checkOpen();
        checkNullCategory(category);
        PIMHandler handler      = PIMHandler.getInstance();
        String     categories[] = handler.getCategories(type, name);
        for (String cat:categories) {
            if (category.equals(cat)) {
                return;
            }
        }
        String newCategories[] = new String[categories.length + 1];
        System.arraycopy(categories, 0, newCategories, 0, categories.length);
        newCategories[categories.length] = category;
        handler.setCategories(type, name, newCategories);
    }
    public Enumeration itemsByCategory(String category) throws PIMException {
        checkReadPermission();
        checkOpen();
        Vector v = new Vector();
        if ((category == null) || category.equals("UNCATEGORIZED")) {
            Enumeration e = items.elements();
            do {
                if (!e.hasMoreElements()) {
                    break;
                }
                AbstractPIMItem item = (AbstractPIMItem) e.nextElement();
                if (item.getCategoriesRaw() == null) {
                    v.addElement(item);
                }
            } while (true);
        } else {
            Enumeration e = items.elements();
            do {
                if (!e.hasMoreElements()) {
                    break;
                }
                AbstractPIMItem item = (AbstractPIMItem) e.nextElement();
                if (item.isInCategory(category)) {
                    v.addElement(item);
                }
            } while (true);
        }

        return v.elements();
    }
    public Enumeration items(PIMItem matchingItem) throws PIMException {
        checkReadPermission();
        checkOpen();
        if (!equals(matchingItem.getPIMList())) {
            throw new IllegalArgumentException("Cannot match item from another list");
        }
        int    searchFields[]         = matchingItem.getFields();
        int    searchFieldDataTypes[] = new int[searchFields.length];
        Object searchData[][]         = new Object[searchFields.length][];
        for (int i = 0;i < searchFields.length;i++) {
            searchFieldDataTypes[i] = getFieldDataType(searchFields[i]);
            searchData[i]           = new Object[matchingItem.countValues(searchFields[i])];
            for (int j = 0;j < searchData[i].length;j++) {
                searchData[i][j] = ((AbstractPIMItem) matchingItem).getField(searchFields[i], false, false).getValue(j);
                switch (searchFieldDataTypes[i]) {
                default :
                    break;
                case 4 :    // '\004'
                    String s = (String) searchData[i][j];
                    if (s != null) {
                        searchData[i][j] = s.toUpperCase();
                    }

                    break;
                case 5 :    // '\005'
                    String a[] = (String[]) searchData[i][j];
                    if (a == null) {
                        break;
                    }
                    for (int k = 0;k < a.length;k++) {
                        if (a[k] != null) {
                            a[k] = a[k].toUpperCase();
                        }
                    }

                    break;
                }
            }
        }
        Vector      v = new Vector();
        Enumeration e = items.elements();
label0:
        do {
            if (!e.hasMoreElements()) {
                break;
            }
            AbstractPIMItem item = (AbstractPIMItem) e.nextElement();
            for (int i = 0;i < searchFields.length;i++) {
                int field       = searchFields[i];
                int itemIndices = item.countValues(field);
                for (int j = 0;j < searchData[i].length;j++) {
                    boolean matchedThisIndex = false;
                    int     searchAttributes = matchingItem.getAttributes(field, j);
                    for (int k = 0;(k < itemIndices) &&!matchedThisIndex;k++) {
                        Object value = item.getField(field, false, false).getValue(k);
                        switch (searchFieldDataTypes[i]) {
                        default :
                            break;
                        case 1 :    // '\001'
                        case 2 :    // '\002'
                        case 3 :    // '\003'
                        {
                            if (searchData[i][j].equals(value)) {
                                matchedThisIndex = true;
                            }

                            break;
                        }
                        case 0 :    // '\0'
                        {
                            byte a[] = (byte[]) searchData[i][j];
                            byte b[] = (byte[]) value;
                            if ((b == null) || (a.length != b.length)) {
                                break;
                            }
                            boolean arrayMatches = true;
                            for (int m = 0;(m < a.length) && arrayMatches;m++) {
                                arrayMatches = a[m] == b[m];
                            }
                            matchedThisIndex = arrayMatches;

                            break;
                        }
                        case 4 :    // '\004'
                        {
                            String s1 = (String) searchData[i][j];
                            String s2 = (String) value;
                            if (s2 == null) {
                                if (s1 == null) {
                                    matchedThisIndex = true;
                                }

                                break;
                            }
                            if (s2.toUpperCase().indexOf(s1) != -1) {
                                matchedThisIndex = true;
                            }

                            break;
                        }
                        case 5 :    // '\005'
                        {
                            String a[] = (String[]) searchData[i][j];
                            String b[] = (String[]) value;
                            if (a == null) {
                                if (b == null) {
                                    matchedThisIndex = true;
                                }

                                break;
                            }
                            if ((b == null) || (a.length != b.length)) {
                                break;
                            }
                            boolean arrayMatches = true;
                            for (int m = 0;(m < a.length) && arrayMatches;m++) {
                                if ((a[m] != null) && (a[m].length() > 0)) {
                                    arrayMatches = (b[m] != null) && (b[m].toUpperCase().indexOf(a[m]) != -1);
                                }
                            }
                            matchedThisIndex = arrayMatches;

                            break;
                        }
                        }
                    }
                    if (!matchedThisIndex) {
                        continue label0;
                    }
                }
            }
            v.addElement(item);
        } while (true);

        return v.elements();
    }
    public void close() throws PIMException {
        checkOpen();
        open  = false;
        items = null;
    }
    public String getArrayElementLabel(int stringArrayField, int arrayElement) {
        if (getFieldDataType(stringArrayField) != 5) {
            throw new IllegalArgumentException("Not a string array field");
        }
        if (!isSupportedArrayElement(stringArrayField, arrayElement)) {
            throw new IllegalArgumentException("Invalid array element " + arrayElement);
        } else {
            return PIMHandler.getInstance().getArrayElementLabel(type, stringArrayField, arrayElement);
        }
    }
    public int[] getSupportedArrayElements(int stringArrayField) {
        if (getFieldDataType(stringArrayField) != 5) {
            throw new IllegalArgumentException("Not a string array field");
        } else {
            return PIMHandler.getInstance().getSupportedArrayElements(type, stringArrayField);
        }
    }
    public String getAttributeLabel(int attribute) {
        checkAttribute(attribute);
        String label = PIMHandler.getInstance().getAttributeLabel(type, attribute);
        if (label == null) {
            throw new IllegalArgumentException("Invalid attribute: " + attribute);
        } else {
            return label;
        }
    }
    public int maxValues(int field) {
        try {
            checkField(field);
        } catch (UnsupportedFieldException e) {
            return 0;
        }

        return PIMHandler.getInstance().getMaximumValues(type, field);
    }
    public boolean isSupportedAttribute(int field, int attribute) {
        if (!isSupportedField(field)) {
            return false;
        }
        int i;
        for (i = attribute;(i & 1) == 0 && (i != 0);i >>= 1) {}
        return i == 1 && PIMHandler.getInstance().isSupportedAttribute(type, field, attribute);
    }
    public int maxCategories() {
        return -1;
    }
    public String[] getCategories() throws PIMException {
        checkOpen();

        return PIMHandler.getInstance().getCategories(type, name);
    }
    public boolean isCategory(String category) throws PIMException {
        checkOpen();
        checkNullCategory(category);
        String categories[] = PIMHandler.getInstance().getCategories(type, name);
        for (String category1 : categories) {
            if (category.equals(category1)) {
                return true;
            }
        }

        return false;
    }
    public String getName() {
        return name;
    }
    public int stringArraySize(int stringArrayField) {
        if (getFieldDataType(stringArrayField) != 5) {
            throw new IllegalArgumentException("Not a string array field");
        } else {
            return PIMHandler.getInstance().getStringArraySize(type, stringArrayField);
        }
    }
    public boolean isSupportedField(int field) {
        return PIMHandler.getInstance().isSupportedField(type, field);
    }
    public void deleteCategory(String category, boolean deleteUnassignedItems) throws PIMException {
        checkWritePermission();
        checkOpen();
        checkNullCategory(category);
        String categories[]  = PIMHandler.getInstance().getCategories(type, name);
        int    categoryIndex = -1;
        for (int i = 0;(i < categories.length) && (categoryIndex == -1);i++) {
            if (category.equals(categories[i])) {
                categoryIndex = i;
            }
        }
        if (categoryIndex == -1) {
            return;
        }
        String newCategories[] = new String[categories.length - 1];
        System.arraycopy(categories, 0, newCategories, 0, categoryIndex);
        System.arraycopy(categories, categoryIndex + 1, newCategories, categoryIndex,
                         newCategories.length - categoryIndex);
        PIMHandler.getInstance().setCategories(type, name, newCategories);
        AbstractPIMItem a[] = new AbstractPIMItem[items.size()];
        items.copyInto(a);
        for (AbstractPIMItem anA : a) {
            if (!anA.isInCategory(category)) {
                continue;
            }
            anA.removeFromCategory(category);
            if (deleteUnassignedItems && (anA.getCategories().length == 0)) {
                items.removeElement(anA);
            }
        }
    }
    public Enumeration items() throws PIMException {
        checkReadPermission();
        checkOpen();
        final PIMItem data[] = new PIMItem[items.size()];
        items.copyInto(data);

        return new Enumeration() {
            int index;
            public boolean hasMoreElements() {
                return index < data.length;
            }
            public Object nextElement() {
                try {
                    return data[index++];
                } catch (ArrayIndexOutOfBoundsException e) {}

                throw new NoSuchElementException();
            }
            {
                index = 0;
            }
        };
    }
    public int[] getSupportedAttributes(int field) {
        checkField(field);

        return PIMHandler.getInstance().getSupportedAttributes(type, field);
    }
    public int getFieldDataType(int field) {
        int dataType = PIMHandler.getInstance().getFieldDataType(type, field);
        if (dataType == -1) {
            throw AbstractPIMItem.complaintAboutField(type, field);
        } else {
            return dataType;
        }
    }
    public String getFieldLabel(int field) {
        checkField(field);

        return PIMHandler.getInstance().getFieldLabel(type, field);
    }
    public void renameCategory(String currentCategory, String newCategory) throws PIMException {
        checkWritePermission();
        checkOpen();
        if ((currentCategory == null) || (newCategory == null)) {
            throw new NullPointerException("Null category");
        }
        String categories[] = PIMHandler.getInstance().getCategories(type, name);
        if (newCategory.equals(currentCategory)) {
            return;
        }
        int oldCategoryIndex = -1;
        int newCategoryIndex = -1;
        for (int i = 0;i < categories.length;i++) {
            if (currentCategory.equals(categories[i])) {
                oldCategoryIndex = i;

                continue;
            }
            if (newCategory.equals(categories[i])) {
                newCategoryIndex = i;
            }
        }
        if (oldCategoryIndex == -1) {
            throw new PIMException("No such category: " + currentCategory);
        }
        if (newCategoryIndex == -1) {
            categories[oldCategoryIndex] = newCategory;
        } else {
            String a[] = new String[categories.length - 1];
            System.arraycopy(categories, 0, a, 0, oldCategoryIndex);
            System.arraycopy(categories, oldCategoryIndex + 1, a, oldCategoryIndex, a.length - oldCategoryIndex);
            categories = a;
        }
        PIMHandler.getInstance().setCategories(type, name, categories);
        Enumeration e = items.elements();
        do {
            if (!e.hasMoreElements()) {
                break;
            }
            AbstractPIMItem item = (AbstractPIMItem) e.nextElement();
            if (item.isInCategory(currentCategory)) {
                item.removeFromCategory(currentCategory);
                item.addToCategory(newCategory);
            }
        } while (true);
    }
    public boolean isSupportedArrayElement(int stringArrayField, int arrayElement) {
        int dataType = PIMHandler.getInstance().getFieldDataType(type, stringArrayField);
        return dataType == 5 && PIMHandler.getInstance().isSupportedArrayElement(type, stringArrayField, arrayElement);
    }
    public int[] getSupportedFields() {
        return PIMHandler.getInstance().getSupportedFields(type);
    }
    public Enumeration items(String matchingValue) throws PIMException {
        checkReadPermission();
        checkOpen();
        matchingValue = matchingValue.toUpperCase();
        Vector      v = new Vector();
        Enumeration e = items.elements();
        while (e.hasMoreElements()) {
            AbstractPIMItem item     = (AbstractPIMItem) e.nextElement();
            int             fields[] = item.getFields();
            for (int i = 0;;i++) {
                if (i >= fields.length) {
                    break;
                }
                switch (getFieldDataType(fields[i])) {
                default :
                    break;
                case 4 :    // '\004'
                {
                    for (int j = item.countValues(fields[i]) - 1;;j--) {
                        if (j < 0) {
                            break;
                        }
                        String value = item.getString(fields[i], j);
                        if ((value != null) && (value.toUpperCase().indexOf(matchingValue) != -1)) {
                            v.addElement(item);
                        }
                    }

                    break;
                }
                case 5 :    // '\005'
                {
                    for (int j = item.countValues(fields[i]) - 1;j >= 0;j--) {
                        String a[] = item.getStringArray(fields[i], j);
                        if (a == null) {
                            continue;
                        }
                        for (int k = 0;;k++) {
                            if (k >= a.length) {
                                break;
                            }
                            if ((a[k] != null) && (a[k].toUpperCase().indexOf(matchingValue) != -1)) {
                                v.addElement(item);
                            }
                        }
                    }

                    break;
                }
                }
            }
        }

        return v.elements();
    }
    protected void checkReadPermission() throws SecurityException {
        if (mode == 2) {
            throw new SecurityException("List cannot be read");
        } else {
            return;
        }
    }
    protected void checkWritePermission() throws SecurityException {
        if (mode == 1) {
            throw new SecurityException("List cannot be written");
        } else {
            return;
        }
    }
    protected void checkOpen() throws PIMException {
        if (!open) {
            throw new PIMException("List is closed.");
        } else {
            return;
        }
    }
    protected void checkNullCategory(String category) {
        if (category == null) {
            throw new NullPointerException("Null category");
        } else {
            return;
        }
    }
    public void addItem(AbstractPIMItem item) {
        items.addElement(item);
        item.setPIMList(this);
    }
    void removeItem(PIMItem item) throws PIMException {
        checkWritePermission();
        checkOpen();
        if (item == null) {
            throw new NullPointerException("Null item");
        }
        if (!items.removeElement(item)) {
            throw new PIMException("Item not in list");
        } else {
            ((AbstractPIMItem) item).remove();

            return;
        }
    }
    Object commit(Object key, byte data[]) {
        return PIMHandler.getInstance().commitListElement(type, name, key, data);
    }
    int getType() {
        return type;
    }
    private void checkField(int field) {
        if (PIMHandler.getInstance().getFieldDataType(type, field) == -1) {
            throw AbstractPIMItem.complaintAboutField(type, field);
        } else {
            return;
        }
    }
    private void checkAttribute(int attribute) {
        if (attribute == 0) {
            return;
        }
        for (;(attribute & 1) == 0 && (attribute != 0);attribute >>= 1) {}
        if (attribute != 1) {
            throw new IllegalArgumentException("Invalid attribute: " + attribute);
        } else {
            return;
        }
    }
}
