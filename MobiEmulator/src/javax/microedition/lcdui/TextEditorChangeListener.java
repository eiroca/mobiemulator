/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface TextEditorChangeListener {
    public static final int ACTION_CARET_MOVE        = 4;
    public static final int ACTION_CONTENT_CHANGE    = 1;
    public static final int ACTION_DIRECTION_CHANGE  = 64;
    public static final int ACTION_INPUT_MODE_CHANGE = 128;
    public static final int ACTION_LANGUAGE_CHANGE   = 256;
    public static final int ACTION_TRAVERSE_NEXT     = 16;
    public static final int ACTION_TRAVERSE_PREVIOUS = 8;
    void inputAction(TextEditor textEditor, int actions);
}
