package javax.microedition.lcdui.game;

import java.util.Vector;

// ~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class LayerManager {

  Vector<Layer> layers = new Vector();
  int viewportHeight;
  int viewportWidth;
  int viewportX;
  int viewportY;

  public LayerManager() {
    layers = new Vector();
    setViewWindow(0, 0, 0x7FFFFF, 0x7FFFFF);
  }

  public void append(final Layer l) {
    layers.addElement(l);
  }

  public void insert(final Layer l, final int index) {
    layers.insertElementAt(l, index);
  }

  public Layer getLayerAt(final int index) {
    return layers.get(index);
  }

  public int getSize() {
    return layers.size();
  }

  public void remove(final Layer l) {
    // int pos;
    // if(layers.contains(l))
    // pos=layers.indexOf(l);
    layers.removeElement(l);
  }

  public void paint(final Graphics g, final int x, final int y) {
    // g.setClip(viewportX, viewportY, viewportWidth, viewportHeight);
    final int clipX = g.getClipX();
    final int clipY = g.getClipY();
    final int clipW = g.getClipWidth();
    final int clipH = g.getClipHeight();
    g.translate(x - viewportX, y - viewportY);
    g.clipRect(viewportX, viewportY, viewportWidth, viewportHeight);
    // while adding top to bottom but while painting down to top layer
    // [stack painting]
    for (int i = layers.size() - 1; i >= 0; i--) {
      final Layer layerObject = layers.elementAt(i);
      if (layerObject.isVisible()) {
        layerObject.paint(g);
      }
    }
    g.translate(-x + viewportX, -y + viewportY);
    g.setClip(clipX, clipY, clipW, clipH);
  }

  public void setViewWindow(final int x, final int y, final int width, final int height) {
    if ((width < 0) || (height < 0)) {
      throw new IllegalArgumentException();
    }
    else {
      viewportX = x;
      viewportY = y;
      viewportWidth = width;
      viewportHeight = height;
    }
  }

}
