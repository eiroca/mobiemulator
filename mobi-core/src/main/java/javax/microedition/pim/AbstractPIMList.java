package javax.microedition.pim;

// ~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

// Referenced classes of package javax.microedition.pim:
// AbstractPIMItem, PIMHandler, PIMField
public abstract class AbstractPIMList implements PIMList {

  private Vector items;
  private final int mode;
  private final String name;
  private boolean open;
  private final int type;

  AbstractPIMList(final int type, final String name, final int mode) {
    items = new Vector();
    open = true;
    this.type = type;
    this.name = name;
    this.mode = mode;
  }

  @Override
  public void addCategory(final String category) throws PIMException {
    checkWritePermission();
    checkOpen();
    checkNullCategory(category);
    final PIMHandler handler = PIMHandler.getInstance();
    final String categories[] = handler.getCategories(type, name);
    for (final String cat : categories) {
      if (category.equals(cat)) { return; }
    }
    final String newCategories[] = new String[categories.length + 1];
    System.arraycopy(categories, 0, newCategories, 0, categories.length);
    newCategories[categories.length] = category;
    handler.setCategories(type, name, newCategories);
  }

  @Override
  public Enumeration itemsByCategory(final String category) throws PIMException {
    checkReadPermission();
    checkOpen();
    final Vector v = new Vector();
    if ((category == null) || category.equals("UNCATEGORIZED")) {
      final Enumeration e = items.elements();
      do {
        if (!e.hasMoreElements()) {
          break;
        }
        final AbstractPIMItem item = (AbstractPIMItem)e.nextElement();
        if (item.getCategoriesRaw() == null) {
          v.addElement(item);
        }
      }
      while (true);
    }
    else {
      final Enumeration e = items.elements();
      do {
        if (!e.hasMoreElements()) {
          break;
        }
        final AbstractPIMItem item = (AbstractPIMItem)e.nextElement();
        if (item.isInCategory(category)) {
          v.addElement(item);
        }
      }
      while (true);
    }

    return v.elements();
  }

  @Override
  public Enumeration items(final PIMItem matchingItem) throws PIMException {
    checkReadPermission();
    checkOpen();
    if (!equals(matchingItem.getPIMList())) { throw new IllegalArgumentException("Cannot match item from another list"); }
    final int searchFields[] = matchingItem.getFields();
    final int searchFieldDataTypes[] = new int[searchFields.length];
    final Object searchData[][] = new Object[searchFields.length][];
    for (int i = 0; i < searchFields.length; i++) {
      searchFieldDataTypes[i] = getFieldDataType(searchFields[i]);
      searchData[i] = new Object[matchingItem.countValues(searchFields[i])];
      for (int j = 0; j < searchData[i].length; j++) {
        searchData[i][j] = ((AbstractPIMItem)matchingItem).getField(searchFields[i], false, false).getValue(j);
        switch (searchFieldDataTypes[i]) {
          default:
            break;
          case 4: // '\004'
            final String s = (String)searchData[i][j];
            if (s != null) {
              searchData[i][j] = s.toUpperCase();
            }

            break;
          case 5: // '\005'
            final String a[] = (String[])searchData[i][j];
            if (a == null) {
              break;
            }
            for (int k = 0; k < a.length; k++) {
              if (a[k] != null) {
                a[k] = a[k].toUpperCase();
              }
            }

            break;
        }
      }
    }
    final Vector v = new Vector();
    final Enumeration e = items.elements();
    label0: do {
      if (!e.hasMoreElements()) {
        break;
      }
      final AbstractPIMItem item = (AbstractPIMItem)e.nextElement();
      for (int i = 0; i < searchFields.length; i++) {
        final int field = searchFields[i];
        final int itemIndices = item.countValues(field);
        for (int j = 0; j < searchData[i].length; j++) {
          boolean matchedThisIndex = false;
          matchingItem.getAttributes(field, j);
          for (int k = 0; (k < itemIndices) && !matchedThisIndex; k++) {
            final Object value = item.getField(field, false, false).getValue(k);
            switch (searchFieldDataTypes[i]) {
              default:
                break;
              case 1: // '\001'
              case 2: // '\002'
              case 3: // '\003'
              {
                if (searchData[i][j].equals(value)) {
                  matchedThisIndex = true;
                }

                break;
              }
              case 0: // '\0'
              {
                final byte a[] = (byte[])searchData[i][j];
                final byte b[] = (byte[])value;
                if ((b == null) || (a.length != b.length)) {
                  break;
                }
                boolean arrayMatches = true;
                for (int m = 0; (m < a.length) && arrayMatches; m++) {
                  arrayMatches = a[m] == b[m];
                }
                matchedThisIndex = arrayMatches;

                break;
              }
              case 4: // '\004'
              {
                final String s1 = (String)searchData[i][j];
                final String s2 = (String)value;
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
              case 5: // '\005'
              {
                final String a[] = (String[])searchData[i][j];
                final String b[] = (String[])value;
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
                for (int m = 0; (m < a.length) && arrayMatches; m++) {
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
    }
    while (true);

    return v.elements();
  }

  @Override
  public void close() throws PIMException {
    checkOpen();
    open = false;
    items = null;
  }

  @Override
  public String getArrayElementLabel(final int stringArrayField, final int arrayElement) {
    if (getFieldDataType(stringArrayField) != 5) { throw new IllegalArgumentException("Not a string array field"); }
    if (!isSupportedArrayElement(stringArrayField, arrayElement)) {
      throw new IllegalArgumentException("Invalid array element " + arrayElement);
    }
    else {
      return PIMHandler.getInstance().getArrayElementLabel(type, stringArrayField, arrayElement);
    }
  }

  @Override
  public int[] getSupportedArrayElements(final int stringArrayField) {
    if (getFieldDataType(stringArrayField) != 5) {
      throw new IllegalArgumentException("Not a string array field");
    }
    else {
      return PIMHandler.getInstance().getSupportedArrayElements(type, stringArrayField);
    }
  }

  @Override
  public String getAttributeLabel(final int attribute) {
    checkAttribute(attribute);
    final String label = PIMHandler.getInstance().getAttributeLabel(type, attribute);
    if (label == null) {
      throw new IllegalArgumentException("Invalid attribute: " + attribute);
    }
    else {
      return label;
    }
  }

  @Override
  public int maxValues(final int field) {
    try {
      checkField(field);
    }
    catch (final UnsupportedFieldException e) {
      return 0;
    }

    return PIMHandler.getInstance().getMaximumValues(type, field);
  }

  @Override
  public boolean isSupportedAttribute(final int field, final int attribute) {
    if (!isSupportedField(field)) { return false; }
    int i;
    for (i = attribute; ((i & 1) == 0) && (i != 0); i >>= 1) {
    }
    return (i == 1) && PIMHandler.getInstance().isSupportedAttribute(type, field, attribute);
  }

  @Override
  public int maxCategories() {
    return -1;
  }

  @Override
  public String[] getCategories() throws PIMException {
    checkOpen();

    return PIMHandler.getInstance().getCategories(type, name);
  }

  @Override
  public boolean isCategory(final String category) throws PIMException {
    checkOpen();
    checkNullCategory(category);
    final String categories[] = PIMHandler.getInstance().getCategories(type, name);
    for (final String category1 : categories) {
      if (category.equals(category1)) { return true; }
    }

    return false;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int stringArraySize(final int stringArrayField) {
    if (getFieldDataType(stringArrayField) != 5) {
      throw new IllegalArgumentException("Not a string array field");
    }
    else {
      return PIMHandler.getInstance().getStringArraySize(type, stringArrayField);
    }
  }

  @Override
  public boolean isSupportedField(final int field) {
    return PIMHandler.getInstance().isSupportedField(type, field);
  }

  @Override
  public void deleteCategory(final String category, final boolean deleteUnassignedItems) throws PIMException {
    checkWritePermission();
    checkOpen();
    checkNullCategory(category);
    final String categories[] = PIMHandler.getInstance().getCategories(type, name);
    int categoryIndex = -1;
    for (int i = 0; (i < categories.length) && (categoryIndex == -1); i++) {
      if (category.equals(categories[i])) {
        categoryIndex = i;
      }
    }
    if (categoryIndex == -1) { return; }
    final String newCategories[] = new String[categories.length - 1];
    System.arraycopy(categories, 0, newCategories, 0, categoryIndex);
    System.arraycopy(categories, categoryIndex + 1, newCategories, categoryIndex,
        newCategories.length - categoryIndex);
    PIMHandler.getInstance().setCategories(type, name, newCategories);
    final AbstractPIMItem a[] = new AbstractPIMItem[items.size()];
    items.copyInto(a);
    for (final AbstractPIMItem anA : a) {
      if (!anA.isInCategory(category)) {
        continue;
      }
      anA.removeFromCategory(category);
      if (deleteUnassignedItems && (anA.getCategories().length == 0)) {
        items.removeElement(anA);
      }
    }
  }

  @Override
  public Enumeration items() throws PIMException {
    checkReadPermission();
    checkOpen();
    final PIMItem data[] = new PIMItem[items.size()];
    items.copyInto(data);

    return new Enumeration() {

      int index;

      @Override
      public boolean hasMoreElements() {
        return index < data.length;
      }

      @Override
      public Object nextElement() {
        try {
          return data[index++];
        }
        catch (final ArrayIndexOutOfBoundsException e) {
        }

        throw new NoSuchElementException();
      }

      {
        index = 0;
      }
    };
  }

  @Override
  public int[] getSupportedAttributes(final int field) {
    checkField(field);

    return PIMHandler.getInstance().getSupportedAttributes(type, field);
  }

  @Override
  public int getFieldDataType(final int field) {
    final int dataType = PIMHandler.getInstance().getFieldDataType(type, field);
    if (dataType == -1) {
      throw AbstractPIMItem.complaintAboutField(type, field);
    }
    else {
      return dataType;
    }
  }

  @Override
  public String getFieldLabel(final int field) {
    checkField(field);

    return PIMHandler.getInstance().getFieldLabel(type, field);
  }

  @Override
  public void renameCategory(final String currentCategory, final String newCategory) throws PIMException {
    checkWritePermission();
    checkOpen();
    if ((currentCategory == null) || (newCategory == null)) { throw new NullPointerException("Null category"); }
    String categories[] = PIMHandler.getInstance().getCategories(type, name);
    if (newCategory.equals(currentCategory)) { return; }
    int oldCategoryIndex = -1;
    int newCategoryIndex = -1;
    for (int i = 0; i < categories.length; i++) {
      if (currentCategory.equals(categories[i])) {
        oldCategoryIndex = i;

        continue;
      }
      if (newCategory.equals(categories[i])) {
        newCategoryIndex = i;
      }
    }
    if (oldCategoryIndex == -1) { throw new PIMException("No such category: " + currentCategory); }
    if (newCategoryIndex == -1) {
      categories[oldCategoryIndex] = newCategory;
    }
    else {
      final String a[] = new String[categories.length - 1];
      System.arraycopy(categories, 0, a, 0, oldCategoryIndex);
      System.arraycopy(categories, oldCategoryIndex + 1, a, oldCategoryIndex, a.length - oldCategoryIndex);
      categories = a;
    }
    PIMHandler.getInstance().setCategories(type, name, categories);
    final Enumeration e = items.elements();
    do {
      if (!e.hasMoreElements()) {
        break;
      }
      final AbstractPIMItem item = (AbstractPIMItem)e.nextElement();
      if (item.isInCategory(currentCategory)) {
        item.removeFromCategory(currentCategory);
        item.addToCategory(newCategory);
      }
    }
    while (true);
  }

  @Override
  public boolean isSupportedArrayElement(final int stringArrayField, final int arrayElement) {
    final int dataType = PIMHandler.getInstance().getFieldDataType(type, stringArrayField);
    return (dataType == 5) && PIMHandler.getInstance().isSupportedArrayElement(type, stringArrayField, arrayElement);
  }

  @Override
  public int[] getSupportedFields() {
    return PIMHandler.getInstance().getSupportedFields(type);
  }

  @Override
  public Enumeration items(String matchingValue) throws PIMException {
    checkReadPermission();
    checkOpen();
    matchingValue = matchingValue.toUpperCase();
    final Vector v = new Vector();
    final Enumeration e = items.elements();
    while (e.hasMoreElements()) {
      final AbstractPIMItem item = (AbstractPIMItem)e.nextElement();
      final int fields[] = item.getFields();
      for (int i = 0;; i++) {
        if (i >= fields.length) {
          break;
        }
        switch (getFieldDataType(fields[i])) {
          default:
            break;
          case 4: // '\004'
          {
            for (int j = item.countValues(fields[i]) - 1;; j--) {
              if (j < 0) {
                break;
              }
              final String value = item.getString(fields[i], j);
              if ((value != null) && (value.toUpperCase().indexOf(matchingValue) != -1)) {
                v.addElement(item);
              }
            }

            break;
          }
          case 5: // '\005'
          {
            for (int j = item.countValues(fields[i]) - 1; j >= 0; j--) {
              final String a[] = item.getStringArray(fields[i], j);
              if (a == null) {
                continue;
              }
              for (int k = 0;; k++) {
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
    }
    else {
      return;
    }
  }

  protected void checkWritePermission() throws SecurityException {
    if (mode == 1) {
      throw new SecurityException("List cannot be written");
    }
    else {
      return;
    }
  }

  protected void checkOpen() throws PIMException {
    if (!open) {
      throw new PIMException("List is closed.");
    }
    else {
      return;
    }
  }

  protected void checkNullCategory(final String category) {
    if (category == null) {
      throw new NullPointerException("Null category");
    }
    else {
      return;
    }
  }

  public void addItem(final AbstractPIMItem item) {
    items.addElement(item);
    item.setPIMList(this);
  }

  void removeItem(final PIMItem item) throws PIMException {
    checkWritePermission();
    checkOpen();
    if (item == null) { throw new NullPointerException("Null item"); }
    if (!items.removeElement(item)) {
      throw new PIMException("Item not in list");
    }
    else {
      ((AbstractPIMItem)item).remove();

      return;
    }
  }

  Object commit(final Object key, final byte data[]) {
    return PIMHandler.getInstance().commitListElement(type, name, key, data);
  }

  int getType() {
    return type;
  }

  private void checkField(final int field) {
    if (PIMHandler.getInstance().getFieldDataType(type, field) == -1) {
      throw AbstractPIMItem.complaintAboutField(type, field);
    }
    else {
      return;
    }
  }

  private void checkAttribute(int attribute) {
    if (attribute == 0) { return; }
    for (; ((attribute & 1) == 0) && (attribute != 0); attribute >>= 1) {
    }
    if (attribute != 1) {
      throw new IllegalArgumentException("Invalid attribute: " + attribute);
    }
    else {
      return;
    }
  }
}
