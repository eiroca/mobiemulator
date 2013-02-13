/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public final class TableLayoutPolicy extends FormLayoutPolicy {
    private int  cols;
    private Form form;
    private int  viewportHeight;
    private int  viewportWidth;
    private int  viewportX;
    private int  viewportY;
    public TableLayoutPolicy(Form form, int columns) {
        if (columns < 1) {
            throw new java.lang.IllegalArgumentException();
        }
        if (form == null) {
            throw new java.lang.NullPointerException();
        }
        this.form = form;
        this.cols = columns;
    }

    public int getColumns() {
        return this.cols;
    }
    protected void doLayout(int viewportX, int viewportY, int viewportWidth, int viewportHeight, int[] totalSize) {
        this.viewportX      = viewportX;
        this.viewportY      = viewportY;
        this.viewportWidth  = viewportWidth;
        this.viewportHeight = viewportHeight;
        totalSize           = arrangeItems(this.form, this.cols, viewportX, viewportY, viewportWidth, viewportHeight);
    }
    protected Item getTraverse(Item item, int dir) {
        return null;
    }
    private int[] arrangeItems(Form form, int cols, int viewportX, int viewportY, int viewportWidth,
                               int viewportHeight) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
