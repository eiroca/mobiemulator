/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Spacer extends Item {
    private int height = 0;
    private int width  = 0;
    public Spacer(int minWidth, int minHeight) {
        width  = minWidth;
        height = minHeight;
    }

    public void setMinimumSize(int minWidth, int minHeight) {
        width  = minWidth;
        height = minHeight;
    }
    public void addCommand(Command cmd) {
        throw new IllegalStateException();
    }
    public void setDefaultCommand(Command cmd) {
        throw new IllegalStateException();
    }
    public void setLabel(String label) {
        throw new IllegalStateException();
    }
    @Override
    public void paint(Graphics g) {}
    @Override
    public void doLayout() {
        itemWidth  = width;
        itemHeight = height;
    }
    @Override
    public void handleKey(int keyCode) {
        switch (keyCode) {
        case Displayable.KEY_UP_ARROW :
        case Displayable.KEY_NUM2 :
            handledKey = false;    // true for consumed by this item it won't move up / down to next item

            break;
        case Displayable.KEY_DOWN_ARROW :
        case Displayable.KEY_NUM8 :
            handledKey = false;

            break;
        case Displayable.KEY_LEFT_ARROW :
        case Displayable.KEY_NUM4 :
            handledKey = false;

            break;
        case Displayable.KEY_RIGHT_ARROW :
        case Displayable.KEY_NUM6 :
            handledKey = false;

            break;
        }
    }
}
