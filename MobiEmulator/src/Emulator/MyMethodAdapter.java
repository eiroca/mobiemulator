/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package Emulator;

//~--- non-JDK imports --------------------------------------------------------

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Hashtable;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Ashok Kumar Gujarathi
 */
class MyMethodAdapter extends MethodAdapter {
    public static Hashtable classMapping;
    public static Hashtable methodMapping;
    private static String className = "java/lang/Class";
    private static String resourceMethodName = "getResourceAsStream";
    private static String customClassName = "Emulator/CustomMethod";
    private static String beginMethod = "beginMethod";
    private static String endMethod = "endMethod";
    private static String newInstanceString = "newInstance";
    private static String customMethodParams = "(Ljava/lang/String;)V";
    private String formatedMethodName = null;
    private String methodpath = null;
    private int access;
    String classname;
    private int lineno;
    String methoddesc;
    String methodname;

    static {
        methodMapping = new Hashtable();
        classMapping = new Hashtable();
        classMapping.put("java/lang/Class", "Emulator/MobiEmulator");
        classMapping.put("java/lang/System", "Emulator/CustomMethod");
        classMapping.put("java/lang/Thread", "Emulator/CustomMethod");
        methodMapping.put("getResourceAsStream", "getResourceAsStream");
        methodMapping.put("gc", "gc");
        methodMapping.put("sleep", "sleep");
        methodMapping.put("yield", "yield");
        methodMapping.put("currentTimeMillis", "currentTimeMillis");
    }


    public MyMethodAdapter(MyClassAdapter aThis, MethodVisitor mv, int access, String classname, String methodName,
                           String methoddesc) {
        super(mv);
        this.access = access;
        this.classname = classname;
        this.methodname = methodName;
        this.methoddesc = methoddesc;
        formatedMethodName = formatmethodName(this.methodname, this.methoddesc);
        methodpath = this.classname + "." + this.methodname + this.methoddesc;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        // ((MethodFrame.Method)MethodFrame.methods.get(methodpath)).refCount+=1;
        if (classMapping.containsKey(owner) && methodMapping.containsKey(name)) {
            super.visitMethodInsn(Opcodes.INVOKESTATIC /* invokestatic */, (String) classMapping.get(owner), (String) methodMapping.get(name),
                    desc);
            if ((owner.equals(className)) && (name.equals(resourceMethodName))) {
                super.visitInsn(Opcodes.SWAP);
                super.visitInsn(Opcodes.POP);
            }
            return;
        }

//        if ((owner.equals("java/lang/Class")) && (name.equals("getResourceAsStream"))) {
//            super.visitMethodInsn(Opcodes.INVOKESTATIC /* invokestatic */, "Emulator/MobiEmulator", "getResourceAsStream",
//                                  desc);
//            super.visitInsn(Opcodes.SWAP);
//            super.visitInsn(Opcodes.POP);
//
//            return;
//        }
//        if ((owner.equals("java/lang/System")) && (name.equals("gc"))) {
//            super.visitMethodInsn(Opcodes.INVOKESTATIC /* invokestatic */, "Emulator/CustomMethod", "gc", desc);
//            // super.visitInsn(Opcodes.SWAP);
//            // super.visitInsn(Opcodes.POP);
//
//            return;
//        }
//        if ((owner.equals("java/lang/Thread")) && (name.equals("yield"))) {
//            super.visitMethodInsn(Opcodes.INVOKESTATIC /* invokestatic */, "Emulator/CustomMethod", "yield", desc);
//            // super.visitInsn(Opcodes.SWAP);
//            // super.visitInsn(Opcodes.POP);
//
//            return;
//        }
//        if ((owner.equals("java/lang/Class")) && (name.equals("currentTimeMillis"))) {
//            super.visitMethodInsn(Opcodes.INVOKESTATIC /* invokestatic */, "Emulator/CustomMethod",
//                                  "currentTimeMillis", desc);
//            // super.visitInsn(Opcodes.SWAP);
//            // super.visitInsn(Opcodes.POP);
//
//            return;
//        }
        // System.out.println("owner >> " + owner + " name >> " + name + ">> " + desc);
//      int val=0;
//      if(MobiEmulator.mobiEmulatorInstance.profilerframe.methods.containsKey(name+">"+desc))
//      {
//          val=(Integer)MobiEmulator.mobiEmulatorInstance.profilerframe.methods.get(name+">"+desc);
//      }
//      MobiEmulator.mobiEmulatorInstance.profilerframe.methods.put(name+">"+desc, ++val);
//      //System.out.println("begin of method "+owner+"->"+name);
//         if (owner.equals("java/lang/String"))
//    {
//      if ((name.equals("<init>")) && (desc.startsWith("([B")) && (!desc.endsWith("Ljava/lang/String;)V")))
//      {
//        //a(1);
//       // super.visitLdcInsn();
//        super.visitMethodInsn(opcode, owner, name, desc.substring(0, desc.length() - 2) + "Ljava/lang/String;)V");
//        return;
//      }
//      if ((name.equals("getBytes")) && (desc.startsWith("()")))
//      {
//        //a(1);
//       // super.visitLdcInsn(j.jdField_a_of_type_JavaLangString);
//        super.visitMethodInsn(opcode, owner, name, "(Ljava/lang/String;)[B");
//        return;
//      }
//    }
        super.visitMethodInsn(opcode, owner, name, desc);
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 1, maxLocals);
    }

    @Override
    public void visitLineNumber(int lineno, Label first) {
        super.visitLineNumber(lineno, first);
        this.lineno = lineno;
    }

    @Override
    public void visitCode() {
        // System.out.println("claname "+this.classname+" methodname "+this.methodname+" desc "+methoddesc);
        if (MobiEmulator.mobiEmulatorInstance.monitorMethodCalls && (access & Opcodes.ACC_INTERFACE) == 0) {
            super.visitLdcInsn(methodpath);
            visitMethodInsn(Opcodes.INVOKESTATIC, customClassName, beginMethod, customMethodParams);
        }
        super.visitCode();
    }

    public final void visitIntInsn(int Opcode, int paramInt2) {
        if (MobiEmulator.mobiEmulatorInstance.monitorNewTraces && (Opcode == Opcodes.NEWARRAY)) {
            switch (paramInt2) {
                case 4:
                    System.out.println("new boolean[]" + " at line " + lineno + " from class " + this.classname + "."
                            + formatedMethodName);

                    break;
                case 5:
                    System.out.println("new char[]" + " at line " + lineno + "from class " + this.classname + "."
                            + formatedMethodName);

                    break;
                case 6:
                    System.out.println("new float[]" + " at line " + lineno + " created from class " + this.classname + "."
                            + formatedMethodName);

                    break;
                case 7:
                    System.out.println("new double[]" + " at line " + lineno + " created from class " + this.classname
                            + "." + formatedMethodName);

                    break;
                case 8:
                    System.out.println("new byte[]" + " at line " + lineno + " created from class " + this.classname + "."
                            + formatedMethodName);

                    break;
                case 9:
                    System.out.println("new short[]" + " at line " + lineno + " created from class " + this.classname + "."
                            + formatedMethodName);

                    break;
                case 10:
                    System.out.println("new int[]" + " at line " + lineno + " from class " + this.classname + "."
                            + formatedMethodName);

                    break;
                case 11:
                    System.out.println("new long[]" + " at line " + lineno + " from class " + this.classname + "."
                            + formatedMethodName);
            }
        }
        super.visitIntInsn(Opcode, paramInt2);
    }

    @Override
    public void visitTypeInsn(int opcode, String Type) {
        if (MobiEmulator.mobiEmulatorInstance.monitorNewTraces) {
            if (opcode == Opcodes.NEW) {
                super.visitLdcInsn(new Integer(opcode));
                super.visitLdcInsn(Type + " created from class " + this.classname + "." + formatedMethodName);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, customClassName, newInstanceString,
                        customMethodParams);
            }
        }
        super.visitTypeInsn(opcode, Type);
    }

    @Override
    public void visitInsn(int opcode) {
        if (MobiEmulator.mobiEmulatorInstance.monitorMethodCalls) {
            switch (opcode) {
                case Opcodes.ARETURN:
                case Opcodes.DRETURN:
                case Opcodes.FRETURN:
                case Opcodes.IRETURN:
                case Opcodes.LRETURN:
                case Opcodes.RETURN:
                case Opcodes.ATHROW:
                    if (MobiEmulator.mobiEmulatorInstance.monitorMethodCalls && (access & Opcodes.ACC_INTERFACE) == 0) {
                        this.visitLdcInsn(methodpath);
                        this.visitMethodInsn(Opcodes.INVOKESTATIC, customClassName, endMethod,
                                customMethodParams);
                    }

                    break;
                default:
                    break;
            }
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitEnd() {
        // System.out.println("end of method");
        // System.out.println("" +getText());
        // more memory taking
//      if(MobiEmulator.mobiEmulatorInstance.monitorMethodCalls)
//      {
//          ((MethodFrame.Method)MethodFrame.methods.get(methodpath)).bytecode=getText();
//      }
        super.visitEnd();
    }

    private String formatmethodName(String methodname, String methoddesc) {
        org.objectweb.asm.commons.Method m = new org.objectweb.asm.commons.Method(methodname, methoddesc);
        StringBuffer sb = new StringBuffer();
        sb.append(m.getReturnType().getClassName()).append(" ").append(methodname).append("(");
        Type[] paramTypes = m.getArgumentTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            sb.append(paramTypes[i].getClassName()).append((paramTypes[i].getSize() > 1) ? "[]"
                    : "").append((i < paramTypes.length - 1) ? ", "
                    : "");
        }
        sb.append(")");
        m = null;
        paramTypes = null;

        return sb.toString();
    }
}
