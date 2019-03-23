package com.mascotcapsule.micro3d.v3;

public class Effect3D {

  public static final int NORMAL_SHADING = 0;
  public static final int TOON_SHADING = 1;

  public Effect3D() {
  }

  public Effect3D(Light paramLight, int paramInt, boolean paramBoolean, Texture paramTexture) {
  }

  public final Light getLight() {
    return null;
  }

  public final void setLight(Light paramLight) {
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
  public final void setShading(int paramInt) {
  }

  public final void setShadingType(int paramInt) {
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
  public final void setThreshold(int paramInt1, int paramInt2, int paramInt3) {
  }

  public final void setToonParams(int paramInt1, int paramInt2, int paramInt3) {
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
  public final void setSemiTransparentEnabled(boolean paramBoolean) {
  }

  public final void setTransparency(boolean paramBoolean) {
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
  public final void setSphereMap(Texture paramTexture) {
  }

  public final void setSphereTexture(Texture paramTexture) {
  }

}
