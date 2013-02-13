package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.imageio.ImageIO;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.KeyActionMapping;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MidletListener;
import javax.microedition.midlet.MidletUtils;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.wireless.messaging.Message;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Ashok Kumar Gujarathi
 */

public class MobiEmulator extends JFrame {
    public static final String EMULATOR_TITLE = "MobiEmulator";
    private static BufferedImage midletIcon=null;
    public static boolean isDebugOn = !true;
    private static Attributes jadProperties = new Attributes();
    public static boolean isPrintOff = false;
    public static boolean isAlwaysShowLog = false;
    private static String emulatorPath = new java.io.File("").getAbsolutePath();
    public static boolean cacheJarResources = !true;
    public static int totalByteCodeSize = 0;
    private static JarResources cachedJarResources;
    public static MobiEmulator mobiEmulatorInstance;
    private static String openJarFilePath;
    private static JarResources buildClasses;
    public static JarFile jarfile;
    public int canvasPanelHeight = 320;
    public int canvasPanelWidth = 240;
    public int fpsValue = 55;
    private int frameHeight = 320;
    private int frameWidth = 240;
    private int MenuBarHeight = 25;
    private File openJarfile = null;
    private int StatusPanelHeight = 20;
    private int ToolBarHeight = 25;
    public boolean isCurrentJarUnloaded = false;
    public boolean performanceInvestigate = false;
    public int paintRecordCount = 0;
    private float windowZoom = 1.0f;
    public boolean showkeypad = false;
    private JMenuItem recentList[] = new JMenuItem[5];
    private String recentJarUrls[] = new String[recentList.length];
    boolean monitorNewTraces = false;
    boolean monitorMethodCalls = false;
    boolean methodByteCode = false;
    public Vector jarClasses = new Vector();
    public boolean isProfilerViewEnabled = false;
    boolean isMethodViewEnabled = false;
    boolean isMemoryViewEnabled = false;
    public boolean isLoggerEnabled = false;
    private boolean isKeyReleased = true;    // at starting key is released state
    private boolean isJadCreateEnabled = false;
    public boolean isForceRepaint = !true;
    private boolean isAlwaysOnTop = false;
    private Hashtable buildContents = new Hashtable();
    private boolean beanShellVisible = false;
    private JMenuItem AlwaysTop;
    private JMenuItem BicubicQuality;
    private JMenuItem BilinearQuality;
    private JMenuItem CanvasOrientation;
    public MainCanvasPanel canvasPanel;
    private JMenu qualitySubMenu;
    private JMenuItem CanvasRotation;
    private JMenu emulEditMenu;
    private JMenuItem EmulLog;
    private JMenuItem emulMemoryview;
    private JMenuBar emulMenuBar;
    private JMenu emulOptions;
    private JToolBar emulToolBar;
    private IntegerField emulatorHeightInput;
    private JLabel emulatorWidth;
    private IntegerField emulatorWidthInput;
    private JMenuItem Exit;
    private JSlider fpsslider;
    private JMenu fileMenu;
    private JButton infoButton;
    private JMenuItem infoMenuItem;
    private KeyPad keyPadFrame;
    public String mainclass;
    private Attributes manifestAttributes;
    private Properties manifestProperties;
    public MultiClassLoader multiClassLoader;
    private JMenuItem NearestNeighboutQuality;
    private String openjarfilename;
    private JMenuItem openJarMenuItem;
    public JMenuItem resumeMenuItem;
    private JMenuItem Settings;
    public MainStatusPanel statusPanel;
    public JMenuItem suspendMenuitem;
    private JMenuItem xrayView;
    private JMenuItem about;
    private About aboutBox;
    private JMenuItem beanShell;
    private BeanShell bsh;
    private String buildClassesDir;
    private JMenuItem callSmsServiceItem;
    private JMenuItem captureAVIStart;
    private JMenuItem captureAVIStop;
    private JMenuItem captureArea;
    private JMenuItem captureGIFStart;
    private JMenuItem captureGIFStop;
    private JMenuItem captureScreenShot;
    private JMenuItem captureToClipboard;
    private JCheckBoxMenuItem ccwCanvasRotation;
    public CallSMSManager csManager;
    private JCheckBoxMenuItem developerMode;
    private JLabel fpsLabel;
    private JMenu helpMenu;
    private InputStream jarInputStream;
    private JMenuItem keyPadItem;
    private ZipEntry localZipEntry;

    /* Log Frame */
    public LogFrame logFrame;
    public JFrame memoryFrame;
    MethodFrame methodFrame;
    private JMenuItem methodView;
    private MIDlet midlet;
    private JMenu modeItem;
    MemoryView mvf;
    private JMenuItem newWindow;

    /* options frame */
    public Options options;
    private JMenuItem paintInvestigator;
    public PaintFrameStack paintStackFrame;
    private JCheckBoxMenuItem playerMode;
    private JMenuItem profilerMenuItem;
    public Profiler profilerframe;
    private JMenuItem recentMRU1;
    private JMenuItem recentMRU2;
    private JMenuItem recentMRU3;
    private JMenu recentMenuItem;
    private JMenuItem restartMenuItem;
    private JMenuItem saveJarMenuItem;
    private JButton settingButton;
    private JMenuItem unloadJarOption;
    private MultiClassLoader urlClassLoader;
    private JMenuItem zoomIn;
    private JMenuItem zoomOut;
    private JMenuItem settingsItem;
    private JPopupMenu dimensionPopup;
    private JButton dimensionButton;
    private JPopupMenu fpsPopupMenu;
    private JButton fpsPopButton;
    private JButton newWindowButton;
    private JButton memoryViewButton;
    private JButton paintrecorderButton;


    private final String captureText = "Capture";
    private final String captureScreenAreaText = "CaptureScreenArea";


    /**
     * Launch the application
     *
     * @param args
     */

    /**
     * Create the frame
     */
    public MobiEmulator() {

        mobiEmulatorInstance = this;
        setTitle(EMULATOR_TITLE);
        setResizable(false);
        setSystemProperties();

        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (options != null) {
                    options.setMRUList(recentJarUrls);
                    options.writeSettings();
                }
            }


        });
        JComponent MainComponent = (JComponent) getContentPane();
        updateDebugSettings();
        MainComponent.setTransferHandler(new MyFileTransferHandler(mobiEmulatorInstance));    // see
        // below
        bsh = new BeanShell();
        addWindowListener(new WindowAdapter() {
            public void windowIconified(final WindowEvent arg0) {
            }

            public void windowDeiconified(final WindowEvent e) {
            }

            public void windowClosed(final WindowEvent e) {
                System.exit(0);
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasPanel = new MainCanvasPanel(mobiEmulatorInstance, frameWidth, frameHeight);
        options = new Options(this);
        csManager = new CallSMSManager();
        logFrame = new LogFrame(mobiEmulatorInstance);
        mvf = new MemoryView();
        paintStackFrame = new PaintFrameStack();
        profilerframe = new Profiler(mobiEmulatorInstance);
        methodFrame = new MethodFrame();
        keyPadFrame = new KeyPad(mobiEmulatorInstance);
        keyPadFrame.setVisible(showkeypad);
        loadDefaultIcon();
        // canvasPanel.setCanvasWidth(canvasPanelWidth);
        // canvasPanel.setCanvasHeight(canvasPanelHeight);
        if (mobiEmulatorInstance.canvasPanel.DemoVersion) {
            JLabel demoLabel = new JLabel();
            demoLabel.setText(
                    "<html>DEMO VERSION <br> In DEVELOPMENT VERSION<br>SUPPORTED MIDP 2.0 <br>AND NO OTHER SPECIFIC APIs [M3G]<br><br><br>Drag and Drop Jar files here</html>");
            canvasPanel.add(demoLabel, BorderLayout.CENTER);
        }
        setSize(new Dimension(frameWidth, frameHeight + MenuBarHeight + ToolBarHeight + StatusPanelHeight));
        getContentPane().add(canvasPanel, BorderLayout.WEST);

        options.getMRUList().copyInto(recentJarUrls);


        emulMenuBar = new JMenuBar();
        setJMenuBar(emulMenuBar);
        fileMenu = new JMenu();
        fileMenu.setActionCommand("Menu");
        fileMenu.setText("Menu");
        emulMenuBar.add(fileMenu);

        createMenuItem(fileMenu, Constants.newWindowText, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_MASK));
        createMenuItem(fileMenu, Constants.openJarText, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_MASK));
        createMenuItem(fileMenu, Constants.saveText, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        createMenuItem(fileMenu, Constants.suspendText, KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        createMenuItem(fileMenu, Constants.resumeText, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        createMenuItem(fileMenu, Constants.restartText, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_MASK));


        recentMenuItem = new JMenu();
        recentMenuItem.setText("Recent JarFiles");
        updateMRUList(recentMenuItem);

        fileMenu.add(recentMenuItem);
        createMenuItem(fileMenu, Constants.exitText, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        emulEditMenu = new JMenu();
        emulEditMenu.setActionCommand("Edit");
        emulEditMenu.setText("Edit");
        emulMenuBar.add(emulEditMenu);

        createMenuItem(emulEditMenu, Constants.captureText, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK));
        createMenuItem(emulEditMenu, Constants.captureScreenAreaText, KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));
        createMenuItem(emulEditMenu, Constants.captureToClipboardText, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        createMenuItem(emulEditMenu, Constants.captureGifStartText, KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.ALT_MASK));
        createMenuItem(emulEditMenu, Constants.captureGifStopText, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_MASK));
        createMenuItem(emulEditMenu, Constants.captureAVIStartText, KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_MASK));
        createMenuItem(emulEditMenu, Constants.captureAVIStopText, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_MASK));
        createMenuItem(emulEditMenu, Constants.zoomInText, KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0));
        createMenuItem(emulEditMenu, Constants.zoomOutText, KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0));
        emulEditMenu.addSeparator();
        createMenuItem(emulEditMenu, Constants.settingsText, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_MASK + KeyEvent.CTRL_MASK));

        emulOptions = new JMenu();
        emulOptions.setActionCommand("Options");
        emulOptions.setText("Options");
        emulMenuBar.add(emulOptions);

        createMenuItem(emulOptions, Constants.alwaysTopText, KeyStroke.getKeyStroke(KeyEvent.VK_T, 0));
        createMenuItem(emulOptions, Constants.beanShellText, KeyStroke.getKeyStroke(KeyEvent.VK_B, 0));
        EmulLog = createMenuItem(emulOptions, Constants.logText, KeyStroke.getKeyStroke(KeyEvent.VK_L, 0));
        createMenuItem(emulOptions, Constants.infoText, KeyStroke.getKeyStroke(KeyEvent.VK_I, 0));
        methodView = createMenuItem(emulOptions, Constants.methodsText, KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK));
        emulMemoryview = createMenuItem(emulOptions, Constants.memoryViewText, KeyStroke.getKeyStroke(KeyEvent.VK_M, 0));
        createMenuItem(emulOptions, Constants.keypadText, KeyStroke.getKeyStroke(KeyEvent.VK_K, 0));
        createMenuItem(emulOptions, Constants.canvasOrientationText, KeyStroke.getKeyStroke(KeyEvent.VK_O, 0));
        createMenuItem(emulOptions, Constants.canvasRotationText, KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0));
        profilerMenuItem = createMenuItem(emulOptions, Constants.profilerText, KeyStroke.getKeyStroke(KeyEvent.VK_P, 0));
        createMenuItem(emulOptions, Constants.paintInvestigatorText, KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));


        ccwCanvasRotation = new JCheckBoxMenuItem();
        ccwCanvasRotation.setText("CCW Rotation");
        ccwCanvasRotation.setSelected(false);
        emulOptions.add(ccwCanvasRotation);

        qualitySubMenu = new JMenu();
        qualitySubMenu.setText("Quality");
        emulOptions.add(qualitySubMenu);
        createMenuItem(qualitySubMenu, Constants.bicubicQualityText, KeyStroke.getKeyStroke(KeyEvent.VK_B, 0));
        createMenuItem(qualitySubMenu, Constants.nearestNeighbourText, KeyStroke.getKeyStroke(KeyEvent.VK_N, 0));
        createMenuItem(qualitySubMenu, Constants.bilinearText, KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        createMenuItem(emulOptions, Constants.unloadJarText, KeyStroke.getKeyStroke(KeyEvent.VK_U, 0));
        createMenuItem(emulOptions, Constants.xrayViewText, KeyStroke.getKeyStroke(KeyEvent.VK_X, 0));
        createMenuItem(emulOptions, Constants.callsmsText, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK));

        Settings = new JMenuItem();
        Settings.setText("Settings");
        Settings.setActionCommand("Settings");
        Settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        Settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (options != null) {
                    options.setVisible(!options.isVisible());
                }
            }
        });
        emulToolBar = new JToolBar();
        emulToolBar.setFloatable(false);
        emulToolBar.setFocusable(false);
        emulToolBar.setPreferredSize(new Dimension(30, 30));
        getContentPane().add(emulToolBar, BorderLayout.NORTH);

        newWindowButton = createToolBarButton(emulToolBar, getImageIcon(Constants.newwindowicon), "New Window");
        createDimensionPopup();
        dimensionButton = createToolBarButton(emulToolBar, getImageIcon(Constants.widthheightIcon), "ResizeScreen");
        infoButton = new JButton();
        infoButton.addActionListener(new ActionListener() {
            private boolean isInfoVisible;

            public void actionPerformed(final ActionEvent a) {
                canvasPanel.isInfoVisible = !canvasPanel.isInfoVisible;
                // canvasPanel.updateInfoFramePos();
                canvasPanel.infoFrame.setVisible(canvasPanel.isInfoVisible);
                canvasPanel.requestFocus();
            }
        });
        infoButton.setText("i");

        createFpsPopup();
        fpsPopButton = createToolBarButton(emulToolBar, getImageIcon(Constants.fpsIcon), "FPS");
        memoryViewButton = createToolBarButton(emulToolBar, getImageIcon(Constants.memoryviewIcon), "MemoryView");
        paintrecorderButton = createToolBarButton(emulToolBar, getImageIcon(Constants.paintrecorderIcon), "PaintRecorder");
        settingButton = createToolBarButton(emulToolBar, getImageIcon(Constants.settingsicon), "Settings");
        settingButton.setBorderPainted(false);

        modeItem = new JMenu("Mode");
        developerMode = new JCheckBoxMenuItem();
        developerMode.setText("Developer");
        developerMode.setState(options.getMode());
        developerMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDevloperMode(true);
                options.setDeveloperMode(true);
                playerMode.setState(false);
                developerMode.setState(true);
            }
        });
        playerMode = new JCheckBoxMenuItem();
        playerMode.setText("Player");
        playerMode.setState(!options.getMode());
        playerMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                options.setDeveloperMode(false);
                setDevloperMode(false);
                developerMode.setState(false);
                playerMode.setState(true);
            }
        });
        setDevloperMode(options.getMode());
        modeItem.add(developerMode);
        modeItem.add(playerMode);
        emulMenuBar.add(modeItem);
        helpMenu = new JMenu("Help");
        createMenuItem(helpMenu, Constants.aboutText, KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_MASK));
        // About Box
        aboutBox = new About();
        emulMenuBar.add(helpMenu);
        statusPanel = new MainStatusPanel(mobiEmulatorInstance, frameWidth, StatusPanelHeight);
        statusPanel.setLeftText("LSK");
        statusPanel.setRightText("RSK");
        statusPanel.setCenterText("ZOOM 1x");
        getContentPane().add(statusPanel, BorderLayout.SOUTH);
        pack();
        canvasPanel.requestFocus();
        // load modes
        loadDeveloperMode();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dim.width >> 1) - (getPreferredSize().width >> 1), (dim.height >> 1) - (getPreferredSize().height >> 1));
        System.out.println("MobiEmulator Started..");
    }

    private JButton createToolBarButton(JToolBar toolBar, ImageIcon imageIcon, String tooltip) {
        JButton button = new JButton(imageIcon);
        button.setToolTipText(tooltip);
        button.addActionListener(toolbarListener);
        toolBar.add(button);
        return button;
    }

    private void updateMRUList(JMenu menuitem) {
        recentList = null;
        menuitem.removeAll();

        if (options != null) {
            recentList = new JMenuItem[recentJarUrls.length];
            for (int i = 0; i < recentJarUrls.length; i++) {
                if ((recentJarUrls[i] != null) && !recentJarUrls[i].equals("null")) {
                    recentList[i] = new JMenuItem(recentJarUrls[i]);
                    recentList[i].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JMenuItem item = (JMenuItem) e.getSource();
                            if (item.getText() != null) {
                                setJarFilePath(item.getText());
                                loadJarFile(new File(item.getText()));
                            }
                        }
                    });
                    menuitem.add(recentList[i]);
                }

            }
        }

    }

    public void appendMRUToList(String path) {
        if (recentJarUrls == null)
            return;
        if (recentJarUrls[0] != null && recentJarUrls[0].equals(path))
            return;
        System.arraycopy(recentJarUrls, 0, recentJarUrls, 1, 4);
        recentJarUrls[0] = path;
        updateMRUList(recentMenuItem);

    }

    private ImageIcon getImageIcon(String path) {
        InputStream is = this.getClass().getResourceAsStream(path);
        BufferedImage image = null;
        try {
            image = ImageIO.read(is);
            is.close();
            is = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ImageIcon icon = new ImageIcon(image);
        image = null;
        return icon;

    }

    private void createFpsPopup() {
        fpsPopupMenu = new JPopupMenu();
        fpsPopupMenu.setLayout(new FlowLayout());
        fpsLabel = new JLabel();
        fpsPopupMenu.add(fpsLabel);
        fpsLabel.setText("FPS " + fpsValue);
        fpsslider = new JSlider();
        fpsslider.setLayout(new BorderLayout());
        fpsslider.setMinimumSize(new Dimension(30, 20));
        fpsslider.setPreferredSize(new Dimension(100, 20));
        fpsslider.setValue(fpsValue);
        fpsslider.setMinimum(1);
        fpsslider.setMaximum(100);
        fpsslider.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent ce) {
                fpsValue = ((JSlider) (ce.getSource())).getValue();
                fpsLabel.setText("FPS " + fpsValue);
                canvasPanel.requestFocus();
            }
        });
        fpsPopupMenu.add(fpsslider);


    }

    public void createDimensionPopup() {
        dimensionPopup = new JPopupMenu();
        dimensionPopup.setLayout(new FlowLayout());
        emulatorWidth = new JLabel();
        emulatorWidth.setText("W");
        dimensionPopup.add(emulatorWidth);
        emulatorWidthInput = new IntegerField(3);
        emulatorWidthInput.setColumns(4);
        emulatorWidthInput.setText("240");
        emulatorWidthInput.setPreferredSize(new Dimension(30, 20));
        emulatorWidthInput.setMinimumSize(new Dimension(0, 0));
        emulatorWidthInput.setMaximumSize(new Dimension(20, 20));
        dimensionPopup.add(emulatorWidthInput);
        final JLabel EmulatorHeight = new JLabel();
        EmulatorHeight.setText("H");
        dimensionPopup.add(EmulatorHeight);
        emulatorHeightInput = new IntegerField(3);
        // emulatorHeightInput.setBorder(new IntegerField.RoundedBorder());
        emulatorHeightInput.setColumns(4);
        emulatorHeightInput.setText("320");
        emulatorHeightInput.setMaximumSize(new Dimension(30, 20));
        emulatorHeightInput.setPreferredSize(new Dimension(30, 20));
        dimensionPopup.add(emulatorHeightInput);
        final JButton dimensionUpdate = new JButton();
        dimensionUpdate.setIcon(getImageIcon(Constants.correcticon));
        dimensionUpdate.setPreferredSize(new Dimension(28, 26));
        dimensionUpdate.setBorderPainted(false);
        dimensionUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                if (emulatorWidthInput.getText().equals("") || emulatorHeightInput.getText().equals("")) {
                    return;
                }
                if ((Integer.parseInt(emulatorWidthInput.getText()) > 999)
                        || (Integer.parseInt(emulatorHeightInput.getText()) > 999)) {
                    JOptionPane.showMessageDialog(mobiEmulatorInstance, "Width and height must be <999", "Error",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }
                int Width = Integer.parseInt(emulatorWidthInput.getText());
                int Height = Integer.parseInt(emulatorHeightInput.getText());
                updateScreenSize(Width, Height);
                dimensionPopup.setVisible(false);
            }
        });

        dimensionPopup.add(dimensionUpdate);

    }

    private void setDevloperMode(boolean isDeveloperMode) {
        if (isDeveloperMode) {
            loadDeveloperMode();
        } else {
            loadPlayerMode();
        }
    }

    private void saveCachedJar(String storePath, JarResources jarcache) {
        if ((jarcache == null) || storePath.equals("")) {
            return;
        }
        Hashtable jarfiles = jarcache.getJarCache();
        Enumeration names = jarfiles.keys();
        ZipOutputStream jaroutput = null;
        try {
            jaroutput = new ZipOutputStream(new FileOutputStream(storePath + "\\" + getJarFileName()));
            jaroutput.setLevel(Deflater.BEST_COMPRESSION);
            while (names.hasMoreElements()) {
                String path = ((String) names.nextElement());
                jaroutput.putNextEntry(new ZipEntry(path));
                jaroutput.write((byte[]) jarfiles.get(path));
                jaroutput.closeEntry();
            }
            jaroutput.close();
        } catch (Exception e) {
        }
        createJadFile(storePath + "\\" + getJarFileName());
    }

    private void createNewInstance() {
        String url = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();    // for jar path
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        url = url.replace("%20", " ");
        url = "\"" + url + "\"";
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec("cmd.exe /c start " + (Main.getConsoleEnabled() ? "java"
                    : "javaw") + " -jar " + url + " false" + " " + (++Main.uniqueID));
        } catch (Exception ex) {
            Logger.getLogger(MobiEmulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void openJar(ActionEvent e) {
        // TODO Auto-generated method stub
        JFileChooser jf = new JFileChooser();
        jf.showOpenDialog(this);
        jf.setVisible(true);
        openJarfile = jf.getSelectedFile();
        if (openJarfile != null) {
            setJarFileName(openJarfile.getName());
            setJarFilePath(openJarfile.getAbsolutePath());
            appendMRUToList(openJarfile.getAbsolutePath());
            loadJarFile(openJarfile);
        }
    }

    private void restartJar(String jarFilePath) {
        unLoadCurrentJar();
        loadJarFile(new File(jarFilePath));
    }

    public void updateScreenSize(int Width, int Height) {
        if ((Width == 0) || (Height == 0)) {
            JOptionPane.showMessageDialog(this, "WIDTH OR HEIGHT NOT BE 0", "ERROR", JOptionPane.INFORMATION_MESSAGE);

            return;
        }
        Insets insets = getInsets();
        frameWidth = Width;
        frameHeight = Height;
        setSize(new Dimension((int) (frameWidth) + insets.left + insets.right,
                (int) (frameHeight) + MenuBarHeight + ToolBarHeight + StatusPanelHeight + insets.top
                        + insets.bottom));
        canvasPanel.setPreferredSize(new Dimension(frameWidth, frameHeight));
        setCanvasWidth(frameWidth);
        setCanvasHeight(frameHeight);
        canvasPanel.setCanvasWidth(frameWidth);
        canvasPanel.setCanvasHeight(frameHeight);
        if (canvasPanel.midp_screen_image != null) {
            canvasPanel.midp_screen_image = null;
        }
        if (canvasPanel.midp_screen_backscreen != null) {
            canvasPanel.midp_screen_backscreen = null;
        }
        canvasPanel.midp_screen_image = new BufferedImage(canvasPanel.getCanvasWidth(), canvasPanel.getCanvasHeight(),
                BufferedImage.TYPE_INT_RGB);
        canvasPanel.midp_screen_backscreen = new BufferedImage(canvasPanel.getCanvasWidth(),
                canvasPanel.getCanvasHeight(), BufferedImage.TYPE_INT_RGB);
        canvasPanel.setZoomVal(1.0f);
        canvasPanel.repaint();
        doLayout();
        invalidate();
        repaint();
        canvasPanel.requestFocus();
        if (paintStackFrame != null)
            paintStackFrame.updateImageDimension(frameWidth, frameHeight);

    }

    public void setInfoVisible(boolean visible) {
    }

    public void keyRepeated(KeyEvent ke) {
        if (canvasPanel.getCurrentDisplayable() == null) {
            return;
        }
        if (canvasPanel.isKeyRepeatedSupported) {
            canvasPanel.getCurrentDisplayable().invokeKeyRepeated(ke.getKeyCode());
        }
    }

    public void keyPressed(KeyEvent ke) {
        if (canvasPanel.getCurrentDisplayable() == null) {
            return;
        }
        if (!canvasPanel.isKeyRepeatedSupported) {
            if (isKeyReleased) {
                canvasPanel.getCurrentDisplayable().invokeKeyPressed(ke.getKeyCode());
            }
        } else {
            canvasPanel.getCurrentDisplayable().invokeKeyPressed(ke.getKeyCode());
        }
        isKeyReleased = false;
    }

    public void keyReleased(KeyEvent ke) {
        if (canvasPanel.getCurrentDisplayable() == null) {
            return;
        }
        canvasPanel.getCurrentDisplayable().invokeKeyReleased(ke.getKeyCode());
        isKeyReleased = true;
    }

    public static void setSystemProperties() {
        System.setProperty("microedition.configuration", "CDLC1.1");
        System.setProperty("microedition.profiles", "MIDP2.0");
        // System.setProperty("microedition.m3g.version", "1.1");
        System.setProperty("microedition.encoding", "UTF-8");
        if (System.getProperty("microedition.locale") == null) {
            System.setProperty("microedition.locale", "en_US");
        }
        if (System.getProperty("microedition.platform") == null) {
            System.setProperty("microedition.platform", "SonyEricssonK800i");
        }
        System.setProperty("microedition.media.version", "1.0");
        System.setProperty("supports.mixing", "true");
        System.setProperty("supports.audio.capture", "false");
        System.setProperty("supports.video.capture", "false");
        System.setProperty("supports.recording", "false");
        System.setProperty("bluetooth.api.version", "1.0");
        System.setProperty("microedition.io.file.FileConnection.version", "1.0");
        System.setProperty("fileconn.dir", "file://" + "root/");
        System.setProperty("fileconn.dir.photos", "file://" + "root/photos/");
        System.setProperty("microedition.pim.version", "1.0");
        System.setProperty("wireless.messaging.sms.smsc", "+1001");
        System.setProperty("wireless.messaging.mms.mmsc", "http://www.google.com");
    }

    public void setJarFilePath(String absolutePath) {
        openJarFilePath = absolutePath;

    }

    public void setJarFileName(String name) {
        openjarfilename = name;
    }

    public String getJarFileName() {
        return openjarfilename;
    }

    public static String getJarFilePath() {
        return openJarFilePath;
    }

    public void unLoadCurrentJar() {
        try {
            Thread.currentThread().setContextClassLoader(null);
            // Display.getDisplay(getCurrentMIDlet()).setCurrent(null);
            canvasPanel.midp_screen = null;
            canvasPanel.midp_canvas = null;
            mobiEmulatorInstance.setIconImage(null);
            mvf.deInitialize();
            methodFrame.deInitialize();
            canvasPanel.midp_screen_image = null;
            canvasPanel.midp_screen_backscreen = null;
            // /canvasPanel.deInitialize();
            remove(canvasPanel);
            // repaint();
            mainclass = null;
            jarfile = null;
            jarInputStream = null;
            localZipEntry = null;
            if (multiClassLoader != null) {
                multiClassLoader.deInitialize();
            }
            multiClassLoader = null;
            manifestAttributes = null;
            manifestProperties = null;
            if (cachedJarResources != null) {
                cachedJarResources.deInitialize();
            }
            cachedJarResources = null;
            memoryFrame = null;
            profilerframe.deInitialize();
            System.gc();
            // System.runFinalization();
            repaint();
            add(canvasPanel);
            canvasPanel.requestFocus();
            canvasPanel.midp_screen_image = null;
            canvasPanel.midp_screen_backscreen = null;
            invalidate();
            validate();
            isCurrentJarUnloaded = true;
            loadDefaultIcon();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadJarFile(File openJarfile) {
        // clean previous game or make it clean
        // if (mainclass != null || midlet != null || jarfile != null || cachedJarResources != null)
        {
            unLoadCurrentJar();
        }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        if (openJarfile == null) {
            return;
        }
        updateScreenSize(frameWidth, frameHeight);
        try {
            jarfile = new JarFile(openJarfile);
            String path = openJarfile.getCanonicalPath();
            String jarfilename = path.substring(path.lastIndexOf("\\") + 1);
            logFrame.setLogFile(((Main.uniqueID == 0) ? ""
                    : "Emulator_" + Main.uniqueID) + jarfilename + ".log");
        } catch (IOException e) {
            e.printStackTrace();

            return;
        }
        MidletUtils.getInstance().setMidletListener(midletListener);
        multiClassLoader = new MultiClassLoader();
        Thread.currentThread().setContextClassLoader(multiClassLoader);
        updateDebugSettings();
        // update modes
        // loadPlayerMode();
        localZipEntry = jarfile.getEntry("META-INF/MANIFEST.MF");
        try {
            jarInputStream = jarfile.getInputStream(localZipEntry);
            loadManifest();
            if (jarInputStream != null) {
                jarInputStream.close();
            }
        } catch (Exception e) {
            System.out.println("Exception while loading jar - jar corrupted");
        }
        jarInputStream = null;
        loadJadAttributes(getJarFilePath());
        mainclass = getMainClass(manifestProperties);
        // cacheJarResources=false;
        if (cacheJarResources) {
            System.out.println("Caching jar resources");
            cachedJarResources = new JarResources(getJarFilePath());
            multiClassLoader.setCacheJarResources(cachedJarResources);
            // jarfile = null;
        }
        MidletUtils.getInstance().setMidletListener(midletListener);
        loadMIDletIcon(manifestAttributes);
        loadMainClass(mainclass);
        if (isJadCreateEnabled) {
            createJadFile(getJarFilePath());
        }
        profilerframe.listClasseFields(jarClasses);
        profilerframe.updateWatchClasses(jarClasses);

    }

    private void createJadFile(String jarfilepath) {
        try {
            FileOutputStream fout = new FileOutputStream(jarfilepath.substring(0, jarfilepath.length() - 3)
                    + "jad");
            byte[] nextlineBytes = "\n".getBytes();
            if ((jadProperties != null) && (jadProperties.size() > 0)) {
                Set keyset = jadProperties.keySet();
                Object objs[] = keyset.toArray();
                for (Object obj : objs) {
                    String key = obj.toString();
                    String str = key + ": " + jadProperties.getValue(key);
                    fout.write(str.getBytes());
                    fout.write(nextlineBytes);
                    key = str = null;
                }
            } else {
                // fout.write
                Enumeration elist = manifestProperties.propertyNames();
                Object item = null;
                while (elist.hasMoreElements()) {
                    item = elist.nextElement();
                    String jadPropertyItem = "";
                    if (!item.toString().equals("Manifest-Version")) {
                        jadPropertyItem = item.toString() + ": " + manifestProperties.getProperty(item.toString());
                        fout.write(jadPropertyItem.getBytes());
                        fout.write(nextlineBytes);
                    }
                }
                fout.write(("MIDlet-Jar-URL: " + getJarFileName()).getBytes());
                fout.write(nextlineBytes);
                int jarSize = (int) new File(jarfilepath).length();
                fout.write(("MIDlet-Jar-Size: " + jarSize).getBytes());
                fout.write(nextlineBytes);
                elist = null;
                item = null;
            }
            fout.close();
            fout = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultIcon() {
        try {
            midletIcon=ImageIO.read(this.getClass().getResourceAsStream("/icons/MobiIcon.png"));
            setIconImage(midletIcon);
            setMidletIcon(midletIcon);
        } catch (IOException ex) {
            Logger.getLogger(MobiEmulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadMainClass(String MainClass) {
        jarClasses = getClassesList();
        try {
            System.out.println("MainClass is " + MainClass);
            Class loadedClass = multiClassLoader.loadClass(MainClass);
            midlet = (MIDlet) loadedClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isForceRepaint) {
            canvasPanel.startThread();
        }
        setCurrentMIDlet(midlet);
        midlet.amsStartApp(midletListener);
    }

    public void loadClassesFromPath(String name) {
        try {
            File f = new File(name);
            buildClassesDir = f.getPath();
            File[] listFiles = null;
            if (f.isDirectory()) {
                listFiles = f.listFiles();
            }
            if (listFiles != null) {
                for (File listFile : listFiles) {
                    try {
                        getFiles(listFile, "");
                    } catch (IOException ex) {
                        Logger.getLogger(MobiEmulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (buildClasses == null) {
                buildClasses = new JarResources();
            }
            buildClasses.setJarCache(buildContents);
            if (urlClassLoader == null) {
                urlClassLoader = new MultiClassLoader();
                urlClassLoader.setCacheJarResources(buildClasses);
                updateScreenSize(getCanvasWidth(), getCanvasHeight());
                Class mainClass = urlClassLoader.loadClass("NumChallenge");
                MIDlet m = (MIDlet) mainClass.newInstance();
                setCurrentMIDlet(midlet);
                m.amsStartApp(midletListener);
            } else {
                urlClassLoader.setCacheJarResources(buildClasses);
            }
            urlClassLoader.setCacheJarResources(buildClasses);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MobiEmulator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MobiEmulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getFiles(File f, String previousPath) throws IOException {
        String path = previousPath;
        path += (!previousPath.equals("") ? "\\"
                : "");
        if (f.isDirectory()) {
            path += f.getName();
            File files[] = f.listFiles();
            for (File file : files) {
                getFiles(file, path);
            }
            files = null;
        } else {
            System.out.println("path is " + path + f.getName());
            InputStream is = new FileInputStream(f);
            byte data[] = new byte[is.available()];
            is.read(data);
            is.close();
            is = null;
            buildContents.put(path + f.getName(), data);
            data = null;
        }
    }

    private void updateMemoryView() {
        for (int i = 0; i < jarClasses.size(); i++) {
            Class cl = null;
            Object ob = null;
            try {
                cl = Class.forName(jarClasses.elementAt(i).toString(), false, multiClassLoader);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Field fields[] = null;
            try {
                fields = cl.getDeclaredFields();
            } catch (Exception ex) {
                Logger.getLogger(MobiEmulator.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Field field : fields) {
                field.setAccessible(true);
                if ((field != null) && (field.getType() == Image.class)) {
                    Object value = null;
                    try {
                        // Object c=Class.forName(classes.elementAt(i).toString(),false,multiClassLoader).newInstance();
                        value = field.get(null);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(MobiEmulator.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(MobiEmulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public Vector getClassesList() {
        Vector Classes = new Vector();
        Enumeration jarEntries = null;
        ZipEntry ze = null;
        jarEntries = jarfile.entries();
        while (jarEntries.hasMoreElements()) {
            ze = (ZipEntry) jarEntries.nextElement();
            if (ze.getName().endsWith(".class")) {
                String className = ze.getName().substring(0, ze.getName().length() - 6).replace('/', '.');
                System.out.println("class :: " + className);
                Classes.add(className);
                totalByteCodeSize += ze.getSize();
            }
        }
        jarEntries = null;
        ze = null;

        return Classes;
    }

    public MultiClassLoader getCustomCLassLoader() {
        return multiClassLoader;
    }

    private void loadMIDletIcon(Attributes manifestAttributes) {
        String imagepath = "";
        String midletattr = manifestAttributes.getValue("MIDlet-1");
        StringTokenizer st = new StringTokenizer(midletattr, ",");
        st.nextToken();
        imagepath = st.nextToken().trim();
        if (imagepath.equals("")) {
            System.out.println("MIDlet Icon not found" + imagepath);

            return;
        }
        try {
            System.out.println(">> MIDlet icon : " + imagepath);
            InputStream is=getResourceAsStream(imagepath);
            if (is == null) {
                System.out.println("MIDlet icon not found " + imagepath);
                return;
            }
            midletIcon=javax.microedition.lcdui.Image.createImage(is)._image;
            mobiEmulatorInstance.setIconImage(midletIcon);
            setMidletIcon(midletIcon);
        } catch (Exception ex) {
            System.out.println("MIDlet Icon not found " + imagepath);
        }
    }

    
    public static InputStream getResourceAsStream(String resourcename) throws IOException {
        if (resourcename.startsWith("/")) {
            resourcename = resourcename.substring(1);
        }
        if (cacheJarResources && (cachedJarResources.getResource(resourcename) != null)) {
            Profiler.getResourceStreamCallCount += 1;
            byte data[] = cachedJarResources.getResource(resourcename);
            Profiler.getResourceStreamBytes += data.length;
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            data = null;

            return bais;
        } else {
            if (!getJarFilePath().equals("")) {
                String packagePath = mobiEmulatorInstance.getClass().getName();
                Profiler.getResourceStreamCallCount += 1;
                try {
                    ZipEntry ze = jarfile.getEntry(resourcename);
                    if (ze == null) {
                        System.out.println("Resource loaded >>> " + resourcename + " null");

                        return new ByteArrayInputStream(null);
                    }
                    byte[] data = new byte[(int) ((ZipEntry) ze).getSize()];
                    DataInputStream dis = new DataInputStream(jarfile.getInputStream(ze));
                    dis.readFully(data);
                    System.out.println("Resource loaded >>> " + resourcename + " Size " + data.length + "bytes");
                    Profiler.getResourceStreamBytes += data.length;
                    ByteArrayInputStream bais = new ByteArrayInputStream(data);
                    if (dis != null) {
                        dis.close();
                    }
                    dis = null;
                    ze = null;
                    data = null;

                    return bais;
                } catch (Exception e) {
                    System.out.println("unable to load resource from jar file");
                }
            }
        }

        return null;
    }

    private String getMainClass(Properties ManifestProperties) {
        String ClassName = manifestAttributes.getValue("MIDlet-1");  
        StringTokenizer localStringTokenizer = new StringTokenizer(ClassName, ", ");
        while (localStringTokenizer.hasMoreTokens()) {
            ClassName = localStringTokenizer.nextToken();
        }
        System.out.println("Main ClassName is " + ClassName);
        localStringTokenizer = null;
        return ClassName;
    }

    private void loadManifest() {
        manifestProperties = new Properties();
        manifestAttributes = new Attributes();
        try {
            // Get the manifest
            Manifest manifest = jarfile.getManifest();
            // Get the main attributes in the manifest
            manifestAttributes = (Attributes) manifest.getMainAttributes();
            manifestProperties.load(jarInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAppProperty(String key) {
        return jadProperties.getValue(key);
    }

    private void loadJadAttributes(String Path) {
        String JadFilePath = Path;
        Manifest jadmanifest = null;
        try {
            jadmanifest = new Manifest(new FileInputStream(JadFilePath.substring(0, JadFilePath.length() - 3) + "jad"));
        } catch (Exception ex) {
            System.out.println(" " + JadFilePath.substring(0, JadFilePath.length() - 3) + "jad not found ");

            return;
        }
        jadProperties = jadmanifest.getMainAttributes();
        jadmanifest = null;
    }

    public int getCanvasWidth() {
        return canvasPanelWidth;
    }

    public int getCanvasHeight() {
        return canvasPanelHeight;
    }

    public void setCanvasWidth(int Width) {
        canvasPanelWidth = Width;
    }

    public void setCanvasHeight(int Height) {
        canvasPanelHeight = Height;
    }

    public void updateMainFrame(float zoomVal) {
        // if(canvasZoom>1)
        setWindowZoom(zoomVal);
        canvasPanel.setZoomVal(zoomVal);
        Insets insets = getInsets();
        setSize(new Dimension((int) (frameWidth * zoomVal) + insets.left + insets.right,
                (int) (frameHeight * zoomVal) + MenuBarHeight + ToolBarHeight + StatusPanelHeight
                        + insets.top + insets.bottom));
        canvasPanel.updateCanvasSize((int) ((frameWidth) * zoomVal), (int) ((frameHeight) * zoomVal));
        doLayout();
        invalidate();
        repaint();
    }

    /* Log Frame Method */
    public void updateLogFramePos() {
        logFrame.setLocation(new Point(MobiEmulator.mobiEmulatorInstance.getX() + MobiEmulator.mobiEmulatorInstance.getWidth(),
                MobiEmulator.mobiEmulatorInstance.getY()));
    }

    public void setWindowZoom(float canvasZoom) {
        windowZoom = canvasZoom;
    }

    public float getZoomVal() {
        return windowZoom;
    }

    public void zoomout() {
        float zoomval = getZoomVal();
        zoomval -= 0.5;
        if (zoomval <= 0.5) {
            zoomval = 0.5f;
        }
        statusPanel.setCenterText("ZOOM " + (zoomval + " x"));
        updateMainFrame(zoomval);
    }

    public void zoomin() {
        float zoomval = getZoomVal();
        zoomval += 0.5;
        if (zoomval >= 3) {
            zoomval = 3.0f;
        }
        statusPanel.setCenterText("ZOOM " + (zoomval + " x"));
        updateMainFrame(zoomval);
    }
    
    private void setMidletIcon(BufferedImage icon){
        options.setIconImage(icon);
        mvf.setIconImage(icon);
        methodFrame.setIconImage(icon);
        paintStackFrame.setIconImage(icon);
        logFrame.setIconImage(icon);
        profilerframe.setIconImage(icon);
        canvasPanel.xRayFrame.setIconImage(icon);
        keyPadFrame.setIconImage(icon);
    }
    
    public void updateDebugSettings() {
        if (multiClassLoader != null) {
            multiClassLoader.monitorOn = options.isDebugOptionEnabled(Options.DebugClassLoading);
        }
        if ((canvasPanel != null) && (options != null)) {
            cacheJarResources = options.isDebugOptionEnabled(Options.CacheJarResources);
            canvasPanel.isKeyRepeatedSupported = options.isDebugOptionEnabled(Options.KeyRepeatedEvents);
            canvasPanel.isPointerEventsSupported = options.isDebugOptionEnabled(Options.PointerEvents);
        }
        if (options != null) {
            isAlwaysShowLog = options.isDebugOptionEnabled(Options.IsAlwaysLogVisible);
            isPrintOff = options.isDebugOptionEnabled(Options.IsPrintOff);
        }
        if (options != null) {
            monitorMethodCalls = options.isDebugOptionEnabled(Options.MonitorMethods);
//            methodByteCode        = options.isDebugOptionEnabled(Options.MonitorMethodByteCode);
            monitorNewTraces = options.isDebugOptionEnabled(Options.TrackNewCalls);
            isMemoryViewEnabled = options.isDebugOptionEnabled(Options.IsMemoryViewEnabled);
            isProfilerViewEnabled = options.isDebugOptionEnabled(Options.IsProfilerEnabled);
        }
    }

    private void loadPlayerMode() {
        isAlwaysShowLog = false;
        isPrintOff = true;
        if (multiClassLoader != null) {
            multiClassLoader.monitorOn = false;
        }
        monitorMethodCalls = false;
        monitorNewTraces = false;
        methodByteCode = false;
        isMethodViewEnabled = false;
        isMemoryViewEnabled = false;
        isProfilerViewEnabled = false;
        isLoggerEnabled = false;
        emulMemoryview.setEnabled(isMemoryViewEnabled);
        methodView.setEnabled(isMethodViewEnabled);
        profilerMenuItem.setEnabled(isProfilerViewEnabled);
        if (logFrame.isVisible()) {
            logFrame.setVisible(false);
        }
        if (mvf.isVisible()) {
            mvf.setVisible(false);
        }
        if (methodView.isVisible()) {
            methodView.setVisible(false);
        }
        if (profilerframe.isVisible()) {
            profilerframe.setVisible(false);
        }
        if (paintStackFrame.isVisible()) {
            paintStackFrame.setVisible(false);
        }
        EmulLog.setEnabled(isLoggerEnabled);
    }

    private void loadDeveloperMode() {
        updateDebugSettings();
        isMethodViewEnabled = true;
        isMemoryViewEnabled = true;
        isProfilerViewEnabled = true;
        isLoggerEnabled = true;
        emulMemoryview.setEnabled(isMemoryViewEnabled);
        methodView.setEnabled(isMethodViewEnabled);
        profilerMenuItem.setEnabled(isProfilerViewEnabled);
        EmulLog.setEnabled(isLoggerEnabled);
    }

    private void rotateCanvas(int angleMultiplier) {
        Insets insets;
        if (angleMultiplier % 2 != 0) {
            insets = getInsets();
            frameWidth = canvasPanel.getCanvasHeight();
            frameHeight = canvasPanel.getCanvasWidth();
        } else {
            insets = getInsets();
            frameWidth = canvasPanel.getCanvasWidth();
            frameHeight = canvasPanel.getCanvasHeight();
        }
        setSize(new Dimension((int) (frameWidth) + insets.left + insets.right,
                (int) (frameHeight) + MenuBarHeight + ToolBarHeight + StatusPanelHeight + insets.top
                        + insets.bottom));
        canvasPanel.setPreferredSize(new Dimension(frameWidth, frameHeight));
        doLayout();
        invalidate();
        repaint();
        frameWidth = getCanvasHeight();
        frameHeight = getCanvasWidth();
        if (mobiEmulatorInstance.canvasPanel.getCurrentDisplayable() != null) {
            mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeScreenChanged(frameWidth, frameHeight);
        }
    }

    public void setCurrentMIDlet(MIDlet midlet) {
        this.midlet = midlet;
    }

    public MIDlet getCurrentMIDlet() {
        return this.midlet;
    }

    public void setInputWidthHeight(String w, String h) {
        if (emulatorHeightInput == null || emulatorWidthInput == null)
            return;
        emulatorHeightInput.setText(h);
        emulatorWidthInput.setText(w);

    }

    private JMenuItem createMenuItem(JMenu parent, String menu, KeyStroke keyStroke) {
        JMenuItem item = new JMenuItem();
        if (menu != null) {
            item.setText(menu);
            item.setActionCommand(menu);
        }
        item.setAccelerator(keyStroke);
        item.addActionListener(menuListener);
        parent.add(item);
        return item;
    }

    private ActionListener menuListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            if (action.equals(Constants.newWindowText)) {
                createNewInstance();
            }
            if (action.equals(Constants.openJarText)) {
                openJar(e);
            }
            if (action.equals(Constants.saveText)) {
                if (cacheJarResources && (cachedJarResources != null)) {
                    JFileChooser fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnVal = fc.showSaveDialog(mobiEmulatorInstance);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        saveCachedJar(fc.getSelectedFile().getAbsolutePath(), cachedJarResources);
                    }
                } else {
                    JOptionPane.showInternalMessageDialog(MobiEmulator.mobiEmulatorInstance.getContentPane(),
                            "Jar is not Cached or \n CacheJarResoruces is not Enabled");
                }
            }
            if (action.equals(Constants.suspendText)) {
                canvasPanel.canvasPause();
                canvasPanel.requestFocus();
            }
            if (action.equals(Constants.resumeText)) {
                canvasPanel.canvasResume();
                canvasPanel.requestFocus();

            }
            if (action.equals(Constants.restartText)) {
                restartJar(getJarFilePath());
            }
            if (action.equals(Constants.exitText)) {
                unLoadCurrentJar();
                System.exit(1);
            }


            if (action.equals(Constants.captureText)) {
                if (!TextField.isFocused) {
                    return;
                }
                canvasPanel.takeScreenShot(0, 0, frameWidth, frameHeight, false);
            }
            if (action.equals(Constants.captureScreenAreaText)) {
                int x = canvasPanel.infoSelectedRect.x;
                int y = canvasPanel.infoSelectedRect.y;
                int w = canvasPanel.infoSelectedRect.width;
                int h = canvasPanel.infoSelectedRect.height;
                canvasPanel.takeScreenShot(x, y, w, h, false);
            }
            if (action.equals(Constants.captureToClipboardText)) {
                canvasPanel.takeScreenShot(0, 0, frameWidth, frameHeight, true);
            }
            if (action.equals(Constants.captureGifStartText)) {
                canvasPanel.startGIFCapture();
                canvasPanel.captureGIF = true;
            }
            if (action.equals(Constants.captureGifStopText)) {
                canvasPanel.stopGIFCapture();
            }
            if (action.equals(Constants.captureAVIStartText)) {
                canvasPanel.startCaptureAVI();
            }
            if (action.equals(Constants.captureAVIStopText)) {
                canvasPanel.stopCaptureAVI();
            }
            if (action.equals(Constants.zoomInText)) {
                if (TextField.isFocused) {
                    return;
                }
                zoomin();
            }
            if (action.equals(Constants.zoomOutText)) {
                if (TextField.isFocused) {
                    return;
                }
                zoomout();
            }
            if (action.equals(Constants.settingsText)) {
                options.setLocation(MobiEmulator.mobiEmulatorInstance.getX() + MobiEmulator.mobiEmulatorInstance.getWidth(),
                        MobiEmulator.mobiEmulatorInstance.getY());
                options.setVisible(!options.isVisible());
            }

            if (action.equals(Constants.alwaysTopText)) {
                if (TextField.isFocused) {
                    return;
                }
                isAlwaysOnTop = !isAlwaysOnTop;
                setAlwaysOnTop(isAlwaysOnTop);
            }
            if (action.equals(Constants.beanShellText)) {
                if (TextField.isFocused) {
                    return;
                }
                bsh.setVisible(!bsh.isVisible());
                canvasPanel.requestFocus();
            }
            if (action.equals(Constants.logText)) {
                if (TextField.isFocused) {
                    return;
                }
                mobiEmulatorInstance.updateLogFramePos();
                logFrame.setVisible(!logFrame.isVisible());
                canvasPanel.requestFocus();
            }
            if (action.equals(Constants.infoText)) {
                if (TextField.isFocused) {
                    return;
                }
                canvasPanel.isInfoVisible = !mobiEmulatorInstance.canvasPanel.isInfoVisible;
                canvasPanel.updateInfoFramePos(MobiEmulator.mobiEmulatorInstance.getX(), MobiEmulator.mobiEmulatorInstance.getY());
                canvasPanel.infoFrame.setVisible(mobiEmulatorInstance.canvasPanel.isInfoVisible);
                canvasPanel.requestFocus();
            }
            if (action.equals(Constants.methodsText)) {
                // updateMemoryView();
                // mvf.updateMemoryView();
                if (TextField.isFocused) {
                    return;
                }
                methodFrame.setVisible(!methodFrame.isVisible());
                // updateMemoryView();
                // memoryFrame.setVisible(!memoryFrame.isVisible());
            }
            if (action.equals(Constants.memoryViewText)) {
                // updateMemoryView();
                // mvf.updateMemoryView();
                if (TextField.isFocused) {
                    return;
                }
                mvf.setVisible(!mvf.isVisible());
                // updateMemoryView();
                // memoryFrame.setVisible(!memoryFrame.isVisible());
            }
            if (action.equals(Constants.keypadText)) {
                if (TextField.isFocused) {
                    return;
                }
                showkeypad = !showkeypad;
                keyPadFrame.setVisible(showkeypad);
                canvasPanel.requestFocus();
            }
            if (action.equals(Constants.canvasOrientationText)) {
                if (TextField.isFocused) {
                    return;
                }
                updateScreenSize(getCanvasHeight(), getCanvasWidth());
                frameWidth = getCanvasHeight();
                frameHeight = getCanvasWidth();
                mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeScreenChanged(frameWidth, frameHeight);
            }
            if (action.equals(Constants.canvasRotationText)) {
                if (TextField.isFocused) {
                    return;
                }
                mobiEmulatorInstance.canvasPanel.canvasRotation = true;
                int curValue = mobiEmulatorInstance.canvasPanel.canvasRotationAngleMultiplier;
                curValue = ((ccwCanvasRotation.getState() ? --curValue
                        : ++curValue) > 3 ? 0
                        : (curValue < 0) ? 3
                        : curValue);
                mobiEmulatorInstance.canvasPanel.canvasRotationAngleMultiplier = curValue;
                rotateCanvas(mobiEmulatorInstance.canvasPanel.canvasRotationAngleMultiplier);
            }
            if (action.equals(Constants.profilerText)) {
                if (TextField.isFocused) {
                    return;
                }
                profilerframe.setVisible(!profilerframe.isVisible());
            }
            if (action.equals(Constants.paintInvestigatorText)) {
                if (!canvasPanel.isCanvasPaused()) {
                    paintStackFrame.startRecording();
                } else {
                    paintStackFrame.setVisible(true);
                }
            }
            if (action.equals(Constants.bicubicQualityText)) {
                canvasPanel.Quality = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
            }
            if (action.equals(Constants.nearestNeighbourText)) {
                canvasPanel.Quality = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            }
            if (action.equals(Constants.bilinearText)) {
                canvasPanel.Quality = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            }
            if (action.equals(Constants.unloadJarText)) {
                if (TextField.isFocused) {
                    return;
                }
                unLoadCurrentJar();
            }
            if (action.equals(Constants.callsmsText)) {
                csManager.setVisible(!csManager.isVisible());
            }
            if (action.equals(Constants.xrayViewText)) {
                if (TextField.isFocused) {
                    return;
                }
                if (canvasPanel.xRayFrame != null) {
                    canvasPanel.xRayFrame.setVisible(!canvasPanel.xRayFrame.isVisible());
                }
                canvasPanel.requestFocus();

            }

            if (action.equals(Constants.aboutText)) {
                aboutBox.update();
                aboutBox.setAboutVisible(!aboutBox.isAboutVisible());
            }
        }
    };

    private ActionListener toolbarListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            if (btn.equals(settingButton)) {
                if (options == null)
                    return;
                options.setLocation(MobiEmulator.mobiEmulatorInstance.getX() + MobiEmulator.mobiEmulatorInstance.getWidth(),
                        MobiEmulator.mobiEmulatorInstance.getY());
                options.setVisible(!options.isVisible());
            }
            if (btn.equals(memoryViewButton)) {
                if (mvf.isEnabled())
                    mvf.setVisible(!mvf.isVisible());
                canvasPanel.requestFocus();
            }
            if (btn.equals(newWindowButton)) {
                createNewInstance();
                canvasPanel.requestFocus();
            }
            if (btn.equals(fpsPopButton)) {
                if (fpsPopupMenu == null)
                    return;
                fpsPopupMenu.show(mobiEmulatorInstance, mobiEmulatorInstance.getInsets().left + canvasPanel.getX(), mobiEmulatorInstance.getInsets().top + (MenuBarHeight) + emulToolBar.getPreferredSize().height);
                canvasPanel.requestFocus();
            }
            if (btn.equals(paintrecorderButton)) {
                if (paintStackFrame == null)
                    return;
                if (!canvasPanel.isCanvasPaused()) {
                    paintStackFrame.startRecording();
                } else {
                    paintStackFrame.setVisible(true);
                }
                canvasPanel.requestFocus();
            }
            if (btn.equals(dimensionButton)) {
                if (dimensionPopup == null)
                    return;
                dimensionPopup.show(mobiEmulatorInstance, mobiEmulatorInstance.getInsets().left + canvasPanel.getX(), mobiEmulatorInstance.getInsets().top + (MenuBarHeight) + emulToolBar.getPreferredSize().height);
                canvasPanel.requestFocus();
            }
        }
    };


    private MidletListener midletListener = new MidletListener() {
        @Override
        public int getCanvasWidth() {
            return canvasPanelWidth;
        }

        @Override
        public int getCanvasHeight() {
            return canvasPanelHeight;
        }

        @Override
        public BufferedImage getXrayScreen() {
            return canvasPanel.getXrayScreen();
        }

        @Override
        public void setCanvasFullScreen(boolean b) {
            mobiEmulatorInstance.canvasPanel.setFullScreen(b);
        }

        @Override
        public boolean isCanvasFullScreen() {
            return mobiEmulatorInstance.canvasPanel.isFullScreen();
        }

        @Override
        public boolean isPerformanceEnabled() {
            return mobiEmulatorInstance.performanceInvestigate;
        }

        @Override
        public int getPaintRecordCount() {
            return MobiEmulator.mobiEmulatorInstance.paintRecordCount;
        }

        @Override
        public int getPaintStackRecordCount() {
            return mobiEmulatorInstance.paintStackFrame.getRecordFrameCount();
        }


        @Override
        public void paintStackStopRecording() {
            mobiEmulatorInstance.paintStackFrame.endRecording();

        }

        @Override
        public void paintStackEndFrame() {
            mobiEmulatorInstance.paintStackFrame.endFrame();
        }

        @Override
        public void paintStackStartFrame() {
            mobiEmulatorInstance.paintStackFrame.startFrame();
        }

        @Override
        public void paintStackStartRecording() {
            mobiEmulatorInstance.paintStackFrame.startRecording();
        }

        @Override
        public BufferedImage getCanvasScreen() {
            return mobiEmulatorInstance.canvasPanel.midp_screen_image;
        }

        @Override
        public void canvasRepaint() {
            mobiEmulatorInstance.canvasPanel.repaint();
        }

        @Override
        public void incrPaintRecordCount() {
            MobiEmulator.mobiEmulatorInstance.paintRecordCount++;
        }

        @Override
        public boolean isPaintRecording() {
            return mobiEmulatorInstance.paintStackFrame.isRecording();
        }

        @Override
        public boolean isProfilerEnabled() {
            return mobiEmulatorInstance.isProfilerViewEnabled;
        }

        @Override
        public void incrRgbCount() {
            if (mobiEmulatorInstance.isProfilerViewEnabled) {
                Profiler.getRGBCount++;
            }
        }

        @Override
        public void incrImageCount() {
            if (mobiEmulatorInstance.isProfilerViewEnabled) {
                Profiler.createImageCount += 1;
            }
        }

        @Override
        public InputStream getResourceStream(String name) throws IOException {
            InputStream is = null;
            is = getResourceAsStream(name);
            return is;
        }

        @Override
        public void performancePaintArea(Graphics g, int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor, String type, String log) {
            if (mobiEmulatorInstance.performanceInvestigate) {
                mobiEmulatorInstance.paintStackFrame.getTasks().add(new ImageTask(g).paintArea(x_src, y_src, width, height,
                        x_dest, y_dest, anchor).setType(PaintTask.COPY_AREA, log));
            }
        }

        @Override
        public void performancePaintRGB(Graphics g, int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha, String type, String log) {

            if (mobiEmulatorInstance.performanceInvestigate) {
                if (type.equals("drawrgb"))
                    mobiEmulatorInstance.paintStackFrame.getTasks().add(new ImageTask(g).paintRGB(rgbData, offset, scanlength,
                            x, y, width, height, processAlpha).setType(PaintTask.DRAW_RGB, log));
            }
        }

        @Override
        public void performancePaintImage(Graphics g, Image img, int x, int y, int anchor, String type, String log) {
            if (mobiEmulatorInstance.performanceInvestigate) {
                mobiEmulatorInstance.paintStackFrame.getTasks().add(new ImageTask(g).paintImage(img, x, y,
                        anchor).setType(PaintTask.DRAW_IMAGE, log));
            }
        }

        @Override
        public void performancePaintRect(Graphics g, int x, int y, int width, int height, String type, String log) {
            if (mobiEmulatorInstance.performanceInvestigate) {
                if (type.equals("drawrect"))
                    mobiEmulatorInstance.paintStackFrame.getTasks().add(new PrimitiveTask(g).paintRect(x, y, width,
                            height).setType(PaintTask.DRAW_RECT, log));
                if (type.equals("fillrect"))
                    mobiEmulatorInstance.paintStackFrame.getTasks().add(new PrimitiveTask(g).paintRect(x, y, width,
                            height).setType(PaintTask.FILL_RECT, log));
            }
        }

        @Override
        public void performancePaintLine(Graphics g, int x1, int y1, int x2, int y2, String type, String log) {
            if (mobiEmulatorInstance.performanceInvestigate) {
                mobiEmulatorInstance.paintStackFrame.getTasks().add(new PrimitiveTask(g).paintLine(x1, y1, x2,
                        y2).setType(PaintTask.DRAW_LINE, log));
            }

        }

        @Override
        public void performancePaintArc(Graphics g, int x, int y, int w, int h, int startAngle, int endAngle, String type, String log) {
            if (mobiEmulatorInstance.performanceInvestigate) {
                if (type.equals("fillarc"))
                    mobiEmulatorInstance.paintStackFrame.getTasks().add(new PrimitiveTask(g).paintArc(x, y, w, h, startAngle,
                            endAngle).setType(PaintTask.FILL_ARC, log));
                if (type.equals("drawarc"))
                    mobiEmulatorInstance.paintStackFrame.getTasks().add(new PrimitiveTask(g).paintArc(x, y, w, h, startAngle,
                            endAngle).setType(PaintTask.DRAW_ARC, log));
            }
        }

        @Override
        public void performanceRoundRect(Graphics g, int x, int y, int w, int h, int r1, int r2, String type, String log) {
            if (mobiEmulatorInstance.performanceInvestigate) {
                if (type.equals("drawroundrect"))
                    mobiEmulatorInstance.paintStackFrame.getTasks().add(new PrimitiveTask(g).paintRoundRect(x, y, w, h, r1,
                            r2).setType(PaintTask.DRAW_ROUNDRECT, log));
                if (type.equals("fillroundrect"))
                    mobiEmulatorInstance.paintStackFrame.getTasks().add(new PrimitiveTask(g).paintRoundRect(x, y, w, h, r1,
                            r2).setType(PaintTask.FILL_ROUNDRECT, log));
            }
        }

        @Override
        public void performancePaintTriangle(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3, String type, String log) {
            if (mobiEmulatorInstance.performanceInvestigate) {
                if (type.equals("drawtriangle"))
                    mobiEmulatorInstance.paintStackFrame.getTasks().add(new PrimitiveTask(g).paintTriangle(x1, y1, x2, y2, x3,
                            y3).setType(PaintTask.DRAW_TRIANGLE, log));
                if (type.equals("filltriangle"))
                    mobiEmulatorInstance.paintStackFrame.getTasks().add(new PrimitiveTask(g).paintTriangle(x1, y1, x2, y2, x3,
                            y3).setType(PaintTask.FILL_TRIANGLE, log));
            }
        }

        @Override
        public void performancePaintRegion(Graphics g, Image src, int x_src, int y_src, int width, int height, int transform, int x_dest, int y_dest, int anchor, String type, String log) {
            if (mobiEmulatorInstance.performanceInvestigate) {
                mobiEmulatorInstance.paintStackFrame.getTasks().add(new ImageTask(g).paintRegion(src, x_src, y_src, width,
                        height, transform, x_dest, y_dest, anchor).setType(PaintTask.DRAW_REGION, log));
            }
        }

        public void performancePaintString(Graphics g, Font f, String str, int x, int y, int anchor, String type, String log) {
            if (mobiEmulatorInstance.performanceInvestigate) {
                mobiEmulatorInstance.paintStackFrame.getTasks().add(new PrimitiveTask(g).paintString(f, str, x, y,
                        anchor).setType(PaintTask.DRAW_STRING, log));
            }
        }

        @Override
        public void incrDrawRGBCount(int pixels) {
            if (mobiEmulatorInstance.isProfilerViewEnabled) {
                Profiler.drawRGBCount += 1;
                Profiler.drawRGBDrawnPixels += (pixels);
            }
        }

        @Override
        public void unLoadCurrentJar() {
            mobiEmulatorInstance.unLoadCurrentJar();
        }

        @Override
        public MIDlet getCurrentMIDlet() {
            return mobiEmulatorInstance.getCurrentMIDlet();
        }

        @Override
        public String getAppProperty(String key) {
            return mobiEmulatorInstance.getAppProperty(key);
        }

        @Override
        public KeyActionMapping[] getOptionKeys() {
            return mobiEmulatorInstance.options.getKeys();
        }

        //status updates

        @Override
        public void updateStatusLeft(String text) {
            mobiEmulatorInstance.statusPanel.setLeftText(text);
            mobiEmulatorInstance.statusPanel.invalidate();
        }

        @Override
        public void updateStatusRight(String text) {
            mobiEmulatorInstance.statusPanel.setRightText(text);
            mobiEmulatorInstance.statusPanel.invalidate();
        }

        @Override
        public void setPos(int x, int y) {
            mobiEmulatorInstance.setLocation(x - 5, y - 5);
            mobiEmulatorInstance.invalidate();
        }

        @Override
        public int[] getPos() {
            return new int[]{mobiEmulatorInstance.getX(), mobiEmulatorInstance.getY()};
        }

        @Override
        public Runnable getRunnable() {
            return mobiEmulatorInstance.canvasPanel.runnable;
        }

        @Override
        public void setRunnable(Runnable r) {
            mobiEmulatorInstance.canvasPanel.runnable = r;
            mobiEmulatorInstance.canvasPanel.repaint();
        }

        @Override
        public void setCurrentDisplay(Displayable nextDisplayable, String type) {
            if (type.equals("canvas"))
                mobiEmulatorInstance.canvasPanel.setDisplayType(MainCanvasPanel.CANVAS);
            if (type.equals("screen"))
                mobiEmulatorInstance.canvasPanel.setDisplayType(MainCanvasPanel.SCREEN);
            mobiEmulatorInstance.canvasPanel.setCurrent(nextDisplayable);
        }

        @Override
        public Displayable getCurrentDisplay() {
            return mobiEmulatorInstance.canvasPanel.getCurrentDisplayable();
        }

        @Override
        public void canvasShowNotify() {
            mobiEmulatorInstance.canvasPanel.midp_canvas.showNotify();
        }

        @Override
        public void canvasHideNotify() {
            mobiEmulatorInstance.canvasPanel.midp_canvas.hideNotify();
        }

        @Override
        public Runnable getCanvas() {
            return mobiEmulatorInstance.canvasPanel;
        }

        @Override
        public void sendMessage(Message msg, String addr) throws IOException {
            mobiEmulatorInstance.csManager.send(msg, addr);
        }

        @Override
        public Message receiveMessage(String address) throws IOException {
            return mobiEmulatorInstance.csManager.receive(address);
        }

        @Override
        public String getMidletClassName() {
            return mobiEmulatorInstance.mainclass;
        }
    };
}
