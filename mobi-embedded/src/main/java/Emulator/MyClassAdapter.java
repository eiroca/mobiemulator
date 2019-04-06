package Emulator;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class MyClassAdapter extends ClassVisitor {

  public static boolean invokeStaticInit;
  public static boolean staticInitFound;
  private String methoddesc;
  private String methodname;
  private String name;
  private static MyMethodAdapter methodAdapter;

  public MyClassAdapter(final ClassVisitor cv) {
    super(Opcodes.ASM4);
    this.cv = cv;
  }

  @Override
  public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
    this.name = name;
    super.visit(version, access, name, signature, superName, interfaces);
    //      if ((access & 0x200) == 512) {
    //          invokeStaticInit = false;
    //      }
  }

  @Override
  public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
    //      if (invokeStaticInit) {
    //          access &= -7;
    //          access |= 1;
    //      }
    return super.visitField(access, name, desc, signature, value);
  }

  @Override
  public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
    methodname = name;
    methoddesc = desc;
    //      if ((invokeStaticInit) && (name.equals("<clinit>"))) {
    //          System.out.println("access=" + access + " name=" + name + " desc=" + desc + " signature=" + signature + " ex=" + exceptions);
    //          name = "static_init";
    //          access |= 1;
    //          staticInitFound = true;
    //      }
    MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
    MyClassAdapter.methodAdapter = null;
    MyClassAdapter.methodAdapter = new MyMethodAdapter(this, mv, access, this.name, methodname, methoddesc);
    mv = null;
    return MyClassAdapter.methodAdapter;
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }

}
