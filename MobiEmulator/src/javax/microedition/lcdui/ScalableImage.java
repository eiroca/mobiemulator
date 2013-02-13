/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ScalableImage extends Image {
    private static ScalableImage prevScalableImage;
    private static ScalableImage scalableImage;
    private int                  height;
    private int                  viewportHeight;
    private int                  viewportWidth;
    private int                  width;
    public static ScalableImage bind(java.lang.Object extScalableImage) {
        prevScalableImage = scalableImage;
        scalableImage     = (ScalableImage) extScalableImage;

        return scalableImage;
    }
    public void unbind() {
        scalableImage = prevScalableImage;
    }
    public void setWidth(int width, boolean matchAspectRatio) {
        height = (getViewportHeight() * width) / getViewportWidth();
    }
    public void setHeight(int height, boolean matchAspectRatio) {
        width = (getViewportWidth() * height) / getViewportHeight();
    }
    public int getViewportHeight() {
        return viewportHeight;
    }
    public int getViewportWidth() {
        return viewportWidth;
    }
}
