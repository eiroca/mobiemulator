package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class FormLayoutPolicy {

  public static final int DIRECTION_LTR = 0;
  public static final int DIRECTION_RTL = 1;
  private static int layoutDir;
  private Form form;

  protected FormLayoutPolicy() {
  }

  protected FormLayoutPolicy(final Form form) {
  }

  protected abstract void doLayout(int viewportX, int viewportY, int viewportWidth, int viewportHeight, int[] totalSize);

  protected abstract Item getTraverse(Item item, int dir);

  protected final Form getForm() {
    return form;
  }

  public static final int getLayoutDirection() {
    return FormLayoutPolicy.layoutDir;
  }

  protected final int getWidth(final Item item) {
    return 0;
  }

  protected final int getHeight(final Item item) {
    return 0;
  }

  protected final void setSize(final Item item, final int width, final int height) {
  }

  protected final int getX(final Item item) {
    return 0;
  }

  protected final int getY(final Item item) {
    return 0;
  }

  protected final void setPosition(final Item item, final int x, final int y) {
  }

  protected final boolean isValid(final Item item) {
    return false;
  }

  protected final void setValid(final Item item, final boolean valid) {
  }

}
