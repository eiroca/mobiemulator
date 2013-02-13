package javax.microedition.pim.formats;

public class VToDoSupport {
    public VToDoSupport() {}

    public static String getFieldLabel(int field) {
        switch (field) {
        case 101 :    // 'e'
            return "STATUS";
        case 102 :    // 'f'
            return "COMPLETED";
        case 105 :    // 'i'
            return "PRIORITY";
        case 103 :    // 'g'
            return "DUE";
        case 104 :    // 'h'
            return "DESCRIPTION";
        case 106 :    // 'j'
            return "LAST-MODIFIED";
        case 107 :    // 'k'
            return "SUMMARY";
        case 108 :    // 'l'
            return "UID";
        case 100 :    // 'd'
            return "CLASS";
        }

        return null;
    }
    public static int getFieldCode(String fieldName) {
        if (fieldName.equals("DESCRIPTION")) {
            return 104;
        }
        if (fieldName.equals("LAST-MODIFIED")) {
            return 106;
        }
        if (fieldName.equals("SUMMARY")) {
            return 107;
        }
        if (fieldName.equals("UID")) {
            return 108;
        }
        if (fieldName.equals("PRIORITY")) {
            return 105;
        }
        if (fieldName.equals("DESCRIPTION")) {
            return 104;
        }
        if (fieldName.equals("COMPLETED")) {
            return 102;
        }
        if (fieldName.equals("STATUS")) {
            return 101;
        }
        if (fieldName.equals("DUE")) {
            return 103;
        }

        return !fieldName.equals("CLASS") ? -1
                                          : 100;
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
