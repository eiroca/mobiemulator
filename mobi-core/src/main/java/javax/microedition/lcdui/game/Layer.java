package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class Layer {

  int height;
  boolean visible;
  int width;
  int x;
  int y;

  Layer(final int width, final int height) {
    visible = true;
    setWidthImpl(width);
    setHeightImpl(height);
  }

  public void setPosition(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public void move(final int dx, final int dy) {
    // System.out.println("before "+x+" "+y+" dx "+dx+" dy "+dy);
    x += dx;
    y += dy;
    // System.out.println("moved to new pos"+x+" y "+y);
  }

  public final int getX() {
    return x;
  }

  public final int getY() {
    return y;
  }

  public final int getWidth() {
    return width;
  }

  public final int getHeight() {
    return height;
  }

  public void setVisible(final boolean visible) {
    this.visible = visible;
  }

  public final boolean isVisible() {
    return visible;
  }

  public abstract void paint(Graphics g);

  void setWidthImpl(final int width) {
    if (width < 0) {
      throw new IllegalArgumentException();
    }
    else {
      this.width = width;

      return;
    }
  }

  void setHeightImpl(final int height) {
    if (height < 0) {
      throw new IllegalArgumentException();
    }
    else {
      this.height = height;

      return;
    }
  }

}
