/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- non-JDK imports --------------------------------------------------------

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

//~--- JDK imports ------------------------------------------------------------
/**
 *
 * @author Ashok Kumar Gujarathi
 */

public class MultiClassLoader extends ClassLoader {
    private static JarResources jarresources;
    // ---------- Fields --------------------------------------
    private Hashtable           classes     = new Hashtable();
    ClassReader                 cr          = null;
    ClassWriter                 cw          = null;
    MyClassAdapter              mc          = null;
    protected boolean           monitorOn   = false;
    private int                 readercount = 0;
    private Class               resultClass = null;
    private char                classNameReplacementChar;
    public byte[]               classbytes;
    // ---------- Initialization ------------------------------
    public MultiClassLoader() {
        // super(MultiClassLoader.class.getClassLoader());
        //loadDefaultClasses();
    }

    public void loadDefaultClasses() {
        try {
            classes.put("java.io.ByteArrayInputStream", java.io.ByteArrayInputStream.class);
            classes.put("java.io.ByteArrayOutputStream", java.io.ByteArrayOutputStream.class);
            classes.put("java.io.DataInputStream", java.io.DataInputStream.class);
            classes.put("java.io.DataOutputStream", java.io.DataOutputStream.class);
            classes.put("java.io.InputStream", java.io.InputStream.class);
            classes.put("java.io.InputStreamReader", java.io.InputStreamReader.class);
            classes.put("java.io.OutputStream", java.io.OutputStream.class);
            classes.put("java.io.OutputStreamWriter", java.io.OutputStreamWriter.class);
            classes.put("java.io.PrintStream", java.io.PrintStream.class);
            classes.put("java.io.Reader", java.io.Reader.class);
            classes.put("java.io.Writer", java.io.Writer.class);
            classes.put("java.io.EOFException", java.io.EOFException.class);
            classes.put("java.io.InterruptedIOException", java.io.InterruptedIOException.class);
            classes.put("java.io.IOException", java.io.IOException.class);
            classes.put("java.io.UnsupportedEncodingException", java.io.UnsupportedEncodingException.class);
            classes.put("java.io.UTFDataFormatException", java.io.UTFDataFormatException.class);
            classes.put("java.util.Calendar", java.util.Calendar.class);
            classes.put("java.util.Date", java.util.Date.class);
            classes.put("java.util.Hashtable", java.util.Hashtable.class);
            classes.put("java.util.Random", java.util.Random.class);
            classes.put("java.util.Stack", java.util.Stack.class);
            classes.put("java.util.Timer", java.util.Timer.class);
            classes.put("java.util.TimerTask", java.util.TimerTask.class);
            classes.put("java.util.TimeZone", java.util.TimeZone.class);
            classes.put("java.util.Vector", java.util.Vector.class);
            classes.put("java.util.Enumeration", java.util.Enumeration.class);
            classes.put("java.lang.Boolean", java.lang.Boolean.class);
            classes.put("java.lang.Byte", java.lang.Byte.class);
            classes.put("java.lang.Character", java.lang.Character.class);
            classes.put("java.lang.Class", java.lang.Class.class);
            classes.put("java.lang.Double", java.lang.Double.class);
            classes.put("java.lang.Float", java.lang.Float.class);
            classes.put("java.lang.Integer", java.lang.Integer.class);
            classes.put("java.lang.Long", java.lang.Long.class);
            classes.put("java.lang.Math", java.lang.Math.class);
            classes.put("java.lang.Object", java.lang.Object.class);
            classes.put("java.lang.Runtime", java.lang.Runtime.class);
            classes.put("java.lang.Short", java.lang.Short.class);
            classes.put("java.lang.String", java.lang.String.class);
            classes.put("java.lang.StringBuffer", java.lang.StringBuffer.class);
            classes.put("java.lang.System", java.lang.System.class);
            classes.put("java.lang.Thread", java.lang.Thread.class);
            classes.put("java.lang.Throwable", java.lang.Throwable.class);
            classes.put("java.lang.ArithmeticException", java.lang.ArithmeticException.class);
            classes.put("java.lang.ArrayIndexOutOfBoundsException", java.lang.ArrayIndexOutOfBoundsException.class);
            classes.put("java.lang.ArrayStoreException", java.lang.ArrayStoreException.class);
            classes.put("java.lang.ClassCastException", java.lang.ClassCastException.class);
            classes.put("java.lang.ClassNotFoundException", java.lang.ClassNotFoundException.class);
            classes.put("java.lang.Exception", java.lang.Exception.class);
            classes.put("java.lang.IllegalAccessException", java.lang.IllegalAccessException.class);
            classes.put("java.lang.IllegalArgumentException", java.lang.IllegalArgumentException.class);
            classes.put("java.lang.IllegalMonitorStateException", java.lang.IllegalMonitorStateException.class);
            classes.put("java.lang.IllegalStateException", java.lang.IllegalStateException.class);
            classes.put("java.lang.IllegalThreadStateException", java.lang.IllegalThreadStateException.class);
            classes.put("java.lang.IndexOutOfBoundsException", java.lang.IndexOutOfBoundsException.class);
            classes.put("java.lang.InstantiationException", java.lang.InstantiationException.class);
            classes.put("java.lang.InterruptedException", java.lang.InterruptedException.class);
            classes.put("java.lang.NegativeArraySizeException", java.lang.NegativeArraySizeException.class);
            classes.put("java.lang.NullPointerException", java.lang.NullPointerException.class);
            classes.put("java.lang.NumberFormatException", java.lang.NumberFormatException.class);
            classes.put("java.lang.RuntimeException", java.lang.RuntimeException.class);
            classes.put("java.lang.SecurityException", java.lang.SecurityException.class);
            classes.put("java.lang.StringIndexOutOfBoundsException", java.lang.StringIndexOutOfBoundsException.class);
            classes.put("java.lang.Runnable", java.lang.Runnable.class);
            classes.put("java.lang.Error", java.lang.Error.class);
            classes.put("java.lang.NoClassDefFoundError", java.lang.NoClassDefFoundError.class);
            classes.put("java.lang.OutOfMemoryError", java.lang.OutOfMemoryError.class);
            classes.put("java.lang.VirtualMachineError", java.lang.VirtualMachineError.class);
        } catch (Exception e) {}
    }
    // ---------- Superclass Overrides ------------------------

    /**
     * This is a simple version for external clients since they will always want
     * the class resolved before it is returned to them.
     */
    @Override
    public Class loadClass(String className) throws ClassNotFoundException {
        if (monitorOn) {
            System.out.println("loading " + className + ".class");
        }

        return (myloadClass(className, true));
    }
    public void setCacheJarResources(JarResources jr) {
        jarresources = jr;
    }
    // ---------- Abstract Implementation ---------------------
    public Class myloadClass(String className, boolean resolveIt) throws ClassNotFoundException {
        // byte[] classBytes=null;
        monitor(">> MultiClassLoader.loadClass(" + className + ", " + resolveIt + ")");
        // ----- Check our local cache of classes
        resultClass = (Class) classes.get(className);
        if (resultClass != null) {
            monitor(">> returning cached result.");

            return resultClass;
        }
        // ----- Check with the primordial class loader
//      try { result =
//      super.findSystemClass(className);
//      monitor(">> returning system class (in CLASSPATH)."); return result; }
//      catch (ClassNotFoundException e) { monitor(">> Not a system class."); }
//      
        // ----- Try to load it from preferred source // Note loadClassBytes() is an abstract method
        resultClass = findClass(className);
        if (classbytes == null) {
            throw new ClassNotFoundException();
        }
        // ----- Define it (parse the class file)
        // result = defineClass(className,classBytes, 0, classBytes.length);
        if (resultClass == null) {
            throw new ClassFormatError();
        }
        // ----- Resolve if necessary
//      if (resolveIt) {
//          resolveClass(resultClass);
//      // Done 
//      }
        classes.put(className, resultClass);
        resultClass = null;
        monitor(">> Returning newly loaded class.");

        return (Class) classes.get(className);
    }
    // ---------- Public Methods ------------------------------

    /**
     * This optional call allows a class name such as "COM.test.Hello" to be
     * changed to "COM_test_Hello", which is useful for storing classes from
     * different packages in the same retrival directory. In the above example
     * the char would be '_'.
     */
    public void setClassNameReplacementChar(char replacement) {
        classNameReplacementChar = replacement;
    }
    // ---------- Protected Methods ---------------------------
    public Class loadClassBytes(String className) {    // System.out.println("jar resources is null"+(jarresources==null));
        // className = formatClassName (className);
        if ((jarresources != null) && (jarresources.getResource(className.replace('.', '/') + ".class") != null)) {
            // System.out.println("in jar loading class");
            classbytes = jarresources.getResource(className.replace('.', '/') + ".class");
            try {
                this.MyClassAdapt(new ByteArrayInputStream(classbytes));
            } catch (IOException ex) {
                Logger.getLogger(MultiClassLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
            resultClass = defineClass(className, classbytes, 0, classbytes.length);

            return resultClass;
        } else {
            try {
                return findClass(className);
            } catch (Exception ex) {
                Logger.getLogger(MultiClassLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }
    @Override
    protected Class findClass(String className) throws ClassNotFoundException {
        try {
            String replaceClassName = className.replace('.', '/') + ".class";
            if ((jarresources != null) && MobiEmulator.mobiEmulatorInstance.cacheJarResources
                    && (jarresources.getResource(className.replace('.', '/') + ".class") != null)) {
                ByteArrayInputStream bais = new ByteArrayInputStream(jarresources.getResource(replaceClassName));
                this.MyClassAdapt(bais);
                if (bais != null) {
                    bais.close();
                }
                bais             = null;
                replaceClassName = null;
                resultClass      = defineClass(className, classbytes, 0, classbytes.length);

                return resultClass;
            } else if (MobiEmulator.mobiEmulatorInstance.jarfile != null) {
                JarFile  localJarFile  = MobiEmulator.mobiEmulatorInstance.jarfile;
                ZipEntry localZipEntry = localJarFile.getEntry(replaceClassName);
                if (localZipEntry != null) {
                    InputStream localInputStream = localJarFile.getInputStream(localZipEntry);
                    this.MyClassAdapt(localInputStream);
                    /*
                     * ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     * byte[] data = new byte[512]; while
                     * (localInputStream.available() > 0) { int count =
                     * localInputStream.read(data); baos.write(data, 0, count);
                     * } localInputStream.close(); byte[] arrayOfByte =
                     * baos.toByteArray();
                     */
                    localJarFile  = null;
                    localZipEntry = null;
                    if (localInputStream != null) {
                        localInputStream.close();
                    }
                    localInputStream = null;
                    System.out.println("loading class from jar >>> " + className + ".class");
                    replaceClassName = null;
                    resultClass      = defineClass(className, classbytes, 0, classbytes.length);

                    return resultClass;
                } else {
                    // search classpath for external jars
                    // return super.findSystemClass(className);
                    if ((MobiEmulator.mobiEmulatorInstance.options.customJarPaths != null)
                            && (MobiEmulator.mobiEmulatorInstance.options.customJarPaths.length > 0)) {
                        int len = MobiEmulator.mobiEmulatorInstance.options.customJarPaths.length;
                        for (int i = 0;i < len;i++) {
                            JarFile  customjf         = new JarFile(MobiEmulator.mobiEmulatorInstance.options.customJarPaths[i]);
                            ZipEntry customClassEntry = customjf.getEntry(replaceClassName);
                            if (customClassEntry != null) {
                                InputStream is = customjf.getInputStream(customClassEntry);
                                this.MyClassAdapt(is);
                                customjf         = null;
                                customClassEntry = null;
                                if (is != null) {
                                    is.close();
                                }
                                is = null;
                                System.out.println("loading class from custom jar >>> " + className + ".class");
                                replaceClassName = null;
                                resultClass      = defineClass(className, classbytes, 0, classbytes.length);

                                return resultClass;
                            }
                        }
                    }
                }
            }
        } catch (Exception localException) {
            System.out.println("Exception while loading " + className + ".class");
            localException.printStackTrace();
        }
        try {
            resultClass = super.findSystemClass(className);

            return resultClass;
//          classbytes = this.MyClassAdapt(is);
//          return defineClass(className, classbytes, 0, classbytes.length);
        } catch (ClassCastException ex) {
            Logger.getLogger(MultiClassLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    public void MyClassAdapt(InputStream in) throws IOException {
        cr = new ClassReader(in);
        // if(MobiEmulator.mobiEmulatorInstance.monitorMethodCalls||true)
        {
            ClassNode classNode = new ClassNode();
            cr.accept(classNode, 0);
            MobiEmulator.mobiEmulatorInstance.methodFrame.addMethods(classNode);
            classNode = null;
        }
        cr=null;
        in.reset();
        cr = new ClassReader(in);
        cw = new ClassWriter(0);
        mc = new MyClassAdapter(cw);
        cr.accept(mc, 0);

        this.classbytes = cw.toByteArray();
        cr              = null;
        cw              = null;
        mc              = null;
        // readercount++;
//        if (MobiEmulator.mobiEmulatorInstance.methodByteCode) {
//            ClassReader  cr1 = new ClassReader(in);
//            ClassWriter  cw1 = new ClassWriter(0);
//            ClassVisitor cv1 = new TraceClassAdapter(cw1);
//            cr1.accept(cv1, 0);
//            cr1 = null;
//            cw1 = null;
//            cv1 = null;
//        }
        in.close();
        in = null;
    }
    public int getTotalClassLoaded() {
        return readercount;
    }
    protected String formatClassName(String className) {
        if (classNameReplacementChar == '\u0000') {
            // '/' is used to map the package to the path
            return className.replace('.', '/') + ".class";
        } else {
            // Replace '.' with custom char, such as '_'
            return className.replace('.', classNameReplacementChar) + ".class";
        }
    }
    protected void monitor(String text) {
        if (monitorOn) {
            print(text);
        }
    }
    // --- Std
    protected static void print(String text) {
        System.out.println(text);
    }
    @Override
    public InputStream getResourceAsStream(String paramString) {
        try {
            return MobiEmulator.mobiEmulatorInstance.getResourceAsStream(paramString);
        } catch (IOException ex) {
            Logger.getLogger(MultiClassLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        // return super.getResourceAsStream(paramString);
        return null;
    }
    public void deInitialize() {
        classes.clear();
        classes      = null;
        classbytes   = null;
        jarresources = null;
//      cr.removeAllElements();
//      cw.removeAllElements();
//      mc.removeAllElements();
//      
        cw           = null;
        cr           = null;
        mc           = null;
    }
//    static class TraceClassAdapter extends ClassAdapter implements Opcodes {
//        private String owner;
//        public TraceClassAdapter(ClassVisitor cv) {
//            super(cv);
//        }
//
//        public void visit(int version, int access, String name, String signature, String superName,
//                          String[] interfaces) {
//            this.owner = name;
//            super.visit(version, access, name, signature, superName, interfaces);
//        }
//        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//            MethodVisitor mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
//
//            return (mv == null) ? null
//                                : new TraceCodeAdapter(mv, this.owner,
//                                (MethodFrame.Method) MethodFrame.methods.get(this.owner + '.' + name + desc));
//        }
//    }
//
//    static class TraceCodeAdapter extends TraceMethodVisitor implements Opcodes {
//        private MethodFrame.Method method;
//        private String             owner;
//        public TraceCodeAdapter(MethodVisitor mv, String owner, MethodFrame.Method method) {
//            super();
//            this.owner  = owner;
//            this.method = method;
//        }
//
//        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
//            MethodFrame.Method method = (MethodFrame.Method) MethodFrame.methods.get(owner + '.' + name + desc);
//            if (method != null) {
//                method.refCount += 1;
//            }
//            super.visitMethodInsn(opcode, owner, name, desc);
//        }
//        public void visitEnd() {
//            if (this.method != null) {
//                this.method.bytecode = getText();
//            }
//            super.visitEnd();
//        }
//    }
}    // End class

