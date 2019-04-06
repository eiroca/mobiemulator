package com.nec.mascotcapsule.v3;

public class Light {

  public Light() {
  }

  public Light(final Vector3D dir, final int dirIntensity, final int ambIntensity) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final int getDirIntensity() {
    return 0;
  }

  public final int getParallelLightIntensity() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setDirIntensity(final int i) {
  }

  public final void setParallelLightIntensity(final int i) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final int getAmbIntensity() {
    return 0;
  }

  public final int getAmbientIntensity() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setAmbIntensity(final int i) {
  }

  public final void setAmbientIntensity(final int i) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final Vector3D getDirection() {
    return null;
  }

  public final Vector3D getParallelLightDirection() {
    return null;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setDirection(final Vector3D vector3d) {
  }

  public final void setParallelLightDirection(final Vector3D vector3d) {
  }

}
