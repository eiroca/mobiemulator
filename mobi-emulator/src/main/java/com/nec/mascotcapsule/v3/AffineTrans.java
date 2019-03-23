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

  public AffineTrans(int m00, int m01, int m02, int m03, int m10, int m11, int m12, int m13, int m20, int m21, int m22, int m23) {
  }

  public AffineTrans(AffineTrans a) {
  }

  public AffineTrans(int[][] a) {
  }

  public AffineTrans(int[] a) {
  }

  public AffineTrans(int[] a, int offset) {
  }

  public final void get(int[] ai) {
  }

  public final void get(int[] ai, int i) {
  }

  public final void set(int[] ai, int i) {
  }

  public final void set(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2) {
  }

  public final void set(AffineTrans affinetrans) {
  }

  public final void set(int[][] ai) {
  }

  public final void set(int[] ai) {
  }

  /**
   * @deprecated
   */
  public final Vector3D transPoint(Vector3D v) {
    return null;
  }

  public final Vector3D transform(Vector3D v) {
    return null;
  }

  /**
   * @deprecated
   */
  public final void multiply(AffineTrans affinetrans) {
  }

  public final void mul(AffineTrans affinetrans) {
  }

  /**
   * @deprecated
   */
  public final void multiply(AffineTrans affinetrans, AffineTrans affinetrans1) {
  }

  public final void mul(AffineTrans affinetrans, AffineTrans affinetrans1) {
  }

  public final void setRotationX(int i) {
  }

  public final void setRotationY(int i) {
  }

  public final void setRotationZ(int i) {
  }

  public final void setIdentity() {
  }

  /**
   * @deprecated
   */
  public final void rotationX(int i) {
  }

  /**
   * @deprecated
   */
  public final void rotationY(int i) {
  }

  /**
   * @deprecated
   */
  public final void rotationZ(int i) {
  }

  /**
   * @deprecated
   */
  public final void rotationV(Vector3D vector3d, int i) {
  }

  public final void setRotation(Vector3D vector3d, int i) {
  }

  /**
   * @deprecated
   */
  public final void setViewTrans(Vector3D vector3d, Vector3D vector3d1, Vector3D vector3d2) {
  }

  public final void lookAt(Vector3D vector3d, Vector3D vector3d1, Vector3D vector3d2) {
  }

}
