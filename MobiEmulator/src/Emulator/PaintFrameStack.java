/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Graphics;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class PaintFrameStack extends JFrame implements TreeSelectionListener {
    private int                  count            = 0;
    private int                  prevNoFrames     = 0;
    private ImageTask            curImageTask     = null;
    private PrimitiveTask        curPrimitiveTask = null;
    private int                  frameNumber      = 0;
    private Vector<PaintTask>    frameTask        = new Vector();
    private Hashtable            paintFrames      = new Hashtable();
    private Vector               nameList         = new Vector();
    private boolean              isPlayEnabled    = false;
    private String               selectedFrame    = "Frame0";
    private int                  selectionIndex   = 0;
    private boolean              recordingStarted = false;
    private Boolean              isStop           = false;
    private JCheckBox            borderBox;
    private JCheckBox            clipBox;
    private boolean              completedFrame;
    private int                  delay;
    private JLabel               delayLabel;
    private JTextField           delayValue;
    private IntegerField         endField;
    private JLabel               endLabel;
    private int                  endValue;
    private JScrollPane          eventScrollPane;
    private JTree                eventtree;
    private JScrollPane          exceptionScrollPane;
    private BufferedImage        image;
    private JSplitPane           imageInfoSplitPane;
    private JPanel               imageListPanel;
    private PaintStackImagePanel imagePanel;
    private JScrollPane          imageScrollPane;
    private JPanel               imageViewPanel;
    private InfoPanel            infoPanel;
    private JScrollPane          infoScrollPane;
    private JSplitPane           mainSplitPane;
    private JPanel               playPanel;
    private JButton              playPause;
    private int                  prevNodeIndex;
    private IntegerField         recordFramesValue;
    private JLabel               recordframesLabel;
    private JTabbedPane          stackInfoTabPane;
    private JScrollPane          stackScrollPane;
    private JList                stacklist;
    private JTextArea            stacktraceLabel;
    private IntegerField         startField;
    private JLabel               startLabel;
    private int                  startValue;
    private JButton              stop;
    private JLabel               zoomLabel;
    private JSlider              zoomSlider;
    private PaintThread          paintThread;
    public PaintFrameStack() {
        super();
        getContentPane().setLayout(new BorderLayout());
        setTitle("PaintRecorder");
        init();
        //
    }

    public boolean isRecording() {
        return recordingStarted;
    }
    private void init() {
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mainSplitPane = new JSplitPane();
        paintThread   = new PaintThread();
        mainSplitPane.setDividerSize(8);
        mainSplitPane.setDividerLocation(100);
        getContentPane().add(mainSplitPane);
        imageListPanel = new JPanel();
        imageListPanel.setLayout(new BorderLayout());
        mainSplitPane.setLeftComponent(imageListPanel);
        stackScrollPane = new JScrollPane();
        stacklist       = new JList();
        stackScrollPane.setViewportView(stacklist);
        // stacklist.addListSelectionListener(this);
        // imageListPanel.add(stackScrollPane, BorderLayout.NORTH);
        eventScrollPane = new JScrollPane();
        if (paintFrames.size() > 0) {
            eventtree = new JTree(paintFrames);
        } else {
            eventtree = new JTree();
        }
        eventtree.addTreeSelectionListener(this);
        if (paintFrames.size() > 0) {
            eventScrollPane.setViewportView(eventtree);
        }
        imageListPanel.add(eventScrollPane, BorderLayout.CENTER);
        imageViewPanel = new JPanel();
        imageViewPanel.setLayout(new BorderLayout());
        mainSplitPane.setRightComponent(imageViewPanel);
        mainSplitPane.setContinuousLayout(true);
        imageInfoSplitPane = new JSplitPane();
        imageInfoSplitPane.setDividerSize(8);
        imageInfoSplitPane.setResizeWeight(1.0);
        imageInfoSplitPane.setMinimumSize(new Dimension(100, 100));
        imageInfoSplitPane.setDividerLocation(250);
        imageInfoSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        imageInfoSplitPane.setContinuousLayout(true);
        imageViewPanel.add(imageInfoSplitPane, BorderLayout.CENTER);
        imagePanel      = new PaintStackImagePanel();
        imageScrollPane = new JScrollPane();
        imageScrollPane.setViewportView(imagePanel);
        imageInfoSplitPane.setLeftComponent(imageScrollPane);
        image = new BufferedImage(MobiEmulator.mobiEmulatorInstance.getCanvasWidth(), MobiEmulator.mobiEmulatorInstance.getCanvasHeight(),
                                  BufferedImage.TYPE_INT_ARGB);
        stackInfoTabPane    = new JTabbedPane();
        exceptionScrollPane = new JScrollPane();
        stacktraceLabel     = new JTextArea();
        exceptionScrollPane.setViewportView(stacktraceLabel);
        stackInfoTabPane.add(exceptionScrollPane, "StackTrace");
        infoPanel      = new InfoPanel();
        infoScrollPane = new JScrollPane(infoPanel);
        stackInfoTabPane.addTab("Image/Info", null, infoScrollPane, null);
        imageInfoSplitPane.setRightComponent(stackInfoTabPane);
        playPanel = new JPanel();
        playPanel.setLayout(new BoxLayout(playPanel, BoxLayout.X_AXIS));
        getContentPane().add(playPanel, BorderLayout.NORTH);
        recordframesLabel = new JLabel();
        recordframesLabel.setText("RecordFrames");
        playPanel.add(recordframesLabel);
        recordFramesValue = new IntegerField();
        recordFramesValue.setColumns(3);
        recordFramesValue.setText("1");
        playPanel.add(recordFramesValue);
        playPause = new JButton();
        playPause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ie) {
                if (MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable() == null) {
                    return;
                }
                // if (playPause.isSelected()) {
                // playPause.setText("Pause");
                isPlayEnabled = true;
                Vector tasks = ((Vector<PaintTask>) paintFrames.get(selectedFrame));
                try {
                    delay = Integer.parseInt(delayValue.getText());
                } catch (Exception e) {
                    delay = 200;
                }
                try {
                    startValue = Integer.parseInt(startField.getText());
                } catch (Exception e) {
                    startValue = prevNodeIndex;
                    if (prevNodeIndex == tasks.size() - 1) {
                        startValue = 0;
                    }
                }
                try {
                    endValue = Integer.parseInt(endField.getText());
                } catch (Exception e) {
                    endValue = tasks.size() - 1;
                }
                selectionIndex = prevNodeIndex;
//                if(selectionIndex==0)
//                   paintThread.setGraphics(createGraphics());
                drawTasks(tasks, selectionIndex);
                // } else {
                // playPause.setText("Play");
                // isPlayEnabled = false;
                // }
            }
        });
        playPause.setText("Play");
        playPanel.add(playPause);
        stop = new JButton();
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                isPlayEnabled = false;
                // selectionIndex=count+1;
                playPause.setSelected(false);
                paintThread.stop();
                isStop = true;
            }
        });
        stop.setText("Stop");
        playPanel.add(stop);
        startLabel = new JLabel("StartIndex");
        startField = new IntegerField(3);
        startField.setColumns(3);
        playPanel.add(startLabel);
        playPanel.add(startField);
        endLabel = new JLabel("ToIndex");
        endField = new IntegerField(3);
        endField.setColumns(3);
        playPanel.add(endLabel);
        playPanel.add(endField);
        delayLabel = new JLabel();
        delayLabel.setText("Delay ms");
        playPanel.add(delayLabel);
        delayValue = new IntegerField(3);
        delayValue.setColumns(3);
        delayValue.setText("200");
        playPanel.add(delayValue);
        borderBox = new JCheckBox();
        borderBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ce) {
                imagePanel.highlightImageBorder = borderBox.isSelected();
                imagePanel.repaint();
            }
        });
        borderBox.setText("showBorder");
        playPanel.add(borderBox);
        clipBox = new JCheckBox();
        clipBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ce) {
                imagePanel.highlightClip = clipBox.isSelected();
                imagePanel.repaint();
            }
        });
        clipBox.setText("showClip");
        playPanel.add(clipBox);
        zoomLabel = new JLabel();
        zoomLabel.setText("Zoom");
        playPanel.add(zoomLabel);
        zoomSlider = new JSlider();
        zoomSlider.setMinorTickSpacing(1);
        zoomSlider.setMajorTickSpacing(1);
        zoomSlider.setPreferredSize(new Dimension(140, 20));
        zoomSlider.setValue(1);
        zoomSlider.setMinimumSize(new Dimension(10, 20));
        zoomSlider.setMaximumSize(new Dimension(20, 20));
        zoomSlider.setMaximum(4);
        zoomSlider.setMinimum(1);
        zoomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                if (!zoomSlider.getValueIsAdjusting()) {
                    imagePanel.setZoom(zoomSlider.getValue());
                    imagePanel.repaint();
                }
            }
        });
        playPanel.add(zoomSlider);
    }
    private Graphics createGraphics() {
        image.getGraphics().setColor(Color.GRAY);
        image.getGraphics().fillRect(0, 0, image.getWidth(), image.getHeight());
        return new Graphics(image.createGraphics(), image);  
    }
    public void updateImageDimension(int w, int h) {
        image = null;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }
    public void setPaintStack() {
        eventScrollPane.remove(eventtree);
        eventtree = null;
        imageListPanel.remove(eventScrollPane);
        // sort hashtable
        if (paintFrames.size() > 0) {
            eventtree = new JTree(paintFrames);
        } else {
            eventtree = new JTree();
        }
        eventtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        eventtree.addTreeSelectionListener(this);
        if (paintFrames.size() > 0) {
            eventScrollPane.setViewportView(eventtree);
        }
        imageListPanel.add(eventScrollPane);
//      stackScrollPane.remove(stacklist);
//      stacklist.removeListSelectionListener(this);
//      imageListPanel.remove(stackScrollPane);
//
//      stacklist = null;
//      stacklist = new JList(nameList);
//      stacklist.addListSelectionListener(this);
//      stackScrollPane.setViewportView(stacklist);
//      imageListPanel.add(stackScrollPane);
    }
    public void valueChanged(TreeSelectionEvent e) {
        if (MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable() == null) {
            return;
        }
        if (eventtree == null) {
            return;
        }
        if (isPlayEnabled) {
            return;
        }
        DefaultMutableTreeNode node      = (DefaultMutableTreeNode) eventtree.getSelectionPath().getLastPathComponent();
        int                    nodeIndex = node.getParent().getIndex(node);
        String                 root;
        if (node.isLeaf())    // leaf selected
        {
            root          = node.getParent().toString();
            prevNodeIndex = nodeIndex;
            selectedFrame = root;
        } else                // root selected
        {
            root          = node.getUserObject().toString();
            nodeIndex     = node.getChildCount() - 1;
            selectedFrame = root;
        }
        if (!isPlayEnabled) {
            if (!node.isRoot()) {
                Vector tasks = ((Vector<PaintTask>) paintFrames.get(root));
                drawTasks(tasks, nodeIndex);
            }
        }
    }
    public void drawTasks(Vector tasks, int selectionIndex) {
        paintThread.start(tasks, selectionIndex);
        paintThread.start();
    }
    
    public void addFrameToStack(String frameno, Vector<PaintTask> frame) {
        paintFrames.put(frameno, frame.clone());
    }
    public void removeFrameFromStack(String frameno) {
        paintFrames.remove(frameno);
    }
    public void clearAllFrames() {
        if (paintFrames.size() > 1) {
            paintFrames.clear();
        }
    }
    public void startFrame() {
        frameTask      = null;
        frameTask      = new Vector();
        completedFrame = false;
    }
    public void endFrame() {
        addFrameToStack("Frame" + frameNumber, frameTask);
        frameTask = null;
        frameNumber++;
        completedFrame = true;
        // nameList.removeAllElements();
    }
    public void endRecording() {
        setPaintStack();
        setVisible(true);
        recordingStarted                   = false;
        if (MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable() != null) {
            MobiEmulator.mobiEmulatorInstance.canvasPanel.canvasPause();
        }
        MobiEmulator.mobiEmulatorInstance.performanceInvestigate = false;
    }
    public void startRecording() {
        frameNumber = 0;
        MobiEmulator.mobiEmulatorInstance.paintRecordCount = 0;
        clearAllFrames();
        if (MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable() != null) {
            MobiEmulator.mobiEmulatorInstance.canvasPanel.canvasResume();
            MobiEmulator.mobiEmulatorInstance.performanceInvestigate = true;
            startFrame();
            recordingStarted = true;
        } else {
            endRecording();
        }
    }
    public Vector getTasks() {
        return frameTask;
    }
    public int getRecordFrameCount() {
        try {
            int noOfFramesReq=Integer.parseInt(recordFramesValue.getText());
            if(prevNoFrames!=noOfFramesReq)
                prevNoFrames=noOfFramesReq;
            return prevNoFrames;
        } catch (Exception e) {
            return 1;
        }
    }
    class InfoPanel extends JPanel {
        private Dimension dimen;
        private String    drawText;
        private Image     infoImage;
        public InfoPanel() {
            dimen = new Dimension(getPreferredSize());
        }

        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            if (infoImage != null) {
                g.drawImage(infoImage, 0, 0, null);
            }
            if ((drawText != null) &&!drawText.equals("")) {
                g.setColor(Color.BLACK);
                g.drawString(drawText, 0, (infoImage != null) ? infoImage.getHeight(null) + 20
                        : 0);
            }
            if (infoImage != null) {
                dimen.width  = infoImage.getWidth(null) + 2;
                dimen.height = infoImage.getHeight(null) + 20;
                setPreferredSize(dimen);
            }
            infoScrollPane.invalidate();
            infoScrollPane.setViewportView(this);
        }
        public void setImage(BufferedImage img) {
            this.infoImage = null;
            this.infoImage = img;
        }
        private void setText(String string) {
            drawText = string;
        }
    }

    class PaintStackImagePanel extends JPanel {
        public boolean highlightBorder      = true;
        private double zoomvalue            = 1.0;
        public boolean highlightImageBorder = false;
        public boolean highlightClip        = false;
        private int    clipheight;
        private int    clipwidth;
        private int    clipx;
        private int    clipy;
        private int    height;
        private int    width;
        private int    x;
        private int    y;
        public PaintStackImagePanel() {
            setPreferredSize(new Dimension(MobiEmulator.mobiEmulatorInstance.getCanvasWidth(), MobiEmulator.mobiEmulatorInstance.getCanvasHeight()));
        }
        public Dimension getPreferredSize() {
            return new Dimension((int) (MobiEmulator.mobiEmulatorInstance.getCanvasWidth() * zoomvalue),
                                 (int) (MobiEmulator.mobiEmulatorInstance.getCanvasHeight() * zoomvalue));
        }
        @Override
        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            Graphics2D g2D = (Graphics2D) g;
            g2D.scale(zoomvalue, zoomvalue);
            g2D.drawImage(image, 0, 0, null);
            if (highlightBorder) {
                g2D.setColor(Color.BLUE);
                g2D.drawRect(0, 0, MobiEmulator.mobiEmulatorInstance.getCanvasWidth(), MobiEmulator.mobiEmulatorInstance.getCanvasHeight());
            }
            if (highlightImageBorder) {
                g2D.setColor(Color.GREEN);
                g2D.drawRect(x, y, width, height);
            }
            if (highlightClip) {
                g2D.setColor(Color.RED);
                g2D.drawRect(clipx, clipy, clipwidth, clipheight);
            }
        }
        public void setZoom(double zoom) {
            this.zoomvalue = zoom;
            setPreferredSize(new Dimension((int) (MobiEmulator.mobiEmulatorInstance.getCanvasWidth() * this.zoomvalue),
                                           (int) (MobiEmulator.mobiEmulatorInstance.getCanvasHeight() * this.zoomvalue)));
            imageScrollPane.invalidate();
            imageScrollPane.setViewportView(this);
        }
        public void drawImageBorder(int x, int y, int w, int h) {
            this.x      = x;
            this.y      = y;
            this.width  = w;
            this.height = h;
        }
        public void drawClip(int x, int y, int w, int h) {
            this.clipx      = x;
            this.clipy      = y;
            this.clipwidth  = w;
            this.clipheight = h;
        }
    }

    class PaintThread implements Runnable {
        int     selectionIndex = 0;
        boolean isPaused       = true;
        Vector  tasks;
        Thread  paintingThread;
        Graphics g;
        public PaintThread() {
            paintingThread = new Thread(this);
            paintingThread.start();
        }

        public void start(Vector tasks, int selectionIndex) {
            this.tasks          = tasks;
            this.selectionIndex = selectionIndex;
            isPaused            = false;
        }
        public void start() {
            isPaused            = false;
        }
        public void stop() {
            isPaused = true;
        }
        public void run() {
            while (true) {
                if (!isPaused&&tasks!=null) {
                    int len = tasks.size();
                    count = 0;
                   if (selectionIndex < len) {
                        if (isPlayEnabled) {
                            count          = startValue;
                            selectionIndex = endValue;
                        }
                        if(count==0) {
                            image.getGraphics().setColor(Color.GRAY);
                            image.getGraphics().fillRect(0, 0, image.getWidth(), image.getHeight());
                            g= new Graphics(image.createGraphics(), image);  
                        }
                        while (count <= selectionIndex) {
                            if (tasks.get(count) instanceof PrimitiveTask) {
                                curPrimitiveTask = null;
                                curPrimitiveTask = ((PrimitiveTask) tasks.get(count));
                                curPrimitiveTask.drawTask(g);
                                infoPanel.setImage(null);
                                infoPanel.setText("Type: " + curPrimitiveTask.getTypeString() + " drawn at X "
                                                  + curPrimitiveTask.x + " Y " + curPrimitiveTask.y + " W "
                                                  + curPrimitiveTask.width + " H " + curPrimitiveTask.height
                                                  + " color " + curPrimitiveTask.color);
                                imagePanel.drawImageBorder(curPrimitiveTask.x, curPrimitiveTask.y,
                                                           curPrimitiveTask.width, curPrimitiveTask.height);
                                imagePanel.drawClip(curPrimitiveTask.clipX, curPrimitiveTask.clipY,
                                                    curPrimitiveTask.clipWidth, curPrimitiveTask.clipHeight);
                                stacktraceLabel.setText(curPrimitiveTask.getStackTrace());
                            } else {
                                curImageTask = null;
                                curImageTask = ((ImageTask) tasks.get(count));
                                curImageTask.drawTask(g);
                                if (curImageTask.image != null) {
                                    infoPanel.setImage(curImageTask.image._image);
                                }
                                infoPanel.setText("Type: " + curImageTask.getTypeString() + " drawn at X "
                                                  + curImageTask.x + " Y " + curImageTask.y + " W "
                                                  + curImageTask.width + " H " + curImageTask.height);
                                imagePanel.drawImageBorder((curImageTask.destx == 0) ? curImageTask.x
                                        : curImageTask.destx, (curImageTask.desty == 0) ? curImageTask.y
                                        : curImageTask.desty, curImageTask.width, curImageTask.height);
                                imagePanel.drawClip(curImageTask.clipX, curImageTask.clipY, curImageTask.clipWidth,
                                                    curImageTask.clipHeight);
                                stacktraceLabel.setText(curImageTask.getStackTrace());
                            }
                            
                            if (isPlayEnabled) {
                                // selectionIndex=len;
                                if (count == selectionIndex) {
                                    isPlayEnabled = false;
                                }
                                imagePanel.paintComponent(imagePanel.getGraphics());
                                try {
                                    Thread.sleep(delay);
                                    // eventtree.setSelectionRow(count+1);
                                    eventtree.setSelectionPath(eventtree.getPathForRow(count + 1));
                                    eventScrollPane.invalidate();
                                } catch (Exception ex) {}
                                // stacklist.setSelectedIndex(count);
                            }
                            count++;
                        }
                    }
                    infoPanel.repaint();
                    imagePanel.repaint();
                    isPaused=true;
                } else {
                        try {
                        paintingThread.sleep(1000);
                    } catch (Exception e) {}
                
                }
            }
        }

//        private void setGraphics(Graphics createGraphics) {
//            g=createGraphics;
//        }
    }
}
