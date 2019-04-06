package Emulator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Vector;
// ~--- JDK imports ------------------------------------------------------------
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

class ClassProfile {

  Vector classfields = new Vector();
  String className;
  Class clazz;

  ClassProfile(final String className) {
    this.className = className;
    try {
      clazz = Class.forName(className, false, MobiEmulator.mobiEmulatorInstance.multiClassLoader);
      Field f[] = getAllFields(clazz);
      for (final Field aF : f) {
        if (!Modifier.isFinal(aF.getModifiers())) {
          classfields.add(aF);
          aF.setAccessible(true);
        }
      }
      f = null;
    }
    catch (final ClassNotFoundException ex) {
    }
  }

  Field[] getAllFields(final Class profileClass) {
    Vector fields = new Vector();
    if (profileClass.isArray()) {
      for (int i = 0; i < Array.getLength(Profiler.instance); i++) {
        fields.add(Integer.toString(i));
      }
    }
    getSuperClassFields(profileClass, fields);
    final Field[] classFields = new Field[fields.size()];
    for (int i = 0; i < classFields.length; i++) {
      classFields[i] = ((Field)fields.get(i));
    }
    fields.removeAllElements();
    fields = null;

    return classFields;
  }

  void getSuperClassFields(final Class classToGetFields, final Vector fields) {
    if (Profiler.ignoreClasses.containsKey(classToGetFields.getName())) { return; }
    if (classToGetFields.getSuperclass() != null) {
      getSuperClassFields(classToGetFields.getSuperclass(), fields);
    }
    Field[] arrayOfField = classToGetFields.getDeclaredFields();
    for (final Field anArrayOfField : arrayOfField) {
      fields.add(anArrayOfField);
    }
    arrayOfField = null;
  }

  @Override
  public String toString() {
    return className;
  }
}

class MyCellRenderer extends DefaultTableCellRenderer {

  /**
   *
   */
  private static final long serialVersionUID = -4022663067664312855L;

  public MyCellRenderer() {
  }

  @Override
  public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int col) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

    return null;
  }
}

class MyTableModel extends AbstractTableModel {

  /**
   *
   */
  private static final long serialVersionUID = 5596255030978228676L;
  private final Profiler profiler;

  MyTableModel(final Profiler instance) {
    profiler = instance;
  }

  @Override
  public int getRowCount() {
    return profiler.varNames.length;
  }

  @Override
  public int getColumnCount() {
    return profiler.columnNames.length;
  }

  @Override
  public String getColumnName(final int col) {
    return ((col == 0) ? "ProfilerName"
        : "Count");
  }

  @Override
  public Object getValueAt(final int rowIndex, final int columnIndex) {
    return profiler.varNames[rowIndex][columnIndex];
  }

  public void updateData() {
    updateProfilerData();
  }

  private void updateProfilerData() {
    profiler.varNames = new String[][] {
        {
            "createImageCount", "" + Profiler.createImageCount
        },
        {
            "drawImageCallCount", "" + Profiler.drawImageCallCount
        },
        {
            "drawImagePixelCount", "" + Profiler.drawImagePixelCount
        },
        {
            "drawRGBCount", "" + Profiler.drawRGBCount
        }, {
            "drawRGBDrawnPixels", "" + Profiler.drawRGBDrawnPixels
        },
        {
            "getRGBCount", "" + Profiler.getRGBCount
        }, {
            "SystemGCcount", "" + Profiler.SystemGCcount
        },
        {
            "sleepcallCount", "" + Profiler.sleepcallCount
        },
        {
            "sleepTotalmsCount ", "" + Profiler.sleepTotalmsCount
        },
        {
            "yieldCallCount ", "" + Profiler.yieldCallCount
        },
        {
            "drawRegionCallCount", "" + Profiler.drawRegionCallCount
        },
        {
            "drawRegionDrawnPixels", "" + Profiler.drawRegionDrawnPixels
        },
        {
            "getResourceStreamCallCount", "" + Profiler.getResourceStreamCallCount
        },
        {
            "getResourceStreamBytes", "" + Profiler.getResourceStreamBytes
        },
    };
    final Rectangle visibleRect = profiler.profileTable.getVisibleRect();
    final int len = profiler.varNames.length;
    for (int i = 0; i < len; i++) {
      final Rectangle cellRect = profiler.profileTable.getCellRect(i, 1, false);
      if (visibleRect.contains(cellRect)) {
        profiler.profileTable.repaint(cellRect);
      }
    }
  }
}

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Profiler extends JFrame {

  /**
   *
   */
  private static final long serialVersionUID = 1540964006742660263L;
  public static int SystemGCcount = 0;
  public static int createImageCount = 0;
  public static int currentTimeMillisCallCount = 0;
  public static int drawImageCallCount = 0;
  public static int drawImagePixelCount = 0;
  public static int drawRGBCount = 0;
  public static int drawRGBDrawnPixels = 0;
  public static int drawRegionCallCount = 0;
  public static int drawRegionDrawnPixels = 0;
  public static int getRGBCount = 0;
  public static int getResourceStreamBytes = 0;
  public static int getResourceStreamCallCount = 0;
  public static int sleepTotalmsCount = 0;
  public static int sleepcallCount = 0;
  public static int yieldCallCount = 0;
  public static Hashtable ignoreClasses = new Hashtable();
  static Profiler instance;
  static {
    Profiler.ignoreClasses.put("javax.microedition.lcdui.Canvas", "javax.microedition.lcdui.Canvas");
    Profiler.ignoreClasses.put("javax.microedition.lcdui.Displayable", "javax.microedition.lcdui.Displayable");
    Profiler.ignoreClasses.put("javax.microedition.lcdui.MIDlet", "javax.microedition.lcdui.MIDlet");
    Profiler.ignoreClasses.put("javax.microedition.lcdui.Form", "javax.microedition.lcdui.Form");
  }
  private String filterText = "";
  public Hashtable items = new Hashtable();
  private boolean isClassChanged = false;
  private final StringBuilder sb = new StringBuilder();
  private JPanel categoryPanel;
  private JComboBox classCmbbox;
  private JLabel classLabel;
  public String columnNames[];
  private final MobiEmulator mobiEmulatorInstance;
  private JPanel profilePanel;
  private JScrollPane profileScrollPane;
  public JTable profileTable;
  private MyTableModel profilemodel;
  private JTextField searchField;
  private JLabel searchLabel;
  public String varNames[][];
  private JPanel watchPanel;
  private JScrollPane watchScrollPane;
  public JTable watchTable;
  private WatchTableModel watchmodel;

  public Profiler(final MobiEmulator mobiEmulatorInstance) {
    this.mobiEmulatorInstance = mobiEmulatorInstance;
    Profiler.instance = this;
    init();
  }

  private void init() {
    setTitle("Profiler/Watches");
    setSize(new Dimension(400, 400));
    setLayout(new BorderLayout());
    varNames = new String[][] {
        {
            "createImageCount", "" + Profiler.createImageCount
        }, {
            "drawImageCallCount", "" + Profiler.drawImageCallCount
        },
        {
            "drawImagePixelCount", "" + Profiler.drawImagePixelCount
        }, {
            "drawRGBCount", "" + Profiler.drawRGBCount
        },
        {
            "drawRGBDrawnPixels", "" + Profiler.drawRGBDrawnPixels
        }, {
            "getRGBCount", "" + Profiler.getRGBCount
        },
        {
            "SystemGCcount", "" + Profiler.SystemGCcount
        }, {
            "sleepcallCount", "" + Profiler.sleepcallCount
        },
        {
            "sleepTotalmsCount ", "" + Profiler.sleepTotalmsCount
        }, {
            "yieldCallCount ", "" + Profiler.yieldCallCount
        },
        {
            "drawRegionCallCount", "" + Profiler.drawRegionCallCount
        },
        {
            "drawRegionDrawnPixels", "" + Profiler.drawRegionDrawnPixels
        },
        {
            "getResourceStreamCallCount", "" + Profiler.getResourceStreamCallCount
        },
        {
            "getResourceStreamBytes", "" + Profiler.getResourceStreamBytes
        },
    };
    columnNames = new String[] {
        "Variable", "Value"
    };
    profileTable = new JTable(varNames, columnNames);
    profileTable.setSelectionMode(0);
    profilemodel = new MyTableModel(this);
    profileTable.setModel(profilemodel);
    // dataPanel=new DataPanel(this);
    profilePanel = new JPanel();
    TitledBorder profileTitle = new TitledBorder("Profiler");
    profilePanel.setBorder(profileTitle);
    profileTitle = null;
    profilePanel.setLayout(new BorderLayout());
    profilePanel.setSize(400, 200);
    profileScrollPane = new JScrollPane(profileTable);
    profileScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    profileScrollPane.setPreferredSize(new Dimension(profilePanel.getWidth(), profilePanel.getHeight()));
    profilePanel.add(profileScrollPane, BorderLayout.CENTER);
    // watch panel
    watchPanel = new JPanel();
    TitledBorder watchTitle = new TitledBorder("Watches");
    watchPanel.setBorder(watchTitle);
    watchTitle = null;
    watchPanel.setLayout(new BorderLayout());
    categoryPanel = new JPanel();
    categoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    classLabel = new JLabel("Class");
    classCmbbox = new JComboBox();
    classCmbbox.setPreferredSize(new Dimension(200, 20));
    classCmbbox.addActionListener(e -> {
      final JComboBox jcmb = (JComboBox)e.getSource();
      final int i = jcmb.getSelectedIndex();
      updateWatchClasses(MobiEmulator.mobiEmulatorInstance.jarClasses);
      isClassChanged = true;
      classCmbbox.setSelectedIndex(i);
    });
    searchLabel = new JLabel("search");
    searchField = new JTextField();
    searchField.addKeyListener(new KeyAdapter() {

      @Override
      public void keyTyped(final KeyEvent e) {
        filterText = searchField.getText();
      }
    });
    searchField.setPreferredSize(new Dimension(100, 20));
    categoryPanel.add(classLabel);
    categoryPanel.add(classCmbbox);
    categoryPanel.add(searchLabel);
    categoryPanel.add(searchField);
    categoryPanel.setVisible(true);
    watchPanel.add(categoryPanel, BorderLayout.NORTH);
    watchTable = new JTable(new String[][] {
        {
            "", ""
        }, {
            "", ""
        }
    }, columnNames);
    watchTable.setSelectionMode(0);
    watchmodel = new WatchTableModel(this);
    watchTable.setModel(watchmodel);
    watchPanel.setSize(400, 200);
    watchScrollPane = new JScrollPane(watchTable);
    watchScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    watchScrollPane.setPreferredSize(new Dimension(watchPanel.getWidth(), watchPanel.getHeight()));
    watchPanel.add(watchScrollPane, BorderLayout.CENTER);
    add(profilePanel, BorderLayout.NORTH);
    add(watchPanel, BorderLayout.CENTER);
    setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    pack();
  }

  public void updateWatchClasses(final Vector jarClasses) {
    if ((jarClasses != null) && (classCmbbox.getItemCount() > 0)) {
      // watchTable=null;
      ClassProfile clsProfile = (ClassProfile)classCmbbox.getSelectedItem();
      clsProfile.getAllFields(clsProfile.clazz);
      final Vector f = ((ClassProfile)classCmbbox.getSelectedItem()).classfields;
      f.size();
      // System.out.println("len is "+len);
      final Vector variables = new Vector();
      final Vector values = new Vector();
      for (final Object aF : f) {
        final Field curFld = ((Field)aF);
        final String name = curFld.getName();
        final String val = getValue(clsProfile.clazz, curFld, false);
        // System.out.println("field is "+((Field)f.get(i)).getName()+" name is "+clsProfile.className);
        if (filterText.equals("") || (filterText.equals(name))) {
          variables.add(name);
          // if(!Modifier.isFinal(((Field)f.get(i)).getModifiers())&&clsProfile.clazz!=null&&!((Field)f.get(i)).getClass().isPrimitive())
          {
            values.add("" + val);
          }
          //                  else
          //                  values.add("");
        }
      }
      clsProfile = null;
      final Vector columns = new Vector();
      columns.add("Field");
      columns.add("Value");
      // watchTable=new JTable(variables,columns );
      watchmodel.updateWatchData(variables, values);
      if (isClassChanged || !filterText.equals("")) {
        watchmodel.fireTableDataChanged();
        isClassChanged = false;
      }
      // watchPanel.remove(watchTable);
      // watchPanel.add(watchTable);
      // watchTable.getModel().fireTableDataChanged();
    }
  }

  String getValue(final Object clzObj, final Field fld, final boolean isHex) {
    try {
      if (fld.get(clzObj) == null) { return "null"; }
      if (fld.getType() == Integer.TYPE) { return isHex ? "0x" + Integer.toHexString(fld.getInt(clzObj))
          : String.valueOf(fld.getInt(clzObj)); }
      if (fld.getType() == Boolean.TYPE) { return String.valueOf(fld.getBoolean(clzObj)); }
      if (fld.getType() == Byte.TYPE) { return isHex ? "0x" + Integer.toHexString(fld.getByte(clzObj))
          : String.valueOf(fld.getByte(clzObj)); }
      if (fld.getType() == Short.TYPE) { return isHex ? "0x" + Integer.toHexString(fld.getShort(clzObj))
          : String.valueOf(fld.getShort(clzObj)); }
      if (fld.getType() == Long.TYPE) { return isHex ? "0x" + Long.toHexString(fld.getLong(clzObj))
          : String.valueOf(fld.getLong(clzObj)); }
      if (fld.getType() == String.class) { return String.valueOf(fld.get(clzObj)); }
      if (fld.getType().isArray()) {
        final Object localObject = fld.get(clzObj);
        sb.setLength(0);
        sb.append("length=");
        sb.append(Array.getLength(localObject));
        sb.append(" {");
        final int i = Math.min(Array.getLength(localObject), 20);
        for (int j = 0; j < i; j++) {
          sb.append(Array.get(localObject, j));
          sb.append(' ');
        }
        if (i < Array.getLength(localObject)) {
          sb.append("...");
        }
        sb.append('}');

        return sb.toString();
      }

      return fld.get(clzObj).toString();
    }
    catch (final Exception localException) {
    }

    return "";
  }

  public void listClasseFields(final Vector classes) {
    classCmbbox.removeAllItems();
    for (final Object aClass : classes) {
      final String className = (String)aClass;
      final ClassProfile classprofile = new ClassProfile(className);
      if ((classprofile.classfields != null) && !Profiler.ignoreClasses.contains(className)) {
        classCmbbox.addItem(classprofile);
      }
    }
    // classCmbbox.setSelectedIndex(0);
  }

  public void updateProfiler() {
    profilemodel.updateData();
    // model.fireTableDataChanged();
  }

  public void updateWatches() {
    watchmodel.updateWatchData(null, null);
    // model.fireTableDataChanged();
  }

  public void deInitialize() {
    classCmbbox.removeAllItems();
    // classCmbbox=null;
    // classCmbbox=new JComboBox();
    reset();
  }

  void reset() {
    Profiler.createImageCount = 0;
    Profiler.drawImageCallCount = 0;
    Profiler.drawImagePixelCount = 0;
    Profiler.drawRGBCount = 0;
    Profiler.drawRGBDrawnPixels = 0;
    Profiler.getRGBCount = 0;
    Profiler.SystemGCcount = 0;
    Profiler.sleepcallCount = 0;
    Profiler.sleepTotalmsCount = 0;
    Profiler.yieldCallCount = 0;
    Profiler.drawRegionCallCount = 0;
    Profiler.drawRegionDrawnPixels = 0;
    Profiler.getResourceStreamCallCount = 0;
    Profiler.getResourceStreamBytes = 0;
  }
}

class WatchTableModel extends AbstractTableModel {

  /**
   *
   */
  private static final long serialVersionUID = -7293632599239353801L;
  String[] columnNames = {
      "Variable", "Value"
  };
  private Vector fields = new Vector();
  private Vector values = new Vector();
  private final Profiler profiler;

  WatchTableModel(final Profiler instance) {
    profiler = instance;
  }

  @Override
  public int getRowCount() {
    return fields.size();
  }

  @Override
  public int getColumnCount() {
    return 2;
  }

  @Override
  public String getColumnName(final int col) {
    return ((col == 0) ? "Field" : "Value");
  }

  @Override
  public Object getValueAt(final int rowIndex, final int columnIndex) {
    if (rowIndex < fields.size()) {
      if (columnIndex == 0) { return fields.get(rowIndex); }
      return values.get(rowIndex);
    }
    return "";
  }

  public void updateWatchData(final Vector rows, final Vector vals) {
    if ((rows == null) || (vals == null)) { return; }
    fields = new Vector();
    values = new Vector();
    fields = rows;
    values = vals;
    final Rectangle visibleRect = profiler.watchTable.getVisibleRect();
    // System.out.println("w is "+visibleRect.width+" h "+visibleRect.height);
    final int len = rows.size();
    for (int i = 0; i < len; i++) {
      final Rectangle cellRect = profiler.watchTable.getCellRect(i, 1, false);
      if (visibleRect.contains(cellRect)) {
        profiler.watchTable.repaint(cellRect);
      }
    }
  }

}
