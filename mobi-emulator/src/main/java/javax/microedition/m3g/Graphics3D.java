package javax.microedition.m3g;

import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;

public class Graphics3D {

  int swerveHandle;
  public static final int ANTIALIAS = 2;
  public static final int DITHER = 4;
  public static final int TRUE_COLOR = 8;
  public static final int OVERWRITE = 16;
  static final int SUPPORTANTIALIASING = 0;
  static final int SUPPORTTRUECOLOR = 1;
  static final int SUPPORTDITHERING = 2;
  static final int SUPPORTMIPMAP = 3;
  static final int SUPPORTPERSPECTIVECORRECTION = 4;
  static final int SUPPORTLOCALCAMERALIGHTING = 5;
  static final int MAXLIGHTS = 6;
  static final int MAXVIEWPORTDIMENSION = 7;
  static final int MAXTEXTUREDIMENSION = 8;
  static final int MAXSPRITECROPDIMENSION = 9;
  static final int NUMTEXTUREUNITS = 10;
  static final int MAXTRANSFORMSPERVERTEX = 11;
  static final int MAXVIEWPORTWIDTH = 12;
  static final int MAXVIEWPORTHEIGHT = 13;
  private static Graphics3D instance;
  private Object boundTarget = null;

  private boolean isGraphics = true;

  private boolean preload = true;

  private int clipX = 0;

  private int clipY = 0;

  private int clipWidth = 0;

  private int clipHeight = 0;

  private int viewportX = 0;

  private int viewportY = 0;

  private int viewportWidth = 0;

  private int viewportHeight = 0;
  private static int maxViewportWidth;
  private static int maxViewportHeight;

  private final int pixels[] = null;
  private int width;
  private int height;

  Graphics3D() {
  }

  Graphics3D(final int handle) {
    swerveHandle = handle;
  }

  public native float getDepthRangeNear();

  public native float getDepthRangeFar();

  public native boolean isDepthBufferEnabled();

  public native int getHints();

  public native int getLightCount();

  private static native int createImpl();

  public static final Graphics3D getInstance() {

    if (Graphics3D.instance == null) {
      try {
        Graphics3D.instance = (Graphics3D)Engine.instantiateJavaPeer(Graphics3D.createImpl());
      }
      catch (final Exception e) {
        //instance=new Graphics3D();
      }
    }

    return Graphics3D.instance;
  }

  private void bindTargetGraphics(final Graphics3D aThis, final Graphics g, final int clipX, final int clipY, final int clipWidth, final int clipHeight) {
    width = g.getGraphicsImage().getWidth();
    height = g.getGraphicsImage().getHeight();
    g.getGraphicsImage().getData().getPixels(0, 0, width, height, pixels);
    Helpers.bindTarget(this, g, this.clipX, this.clipY, this.clipWidth, this.clipHeight);
  }

  private static native void bindTarget(Graphics3D paramGraphics3D, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

  private native void setBackBufferImage2D(Image2D paramImage2D);

  private native void setHints(boolean paramBoolean, int paramInt);

  public Object getTarget() {
    return boundTarget;
  }

  public synchronized void bindTarget(final Object target, final boolean depthBuffer, final int hints) {
    if (boundTarget != null) { throw new IllegalStateException(); }
    if (target == null) { throw new NullPointerException(); }
    if ((hints & 0xFFFFFFE1) != 0) { throw new IllegalArgumentException(); }

    if ((target instanceof Graphics)) {
      isGraphics = true;
      preload = ((hints & 0x10) == 0);

      bindTarget((Graphics)target);
    }
    else if ((target instanceof Image2D)) {
      isGraphics = false;

      setBackBufferImage2D((Image2D)target);

      boundTarget = target;
    }
    else {
      throw new IllegalArgumentException();
    }

    setHints(depthBuffer, hints);
  }

  public synchronized void bindTarget(final Object target) {
    if (boundTarget != null) { throw new IllegalStateException(); }
    if (target == null) { throw new NullPointerException(); }

    if ((target instanceof Graphics)) {
      isGraphics = true;
      preload = true;

      bindTarget((Graphics)target);
      return;
    }
    else if ((target instanceof Image2D)) {
      isGraphics = false;

      setBackBufferImage2D((Image2D)target);

      boundTarget = target;
      return;
    }
    else {
      throw new IllegalArgumentException();
    }
  }

  private void bindTarget(final Graphics g) {
    clipX = (g.getTranslateX() + g.getClipX());
    clipY = (g.getTranslateY() + g.getClipY());
    clipWidth = g.getClipWidth();
    clipHeight = g.getClipHeight();

    if ((clipWidth > Graphics3D.maxViewportWidth) || (clipHeight > Graphics3D.maxViewportHeight)) { throw new IllegalArgumentException(); }

    boundTarget = g;

    bindTargetGraphics(this, g, clipX, clipY, clipWidth, clipHeight);
  }

  public synchronized void releaseTarget() {
    if (boundTarget == null) { return; }

    if (isGraphics) {
      //releaseTargetGraphics((Graphics)this.boundTarget);
      Helpers.releaseTarget(this, ((Graphics)boundTarget));
    }
    else {
      setBackBufferImage2D(null);
    }
    boundTarget = null;
  }

  public synchronized void setViewport(int x, int y, final int width, final int height) {
    if ((width <= 0) || (width > Graphics3D.maxViewportWidth) || (height <= 0) || (height > Graphics3D.maxViewportHeight)) { throw new IllegalArgumentException(); }
    viewportX = x;
    viewportY = y;
    viewportWidth = width;
    viewportHeight = height;

    if (boundTarget != null) {
      if ((boundTarget instanceof Graphics)) {
        final Graphics g = (Graphics)boundTarget;

        x += g.getTranslateX();
        y += g.getTranslateY();

        preload = ((preload) && ((x > clipX) || ((x + width) < (clipX + clipWidth)) || (y > clipY) || ((y + height) < (clipY + clipHeight))));

        setViewportImpl(x, y, width, height);

        preload = false;
      }
      else {
        setViewportImpl(x, y, width, height);
      }
    }
  }

  private native void setViewportImpl(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

  public int getViewportX() {
    return viewportX;
  }

  public int getViewportY() {
    return viewportY;
  }

  public int getViewportWidth() {
    return viewportWidth;
  }

  public int getViewportHeight() {
    return viewportHeight;
  }

  public native void setDepthRange(float paramFloat1, float paramFloat2);

  private native void clearImpl(Background paramBackground);

  public synchronized void clear(final Background background) {
    if (boundTarget == null) { throw new IllegalStateException(); }

    preload = ((preload) && (background != null) && (!background.isColorClearEnabled()));

    clearImpl(background);

    preload = false;
  }

  public void render(final VertexBuffer vertices, final IndexBuffer triangles, final Appearance appearance, final Transform transform) {
    render(vertices, triangles, appearance, transform, -1);
  }

  private native void renderPrimitive(VertexBuffer paramVertexBuffer, IndexBuffer paramIndexBuffer, Appearance paramAppearance, Transform paramTransform, int paramInt);

  public synchronized void render(final VertexBuffer vertices, final IndexBuffer triangles, final Appearance appearance, final Transform transform, final int scope) {
    if (boundTarget == null) { throw new IllegalStateException(); }

    renderPrimitive(vertices, triangles, appearance, transform, scope);

    preload = false;
  }

  private native void renderNode(Node paramNode, Transform paramTransform);

  public synchronized void render(final Node node, final Transform transform) {
    if (boundTarget == null) { throw new IllegalStateException(); }

    renderNode(node, transform);

    preload = false;
  }

  private native void renderWorld(World paramWorld);

  public synchronized void render(final World world) {
    if (boundTarget == null) { throw new IllegalStateException(); }

    final Background background = world.getBackground();
    preload = ((preload) && (background != null) && (!background.isColorClearEnabled()));

    renderWorld(world);

    preload = false;
  }

  public Camera getCamera(final Transform transform) {
    final Camera __res = (Camera)Engine.instantiateJavaPeer(getCameraImpl(transform));
    return __res;
  }

  private native int getCameraImpl(Transform paramTransform);

  public void setCamera(final Camera camera, final Transform transform) {
    setCameraImpl(camera, transform);
    Engine.addXOT(camera);
  }

  private native void setCameraImpl(Camera paramCamera, Transform paramTransform);

  public void setLight(final int index, final Light light, final Transform transform) {
    setLightImpl(index, light, transform);
    Engine.addXOT(light);
  }

  private native void setLightImpl(int paramInt, Light paramLight, Transform paramTransform);

  public Light getLight(final int index, final Transform transform) {
    final Light __res = (Light)Engine.instantiateJavaPeer(getLightImpl(index, transform));
    return __res;
  }

  private native int getLightImpl(int paramInt, Transform paramTransform);

  public int addLight(final Light light, final Transform transform) {
    final int __res = addLightImpl(light, transform);
    Engine.addXOT(light);
    return __res;
  }

  private native int addLightImpl(Light paramLight, Transform paramTransform);

  public native void resetLights();

  public static final synchronized Hashtable getProperties() {
    final Hashtable properties = new Hashtable();

    properties.put("supportAntialiasing", Boolean.valueOf(Graphics3D.getCapability(0) == 1));
    properties.put("supportTrueColor", Boolean.valueOf(Graphics3D.getCapability(1) == 1));
    properties.put("supportDithering", Boolean.valueOf(Graphics3D.getCapability(2) == 1));
    properties.put("supportMipmapping", Boolean.valueOf(Graphics3D.getCapability(3) == 1));
    properties.put("supportPerspectiveCorrection", Boolean.valueOf(Graphics3D.getCapability(4) == 1));
    properties.put("supportLocalCameraLighting", Boolean.valueOf(Graphics3D.getCapability(5) == 1));
    properties.put("maxLights", new Integer(Graphics3D.getCapability(6)));
    properties.put("maxViewportDimension", new Integer(Graphics3D.getCapability(7)));
    properties.put("maxTextureDimension", new Integer(Graphics3D.getCapability(8)));
    properties.put("maxSpriteCropDimension", new Integer(Graphics3D.getCapability(9)));
    properties.put("numTextureUnits", new Integer(Graphics3D.getCapability(10)));
    properties.put("maxTransformsPerVertex", new Integer(Graphics3D.getCapability(11)));

    properties.put("maxViewportWidth", new Integer(Graphics3D.maxViewportWidth));
    properties.put("maxViewportHeight", new Integer(Graphics3D.maxViewportHeight));

    properties.put("C3A458D3-2015-41f5-8338-66A2D3014335", Engine.getVersionMajor() + "." + Engine.getVersionMinor() + "." + Engine.getRevisionMajor() + "." + Engine.getRevisionMinor() + ":" + Engine.getBranchNumber());
    try {
      properties.put("com.superscape.m3gx.DebugUtils", Class.forName("javax.microedition.m3g.DebugUtils").newInstance());
    }
    catch (final Exception e) {
    }

    return properties;
  }

  private static native int getCapability(int paramInt);

  static {
    try {
      System.load("m3g.dll");
    }
    catch (final UnsatisfiedLinkError e) {
      try {
        System.loadLibrary("m3g");
      }
      catch (final Exception ex) {
        ex.printStackTrace();
      }

    }
    Engine.cacheFID(Graphics3D.class, 1);

    Graphics3D.instance = null;

    Graphics3D.maxViewportWidth = Graphics3D.getCapability(12);

    Graphics3D.maxViewportHeight = Graphics3D.getCapability(13);
  }
}
