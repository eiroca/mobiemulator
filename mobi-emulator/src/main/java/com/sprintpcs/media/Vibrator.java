package com.sprintpcs.media;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Vibrator {

  public static void vibrate(final int duration) {
    final javax.microedition.lcdui.Display d = new javax.microedition.lcdui.Display();
    d.vibrate(duration);
  }

}
