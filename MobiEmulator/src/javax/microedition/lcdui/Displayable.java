/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details.
 */


package javax.microedition.lcdui;

//~--- non-JDK imports --------------------------------------------------------

import javax.microedition.midlet.MidletUtils;
import java.awt.event.KeyEvent;
import java.util.Vector;

//~--- JDK imports ------------------------------------------------------------

public class Displayable {
    public static final int DOWN = 6;
    public static final int FIRE = 8;
    public static final int GAME_A = 9;
    public static final int GAME_B = 10;
    public static final int GAME_C = 11;
    public static final int GAME_D = 12;
    public static final int KEY_DOWN_ARROW = -2;
    public static final int KEY_END = -11;
    public static final int KEY_FIRE = -5;
    public static final int KEY_LEFT_ARROW = -3;
    public static final int KEY_NUM0 = 48;
    public static final int KEY_NUM1 = 49;
    public static final int KEY_NUM2 = 50;
    public static final int KEY_NUM3 = 51;
    public static final int KEY_NUM4 = 52;
    public static final int KEY_NUM5 = 53;
    public static final int KEY_NUM6 = 54;
    public static final int KEY_NUM7 = 55;
    public static final int KEY_NUM8 = 56;
    public static final int KEY_NUM9 = 57;
    public static final int KEY_POUND = 35;
    public static final int KEY_RIGHT_ARROW = -4;
    public static final int KEY_SEND = -10;
    public static final int KEY_SOFTKEY1 = -6;
    public static final int KEY_SOFTKEY2 = -7;
    public static final int KEY_SOFTKEY3 = -8;    // I wonder where -9 is?
    public static final int KEY_STAR = 42;
    // Borrow these mappings from the Nokia FullCanvas definition
    public static final int KEY_UP_ARROW = -1;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int UP = 1;
    // Constants for the javax.microedition.lcdui.Canvas class
    //
    public static Object globalSync = new Object();
    public static int SCREEN_WIDTH = 240;
    public static int SCREEN_HEIGHT = 320;
    public static int j2seKeyPressedValue = -99;
    public static boolean isKeyReleased = false;
    public static boolean isKeyPressed = false;
    public static Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    public static int fontHeight = font.getHeight();
    public static int boldFontHeight = font.getHeight();
    public static Font boldFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    public static char keyText;
    private int commandindex = 0;
    public int keyPressedValue = -99;
    public int keyRelasedValue = -99;
    public KeyEvent keyTypedEvent = null;
    public final Object paintsync = new Object();
    private int speed = 3;
    private int tickerPos = getWidth() + (speed << 2);
    public Vector<Command> rskCommands = new Vector();
    private Vector<Command> Commands = new Vector();
    private String title = null;
    private CommandListener cmdListener;
    protected KeyActionMapping keys[];
    private Command lskCommand;
    public boolean showCommandMenu;
    public Ticker ticker;

    /**
     * Constructor. Quite a lot of work concerning the kayboard input is done in
     * Displayable. This is so that it's subclasses (Canvas primarily, but
     * Screen too, and hence Form, List, TextBox and Alert if they get
     * implemented) can get keyboard handling.
     */
    protected Displayable() {
        Commands = new Vector();
        rskCommands = new Vector();
        cmdListener = null;
        ticker = null;
        boolean invert_numpad = true;
        keys = MidletUtils.getInstance().getMidletListener().getOptionKeys();
        SCREEN_WIDTH = MidletUtils.getInstance().getMidletListener().getCanvasWidth();
        SCREEN_HEIGHT = MidletUtils.getInstance().getMidletListener().getCanvasHeight();
    }

    protected KeyActionMapping getKeyActionMapping(int j2se_key_code) {
        KeyActionMapping mapping = null;
        if (keys != null) {
            for (KeyActionMapping key : keys) {
                if (key.j2se_key_code == j2se_key_code) {
                    mapping = key;
                    break;
                }
            }
        }

        return mapping;
    }

    public int getKeyCode(int gameAction) {
        int midp_key_code = 0;
        if (keys != null) {
            for (KeyActionMapping key : keys) {
                if (key.midp_game_action == gameAction) {
                    midp_key_code = key.midp_key_code;    // pick first code from
                    // list

                    break;
                }
            }
        }

        return midp_key_code;
    }

    public int getGameAction(int midp_key_code) {
        int gameAction = 0;
        if (keys != null) {
            for (KeyActionMapping key : keys) {
                if (key.midp_key_code == midp_key_code) {
                    gameAction = key.midp_game_action;

                    break;
                }
            }
        }

        return gameAction;
    }

    public String getKeyName(int midp_key_code) {
        String keyName = null;
        if (keys != null) {
            for (KeyActionMapping key : keys) {
                if (key.midp_key_code == midp_key_code) {
                    keyName = key.key_name;

                    break;
                }
            }
        }

        return keyName;
    }

    public boolean isShown() {
        return (Display.getDisplay(null).getCurrent() == this);
    }

    //  public void setCurrent(boolean current) {
//  }
    public void setCommandListener(CommandListener c) {
        this.cmdListener = c;
    }

    public void addCommand(Command c) {
        if (Commands.contains(c)) {
            return;
        }
        Commands.addElement(c);
        updateCommands();
    }

    public void removeCommand(Command c) {
        Commands.removeElement(c);
        updateCommands();
    }

    public int getCommandsCount() {
        return Commands.size();
    }

    public void invokePaint(javax.microedition.lcdui.Graphics g) {
    }

    public void invokeKeyPressed(int j2se_key_code) {
        j2seKeyPressedValue = j2se_key_code;
        isKeyPressed = true;
        isKeyReleased = false;
        KeyActionMapping key = getKeyActionMapping(j2se_key_code);
        if (key != null) {
            if (key.key_name.equals("LEFTSOFTKEY") && (cmdListener != null)) {
                this.cmdListener.commandAction(lskCommand, this);
            }
            if (key.key_name.equals("RIGHTSOFTKEY") && (cmdListener != null)) {
                if (rskCommands.size() == 1) {
                    this.cmdListener.commandAction(rskCommands.get(0), this);
                } else {
                    showCommandMenu = !showCommandMenu;
                }
            }
            if (showCommandMenu) {
                if (key.key_name.equals("DPADUP") || key.key_name.equals("KEY_2"))                                   // UP
                {
                    if (commandindex > 0) {
                        commandindex--;
                    }
                }
                if (key.key_name.equals("DPADDOWN") || key.key_name.equals("KEY_8"))                                 // UP
                {
                    if (commandindex < rskCommands.size() - 1) {
                        commandindex++;
                    }
                }
                if ((key.key_name.equals("DPADCENTER") || key.key_name.equals("KEY_5")) && (cmdListener != null))    // UP
                {
                    this.cmdListener.commandAction(rskCommands.get(commandindex), this);
                }
            }
        }
        // allowing these keys for textfields in forms
        // 127 - delete
        // 8 backspace
        // 35 - pageup
        // 36 - pagedown
        if ((key != null)
                || ((j2se_key_code == 36) || (j2se_key_code == 35) || (j2se_key_code == 8) || (j2se_key_code == 127))) {
            if (key == null) {
                keyPressed(j2se_key_code);
            } else {
                keyPressed(key.midp_key_code);
                keyPressedValue = key.midp_key_code;
            }
        }
    }

    public void invokeKeyRepeated(int j2se_key_code) {
        j2seKeyPressedValue = j2se_key_code;
        isKeyPressed = true;
        isKeyReleased = false;
        KeyActionMapping key = getKeyActionMapping(j2se_key_code);
        if ((key != null)
                || ((j2se_key_code == 36) || (j2se_key_code == 35) || (j2se_key_code == 8) || (j2se_key_code == 127))) {
            if (key == null) {
                keyRepeated(j2se_key_code);
            } else {
                keyRepeated(key.midp_key_code);
            }
        }
    }

    public void invokeKeyReleased(int j2se_key_code) {
        j2seKeyPressedValue = j2se_key_code;
        isKeyPressed = false;
        isKeyReleased = true;
        KeyActionMapping key = getKeyActionMapping(j2se_key_code);
        if (key != null) {
            keyReleased(key.midp_key_code);
            keyPressedValue = -99;
        }
    }

    public void invokepointerPressed(int x, int y) {
        pointerPressed(x, y);
    }

    public void invokepointerDragged(int x, int y) {
        pointerDragged(x, y);
    }

    public void invokepointerReleased(int x, int y) {
        pointerReleased(x, y);
    }

    protected void keyPressed(int keyCode) {
    }

    protected void keyRepeated(int keyCode) {
    }

    protected void keyReleased(int keyCode) {
    }

    protected void pointerPressed(int x, int y) {
    }

    protected void pointerDragged(int x, int y) {
    }

    protected void pointerReleased(int x, int y) {
    }

    public void invokeScreenChanged(int w, int h) {
        sizeChanged(w, h);
    }

    protected void sizeChanged(int w, int h) {
    }

    public int getWidth() {
        return SCREEN_WIDTH;
    }

    public int getHeight() {
        return SCREEN_HEIGHT;
    }

    public void setTitle(String s) {
        title = s;
    }

    public String getTitle() {
        return title;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public Ticker getTicker() {
        return this.ticker;
    }

    public int drawTicker(Graphics g, int yPos) {
        int y = yPos;
        if (this.ticker.getString() == null) {
            return 0;
        }
        g.setColor(0XFFFFFFFF);
        g.fillRect(0, y, getWidth(), (fontHeight + 5));
        g.setColor(0x000000);
        g.setStrokeStyle(Graphics.DOTTED);
        g.drawLine(0, y, getWidth(), y);
        g.drawLine(0, y + fontHeight + 3, getWidth(), y + fontHeight + 3);
        g.setColor(0x0000F1);
        g.drawString(ticker.getString(), tickerPos, y + 1, Graphics.TOP | Graphics.RIGHT);
        g.setStrokeStyle(Graphics.SOLID);
        if ((tickerPos + font.stringWidth(ticker.getString())) > -(speed << 2)) {
            tickerPos -= speed;
        }
        if (tickerPos <= -(speed << 2)) {
            tickerPos = getWidth() + (speed << 4);
        }

        return y + fontHeight + 5 - yPos;
    }

    public int drawTitle(Graphics g, String str, int yPos) {
        int padding = 4;
        int fontHeight = Font.getDefaultFont().getHeight();
        int stringWidth = (Font.getDefaultFont().stringWidth(str));
        g.setColor(0XFFFFFFFF);
        g.fillRect(0, yPos, getWidth(), (fontHeight + 2));
        g.setColor(0);
        g.fillRect(padding, fontHeight / 2 - 1, getWidth() - 2 * padding, 4);
        g.setColor(0xFFFFFFFF);
        g.fillRect(((getWidth() >> 1) - (stringWidth >> 1) - padding), fontHeight / 2 - 1, stringWidth + padding,
                fontHeight - 2);
        g.setColor(0x0000FF);
        g.drawString(str, (getWidth() >> 1) - (stringWidth >> 1) + 2 * padding + 5, 2, Graphics.HCENTER | Graphics.TOP);
        yPos = Font.getDefaultFont().getHeight() + 2;

        return yPos;
    }

    public void drawVerticalScrollbar(Graphics g, int x, int y, int w, int h) {
        g.setColor(0);
        // System.out.println("y is "+y+" h is "+h);
        g.fillRect(x, y, w, h);
    }

    public void showCommandMenu(Graphics g) {
        int menuHeight = rskCommands.size() * (fontHeight + 4);
        int menuWidth = 0;
        for (Command rskCommand : rskCommands) {
            menuWidth = Math.max(font.stringWidth(rskCommand.getLabel()), menuWidth);
        }
        int y = SCREEN_HEIGHT - menuHeight;
        int x = SCREEN_WIDTH - menuWidth - 20;
        g.setColor(0XFFFFFF);    // commands menu color
        g.fillRect(x - 2, y - 2, menuWidth + 20 + 2, menuHeight + 2);
        g.setColor(0);
        g.drawRect(x - 3, y - 3, menuWidth + 20 + 3, menuHeight + 3);
        g.setColor(0);
        g.setFont(boldFont);
        for (int i = 0; i < rskCommands.size(); i++) {
            if (i == commandindex) {
                g.setColor(0xCCCC99);
                g.fillRect(x - 2, y - 2, menuWidth + 20 + 2, boldFontHeight + 4);
                g.setColor(0);
            }
            g.drawString(rskCommands.get(i).getLabel(), x + 2, y + 2, Graphics.LEFT | Graphics.TOP);
            y += (boldFontHeight + 4);
        }
    }

    private void updateCommands() {
        int commandsSize = Commands.size();
        rskCommands = new Vector();
        for (javax.microedition.lcdui.Command Command : Commands) {
            if (Command.getCommandType() == Command.BACK) {
                lskCommand = (Command) Command;
            } else if ((lskCommand == null) && (Command.getCommandType() == Command.EXIT)) {
                lskCommand = (Command) Command;
            } else {
                rskCommands.add((Command) Command);
            }
        }
        if (lskCommand != null) {
            MidletUtils.getInstance().getMidletListener().updateStatusLeft(lskCommand.getLabel());
        }
        if (rskCommands.size() > 1) {
            MidletUtils.getInstance().getMidletListener().updateStatusRight("MENU");
        } else if (rskCommands.size() == 1) {
            MidletUtils.getInstance().getMidletListener().updateStatusRight(rskCommands.get(0).getLabel());
        }

    }

    public void deInit() {
        Commands = null;
        rskCommands = null;
        lskCommand = null;
        cmdListener = null;
    }
}
