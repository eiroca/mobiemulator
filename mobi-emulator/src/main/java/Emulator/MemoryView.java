package Emulator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.microedition.lcdui.Image;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
// ~--- non-JDK imports --------------------------------------------------------
import Emulator.ClassProfiler.Instance;

class ImageSorter implements Comparator {

  boolean ascending;

  ImageSorter(final boolean isAscending) {
    ascending = isAscending;
  }

  @Override
  public int compare(final Object obj1, final Object obj2) {
    final ClassProfiler.Instance instance1 = (ClassProfiler.Instance)(ascending ? obj1 : obj2);
    final ClassProfiler.Instance instance2 = (ClassProfiler.Instance)(ascending ? obj2 : obj1);
    return instance1.totalSize - instance2.totalSize;
  }
}

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class MemoryView extends JFrame implements Runnable, MouseListener {

  /**
   *
   */
  private static final long serialVersionUID = 8162144931781798233L;
  private final int maxzoom = 8;
  private final long refreshTime = 1000;
  private long currentTime = System.currentTimeMillis();
  private JCheckBox autoUpdate;
  private JButton btnResetimagecount;
  private JButton btnUpdateimages;
  private JLabel byteCodeSize;
  private JLabel classSize;
  private JLabel classSizeVal;
  private ClassProfiler clsProfiler;
  private JPanel contentPane;
  private Box imageBox;
  private JPanel imageInfoPanel;
  private ImagesPanel imagePanel;
  private JLabel imageResol;
  private JLabel imageResolVal;
  private JLabel imageDrawnCount;
  private JScrollPane imageScrollPane;
  private JLabel imageSize;
  private JLabel imageSizeVal;
  private Box imageValBox;
  private JLabel imagescount;
  private JLabel imagescountVal;
  private JCheckBox imgsUnused;
  private JCheckBox imgsUsed;
  private final MemoryView instance;
  private boolean isAscending;
  private JLabel lblZoom;
  private JPanel memoryInfoPanel;
  private Box memoryLabelBox;
  private Box memoryValueBox;
  private JLabel objectsSizeVal;
  private boolean previousSort;
  private JLabel sort;
  private JComboBox sortBox;
  private JLabel totalByteCodeLaoded;
  private JLabel totalMemoryUsed;
  private JLabel totalMemoryVal;
  private JLabel totalObjectsSize;
  private JSlider zoomSlider;
  private static String EmptyString = "";

  public MemoryView() {
    instance = this;
    init();
    new Thread(this).start();
  }

  /**
   * Create the frame.
   */
  public void init() {
    setTitle("MemoryView");
    setBounds(100, 100, 550, 620);
    clsProfiler = new ClassProfiler();
    clsProfiler.parseJar();
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(new BorderLayout(0, 0));
    imageScrollPane = new JScrollPane();
    imagePanel = new ImagesPanel(instance);
    imageScrollPane.setViewportView(imagePanel);
    contentPane.add(imageScrollPane, BorderLayout.CENTER);
    final JPanel settingsPanel = new JPanel();
    settingsPanel.setPreferredSize(new Dimension(getWidth(), 80));
    settingsPanel.setToolTipText("Settings");
    contentPane.add(settingsPanel, BorderLayout.NORTH);
    settingsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    autoUpdate = new JCheckBox("AutoUpdate");
    autoUpdate.addItemListener(arg0 -> {
    });
    autoUpdate.setToolTipText("AutoUpdate");
    settingsPanel.add(autoUpdate);
    btnUpdateimages = new JButton("UpdateImages");
    btnUpdateimages.addActionListener(e -> update());
    settingsPanel.add(btnUpdateimages);
    btnResetimagecount = new JButton("ResetImagesCount");
    btnResetimagecount.addActionListener(e -> imagePanel.resetImageCount());
    settingsPanel.add(btnResetimagecount);
    imgsUnused = new JCheckBox("ImagesUnused");
    imgsUnused.setSelected(true);
    imgsUnused.addItemListener(arg0 -> {
    });
    settingsPanel.add(imgsUnused);
    imgsUsed = new JCheckBox("ImagesUsed");
    imgsUsed.setSelected(true);
    imgsUsed.addItemListener(arg0 -> {
    });
    settingsPanel.add(imgsUsed);
    zoomSlider = new JSlider(1, 8, 1);
    final Hashtable labelTable = new Hashtable();
    for (int i = 1; i <= maxzoom; i++) {
      labelTable.put(i, new JLabel("" + (50 * i)));
    }
    zoomSlider.setLabelTable(labelTable);
    zoomSlider.addChangeListener(ce -> {
      int zoomVal = 50;
      if (!zoomSlider.getValueIsAdjusting()) {
        zoomVal = zoomSlider.getValue();
      }
      imagePanel.setZoom((zoomVal));
    });
    sort = new JLabel("SortBy");
    settingsPanel.add(sort);
    sortBox = new JComboBox();
    sortBox.addActionListener(e -> {
      final int i = sortBox.getSelectedIndex();
      isAscending = i != 0;
    });
    sortBox.setModel(new DefaultComboBoxModel(new String[] {
        "LargeSize", "SmallSize"
    }));
    settingsPanel.add(sortBox);
    lblZoom = new JLabel("Zoom");
    settingsPanel.add(lblZoom);
    zoomSlider.setValue(2);
    zoomSlider.setMinimum(1);
    zoomSlider.setMaximum(8);
    zoomSlider.setMajorTickSpacing(1);
    zoomSlider.setMinorTickSpacing(1);
    zoomSlider.setToolTipText("ImageZoom");
    zoomSlider.setPaintLabels(true);
    zoomSlider.setPaintTicks(true);
    settingsPanel.add(zoomSlider);
    final JPanel infoPanel = new JPanel();
    contentPane.add(infoPanel, BorderLayout.SOUTH);
    infoPanel.setLayout(new BorderLayout());
    imageInfoPanel = new JPanel();
    imageInfoPanel.setBorder(new TitledBorder("ImageInfo"));
    imageBox = Box.createVerticalBox();
    imageValBox = Box.createVerticalBox();
    imagescount = new JLabel("ImagesCount");
    imagescountVal = new JLabel("" + imagePanel.imagesCount);
    imageSize = new JLabel("ImageSize");
    imageSizeVal = new JLabel();
    imageResol = new JLabel("ImageResol");
    imageResolVal = new JLabel();
    imageDrawnCount = new JLabel();

    imageBox.add(imagescount);
    imageBox.add(imageSize);
    imageBox.add(imageResol);
    imageBox.add(imageDrawnCount);
    imageValBox.add(imagescountVal);
    imageValBox.add(imageSizeVal);
    imageValBox.add(imageResolVal);
    imageInfoPanel.add(imageBox, BorderLayout.WEST);
    imageInfoPanel.add(imageValBox, BorderLayout.EAST);
    infoPanel.add(imageInfoPanel, BorderLayout.WEST);
    memoryInfoPanel = new JPanel();
    memoryInfoPanel.setBorder(new TitledBorder("MemoryInfo"));
    memoryInfoPanel.setPreferredSize(new Dimension(200, 100));
    memoryLabelBox = Box.createVerticalBox();
    memoryValueBox = Box.createVerticalBox();
    classSize = new JLabel("No Of Classes");
    memoryLabelBox.add(classSize);
    classSizeVal = new JLabel("");
    memoryValueBox.add(classSizeVal);
    totalObjectsSize = new JLabel("TotalObjectsSize");
    memoryLabelBox.add(totalObjectsSize);
    objectsSizeVal = new JLabel("" + 0);
    memoryValueBox.add(objectsSizeVal);
    totalByteCodeLaoded = new JLabel("TotalByteCodeLoaded");
    memoryLabelBox.add(totalByteCodeLaoded);
    byteCodeSize = new JLabel("" + 0);
    memoryValueBox.add(byteCodeSize);
    totalMemoryUsed = new JLabel("TotalMemoryUsed");
    memoryLabelBox.add(totalMemoryUsed);
    totalMemoryVal = new JLabel("" + 0);
    memoryValueBox.add(totalMemoryVal);
    memoryInfoPanel.add(memoryLabelBox, BorderLayout.WEST);
    memoryInfoPanel.add(memoryValueBox, BorderLayout.EAST);
    infoPanel.add(memoryInfoPanel, BorderLayout.EAST);
    imagePanel.repaint();
    imagePanel.addMouseListener(this);
  }

  @Override
  public void run() {
    while (true) {
      if (autoUpdate.isSelected() && isVisible()) {
        update();
      }
      try {
        Thread.sleep(refreshTime);
      }
      catch (final Exception e) {
      }
    }
  }

  public void update() {
    //      if(System.currentTimeMillis()-currentTime<refreshTime)
    //      {
    //          return;
    //      }
    clsProfiler.parseJar();
    imagePanel.updateSize();
    imagescountVal.setText(String.valueOf(imagePanel.imagesCount));
    if ((imagePanel.selectedImage != null)) {
      imageSizeVal.setText(String.valueOf(imagePanel.selectedImage.totalSize) + " bytes");
      imageResolVal.setText(String.valueOf(((Image)imagePanel.selectedImage.obj)._image.getWidth() + "x"
          + ((Image)imagePanel.selectedImage.obj)._image.getHeight()));
      imageDrawnCount.setText("Draw Count: " + String.valueOf(((Image)imagePanel.selectedImage.obj).drawCount));
    }
    classSizeVal.setText(String.valueOf(MobiEmulator.mobiEmulatorInstance.jarClasses.size()));
    byteCodeSize.setText(String.valueOf(MobiEmulator.totalByteCodeSize));
    objectsSizeVal.setText(String.valueOf(ClassProfiler.totalObjectsSize));
    totalMemoryVal.setText(String.valueOf(MobiEmulator.totalByteCodeSize + ClassProfiler.totalObjectsSize));
    currentTime = System.currentTimeMillis();
  }

  public void deInitialize() {
    imagePanel.deInitialize();
    clsProfiler.deInitialize();
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    if (imagePanel != null) {
      imagePanel.findImage(e);
    }
    if (e.getButton() == MouseEvent.BUTTON3) {
      if (!e.isPopupTrigger()) {
        imagePanel.menu.show(imagePanel, e.getX(), e.getY());
      }
    }
    imagePanel.setToolTipText(imagePanel.tooltipTexts[imagePanel.selectedInstance]);
  }

  @Override
  public void mousePressed(final MouseEvent e) {
  }

  @Override
  public void mouseReleased(final MouseEvent e) {
  }

  @Override
  public void mouseEntered(final MouseEvent e) {
    //      if(imagePanel!=null)
    //      {
    //          imagePanel.findImage(e);
    //      }
  }

  @Override
  public void mouseExited(final MouseEvent e) {
  }

  private class ImagesPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 6742627618443640573L;
    public int imagesCount = 0;
    private final String memoryImagesDir = "./memoryViewImages/";
    JPopupMenu menu = new JPopupMenu("Save");
    private Vector<Rectangle> imagePositions = new Vector();
    private final MemoryView memoryViewInstance;
    private int pointedX;
    private int pointedY;
    private final JMenuItem saveImageOption;
    public Instance selectedImage;
    int selectedInstance;
    private String[] tooltipTexts;
    int totalHeight;
    int zoom;

    private ImagesPanel(final MemoryView instance) {
      memoryViewInstance = instance;
      totalHeight = 0;
      zoom = 1;
      ToolTipManager.sharedInstance().setInitialDelay(10);
      ToolTipManager.sharedInstance().setReshowDelay(10);
      ToolTipManager.sharedInstance().setLightWeightPopupEnabled(true);
      saveImageOption = new JMenuItem("Save");
      saveImageOption.addActionListener(e -> saveImage());
      menu.add(saveImageOption);
    }

    public int getTotalHeight() {
      paint(null);

      return totalHeight;
    }

    void setZoom(final int paramInt) {
      zoom = paramInt;
      updateSize();
    }

    void updateSize() {
      // int zoom=((this.zoom < 100)?(this.zoom*50) :(this.zoom/50));
      setPreferredSize(new Dimension((int)(getParent().getWidth() * ((zoom < 2) ? (1)
          : zoom * 0.5)) - 40, (int)(memoryViewInstance.imagePanel.getTotalHeight()
              * ((zoom < 2) ? (1)
                  : zoom * 0.5))
              + 20));
      revalidate();
      repaint();
    }

    void saveImage() {
      if (!(new File(memoryImagesDir)).exists()) {
        new File(memoryImagesDir).mkdirs();
      }
      // Vector localVector = this.memoryViewInstance.clsProfiler.getInstancesOf("javax.microedition.lcdui.Image");
      // ClassProfiler.Instance instance=((ClassProfiler.Instance)localVector.get(selectedInstance));
      String name = imagePanel.selectedImage.path;
      try {
        ImageIO.write(((Image)imagePanel.selectedImage.obj)._image, "png",
            new File(memoryImagesDir + name + ".png"));
      }
      catch (final IOException ex) {
        Logger.getLogger(MemoryView.class.getName()).log(Level.SEVERE, null, ex);
      }
      //          instance=null;
      //          localVector.removeAllElements();
      //          localVector=null;
      name = null;
    }

    @Override
    public void paint(final Graphics graphics) {
      // System.out.println("calling paint memoryview");
      int i = 10;
      int j = 10;
      int k = 0;
      totalHeight = 0;
      Graphics2D g2D = (Graphics2D)graphics;
      if (g2D != null) {
        // int zoom=((this.zoom < 2)?(this.zoom*50) :(this.zoom/50));
        // System.out.println("scaling zoom is "+zoom);
        g2D.scale(zoom * 0.5, zoom * 0.5);
        g2D.setColor(Color.gray);
        g2D.fillRect(0, 0, getWidth(), getHeight());
        g2D.setStroke(new BasicStroke(0.01F));
      }
      Vector imgsVector = memoryViewInstance.clsProfiler.getInstancesOf("javax.microedition.lcdui.Image");
      // if(previousSort!=isAscending)
      {
        Collections.sort(imgsVector, new ImageSorter(isAscending));
        previousSort = isAscending;
      }
      imagesCount = imgsVector.size();
      if (tooltipTexts != null) {
        tooltipTexts = null;
      }

      if (imagePositions != null) {
        imagePositions.removeAllElements();
        imagePositions = null;
      }
      tooltipTexts = new String[imagesCount];
      imagePositions = new Vector<>(imagesCount);
      for (int m = 0; m < imagesCount; m++) {
        if (m == selectedInstance) {
          selectedImage = ((ClassProfiler.Instance)imgsVector.get(m));
          tooltipTexts[m] = MemoryView.EmptyString;
        }
        final Image curImage = (Image)((ClassProfiler.Instance)imgsVector.get(m)).obj;
        if ((curImage.drawCount <= 0) && (!memoryViewInstance.imgsUnused.isSelected())) {
          imagePositions.add(new Rectangle());
          tooltipTexts[m] = MemoryView.EmptyString;
          continue;
        }
        if ((curImage.drawCount != 0) && (!memoryViewInstance.imgsUsed.isSelected())) {
          imagePositions.add(new Rectangle());
          continue;
        }

        if (((ClassProfiler.Instance)imgsVector.get(m)) != null) {
          tooltipTexts[m] = ((ClassProfiler.Instance)imgsVector.get(m)).path;
        }
        else {
          tooltipTexts[m] = MemoryView.EmptyString;
        }
        if ((j + curImage.getWidth() + 20) > getParent().getWidth()) {
          j = 10;
          i += k + 10;
          k = 0;
        }
        //              if (((curImage.drawCount <= 0) || (!this.memoryViewInstance.imgsUnused.isSelected())) && ((curImage.drawCount != 0) || (!this.memoryViewInstance.imgsUnused.isSelected()))) {
        //                  continue;
        //              }
        final int n = 1; // this.zoom == 100 ? 1 : 0;
        if (g2D != null) {
          g2D.setColor((m == selectedInstance) ? Color.green
              : Color.black);
          g2D.drawRect(j - n, i - n, curImage.getWidth() + n, curImage.getHeight() + n);
          g2D.drawImage(curImage._image, j, i, null);
          imagePositions.add(new Rectangle(j, i, j + curImage.getWidth(), i + curImage.getHeight()));
        }
        j += curImage.getWidth() + 10;
        k = Math.max(k, curImage.getHeight());
      }
      totalHeight = (i + k);
      imgsVector = null;
      g2D = null;
    }

    private void findImage(final MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      final double val = (zoom * 0.5);
      if (zoom < 2) {
        x = x * 2;
        y = y * 2;
      }
      else if (zoom > 2) {
        x = (int)(x / val);
        y = (int)(y / val);
      }
      final int len = imagePositions.size();
      for (int i = 0; i < len; i++) {
        if (imagePositions.get(i).contains(x, y)) {
          selectedInstance = i;
        }
      }
      pointedX = x;
      pointedY = y;
    }

    public void resetImageCount() {
      Vector localVector = memoryViewInstance.clsProfiler.getInstancesOf("javax.microedition.lcdui.Image");
      imagesCount = localVector.size();
      Image curImage = null;
      for (final Object local : localVector) {
        curImage = (Image)((Instance)local).obj;
        curImage.drawCount = 0;

      }
      curImage = null;
      localVector = null;
    }

    public void deInitialize() {
      imagePositions = null;
      imagesCount = 0;
    }
    //      public void mouseClicked(MouseEvent e) {
    //          findImage(e);
    //      }
    //
    //      public void mousePressed(MouseEvent e) {
    //
    //      }
    //
    //      public void mouseReleased(MouseEvent e) {
    //
    //      }
    //
    //      public void mouseEntered(MouseEvent e) {
    //
    //      }
    //
    //      public void mouseExited(MouseEvent e) {
    //
    //      }
    //
  }
}
