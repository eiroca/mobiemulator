package com.mascotcapsule.micro3d.v3;

public class Effect3D {

  public static final int NORMAL_SHADING = 0;
  public static final int TOON_SHADING = 1;

  public Effect3D() {
  }

  public Effect3D(final Light paramLight, final int paramInt, final boolean paramBoolean, final Texture paramTexture) {
  }

  public final Light getLight() {
    return null;
  }

  public final void setLight(final Light paramLight) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final int getShading() {
    return 0;
  }

  public final int getShadingType() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setShading(final int paramInt) {
  }

  public final void setShadingType(final int paramInt) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final int getThreshold() {
    return 0;
  }

  public final int getToonThreshold() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final int getThresholdHigh() {
    return 0;
  }

  public final int getToonHigh() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final int getThresholdLow() {
    return 0;
  }

  public final int getToonLow() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setThreshold(final int paramInt1, final int paramInt2, final int paramInt3) {
  }

  public final void setToonParams(final int paramInt1, final int paramInt2, final int paramInt3) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final boolean isSemiTransparentEnabled() {
    return false;
  }

  public final boolean isTransparency() {
    return false;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setSemiTransparentEnabled(final boolean paramBoolean) {
  }

  public final void setTransparency(final boolean paramBoolean) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final Texture getSphereMap() {
    return null;
  }

  public final Texture getSphereTexture() {
    return null;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setSphereMap(final Texture paramTexture) {
  }

  public final void setSphereTexture(final Texture paramTexture) {
  }

}
