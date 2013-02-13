/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Command {
    public static final int BACK      = 2;
    public static final int CANCEL    = 3;
    public static final int EXIT      = 7;
    public static final int HELP      = 5;
    public static final int ITEM      = 8;
    public static final int OK        = 4;
    public static final int SCREEN    = 1;
    public static final int STOP      = 6;
    public String           Longlabel = null;
    public String           label     = null;
    public int              commandType;
    public int              priority;
    public Command(String label, int commandType, int priority) {
        this(label, label, commandType, priority);
    }

    public Command(String shortLabel, String longLabel, int commandType, int priority) {
        this.label       = shortLabel;
        this.Longlabel   = longLabel;
        this.commandType = commandType;
        this.priority    = priority;
    }

    public String getLabel() {
        return label;
    }
    public String getLongLabel() {
        return Longlabel;
    }
    public int getCommandType() {
        return commandType;
    }
    public int getPriority() {
        return priority;
    }
}
