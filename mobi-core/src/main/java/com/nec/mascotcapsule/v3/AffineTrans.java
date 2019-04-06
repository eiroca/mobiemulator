package com.nec.mascotcapsule.v3;

public class AffineTrans {

  public int m00;
  public int m01;
  public int m02;
  public int m03;
  public int m10;
  public int m11;
  public int m12;
  public int m13;
  public int m20;
  public int m21;
  public int m22;
  public int m23;

  public AffineTrans() {
  }

  public AffineTrans(final int m00, final int m01, final int m02, final int m03, final int m10, final int m11, final int m12, final int m13, final int m20, final int m21, final int m22, final int m23) {
  }

  public AffineTrans(final AffineTrans a) {
  }

  public AffineTrans(final int[][] a) {
  }

  public AffineTrans(final int[] a) {
  }

  public AffineTrans(final int[] a, final int offset) {
  }

  public final void get(final int[] ai) {
  }

  public final void get(final int[] ai, final int i) {
  }

  public final void set(final int[] ai, final int i) {
  }

  public final void set(final int i, final int j, final int k, final int l, final int i1, final int j1, final int k1, final int l1, final int i2, final int j2, final int k2, final int l2) {
  }

  public final void set(final AffineTrans affinetrans) {
  }

  public final void set(final int[][] ai) {
  }

  public final void set(final int[] ai) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final Vector3D transPoint(final Vector3D v) {
    return null;
  }

  public final Vector3D transform(final Vector3D v) {
    return null;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void multiply(final AffineTrans affinetrans) {
  }

  public final void mul(final AffineTrans affinetrans) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void multiply(final AffineTrans affinetrans, final AffineTrans affinetrans1) {
  }

  public final void mul(final AffineTrans affinetrans, final AffineTrans affinetrans1) {
  }

  public final void setRotationX(final int i) {
  }

  public final void setRotationY(final int i) {
  }

  public final void setRotationZ(final int i) {
  }

  public final void setIdentity() {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void rotationX(final int i) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void rotationY(final int i) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void rotationZ(final int i) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void rotationV(final Vector3D vector3d, final int i) {
  }

  public final void setRotation(final Vector3D vector3d, final int i) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setViewTrans(final Vector3D vector3d, final Vector3D vector3d1, final Vector3D vector3d2) {
  }

  public final void lookAt(final Vector3D vector3d, final Vector3D vector3d1, final Vector3D vector3d2) {
  }

}
