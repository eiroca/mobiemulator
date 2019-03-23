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

  public static void resetUserInactivityTime() {
  }

  public static void setLights(final int aNum, final int aLevel) {
  }

  public static void flashLights(final long aDuration) {
  }

  public static void startVibra(final int aFreq, final long aDuration) {
    DeviceControl.DeviceDisplay.vibrate((int)aDuration);
  }

  public static void stopVibra() {
    DeviceControl.DeviceDisplay.vibrate(0);
  }

}
