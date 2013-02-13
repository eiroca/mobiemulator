/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui.game;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Graphics;
import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class LayerManager {
    Vector<Layer> layers = new Vector();
    int           viewportHeight;
    int           viewportWidth;
    int           viewportX;
    int           viewportY;
    public LayerManager() {
        layers = new Vector();
        setViewWindow(0, 0, 0x7FFFFF, 0x7FFFFF);
    }

    public void append(Layer l) {
        layers.addElement(l);
    }
    public void insert(Layer l, int index) {
        layers.insertElementAt(l, index);
    }
    public Layer getLayerAt(int index) {
        return layers.get(index);
    }
    public int getSize() {
        return layers.size();
    }
    public void remove(Layer l) {
        // int pos;
        // if(layers.contains(l))
        // pos=layers.indexOf(l);
        layers.removeElement(l);
    }
    public void paint(Graphics g, int x, int y) {
        // g.setClip(viewportX, viewportY, viewportWidth, viewportHeight);
        int clipX = g.getClipX();
        int clipY = g.getClipY();
        int clipW = g.getClipWidth();
        int clipH = g.getClipHeight();
        g.translate(x - this.viewportX, y - this.viewportY);
        g.clipRect(this.viewportX, this.viewportY, this.viewportWidth, this.viewportHeight);
        // while adding top to bottom but while painting down to top layer
        // [stack painting]
        for (int i = layers.size() - 1;i >= 0;i--) {
            Layer layerObject = layers.elementAt(i);
            if (layerObject.isVisible()) {
                layerObject.paint(g);
            }
        }
        g.translate(-x + this.viewportX, -y + this.viewportY);
        g.setClip(clipX, clipY, clipW, clipH);
    }
    public void setViewWindow(int x, int y, int width, int height) {
        if ((width < 0) || (height < 0)) {
            throw new IllegalArgumentException();
        } else {
            this.viewportX      = x;
            this.viewportY      = y;
            this.viewportWidth  = width;
            this.viewportHeight = height;
        }
    }
}
