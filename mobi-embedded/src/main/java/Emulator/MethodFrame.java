package Emulator;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class MethodFrame extends JFrame {

  /**
   *
   */
  private static final long serialVersionUID = 8862126493747942267L;
  public static Hashtable methods = new Hashtable();
  private String filterString = "";
  private boolean iskeyTyped = false;
  private JButton filterButton;
  private JLabel filterLabel;
  private JPanel filterPanel;
  private JTextField filterText;
  private MethodModel methodModel;
  private JPanel methodPanel;
  private JTable methodTable;
  private JButton resetCalls;
  private JScrollPane scrollPane;

  public MethodFrame() {
    init();
  }

  public void init() {
    setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    setTitle("Method Profiler");
    setBounds(100, 100, 450, 300);
    setLayout(new BorderLayout());
    methodPanel = new JPanel();
    methodPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    methodPanel.setLayout(new BorderLayout(0, 0));
    resetCalls = new JButton("ResetCalls");
    resetCalls.addActionListener(e -> resetMethods());
    filterLabel = new JLabel("Filter");
    filterText = new JTextField();
    filterText.setColumns(25);
    filterText.addKeyListener(new KeyAdapter() {

      @Override
      public void keyTyped(final KeyEvent e) {
        filterString = filterText.getText();
        iskeyTyped = true;
      }

      @Override
      public void keyPressed(final KeyEvent e) {
        filterString = filterText.getText();
        iskeyTyped = true;
      }
    });
    filterPanel = new JPanel(new BorderLayout());
    filterPanel.add(filterLabel, BorderLayout.WEST);
    filterPanel.add(filterText, BorderLayout.CENTER);
    filterPanel.add(resetCalls, BorderLayout.EAST);
    scrollPane = new JScrollPane();
    methodPanel.add(scrollPane, BorderLayout.CENTER);
    methodTable = new JTable();
    methodModel = new MethodModel();
    methodTable.setModel(methodModel);
    updateMethodFrame();
    scrollPane.setViewportView(methodTable);
    getContentPane().add(methodPanel, BorderLayout.CENTER);
    getContentPane().add(filterPanel, BorderLayout.NORTH);
  }

  void addMethods(final ClassNode classNode) {
    Iterator it = classNode.methods.iterator();
    while (it.hasNext()) {
      Method method = new Method(classNode.name, (MethodNode)it.next());
      MethodFrame.methods.put(method.methodpath, method);
      method = null;
    }
    it = null;
  }

  void resetMethods() {
    Enumeration methodList = MethodFrame.methods.elements();
    while (methodList.hasMoreElements()) {
      ((Method)methodList.nextElement()).reset();
    }
    // methodModel.setData(methodData);
    methodList = null;
  }

  void updateMethodFrame() {
    // System.out.println("in update method frame");
    Vector methodData = new Vector();
    Enumeration methodList = MethodFrame.methods.elements();
    while (methodList.hasMoreElements()) {
      Method mthd = (Method)methodList.nextElement();
      if (filterString.equals("") || mthd.node.name.equals(filterString)) {
        methodData.add(mthd);
      }
      mthd = null;
    }
    methodModel.setData(methodData);
    methodData = null;
    methodList = null;
    if (!filterString.equals("") && iskeyTyped) {
      methodModel.fireTableDataChanged();
      iskeyTyped = false;
    }
  }

  public void deInitialize() {
    final int len = MethodFrame.methods.size();
    for (int i = 0; i < len; i++) {
      Method mthd = ((Method)MethodFrame.methods.get(i));
      if (mthd != null) {
        mthd.deInitialize();
      }
      mthd = null;
    }
    MethodFrame.methods.clear();
    updateMethodFrame();
  }

  class Method {

    int callCount = 0;
    int entrycount = 0;
    int exitcount = 0;
    int refCount = 0;
    float averageTime;
    long beginTime;
    List bytecode;
    String className;
    private String methodDescription;
    public String methodpath;
    MethodNode node;
    float percentTime;
    long totalTime;

    public Method(final String clsName, final MethodNode methodNode) {
      className = clsName;
      node = methodNode;
      formatmethodName();
      methodpath = clsName + "." + node.name + node.desc;
      refCount = 0;
      callCount = 0;
    }

    private void formatmethodName() {
      org.objectweb.asm.commons.Method m = new org.objectweb.asm.commons.Method(node.name, node.desc);
      final StringBuffer sb = new StringBuffer();
      sb.append(m.getReturnType().getClassName()).append(" ").append(node.name).append("(");
      org.objectweb.asm.Type[] paramTypes = m.getArgumentTypes();
      for (int i = 0; i < paramTypes.length; i++) {
        sb.append(paramTypes[i].getClassName()).append((paramTypes[i].getSize() > 1) ? "[]" : "").append((i < (paramTypes.length - 1)) ? ", " : "");
      }
      sb.append(")");
      m = null;
      paramTypes = null;
      methodDescription = sb.toString();
      sb.setLength(0);
    }

    @Override
    public String toString() {
      return methodpath;
    }

    public void deInitialize() {
      methodpath = null;
      methodDescription = null;
      reset();
      className = null;
    }

    public void reset() {
      callCount = 0;
      averageTime = 0;
      totalTime = 0;
      beginTime = 0;
      percentTime = 0;
      totalTime = 0;
      entrycount = 0;
      exitcount = 0;
    }
  }

  public class MethodModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 4497066695245844604L;
    String[] cols = new String[] {
        "class", "method", "description", "refcount", "entrys", "exits", "callcount", "avg time", "totaltime"
    };
    Vector data = new Vector();

    MethodModel() {
    }

    @Override
    public String getColumnName(final int col) {
      return cols[col];
    }

    @Override
    public int getRowCount() {
      return data.size();
    }

    @Override
    public int getColumnCount() {
      return cols.length;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
      Method curMethod = (Method)data.get(rowIndex);
      switch (columnIndex) {
        case 0:
          return curMethod.className;
        case 1:
          return curMethod.node.name;
        case 2:
          return curMethod.methodDescription;
        case 3:
          return String.valueOf(curMethod.refCount);
        case 4:
          return String.valueOf(curMethod.entrycount);
        case 5:
          return String.valueOf(curMethod.exitcount);
        case 6:
          return String.valueOf(curMethod.callCount);
        case 7:
          return String.valueOf(curMethod.averageTime);
        case 8:
          return String.valueOf(curMethod.totalTime);
      }
      curMethod = null;

      return "";
    }

    public void setData(final Vector Data) {
      if (data != null) {
        data = null;
      }

      data = Data;
      Rectangle visibleRect = methodTable.getVisibleRect();
      final int len = data.size();
      for (int i = 0; i < len; i++) {
        final Rectangle cellRect = methodTable.getCellRect(i, 4, false); // updating cell entry
        if (visibleRect.contains(cellRect) || visibleRect.intersects(cellRect)) {
          methodTable.repaint(cellRect);
        }
        final Rectangle cellRect1 = methodTable.getCellRect(i, 5, false); // updating avg time exit
        if (visibleRect.contains(cellRect1) || visibleRect.intersects(cellRect1)) {
          methodTable.repaint(cellRect1);
        }
        final Rectangle cellRect2 = methodTable.getCellRect(i, 6, false); // updating totaltime  callcount
        if (visibleRect.contains(cellRect2) || visibleRect.intersects(cellRect2)) {
          methodTable.repaint(cellRect2);
        }
        final Rectangle cellRect3 = methodTable.getCellRect(i, 7, false); // updating avg time callcount
        if (visibleRect.contains(cellRect3) || visibleRect.intersects(cellRect3)) {
          methodTable.repaint(cellRect3);
        }
        final Rectangle cellRect4 = methodTable.getCellRect(i, 9, false); // updating totaltime  callcount
        if (visibleRect.contains(cellRect4) || visibleRect.intersects(cellRect4)) {
          methodTable.repaint(cellRect4);
        }
      }
      visibleRect = null;
    }
  }

}
