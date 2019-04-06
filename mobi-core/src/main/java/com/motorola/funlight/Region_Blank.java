package com.motorola.funlight;

class Region_Blank implements Region {

  @Override
  public int getID() {
    return 0;
  }

  @Override
  public int setColor(final int i) {
    return 1;
  }

  @Override
  public int setColor(final byte byte0, final byte byte1, final byte byte2) {
    return 1;
  }

  @Override
  public int getColor() {
    return 0;
  }

  @Override
  public int getControl() {
    return 1;
  }

  @Override
  public void releaseControl() {
  }

  @Override
  public String toString() {
    return "Blank";
  }

}
