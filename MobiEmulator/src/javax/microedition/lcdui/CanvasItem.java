/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class CanvasItem {
    int             height;
    private boolean isVisible;
    private Object  parent;
    int             posX;
    int             posY;
    int             posZ;
    int             width;
    public CanvasItem() {}

    public void setParent(java.lang.Object parent) {
        this.parent = parent;
    }
    public java.lang.Object getParent() {
        return parent;
    }
    public int getHeight() {
        if (parent instanceof Canvas) {
            return ((Canvas) (parent)).getHeight();
        } else if (parent instanceof CanvasItem) {
            return ((CanvasItem) (parent)).getHeight();
        }

        return 0;
    }
    public int getWidth() {
        if (parent instanceof Canvas) {
            return ((Canvas) (parent)).getWidth();
        } else if (parent instanceof CanvasItem) {
            return ((CanvasItem) (parent)).getWidth();
        }

        return 0;
    }
    public void setPositionX(int x) {
        posX = x;
    }
    public void setPositionY(int y) {
        posY = y;
    }
    public int getPositionX() {
        return posX;
    }
    public int getPositionY() {
        return posY;
    }
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    public void setZPosition(int z) {
        posZ = z;
    }
    public int getZPosition() {
        return posZ;
    }
}
