/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.samsung.util;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Vibration {
    static javax.microedition.lcdui.Display DeviceDisplay;

    public static boolean isSupported() {
        return true;
    }

    public static void start(int duration, int strength) {
        DeviceDisplay = new javax.microedition.lcdui.Display();
        DeviceDisplay.vibrate(duration);
    }

    public static void stop() {
        DeviceDisplay.vibrate(0);
    }
}
