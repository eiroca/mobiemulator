package Emulator;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.lcdui.Image;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ClassProfiler {

  public static final String BYTEARRAY = "[B";
  public static final String INTARRAY = "[I";
  public static final String BOOLARRAY = "[Z";
  public static final String STRINGARRAY = "[S";
  public static final String DOUBLEARRAY = "[D";
  public static final String CHARARRAY = "[C";
  public static final String LONGARRAY = "[J";
  public static Hashtable ignoreClasses = new Hashtable();
  public static int totalObjectsSize = 0;
  static {
    ClassProfiler.ignoreClasses.put("javax.microedition.lcdui.Canvas", "javax.microedition.lcdui.Canvas");
    ClassProfiler.ignoreClasses.put("javax.microedition.lcdui.Displayable", "javax.microedition.lcdui.Displayable");
    // ignoreClasses.put("javax.microedition..midlet.MIDlet","javax.microedition.midlet.MIDlet");
  }
  public Hashtable classProfiles = new Hashtable();
  private final Hashtable instances = new Hashtable();

  public ClassProfiler() {
  }

  public void parseJar() {
    classProfiles.clear();
    instances.clear();
    ClassProfiler.totalObjectsSize = 0;
    if ((MobiEmulator.mobiEmulatorInstance != null) && (MobiEmulator.mobiEmulatorInstance.jarClasses.size() > 0)) {
      for (int i = 0; i < MobiEmulator.mobiEmulatorInstance.jarClasses.size(); i++) {
        try {
          final String classname = (String)MobiEmulator.mobiEmulatorInstance.jarClasses.get(i);
          parseClasses(Class.forName(classname, false, MobiEmulator.mobiEmulatorInstance.multiClassLoader), null, classname);
        }
        catch (final Exception ex) {
          ex.printStackTrace();
        }
      }
      ClassProfiler.totalObjectsSize = getTotalObjectSize();
    }
  }

  public int getTotalObjectSize() {
    int i = 0;
    final Enumeration list = classProfiles.elements();
    while (list.hasMoreElements()) {
      i += ((ClassProfiler.ClassProfile)list.nextElement()).getSizeOfInstances();
    }

    return i;
  }

  public void parseClasses(final Class cls, final Object clzobject, final String name) {
    final String classname = cls.getName();
    ClassProfile clsProfile = (ClassProfile)classProfiles.get(classname);
    // System.out.println("local ClassProfile "+(clsProfile==null));
    if (ClassProfiler.ignoreClasses.containsKey(classname)) { return; }
    if (cls.isInterface()) { return; }
    if (clsProfile == null) {
      clsProfile = new ClassProfile(classname);
      classProfiles.put(classname, clsProfile);
    }
    if (clzobject != null) {
      if (instances.containsKey(clzobject)) {
        final Instance localInstance = (Instance)instances.get(clzobject);
        if (findClassPath(name) < findClassPath(localInstance.path)) {
          localInstance.path = name;
        }

        return;
      }
      final Instance classInstance = new Instance(name, clzobject);
      clsProfile.classInstances.add(classInstance);
      instances.put(clzobject, classInstance);
    }
    if ((clzobject != null) && (cls.isArray())) {
      if (cls.getComponentType().isPrimitive()) { return; }
      if ((classname.startsWith(ClassProfiler.BYTEARRAY)) || (classname.startsWith(ClassProfiler.CHARARRAY)) || (classname.startsWith(ClassProfiler.DOUBLEARRAY))
          || (classname.startsWith(ClassProfiler.INTARRAY)) || (classname.startsWith(ClassProfiler.LONGARRAY)) || (classname.startsWith(ClassProfiler.STRINGARRAY))
          || (classname.startsWith(ClassProfiler.BOOLARRAY))) {
        return;
      }
      // only if object is instance of class not primitive types
      for (int i = 0; i < Array.getLength(clzobject); i++) {
        final Object arrayObject = Array.get(clzobject, i);
        if (arrayObject != null) {
          parseClasses(arrayObject.getClass(), arrayObject, name + '[' + i + ']');
        }
      }

      return;
    }
    Object vectorObj = null;
    if ((clzobject != null) && ((clzobject instanceof Vector))) {
      vectorObj = ((Vector)clzobject).elements();
      while (((Enumeration)vectorObj).hasMoreElements()) {
        final Object vectorElement = ((Enumeration)vectorObj).nextElement();
        if (vectorElement != null) {
          parseClasses(vectorElement.getClass(), vectorElement, name + "(VectorElement)");
        }
      }

      return;
    }
    Object hashtableObj;
    if ((clzobject instanceof Hashtable)) {
      vectorObj = ((Hashtable)clzobject).keys();
      while (((Enumeration)vectorObj).hasMoreElements()) {
        final Object hashtableElement = ((Enumeration)vectorObj).nextElement();
        hashtableObj = ((Hashtable)clzobject).get(hashtableElement);
        if (hashtableObj != null) {
          parseClasses(hashtableObj.getClass(), hashtableObj, name + "(HashtableKey=" + hashtableElement + ")");
        }
      }

      return;
    }
    if ((cls.getPackage() != null) && (!java.io.InputStream.class.isAssignableFrom(cls))) {
      // if ((!(v == null ? (MemoryViewFrame.v = c("java.io.InputStream")) : v).isAssignableFrom(cls)) && (!ignoreClasses.containsKey(str1)));
      return;
    }
    else {
      final Field[] classFields = getAllFields(cls);
      String fieldName;
      for (final Field classField : classFields) {
        fieldName = classField.getName();
        // skipping final primitive types and non static objects
        if (((Modifier.isFinal(classField.getModifiers())) && (classField.getType().isPrimitive()))
            || ((!Modifier.isStatic(classField.getModifiers())) && (clzobject == null))) {
          continue;
        }
        try {
          classField.setAccessible(true);
          final Object fld = classField.get(clzobject);
          // this.variableAccess.getField(clzobject, classFields[j]);
          final String str2 = name + '.' + fieldName;
          //                  if(localObject4==null)
          //                      continue;
          // if field is instance of class (may be has some more fields)
          if ((!classField.getType().isPrimitive()) || (fld == null)) {
            if (fld != null) {
              parseClasses(fld.getClass(), fld, str2);
            }
          }
          // System.out.println("completed getting fields for "+cls.getName());
        }
        catch (final Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  public void deInitialize() {
    final int len = classProfiles.size();
    for (int i = 0; i < len; i++) {
      ((ClassProfile)classProfiles.get(i)).deInitialize();
    }
    classProfiles.clear();
    instances.clear();
  }

  public Field[] getAllFields(final Class clazz) {
    Vector fields = new Vector();
    if (clazz.isArray()) {
      final int size = Array.getLength(clazz);
      for (int i = 0; i < size; i++) {
        fields.add(Integer.toString(i));
      }
    }
    getSuperClassFields(clazz, fields);
    final Field[] classFields = new Field[fields.size()];
    int i = 0;
    for (final Object field : fields) {
      classFields[i++] = ((Field)field);
    }
    fields.removeAllElements();
    fields = null;

    return classFields;
  }

  void getSuperClassFields(final Class classToGetFields, final Vector fields) {
    if (ClassProfiler.ignoreClasses.containsKey(classToGetFields.getName())) { return; }
    if (classToGetFields.getSuperclass() != null) {
      getSuperClassFields(classToGetFields.getSuperclass(), fields);
    }
    Field[] superclassFields = classToGetFields.getDeclaredFields();
    for (final Field superclassField : superclassFields) {
      fields.add(superclassField);
    }
    superclassFields = null;
  }

  Vector getInstancesOf(final String classname) {
    if (classname == null) { return null; }
    final ClassProfiler.ClassProfile clzprofile = (ClassProfiler.ClassProfile)classProfiles.get(classname);
    final Vector instanceVector = new Vector();
    if (clzprofile != null) {
      final Enumeration instancesList = clzprofile.classInstances.elements();
      while (instancesList.hasMoreElements()) {
        instanceVector.add((instancesList.nextElement()));
      }
    }

    return instanceVector;
  }

  public int sizeof(final Class paramClass, final Object paramObject) {
    Field[] arrayOfField = paramClass.getDeclaredFields();
    int i = 0;
    for (final Field localField : arrayOfField) {
      Class localClass = localField.getType();
      localField.getName();
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
      }
      else if (localClass == Integer.TYPE) {
        i += 4;
      }
      else if (localClass == Short.TYPE) {
        i += 4;
      }
      else if (localClass == Byte.TYPE) {
        i += 4;
      }
      else if (localClass == Boolean.TYPE) {
        i += 4;
      }
      else {
        i += 4;
      }
      localClass = null;
    }
    if (paramObject != null) {
      i += 15;
      if (paramClass == long[].class) {
        i += 8 * Array.getLength(paramObject);
      }
      else if (paramClass == int[].class) {
        i += 4 * Array.getLength(paramObject);
      }
      else if (paramClass == short[].class) {
        i += 2 * Array.getLength(paramObject);
      }
      else if (paramClass == byte[].class) {
        i += Array.getLength(paramObject);
      }
      else if (paramClass == boolean[].class) {
        i += 4 * Array.getLength(paramObject);
      }
      else if (paramClass == String.class) {
        i += 2 * ((String)paramObject).length();
      }
      else if (paramClass == Image.class) {
        final Image localImage = (Image)paramObject;
        i += localImage.getWidth() * localImage.getHeight() * 2;
      }
      else if (paramClass.isArray()) {
        i += 4 * Array.getLength(paramObject);
      }
    }
    arrayOfField = null;

    return i;
  }

  int findClassPath(final String path) {
    int i = 0;
    int j = -1;
    do {
      j = path.indexOf('.', j + 1);
      i++;
    }
    while (j >= 0);

    return i;
  }

  class ClassProfile implements Comparable {

    public Vector classInstances = new Vector();
    private int staticClassSize = 0;
    public String name;

    ClassProfile(final String classname) {
      name = classname;
      try {
        staticClassSize = sizeof(Class.forName(classname, false, MobiEmulator.mobiEmulatorInstance.multiClassLoader), null);
      }
      catch (final Exception localException) {
        localException.printStackTrace();
      }
    }

    public int getSizeOfInstances() {
      int i = staticClassSize;
      Enumeration localEnumeration = classInstances.elements();
      while (localEnumeration.hasMoreElements()) {
        i += ((Instance)localEnumeration.nextElement()).totalSize;
      }
      localEnumeration = null;

      return i;
    }

    @Override
    public String toString() {
      return "";
    }

    @Override
    public int compareTo(final Object obj) {
      return name.compareTo(((ClassProfile)obj).name);
    }

    public void deInitialize() {
      classInstances.removeAllElements();
      classInstances = null;
      name = null;
      staticClassSize = 0;
    }
  }

  class Instance implements Comparable {

    Object obj;
    String path;
    int totalSize;

    public Instance(final String packagePath, final Object classObj) {
      path = packagePath;
      obj = classObj;
      totalSize = sizeof(classObj.getClass(), classObj);
    }

    @Override
    public int compareTo(final Object o) {
      return totalSize - ((Instance)o).totalSize;
    }
  }

}
