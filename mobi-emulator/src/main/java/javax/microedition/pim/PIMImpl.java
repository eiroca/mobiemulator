package javax.microedition.pim;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

// ~--- JDK imports ------------------------------------------------------------

// import com.sun.midp.midlet.MIDletSuite;
// import com.sun.midp.midlet.Scheduler;
// import com.sun.midp.security.Permissions;

import javax.microedition.pim.formats.VCalendar10Format;
import javax.microedition.pim.formats.VCard21Format;
import javax.microedition.pim.formats.VCard30Format;

public final class PIMImpl extends PIM {

  private static final PIMFormat[] formats = {
      new VCalendar10Format(), new VCard21Format(), new VCard30Format()
  };

  @Override
  public PIMItem[] fromSerialFormat(final InputStream is, final String enc) throws PIMException, UnsupportedEncodingException {
    return fromSerialFormat(is, enc, null);
  }

  private PIMItem[] fromSerialFormat(final InputStream is, String enc, final PIMList list)
      throws PIMException, UnsupportedEncodingException {
    if (enc == null) {
      enc = "UTF-8";
    }
    final InputStream in = new MarkableInputStream(is);
    in.mark(2147483647);
    try {
      for (final PIMFormat format : PIMImpl.formats) {
        try {
          final PIMItem[] items = format.decode(in, enc, list);
          if (items == null) { throw new PIMException("Empty stream or insufficient data"); }

          return items;
        }
        catch (final UnsupportedPIMFormatException e) {
          in.reset();
          in.mark(2147483647);
        }
      }

      throw new PIMException("Format not recognized");
    }
    catch (final UnsupportedEncodingException e) {
      throw e;
    }
    catch (final IOException e) {
    }

    throw new PIMException();
  }

  @Override
  public String[] listPIMLists(final int pimListType) {
    checkPermissions(pimListType, 1);
    validatePimListType(pimListType);

    return PIMHandler.getInstance().getListNames(pimListType);
  }

  private void checkListPermissions(final AbstractPIMList list, final int mode) {
    if (mode == 1) {
      list.checkReadPermission();
    }
    else if (mode == 2) {
      list.checkWritePermission();
    }
    else if (mode == 3) {
      list.checkReadPermission();
      list.checkWritePermission();
    }
    else {
      throw new IllegalArgumentException("Invalid mode " + mode);
    }
  }

  private int[] getPermissions(final int listType, final int mode) {
    switch (listType) {
      case 1:
        switch (mode) {
          case 1:
            return new int[] {
                25
            };
          case 2:
            return new int[] {
                26
            };
          case 3:
            return new int[] {
                25, 26
            };
        }

        throw new IllegalArgumentException("Not a valid mode: " + mode);
      case 2:
        switch (mode) {
          case 1:
            return new int[] {
                27
            };
          case 2:
            return new int[] {
                28
            };
          case 3:
            return new int[] {
                27, 28
            };
        }

        throw new IllegalArgumentException("Not a valid mode: " + mode);
      case 3:
        switch (mode) {
          case 1:
            return new int[] {
                29
            };
          case 2:
            return new int[] {
                30
            };
          case 3:
            return new int[] {
                29, 30
            };
        }

        throw new IllegalArgumentException("Not a valid mode: " + mode);
    }

    throw new IllegalArgumentException("Not a valid list type: " + listType);
  }

  private void checkPermissions(final int pimListType, final int mode) {
    getPermissions(pimListType, mode);
  }

  @Override
  public PIMList openPIMList(final int pimListType, final int mode) throws PIMException {
    validatePimListType(pimListType);
    validateMode(mode);
    checkPermissions(pimListType, mode);
    final String listName = PIMHandler.getInstance().getDefaultListName(pimListType);
    if (listName == null) { throw new PIMException("List not available"); }

    return openPIMListImpl(pimListType, mode, listName);
  }

  @Override
  public PIMList openPIMList(final int pimListType, final int mode, final String name) throws PIMException {
    if (name == null) { throw new NullPointerException("PIM list name cannot be null"); }
    validatePimListType(pimListType);
    validateMode(mode);
    checkPermissions(pimListType, mode);
    validateName(pimListType, name);

    return openPIMListImpl(pimListType, mode, name);
  }

  private PIMList openPIMListImpl(final int pimListType, final int mode, final String name) throws PIMException {
    AbstractPIMList list;
    switch (pimListType) {
      case 1:
        list = new ContactListImpl(name, mode);

        break;
      case 2:
        list = new EventListImpl(name, mode);

        break;
      case 3:
        list = new ToDoListImpl(name, mode);

        break;
      default:
        throw new Error("Unreachable code");
    }
    final Object[] keys = PIMHandler.getInstance().getListKeys(pimListType, name);
    for (final Object key : keys) {
      final byte[] data = PIMHandler.getInstance().getListElement(pimListType, name, key);
      try {
        final PIMItem[] items = fromSerialFormat(new ByteArrayInputStream(data), "UTF-8", list);
        for (final PIMItem item2 : items) {
          final AbstractPIMItem item = (AbstractPIMItem)item2;
          item.setKey(key);
          list.addItem(item);
          // causing exception as other fields not available
          // item.setDefaultValues();
          item.setModified(false);
        }
      }
      catch (final UnsupportedEncodingException e) {
        throw new Error("UTF-8 not supported");
      }
      catch (final PIMException e) {
      }
    }

    return list;
  }

  @Override
  public String[] supportedSerialFormats(final int pimListType) {
    validatePimListType(pimListType);
    int supportedFormatCount = 0;
    for (final PIMFormat format : PIMImpl.formats) {
      if (format.isTypeSupported(pimListType)) {
        supportedFormatCount++;
      }
    }
    final String[] supportedFormats = new String[supportedFormatCount];
    for (final PIMFormat format : PIMImpl.formats) {
      if (format.isTypeSupported(pimListType)) {
        supportedFormatCount--;
        supportedFormats[supportedFormatCount] = format.getName();
      }
    }

    return supportedFormats;
  }

  @Override
  public void toSerialFormat(final PIMItem item, final OutputStream os, String enc, final String dataFormat)
      throws PIMException, UnsupportedEncodingException {
    if (enc == null) {
      enc = "UTF-8";
    }
    if (dataFormat == null) { throw new NullPointerException("Null data format"); }
    if (item == null) { throw new NullPointerException("Null PIM item"); }
    try {
      for (final PIMFormat format : PIMImpl.formats) {
        if (format.getName().equals(dataFormat)) {
          format.encode(os, enc, item);

          return;
        }
      }

      throw new PIMException("Data format '" + dataFormat + "' not supported.");
    }
    catch (final UnsupportedEncodingException e) {
      throw e;
    }
    catch (final IOException e) {
    }

    throw new PIMException();
  }

  private void validatePimListType(final int pimListType) {
    switch (pimListType) {
      case 1:
      case 2:
      case 3:
        break;
      default:
        throw new IllegalArgumentException("Not a valid PIM list type: " + pimListType);
    }
  }

  private void validateMode(final int mode) {
    switch (mode) {
      case 1:
      case 2:
      case 3:
        break;
      default:
        throw new IllegalArgumentException("Invalid PIM list mode: " + mode);
    }
  }

  private void validateName(final int pimListType, final String name) throws PIMException {
    final String[] names = PIMHandler.getInstance().getListNames(pimListType);
    for (final String name2 : names) {
      if (name.equals(name2)) { return; }
    }

    throw new PIMException("PIM list does not exist: '" + name + "'");
  }
}
