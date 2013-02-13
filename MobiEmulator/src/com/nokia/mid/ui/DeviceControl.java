/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.nokia.mid.ui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class DeviceControl {
    static javax.microedition.lcdui.Display DeviceDisplay = new javax.microedition.lcdui.Display();

    public static int getUserInactivityTime() {
        return 0;
    }

    public static void resetUserInactivityTime() {}

    public static void setLights(int aNum, int aLevel) {}

    public static void flashLights(long aDuration) {}

    public static void startVibra(int aFreq, long aDuration) {
        DeviceDisplay.vibrate((int) aDuration);
    }

    public static void stopVibra() {
        DeviceDisplay.vibrate(0);
    }
}
