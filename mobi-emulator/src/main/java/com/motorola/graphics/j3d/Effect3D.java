package com.motorola.graphics.j3d;

public class Effect3D {

  public static final int NORMAL_SHADING = 0;
  public static final int TOON_SHADING = 1;

  public Effect3D() {
  }

  public Effect3D(Light light, int i, boolean flag, Texture texture) {
  }

  public Light getLight() {
    return null;
  }

  public void setLight(Light light) {
  }

  public int getShading() {
    return 0;
  }

  public void setShading(int i) {
  }

  public int getThreshold() {
    return 0;
  }

  public int getThresholdHigh() {
    return 0;
  }

  public int getThresholdLow() {
    return 0;
  }

  public void setThreshold(int i, int j, int k) {
  }

  public boolean isSemiTransparentEnabled() {
    return false;
  }

  public void setSemiTransparentEnabled(boolean flag) {
  }

  public Texture getSphereMap() {
    return null;
  }

  public void setSphereMap(Texture texture) {
  }

}
