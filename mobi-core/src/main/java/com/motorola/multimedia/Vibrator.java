package com.motorola.multimedia;

import java.util.TimerTask;

public class Vibrator extends TimerTask {

  public static final int MAX_VIBRATE_TIME = 300000;
  public static final int MIN_PAUSE_TIME = 2000;
  public static final int VIBRATE_SILENT = 0;
  public static final int VIBRATE_SHORT = 1;
  public static final int VIBRATE_LONG = 2;
  public static final int VIBRATE_2SHORT = 3;
  public static final int VIBRATE_SHORT_LONG = 4;
  public static final int VIBRATE_PULSE = 5;

  @Override
  public void run() {
  }

  public static void vibrateFor(final int timeInMs) {
  }

  public static void vibratePeriodically(final int timeInMs) {
  }

  public static void vibratePeriodically(final int timeOnInMs, final int timeOffInMs) {
  }

  public static void vibratorOff() {
  }

  public static void vibratorOn() {
  }

  public static void setVibrateTone(final int tone) {
  }

  private static void vibratorStart() {
  }

}
