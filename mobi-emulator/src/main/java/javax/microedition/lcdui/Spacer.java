package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Spacer extends Item {

  private int height = 0;
  private int width = 0;

  public Spacer(final int minWidth, final int minHeight) {
    width = minWidth;
    height = minHeight;
  }

  public void setMinimumSize(final int minWidth, final int minHeight) {
    width = minWidth;
    height = minHeight;
  }

  @Override
  public void addCommand(final Command cmd) {
    throw new IllegalStateException();
  }

  @Override
  public void setDefaultCommand(final Command cmd) {
    throw new IllegalStateException();
  }

  @Override
  public void setLabel(final String label) {
    throw new IllegalStateException();
  }

  @Override
  public void paint(final Graphics g) {
  }

  @Override
  public void doLayout() {
    itemWidth = width;
    itemHeight = height;
  }

  @Override
  public void handleKey(final int keyCode) {
    switch (keyCode) {
      case Displayable.KEY_UP_ARROW:
      case Displayable.KEY_NUM2:
        handledKey = false; // true for consumed by this item it won't move up / down to next item
        break;
      case Displayable.KEY_DOWN_ARROW:
      case Displayable.KEY_NUM8:
        handledKey = false;
        break;
      case Displayable.KEY_LEFT_ARROW:
      case Displayable.KEY_NUM4:
        handledKey = false;
        break;
      case Displayable.KEY_RIGHT_ARROW:
      case Displayable.KEY_NUM6:
        handledKey = false;
        break;
    }
  }

}
