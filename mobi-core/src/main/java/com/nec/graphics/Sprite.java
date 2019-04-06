package com.nec.graphics;

import javax.microedition.lcdui.Image;

public final class Sprite {

  protected int x;
  protected int y;
  protected int flags;
  protected int priority;
  protected boolean visible;
  protected Image image;
  static final int DEFAULT_PRIORITY = 15;

  public Sprite() {
    image = null;
  }

  public Sprite(final Image image) throws NullPointerException {
    this.image = null;
  }

  public Sprite(final Image image, final int x, final int y, final boolean visible) throws IllegalArgumentException, NullPointerException {
    this.image = null;
  }

  public void setLocation(final int i, final int j) throws IllegalArgumentException {
  }

  public void setImage(final Image image1) throws NullPointerException {
  }

  public void setVisible(final boolean flag) {
  }

  public boolean isVisible() {
    return false;
  }

}
