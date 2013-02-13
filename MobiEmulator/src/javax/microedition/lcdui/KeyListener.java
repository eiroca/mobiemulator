/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class KeyListener {
    public static final int MODIFIER_ALT     = 65536;
    public static final int MODIFIER_CHR     = 8388608;
    public static final int MODIFIER_COMMAND = 4194304;
    public static final int MODIFIER_CTRL    = 262144;
    public static final int MODIFIER_MASK    = 13041664;
    public static final int MODIFIER_SHIFT   = 131072;
    public void keyPressed(int keyCode, int keyModifier) {}
    public void keyReleased(int keyCode, int keyModifier) {}
    public void keyRepeated(int keyCode, int keyModifier) {}
}
