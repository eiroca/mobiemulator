package Emulator;

// ~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class VariableAccess {

  StringBuffer sb = new StringBuffer(2000);

  public VariableAccess() {
  }

  String getValue(final Object paramObject, final Field paramField, final boolean paramBoolean) {
    try {
      if (paramField.get(paramObject) == null) { return "null"; }
      if (paramField.getType() == Integer.TYPE) { return paramBoolean ? "0x" + Integer.toHexString(paramField.getInt(paramObject))
          : String.valueOf(paramField.getInt(paramObject)); }
      if (paramField.getType() == Boolean.TYPE) { return String.valueOf(paramField.getBoolean(paramObject)); }
      if (paramField.getType() == Byte.TYPE) { return paramBoolean ? "0x" + Integer.toHexString(paramField.getByte(paramObject))
          : String.valueOf(paramField.getByte(paramObject)); }
      if (paramField.getType() == Short.TYPE) { return paramBoolean ? "0x" + Integer.toHexString(paramField.getShort(paramObject))
          : String.valueOf(paramField.getShort(paramObject)); }
      if (paramField.getType() == Long.TYPE) { return paramBoolean ? "0x" + Long.toHexString(paramField.getLong(paramObject))
          : String.valueOf(paramField.getLong(paramObject)); }
      if (paramField.getType() == String.class) { return String.valueOf(paramField.get(paramObject)); }
      if (paramField.getType().isArray()) {
        final Object localObject = paramField.get(paramObject);
        sb.setLength(0);
        sb.append("length=");
        sb.append(Array.getLength(localObject));
        sb.append(" {");
        final int i = Math.min(Array.getLength(localObject), 20);
        for (int j = 0; j < i; j++) {
          sb.append(Array.get(localObject, j));
          sb.append(' ');
        }
        if (i < Array.getLength(localObject)) {
          sb.append("...");
        }
        sb.append('}');

        return sb.toString();
      }

      return paramField.get(paramObject).toString();
    }
    catch (final Exception localException) {
    }

    return "???";
  }

  void setValue(final Object paramObject, final Field paramField, String paramString) {
    try {
      final int i = paramString.startsWith("0x") ? 16
          : 10;
      if (i == 16) {
        paramString = paramString.substring(2);
      }
      if (paramField.getType() == Long.TYPE) {
        paramField.setLong(paramObject, Long.parseLong(paramString, i));
      }
      else if (paramField.getType() == Integer.TYPE) {
        paramField.setInt(paramObject, Integer.parseInt(paramString, i));
      }
      else if (paramField.getType() == Short.TYPE) {
        paramField.setShort(paramObject, (short)Integer.parseInt(paramString, i));
      }
      else if (paramField.getType() == Byte.TYPE) {
        paramField.setByte(paramObject, (byte)Integer.parseInt(paramString, i));
      }
      else if (paramField.getType() == Boolean.TYPE) {
        paramField.setBoolean(paramObject, Boolean.valueOf(paramString));
      }
      else if (paramField.getType() == String.class) {
        paramField.set(paramObject, paramString);
      }
    }
    catch (final Exception localException) {
    }
  }

  Object getField(final Object paramObject, final Field paramField) {
    try {
      return paramField.get(paramObject);
    }
    catch (final Exception localException) {
    }

    return null;
  }

  Object getField(final String paramString1, final Object paramObject, final String paramString2) {
    try {
      return Class.forName(paramString1).getDeclaredField(paramString2).get(paramObject);
    }
    catch (final Exception localException) {
      localException.printStackTrace();
    }

    return null;
  }

  void setField(final String paramString1, final Object paramObject1, final String paramString2, final Object paramObject2) {
    try {
      Class.forName(paramString1).getDeclaredField(paramString2).set(paramObject1, paramObject2);
    }
    catch (final Exception localException) {
      localException.printStackTrace();
    }
  }
}
