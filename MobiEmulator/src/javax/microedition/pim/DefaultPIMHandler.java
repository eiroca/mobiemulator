package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.pim.formats.FormatSupport;
import javax.microedition.pim.formats.VCardSupport;
import javax.microedition.pim.formats.VEventSupport;
import javax.microedition.pim.formats.VToDoSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

//import com.sun.midp.main.Configuration;

//Referenced classes of package javax.microedition.pim:
//           PIMHandler
public class DefaultPIMHandler extends PIMHandler {
    private int getListElementBufferLength         = 256;
    private int getSupportedAttributesBufferLength = 16;
    private int getSupportedFieldsBufferLength     = 16;
    // Set the pim path
    String      pimPath                            = "./pim";
    String[]    fileNames;
    public DefaultPIMHandler() {}

    public byte[] getDefaultBinaryValue(int listType, int field) {
        return null;
    }
    public long getDefaultDateValue(int listType, int field) {
        return 0L;
    }
    public int getDefaultIntValue(int i, int j) {
        return 0;
    }
    public String[] getDefaultStringArrayValue(int listType, int field) {
        int length = getStringArraySize(listType, field);

        return new String[length];
    }
    public String getDefaultStringValue(int listType, int field) {
        return null;
    }
    public boolean getDefaultBooleanValue(int listType, int field) {
        return false;
    }
    public int getFieldDataType(int i, int j) {
        String str = getFieldLabel(i, j);
        switch (i) {
        case 1 :
            if (str.equals("FN")) {
                return 4;
            }
            if (str.equals("LABEL")) {
                return 5;
            }
            if (str.equals("ADR")) {
                return 5;
            }
            if (str.equals("BDAY")) {
                return 2;
            }
            if (str.equals("N")) {
                return 5;
            }
            if (str.equals("PHOTO")) {
                return 0;
            }
            if (str.equals("TEL")) {
                return 4;
            }
            if (str.equals("TITLE")) {
                return 4;
            }
            if (str.equals("REV")) {
                return 2;
            }
            if (str.equals("URL")) {
                return 4;
            }
            if (str.equals("UID")) {
                return 4;
            }
            if (str.equals("KEY")) {
                return 4;
            }
            if (str.equals("NICKNAME")) {
                return 4;
            }
            if (str.equals("NOTE")) {
                return 4;
            }
            if (str.equals("EMAIL")) {
                return 4;
            }

            return !str.equals("ORG") ? 5
                                      : 4;
        case 2 :
            break;
        case 3 :
        }

        return 4;
    }
    public boolean hasDefaultValue(int i, int j) {
        return true;
    }
    public boolean isSupportedField(int i, int j) {
        return true;
    }
    public int[] getSupportedAttributes(int listType, int field) {
        int buffer[]  = new int[getSupportedAttributesBufferLength];
        int itemsRead = getSupportedAttributesImpl(listType, field, buffer);
        if (itemsRead < 0) {
            getSupportedAttributesBufferLength = -itemsRead;
            buffer                             = new int[getSupportedAttributesBufferLength];
            itemsRead                          = getSupportedAttributesImpl(listType, field, buffer);
        }
        int result[] = new int[itemsRead];
        System.arraycopy(buffer, 0, result, 0, itemsRead);

        return result;
    }
    public int getSupportedAttributesMask(int i, int j) {
        return -1;
    }
    public int getSupportedAttributesImpl(int i, int j, int ai[]) {
        return -1;
    }
    public boolean isSupportedAttribute(int i, int j, int k) {
        return true;
    }
    public native String getAttributeLabel(int i, int j);
    public String getFieldLabel(int listType, int field) {
        return getFieldLabelImpl(listType, field, System.getProperty("microedition.locale"));
    }
    public String getFieldLabelImpl(int i, int j, String s) {
        String str = null;
        switch (i) {
        case 1 :
            str = VCardSupport.getFieldLabel(j);

            break;
        case 2 :
            str = VEventSupport.getFieldLabel(j);

            break;
        case 3 :
            str = VToDoSupport.getFieldLabel(j);
        }

        return str;
    }
    public int[] getSupportedFields(int listType) {
        int buffer[]  = new int[getSupportedFieldsBufferLength];
        int itemsRead = getSupportedFieldsImpl(listType, buffer);
        if (itemsRead < 0) {
            getSupportedFieldsBufferLength = -itemsRead;
            buffer                         = new int[getSupportedFieldsBufferLength];
            itemsRead                      = getSupportedFieldsImpl(listType, buffer);
        }
        int result[] = new int[itemsRead];
        System.arraycopy(buffer, 0, result, 0, itemsRead);

        return result;
    }
    public int getSupportedFieldsImpl(int ival, int ai[]) {
        int i = 0;
        switch (ival) {
        case 1 :
            i = 19;

            break;
        case 2 :
            i = 9;

            break;
        case 3 :
            i = 8;
        }
        if (ai.length < i) {
            i = -i;
        } else {
            for (int j = 0;j < i;j++) {
                ai[j] = (100 + j);
            }
        }

        return i;
    }
    public int getStringArraySize(int i, int j) {
        return 256;
    }
    public int[] getSupportedArrayElements(int listType, int field) {
        int size     = getStringArraySize(listType, field);
        int result[] = new int[size];
        for (int i = 0;i < size;i++) {
            result[i] = i;
        }

        return result;
    }
    public String getArrayElementLabel(int listType, int field, int arrayElement) {
        return getArrayElementLabelImpl(listType, field, arrayElement, System.getProperty("microedition.locale"));
    }
    public native String getArrayElementLabelImpl(int i, int j, int k, String s);
    public boolean isSupportedArrayElement(int listType, int field, int arrayElement) {
        return (arrayElement >= 0) && (arrayElement < getStringArraySize(listType, field));
    }
    public String getDefaultListName(int listType) {
        return getDefaultListNameImpl(listType, System.getProperty("microedition.locale"));
    }
    public String getDefaultListNameImpl(int i, String s) {
        switch (i) {
        case 1 :
            return "CONTACTS";
        case 2 :
            return "EVENTS";
        case 3 :
            return "TODO";
        }

        return null;
    }
    public String[] getListNames(int listType) {
        int    length  = getListNamesLength(listType);
        String locale  = System.getProperty("microedition.locale");
        String names[] = new String[length];
        for (int i = 0;i < length;i++) {
            names[i] = getListName(listType, i, locale);
        }

        return names;
    }
    public int getListNamesLength(int i) {
        return 0;
    }
    public String getListName(int i, int j, String s) {
        return "";
    }
    public Object commitListElement(int listType, String listName, Object elementKey, byte elementData[]) {
        return commitListElementImpl(listType, listName, (String) elementKey, elementData);
    }
    public byte[] getListElement(int listType, String listName, Object elementKey) {
        byte buffer[]  = new byte[getListElementBufferLength];
        int  bytesRead = getListElementImpl(listType, listName, (String) elementKey, buffer);
        if (bytesRead < 0) {
            getListElementBufferLength = -bytesRead;
            buffer                     = new byte[getListElementBufferLength];
            bytesRead                  = getListElementImpl(listType, listName, (String) elementKey, buffer);
        }
        byte result[] = new byte[bytesRead];
        System.arraycopy(buffer, 0, result, 0, bytesRead);

        return result;
    }
    public Object[] getListKeys(int listType, String listName) {
        int    length = getListKeysLength(listType, listName);
        Object keys[] = new Object[length];
        for (int i = 0;i < length;i++) {
            keys[i] = getListKey(listType, listName, i);
        }

        return keys;
    }
    public int getListKeysLength(int i1, String str) {
        int i = 0;
        checkDirExists(str);
        File   localFile   = new File(reDirect(str));
        File[] arrayOfFile = localFile.listFiles();
        this.fileNames = new String[1024];
        for (File anArrayOfFile : arrayOfFile) {
            if (!anArrayOfFile.getName().endsWith(".vcf")) {
                continue;
            }
            this.fileNames[(i++)] = anArrayOfFile.getName();
        }

        return i;
    }
    private String reDirect(String path) {
        return new File("").getAbsolutePath() + "/pim/" + path;
    }
    private void checkDirExists(String path) {
        if (!new File(pimPath).exists()) {
            new File(pimPath).mkdir();
        }
        String checkpath = pimPath + "/" + path;
        if (!new File(checkpath).exists()) {
            new File(checkpath).mkdir();
        }
    }
    public String getListKey(int i, String s, int j) {
        return this.fileNames[j];
    }
    public String commitListElementImpl(int i, String s, String s1, byte abyte0[]) {
        // commit to file by creating file
        try {
            checkDirExists(s);
            StringBuffer sb       = new StringBuffer(new String(abyte0));
            String       UIDLine  = sb.substring(sb.indexOf("UID:") + 4, sb.indexOf(".vcf"));
            String       filename = UIDLine.replace(";", "");
            sb.replace(sb.indexOf("UID:"), sb.indexOf(".vcf") + 4, "");
            File             localFile = new File(reDirect(s) + "\\" + filename + ".vcf");
            FileOutputStream fo        = new FileOutputStream(localFile);
            fo.write(sb.toString().replace(";;;;", "").getBytes());
            fo.close();

            return ("" + (++i));
        } catch (Exception e) {}

        return null;
    }
    public int getListElementImpl(int i1, String s, String s1, byte abyte0[]) {
        int i = 0;
        try {
            checkDirExists(s);
            File localFile = new File(reDirect(s) + "\\" + s1);
            i = (int) localFile.length();
            if (i > abyte0.length) {
                i *= -1;
            } else {
                new FileInputStream(localFile).read(abyte0, 0, i);
            }
        } catch (FileNotFoundException localFileNotFoundException) {}
        catch (IOException localIOException) {}

        return i;
    }
    public long parseDate(String s) {
        return new Date().getTime();
    }
    public String composeDate(long l) {
        Calendar cal   = Calendar.getInstance();
        int      month = cal.get(Calendar.MONTH);
        int      year  = cal.get(Calendar.YEAR);
        int      day   = cal.get(Calendar.DAY_OF_MONTH);

        return ("" + day + month + year);
    }
    public long parseDateTime(String s) {
        return Date.parse(s);
    }
    public String composeDateTime(long l) {
        return new Date(l).toString();
    }
    public int getMaximumValues(int i, int j) {
        return 2147483647;
    }
    public String[] getCategories(int listType, String listName) {
        char cs[]   = new char[32];
        int  length = getCategoriesImpl(listType, listName, cs);
        if (length < 0) {
            cs     = new char[-length];
            length = getCategoriesImpl(listType, listName, cs);
        }
        String categories = String.valueOf(cs, 0, length);

        return FormatSupport.split(categories, '\n', 0);
    }
    private native int getCategoriesImpl(int i, String s, char ac[]);
    public void setCategories(int listType, String listName, String categories[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i < categories.length;i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(categories[i]);
        }
        setCategoriesImpl(listType, listName, sb.toString());
    }
    private static native void setCategoriesImpl(int i, String s, String s1);
}
