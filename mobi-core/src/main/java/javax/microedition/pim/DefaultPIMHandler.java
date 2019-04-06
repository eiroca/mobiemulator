package javax.microedition.pim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.pim.formats.FormatSupport;
import javax.microedition.pim.formats.VCardSupport;
import javax.microedition.pim.formats.VEventSupport;
import javax.microedition.pim.formats.VToDoSupport;

// import com.sun.midp.main.Configuration;

// Referenced classes of package javax.microedition.pim:
// PIMHandler
public class DefaultPIMHandler extends PIMHandler {

  private int getListElementBufferLength = 256;
  private int getSupportedAttributesBufferLength = 16;
  private int getSupportedFieldsBufferLength = 16;
  // Set the pim path
  String pimPath = "./pim";
  String[] fileNames;

  public DefaultPIMHandler() {
  }

  @Override
  public byte[] getDefaultBinaryValue(final int listType, final int field) {
    return null;
  }

  @Override
  public long getDefaultDateValue(final int listType, final int field) {
    return 0L;
  }

  @Override
  public int getDefaultIntValue(final int i, final int j) {
    return 0;
  }

  @Override
  public String[] getDefaultStringArrayValue(final int listType, final int field) {
    final int length = getStringArraySize(listType, field);

    return new String[length];
  }

  @Override
  public String getDefaultStringValue(final int listType, final int field) {
    return null;
  }

  @Override
  public boolean getDefaultBooleanValue(final int listType, final int field) {
    return false;
  }

  @Override
  public int getFieldDataType(final int i, final int j) {
    final String str = getFieldLabel(i, j);
    switch (i) {
      case 1:
        if (str.equals("FN")) { return 4; }
        if (str.equals("LABEL")) { return 5; }
        if (str.equals("ADR")) { return 5; }
        if (str.equals("BDAY")) { return 2; }
        if (str.equals("N")) { return 5; }
        if (str.equals("PHOTO")) { return 0; }
        if (str.equals("TEL")) { return 4; }
        if (str.equals("TITLE")) { return 4; }
        if (str.equals("REV")) { return 2; }
        if (str.equals("URL")) { return 4; }
        if (str.equals("UID")) { return 4; }
        if (str.equals("KEY")) { return 4; }
        if (str.equals("NICKNAME")) { return 4; }
        if (str.equals("NOTE")) { return 4; }
        if (str.equals("EMAIL")) { return 4; }

        return !str.equals("ORG") ? 5
            : 4;
      case 2:
        break;
      case 3:
    }

    return 4;
  }

  @Override
  public boolean hasDefaultValue(final int i, final int j) {
    return true;
  }

  @Override
  public boolean isSupportedField(final int i, final int j) {
    return true;
  }

  @Override
  public int[] getSupportedAttributes(final int listType, final int field) {
    int buffer[] = new int[getSupportedAttributesBufferLength];
    int itemsRead = getSupportedAttributesImpl(listType, field, buffer);
    if (itemsRead < 0) {
      getSupportedAttributesBufferLength = -itemsRead;
      buffer = new int[getSupportedAttributesBufferLength];
      itemsRead = getSupportedAttributesImpl(listType, field, buffer);
    }
    final int result[] = new int[itemsRead];
    System.arraycopy(buffer, 0, result, 0, itemsRead);

    return result;
  }

  @Override
  public int getSupportedAttributesMask(final int i, final int j) {
    return -1;
  }

  public int getSupportedAttributesImpl(final int i, final int j, final int ai[]) {
    return -1;
  }

  @Override
  public boolean isSupportedAttribute(final int i, final int j, final int k) {
    return true;
  }

  @Override
  public native String getAttributeLabel(int i, int j);

  @Override
  public String getFieldLabel(final int listType, final int field) {
    return getFieldLabelImpl(listType, field, System.getProperty("microedition.locale"));
  }

  public String getFieldLabelImpl(final int i, final int j, final String s) {
    String str = null;
    switch (i) {
      case 1:
        str = VCardSupport.getFieldLabel(j);

        break;
      case 2:
        str = VEventSupport.getFieldLabel(j);

        break;
      case 3:
        str = VToDoSupport.getFieldLabel(j);
    }

    return str;
  }

  @Override
  public int[] getSupportedFields(final int listType) {
    int buffer[] = new int[getSupportedFieldsBufferLength];
    int itemsRead = getSupportedFieldsImpl(listType, buffer);
    if (itemsRead < 0) {
      getSupportedFieldsBufferLength = -itemsRead;
      buffer = new int[getSupportedFieldsBufferLength];
      itemsRead = getSupportedFieldsImpl(listType, buffer);
    }
    final int result[] = new int[itemsRead];
    System.arraycopy(buffer, 0, result, 0, itemsRead);

    return result;
  }

  public int getSupportedFieldsImpl(final int ival, final int ai[]) {
    int i = 0;
    switch (ival) {
      case 1:
        i = 19;

        break;
      case 2:
        i = 9;

        break;
      case 3:
        i = 8;
    }
    if (ai.length < i) {
      i = -i;
    }
    else {
      for (int j = 0; j < i; j++) {
        ai[j] = (100 + j);
      }
    }

    return i;
  }

  @Override
  public int getStringArraySize(final int i, final int j) {
    return 256;
  }

  @Override
  public int[] getSupportedArrayElements(final int listType, final int field) {
    final int size = getStringArraySize(listType, field);
    final int result[] = new int[size];
    for (int i = 0; i < size; i++) {
      result[i] = i;
    }

    return result;
  }

  @Override
  public String getArrayElementLabel(final int listType, final int field, final int arrayElement) {
    return getArrayElementLabelImpl(listType, field, arrayElement, System.getProperty("microedition.locale"));
  }

  public native String getArrayElementLabelImpl(int i, int j, int k, String s);

  @Override
  public boolean isSupportedArrayElement(final int listType, final int field, final int arrayElement) {
    return (arrayElement >= 0) && (arrayElement < getStringArraySize(listType, field));
  }

  @Override
  public String getDefaultListName(final int listType) {
    return getDefaultListNameImpl(listType, System.getProperty("microedition.locale"));
  }

  public String getDefaultListNameImpl(final int i, final String s) {
    switch (i) {
      case 1:
        return "CONTACTS";
      case 2:
        return "EVENTS";
      case 3:
        return "TODO";
    }

    return null;
  }

  @Override
  public String[] getListNames(final int listType) {
    final int length = getListNamesLength(listType);
    final String locale = System.getProperty("microedition.locale");
    final String names[] = new String[length];
    for (int i = 0; i < length; i++) {
      names[i] = getListName(listType, i, locale);
    }

    return names;
  }

  public int getListNamesLength(final int i) {
    return 0;
  }

  public String getListName(final int i, final int j, final String s) {
    return "";
  }

  @Override
  public Object commitListElement(final int listType, final String listName, final Object elementKey, final byte elementData[]) {
    return commitListElementImpl(listType, listName, (String)elementKey, elementData);
  }

  @Override
  public byte[] getListElement(final int listType, final String listName, final Object elementKey) {
    byte buffer[] = new byte[getListElementBufferLength];
    int bytesRead = getListElementImpl(listType, listName, (String)elementKey, buffer);
    if (bytesRead < 0) {
      getListElementBufferLength = -bytesRead;
      buffer = new byte[getListElementBufferLength];
      bytesRead = getListElementImpl(listType, listName, (String)elementKey, buffer);
    }
    final byte result[] = new byte[bytesRead];
    System.arraycopy(buffer, 0, result, 0, bytesRead);

    return result;
  }

  @Override
  public Object[] getListKeys(final int listType, final String listName) {
    final int length = getListKeysLength(listType, listName);
    final Object keys[] = new Object[length];
    for (int i = 0; i < length; i++) {
      keys[i] = getListKey(listType, listName, i);
    }

    return keys;
  }

  public int getListKeysLength(final int i1, final String str) {
    int i = 0;
    checkDirExists(str);
    final File localFile = new File(reDirect(str));
    final File[] arrayOfFile = localFile.listFiles();
    fileNames = new String[1024];
    for (final File anArrayOfFile : arrayOfFile) {
      if (!anArrayOfFile.getName().endsWith(".vcf")) {
        continue;
      }
      fileNames[(i++)] = anArrayOfFile.getName();
    }

    return i;
  }

  private String reDirect(final String path) {
    return new File("").getAbsolutePath() + "/pim/" + path;
  }

  private void checkDirExists(final String path) {
    if (!new File(pimPath).exists()) {
      new File(pimPath).mkdir();
    }
    final String checkpath = pimPath + "/" + path;
    if (!new File(checkpath).exists()) {
      new File(checkpath).mkdir();
    }
  }

  public String getListKey(final int i, final String s, final int j) {
    return fileNames[j];
  }

  public String commitListElementImpl(int i, final String s, final String s1, final byte abyte0[]) {
    // commit to file by creating file
    try {
      checkDirExists(s);
      final StringBuffer sb = new StringBuffer(new String(abyte0));
      final String UIDLine = sb.substring(sb.indexOf("UID:") + 4, sb.indexOf(".vcf"));
      final String filename = UIDLine.replace(";", "");
      sb.replace(sb.indexOf("UID:"), sb.indexOf(".vcf") + 4, "");
      final File localFile = new File(reDirect(s) + "\\" + filename + ".vcf");
      final FileOutputStream fo = new FileOutputStream(localFile);
      fo.write(sb.toString().replace(";;;;", "").getBytes());
      fo.close();

      return ("" + (++i));
    }
    catch (final Exception e) {
    }

    return null;
  }

  public int getListElementImpl(final int i1, final String s, final String s1, final byte abyte0[]) {
    int i = 0;
    try {
      checkDirExists(s);
      final File localFile = new File(reDirect(s) + "\\" + s1);
      i = (int)localFile.length();
      if (i > abyte0.length) {
        i *= -1;
      }
      else {
        new FileInputStream(localFile).read(abyte0, 0, i);
      }
    }
    catch (final FileNotFoundException localFileNotFoundException) {
    }
    catch (final IOException localIOException) {
    }

    return i;
  }

  @Override
  public long parseDate(final String s) {
    return new Date().getTime();
  }

  @Override
  public String composeDate(final long l) {
    final Calendar cal = Calendar.getInstance();
    final int month = cal.get(Calendar.MONTH);
    final int year = cal.get(Calendar.YEAR);
    final int day = cal.get(Calendar.DAY_OF_MONTH);

    return ("" + day + month + year);
  }

  @Override
  public long parseDateTime(final String s) {
    return Date.parse(s);
  }

  @Override
  public String composeDateTime(final long l) {
    return new Date(l).toString();
  }

  @Override
  public int getMaximumValues(final int i, final int j) {
    return 2147483647;
  }

  @Override
  public String[] getCategories(final int listType, final String listName) {
    char cs[] = new char[32];
    int length = getCategoriesImpl(listType, listName, cs);
    if (length < 0) {
      cs = new char[-length];
      length = getCategoriesImpl(listType, listName, cs);
    }
    final String categories = String.valueOf(cs, 0, length);

    return FormatSupport.split(categories, '\n', 0);
  }

  private native int getCategoriesImpl(int i, String s, char ac[]);

  @Override
  public void setCategories(final int listType, final String listName, final String categories[]) {
    final StringBuffer sb = new StringBuffer();
    for (int i = 0; i < categories.length; i++) {
      if (i > 0) {
        sb.append("\n");
      }
      sb.append(categories[i]);
    }
    DefaultPIMHandler.setCategoriesImpl(listType, listName, sb.toString());
  }

  private static native void setCategoriesImpl(int i, String s, String s1);
}
