/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class Item {
    public static final int    BUTTON                = 2;
    public static final int    HYPERLINK             = 1;
    public static final int    LAYOUT_2              = 16384;
    public static final int    LAYOUT_BOTTOM         = 32;
    public static final int    LAYOUT_CENTER         = 3;
    public static final int    LAYOUT_DEFAULT        = 0;
    public static final int    LAYOUT_EXPAND         = 2048;
    public static final int    LAYOUT_LEFT           = 1;
    public static final int    LAYOUT_NEWLINE_AFTER  = 512;
    public static final int    LAYOUT_NEWLINE_BEFORE = 256;
    public static final int    LAYOUT_RIGHT          = 2;
    public static final int    LAYOUT_SHRINK         = 1024;
    public static final int    LAYOUT_TOP            = 16;
    public static final int    LAYOUT_VCENTER        = 48;
    public static final int    LAYOUT_VEXPAND        = 8192;
    public static final int    LAYOUT_VSHRINK        = 4096;
    public static final int    PLAIN                 = 0;
    public static Font         font                  = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
                                                           Font.SIZE_SMALL);
    public static int          fontHeight            = font.getHeight();
    public int                 itemHeight            = 0;
    public int                 itemWidth             = 0;
    public KeyEvent            keyEvent              = null;
    public int                 prefferedwidth        = Displayable.SCREEN_WIDTH;
    public int                 prefferedheight       = Displayable.SCREEN_HEIGHT;
    public int                 minWidth              = Displayable.SCREEN_WIDTH;
    public int                 minHeight             = Displayable.SCREEN_HEIGHT;
    public boolean             isSelected            = false;
    public int                 yPos                  = 0;
    public boolean             handledKey            = false;
    public CommandListener     cmdListener;
    public Vector<Command>     commands;
    public Command             defualtCommand;
    public ItemStateListener   iStateListener;
    public ItemCommandListener icommandListener;
    public String              label;
    public int                 layout;
    public void addCommand(Command cmd) {
        commands.add(cmd);
    }
    public String getLabel() {
        return this.label;
    }
    public int getLayout() {
        return this.layout;
    }
    public int getMinimumHeight() {
        return minHeight;
    }
    public int getMinimumWidth() {
        return minWidth;
    }
    public int getPreferredHeight() {
        return prefferedheight;
    }
    public int getPreferredWidth() {
        return prefferedwidth;
    }
    public void notifyStateChanged() {
        System.out.println("* notifyStateChanged not implemented yet*");
    }
    public void removeCommand(Command cmd) {
        commands.remove(cmd);
    }
    public void setDefaultCommand(Command cmd) {
        defualtCommand = cmd;
    }
    public void setItemCommandListener(ItemCommandListener l) {
        this.icommandListener = l;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setLayout(int layout) {
        this.layout = layout;
    }
    public void setPreferredSize(int width, int height) {
        if ((width < -1) || (height < -1)) {
            throw new IllegalArgumentException();
        } else if ((width == -1) || (height == -1)) {
            this.prefferedwidth  = Displayable.SCREEN_WIDTH;
            this.prefferedheight = Displayable.SCREEN_HEIGHT;
        }
        if ((width > Displayable.SCREEN_WIDTH) || (height > Displayable.SCREEN_HEIGHT)) {
            this.prefferedwidth  = Displayable.SCREEN_WIDTH;
            this.prefferedheight = Displayable.SCREEN_HEIGHT;
        } else {
            this.prefferedwidth  = width;
            this.prefferedheight = height;
        }
    }
    public abstract void paint(Graphics g);
    public abstract void doLayout();
    public abstract void handleKey(int keyCode);
    public void invokeItemCommandAction(Command c, Item i) {
        icommandListener.commandAction(c, i);
    }
    public void invokeCommandAction(Command c, Displayable d) {
        cmdListener.commandAction(c, d);
    }
    public void invokeStateChangedAction(Item i) {
        iStateListener.itemStateChanged(i);
    }
}
