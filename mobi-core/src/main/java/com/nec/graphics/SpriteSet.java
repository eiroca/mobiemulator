package com.nec.graphics;

public final class SpriteSet {

  protected static int MAX_PRIORITY = 31;
  protected static int MAX_SPRITES = 32;

  protected int numSprites;

  protected Sprite[] spriteList;

  public SpriteSet(final int num) throws IllegalArgumentException {
  }

  public SpriteSet(final Sprite[] sprites, final int num) throws NullPointerException, IllegalArgumentException {
  }

  public int getCount() {
    return 0;
  }

  public Sprite[] getSprites() {
    return null;
  }

  public Sprite getSprite(final int index) throws ArrayIndexOutOfBoundsException {
    return null;
  }

  public void setCollisionAll() throws NullPointerException {
  }

  public void setCollisionOf(final int i) throws NullPointerException, IllegalArgumentException {
  }

  public boolean isCollision(final int index1, final int index2) throws ArrayIndexOutOfBoundsException {
    return false;
  }

  public int getCollisionFlag(final int index) throws ArrayIndexOutOfBoundsException {
    return 0;
  }

  public void setPriority(final int i, final int j) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
  }

}
