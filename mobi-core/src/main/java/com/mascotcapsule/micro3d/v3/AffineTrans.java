package com.mascotcapsule.micro3d.v3;

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

  public AffineTrans(final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4, final int paramInt5, final int paramInt6, final int paramInt7, final int paramInt8, final int paramInt9, final int paramInt10, final int paramInt11, final int paramInt12) {
  }

  public AffineTrans(final AffineTrans paramAffineTrans) {
  }

  public AffineTrans(final int[][] paramArrayOfInt) {
  }

  public AffineTrans(final int[] paramArrayOfInt) {
  }

  public AffineTrans(final int[] paramArrayOfInt, final int paramInt) {
  }

  public final void get(final int[] paramArrayOfInt) {
  }

  public final void get(final int[] paramArrayOfInt, final int paramInt) {
  }

  public final void set(final int[] paramArrayOfInt, final int paramInt) {
  }

  public final void set(final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4, final int paramInt5, final int paramInt6, final int paramInt7, final int paramInt8, final int paramInt9, final int paramInt10, final int paramInt11, final int paramInt12) {
  }

  public final void set(final AffineTrans paramAffineTrans) {
  }

  public final void set(final int[][] paramArrayOfInt) {
  }

  public final void set(final int[] paramArrayOfInt) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final Vector3D transPoint(final Vector3D paramVector3D) {
    return null;
  }

  public final Vector3D transform(final Vector3D paramVector3D) {
    return null;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void multiply(final AffineTrans paramAffineTrans) {
  }

  public final void mul(final AffineTrans paramAffineTrans) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void multiply(final AffineTrans paramAffineTrans1, final AffineTrans paramAffineTrans2) {
  }

  public final void mul(final AffineTrans paramAffineTrans1, final AffineTrans paramAffineTrans2) {
  }

  public final void setRotationX(final int paramInt) {
  }

  public final void setRotationY(final int paramInt) {
  }

  public final void setRotationZ(final int paramInt) {
  }

  public final void setIdentity() {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void rotationX(final int paramInt) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void rotationY(final int paramInt) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void rotationZ(final int paramInt) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void rotationV(final Vector3D paramVector3D, final int paramInt) {
  }

  public final void setRotation(final Vector3D paramVector3D, final int paramInt) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setViewTrans(final Vector3D paramVector3D1, final Vector3D paramVector3D2, final Vector3D paramVector3D3) {
  }

  public final void lookAt(final Vector3D paramVector3D1, final Vector3D paramVector3D2, final Vector3D paramVector3D3) {
  }
}
