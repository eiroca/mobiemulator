/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Image;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ClassProfiler {
    public static final String BYTEARRAY="[B";
    public static final String INTARRAY="[I";
    public static final String BOOLARRAY="[Z";
    public static final String STRINGARRAY="[S";
    public static final String DOUBLEARRAY="[D";
    public static final String CHARARRAY="[C";
    public static final String LONGARRAY="[J";
    public static Hashtable ignoreClasses    = new Hashtable();
    public static int       totalObjectsSize = 0;
    static {
        ignoreClasses.put("javax.microedition.lcdui.Canvas", "javax.microedition.lcdui.Canvas");
        ignoreClasses.put("javax.microedition.lcdui.Displayable", "javax.microedition.lcdui.Displayable");
        // ignoreClasses.put("javax.microedition..midlet.MIDlet","javax.microedition.midlet.MIDlet");
    }
    public Hashtable  classProfiles = new Hashtable();
    private Hashtable instances     = new Hashtable();
    public ClassProfiler() {}

    public void parseJar() {
        classProfiles.clear();
        instances.clear();
        totalObjectsSize = 0;
        if ((MobiEmulator.mobiEmulatorInstance != null) && (MobiEmulator.mobiEmulatorInstance.jarClasses.size() > 0)) {
            for (int i = 0;i < MobiEmulator.mobiEmulatorInstance.jarClasses.size();i++) {
                try {
                    String classname = (String) MobiEmulator.mobiEmulatorInstance.jarClasses.get(i);
                    parseClasses(Class.forName(classname, false, MobiEmulator.mobiEmulatorInstance.multiClassLoader), null, classname);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            totalObjectsSize = getTotalObjectSize();
        }
    }
    public int getTotalObjectSize() {
        int         i    = 0;
        Enumeration list = classProfiles.elements();
        while (list.hasMoreElements()) {
            i += ((ClassProfiler.ClassProfile) list.nextElement()).getSizeOfInstances();
        }

        return i;
    }

    public void parseClasses(Class cls, Object clzobject, String name) {
        String       classname  = cls.getName();
        ClassProfile clsProfile = (ClassProfile) this.classProfiles.get(classname);
        // System.out.println("local ClassProfile "+(clsProfile==null));
        if (ignoreClasses.containsKey(classname)) {
            return;
        }
        if (cls.isInterface()) {
            return;
        }
        if (clsProfile == null) {
            clsProfile = new ClassProfile(classname);
            this.classProfiles.put(classname, clsProfile);
        }
        if (clzobject != null) {
            if (this.instances.containsKey(clzobject)) {
                Instance localInstance = (Instance) this.instances.get(clzobject);
                if (findClassPath(name) < findClassPath(localInstance.path)) {
                    localInstance.path = name;
                }

                return;
            }
            Instance classInstance = new Instance(name, clzobject);
            clsProfile.classInstances.add(classInstance);
            this.instances.put(clzobject, classInstance);
        }
        if ((clzobject != null) && (cls.isArray())) {
            if (cls.getComponentType().isPrimitive()) {
                return;
            }
            if ((classname.startsWith(BYTEARRAY)) || (classname.startsWith(CHARARRAY)) || (classname.startsWith(DOUBLEARRAY))
                    || (classname.startsWith(INTARRAY)) || (classname.startsWith(LONGARRAY)) || (classname.startsWith(STRINGARRAY))
                    || (classname.startsWith(BOOLARRAY))) {
                return;
            }
            // only if object is instance of class not primitive types
            for (int i = 0;i < Array.getLength(clzobject);i++) {
                Object arrayObject = Array.get(clzobject, i);
                if (arrayObject != null) {
                    parseClasses(arrayObject.getClass(), arrayObject, name + '[' + i + ']');
                }
            }

            return;
        }
        Object vectorObj = null;
        if ((clzobject != null) && ((clzobject instanceof Vector))) {
            vectorObj = ((Vector) clzobject).elements();
            while (((Enumeration) vectorObj).hasMoreElements()) {
                Object vectorElement = ((Enumeration) vectorObj).nextElement();
                if (vectorElement != null) {
                    parseClasses(vectorElement.getClass(), vectorElement, name + "(VectorElement)");
                }
            }

            return;
        }
        Object hashtableObj;
        if ((clzobject instanceof Hashtable)) {
            vectorObj = ((Hashtable) clzobject).keys();
            while (((Enumeration) vectorObj).hasMoreElements()) {
                Object hashtableElement = ((Enumeration) vectorObj).nextElement();
                hashtableObj = ((Hashtable) clzobject).get(hashtableElement);
                if (hashtableObj != null) {
                    parseClasses(hashtableObj.getClass(), hashtableObj, name + "(HashtableKey=" + hashtableElement + ")");
                }
            }

            return;
        }
        if ((cls.getPackage() != null) && (!java.io.InputStream.class.isAssignableFrom(cls))) {
            // if ((!(v == null ? (MemoryViewFrame.v = c("java.io.InputStream")) : v).isAssignableFrom(cls)) && (!ignoreClasses.containsKey(str1)));
            return;
        } else {
            Field[] classFields = getAllFields(cls);
            String  fieldName;
            for (Field classField : classFields) {
                fieldName = classField.getName();
                // skipping final primitive types and non static objects
                if (((Modifier.isFinal(classField.getModifiers())) && (classField.getType().isPrimitive()))
                        || ((!Modifier.isStatic(classField.getModifiers())) && (clzobject == null))) {
                    continue;
                }
                try {
                    classField.setAccessible(true);
                    Object fld = classField.get(clzobject);
                    // this.variableAccess.getField(clzobject, classFields[j]);
                    String str2 = name + '.' + (String) fieldName;
//                  if(localObject4==null)
//                      continue;
                    // if field is instance of class (may be has some more fields)
                    if ((!classField.getType().isPrimitive()) || (fld == null)) {
                        if (fld != null) {
                            parseClasses(fld.getClass(), fld, str2);
                        }
                    }
                    // System.out.println("completed getting fields for "+cls.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public void deInitialize() {
        int len = classProfiles.size();
        for (int i = 0;i < len;i++) {
            ((ClassProfile) classProfiles.get(i)).deInitialize();
        }
        classProfiles.clear();
        instances.clear();
    }
    public Field[] getAllFields(Class clazz) {
        Vector fields = new Vector();
        if (clazz.isArray()) {
            int size=Array.getLength(clazz);
            for (int i = 0;i <size ;i++) {
                fields.add(Integer.toString(i));
            }
        }
        getSuperClassFields(clazz, fields);
        Field[] classFields = new Field[fields.size()];
        int fieldsLen=classFields.length;
        int i=0;
        for (Object field : fields) {
            classFields[i++] = ((Field) field);
        }
        fields.removeAllElements();
        fields = null;

        return classFields;
    }
    void getSuperClassFields(Class classToGetFields, Vector fields) {
        if (ignoreClasses.containsKey(classToGetFields.getName())) {
            return;
        }
        if (classToGetFields.getSuperclass() != null) {
            getSuperClassFields(classToGetFields.getSuperclass(), fields);
        }
        Field[] superclassFields = classToGetFields.getDeclaredFields();
        for (Field superclassField : superclassFields) {
            fields.add(superclassField);
        }
        superclassFields = null;
    }
    Vector getInstancesOf(String classname) {
        if (classname == null) {
            return null;
        }
        ClassProfiler.ClassProfile clzprofile     = (ClassProfiler.ClassProfile) classProfiles.get(classname);
        Vector                     instanceVector = new Vector();
        if (clzprofile != null) {
            Enumeration instancesList = clzprofile.classInstances.elements();
            while (instancesList.hasMoreElements()) {
                instanceVector.add(((ClassProfiler.Instance) instancesList.nextElement()));
            }
        }

        return instanceVector;
    }
    public int sizeof(Class paramClass, Object paramObject) {
        Field[] arrayOfField = paramClass.getDeclaredFields();
        int     i            = 0;
        for (Field localField : arrayOfField) {
            Class localClass = localField.getType();
            String str = localField.getName();
            // skipping the final primitive types
            if ((Modifier.isFinal(localField.getModifiers())) && (localClass.isPrimitive())) {
                continue;
            }
            // skip the static non null objeccts
            if ((Modifier.isStatic(localField.getModifiers())) && (paramObject != null)) {
                continue;
            }
            // skip the non static null objects
            if ((!Modifier.isStatic(localField.getModifiers())) && (paramObject == null)) {
                continue;
            }
            if (localClass == Long.TYPE) {
                i += 8;
            } else if (localClass == Integer.TYPE) {
                i += 4;
            } else if (localClass == Short.TYPE) {
                i += 4;
            } else if (localClass == Byte.TYPE) {
                i += 4;
            } else if (localClass == Boolean.TYPE) {
                i += 4;
            } else {
                i += 4;
            }
            localClass = null;
        }
        if (paramObject != null) {
            i += 15;
            if (paramClass == long[].class) {
                i += 8 * Array.getLength(paramObject);
            } else if (paramClass == int[].class) {
                i += 4 * Array.getLength(paramObject);
            } else if (paramClass == short[].class) {
                i += 2 * Array.getLength(paramObject);
            } else if (paramClass == byte[].class) {
                i += Array.getLength(paramObject);
            } else if (paramClass == boolean[].class) {
                i += 4 * Array.getLength(paramObject);
            } else if (paramClass == String.class) {
                i += 2 * ((String) paramObject).length();
            } else if (paramClass == Image.class) {
                Image localImage = (Image) paramObject;
                i += localImage.getWidth() * localImage.getHeight() * 2;
            } else if (paramClass.isArray()) {
                i += 4 * Array.getLength(paramObject);
            }
        }
        arrayOfField = null;

        return i;
    }
    int findClassPath(String path) {
        int i = 0;
        int j = -1;
        do {
            j = path.indexOf('.', j + 1);
            i++;
        } while (j >= 0);

        return i;
    }
    class ClassProfile implements Comparable {
        public Vector classInstances  = new Vector();
        private int   staticClassSize = 0;
        public String name;
        ClassProfile(String classname) {
            this.name = classname;
            try {
                this.staticClassSize = sizeof(Class.forName(classname, false, MobiEmulator.mobiEmulatorInstance.multiClassLoader), null);
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }

        public int getSizeOfInstances() {
            int         i                = this.staticClassSize;
            Enumeration localEnumeration = this.classInstances.elements();
            while (localEnumeration.hasMoreElements()) {
                i += ((Instance) localEnumeration.nextElement()).totalSize;
            }
            localEnumeration = null;

            return i;
        }
        public String toString() {
            return "";
        }
        public int compareTo(Object obj) {
            return this.name.compareTo(((ClassProfile) obj).name);
        }
        public void deInitialize() {
            classInstances.removeAllElements();
            classInstances  = null;
            name            = null;
            staticClassSize = 0;
        }
    }

    class Instance implements Comparable {
        Object obj;
        String path;
        int    totalSize;
        public Instance(String packagePath, Object classObj) {
            this.path      = packagePath;
            this.obj       = classObj;
            this.totalSize = sizeof(classObj.getClass(), classObj);
        }

        public int compareTo(Object o) {
            return this.totalSize - ((Instance) o).totalSize;
        }
    }
}
