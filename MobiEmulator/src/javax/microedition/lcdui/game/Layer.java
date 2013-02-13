/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui.game;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class Layer {
    int     height;
    boolean visible;
    int     width;
    int     x;
    int     y;
    Layer(int width, int height) {
        visible = true;
        setWidthImpl(width);
        setHeightImpl(height);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void move(int dx, int dy) {
        // System.out.println("before "+x+" "+y+" dx "+dx+" dy "+dy);
        this.x += dx;
        this.y += dy;
        // System.out.println("moved to new pos"+x+" y "+y);
    }
    public final int getX() {
        return this.x;
    }
    public final int getY() {
        return this.y;
    }
    public final int getWidth() {
        return width;
    }
    public final int getHeight() {
        return height;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public final boolean isVisible() {
        return visible;
    }
    public abstract void paint(Graphics g);
    void setWidthImpl(int width) {
        if (width < 0) {
            throw new IllegalArgumentException();
        } else {
            this.width = width;

            return;
        }
    }
    void setHeightImpl(int height) {
        if (height < 0) {
            throw new IllegalArgumentException();
        } else {
            this.height = height;

            return;
        }
    }
}
