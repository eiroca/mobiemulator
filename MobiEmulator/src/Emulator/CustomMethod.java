/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- non-JDK imports --------------------------------------------------------

import Emulator.MethodFrame.Method;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class CustomMethod {
    public static void gc() {
        Profiler.SystemGCcount += 1;
    }
    public static void yield() throws InterruptedException {
        Profiler.yieldCallCount += 1;
        Thread.yield();
    }
    public static void sleep(long ms) throws InterruptedException {
        Profiler.sleepcallCount    += 1;
        Profiler.sleepTotalmsCount += ms;
        Thread.sleep(ms);
    }
    public static long currentTimeMillis() {
        Profiler.currentTimeMillisCallCount += 1;
        return System.currentTimeMillis();
//      jdField_a_of_type_Long += j.f * (l - b);
//      b = l;
        // return l;
    }
    public static void beginMethod(String methodPath) {
        MethodFrame.Method mthd = null;
        mthd = (MethodFrame.Method) MethodFrame.methods.get(methodPath);
//        System.out.println("Method Begin"+methodPath);
        if (mthd != null) {
            // int i = a();
            mthd.entrycount += 1;
            mthd.callCount  += 1;
            mthd.beginTime  = System.currentTimeMillis();
        }
        mthd = null;
    }
    public static void endMethod(String methodPath) {
//        System.out.println("Method End "+methodPath);
        Method mthd = null;
        mthd = (MethodFrame.Method) MethodFrame.methods.get(methodPath);
        if (mthd != null) {
            mthd.exitcount += 1;
            if (mthd.callCount > 0) {
                mthd.totalTime   += (System.currentTimeMillis() - mthd.beginTime);
                mthd.averageTime = ((float) mthd.totalTime / mthd.callCount);
            }
            // a();
        }
        mthd = null;
    }
    public static void newInstance(int opcode, String type) {
        System.out.println("New instance: " + type);
    }
}
