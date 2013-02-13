package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Displayable;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MainCanvasPanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
    public static int                      CANVAS                        = 0;
    public static int                      SCREEN                        = 1;
    public int canvasRotationAngle = 90;
    public int canvasRotationAngleMultiplier = 0;
    public float canvasZoom = 1.0f;
    public final boolean                   DemoVersion                   = false;
    private int mainCanvasPanelHeight = 320;
    private int mainCanvasPanelWidth = 240;
    public BufferedImage xrayScreen = null;
    MJPEGGenerator                         aviRecorder                   = null;
    private long                           firstPaintTime                = 0;
    public BufferedImage                   midp_screen_backscreen        = null;

    /** A MIDP Image object, used as the midlet rendering context */
    public BufferedImage                   midp_screen_image             = null;
    public BufferedImage                   midp_stackImage               = null;
    public Runnable                        runnable                      = null;
    public int                             screenType                    = CANVAS;
    private long                           lastRenderTime                = System.currentTimeMillis();
    public boolean                         isPointerEventsSupported      = false;
    public boolean                         isKeyRepeatedSupported        = true;
    public boolean                         isInfoVisible                 = false;
    public boolean                         captureGIF                    = false;
    private boolean                        captureArea                   = true;
    public Object                          Quality                       =
        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
    public Rectangle infoSelectedRect = new Rectangle(0, 0, 0, 0);
    private boolean firstPaint = true;
    public boolean defaultBackBuffer = !true;

    /** Rotation variable for Canvas */
    public boolean canvasRotation = false;
    public boolean                         isCanvasPaintPaused             = false;
    private Thread emulThread;
    private boolean fullScreenMode;
    private JLabel infoColorBox;
    private JLabel infoColorLabel;

    /* Infoframe variables */
    public JFrame infoFrame;
    private JLabel infoRectLabel;
    private JLabel infoXYLabel;
    private MobiEmulator mainInstance;
    public java.awt.Graphics2D xrayGraphics;
    boolean                                captureAVI;
    private Capture                        caputurer;
    private KeyEvent                       currentKeyEvent;
    private boolean                        isKeyReleased;
    // public BufferedImage canvas_FrontScreen = null;
    // public javax.microedition.lcdui.Graphics midp_screen_graphics = null;
    // public Graphics midp_screen_backscreen_graphics = null;
    public javax.microedition.lcdui.Canvas midp_canvas;
    public javax.microedition.lcdui.Screen midp_screen;
    private int                            mouseDraggedX;
    private int                            mouseDraggedY;
    private int                            mousePressedX;
    private int                            mousePressedY;
    private int                            mouseReleasedX;
    private int                            mouseReleasedY;
    public Xrayframe                       xRayFrame;
    public MainCanvasPanel(MobiEmulator maininstance, int Width, int Height) {
        this.mainInstance = maininstance;
        mainCanvasPanelWidth = Width;
        mainCanvasPanelHeight = Height;
        // setBackground(new Color(0x0));
//      if (midp_screen_image == null) {
//          midp_screen_image = new BufferedImage(mainCanvasPanelWidth,
//                  mainCanvasPanelHeight, BufferedImage.TYPE_INT_ARGB);
//      }
//      if (midp_screen_backscreen == null) {
//          midp_screen_backscreen = new BufferedImage(mainCanvasPanelWidth,
//                  mainCanvasPanelHeight, BufferedImage.TYPE_INT_ARGB);
//
//      }
        if (xrayScreen == null) {
            xrayScreen = new BufferedImage(mainCanvasPanelWidth, mainCanvasPanelHeight, BufferedImage.TYPE_INT_ARGB);
            xrayGraphics = xrayScreen.createGraphics();
            // javax.microedition.lcdui.Graphics.setXrayGraphics(xrayGraphics);
        }
        caputurer = new Capture(maininstance);
        xRayFrame = new Xrayframe();
        xRayFrame.setVisible(false);
        setPreferredSize(new Dimension(mainCanvasPanelWidth, mainCanvasPanelHeight));
        setLocation(new Point(10, 10));
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setVisible(true);

        /* Info Frame */
        infoFrame = new JFrame("Info");
        infoFrame.setUndecorated(true);
        infoFrame.setLocation(this.getX() + this.getWidth(), this.getY());
        infoFrame.setLayout(new BorderLayout());
        infoFrame.setSize(180, 180);
        infoXYLabel = new JLabel();
        infoXYLabel.setText("X,Y");
        infoFrame.add(infoXYLabel, BorderLayout.NORTH);
        infoColorLabel = new JLabel();
        infoColorLabel.setText("Color Value");
        infoFrame.add(infoColorLabel, BorderLayout.WEST);
        infoRectLabel = new JLabel();
        infoRectLabel.setText("Rect ");
        infoFrame.add(infoRectLabel, BorderLayout.SOUTH);
        infoColorBox = new JLabel();
        infoColorBox.setText(" ");
        infoColorBox.setOpaque(true);
        // infoColorBox.setBounds(infoFrame.getWidth()-20,10);
        infoFrame.add(infoColorBox, BorderLayout.CENTER);
        infoFrame.setVisible(isInfoVisible);
        caputurer.gifRecordWidth = getCanvasWidth();
        caputurer.gifRecordHeight = getCanvasHeight();
    }

    public void startThread() {
        emulThread = new Thread(this);
        emulThread.start();
    }
    public void stopThread() {
        try {
            if ((emulThread != null) && emulThread.isAlive()) {
                emulThread.join();
            }
        } catch (Exception ex) {}
    }

    public int getCanvasWidth() {
        return mainCanvasPanelWidth;
    }

    public int getCanvasHeight() {
        return mainCanvasPanelHeight;
    }
    public void run() {
        // TODO Auto-generated mebthod stub
        // System.out.println("in run of canvaspanel");
        while (true) {
            // System.out.println("in thread loop");
            if (!isCanvasPaintPaused) {
                if (screenType == SCREEN) {
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {}
                    // System.out.println("screentype is "+screenType);
                    // if(midp_screen!=null&& midp_screen_image!=null)
                    midp_screen.invokePaint(new javax.microedition.lcdui.Graphics(midp_screen_image.createGraphics(),midp_screen_image));
                } else {
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {}
                }
                repaint();
            }
        }
    }
    public void setCanvasPaused(boolean value) {
        isCanvasPaintPaused = value;
    }
    public boolean isCanvasPaused() {
        return isCanvasPaintPaused;
    }
    public void canvasPause() {
        
        if(midp_canvas!=null)
        midp_canvas.invokeHideNotify();
        setCanvasPaused(true);
        requestFocus();
        // Maininstance.midlet.amsPauseApp();
        // setCanvasPaused(true);
        // System.out.println("in canvaspause");
    }
    public void canvasResume() {
        
        if(midp_canvas!=null)
        midp_canvas.invokeShowNotify();
        setCanvasPaused(false);
        requestFocus();
        // Maininstance.midlet.amsStartApp();
        // setCanvasPaused(false);
    }
    public void setDisplayType(int type) {
        screenType = type;
    }
    public int getDisplayType() {
        return screenType;
    }
    @Override
    public void paintComponent(Graphics g) {
        if (getCurrentDisplayable() == null) {
            return;
        }
        // System.out.println("in paint component");
        if (midp_screen_image == null) {
            midp_screen_image = new BufferedImage(getCanvasWidth(), getCanvasHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        if (midp_screen_backscreen == null) {
            midp_screen_backscreen = new BufferedImage(getCanvasWidth(), getCanvasHeight(),
                    BufferedImage.TYPE_INT_ARGB);
        }
       
//      if (xrayScreen == null) {
//          xrayScreen = new BufferedImage(mainCanvasPanelWidth,
//                  mainCanvasPanelHeight, BufferedImage.TYPE_INT_ARGB);
//          xrayGraphics = xrayScreen.createGraphics();
//          javax.microedition.lcdui.Graphics.setXrayGraphics(xrayGraphics);
//      }
        if (DemoVersion && firstPaint) {
            firstPaintTime = System.currentTimeMillis();
            firstPaint = false;
        }
        Graphics2D g2 = (Graphics2D) g;
        // Graphics g2=g;
        // java.awt.Graphics xrayGraphics = xrayScreen._image.getGraphics();
        // javax.microedition.lcdui.Graphics.setXrayGraphics(xrayScreen._image.getGraphics());
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, Quality);
        synchronized (getCurrentDisplayable().paintsync) {
//          g.setColor(Color.WHITE);
//          g.fillRect(0, 0, getCanvasWidth(), getCanvasHeight());
            if (canvasRotation && (canvasRotationAngleMultiplier != 0)) {
                Graphics2D      g2D       = (Graphics2D) g;    // .createGraphics();
                AffineTransform at        = new AffineTransform();
                AffineTransform bt        = g2D.getTransform();
                int             rotatedDx = Math.abs((int) ((getCanvasWidth() - getCanvasHeight()) / canvasZoom) >> 1);
                at.setToTranslation((canvasRotationAngleMultiplier % 2 != 0) ? rotatedDx
                        : 0, (canvasRotationAngleMultiplier % 2 != 0) ? -rotatedDx
                        : 0);
                at.rotate(Math.toRadians(canvasRotationAngle * canvasRotationAngleMultiplier),
                          midp_screen_image.getWidth(this) / 2, midp_screen_image.getHeight(this) / 2);
                g2D.drawImage(midp_screen_image, at, this);
                g2D.setTransform(bt);
                g2D.dispose();
            } else {
                midp_screen_backscreen.createGraphics().drawImage(midp_screen_image, 0, 0,
                        midp_screen_image.getWidth(null), midp_screen_image.getHeight(null), null);
            }
            if (defaultBackBuffer) {
                g2.drawImage(midp_screen_backscreen, 0, 0, (int) (midp_screen_backscreen.getWidth(null) * canvasZoom),
                             (int) (midp_screen_backscreen.getHeight(null) * canvasZoom), null);
            } else {
                g2.drawImage(midp_screen_image, 0, 0, (int) (midp_screen_image.getWidth(null) * canvasZoom),
                             (int) (midp_screen_image.getHeight(null) * canvasZoom), null);
            }
            if (DemoVersion) {
                Font f = new Font("Arial", Font.BOLD, 20);
                g2.setFont(f);
                FontMetrics metrics = g2.getFontMetrics();
                int         Width   = metrics.stringWidth("DEMO") + 10;
                int         Height  = (20) * (mainCanvasPanelHeight / 100);
                g2.setColor(Color.RED);
                g2.fillRect(0, 0, Width, Height);
                // g2.fillRect(mainCanvasPanelWidth-Width,
                // mainCanvasPanelHeight-Height, Width, Height);
                g2.setColor(Color.BLACK);
                g2.drawString("DEMO", 10, metrics.getHeight());
                // g2.drawString("DEMO",mainCanvasPanelWidth-Width,mainCanvasPanelHeight-Height+metrics.getHeight());
                // if (System.currentTimeMillis() - firstPaintTime > (5 * 60 *
                // 1000)) {
                // System.exit(1);
                // }
            }
            if(isCanvasPaused()){
                FontMetrics metrics = g2.getFontMetrics();
                int         Width   = metrics.stringWidth("PAUSED") + 10;
                int         Height  = (10) * (mainCanvasPanelHeight / 100);
                g2.setColor(Color.RED);
                g2.fillRect(0, getCanvasHeight()-Height, Width, Height);
                g2.setColor(Color.BLACK);
                g2.drawString("PAUSED", 10, getCanvasHeight()-metrics.getHeight());
                
            }
            // Limit the FPS
            FPSLimiter();

            /* Selected Screen Rect */
            if (isInfoVisible || caputurer.captureArea) {
                g2.setColor(new Color(0x00FF00));
                g2.drawRect((mousePressedX < mouseDraggedX) ? mousePressedX
                        : mouseDraggedX, (mousePressedY < mouseDraggedY) ? mousePressedY
                        : mouseDraggedY, infoSelectedRect.width, infoSelectedRect.height);
            }

            /* Total GameWidth */
            g2.setColor(new Color(0x0000FF));
            g.drawRect(-1, -1, (int) (midp_screen_image.getWidth(null) * canvasZoom),
                       (int) (midp_screen_image.getHeight(null) * canvasZoom));
            g2.dispose();
            // g.drawImage(canvas_FrontScreen,0,0,(int)(midp_screen_image.getWidth(null)*canvasZoom),(int)(midp_screen_image.getHeight(null)*canvasZoom),null);
            lastRenderTime = System.currentTimeMillis();
            // repaint();
            // Capture Part
            if (captureGIF) {
                caputurer.captureGIFArea();
            }
            if (captureAVI) {
                caputurer.captureAVIArea();
            }
            if (runnable != null) {
                runnable.run();
            }
            xRayFrame.update();
            
        }
        // if(xRayFrame.isVisible())
        {}
        if (!isKeyReleased && isKeyRepeatedSupported && false) {
            if (currentKeyEvent != null) {
                mainInstance.keyRepeated(currentKeyEvent);
            }
        }
        if ((this.mainInstance.profilerframe != null) && this.mainInstance.profilerframe.isVisible()) {
            this.mainInstance.profilerframe.updateProfiler();
            this.mainInstance.profilerframe.updateWatchClasses(this.mainInstance.jarClasses);
        }
//      if(this.mainInstance.mvf.autoUpdate.isSelected()&&this.mainInstance.mvf.isVisible())
//      {
//          this.mainInstance.mvf.run();
//      }
        if (this.mainInstance.methodFrame.isVisible()) {
            this.mainInstance.methodFrame.updateMethodFrame();
        }
    }
    public void FPSLimiter() {
        // TODO Auto-generated method stub
        long dt   = System.currentTimeMillis() - lastRenderTime;
        long wait = 1000 / mainInstance.fpsValue;
        try {
            Thread.sleep(wait - dt);
        } catch (Exception e) {}
    }
    public void keyPressed(KeyEvent ke) {
        // TODO Auto-generated method stub
        requestFocus();
        if (getCurrentDisplayable() == null) {
            return;
        }
        getCurrentDisplayable().keyTypedEvent = ke;
        Displayable.keyText                   = ke.getKeyChar();
        if ((ke.getKeyCode() == KeyEvent.VK_ADD) || (ke.getKeyCode() == KeyEvent.VK_PLUS)) {
            // System.out.println("dfsdfsdddsfsdf");
            mainInstance.zoomin();
        }
        if ((ke.getKeyCode() == KeyEvent.VK_MINUS) || (ke.getKeyCode() == KeyEvent.VK_SUBTRACT)) {
            mainInstance.zoomout();
        }
        isKeyReleased   = false;
        currentKeyEvent = ke;
        mainInstance.keyPressed(ke);
    }
    public void keyReleased(KeyEvent ke) {
        isKeyReleased   = true;
        currentKeyEvent = null;
        mainInstance.keyReleased(ke);
    }
    public void keyTyped(KeyEvent ke) {
        // TODO Auto-generated method stub
        // System.out.println("in key sswwws")
        if (getCurrentDisplayable() == null) {
            return;
        }
        getCurrentDisplayable().keyTypedEvent = ke;
        Displayable.keyText                   = ke.getKeyChar();
    }
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        if (getCurrentDisplayable() == null) {
            return;
        }
    }
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        requestFocus();
    }
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
        if (getCurrentDisplayable() == null) {
            return;
        }
        requestFocus();
        mousePressedX = getRotatedX(e.getX(), e.getY());
        mousePressedY = getRotatedY(e.getX(), e.getY());
        if (!isPointerinRect(mousePressedX, mousePressedY)) {
            return;
        }
        if (isPointerEventsSupported) {
            getCurrentDisplayable().invokepointerPressed((int) (mousePressedX / getZoomVal()),
                    (int) (mousePressedY / getZoomVal()));
        }
        infoSelectedRect.x      = mousePressedX;
        infoSelectedRect.y      = mousePressedY;
        infoSelectedRect.width  = 0;
        infoSelectedRect.height = 0;
        if (isInfoVisible) {
            infoRectLabel.setText(" Rect " + "X " + infoSelectedRect.x + ", Y " + infoSelectedRect.y + ", W "
                    + infoSelectedRect.width + ", H " + infoSelectedRect.height);
        }
    }
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        if (getCurrentDisplayable() == null) {
            return;
        }
        mouseReleasedX = getRotatedX(e.getX(), e.getY());    // e.getX();
        mouseReleasedY = getRotatedY(e.getX(), e.getY());    // e.getY();
        if (!isPointerinRect(mouseReleasedX, mouseReleasedY)) {
            return;
        }
        if (isPointerEventsSupported) {
            getCurrentDisplayable().invokepointerReleased((int) (mouseReleasedX / getZoomVal()),
                    (int) (mouseReleasedY / getZoomVal()));
        }
        infoSelectedRect.x = Math.min(mouseDraggedX, infoSelectedRect.x);
        infoSelectedRect.y = Math.min(mouseDraggedY, infoSelectedRect.y);
    }
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        if (getCurrentDisplayable() == null) {
            return;
        }
        mouseDraggedX = getRotatedX(e.getX(), e.getY());
        mouseDraggedY = getRotatedY(e.getX(), e.getY());
        if (!isPointerinRect(mouseDraggedX, mouseDraggedY)) {
            return;
        }
        if (isPointerEventsSupported) {
            getCurrentDisplayable().invokepointerDragged((int) (mouseDraggedX / getZoomVal()),
                    (int) (mouseDraggedY / getZoomVal()));
        }
        if (isInfoVisible || captureArea) {
            infoSelectedRect.width = Math.max(mouseDraggedX, infoSelectedRect.x)
                                     - Math.min(mouseDraggedX, infoSelectedRect.x);
            infoSelectedRect.width  = (infoSelectedRect.width > 0) ? infoSelectedRect.width
                    : -infoSelectedRect.width;
            infoSelectedRect.height = Math.max(mouseDraggedY, infoSelectedRect.y)
                                      - Math.min(mouseDraggedY, infoSelectedRect.y);
            infoSelectedRect.height = (infoSelectedRect.height > 0) ? infoSelectedRect.height
                    : -infoSelectedRect.height;
            if (isInfoVisible) {
                infoRectLabel.setText(" Rect " + "X " + infoSelectedRect.x + ", Y " + infoSelectedRect.y + ", W "
                        + infoSelectedRect.width + ", H " + infoSelectedRect.height);
                int    Colorvalue = midp_screen_image.getRGB(mouseDraggedX, mouseDraggedY);
                String color      = Integer.toHexString(Colorvalue);
                mainInstance.canvasPanel.infoXYLabel.setText("X: " + mouseDraggedX + " Y: " + mouseDraggedY);
                mainInstance.canvasPanel.infoColorLabel.setText(" Color: " + color);
                mainInstance.canvasPanel.infoColorBox.setBackground(new Color(Colorvalue));
            }
        }
    }
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        if (mainInstance.mainclass == null) {
            return;
        }
        if (!isPointerinRect(e.getX(), e.getY())) {
            return;
        }
        int movedx = e.getX();
        int movedy = e.getY();
        // System.out.println(" x and y are "+movedx+" "+movedy);
        mainInstance.canvasPanel.infoXYLabel.setText("X: " + movedx / getZoomVal() + " Y: " + movedy / getZoomVal());
        if (isInfoVisible) {
            if ((movedx >= getCanvasWidth()) || (movedy >= getCanvasHeight())) {
                return;
            }
            int    Colorvalue = midp_screen_image.getRGB((int) (movedx / getZoomVal()), (int) (movedy / getZoomVal()));
            String color      = Integer.toHexString(Colorvalue);
            mainInstance.canvasPanel.infoColorLabel.setText(" Color: " + color);
            mainInstance.canvasPanel.infoColorBox.setBackground(new Color(Colorvalue));
        }
    }
    public void setZoomVal(float Zoomvalue) {
        canvasZoom = Zoomvalue;
    }
    public float getZoomVal() {
        return canvasZoom;
    }
    public void setFullScreen(boolean b) {
        // TODO Auto-generated method stub
        fullScreenMode = b;
    }
    public boolean isFullScreen() {
        return fullScreenMode;
    }
    public javax.microedition.lcdui.Displayable getCurrentDisplayable() {
        // TODO Auto-generated method stub
        if (getDisplayType() == SCREEN) {
            return midp_screen;
        } else {
            return midp_canvas;
        }
    }
    public void setCurrent(javax.microedition.lcdui.Displayable nextDisplayable) {
        // TODO Auto-generated method stub
        if (getDisplayType() == SCREEN) {
            midp_screen = (javax.microedition.lcdui.Screen) nextDisplayable;
        } else {
            midp_canvas = (javax.microedition.lcdui.Canvas) nextDisplayable;
        }
    }
    public void setCanvasWidth(int canvasPanelWidth) {
        // TODO Auto-generated method stub
        mainCanvasPanelWidth = canvasPanelWidth;
    }
    public void setCanvasHeight(int canvasPanelHeight) {
        // TODO Auto-generated method stub
        mainCanvasPanelHeight = canvasPanelHeight;
    }
    public void updateInfoFramePos(int mainWindowX, int mainWindowY) {
        // TODO Auto-generated method stub
        infoFrame.setLocation(new Point((mainWindowX <= 0) ? mainWindowX + (getCanvasWidth() + 2)
                : (mainWindowX - infoFrame.getWidth()), mainWindowY));
    }
    public void updateCanvasSize(int newWidth, int newHeight) {
        setPreferredSize(new Dimension(newWidth, newHeight));
        revalidate();
        setCanvasWidth(newWidth);
        setCanvasHeight(newHeight);
        repaint();
    }
    void takeScreenShot(int x, int y, int w, int h, boolean clipboard) {
        if (caputurer != null) {
            caputurer.takePicture(x, y, w, h, clipboard);
        }
    }
    void startCaptureAVI() {
        captureAVI = true;
        caputurer.setCaptureRect(infoSelectedRect.x, infoSelectedRect.y, infoSelectedRect.width,
                                 infoSelectedRect.height);
        caputurer.startAVI();
    }
    void startGIFCapture() {
        captureGIF = true;
        caputurer.setCaptureRect(infoSelectedRect.x, infoSelectedRect.y, infoSelectedRect.width,
                                 infoSelectedRect.height);
        caputurer.startGIF();
    }
    void stopCaptureAVI() {
        captureAVI = false;
        if (caputurer != null) {
            caputurer.finishAVI();
            caputurer.aviRecorder = null;
        }
    }
    void stopGIFCapture() {
        captureGIF = false;
        if (caputurer != null) {
            caputurer.stopGIF();
            caputurer.gifImage = null;
        }
    }
    private int getRotatedX(int x, int y) {
        int pointerX = x;
        if (canvasRotation && (canvasRotationAngleMultiplier != 0)) {
            switch (canvasRotationAngleMultiplier) {
            case 0 :
                pointerX = x;

                break;
            case 1 :
                pointerX = y;

                break;
            case 2 :
                pointerX = getCanvasWidth() - x;

                break;
            case 3 :
                pointerX = getCanvasWidth() - y;

                break;
            }

            return pointerX;
        } else {
            return pointerX;
        }
    }
    private int getRotatedY(int x, int y) {
        int pointerY = y;
        if (canvasRotation && (canvasRotationAngleMultiplier != 0)) {
            switch (canvasRotationAngleMultiplier) {
            case 0 :
                pointerY = y;

                break;
            case 1 :
                pointerY = getCanvasHeight() - x;

                break;
            case 2 :
                pointerY = getCanvasHeight() - y;

                break;
            case 3 :
                pointerY = x;

                break;
            }

            return pointerY;
        } else {
            return pointerY;
        }
    }
    private boolean isPointerinRect(int x, int y) {
        return !((x <= 0) || (x >= getCanvasWidth()) || (y <= 0) || (y >= getCanvasHeight()));

    }
    public void deInitialize() {
        midp_screen            = null;
        midp_screen_image      = null;
        midp_screen_backscreen = null;
        xrayScreen = null;
        xrayGraphics = null;
        xRayFrame              = null;
        aviRecorder            = null;
        caputurer              = null;
        midp_canvas            = null;
        stopThread();
        infoFrame = null;
        mainInstance = null;
        runnable     = null;
    }

    public BufferedImage getXrayScreen() {
        return xrayScreen;
    }


}
