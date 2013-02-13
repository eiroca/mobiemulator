/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class CustomItem extends Item {
    protected static final int KEY_PRESS           = 4;
    protected static final int KEY_RELEASE         = 8;
    protected static final int KEY_REPEAT          = 16;
    protected static final int NONE                = 0;
    protected static final int POINTER_DRAG        = 128;
    protected static final int POINTER_PRESS       = 32;
    protected static final int POINTER_RELEASE     = 64;
    protected static final int TRAVERSE_HORIZONTAL = 1;
    protected static final int TRAVERSE_VERTICAL   = 2;
    private int                rect[]              = new int[4];
    private Graphics           customGraphics;
    private Image              customImage;
    private String             label;
    protected CustomItem(String label) {
        this.label = label;
    }

    public int getGameAction(int keyCode) {
        return 0;
    }
    protected final int getInteractionModes() {
        return 0;
    }
    protected abstract int getMinContentWidth();
    protected abstract int getMinContentHeight();
    protected abstract int getPrefContentWidth(int height);
    protected abstract int getPrefContentHeight(int width);
    protected void sizeChanged(int w, int h) {}
    protected final void invalidate() {}
    protected abstract void paint(Graphics g, int w, int h);
    protected final void repaint() {}
    protected final void repaint(int x, int y, int w, int h) {}
    protected boolean traverse(int dir, int viewportWidth, int viewportHeight, int[] visRect_inout) {
        return false;
    }
    protected void traverseOut() {}
    protected void keyPressed(int keyCode) {}
    protected void keyReleased(int keyCode) {}
    protected void keyRepeated(int keyCode) {}
    protected void pointerPressed(int x, int y) {}
    protected void pointerReleased(int x, int y) {}
    protected void pointerDragged(int x, int y) {}
    protected void hideNotify() {}
    protected void showNotify() {}
    public void paint(Graphics g) {
        int x = rect[0] = 0;
        int y = rect[1] = yPos;
        int w = getPrefContentWidth(rect[2]);
        int h = getPrefContentWidth(rect[3]);
        if (customImage == null) {
            customImage    = Image.createImage(Displayable.SCREEN_WIDTH, Displayable.SCREEN_HEIGHT);
            customGraphics = customImage.getGraphics();
        }
        g.setColor(0xFFFFFFFF);
        g.fillRect(x, y, w, h);
        g.setColor(0);
        if (label != null) {
            g.drawString(label, x, y, Graphics.TOP | Graphics.LEFT);
            y += (Displayable.boldFontHeight + 4);
        }
        paint(customGraphics, w, h);
        if ((w > Displayable.SCREEN_WIDTH) && (h > Displayable.SCREEN_HEIGHT)) {
            g.setClip(x, y, Displayable.SCREEN_WIDTH, Displayable.SCREEN_HEIGHT);
        }
        g.drawImage(customImage, x, y, 0);
        g.setClip(0, 0, Displayable.SCREEN_WIDTH, Displayable.SCREEN_HEIGHT);
        y += h;
        if (isSelected) {
            g.setColor(0);
            g.drawRect(x - 1, rect[1] - 2, w + 2, (y - yPos) + 4);
        }
        y          += 4;
        itemHeight = y - yPos;
    }
    public void doLayout() {
        int y = yPos;
        if (label != null) {
            y += (Displayable.boldFontHeight + 4);
        }
        int h = 0;
        h               = getPrefContentWidth(h);
        y               += h;
        y               += 4;
        this.itemHeight = y - yPos;
    }
    public void handleKey(int keyCode) {
        switch (keyCode) {
        case Displayable.KEY_DOWN_ARROW :
        case Displayable.KEY_NUM8 :
            handledKey = false;

            break;
        case Displayable.KEY_UP_ARROW :
        case Displayable.KEY_NUM2 :
            handledKey = false;

            break;
        }
    }
}
