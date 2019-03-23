package javax.microedition.pim;

// ~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Referenced classes of package javax.microedition.pim:
// PIMField, EmptyPIMField, ScalarPIMField, VectorPIMField,
// AbstractPIMList, PIMHandler, PIMFormat, ContactImpl,
// EventImpl, ToDoImpl
public abstract class AbstractPIMItem implements PIMItem {

  private String categories[] = null;
  private int fieldKeys[] = new int[0];
  private PIMField fieldValues[] = new PIMField[0];
  private Object key = null;
  private boolean modified = true;
  private boolean removed = false;
  private AbstractPIMList pimList;
  private final int type;

  protected AbstractPIMItem(final AbstractPIMList pimList, final int type) {
    this.pimList = pimList;
    this.type = type;
  }

  protected AbstractPIMItem(final AbstractPIMList pimList, final PIMItem baseItem) {
    this(pimList, pimList.getType());
    final int fields[] = baseItem.getFields();
    for (final int field : fields) {
      if (!pimList.isSupportedField(field)) {
        continue;
      }
      final int dataType = pimList.getFieldDataType(field);
      final int indices = baseItem.countValues(field);
      for (int index = 0; index < indices; index++) {
        final int attributes = baseItem.getAttributes(field, index);
        Object value = null;
        switch (dataType) {
          case 0: // '\0'
            value = baseItem.getBinary(field, index);

            break;
          case 1: // '\001'
            value = baseItem.getBoolean(field, index);

            break;
          case 2: // '\002'
            value = baseItem.getDate(field, index);

            break;
          case 3: // '\003'
            value = baseItem.getInt(field, index);

            break;
          case 4: // '\004'
            value = baseItem.getString(field, index);

            break;
          case 5: // '\005'
            value = baseItem.getStringArray(field, index);

            break;
        }
        try {
          addValue(field, attributes, value, true);
        }
        catch (final FieldFullException ffe) {
        }
        catch (final IllegalArgumentException iae) {
        }
      }
    }
    updateRevision();
  }

  PIMField getField(final int field, final boolean create, final boolean check) {
    PIMField f = getField(field);
    if (f == null) {
      final PIMHandler handler = PIMHandler.getInstance();
      if (check && !handler.isSupportedField(type, field)) { throw AbstractPIMItem.complaintAboutField(type, field); }
      if (create) {
        handler.getFieldDataType(type, field);
        f = new EmptyPIMField();
        putField(field, f);
      }
    }

    return f;
  }

  private void setValue(final int field, final int index, int attributes, final Object value, final boolean force) {
    try {
      checkType(field, value);
      final PIMField pimField = getField(field, false, true);
      if (pimField == null) { throw new IndexOutOfBoundsException("Empty field: " + field); }
      final int currentValues = pimField.getValueCount();
      if ((index < 0) || (index >= currentValues)) { throw new IndexOutOfBoundsException("0 <= index < " + currentValues + ", " + index + " not in range"); }
      if (!force) {
        checkReadOnlyFields(field);
      }
      attributes = filterAttributes(field, attributes);
      pimField.setValue(attributes, value, index);
      modified = true;
    }
    catch (final ClassCastException e) {
      throw new IllegalArgumentException("Wrong type for field");
    }
  }

  private void addValue(final int field, int attributes, final Object value, final boolean force) {
    checkType(field, value);
    PIMField pimField = getField(field, true, true);
    final int maxValues = PIMHandler.getInstance().getMaximumValues(type, field);
    final int currentValues = pimField.getValueCount();
    if ((maxValues != -1) && (currentValues >= maxValues)) { throw new FieldFullException("Can only store " + maxValues + " in field", field); }
    if (!force) {
      checkReadOnlyFields(field);
    }
    if (pimField.isScalar()) {
      if (currentValues == 0) {
        pimField = new ScalarPIMField();
        putField(field, pimField);
      }
      else {
        final Object value0 = pimField.getValue(0);
        final int attributes0 = pimField.getAttributes(0);
        pimField = new VectorPIMField();
        pimField.addValue(attributes0, value0);
        putField(field, pimField);
      }
    }
    attributes = filterAttributes(field, attributes);
    pimField.addValue(attributes, value);
    modified = true;
  }

  private int filterAttributes(final int field, final int attributes) {
    if (attributes == 0) {
      return 0;
    }
    else {
      return attributes & PIMHandler.getInstance().getSupportedAttributesMask(type, field);
    }
  }

  private Object getValue(final int field, final int index) {
    final PIMField pimField = getField(field, false, true);
    if (pimField == null) { throw new IndexOutOfBoundsException("Empty field: " + field); }
    final int currentValues = pimField.getValueCount();
    if ((index < 0) || (index >= currentValues)) {
      throw new IndexOutOfBoundsException("0 <= index < " + currentValues + ", " + index + " not in range");
    }
    else {
      return pimField.getValue(index);
    }
  }

  @Override
  public void addStringArray(final int field, final int attributes, final String value[]) {
    checkType(field, 5);
    validateStringArray(field, value);
    addValue(field, attributes, value, false);
  }

  @Override
  public void addBoolean(final int field, final int attributes, final boolean value) {
    addValue(field, attributes, value, false);
  }

  @Override
  public void removeFromCategory(final String category) {
    if (category == null) { throw new NullPointerException("Null category"); }
    if (categories != null) {
      for (int i = 0; i < categories.length; i++) {
        if (category.equals(categories[i])) {
          if (categories.length == 1) {
            categories = null;
          }
          else {
            final String a[] = new String[categories.length - 1];
            System.arraycopy(categories, 0, a, 0, i);
            System.arraycopy(categories, i + 1, a, i, a.length - i);
            categories = a;
          }
          modified = true;

          return;
        }
      }
    }
  }

  @Override
  public int[] getFields() {
    int emptyFields = 0;
    for (final PIMField fieldValue : fieldValues) {
      if (fieldValue.getValueCount() == 0) {
        emptyFields++;
      }
    }
    final int keys[] = new int[fieldKeys.length - emptyFields];
    int i = 0;
    int j = 0;
    for (; i < keys.length; i++) {
      if ((emptyFields == 0) || (fieldValues[i].getValueCount() != 0)) {
        keys[j++] = fieldKeys[i];
      }
      else {
        emptyFields--;
      }
    }

    return keys;
  }

  @Override
  public boolean getBoolean(final int field, final int index) {
    checkType(field, 1);

    return ((Boolean)getValue(field, index)).booleanValue();
  }

  @Override
  public void addDate(final int field, final int attributes, final long value) {
    addValue(field, attributes, value, false);
  }

  @Override
  public int maxCategories() {
    return -1;
  }

  @Override
  public void setDate(final int field, final int index, final int attributes, final long value) {
    setValue(field, index, attributes, value, false);
  }

  @Override
  public int getInt(final int field, final int index) {
    checkType(field, 3);
    String message;
    try {
      return ((Integer)getValue(field, index)).intValue();
    }
    catch (final ClassCastException e) {
      message = "Cannot convert to integer on field " + field + ": " + getValue(field, index).getClass();
    }

    throw new ClassCastException(message);
  }

  @Override
  public void setBinary(final int field, final int index, final int attributes, final byte value[], final int offset, int length) {
    validateBinaryValue(value, offset, length);
    length = Math.min(length, value.length - offset);
    final byte b[] = new byte[length];
    System.arraycopy(value, offset, b, 0, length);
    setValue(field, index, attributes, b, false);
  }

  @Override
  public int getAttributes(final int field, final int index) {
    return getField(field, true, true).getAttributes(index);
  }

  @Override
  public int countValues(final int field) {
    final PIMField pimField = getField(field, false, true);

    return (pimField != null) ? pimField.getValueCount()
        : 0;
  }

  @Override
  public void addString(final int field, final int attributes, final String value) {
    validateString(value);
    addValue(field, attributes, value, false);
  }

  @Override
  public String[] getCategories() {
    if (categories == null) {
      return new String[0];
    }
    else {
      final String cs[] = new String[categories.length];
      System.arraycopy(categories, 0, cs, 0, categories.length);

      return cs;
    }
  }

  String[] getCategoriesRaw() {
    return categories;
  }

  @Override
  public void setInt(final int field, final int index, final int attributes, final int value) {
    setValue(field, index, attributes, value, false);
  }

  @Override
  public void setStringArray(final int field, final int index, final int attributes, final String value[]) {
    checkType(field, 5);
    validateStringArray(field, value);
    setValue(field, index, attributes, value, false);
  }

  private void validateStringArray(final int field, final String a[]) {
    final int requiredLength = PIMHandler.getInstance().getStringArraySize(type, field);
    if (a.length != requiredLength) { throw new IllegalArgumentException("String array length incorrect: should be " + requiredLength); }
    for (final String anA : a) {
      if (anA != null) { return; }
    }

    throw new IllegalArgumentException("No non-null elements in array");
  }

  private void validateString(final String value) {
    if (value == null) {
      throw new NullPointerException("String field value should not be null");
    }
    else {
      return;
    }
  }

  @Override
  public long getDate(final int field, final int index) {
    checkType(field, 2);
    try {
      return ((Long)getValue(field, index)).longValue();
    }
    catch (final ClassCastException e) {
    }

    throw new ClassCastException();
  }

  @Override
  public void addToCategory(final String category) throws PIMException {
    if (category == null) { throw new NullPointerException("Null category"); }
    if (categories == null) {
      categories = (new String[] {
          category
      });
      modified = true;
    }
    else {
      for (final String category1 : categories) {
        if (category1.equals(category)) { return; }
      }
      final String a[] = new String[categories.length + 1];
      System.arraycopy(categories, 0, a, 0, categories.length);
      a[categories.length] = category;
      categories = a;
      modified = true;
    }
  }

  @Override
  public void addInt(final int field, final int attributes, final int value) {
    addValue(field, attributes, value, false);
  }

  @Override
  public byte[] getBinary(final int field, final int index) {
    checkType(field, 0);

    return (byte[])getValue(field, index);
  }

  @Override
  public void addBinary(final int field, final int attributes, final byte value[], final int offset, int length) {
    validateBinaryValue(value, offset, length);
    length = Math.min(length, value.length - offset);
    final byte b[] = new byte[length];
    System.arraycopy(value, offset, b, 0, length);
    addValue(field, attributes, b, false);
  }

  private void validateBinaryValue(final byte value[], final int offset, final int length) {
    if (value == null) { throw new NullPointerException("Binary field value should not be null"); }
    if (offset < 0) { throw new IllegalArgumentException("Negative offset"); }
    if (offset >= value.length) { throw new IllegalArgumentException("Offset out of range"); }
    if (length <= 0) { throw new IllegalArgumentException("Length must be at least 1"); }
    if (value.length == 0) {
      throw new IllegalArgumentException("Binary array value has zero length");
    }
    else {
      return;
    }
  }

  @Override
  public String[] getStringArray(final int field, final int index) {
    checkType(field, 5);

    return (String[])getValue(field, index);
  }

  @Override
  public void setBoolean(final int field, final int index, final int attributes, final boolean value) {
    setValue(field, index, attributes, value, false);
  }

  @Override
  public PIMList getPIMList() {
    return pimList;
  }

  void setPIMList(final AbstractPIMList list) {
    pimList = list;
  }

  @Override
  public void removeValue(final int field, final int index) {
    PIMField pimField = getField(field, false, true);
    if (pimField == null) { throw new IndexOutOfBoundsException("Empty field: " + field); }
    int currentValues = pimField.getValueCount();
    if ((index < 0) || (index >= currentValues)) { throw new IndexOutOfBoundsException("0 <= index < " + currentValues + ", " + index + " not in range"); }
    checkReadOnlyFields(field);
    pimField.removeValue(index);
    if (--currentValues == 0) {
      removeField(field);
    }
    else if (currentValues == 1) {
      final Object value = pimField.getValue(0);
      final int attributes = pimField.getAttributes(0);
      pimField = new ScalarPIMField();
      pimField.addValue(attributes, value);
      putField(field, pimField);
    }
    modified = true;
  }

  @Override
  public String getString(final int field, final int index) {
    checkType(field, 4);

    return (String)getValue(field, index);
  }

  @Override
  public void setString(final int field, final int index, final int attributes, final String value) {
    validateString(value);
    setValue(field, index, attributes, value, false);
  }

  @Override
  public boolean isModified() {
    return modified;
  }

  void setModified(final boolean modified) {
    this.modified = modified;
  }

  @Override
  public void commit() throws PIMException {
    if (pimList == null) { throw new PIMException("Item is not in a list"); }
    pimList.checkWritePermission();
    pimList.checkOpen();
    updateRevision();
    // setDefaultValues();
    try {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      final PIMFormat format = getEncodingFormat();
      format.encode(baos, "UTF-8", this);
      final Object newKey = pimList.commit(key, baos.toByteArray());
      if (key == null) {
        pimList.addItem(this);
      }
      setKey(newKey);
      updateUID();
      modified = false;
    }
    catch (final IOException e) {
      throw new PIMException("Error persisting PIMItem");
    }
  }

  abstract PIMFormat getEncodingFormat();

  boolean isInCategory(final String category) {
    if (categories == null) { return false; }
    for (final String category1 : categories) {
      if (category1.equals(category)) { return true; }
    }

    return false;
  }

  void setKey(final Object key) {
    this.key = key;
    if (key != null) {
      updateUID();
    }
  }

  Object getKey() {
    return key;
  }

  void remove() throws PIMException {
    if (pimList == null) {
      throw new PIMException("Item is not in a list");
    }
    else {
      pimList.checkWritePermission();
      removed = true;
      pimList.commit(key, null);
      setKey(null);
      pimList = null;

      return;
    }
  }

  protected void setDefaultValues() {
    final int supportedFields[] = pimList.getSupportedFields();
    final PIMHandler handler = PIMHandler.getInstance();
    for (final int field : supportedFields) {
      // sometimes field will null;
      final PIMField pimField = getField(field, false, true);
      if (((pimField != null) && (pimField.getValueCount() != 0)) || !handler.hasDefaultValue(type, field)) {
        continue;
      }
      Object value = null;
      switch (pimList.getFieldDataType(field)) {
        default:
          break;
        case 1: // '\001'
          value = handler.getDefaultBooleanValue(type, field);

          break;
        case 0: // '\0'
          value = handler.getDefaultBinaryValue(type, field);

          break;
        case 2: // '\002'
          value = handler.getDefaultDateValue(type, field);

          break;
        case 3: // '\003'
          value = handler.getDefaultIntValue(type, field);

          break;
        case 4: // '\004'
          value = handler.getDefaultStringValue(type, field);

          break;
        case 5: // '\005'
          value = handler.getDefaultStringArrayValue(type, field);

          break;
      }
      addValue(field, 0, value, false);
    }
  }

  static boolean isValidPIMField(final int type, final int field) {
    switch (type) {
      case 1: // '\001'
        return ContactImpl.isValidPIMField(field);
      case 2: // '\002'
        return EventImpl.isValidPIMField(field);
      case 3: // '\003'
        return ToDoImpl.isValidPIMField(field);
    }

    return false;
  }

  private void checkType(final int field, final Object value) {
    try {
      final int dataType = PIMHandler.getInstance().getFieldDataType(type, field);
      switch (dataType) {
        case 0: // '\0'
          break;
        case 1: // '\001'
          break;
        case 2: // '\002'
          break;
        case 3: // '\003'
          break;
        case 4: // '\004'
          break;
        case 5: // '\005'
          break;
        default:
          throw AbstractPIMItem.complaintAboutField(type, field);
      }
    }
    catch (final ClassCastException cce) {
      throw new IllegalArgumentException(cce.getMessage());
    }
  }

  private void checkType(final int field, final int dataType) {
    final int correctDataType = PIMHandler.getInstance().getFieldDataType(type, field);
    if ((dataType != correctDataType) && (correctDataType != -1)) { throw new IllegalArgumentException("Wrong data type"); }
    if (correctDataType == -1) {
      throw AbstractPIMItem.complaintAboutField(type, field);
    }
    else {
      return;
    }
  }

  static RuntimeException complaintAboutField(final int type, final int field) {
    if (AbstractPIMItem.isValidPIMField(type, field)) {
      return new UnsupportedFieldException(String.valueOf(field));
    }
    else {
      return new IllegalArgumentException("Invalid field " + field);
    }
  }

  private int findFieldKey(final int key) {
    int lowerBound = 0;
    for (int upperBound = fieldKeys.length; lowerBound != upperBound;) {
      final int index = lowerBound + ((upperBound - lowerBound) / 2);
      final int indexKey = fieldKeys[index];
      if (indexKey > key) {
        if (index == upperBound) {
          upperBound--;
        }
        else {
          upperBound = index;
        }
      }
      else {
        if (indexKey == key) { return index; }
        if (index == lowerBound) {
          lowerBound++;
        }
        else {
          lowerBound = index;
        }
      }
    }

    return ~lowerBound;
  }

  public void putField(final int key, final PIMField field) {
    int index = findFieldKey(key);
    if (index >= 0) {
      fieldValues[index] = field;
    }
    else {
      index = ~index;
      final int newKeys[] = new int[fieldKeys.length + 1];
      final PIMField newFields[] = new PIMField[fieldValues.length + 1];
      System.arraycopy(fieldKeys, 0, newKeys, 0, index);
      System.arraycopy(fieldValues, 0, newFields, 0, index);
      newKeys[index] = key;
      newFields[index] = field;
      System.arraycopy(fieldKeys, index, newKeys, index + 1, fieldKeys.length - index);
      System.arraycopy(fieldValues, index, newFields, index + 1, fieldKeys.length - index);
      fieldKeys = newKeys;
      fieldValues = newFields;
    }
  }

  public PIMField getField(final int key) {
    final int index = findFieldKey(key);
    if (index >= 0) {
      return fieldValues[index];
    }
    else {
      return null;
    }
  }

  public void removeField(final int key) {
    final int index = findFieldKey(key);
    if (index >= 0) {
      final int newKeys[] = new int[fieldKeys.length - 1];
      final PIMField newFields[] = new PIMField[fieldValues.length - 1];
      System.arraycopy(fieldKeys, 0, newKeys, 0, index);
      System.arraycopy(fieldValues, 0, newFields, 0, index);
      System.arraycopy(fieldKeys, index + 1, newKeys, index, newKeys.length - index);
      System.arraycopy(fieldValues, index + 1, newFields, index, newKeys.length - index);
      fieldKeys = newKeys;
      fieldValues = newFields;
    }
  }

  private void checkReadOnlyFields(final int field) {
    if (key != null) {
      if (field == getRevisionField()) { throw new IllegalArgumentException("REVISION field is read only except on newly created PIMItems"); }
      if (field == getUIDField()) { throw new IllegalArgumentException("UID field is read only except on newly created PIMItems"); }
    }
  }

  private void updateRevision() {
    final Long value = System.currentTimeMillis();
    final int field = getRevisionField();
    if (countValues(field) == 0) {
      addValue(field, 0, value, true);
    }
    else {
      setValue(field, 0, 0, value, true);
    }
  }

  private void updateUID() {
    final String value = key.toString();
    final int field = getUIDField();
    if (countValues(field) == 0) {
      addValue(field, 0, value, true);
    }
    else {
      setValue(field, 0, 0, value, true);
    }
  }

  protected abstract int getRevisionField();

  protected abstract int getUIDField();

  protected String formatData() {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < fieldValues.length; i++) {
      if (fieldValues[i].getValueCount() == 0) {
        continue;
      }
      final PIMField pimField = fieldValues[i];
      final int field = fieldKeys[i];
      final int valueCount = pimField.getValueCount();
      if (valueCount == 0) {
        continue;
      }
      if (i != 0) {
        sb.append(", ");
      }
      final String label = PIMHandler.getInstance().getFieldLabel(type, field);
      final int dataType = PIMHandler.getInstance().getFieldDataType(type, field);
      for (int j = 0; j < valueCount; j++) {
        sb.append(label);
        if (valueCount != 1) {
          sb.append("[");
          sb.append(j);
          sb.append("]");
        }
        sb.append("=");
        final Object value = pimField.getValue(j);
        if (value == null) {
          sb.append("null");

          continue;
        }
        switch (dataType) {
          case 5: // '\005'
            final String aValue[] = (String[])value;
            sb.append("[");
            for (int k = 0; k < aValue.length; k++) {
              if (k != 0) {
                sb.append(",");
              }
              sb.append(aValue[k]);
            }
            sb.append("]");

            break;
          case 0: // '\0'
            final byte bValue[] = (byte[])value;
            sb.append("<");
            sb.append(bValue.length);
            sb.append(" bytes>");

            break;
          case 2: // '\002'
            final long dValue = ((Long)value).longValue();
            sb.append(PIMHandler.getInstance().composeDateTime(dValue));

            break;
          default:
            sb.append(value);

            break;
        }
      }
    }
    if ((categories != null) && (categories.length != 0)) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append("Categories=[");
      for (int i = 0; i < categories.length; i++) {
        if (i > 0) {
          sb.append(",");
        }
        sb.append(categories[i]);
      }
      sb.append("]");
    }

    return sb.toString();
  }

  protected abstract String toDisplayableString();

  @Override
  public String toString() {
    return "true".equals(System.getProperty("pim.debug")) ? toDisplayableString()
        : super.toString();
  }
}
