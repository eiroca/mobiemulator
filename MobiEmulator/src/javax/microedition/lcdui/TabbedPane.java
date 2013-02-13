/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- JDK imports ------------------------------------------------------------

import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class TabbedPane extends Screen implements TabListener {
    private String         tabbedpanetitle = null;
    private Vector<Screen> tabscreens      = new Vector();
    private boolean        isStrinTab;
    private TabListener    listener;
    private int            selectedIndex;
    private int            tabcount;
    public TabbedPane(java.lang.String title, boolean stringTab, boolean suppressTitle) {
        tabbedpanetitle = title;
        isStrinTab      = stringTab;
    }

    public void addTab(Screen tab, Image icon) {
        tab.icon = icon;
        tabscreens.add(tab);
    }
    public void addTabListener(TabListener tabListener) {
        this.listener = tabListener;
    }
    public void insertTab(int index, Screen tab, Image icon) {
        tabscreens.insertElementAt(tab, index);
        ((Screen) (tabscreens.get(index))).icon = icon;
    }
    public void setFocus(int index) {
        selectedIndex = index;
    }
    public void setTabIcon(int index, Image icon) {
        ((Screen) (tabscreens.get(index))).icon = icon;
    }
    public void removeTab(int index) {
        tabscreens.remove(index);
    }
    public int getCount() {
        return tabscreens.size();
    }
    public int getSelectedIndex() {
        return selectedIndex;
    }
    public Screen getScreen(int index) {
        return tabscreens.get(index);
    }
    public Image getTabIcon(int index) {
        return ((Screen) (tabscreens.get(index))).icon;
    }
    public int getWidth() {
        return (tabscreens.get(selectedIndex).getWidth());
    }
    public int getHeight() {
        return (tabscreens.get(selectedIndex).getHeight());
    }
    @Override
    protected void paint(Graphics g) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }
    public void tabChangeEvent(Screen tab) {
        this.listener.tabChangeEvent(tab);
    }
    public void tabAddedEvent(int index, Screen tab) {
        this.listener.tabAddedEvent(index, tab);
    }
    public void tabRemovedEvent(int index) {
        this.listener.tabRemovedEvent(index);
    }
}
