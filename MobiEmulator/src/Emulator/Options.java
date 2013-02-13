package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.KeyActionMapping;
import javax.microedition.rms.RecordStore;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

class AutoComplete extends JComboBox implements JComboBox.KeySelectionManager {
    private long lap;
    private String searchFor;

    public AutoComplete(Object[] items) {
        super(items);
        lap = new java.util.Date().getTime();
        setKeySelectionManager(this);
        JTextField tf;
        if (getEditor() != null) {
            tf = (JTextField) getEditor().getEditorComponent();
            if (tf != null) {
                tf.setDocument(new CBDocument());
                addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        JTextField tf = (JTextField) getEditor().getEditorComponent();
                        String text = tf.getText();
                        ComboBoxModel aModel = getModel();
                        String current;
                        for (int i = 0; i < aModel.getSize(); i++) {
                            current = aModel.getElementAt(i).toString();
                            if (current.toLowerCase().startsWith(text.toLowerCase())) {
                                aModel.setSelectedItem(current);
                                tf.setText(current);
                                tf.setSelectionStart(text.length());
                                tf.setSelectionEnd(current.length());

                                break;
                            }
                        }
                    }
                });
            }
        }
    }

    public int selectionForKey(char aKey, ComboBoxModel aModel) {
        aModel.notify();
        long now = new java.util.Date().getTime();
        if ((searchFor != null) && (aKey == KeyEvent.VK_BACK_SPACE) && (searchFor.length() > 0)) {
            searchFor = searchFor.substring(0, searchFor.length() - 1);
        } else {
            // System.out.println(lap);
            // Kam nie hier vorbei.
            if (lap + 1000 < now) {
                searchFor = "" + aKey;
            } else {
                searchFor = searchFor + aKey;
            }
        }
        lap = now;
        String current;
        for (int i = 0; i < aModel.getSize(); i++) {
            current = aModel.getElementAt(i).toString().toLowerCase();
            if (current.toLowerCase().startsWith(searchFor.toLowerCase())) {
                aModel.setSelectedItem(current);
                aModel.notify();

                return i;
            }
        }

        return -1;
    }

    public void fireActionEvent() {
        super.fireActionEvent();
    }

    public class CBDocument extends PlainDocument {
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }
            super.insertString(offset, str, a);
            if (!isPopupVisible() && (str.length() != 0)) {
                fireActionEvent();
            }
        }
    }
}

class Device {
    public String centerKey = "";
    public String deviceName = "";
    public String downKey = "";
    public String heap = "";
    public String height = "";
    public String jarLimit = "";
    public String leftKey = "";
    public String lskKey = "";
    public String rightKey = "";
    public String rskKey = "";
    public String upKey = "";
    public String width = "";
}

public class Options extends JFrame implements KeyListener {
    // maintain order with labels
    public static int EnableConsole = 0;
    public static int DebugClassLoading = EnableConsole + 1;
    public static int CacheJarResources = DebugClassLoading + 1;
    public static int KeyRepeatedEvents = CacheJarResources + 1;
    public static int PointerEvents = KeyRepeatedEvents + 1;
    public static int IsPrintOff = PointerEvents + 1;
    public static int IsAlwaysLogVisible = IsPrintOff + 1;
    public static int IsMemoryViewEnabled = IsAlwaysLogVisible + 1;
    public static int IsProfilerEnabled = IsMemoryViewEnabled + 1;
    public static int MonitorMethods = IsProfilerEnabled + 1;
    //    public static int    MonitorMethodByteCode = MonitorMethods + 1;
    public static int TrackNewCalls = MonitorMethods + 1;
    public static String debugLabels[] = {
            "EnableConsole", "DebugClassLoading", "CacheJarResources", "KeyRepeatedEvents", "PointerEvents", "PrintOff",
            "AlwaysLogVisible", "Enable MemoryView", "Enable Profiler", "MonitorMethods", /*"Monitor Method ByteCode",*/
            "Track New Calls",
    };
    public static Vector recentUrls = new Vector();
    private String classesdir = null;
    private int customDeviceKeyCodes[] = new int[10];
    private String customDeviceKeyLabelTexts[] = {
            "LEFTSOFTKEY", "RIGHTSOFTKEY", "DPADUP", "DPADDOWN", "DPADLEFT", "DPADRIGHT", "DPADCENTER", "SENDKEY", "ENDKEY"
    };
    public String customJarPaths[] = null;
    private String customjarsEntry = null;
    private String customkeySettingsHeader = "//*** custom keys***";
    private String editcontinuenote =
            "<html>Note* Once Classpath is set you need to restart MobiEmulator<bt> to Apply Changes in Classpath."
                    + "<br><FONT color=\"#990000\">Limitation* <br><FONT color=\"#000000\">Only changes in method bodies can be reloaded. <br> Changes in class signature are not allowed<br></html>";
    private int keyCodes[] = new int[21];
    private String labelTexts[] = {
            "DPADUP", "DPADDOWN", "DPADLEFT", "DPADRIGHT", "DPADCENTER", "KEY_0", "KEY_1", "KEY_2", "KEY_3", "KEY_4",
            "KEY_5", "KEY_6", "KEY_7", "KEY_8", "KEY_9", "KEY*", "KEY#", "LEFTSOFTKEY", "RIGHTSOFTKEY", "SENDKEY", "ENDKEY"
    };
    private String mapkeySettingsHeader = "//***Key Settings***";
    private String pathSeperator = "/";
    private int recentMRUCount = 5;
    private String rmsExtension = ".rms";
    private String rmsPathSettingsHeader = "//***RMS PATH***";
    private String rmsRecordStoreIdentifier = "#";
    private Hashtable settingsTable = new Hashtable();
    private byte[] nextLine = "\n".getBytes();
    private Hashtable midpKeyCodesTable = new Hashtable();
    private KeyActionMapping[] keys = new KeyActionMapping[21];
    private JLabel keyLabels[] = new JLabel[21];
    private JTextField keyInfos[] = new JTextField[21];
    private Hashtable j2seKeyCodesTable = new Hashtable();

    /**
     * Launch the application
     *
     * @param args
     */
    private boolean isDevelopermode = true;   //default mode
    private int gameActionValues[] = {
            Canvas.UP, Canvas.DOWN, Canvas.LEFT, Canvas.RIGHT, Canvas.FIRE, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    };
    Vector<Device> devices = new Vector();
    Vector deviceNames = new Vector();
    private int defualtKeyCodes[] = {
            KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER, KeyEvent.VK_NUMPAD0,
            KeyEvent.VK_NUMPAD1, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5,
            KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7, KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD9, KeyEvent.VK_MULTIPLY,
            KeyEvent.VK_DIVIDE, KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_INSERT, KeyEvent.VK_PAGE_UP,
    };
    private int defaultMidpKeyCodes[] = {
            Canvas.KEY_UP_ARROW, Canvas.KEY_DOWN_ARROW, Canvas.KEY_LEFT_ARROW, Canvas.KEY_RIGHT_ARROW, Canvas.KEY_FIRE,
            Canvas.KEY_NUM0, Canvas.KEY_NUM7, Canvas.KEY_NUM8, Canvas.KEY_NUM9, Canvas.KEY_NUM4, Canvas.KEY_NUM5,
            Canvas.KEY_NUM6, Canvas.KEY_NUM1, Canvas.KEY_NUM2, Canvas.KEY_NUM3, Canvas.KEY_STAR, Canvas.KEY_POUND,
            Canvas.KEY_SOFTKEY1, Canvas.KEY_SOFTKEY2, Canvas.KEY_SEND, Canvas.KEY_END,
    };
    private JCheckBox debugOptions[] = new JCheckBox[debugLabels.length];
    private boolean debugOptionValues[] = new boolean[debugLabels.length];
    private JLabel customDeviceKeyLabels[] = new JLabel[customDeviceKeyLabelTexts.length];
    private IntegerField customDeviceKeyInfos[] = new IntegerField[customDeviceKeyCodes.length];
    private MobiEmulator mainInstance;
    private JButton browseClassPath;
    private JPanel classPathPanel;
    private JTextField classesPath;
    private JLabel classesdirLabel;
    private JPanel classpathPanel;
    private Vector<String> colunnames;
    private int customDeviceSelectedIndex;
    private JTextArea customJars;
    private JPanel customJarsPanel;
    private JPanel customKeyPanel;
    private JScrollPane custotmJarSCrollPane;
    private JPanel devicePanel;
    private JPanel eACPanel;
    private JButton editcontinueButton;
    private JCheckBox editcontinueChkbox;
    private JCheckBox enableEditandContinue;
    private JPanel keyActionPanel;
    private JPanel keyPanel;
    private JLabel noteLabel;
    private AutoComplete platformBox;
    private JLabel platformLabel;
    private String platformName;
    private Vector records;

    /* Default Settings */
    private String rmsDir;
    private JButton rmsDirBrowse;
    private JLabel rmsDirLabel;
    private JPanel rmsDirPanel;
    private JTextField rmsDirPath;
    private IntegerField rmsMaxRecordStoreValue;
    private JLabel rmsMaxRecordStoresLabel;
    private JLabel rmsMaxRecordsPerStoreLabel;
    private IntegerField rmsMaxRecordsPerStoreValue;
    private JPanel rmsPanel, debugPanel;
    private JPanel rmsSettingsPanel;
    private JScrollPane scrollPane;
    private int selectedRows[];
    private int selectedindex;
    private JTabbedPane tabbedPane;
    private JTable table;
    private Vector totalrecordlist;
    private String defaultDevice = "Sony-Ericsson K800i";

    /**
     * Create the frame
     */
    public Options(MobiEmulator MainInstance) {
        setTitle("Settings");
        this.mainInstance = MainInstance;
        loadDefaultSettings();
        readSettings("settings.txt");
        readDevices("/DeviceList.gz");
        updateSettings();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                writeSettings();
                updateSettings();
            }
        });
        setBounds(100, 100, 342, 427);
        tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        debugPanel = new JPanel();
        Box verticalBox = Box.createVerticalBox();
        debugPanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Debug", null, debugPanel, null);
        rmsPanel = new JPanel();
        rmsPanel.setLayout(new BorderLayout());
        tabbedPane.addTab("RMS", null, rmsPanel, null);
        rmsDirPanel = new JPanel();
        rmsDirPanel.setBorder(new TitledBorder(null, "Rms Directory", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
        rmsDirPanel.setLayout(new BorderLayout());
        rmsPanel.add(rmsDirPanel, BorderLayout.NORTH);
        rmsDirLabel = new JLabel();
        rmsDirPanel.add(rmsDirLabel, BorderLayout.WEST);
        rmsDirLabel.setText("RMS Dir");
        rmsDirBrowse = new JButton();
        rmsDirBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser();
                jf.setCurrentDirectory(new File("./"));
                jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int retval = jf.showOpenDialog(MobiEmulator.mobiEmulatorInstance.options);
                if (retval == JFileChooser.APPROVE_OPTION) {
                    File myFile = jf.getSelectedFile();
                    rmsDirPath.setText(myFile.getAbsolutePath());
                    rmsDir = myFile.getAbsolutePath();
                    updateRecordStore();
                    // DO YOUR PROCESSING HERE. OPEN FILE OR ...
                }
            }
        });
        rmsDirPanel.add(rmsDirBrowse, BorderLayout.EAST);
        rmsDirBrowse.setText("...");
        rmsDirPath = new JTextField();
        rmsDirPanel.add(rmsDirPath, BorderLayout.CENTER);
        rmsDirPath.setText(rmsDir);
        JPanel recordstores = new JPanel();
        recordstores.setBorder(new TitledBorder(null, "RecordStores", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
        recordstores.setLayout(new BorderLayout());
        rmsPanel.add(recordstores);
        scrollPane = new JScrollPane();
        recordstores.add(scrollPane);
        colunnames = new Vector<String>();
        colunnames.addElement("RMS");
        colunnames.addElement("Records");
        colunnames.addElement("Size");
        colunnames.addElement("Modified");
        updateRecordStore();
        table = new JTable(totalrecordlist, colunnames) {
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        };
        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                selectedRows = table.getSelectedRows();
                // System.out.println("selec" + selectedRows[0]);
            }
        });
        scrollPane.setViewportView(table);
        Box bottomBox = Box.createHorizontalBox();
        rmsMaxRecordsPerStoreLabel = new JLabel("MaxRecords/Store");
        rmsMaxRecordsPerStoreValue = new IntegerField(6);
        rmsMaxRecordsPerStoreValue.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                int val = 0;
                try {
                    if (!rmsMaxRecordsPerStoreValue.getText().equals("")) {
                        val = Integer.parseInt(rmsMaxRecordsPerStoreValue.getText());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(rmsPanel, "Not Integer", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (val < 0) {
                    JOptionPane.showMessageDialog(rmsPanel, "Negative Integer", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton deleteButton = new JButton();
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedRows = table.getSelectedRows();
                if (selectedRows.length > 0) {
                    String rmspath = rmsDir;
                    for (int i = 0; i < selectedRows.length; i++) {
                        String filePath = rmspath + pathSeperator + table.getModel().getValueAt(selectedRows[i], 0)
                                + pathSeperator + table.getModel().getValueAt(selectedRows[i], 1);
                        File f = new File(filePath);
                        if (f.exists()) {
                            f.delete();
                        }
                        ((DefaultTableModel) table.getModel()).removeRow(selectedRows[i] - i);
                    }
                }
            }
        });
        bottomBox.add(rmsMaxRecordsPerStoreLabel);
        bottomBox.add(rmsMaxRecordsPerStoreValue);
        bottomBox.add(deleteButton);
        recordstores.add(bottomBox, BorderLayout.SOUTH);
        keyPanel = new JPanel();
        keyPanel.setLayout(new BorderLayout());
        devicePanel = new JPanel();
        platformLabel = new JLabel("Select Platform");
        platformBox = new AutoComplete(deviceNames.toArray());
        platformBox.setEditable(true);
        platformBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateDeviceSettings(devices.get(platformBox.getSelectedIndex()));
            }
        });
        devicePanel.add(platformLabel);
        devicePanel.add(platformBox);
        keyPanel.add(devicePanel, BorderLayout.NORTH);
        keyActionPanel = new JPanel();
        keyActionPanel.setBorder(new TitledBorder("Map Keyboard Keys"));
        keyActionPanel.setLayout(new GridLayout(12, 2));
        tabbedPane.addTab("KeyInfo", null, keyPanel, null);
        // panel = new JPanel();
        // tabbedPane.addTab("New tab", null, panel, null);
        //
        // panel = new JPanel();
        // tabbedPane.addTab("New tab", null, panel, null);
        for (int i = 0; i < keyInfos.length; i++) {
            keyLabels[i] = new JLabel();
            keyLabels[i].setText(labelTexts[i]);
            keyActionPanel.add(keyLabels[i]);
            keyInfos[i] = new JTextField();
            keyInfos[i].setText(KeyEvent.getKeyText(Integer.valueOf(j2seKeyCodesTable.get(labelTexts[i]).toString())));
            keyInfos[i].addKeyListener(this);
            keyInfos[i].setPreferredSize(new Dimension(25, 20));
            keyInfos[i].setColumns(1);
            keyInfos[i].setEditable(false);
            keyInfos[i].setFocusable(true);
            keyActionPanel.add(keyInfos[i]);
        }
        keyPanel.add(keyActionPanel, BorderLayout.CENTER);
        customKeyPanel = new JPanel();
        customKeyPanel.setBorder(new TitledBorder("CustomKeys"));
        customKeyPanel.setLayout(new GridLayout(5, 2));
        for (int j = 0; j < customDeviceKeyLabels.length; j++) {
            customDeviceKeyLabels[j] = new JLabel();
            customDeviceKeyLabels[j].setText(customDeviceKeyLabelTexts[j]);
            customKeyPanel.add(customDeviceKeyLabels[j]);
            customDeviceKeyInfos[j] = new IntegerField(4);
            customDeviceKeyInfos[j].setText(midpKeyCodesTable.get(customDeviceKeyLabelTexts[j]).toString());
            customDeviceKeyInfos[j].addKeyListener(this);
            customDeviceKeyInfos[j].setPreferredSize(new Dimension(25, 20));
            customDeviceKeyInfos[j].setFocusable(true);
            customKeyPanel.add(customDeviceKeyInfos[j]);
        }
        keyPanel.add(customKeyPanel, BorderLayout.SOUTH);
        // after keypanel with customkeys initialize need to override selected device
        if (platformName == null)
            platformName = defaultDevice;
        if ((platformName != null) && !platformName.equals("")) {
            platformBox.setSelectedItem(platformName);
            updateDeviceSettings(devices.get(platformBox.getSelectedIndex()));
        }
        for (int i = 0; i < debugOptions.length; i++) {
            debugOptions[i] = new JCheckBox(debugLabels[i]);
            debugOptions[i].setSelected(debugOptionValues[i]);
            verticalBox.add(debugOptions[i]);
        }
        debugPanel.add(BorderLayout.WEST, verticalBox);
        classPathPanel = new JPanel();
        classPathPanel.setLayout(new BorderLayout());
        eACPanel = new JPanel();
        classpathPanel = new JPanel();
        enableEditandContinue = new JCheckBox();
        classesdirLabel = new JLabel();
        noteLabel = new JLabel();
        noteLabel.setText(editcontinuenote);
        classesPath = new JTextField();
        browseClassPath = new JButton();
        customJarsPanel = new JPanel();
        custotmJarSCrollPane = new JScrollPane();
        customJars = new JTextArea();
        JPanel editcontinueDirPanel = new JPanel();
        editcontinueDirPanel.setLayout(new BorderLayout());
        editcontinueDirPanel.add(classesdirLabel, BorderLayout.WEST);
        editcontinueDirPanel.add(classesPath, BorderLayout.CENTER);
        editcontinueDirPanel.add(browseClassPath, BorderLayout.EAST);
        editcontinueDirPanel.add(enableEditandContinue, BorderLayout.NORTH);
        eACPanel.setBorder(new TitledBorder(null, "Edit And Continue", TitledBorder.LEADING,
                TitledBorder.DEFAULT_POSITION, null, new Color(82, 82, 231)));
        eACPanel.setLayout(new BorderLayout());
        classesPath.setEnabled((classesdir != null && classesdir.length() > 1) ? true : false);
        classesPath.setText((classesdir != null && classesdir.length() > 1) ? classesdir : "");
        enableEditandContinue.setSelected((classesdir != null && classesdir.length() > 1) ? true : false);
        enableEditandContinue.setText("Enable Edit and Continue");
        enableEditandContinue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                classesPath.setEnabled(enableEditandContinue.isSelected());
                browseClassPath.setEnabled(enableEditandContinue.isSelected());
            }
        });
        classesdirLabel.setText("Classes Dir");
        browseClassPath.setText("...");
        browseClassPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser();
                jf.setCurrentDirectory(new File("./"));
                jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int retval = jf.showOpenDialog(MobiEmulator.mobiEmulatorInstance.options);
                if (retval == JFileChooser.APPROVE_OPTION) {
                    File myFile = jf.getSelectedFile();
                    classesPath.setText(myFile.getAbsolutePath().replace("\\", "/"));
                    classesdir = myFile.getAbsolutePath();
                }
            }
        });
        eACPanel.add(noteLabel, BorderLayout.NORTH);
        eACPanel.add(editcontinueDirPanel, BorderLayout.CENTER);
        classPathPanel.add(eACPanel, BorderLayout.NORTH);
        customJarsPanel.setBorder(new TitledBorder(null, "Custom Jars", TitledBorder.LEADING,
                TitledBorder.DEFAULT_POSITION, null, Color.blue));
        customJarsPanel.setLayout(new BorderLayout());
        customJars.setText(customjarsEntry);
        custotmJarSCrollPane.setViewportView(customJars);
        customJarsPanel.add(custotmJarSCrollPane, BorderLayout.CENTER);
        classPathPanel.add(customJarsPanel, BorderLayout.CENTER);
        tabbedPane.add("ClassPath", classPathPanel);
        noteLabel = null;
        //
    }

    public KeyActionMapping[] getKeys() {
        updateKeyMappings(j2seKeyCodesTable, midpKeyCodesTable);
        // keys = new KeyActionMapping []
        // {
        // new KeyActionMapping (KeyEvent.VK_UP, Canvas.UP, KEY_UP_ARROW, "UP"
        // ),
        // new KeyActionMapping (KeyEvent.VK_DOWN, Canvas.DOWN, KEY_DOWN_ARROW,
        // "DOWN" ),
        // new KeyActionMapping (KeyEvent.VK_LEFT, Canvas.LEFT, KEY_LEFT_ARROW,
        // "LEFT" ),
        // new KeyActionMapping (KeyEvent.VK_RIGHT, Canvas.RIGHT,
        // KEY_RIGHT_ARROW, "RIGHT" ),
        // new KeyActionMapping (KeyEvent.VK_ENTER, Canvas.FIRE, KEY_FIRE,
        // "FIRE" ),
        // new KeyActionMapping (KeyEvent.VK_NUMPAD0, 0, Canvas.KEY_NUM0, "0" ),
        // new KeyActionMapping (KeyEvent.VK_NUMPAD1, 0, invert_numpad ?
        // Canvas.KEY_NUM7 : Canvas.KEY_NUM1, "1" ),
        // new KeyActionMapping (KeyEvent.VK_NUMPAD2, 0, invert_numpad ?
        // Canvas.KEY_NUM8 : Canvas.KEY_NUM2, "2" ),
        // new KeyActionMapping (KeyEvent.VK_NUMPAD3, 0, invert_numpad ?
        // Canvas.KEY_NUM9 : Canvas.KEY_NUM3, "3" ),
        // new KeyActionMapping (KeyEvent.VK_NUMPAD4, 0, Canvas.KEY_NUM4, "4" ),
        // new KeyActionMapping (KeyEvent.VK_NUMPAD5, 0, Canvas.KEY_NUM5, "5" ),
        // new KeyActionMapping (KeyEvent.VK_NUMPAD6, 0, Canvas.KEY_NUM6, "6" ),
        // new KeyActionMapping (KeyEvent.VK_NUMPAD7, 0, invert_numpad ?
        // Canvas.KEY_NUM1 : Canvas.KEY_NUM7, "7" ),
        // new KeyActionMapping (KeyEvent.VK_NUMPAD8, 0, invert_numpad ?
        // Canvas.KEY_NUM2 : Canvas.KEY_NUM8, "8" ),
        // new KeyActionMapping (KeyEvent.VK_NUMPAD9, 0, invert_numpad ?
        // Canvas.KEY_NUM3 : Canvas.KEY_NUM9, "9" ),
        // new KeyActionMapping (KeyEvent.VK_MULTIPLY, 0, Canvas.KEY_STAR, "*"
        // ),
        // new KeyActionMapping (KeyEvent.VK_DIVIDE, 0, Canvas.KEY_POUND, "#" ),
        // new KeyActionMapping (KeyEvent.VK_F9, Canvas.GAME_A, KeyEvent.VK_F9,
        // "GAME_A" ),
        // new KeyActionMapping (KeyEvent.VK_F10, Canvas.GAME_B,
        // KeyEvent.VK_F10, "GAME_B" ),
        // new KeyActionMapping (KeyEvent.VK_F11, Canvas.GAME_C,
        // KeyEvent.VK_F11, "GAME_C" ),
        // new KeyActionMapping (KeyEvent.VK_F12, Canvas.GAME_D,
        // KeyEvent.VK_F12, "GAME_D" ),
        //
        // new KeyActionMapping (KeyEvent.VK_F1, 0, KEY_SOFTKEY1, "SOFTKEY1" ),
        // new KeyActionMapping (KeyEvent.VK_F2, 0, KEY_SOFTKEY2, "SOFTKEY2" ),
        // new KeyActionMapping (KeyEvent.VK_INSERT, 0, KEY_SEND, "SEND" ),
        // new KeyActionMapping (KeyEvent.VK_PAGE_UP, 0, KEY_END, "END" ),
        // new KeyActionMapping (KeyEvent.VK_HOME, 0, KEY_SOFTKEY3, "SOFTKEY3"
        // ),

        // new KeyActionMapping (KeyEvent.VK_W, UP, KEY_UP_ARROW, "UP_ARROW" ),
        // new KeyActionMapping (KeyEvent.VK_Z, DOWN, KEY_DOWN_ARROW,
        // "DOWN_ARROW" ),
        // new KeyActionMapping (KeyEvent.VK_A, LEFT, KEY_LEFT_ARROW,
        // "LEFT_ARROW" ),
        // new KeyActionMapping (KeyEvent.VK_S, RIGHT, KEY_RIGHT_ARROW,
        // "RIGHT_ARROW" ),
        // };
        return keys;
    }

    void setDeveloperMode(boolean val) {
        isDevelopermode = val;
    }

    private void updateRecordStore() {
        totalrecordlist = new Vector();
        // records=new Vector<String>();
        // if(!new File("./settings.txt").exists())
        {
            File rmsDirectory = new File(rmsDir);
            if (rmsDirectory.exists()) {
                String[] rmsList = rmsDirectory.list();
                if (rmsList != null) {
                    for (String aRmsList : rmsList) {
                        // System.out.println(">>> " + rmsDir + pathSeperator +
                        // rmsList[i]);
                        if (aRmsList.startsWith(rmsRecordStoreIdentifier)) {
                            File recordstore = new File(rmsDir + pathSeperator + aRmsList);
                            String recordslist[] = recordstore.list();
                            for (int j = 0; j < recordslist.length; j++) {
                                if (recordslist[j].endsWith(rmsExtension)) {
                                    records = new Vector();
                                    File record = new File(rmsDir + pathSeperator + aRmsList + pathSeperator
                                            + recordslist[j]);
                                    records.addElement(aRmsList);
                                    records.addElement(record.getName());
                                    records.addElement(record.length() + "bytes");
                                    records.addElement(new Date(record.lastModified()));
                                    totalrecordlist.addElement(records);
                                }
                            }
                        }
                    }
                }
            }
        }
        table = new JTable(totalrecordlist, colunnames) {
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        };
        if (scrollPane != null) {
            scrollPane.setViewportView(table);
        }
    }

    public String getRmsPath() {
        return rmsDir;
    }

    private void updateKeys() {
        updateKeyMappings(j2seKeyCodesTable, midpKeyCodesTable);
    }

    private void updateRmsPath(String path) {
        RecordStore.RMSDirectory = path;
    }

    private void updateMidpKey(JLabel jLabel, int keyCode) {
        customDeviceKeyCodes[customDeviceSelectedIndex] = keyCode;
        midpKeyCodesTable.put(jLabel.getText(), keyCode);
    }

    private void updateCustomJars(String customjars) {
        if (customjars == null || customjarsEntry.equals("")) {
            return;
        }
        if (customjarsEntry.trim().length() > 0) {
            customJarPaths = customjarsEntry.split(";");
        }
//      customJarPaths=new String[temp.length];
//      System.arraycopy(temp, 0, customJarPaths, 0, temp.length);
//      temp=null;
//        
    }

    private void updateSettings() {
        updateKeys();
        RecordStore.maxRecords = getMaxRecordsPerStore();
        updateRmsPath(getRmsPath());
        if (customjarsEntry != null && !customjarsEntry.equals(""))
            updateCustomJars(customjarsEntry);
        mainInstance.updateDebugSettings();
    }

    public void writeSettings() {
        String filename = "settings.txt";
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(new File(filename));

            /** Debug Parameters */
            for (int i = 0; i < debugOptions.length; i++) {
                debugOptionValues[i] = debugOptions[i].isSelected();
                fo.write((debugLabels[i] + "=" + (debugOptions[i].isSelected() ? "TRUE"
                        : "FALSE")).getBytes());
                fo.write(nextLine);
            }

            /* Writing rms Path */
            fo.write(rmsPathSettingsHeader.getBytes());
            fo.write(nextLine);
            fo.write(("RMSPATH=" + rmsDirPath.getText()).getBytes());
            fo.write(nextLine);

            /* writing key settings */
            // getting custom midp values from textboxes
            for (int i = 0; i < customDeviceKeyLabelTexts.length; i++) {
                midpKeyCodesTable.put(customDeviceKeyLabelTexts[i],
                        Integer.parseInt(customDeviceKeyInfos[i].getText()));
            }
            fo.write(mapkeySettingsHeader.getBytes());
            fo.write(nextLine);
            Enumeration j2sekeyElements = j2seKeyCodesTable.keys();
            while (j2sekeyElements.hasMoreElements()) {
                String curMapKey = j2sekeyElements.nextElement().toString();
                fo.write(("MAP_" + curMapKey + "="
                        + j2seKeyCodesTable.get(curMapKey)).getBytes());
                fo.write(nextLine);
            }
            fo.write(customkeySettingsHeader.getBytes());
            fo.write(nextLine);
            Enumeration midpkeyElements = midpKeyCodesTable.keys();
            while (midpkeyElements.hasMoreElements()) {
                String currentKey = midpkeyElements.nextElement().toString();
                int len = customDeviceKeyLabelTexts.length;
                for (String customDeviceKeyLabelText : customDeviceKeyLabelTexts) {
                    if (customDeviceKeyLabelText.startsWith(currentKey)) {
                        fo.write(("CUSTOM_" + currentKey + "="
                                + midpKeyCodesTable.get(currentKey).toString()).getBytes());
                        fo.write(nextLine);
                    }
                }
            }
            // writing mru list
            for (int i = 0; i < recentUrls.size(); i++) {
                fo.write(("RecentMRU" + i + "=" + (String) recentUrls.get(i)).getBytes());
                fo.write(nextLine);
            }
            // adding mode
            fo.write(("MODE=" + (isDevelopermode ? "TRUE"
                    : "FALSE")).getBytes());
            fo.write(nextLine);
            fo.write(("Platform=" + platformBox.getSelectedItem().toString()).getBytes());
            fo.write(nextLine);
            fo.write(("Classespath=" + classesPath.getText()).getBytes());
            fo.write(nextLine);
            customjarsEntry = customJars.getText();
            fo.write(("CustomJars=" + customJars.getText()).getBytes());
            fo.write(nextLine);
            fo.close();
        } catch (Exception ex) {
            Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
        }
        // readSettings(filename);
        // updateKeys();
    }

    private void readSettings(String filename) {
        Vector contents = new Vector();
        System.out.println("Settings File: "+filename);
        if (!new File(filename).exists()) {
            return;
        }
        BufferedReader reader = null;
        {
            try {
                reader = new BufferedReader(new FileReader(filename));
                String text = null;
                // repeat until all lines is read
                while ((text = reader.readLine()) != null) {
                    contents.addElement(text);
                    if (!text.startsWith("//") && !text.equals(" "))    // ignoring commented setting
                    {
                        System.out.println(text);
                        settingsTable.put(text.substring(0, text.indexOf("=")), text.substring(text.indexOf("=") + 1));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String content[] = new String[contents.size()];
        contents.copyInto(content);
        // all contents readed
        getDebugPanelFromContent(settingsTable);
        getRMSPanelFromContent(settingsTable);
        j2seKeyCodesTable = getKeysPanelFromContent(settingsTable, labelTexts, j2seKeyCodesTable, "MAP_");
        midpKeyCodesTable = getKeysPanelFromContent(settingsTable, customDeviceKeyLabelTexts, midpKeyCodesTable,
                "CUSTOM_");
        updateKeyMappings(j2seKeyCodesTable, midpKeyCodesTable);
        // System.out.println(contents.toString());
        // read mru list
        String str = null;
        for (int i = 0; i < recentMRUCount; i++) {
            str = (String) settingsTable.get("RecentMRU" + i);
            if (str != null) {
                recentUrls.add(str);
            }

        }
        String mode = (String) settingsTable.get("MODE");
        isDevelopermode = (mode == null) ? true
                : ((String) settingsTable.get("MODE")).equals("TRUE") ? true
                : false;
        platformName = (String) settingsTable.get("Platform");
        classesdir = (String) settingsTable.get("Classespath");
        customjarsEntry = (String) settingsTable.get("CustomJars");
        // customJarPaths = customjarsEntry.split(";");
    }

    private void updateDeviceSettings(Device selectedDevice) {
        if (selectedDevice == null) {
            return;
        }
        int count = 0;
        customDeviceKeyInfos[count++].setText(selectedDevice.lskKey);
        customDeviceKeyInfos[count++].setText(selectedDevice.rskKey);
        customDeviceKeyInfos[count++].setText(selectedDevice.upKey);
        customDeviceKeyInfos[count++].setText(selectedDevice.downKey);
        customDeviceKeyInfos[count++].setText(selectedDevice.leftKey);
        customDeviceKeyInfos[count++].setText(selectedDevice.rightKey);
        customDeviceKeyInfos[count++].setText(selectedDevice.centerKey);
        mainInstance.setInputWidthHeight(selectedDevice.width, selectedDevice.height);
        mainInstance.updateScreenSize(Integer.parseInt(selectedDevice.width), Integer.parseInt(selectedDevice.height));
        this.platformBox.requestFocus();
    }

    private void readDevices(String filename) {
        if (filename.equals("") || (filename == null)) {
            return;
        }
        String text = null;
        BufferedReader reader = null;
        try {
            GZIPInputStream gzipDevices = new GZIPInputStream(this.getClass().getResourceAsStream(filename));
            reader = new BufferedReader(new InputStreamReader(gzipDevices));
            while ((text = reader.readLine()) != null) {
                // System.out.println("text is "+text);
                if (!text.startsWith("//")) {
                    devices.addElement(getDevice(text));
                    deviceNames.addElement(text.substring(0, text.indexOf("#")));
                }
                text = null;
            }
            gzipDevices.close();
            gzipDevices = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                reader = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void exportDeviceSet(String filename) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(new File(filename));
            int len = devices.size();
            for (Device device : devices) {
                printDevice(fout, device);
            }
        } catch (Exception ex) {
            Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fout.close();
                fout = null;
            } catch (IOException ex) {
                Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void printDevice(FileOutputStream fout, Device device) throws IOException {
        fout.write(("<Device Name=\"" + device.deviceName + "\">").getBytes());
        nextLine(fout);
        fout.write(("\t<Name=WIDTH value=\"" + device.width + "\"/>").getBytes());
        nextLine(fout);
        fout.write(("\t<Name=HEIGHT value=\"" + device.height + "\"/>").getBytes());
        nextLine(fout);
        fout.write(("\t<Name=LeftSoftKey value=\"" + device.lskKey + "\"/>").getBytes());
        nextLine(fout);
        fout.write(("\t<Name=RightSoftKey value=\"" + device.rskKey + "\"/>").getBytes());
        nextLine(fout);
        fout.write(("\t<Name=Fire value=\"" + device.centerKey + "\"/>").getBytes());
        nextLine(fout);
        fout.write(("\t<Name=Up value=\"" + device.upKey + "\"/>").getBytes());
        nextLine(fout);
        fout.write(("\t<Name=Down value=\"" + device.downKey + "\"/>").getBytes());
        nextLine(fout);
        fout.write(("\t<Name=Left value=\"" + device.leftKey + "\"/>").getBytes());
        nextLine(fout);
        fout.write(("\t<Name=Right value=\"" + device.rightKey + "\"/>").getBytes());
        nextLine(fout);
        fout.write(("</Device>").getBytes());
        nextLine(fout);
    }

    private void nextLine(FileOutputStream fout) throws IOException {
        fout.write(nextLine);
    }

    private Device getDevice(String text) {
        if (text.equals("")) {
            return null;
        }
        Device device = new Device();
        String separator = "#";
        int index = 0;
        int colIndex = 0;
        int previousIndex = 0;
        String value="";
        while ((index = text.indexOf(separator, previousIndex)) != -1) {
            value = text.substring(previousIndex, index);
            switch (colIndex) {
                case 0:
                    device.deviceName = value;

                    break;
                case 1:
                    device.width = value;

                    break;
                case 2:
                    device.height = value;

                    break;
                case 3:
                    device.lskKey = value;

                    break;
                case 4:
                    device.rskKey = value;

                    break;
                case 5:
                    device.centerKey = value;

                    break;
                case 6:
                    device.upKey = value;

                    break;
                case 7:
                    device.downKey = value;

                    break;
                case 8:
                    device.leftKey = value;

                    break;
                case 9:
                    device.rightKey = value;

                    break;
                case 10:
                    device.jarLimit = value;

                    break;
                case 11:
                    device.heap = value;

                    break;
                case 12:
                    // device.rightKey=text.substring(previousIndex, index);
                    break;
            }
            colIndex++;
            previousIndex = index + 1;
        }
        index = 0;
        colIndex = 0;
        previousIndex = 0;
        separator = null;

        return device;
    }

    public Vector getMRUList() {
        return recentUrls;
    }

    public void setMRUList(String items[]) {
        recentUrls.removeAllElements();
        for (String item : items) {
            recentUrls.addElement(item);
        }

    }

    public boolean getMode() {
        return isDevelopermode;
    }

    private void getDebugPanelFromContent(Hashtable content) {
        for (int i = 0; i < debugLabels.length; i++) {
            if (content.containsKey(debugLabels[i])) {
                // System.out.println(">> " + debugLabels[i] + " : " +
                // content.get(debugLabels[i]).toString());
                debugOptionValues[i] = (content.get(debugLabels[i]).toString().equals("TRUE") ? true
                        : false);
            }
        }
    }

    private void getRMSPanelFromContent(Hashtable content) {
        if (content.containsKey("RMSPATH")) {
            String rmsRoot = content.get("RMSPATH").toString();
            rmsDir = rmsRoot;
        }
    }

    private Hashtable getKeysPanelFromContent(Hashtable content, String[] keyLabels, Hashtable keyTable,
                                              String customParam) {
        for (String getKey : keyLabels) {
            if (content.containsKey(customParam + getKey)) {
                keyTable.put(getKey, content.get(customParam + getKey));
            }
        }

        return keyTable;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        selectedindex = -1;
        for (int i = 0; i < keyInfos.length; i++) {
            if (keyInfos[i].hasFocus() && !keyInfos[i].isEditable()) {
                selectedindex = i;
            }
        }
        // customDeviceSelectedIndex = -1;
        // for (int i = 0; i < customDeviceKeyInfos.length; i++) {
        // if (customDeviceKeyInfos[i]!=null&&customDeviceKeyInfos[i].hasFocus()
        // && customDeviceKeyInfos[i].isEditable()) {
        // customDeviceSelectedIndex = i;
        // }
        // }
        if (selectedindex != -1) {
            keyInfos[selectedindex].setText("" + KeyEvent.getKeyText(e.getKeyCode()).toUpperCase());
        }
    }

    public void keyReleased(KeyEvent e) {
        if (selectedindex != -1) {
            keyCodes[selectedindex] = e.getKeyCode();
            keyInfos[selectedindex].setText("" + KeyEvent.getKeyText(keyCodes[selectedindex]).toUpperCase());
            j2seKeyCodesTable.put(keyLabels[selectedindex].getText(), e.getKeyCode());
        }
        // if (customDeviceSelectedIndex != -1) {
        // updateMidpKey(customDeviceKeyLabels[customDeviceSelectedIndex],
        // e.getKeyCode());
        // for (int i = 0; i < keyInfos.length; i++) {
        // System.out.println(">> " + keyLabels[i].getText() + " value " +
        // keyInfos[i].getText());
        // }
        // }
    }

    private void updateKeyMappings(Hashtable j2seKeyCodesTable, Hashtable midpKeyCodesTable) {
        Enumeration keysList = j2seKeyCodesTable.keys();
        if (!keysList.hasMoreElements()) {
            // System.out.println("again loading settings");
            loadDefaultSettings();
        }
        // while(keysList.hasMoreElements())
        // {
        // System.out.println(" value is "+j2seKeyCodesTable.get(keysList.nextElement()).toString());
        // }
        for (int i = 0; (i < keys.length) && keysList.hasMoreElements(); i++) {
            Object key = keysList.nextElement();
            keys[i] = new KeyActionMapping(Integer.parseInt(j2seKeyCodesTable.get(key).toString()),
                    gameActionValues[i],
                    Integer.parseInt(midpKeyCodesTable.get(key).toString()), key.toString());
        }
        //
    }

    private void loadDefaultSettings() {
        for (int i = 0; i < defualtKeyCodes.length; i++) {
            j2seKeyCodesTable.put(labelTexts[i], defualtKeyCodes[i]);
            midpKeyCodesTable.put(labelTexts[i], defaultMidpKeyCodes[i]);
        }
        updateKeyMappings(j2seKeyCodesTable, midpKeyCodesTable);
        rmsDir = "./RMS";
        debugOptionValues[CacheJarResources] = true;
        debugOptionValues[KeyRepeatedEvents] = true;
        debugOptionValues[PointerEvents] = true;
    }

    public boolean isDebugOptionEnabled(int option) {
        return debugOptionValues[option];
    }

    public void deInitialize() {
        devices.removeAllElements();
        devices = null;
    }

    public int getMaxRecordStores() {
        return Integer.parseInt(rmsMaxRecordStoreValue.getText());
    }

    public int getMaxRecordsPerStore() {
        try {
            return Integer.parseInt(rmsMaxRecordsPerStoreValue.getText());
        } catch (Exception e) {
            return -1;
        }
    }
}
