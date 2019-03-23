package com.nec.mascotcapsule.v3;

public class Effect3D {

  public static final int NORMAL_SHADING = 0;
  public static final int TOON_SHADING = 1;

  public Effect3D() {
  }

  public Effect3D(Light light, int shading, boolean isEnableTrans, Texture tex) {
  }

  public final Light getLight() {
    return null;
  }

  public final void setLight(Light light1) {
  }

  /**
   * @deprecated
   */
  public final int getShading() {
    return 0;
  }

  public final int getShadingType() {
    return 0;
  }

  /**
   * @deprecated
   */
  public final void setShading(int i) {
  }

  public final void setShadingType(int i) {
  }

  /**
   * @deprecated
   */
  public final int getThreshold() {
    return 0;
  }

  public final int getToonThreshold() {
    return 0;
  }

  /**
   * @deprecated
   */
  public final int getThresholdHigh() {
    return 0;
  }

  public final int getToonHigh() {
    return 0;
  }

  /**
   * @deprecated
   */
  public final int getThresholdLow() {
    return 0;
  }

  public final int getToonLow() {
    return 0;
  }

  /**
   * @deprecated
   */
  public final void setThreshold(int i, int j, int k) {
  }

  public final void setToonParams(int i, int j, int k) {
  }

  /**
   * @deprecated
   */
  public final boolean isSemiTransparentEnabled() {
    return false;
  }

  public final boolean isTransparency() {
    return false;
  }

  /**
   * @deprecated
   */
  public final void setSemiTransparentEnabled(boolean flag) {
  }

  public final void setTransparency(boolean flag) {
  }

  /**
   * @deprecated
   */
  public final Texture getSphereMap() {
    return null;
  }

  public final Texture getSphereTexture() {
    return null;
  }

  /**
   * @deprecated
   */
  public final void setSphereMap(Texture texture) {
  }

  public final void setSphereTexture(Texture texture) {
  }

}
