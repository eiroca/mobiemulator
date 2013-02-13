package javax.microedition.m3g;

import javax.microedition.lcdui.Graphics;
import java.util.Hashtable;

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

    private int pixels[] = null;
    private int width;
    private int height;

    Graphics3D() {
    }

    Graphics3D(int handle) {
        this.swerveHandle = handle;
    }

    public native float getDepthRangeNear();

    public native float getDepthRangeFar();

    public native boolean isDepthBufferEnabled();

    public native int getHints();

    public native int getLightCount();

    private static native int createImpl();

    public static final Graphics3D getInstance() {

        if (instance == null) {
            try {
                instance = (Graphics3D) Engine.instantiateJavaPeer(createImpl());
            } catch (Exception e) {
                //instance=new Graphics3D();
            }
        }


        return instance;
    }

    private void bindTargetGraphics(Graphics3D aThis, Graphics g, int clipX, int clipY, int clipWidth, int clipHeight) {
        width = g.getGraphicsImage().getWidth();
        height = g.getGraphicsImage().getHeight();
        g.getGraphicsImage().getData().getPixels(0, 0, width, height, pixels);
        Helpers.bindTarget(this, g, this.clipX, this.clipY, this.clipWidth, this.clipHeight);
    }

    private static native void bindTarget(Graphics3D paramGraphics3D, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

    private native void setBackBufferImage2D(Image2D paramImage2D);

    private native void setHints(boolean paramBoolean, int paramInt);

    public Object getTarget() {
        return this.boundTarget;
    }

    public synchronized void bindTarget(Object target, boolean depthBuffer, int hints) {
        if (this.boundTarget != null) {
            throw new IllegalStateException();
        }
        if (target == null) {
            throw new NullPointerException();
        }
        if ((hints & 0xFFFFFFE1) != 0) {
            throw new IllegalArgumentException();
        }

        if ((target instanceof Graphics)) {
            this.isGraphics = true;
            this.preload = ((hints & 0x10) == 0);

            bindTarget((Graphics) target);
        } else if ((target instanceof Image2D)) {
            this.isGraphics = false;

            setBackBufferImage2D((Image2D) target);

            this.boundTarget = target;
        } else {
            throw new IllegalArgumentException();
        }

        setHints(depthBuffer, hints);
    }

    public synchronized void bindTarget(Object target) {
        if (this.boundTarget != null) {
            throw new IllegalStateException();
        }
        if (target == null) {
            throw new NullPointerException();
        }

        if ((target instanceof Graphics)) {
            this.isGraphics = true;
            this.preload = true;

            bindTarget((Graphics) target);
            return;
        } else if ((target instanceof Image2D)) {
            this.isGraphics = false;

            setBackBufferImage2D((Image2D) target);

            this.boundTarget = target;
            return;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void bindTarget(Graphics g) {
        this.clipX = (g.getTranslateX() + g.getClipX());
        this.clipY = (g.getTranslateY() + g.getClipY());
        this.clipWidth = g.getClipWidth();
        this.clipHeight = g.getClipHeight();

        if ((this.clipWidth > maxViewportWidth) || (this.clipHeight > maxViewportHeight)) {
            throw new IllegalArgumentException();
        }

        this.boundTarget = g;

        bindTargetGraphics(this, g, this.clipX, this.clipY, this.clipWidth, this.clipHeight);
    }


    public synchronized void releaseTarget() {
        if (this.boundTarget == null) {
            return;
        }

        if (this.isGraphics) {
            //releaseTargetGraphics((Graphics)this.boundTarget);
            Helpers.releaseTarget(this, ((Graphics) this.boundTarget));
        } else {
            setBackBufferImage2D(null);
        }
        this.boundTarget = null;
    }


    public synchronized void setViewport(int x, int y, int width, int height) {
        if ((width <= 0) || (width > maxViewportWidth) || (height <= 0) || (height > maxViewportHeight)) {
            throw new IllegalArgumentException();
        }
        this.viewportX = x;
        this.viewportY = y;
        this.viewportWidth = width;
        this.viewportHeight = height;

        if (this.boundTarget != null) {
            if ((this.boundTarget instanceof Graphics)) {
                Graphics g = (Graphics) this.boundTarget;

                x += g.getTranslateX();
                y += g.getTranslateY();

                this.preload = ((this.preload) && ((x > this.clipX) || (x + width < this.clipX + this.clipWidth) || (y > this.clipY) || (y + height < this.clipY + this.clipHeight)));

                setViewportImpl(x, y, width, height);

                this.preload = false;
            } else {
                setViewportImpl(x, y, width, height);
            }
        }
    }

    private native void setViewportImpl(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public int getViewportX() {
        return this.viewportX;
    }

    public int getViewportY() {
        return this.viewportY;
    }

    public int getViewportWidth() {
        return this.viewportWidth;
    }

    public int getViewportHeight() {
        return this.viewportHeight;
    }

    public native void setDepthRange(float paramFloat1, float paramFloat2);

    private native void clearImpl(Background paramBackground);

    public synchronized void clear(Background background) {
        if (this.boundTarget == null) {
            throw new IllegalStateException();
        }

        this.preload = ((this.preload) && (background != null) && (!background.isColorClearEnabled()));

        clearImpl(background);

        this.preload = false;
    }

    public void render(VertexBuffer vertices, IndexBuffer triangles, Appearance appearance, Transform transform) {
        render(vertices, triangles, appearance, transform, -1);
    }

    private native void renderPrimitive(VertexBuffer paramVertexBuffer, IndexBuffer paramIndexBuffer, Appearance paramAppearance, Transform paramTransform, int paramInt);

    public synchronized void render(VertexBuffer vertices, IndexBuffer triangles, Appearance appearance, Transform transform, int scope) {
        if (this.boundTarget == null) {
            throw new IllegalStateException();
        }

        renderPrimitive(vertices, triangles, appearance, transform, scope);

        this.preload = false;
    }

    private native void renderNode(Node paramNode, Transform paramTransform);

    public synchronized void render(Node node, Transform transform) {
        if (this.boundTarget == null) {
            throw new IllegalStateException();
        }

        renderNode(node, transform);

        this.preload = false;
    }

    private native void renderWorld(World paramWorld);

    public synchronized void render(World world) {
        if (this.boundTarget == null) {
            throw new IllegalStateException();
        }

        Background background = world.getBackground();
        this.preload = ((this.preload) && (background != null) && (!background.isColorClearEnabled()));

        renderWorld(world);

        this.preload = false;
    }

    public Camera getCamera(Transform transform) {
        Camera __res = (Camera) Engine.instantiateJavaPeer(getCameraImpl(transform));
        return __res;
    }

    private native int getCameraImpl(Transform paramTransform);

    public void setCamera(Camera camera, Transform transform) {
        setCameraImpl(camera, transform);
        Engine.addXOT(camera);
    }

    private native void setCameraImpl(Camera paramCamera, Transform paramTransform);

    public void setLight(int index, Light light, Transform transform) {
        setLightImpl(index, light, transform);
        Engine.addXOT(light);
    }

    private native void setLightImpl(int paramInt, Light paramLight, Transform paramTransform);

    public Light getLight(int index, Transform transform) {
        Light __res = (Light) Engine.instantiateJavaPeer(getLightImpl(index, transform));
        return __res;
    }

    private native int getLightImpl(int paramInt, Transform paramTransform);

    public int addLight(Light light, Transform transform) {
        int __res = addLightImpl(light, transform);
        Engine.addXOT(light);
        return __res;
    }

    private native int addLightImpl(Light paramLight, Transform paramTransform);

    public native void resetLights();

    public static final synchronized Hashtable getProperties() {
        Hashtable properties = new Hashtable();

        properties.put("supportAntialiasing", Boolean.valueOf(getCapability(0)==1));
        properties.put("supportTrueColor", Boolean.valueOf(getCapability(1)==1));
        properties.put("supportDithering", Boolean.valueOf(getCapability(2)==1));
        properties.put("supportMipmapping", Boolean.valueOf(getCapability(3)==1));
        properties.put("supportPerspectiveCorrection", Boolean.valueOf(getCapability(4)==1));
        properties.put("supportLocalCameraLighting", Boolean.valueOf(getCapability(5)==1));
        properties.put("maxLights", new Integer(getCapability(6)));
        properties.put("maxViewportDimension", new Integer(getCapability(7)));
        properties.put("maxTextureDimension", new Integer(getCapability(8)));
        properties.put("maxSpriteCropDimension", new Integer(getCapability(9)));
        properties.put("numTextureUnits", new Integer(getCapability(10)));
        properties.put("maxTransformsPerVertex", new Integer(getCapability(11)));

        properties.put("maxViewportWidth", new Integer(maxViewportWidth));
        properties.put("maxViewportHeight", new Integer(maxViewportHeight));

        properties.put("C3A458D3-2015-41f5-8338-66A2D3014335", Engine.getVersionMajor() + "." + Engine.getVersionMinor() + "." + Engine.getRevisionMajor() + "." + Engine.getRevisionMinor() + ":" + Engine.getBranchNumber());
        try {
            properties.put("com.superscape.m3gx.DebugUtils", Class.forName("javax.microedition.m3g.DebugUtils").newInstance());
        } catch (Exception e) {
        }

        return properties;
    }

    private static native int getCapability(int paramInt);

    static {
        try {
            System.load("m3g.dll");
        } catch (UnsatisfiedLinkError e) {
            try {
                System.loadLibrary("m3g");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        Engine.cacheFID(Graphics3D.class, 1);

        instance = null;

        maxViewportWidth = getCapability(12);

        maxViewportHeight = getCapability(13);
    }
}