/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.media.control;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.media.MediaException;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface VideoControl extends GUIControl {
    public static final int USE_DIRECT_VIDEO = 1;
    public java.lang.Object initDisplayMode(int mode, java.lang.Object arg);
    public void setDisplayLocation(int x, int y);
    public int getDisplayX();
    public int getDisplayY();
    public void setVisible(boolean visible);
    public void setDisplaySize(int width, int height) throws MediaException;
    public void setDisplayFullScreen(boolean fullScreenMode) throws MediaException;
    public int getSourceHeight();
    public int getSourceWidth();
    public int getDisplayWidth();
    public int getDisplayHeight();
    public byte[] getSnapshot(java.lang.String imageType) throws MediaException;
}
