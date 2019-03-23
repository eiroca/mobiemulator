package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public final class TableLayoutPolicy extends FormLayoutPolicy {

  private final int cols;
  private final Form form;
  private int viewportHeight;
  private int viewportWidth;
  private int viewportX;
  private int viewportY;

  public TableLayoutPolicy(final Form form, final int columns) {
    if (columns < 1) { throw new java.lang.IllegalArgumentException(); }
    if (form == null) { throw new java.lang.NullPointerException(); }
    this.form = form;
    cols = columns;
  }

  public int getColumns() {
    return cols;
  }

  @Override
  protected void doLayout(final int viewportX, final int viewportY, final int viewportWidth, final int viewportHeight, int[] totalSize) {
    this.viewportX = viewportX;
    this.viewportY = viewportY;
    this.viewportWidth = viewportWidth;
    this.viewportHeight = viewportHeight;
    totalSize = arrangeItems(form, cols, viewportX, viewportY, viewportWidth, viewportHeight);
  }

  @Override
  protected Item getTraverse(final Item item, final int dir) {
    return null;
  }

  private int[] arrangeItems(final Form form, final int cols, final int viewportX, final int viewportY, final int viewportWidth, final int viewportHeight) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

}
