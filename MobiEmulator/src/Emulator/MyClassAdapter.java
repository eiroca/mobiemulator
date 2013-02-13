/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- non-JDK imports --------------------------------------------------------

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class MyClassAdapter extends ClassAdapter {
    public static boolean invokeStaticInit;
    public static boolean staticInitFound;
    private String        methoddesc;
    private String        methodname;
    private String        name;
    private static MyMethodAdapter methodAdapter;
    public MyClassAdapter(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.name = name;
        super.visit(version, access, name, signature, superName, interfaces);
//      if ((access & 0x200) == 512) {
//          invokeStaticInit = false;
//      }
    }
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
//      if (invokeStaticInit) {
//          access &= -7;
//          access |= 1;
//      }
        return super.visitField(access, name, desc, signature, value);
    }
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        this.methodname = name;
        this.methoddesc = desc;
//      if ((invokeStaticInit) && (name.equals("<clinit>"))) {
//          System.out.println("access=" + access + " name=" + name + " desc=" + desc + " signature=" + signature + " ex=" + exceptions);
//          name = "static_init";
//          access |= 1;
//          staticInitFound = true;
//      }
        MethodVisitor mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
        methodAdapter=null;
        methodAdapter=new MyMethodAdapter(this, mv, access, this.name, this.methodname, this.methoddesc);
        mv=null;
        return methodAdapter;
    }
    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
