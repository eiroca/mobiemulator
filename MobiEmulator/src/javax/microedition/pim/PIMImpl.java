package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

//import com.sun.midp.midlet.MIDletSuite;
//import com.sun.midp.midlet.Scheduler;
//import com.sun.midp.security.Permissions;

import javax.microedition.pim.formats.VCalendar10Format;
import javax.microedition.pim.formats.VCard21Format;
import javax.microedition.pim.formats.VCard30Format;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public final class PIMImpl extends PIM {
    private static final PIMFormat[] formats = { new VCalendar10Format(), new VCard21Format(), new VCard30Format() };
    public PIMItem[] fromSerialFormat(InputStream is, String enc) throws PIMException, UnsupportedEncodingException {
        return fromSerialFormat(is, enc, null);
    }
    private PIMItem[] fromSerialFormat(InputStream is, String enc, PIMList list)
            throws PIMException, UnsupportedEncodingException {
        if (enc == null) {
            enc = "UTF-8";
        }
        InputStream in = new MarkableInputStream(is);
        in.mark(2147483647);
        try {
            for (int i = 0;i < formats.length;i++) {
                try {
                    PIMItem[] items = formats[i].decode(in, enc, list);
                    if (items == null) {
                        throw new PIMException("Empty stream or insufficient data");
                    }

                    return items;
                } catch (UnsupportedPIMFormatException e) {
                    in.reset();
                    in.mark(2147483647);
                }
            }

            throw new PIMException("Format not recognized");
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {}

        throw new PIMException();
    }
    public String[] listPIMLists(int pimListType) {
        checkPermissions(pimListType, 1);
        validatePimListType(pimListType);

        return PIMHandler.getInstance().getListNames(pimListType);
    }
    private void checkListPermissions(AbstractPIMList list, int mode) {
        if (mode == 1) {
            list.checkReadPermission();
        } else if (mode == 2) {
            list.checkWritePermission();
        } else if (mode == 3) {
            list.checkReadPermission();
            list.checkWritePermission();
        } else {
            throw new IllegalArgumentException("Invalid mode " + mode);
        }
    }
    private int[] getPermissions(int listType, int mode) {
        switch (listType) {
        case 1 :
            switch (mode) {
            case 1 :
                return new int[]{ 25 };
            case 2 :
                return new int[]{ 26 };
            case 3 :
                return new int[]{ 25, 26 };
            }

            throw new IllegalArgumentException("Not a valid mode: " + mode);
        case 2 :
            switch (mode) {
            case 1 :
                return new int[]{ 27 };
            case 2 :
                return new int[]{ 28 };
            case 3 :
                return new int[]{ 27, 28 };
            }

            throw new IllegalArgumentException("Not a valid mode: " + mode);
        case 3 :
            switch (mode) {
            case 1 :
                return new int[]{ 29 };
            case 2 :
                return new int[]{ 30 };
            case 3 :
                return new int[]{ 29, 30 };
            }

            throw new IllegalArgumentException("Not a valid mode: " + mode);
        }

        throw new IllegalArgumentException("Not a valid list type: " + listType);
    }
    private void checkPermissions(int pimListType, int mode) {
        int[] permissions = getPermissions(pimListType, mode);
//      MIDletSuite suite = Scheduler.getScheduler().getMIDletSuite();
//
//      for (int i = 0; i < permissions.length; i++) {
//        String name = Permissions.getName(permissions[i]);
//        int status = suite.checkPermission(name);
//        if (status == 0)
//        {
//          suite.checkIfPermissionAllowed(permissions[i]); } else {
//          if (status != 1)
//            continue;
//          permissions[i] = -1;
//        }
//      }
//      for (int i = 0; i < permissions.length; i++) {
//        if (permissions[i] == -1) continue;
//        try {
//          suite.checkForPermission(permissions[i], null);
//        } catch (InterruptedException e) {
//          throw new SecurityException("Security check interrupted: " + e.getMessage());
//        }
//      }
    }
    public PIMList openPIMList(int pimListType, int mode) throws PIMException {
        validatePimListType(pimListType);
        validateMode(mode);
        checkPermissions(pimListType, mode);
        String listName = PIMHandler.getInstance().getDefaultListName(pimListType);
        if (listName == null) {
            throw new PIMException("List not available");
        }

        return openPIMListImpl(pimListType, mode, listName);
    }
    public PIMList openPIMList(int pimListType, int mode, String name) throws PIMException {
        if (name == null) {
            throw new NullPointerException("PIM list name cannot be null");
        }
        validatePimListType(pimListType);
        validateMode(mode);
        checkPermissions(pimListType, mode);
        validateName(pimListType, name);

        return openPIMListImpl(pimListType, mode, name);
    }
    private PIMList openPIMListImpl(int pimListType, int mode, String name) throws PIMException {
        AbstractPIMList list;
        switch (pimListType) {
        case 1 :
            list = new ContactListImpl(name, mode);

            break;
        case 2 :
            list = new EventListImpl(name, mode);

            break;
        case 3 :
            list = new ToDoListImpl(name, mode);

            break;
        default :
            throw new Error("Unreachable code");
        }
        Object[] keys = PIMHandler.getInstance().getListKeys(pimListType, name);
        for (int i = 0;i < keys.length;i++) {
            byte[] data = PIMHandler.getInstance().getListElement(pimListType, name, keys[i]);
            try {
                PIMItem[] items = fromSerialFormat(new ByteArrayInputStream(data), "UTF-8", list);
                for (int j = 0;j < items.length;j++) {
                    AbstractPIMItem item = (AbstractPIMItem) items[j];
                    item.setKey(keys[i]);
                    list.addItem(item);
                    // causing exception as other fields not available
                    // item.setDefaultValues();
                    item.setModified(false);
                }
            } catch (UnsupportedEncodingException e) {
                throw new Error("UTF-8 not supported");
            } catch (PIMException e) {}
        }

        return list;
    }
    public String[] supportedSerialFormats(int pimListType) {
        validatePimListType(pimListType);
        int supportedFormatCount = 0;
        for (int i = 0;i < formats.length;i++) {
            if (formats[i].isTypeSupported(pimListType)) {
                supportedFormatCount++;
            }
        }
        String[] supportedFormats = new String[supportedFormatCount];
        for (int i = 0;i < formats.length;i++) {
            if (formats[i].isTypeSupported(pimListType)) {
                supportedFormatCount--;
                supportedFormats[supportedFormatCount] = formats[i].getName();
            }
        }

        return supportedFormats;
    }
    public void toSerialFormat(PIMItem item, OutputStream os, String enc, String dataFormat)
            throws PIMException, UnsupportedEncodingException {
        if (enc == null) {
            enc = "UTF-8";
        }
        if (dataFormat == null) {
            throw new NullPointerException("Null data format");
        }
        if (item == null) {
            throw new NullPointerException("Null PIM item");
        }
        try {
            for (int i = 0;i < formats.length;i++) {
                if (formats[i].getName().equals(dataFormat)) {
                    formats[i].encode(os, enc, item);

                    return;
                }
            }

            throw new PIMException("Data format '" + dataFormat + "' not supported.");
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {}

        throw new PIMException();
    }
    private void validatePimListType(int pimListType) {
        switch (pimListType) {
        case 1 :
        case 2 :
        case 3 :
            break;
        default :
            throw new IllegalArgumentException("Not a valid PIM list type: " + pimListType);
        }
    }
    private void validateMode(int mode) {
        switch (mode) {
        case 1 :
        case 2 :
        case 3 :
            break;
        default :
            throw new IllegalArgumentException("Invalid PIM list mode: " + mode);
        }
    }
    private void validateName(int pimListType, String name) throws PIMException {
        String[] names = PIMHandler.getInstance().getListNames(pimListType);
        for (int i = 0;i < names.length;i++) {
            if (name.equals(names[i])) {
                return;
            }
        }

        throw new PIMException("PIM list does not exist: '" + name + "'");
    }
}
