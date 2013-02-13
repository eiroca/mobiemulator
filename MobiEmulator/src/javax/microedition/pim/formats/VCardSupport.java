package javax.microedition.pim.formats;

//Referenced classes of package javax.microedition.pim.formats:
//           Extensions
public class VCardSupport {
    public VCardSupport() {}

    public static String getFieldLabel(int field) {
        switch (field) {
        case 105 :    // 'i'
            return "FN";
        case 100 :    // 'd'
            return "ADR";
        case 101 :    // 'e'
            return "BDAY";
        case 106 :    // 'j'
            return "N";
        case 110 :    // 'n'
            return "PHOTO;ENCODING=BASE64";
        case 111 :    // 'o'
            return "PHOTO;VALUE=URL";
        case 115 :    // 's'
            return "TEL";
        case 116 :    // 't'
            return "TITLE";
        case 114 :    // 'r'
            return "REV";
        case 118 :    // 'v'
            return "URL";
        case 117 :    // 'u'
            return "UID";
        case 112 :    // 'p'
            return "KEY;ENCODING=BASE64";
        case 104 :    // 'h'
            return "LABEL";
        case 107 :    // 'k'
            return "NICKNAME";
        case 108 :    // 'l'
            return "NOTE";
        case 113 :    // 'q'
            return "KEY";
        case 103 :    // 'g'
            return "EMAIL";
        case 109 :    // 'm'
            return "ORG";
        case 102 :    // 'f'
        }

        return null;
    }
    public static int getFieldCode(String fieldName) {
        if (fieldName.equals("FN")) {
            return 105;
        }
        if (fieldName.equals("LABEL")) {
            return 104;
        }
        if (fieldName.equals("ADR")) {
            return 100;
        }
        if (fieldName.equals("BDAY")) {
            return 101;
        }
        if (fieldName.equals("N")) {
            return 106;
        }
        if (fieldName.equals("PHOTO")) {
            return 110;
        }
        if (fieldName.equals("TEL")) {
            return 115;
        }
        if (fieldName.equals("TITLE")) {
            return 116;
        }
        if (fieldName.equals("REV")) {
            return 114;
        }
        if (fieldName.equals("URL")) {
            return 118;
        }
        if (fieldName.equals("UID")) {
            return 117;
        }
        if (fieldName.equals("KEY")) {
            return 112;
        }
        if (fieldName.equals("NICKNAME")) {
            return 107;
        }
        if (fieldName.equals("NOTE")) {
            return 108;
        }
        if (fieldName.equals("EMAIL")) {
            return 103;
        }

        return !fieldName.equals("ORG") ? -1
                                        : 109;
    }
    public static String getAttributeLabel(int attr) {
        switch (attr) {
        case 1 :     // '\001'
            return "X-J2MEWTK-ASST";
        case 2 :     // '\002'
            return "CAR";
        case 4 :     // '\004'
            return "FAX";
        case 8 :     // '\b'
            return "HOME";
        case 16 :    // '\020'
            return "CELL";
        case 32 :    // ' '
            return "X-J2MEWTK-OTHER";
        case 64 :    // '@'
            return "PAGER";
        case 128 :
            return "PREF";
        case 256 :
            return "MSG";
        case 512 :
            return "WORK";
        }

        return Extensions.getContactAttributeLabel(attr);
    }
    public static int getAttributeCode(String label, int defaultValue) {
        if (label.equals("CAR")) {
            return 2;
        }
        if (label.equals("FAX")) {
            return 4;
        }
        if (label.equals("HOME")) {
            return 8;
        }
        if (label.equals("CELL")) {
            return 16;
        }
        if (label.equals("X-J2MEWTK-OTHER")) {
            return 32;
        }
        if (label.equals("PAGER")) {
            return 64;
        }
        if (label.equals("PREF")) {
            return 128;
        }
        if (label.equals("MSG")) {
            return 256;
        }
        if (label.equals("WORK")) {
            return 512;
        }
        if (label.equals("X-J2MEWTK-ASST")) {
            return 1;
        } else {
            return Extensions.getContactAttributeCode(label, defaultValue);
        }
    }
    public static String getClassType(int fieldValue) {
        switch (fieldValue) {
        case 200 :
            return "CONFIDENTIAL";
        case 201 :
            return "PRIVATE";
        case 202 :
            return "PUBLIC";
        }

        return null;
    }
    public static int getClassCode(String s) {
        switch (s.length()) {
        default :
            break;
        case 6 :     // '\006'
            if (s.equals("PUBLIC")) {
                return 202;
            }

            break;
        case 7 :     // '\007'
            if (s.equals("PRIVATE")) {
                return 201;
            }

            break;
        case 12 :    // '\f'
            if (s.equals("CONFIDENTIAL")) {
                return 200;
            }

            break;
        }

        return -1;
    }
}
