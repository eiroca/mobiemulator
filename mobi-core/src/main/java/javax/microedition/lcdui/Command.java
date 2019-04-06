package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Command {

  public static final int BACK = 2;
  public static final int CANCEL = 3;
  public static final int EXIT = 7;
  public static final int HELP = 5;
  public static final int ITEM = 8;
  public static final int OK = 4;
  public static final int SCREEN = 1;
  public static final int STOP = 6;
  public String Longlabel = null;
  public String label = null;
  public int commandType;
  public int priority;

  public Command(final String label, final int commandType, final int priority) {
    this(label, label, commandType, priority);
  }

  public Command(final String shortLabel, final String longLabel, final int commandType, final int priority) {
    label = shortLabel;
    Longlabel = longLabel;
    this.commandType = commandType;
    this.priority = priority;
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
