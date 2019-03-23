package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ScalableImage extends Image {

  private static ScalableImage prevScalableImage;
  private static ScalableImage scalableImage;
  private int height;
  private int viewportHeight;
  private int viewportWidth;
  private int width;

  public static ScalableImage bind(final java.lang.Object extScalableImage) {
    ScalableImage.prevScalableImage = ScalableImage.scalableImage;
    ScalableImage.scalableImage = (ScalableImage)extScalableImage;

    return ScalableImage.scalableImage;
  }

  public void unbind() {
    ScalableImage.scalableImage = ScalableImage.prevScalableImage;
  }

  public void setWidth(final int width, final boolean matchAspectRatio) {
    height = (getViewportHeight() * width) / getViewportWidth();
  }

  public void setHeight(final int height, final boolean matchAspectRatio) {
    width = (getViewportWidth() * height) / getViewportHeight();
  }

  public int getViewportHeight() {
    return viewportHeight;
  }

  public int getViewportWidth() {
    return viewportWidth;
  }

}
