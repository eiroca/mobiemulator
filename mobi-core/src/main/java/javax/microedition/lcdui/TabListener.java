package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface TabListener {

  public void tabChangeEvent(Screen tab);

  public void tabAddedEvent(int index, Screen tab);

  public void tabRemovedEvent(int index);

}
