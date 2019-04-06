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

  public static void start(final int duration, final int strength) {
    Vibration.DeviceDisplay = new javax.microedition.lcdui.Display();
    Vibration.DeviceDisplay.vibrate(duration);
  }

  public static void stop() {
    Vibration.DeviceDisplay.vibrate(0);
  }

}
