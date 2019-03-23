package com.nec.mascotcapsule.v3;

public class Light {

  public Light() {
  }

  public Light(Vector3D dir, int dirIntensity, int ambIntensity) {
  }

  /**
   * @deprecated
   */
  public final int getDirIntensity() {
    return 0;
  }

  public final int getParallelLightIntensity() {
    return 0;
  }

  /**
   * @deprecated
   */
  public final void setDirIntensity(int i) {
  }

  public final void setParallelLightIntensity(int i) {
  }

  /**
   * @deprecated
   */
  public final int getAmbIntensity() {
    return 0;
  }

  public final int getAmbientIntensity() {
    return 0;
  }

  /**
   * @deprecated
   */
  public final void setAmbIntensity(int i) {
  }

  public final void setAmbientIntensity(int i) {
  }

  /**
   * @deprecated
   */
  public final Vector3D getDirection() {
    return null;
  }

  public final Vector3D getParallelLightDirection() {
    return null;
  }

  /**
   * @deprecated
   */
  public final void setDirection(Vector3D vector3d) {
  }

  public final void setParallelLightDirection(Vector3D vector3d) {
  }

}
