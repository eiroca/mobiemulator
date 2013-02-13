/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Vector;

class ClassProfile {
    Vector classfields = new Vector();
    String className;
    Class  clazz;
    ClassProfile(String className) {
        this.className = className;
        try {
            clazz = Class.forName(className, false, MobiEmulator.mobiEmulatorInstance.multiClassLoader);
            Field f[] = getAllFields(clazz);
            for (Field aF : f) {
                if (!Modifier.isFinal(aF.getModifiers())) {
                    classfields.add(aF);
                    aF.setAccessible(true);
                }
            }
            f = null;
        } catch (ClassNotFoundException ex) {}
    }

    Field[] getAllFields(Class profileClass) {
        Vector fields = new Vector();
        if (profileClass.isArray()) {
            for (int i = 0;i < Array.getLength(Profiler.instance);i++) {
                fields.add(Integer.toString(i));
            }
        }
        getSuperClassFields(profileClass, fields);
        Field[] classFields = new Field[fields.size()];
        for (int i = 0;i < classFields.length;i++) {
            classFields[i] = ((Field) fields.get(i));
        }
        fields.removeAllElements();
        fields = null;

        return classFields;
    }
    void getSuperClassFields(Class classToGetFields, Vector fields) {
        if (Profiler.ignoreClasses.containsKey(classToGetFields.getName())) {
            return;
        }
        if (classToGetFields.getSuperclass() != null) {
            getSuperClassFields(classToGetFields.getSuperclass(), fields);
        }
        Field[] arrayOfField = classToGetFields.getDeclaredFields();
        for (Field anArrayOfField : arrayOfField) {
            fields.add(anArrayOfField);
        }
        arrayOfField = null;
    }
    public String toString() {
        return className;
    }
}

class MyCellRenderer extends DefaultTableCellRenderer {
    public MyCellRenderer() {}

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int col) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        return null;
    }
}

class MyTableModel extends AbstractTableModel {
    private Profiler profiler;
    MyTableModel(Profiler instance) {
        profiler = instance;
    }

    public int getRowCount() {
        return profiler.varNames.length;
    }
    public int getColumnCount() {
        return profiler.columnNames.length;
    }
    public String getColumnName(int col) {
        return ((col == 0) ? "ProfilerName"
                           : "Count");
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
        return profiler.varNames[rowIndex][columnIndex];
    }
    public void updateData() {
        updateProfilerData();
    }
    private void updateProfilerData() {
        profiler.varNames = new String[][] {
            { "createImageCount", "" + profiler.createImageCount },
            { "drawImageCallCount", "" + profiler.drawImageCallCount },
            { "drawImagePixelCount", "" + profiler.drawImagePixelCount },
            { "drawRGBCount", "" + profiler.drawRGBCount }, { "drawRGBDrawnPixels", "" + profiler.drawRGBDrawnPixels },
            { "getRGBCount", "" + profiler.getRGBCount }, { "SystemGCcount", "" + profiler.SystemGCcount },
            { "sleepcallCount", "" + profiler.sleepcallCount },
            { "sleepTotalmsCount ", "" + profiler.sleepTotalmsCount },
            { "yieldCallCount ", "" + profiler.yieldCallCount },
            { "drawRegionCallCount", "" + profiler.drawRegionCallCount },
            { "drawRegionDrawnPixels", "" + profiler.drawRegionDrawnPixels },
            { "getResourceStreamCallCount", "" + profiler.getResourceStreamCallCount },
            { "getResourceStreamBytes", "" + profiler.getResourceStreamBytes },
        };
        Rectangle visibleRect = profiler.profileTable.getVisibleRect();
        int       len         = profiler.varNames.length;
        for (int i = 0;i < len;i++) {
            Rectangle cellRect = profiler.profileTable.getCellRect(i, 1, false);
            if (visibleRect.contains((Rectangle) cellRect)) {
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
    public static int       SystemGCcount              = 0;
    public static int       createImageCount           = 0;
    public static int       currentTimeMillisCallCount = 0;
    public static int       drawImageCallCount         = 0;
    public static int       drawImagePixelCount        = 0;
    public static int       drawRGBCount               = 0;
    public static int       drawRGBDrawnPixels         = 0;
    public static int       drawRegionCallCount        = 0;
    public static int       drawRegionDrawnPixels      = 0;
    public static int       getRGBCount                = 0;
    public static int       getResourceStreamBytes     = 0;
    public static int       getResourceStreamCallCount = 0;
    public static int       sleepTotalmsCount          = 0;
    public static int       sleepcallCount             = 0;
    public static int       yieldCallCount             = 0;
    public static Hashtable ignoreClasses              = new Hashtable();
    static Profiler         instance;
    static {
        ignoreClasses.put("javax.microedition.lcdui.Canvas", "javax.microedition.lcdui.Canvas");
        ignoreClasses.put("javax.microedition.lcdui.Displayable", "javax.microedition.lcdui.Displayable");
        ignoreClasses.put("javax.microedition.lcdui.MIDlet", "javax.microedition.lcdui.MIDlet");
        ignoreClasses.put("javax.microedition.lcdui.Form", "javax.microedition.lcdui.Form");
    }
    private String          filterText     = "";
    public Hashtable        items          = new Hashtable();
    private boolean         isClassChanged = false;
    private StringBuilder            sb             = new StringBuilder();
    private JPanel          categoryPanel;
    private JComboBox       classCmbbox;
    private JLabel          classLabel;
    public String           columnNames[];
    private MobiEmulator mobiEmulatorInstance;
    private JPanel          profilePanel;
    private JScrollPane      profileScrollPane;
    public JTable           profileTable;
    private MyTableModel    profilemodel;
    private JTextField      searchField;
    private JLabel          searchLabel;
    public String           varNames[][];
    private JPanel          watchPanel;
    private JScrollPane      watchScrollPane;
    public JTable           watchTable;
    private WatchTableModel watchmodel;
    public Profiler(MobiEmulator mobiEmulatorInstance) {
        this.mobiEmulatorInstance = mobiEmulatorInstance;
        instance          = this;
        init();
    }

    private void init() {
        setTitle("Profiler/Watches");
        setSize(new Dimension(400, 400));
        setLayout(new BorderLayout());
        varNames = new String[][] {
            { "createImageCount", "" + createImageCount }, { "drawImageCallCount", "" + drawImageCallCount },
            { "drawImagePixelCount", "" + drawImagePixelCount }, { "drawRGBCount", "" + drawRGBCount },
            { "drawRGBDrawnPixels", "" + drawRGBDrawnPixels }, { "getRGBCount", "" + getRGBCount },
            { "SystemGCcount", "" + SystemGCcount }, { "sleepcallCount", "" + sleepcallCount },
            { "sleepTotalmsCount ", "" + sleepTotalmsCount }, { "yieldCallCount ", "" + yieldCallCount },
            { "drawRegionCallCount", "" + drawRegionCallCount },
            { "drawRegionDrawnPixels", "" + drawRegionDrawnPixels },
            { "getResourceStreamCallCount", "" + getResourceStreamCallCount },
            { "getResourceStreamBytes", "" + getResourceStreamBytes },
        };
        columnNames  = new String[]{ "Variable", "Value" };
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
        profileScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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
        classLabel  = new JLabel("Class");
        classCmbbox = new JComboBox();
        classCmbbox.setPreferredSize(new Dimension(200, 20));
        classCmbbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox jcmb = (JComboBox) e.getSource();
                int       i    = jcmb.getSelectedIndex();
                updateWatchClasses(MobiEmulator.mobiEmulatorInstance.jarClasses);
                isClassChanged = true;
                classCmbbox.setSelectedIndex(i);
            }
        });
        searchLabel = new JLabel("search");
        searchField = new JTextField();
        searchField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
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
            { "", "" }, { "", "" }
        }, columnNames);
        watchTable.setSelectionMode(0);
        watchmodel = new WatchTableModel(this);
        watchTable.setModel(watchmodel);
        watchPanel.setSize(400, 200);
        watchScrollPane = new JScrollPane(watchTable);
        watchScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        watchScrollPane.setPreferredSize(new Dimension(watchPanel.getWidth(), watchPanel.getHeight()));
        watchPanel.add(watchScrollPane, BorderLayout.CENTER);
        add(profilePanel, BorderLayout.NORTH);
        add(watchPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        pack();
    }
    public void updateWatchClasses(Vector jarClasses) {
        if ((jarClasses != null) && (classCmbbox.getItemCount() > 0)) {
            // watchTable=null;
            ClassProfile clsProfile = (ClassProfile) classCmbbox.getSelectedItem();
            clsProfile.getAllFields(clsProfile.clazz);
            Vector f         = ((ClassProfile) classCmbbox.getSelectedItem()).classfields;
            int    len       = f.size();
            // System.out.println("len is "+len);
            Vector variables = new Vector();
            Vector values    = new Vector();
            for (Object aF : f) {
                Field curFld = ((Field) aF);
                String name = curFld.getName();
                String val = getValue(clsProfile.clazz, curFld, false);
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
            Vector columns = new Vector();
            columns.add("Field");
            columns.add("Value");
            // watchTable=new JTable(variables,columns );
            watchmodel.updateWatchData(variables, values);
            if (isClassChanged ||!filterText.equals("")) {
                watchmodel.fireTableDataChanged();
                isClassChanged = false;
            }
            // watchPanel.remove(watchTable);
            // watchPanel.add(watchTable);
            // watchTable.getModel().fireTableDataChanged();
        }
    }
    String getValue(Object clzObj, Field fld, boolean isHex) {
        try {
            if (fld.get(clzObj) == null) {
                return "null";
            }
            if (fld.getType() == Integer.TYPE) {
                return isHex ? "0x" + Integer.toHexString(fld.getInt(clzObj))
                             : String.valueOf(fld.getInt(clzObj));
            }
            if (fld.getType() == Boolean.TYPE) {
                return String.valueOf(fld.getBoolean(clzObj));
            }
            if (fld.getType() == Byte.TYPE) {
                return isHex ? "0x" + Integer.toHexString(fld.getByte(clzObj))
                             : String.valueOf(fld.getByte(clzObj));
            }
            if (fld.getType() == Short.TYPE) {
                return isHex ? "0x" + Integer.toHexString(fld.getShort(clzObj))
                             : String.valueOf(fld.getShort(clzObj));
            }
            if (fld.getType() == Long.TYPE) {
                return isHex ? "0x" + Long.toHexString(fld.getLong(clzObj))
                             : String.valueOf(fld.getLong(clzObj));
            }
            if (fld.getType() == String.class) {
                return String.valueOf(fld.get(clzObj));
            }
            if (fld.getType().isArray()) {
                Object localObject = fld.get(clzObj);
                this.sb.setLength(0);
                this.sb.append("length=");
                this.sb.append(Array.getLength(localObject));
                this.sb.append(" {");
                int i = Math.min(Array.getLength(localObject), 20);
                for (int j = 0;j < i;j++) {
                    this.sb.append(Array.get(localObject, j));
                    this.sb.append(' ');
                }
                if (i < Array.getLength(localObject)) {
                    this.sb.append("...");
                }
                this.sb.append('}');

                return this.sb.toString();
            }

            return fld.get(clzObj).toString();
        } catch (Exception localException) {}

        return "";
    }
    public void listClasseFields(Vector classes) {
        classCmbbox.removeAllItems();
        for (Object aClass : classes) {
            String className = (String) aClass;
            ClassProfile classprofile = new ClassProfile(className);
            if ((classprofile.classfields != null) && !ignoreClasses.contains(className)) {
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
        createImageCount           = 0;
        drawImageCallCount         = 0;
        drawImagePixelCount        = 0;
        drawRGBCount               = 0;
        drawRGBDrawnPixels         = 0;
        getRGBCount                = 0;
        SystemGCcount              = 0;
        sleepcallCount             = 0;
        sleepTotalmsCount          = 0;
        yieldCallCount             = 0;
        drawRegionCallCount        = 0;
        drawRegionDrawnPixels      = 0;
        getResourceStreamCallCount = 0;
        getResourceStreamBytes     = 0;
    }
}

class WatchTableModel extends AbstractTableModel {
    String[] columnNames = { "Variable", "Value" };
    private Vector   fields      = new Vector();
    private Vector   values      = new Vector();
    private Profiler profiler;
    WatchTableModel(Profiler instance) {
        profiler = instance;
    }

    public int getRowCount() {
        return fields.size();
    }
    public int getColumnCount() {
        return 2;
    }
    public String getColumnName(int col) {
        return ((col == 0) ? "Field"
                           : "Value");
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < this.fields.size()) {
            if (columnIndex == 0) {
                return this.fields.get(rowIndex);
            }

            return this.values.get(rowIndex);
        }

        return "";
    }
    public void updateWatchData(Vector rows, Vector vals) {
        if ((rows == null) || (vals == null)) {
            return;
        }
        fields = new Vector();
        values = new Vector();
        fields = rows;
        values = vals;
        Rectangle visibleRect = profiler.watchTable.getVisibleRect();
        // System.out.println("w is "+visibleRect.width+" h "+visibleRect.height);
        int       len         = rows.size();
        for (int i = 0;i < len;i++) {
            Rectangle cellRect = profiler.watchTable.getCellRect(i, 1, false);
            if (visibleRect.contains((Rectangle) cellRect)) {
                profiler.watchTable.repaint(cellRect);
            }
        }
    }
}
