/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package javax.microedition.lcdui.game;

//~--- non-JDK imports --------------------------------------------------------

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MidletUtils;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Ashok Kumar Gujarathi
 */
public abstract class GameCanvas extends Canvas {
    public static final int DOWN_PRESSED = 64;
    public static final int FIRE_PRESSED = 256;
    public static final int GAME_A_PRESSED = 512;
    public static final int GAME_B_PRESSED = 1024;
    public static final int GAME_C_PRESSED = 2048;
    public static final int GAME_D_PRESSED = 4096;
    public static final int LEFT_PRESSED = 4;
    public static final int RIGHT_PRESSED = 32;
    public static final int UP_PRESSED = 2;
    public static javax.microedition.lcdui.Graphics _graphics;
    public static javax.microedition.lcdui.Graphics offScreenGraphics;
    private javax.microedition.lcdui.Image offScreenBuffer = null;

    public GameCanvas() {
        if (offScreenBuffer == null)
            offScreenBuffer =
                    javax.microedition.lcdui.Image.createImage(MidletUtils.getInstance().getMidletListener().getCanvasWidth(),
                            MidletUtils.getInstance().getMidletListener().getCanvasHeight());
    }

    protected GameCanvas(boolean suppressKeyEvents) {
        // offScreenBuffer=javax.microedition.lcdui.Image.createImage(getWidth()
        // ,getHeight());
        // offScreenGraphics=offScreenBuffer.getGraphics();
        // System.out.println("in gamecanvas");
        if (offScreenBuffer == null)
            offScreenBuffer =
                    javax.microedition.lcdui.Image.createImage(MidletUtils.getInstance().getMidletListener().getCanvasWidth(),
                            MidletUtils.getInstance().getMidletListener().getCanvasHeight());
    }

    protected Graphics getGraphics() {
        // System.out.println("in get graphics");
        if (offScreenBuffer == null)
            offScreenBuffer =
                    javax.microedition.lcdui.Image.createImage(MidletUtils.getInstance().getMidletListener().getCanvasWidth(),
                            MidletUtils.getInstance().getMidletListener().getCanvasHeight());
        if (listener.isPerformanceEnabled()
                && (listener.getPaintRecordCount() < listener.getPaintStackRecordCount())) {
//          {
            listener.paintStackStartFrame();
        }

        return new Graphics(offScreenBuffer._image);
    }

    public Graphics createGraphics() {
        return getGraphics();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(offScreenBuffer, 0, 0, Graphics.TOP | Graphics.LEFT);
    }

    public void flushGraphics(int x, int y, int width, int height) {
        // offScreenGraphics.setClip(x, y, width, height);
        synchronized (this.paintsync) {
            if (width > offScreenBuffer.getWidth()) {
                width = offScreenBuffer.getWidth();
            }
            if (height > offScreenBuffer.getHeight()) {
                height = offScreenBuffer.getHeight();
            }
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            listener.getCanvasScreen().createGraphics().drawImage(offScreenBuffer._image, x, y,
                    width, height, null);
        }
        if (listener.isPaintRecording()) {
            listener.paintStackEndFrame();
            listener.incrPaintRecordCount();
        }
        if (listener.getPaintRecordCount() == listener.getPaintStackRecordCount()) {
            listener.paintStackStopRecording();
        }
        listener.canvasRepaint();
        // offScreenGraphics.drawImage(offScreenBuffer, x, y, width,
        // height,g.TOP|g.LEFT );
    }

    public void flushGraphics() {
        // offScreenGraphics.drawImage(offScreenBuffer, 0, 0, null);
        // System.out.println("in gamecanvas flushgraphics");
        flushGraphics(0, 0, offScreenBuffer.getWidth(), offScreenBuffer.getHeight());
    }

    public int getKeyStates() {
        // need to get custom key
        int key = 0;
        switch (keyPressedValue) {
            case Displayable.KEY_UP_ARROW:
                key = GameCanvas.UP_PRESSED;

                break;
            case Displayable.KEY_DOWN_ARROW:
                key = GameCanvas.DOWN_PRESSED;

                break;
            case Displayable.KEY_LEFT_ARROW:
                key = GameCanvas.LEFT_PRESSED;

                break;
            case Displayable.KEY_RIGHT_ARROW:
                key = GameCanvas.RIGHT_PRESSED;

                break;
            case Displayable.KEY_FIRE:
                key = GameCanvas.FIRE_PRESSED;

                break;
        }

        // System.out.println("key pressed is "+key);
        return key;
    }
}
