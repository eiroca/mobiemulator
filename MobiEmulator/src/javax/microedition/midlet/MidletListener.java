package javax.microedition.midlet;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.KeyActionMapping;
import javax.wireless.messaging.Message;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Ashok
 * Date: 7/14/12
 * Time: 11:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MidletListener {

    public int getCanvasWidth();

    public int getCanvasHeight();

    public BufferedImage getXrayScreen();

    public void setCanvasFullScreen(boolean b);

    public boolean isCanvasFullScreen();

    public boolean isPerformanceEnabled();

    public int getPaintRecordCount();

    public int getPaintStackRecordCount();

    public void paintStackStopRecording();

    public void paintStackEndFrame();

    public void paintStackStartFrame();

    public void paintStackStartRecording();

    public BufferedImage getCanvasScreen();

    public void canvasRepaint();

    public void incrPaintRecordCount();

    public boolean isProfilerEnabled();

    public void incrRgbCount();

    public void incrImageCount();

    public InputStream getResourceStream(String name) throws IOException;

    public void performancePaintArea(Graphics g,int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor, String type, String s);

    public void performancePaintRGB(Graphics graphics, int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha, String drawrgb, String s);

    public void performancePaintImage(Graphics graphics, Image img, int x, int y, int anchor, String type, String s);

    public void performancePaintRect(Graphics graphics, int x, int y, int width, int height, String type, String s);

    public void performancePaintLine(Graphics graphics, int x1, int y1, int x2, int y2, String type, String s);

    public void performancePaintArc(Graphics graphics, int x, int y, int w, int h, int sa, int aa, String fillarc, String s);

    public void performanceRoundRect(Graphics graphics, int x, int y, int w, int h, int r1, int r2, String drawroundrect, String s);

    public void performancePaintTriangle(Graphics graphics, int x1, int y1, int x2, int y2, int x3, int y3, String filltriangle, String s);

    public void performancePaintRegion(Graphics graphics, Image src, int x_src, int y_src, int width, int height, int transform, int x_dest, int y_dest, int anchor, String drawregion, String s);

    public void performancePaintString(Graphics graphics,Font f, String str, int x, int y, int anchor, String drawstring, String s);

    public void incrDrawRGBCount(int i);

    public void unLoadCurrentJar();

    public MIDlet getCurrentMIDlet();

    public String getAppProperty(String key);

    public boolean isPaintRecording();

    public KeyActionMapping[] getOptionKeys();

    public void updateStatusLeft(String label);

    public void updateStatusRight(String menu);

    public void setPos(int x, int y);

    public int[] getPos();

    public void setRunnable(Runnable r);

    public Runnable getRunnable();

    public void setCurrentDisplay(Displayable nextDisplayable, String type);

    public Displayable getCurrentDisplay();

    public void canvasShowNotify();

    public void canvasHideNotify();

    public Runnable getCanvas();

    public Message receiveMessage(String address) throws IOException;

    public void sendMessage(Message msg,String address) throws IOException ;

    public String getMidletClassName();
}
