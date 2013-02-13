/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class FormLayoutPolicy {
    public static final int DIRECTION_LTR = 0;
    public static final int DIRECTION_RTL = 1;
    private static int      layoutDir;
    private Form            form;
    protected FormLayoutPolicy() {}

    protected FormLayoutPolicy(Form form) {}

    protected abstract void doLayout(int viewportX, int viewportY, int viewportWidth, int viewportHeight,
                                     int[] totalSize);
    protected abstract Item getTraverse(Item item, int dir);
    protected final Form getForm() {
        return this.form;
    }
    public static final int getLayoutDirection() {
        return layoutDir;
    }
    protected final int getWidth(Item item) {
        return 0;
    }
    protected final int getHeight(Item item) {
        return 0;
    }
    protected final void setSize(Item item, int width, int height) {}
    protected final int getX(Item item) {
        return 0;
    }
    protected final int getY(Item item) {
        return 0;
    }
    protected final void setPosition(Item item, int x, int y) {}
    protected final boolean isValid(Item item) {
        return false;
    }
    protected final void setValid(Item item, boolean valid) {}
}
