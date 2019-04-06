package com.motorola.funlight;

public class FunLight {

  public static final int BLACK = 0;
  public static final int BLANK = 0;
  public static final int BLUE = 255;
  public static final int CYAN = 65535;
  public static final int GREEN = 65280;
  public static final int MAGENTA = 16711935;
  public static final int OFF = 0;
  public static final int ON = 16777215;
  public static final int RED = 16711680;
  public static final int WHITE = 16777215;
  public static final int YELLOW = 16776960;
  public static final int QUEUED = 1;
  public static final int SUCCESS = 0;
  public static final int IGNORED = 2;

  public static int getControl() {
    return 0;
  }

  public static Region getRegion(final int i) {
    return null;
  }

  public static int[] getRegionsIDs() {
    return new int[1];
  }

  public static Region[] getRegions() {
    return null;
  }

  public static void releaseControl() {
  }

  public static int setColor(final int i) {
    return 0;
  }

  public static int setColor(final byte byte0, final byte byte1, final byte byte2) {
    return 0;
  }

}
