package javax.microedition.lcdui;

import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class TabbedPane extends Screen implements TabListener {

  private String tabbedpanetitle = null;
  private final Vector<Screen> tabscreens = new Vector();
  private final boolean isStrinTab;
  private TabListener listener;
  private int selectedIndex;
  private int tabcount;

  public TabbedPane(final java.lang.String title, final boolean stringTab, final boolean suppressTitle) {
    tabbedpanetitle = title;
    isStrinTab = stringTab;
  }

  public void addTab(final Screen tab, final Image icon) {
    tab.icon = icon;
    tabscreens.add(tab);
  }

  public void addTabListener(final TabListener tabListener) {
    listener = tabListener;
  }

  public void insertTab(final int index, final Screen tab, final Image icon) {
    tabscreens.insertElementAt(tab, index);
    (tabscreens.get(index)).icon = icon;
  }

  public void setFocus(final int index) {
    selectedIndex = index;
  }

  public void setTabIcon(final int index, final Image icon) {
    (tabscreens.get(index)).icon = icon;
  }

  public void removeTab(final int index) {
    tabscreens.remove(index);
  }

  public int getCount() {
    return tabscreens.size();
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public Screen getScreen(final int index) {
    return tabscreens.get(index);
  }

  public Image getTabIcon(final int index) {
    return (tabscreens.get(index)).icon;
  }

  @Override
  public int getWidth() {
    return (tabscreens.get(selectedIndex).getWidth());
  }

  @Override
  public int getHeight() {
    return (tabscreens.get(selectedIndex).getHeight());
  }

  @Override
  protected void paint(final Graphics g) {
    // throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void tabChangeEvent(final Screen tab) {
    listener.tabChangeEvent(tab);
  }

  @Override
  public void tabAddedEvent(final int index, final Screen tab) {
    listener.tabAddedEvent(index, tab);
  }

  @Override
  public void tabRemovedEvent(final int index) {
    listener.tabRemovedEvent(index);
  }

}
