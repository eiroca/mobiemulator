package javax.microedition.lcdui;

import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class Item {

  public static final int BUTTON = 2;
  public static final int HYPERLINK = 1;
  public static final int LAYOUT_2 = 16384;
  public static final int LAYOUT_BOTTOM = 32;
  public static final int LAYOUT_CENTER = 3;
  public static final int LAYOUT_DEFAULT = 0;
  public static final int LAYOUT_EXPAND = 2048;
  public static final int LAYOUT_LEFT = 1;
  public static final int LAYOUT_NEWLINE_AFTER = 512;
  public static final int LAYOUT_NEWLINE_BEFORE = 256;
  public static final int LAYOUT_RIGHT = 2;
  public static final int LAYOUT_SHRINK = 1024;
  public static final int LAYOUT_TOP = 16;
  public static final int LAYOUT_VCENTER = 48;
  public static final int LAYOUT_VEXPAND = 8192;
  public static final int LAYOUT_VSHRINK = 4096;
  public static final int PLAIN = 0;
  public static Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
      Font.SIZE_SMALL);
  public static int fontHeight = Item.font.getHeight();
  public int itemHeight = 0;
  public int itemWidth = 0;
  public KeyEvent keyEvent = null;
  public int prefferedwidth = Displayable.SCREEN_WIDTH;
  public int prefferedheight = Displayable.SCREEN_HEIGHT;
  public int minWidth = Displayable.SCREEN_WIDTH;
  public int minHeight = Displayable.SCREEN_HEIGHT;
  public boolean isSelected = false;
  public int yPos = 0;
  public boolean handledKey = false;
  public CommandListener cmdListener;
  public Vector<Command> commands;
  public Command defualtCommand;
  public ItemStateListener iStateListener;
  public ItemCommandListener icommandListener;
  public String label;
  public int layout;

  public void addCommand(final Command cmd) {
    commands.add(cmd);
  }

  public String getLabel() {
    return label;
  }

  public int getLayout() {
    return layout;
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

  public void removeCommand(final Command cmd) {
    commands.remove(cmd);
  }

  public void setDefaultCommand(final Command cmd) {
    defualtCommand = cmd;
  }

  public void setItemCommandListener(final ItemCommandListener l) {
    icommandListener = l;
  }

  public void setLabel(final String label) {
    this.label = label;
  }

  public void setLayout(final int layout) {
    this.layout = layout;
  }

  public void setPreferredSize(final int width, final int height) {
    if ((width < -1) || (height < -1)) {
      throw new IllegalArgumentException();
    }
    else if ((width == -1) || (height == -1)) {
      prefferedwidth = Displayable.SCREEN_WIDTH;
      prefferedheight = Displayable.SCREEN_HEIGHT;
    }
    if ((width > Displayable.SCREEN_WIDTH) || (height > Displayable.SCREEN_HEIGHT)) {
      prefferedwidth = Displayable.SCREEN_WIDTH;
      prefferedheight = Displayable.SCREEN_HEIGHT;
    }
    else {
      prefferedwidth = width;
      prefferedheight = height;
    }
  }

  public abstract void paint(Graphics g);

  public abstract void doLayout();

  public abstract void handleKey(int keyCode);

  public void invokeItemCommandAction(final Command c, final Item i) {
    icommandListener.commandAction(c, i);
  }

  public void invokeCommandAction(final Command c, final Displayable d) {
    cmdListener.commandAction(c, d);
  }

  public void invokeStateChangedAction(final Item i) {
    iStateListener.itemStateChanged(i);
  }

}
