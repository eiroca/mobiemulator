package Emulator;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class VariableAccess {
    StringBuffer sb = new StringBuffer(2000);
    public VariableAccess() {}

    String getValue(Object paramObject, Field paramField, boolean paramBoolean) {
        try {
            if (paramField.get(paramObject) == null) {
                return "null";
            }
            if (paramField.getType() == Integer.TYPE) {
                return paramBoolean ? "0x" + Integer.toHexString(paramField.getInt(paramObject))
                                    : String.valueOf(paramField.getInt(paramObject));
            }
            if (paramField.getType() == Boolean.TYPE) {
                return String.valueOf(paramField.getBoolean(paramObject));
            }
            if (paramField.getType() == Byte.TYPE) {
                return paramBoolean ? "0x" + Integer.toHexString(paramField.getByte(paramObject))
                                    : String.valueOf(paramField.getByte(paramObject));
            }
            if (paramField.getType() == Short.TYPE) {
                return paramBoolean ? "0x" + Integer.toHexString(paramField.getShort(paramObject))
                                    : String.valueOf(paramField.getShort(paramObject));
            }
            if (paramField.getType() == Long.TYPE) {
                return paramBoolean ? "0x" + Long.toHexString(paramField.getLong(paramObject))
                                    : String.valueOf(paramField.getLong(paramObject));
            }
            if (paramField.getType() == String.class) {
                return String.valueOf(paramField.get(paramObject));
            }
            if (paramField.getType().isArray()) {
                Object localObject = paramField.get(paramObject);
                this.sb.setLength(0);
                this.sb.append("length=");
                this.sb.append(Array.getLength(localObject));
                this.sb.append(" {");
                int i = Math.min(Array.getLength(localObject), 20);
                for (int j = 0;j < i;j++) {
                    this.sb.append(Array.get(localObject, j));
                    this.sb.append(' ');
                }
                if (i < Array.getLength(localObject)) {
                    this.sb.append("...");
                }
                this.sb.append('}');

                return this.sb.toString();
            }

            return paramField.get(paramObject).toString();
        } catch (Exception localException) {}

        return "???";
    }
    void setValue(Object paramObject, Field paramField, String paramString) {
        try {
            int i = paramString.startsWith("0x") ? 16
                    : 10;
            if (i == 16) {
                paramString = paramString.substring(2);
            }
            if (paramField.getType() == Long.TYPE) {
                paramField.setLong(paramObject, Long.parseLong(paramString, i));
            } else if (paramField.getType() == Integer.TYPE) {
                paramField.setInt(paramObject, Integer.parseInt(paramString, i));
            } else if (paramField.getType() == Short.TYPE) {
                paramField.setShort(paramObject, (short) Integer.parseInt(paramString, i));
            } else if (paramField.getType() == Byte.TYPE) {
                paramField.setByte(paramObject, (byte) Integer.parseInt(paramString, i));
            } else if (paramField.getType() == Boolean.TYPE) {
                paramField.setBoolean(paramObject, Boolean.valueOf(paramString));
            } else if (paramField.getType() == String.class) {
                paramField.set(paramObject, paramString);
            }
        } catch (Exception localException) {}
    }
    Object getField(Object paramObject, Field paramField) {
        try {
            return paramField.get(paramObject);
        } catch (Exception localException) {}

        return null;
    }
    Object getField(String paramString1, Object paramObject, String paramString2) {
        try {
            return Class.forName(paramString1).getDeclaredField(paramString2).get(paramObject);
        } catch (Exception localException) {
            localException.printStackTrace();
        }

        return null;
    }
    void setField(String paramString1, Object paramObject1, String paramString2, Object paramObject2) {
        try {
            Class.forName(paramString1).getDeclaredField(paramString2).set(paramObject1, paramObject2);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}
