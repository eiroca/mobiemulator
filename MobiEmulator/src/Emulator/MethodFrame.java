/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- non-JDK imports --------------------------------------------------------

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class MethodFrame extends JFrame {
    public static Hashtable methods      = new Hashtable();
    private String          filterString = "";
    private boolean         iskeyTyped   = false;
    private JButton         filterButton;
    private JLabel          filterLabel;
    private JPanel          filterPanel;
    private JTextField      filterText;
    private MethodModel     methodModel;
    private JPanel          methodPanel;
    private JTable          methodTable;
    private JButton         resetCalls;
    private JScrollPane     scrollPane;
    public MethodFrame() {
        init();
    }

    public void init() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Method Profiler");
        setBounds(100, 100, 450, 300);
        setLayout(new BorderLayout());
        methodPanel = new JPanel();
        methodPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        methodPanel.setLayout(new BorderLayout(0, 0));
        resetCalls = new JButton("ResetCalls");
        resetCalls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetMethods();
            }
        });
        filterLabel = new JLabel("Filter");
        filterText  = new JTextField();
        filterText.setColumns(25);
        filterText.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                filterString = filterText.getText();
                iskeyTyped   = true;
            }
            public void keyPressed(KeyEvent e) {
                filterString = filterText.getText();
                iskeyTyped   = true;
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
    void addMethods(ClassNode classNode) {
        Iterator it = classNode.methods.iterator();
        while (it.hasNext()) {
            Method method = new Method(classNode.name, (MethodNode) it.next());
            methods.put(method.methodpath, method);
            method = null;
        }
        it = null;
    }
    void resetMethods() {
        Enumeration methodList = methods.elements();
        while (methodList.hasMoreElements()) {
            ((Method) methodList.nextElement()).reset();
        }
        // methodModel.setData(methodData);
        methodList = null;
    }
    void updateMethodFrame() {
        // System.out.println("in update method frame");
        Vector      methodData = new Vector();
        Enumeration methodList = methods.elements();
        while (methodList.hasMoreElements()) {
            Method mthd = (Method) methodList.nextElement();
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
        int len = methods.size();
        for (int i = 0;i < len;i++) {
            Method mthd = ((Method) methods.get(i));
            if (mthd != null) {
                mthd.deInitialize();
            }
            mthd = null;
        }
        methods.clear();
        updateMethodFrame();
    }
    class Method {
        int            callCount  = 0;
        int            entrycount = 0;
        int            exitcount  = 0;
        int            refCount   = 0;
        float          averageTime;
        long           beginTime;
        List           bytecode;
        String         className;
        private String methodDescription;
        public String  methodpath;
        MethodNode     node;
        float          percentTime;
        long           totalTime;
        public Method(String clsName, MethodNode methodNode) {
            className = clsName;
            node      = methodNode;
            formatmethodName();
            methodpath = clsName + "." + node.name + node.desc;
            refCount   = 0;
            callCount  = 0;
        }

        private void formatmethodName() {
            org.objectweb.asm.commons.Method m          = new org.objectweb.asm.commons.Method(node.name, node.desc);
            StringBuffer sb=new StringBuffer();
            sb.append(m.getReturnType().getClassName()).append(" ").append(node.name).append("(");
            Type[]                           paramTypes = m.getArgumentTypes();
            for (int i = 0;i < paramTypes.length;i++) {
                sb.append(paramTypes[i].getClassName()).append((paramTypes[i].getSize() > 1) ? "[]"
                        : "").append((i < paramTypes.length - 1) ? ", "
                        : "");
            }
            sb.append(")");
            m                 = null;
            paramTypes        = null;
            methodDescription = sb.toString();
            sb.setLength(0);
        }
        public String toString() {
            return methodpath;
        }
        public void deInitialize() {
            methodpath        = null;
            methodDescription = null;
            reset();
            className = null;
        }
        public void reset() {
            callCount   = 0;
            averageTime = 0;
            totalTime   = 0;
            beginTime   = 0;
            percentTime = 0;
            totalTime   = 0;
            entrycount  = 0;
            exitcount   = 0;
        }
    }

    public class MethodModel extends AbstractTableModel {
        String[] cols = new String[] {
            "class", "method", "description", "refcount", "entrys", "exits", "callcount", "avg time", "totaltime"
        };
        Vector   data = new Vector();
        MethodModel() {}

        public String getColumnName(int col) {
            return this.cols[col];
        }
        public int getRowCount() {
            return data.size();
        }
        public int getColumnCount() {
            return cols.length;
        }
        public Object getValueAt(int rowIndex, int columnIndex) {
            Method curMethod = (Method) data.get(rowIndex);
            switch (columnIndex) {
            case 0 :
                return curMethod.className;
            case 1 :
                return curMethod.node.name;
            case 2 :
                return curMethod.methodDescription;
            case 3 :
                return String.valueOf(curMethod.refCount);
            case 4 :
                return String.valueOf(curMethod.entrycount);
            case 5 :
                return String.valueOf(curMethod.exitcount);
            case 6 :
                return String.valueOf(curMethod.callCount);
            case 7 :
                return String.valueOf(curMethod.averageTime);
            case 8 :
                return String.valueOf(curMethod.totalTime);
            }
            curMethod = null;

            return "";
        }
        public void setData(Vector Data) {
            if (data != null) {
                data = null;
            }

            data = Data;
            Rectangle visibleRect = methodTable.getVisibleRect();
            int       len         = data.size();
            for (int i = 0;i < len;i++) {
                Rectangle cellRect = methodTable.getCellRect(i, 4, false);     // updating cell entry
                if (visibleRect.contains((Rectangle) cellRect) || visibleRect.intersects((Rectangle) cellRect)) {
                    methodTable.repaint(cellRect);
                }
                Rectangle cellRect1 = methodTable.getCellRect(i, 5, false);    // updating avg time exit
                if (visibleRect.contains((Rectangle) cellRect1) || visibleRect.intersects((Rectangle) cellRect1)) {
                    methodTable.repaint(cellRect1);
                }
                Rectangle cellRect2 = methodTable.getCellRect(i, 6, false);    // updating totaltime  callcount
                if (visibleRect.contains((Rectangle) cellRect2) || visibleRect.intersects((Rectangle) cellRect2)) {
                    methodTable.repaint(cellRect2);
                }
                Rectangle cellRect3 = methodTable.getCellRect(i, 7, false);    // updating avg time callcount
                if (visibleRect.contains((Rectangle) cellRect3) || visibleRect.intersects((Rectangle) cellRect3)) {
                    methodTable.repaint(cellRect3);
                }
                Rectangle cellRect4 = methodTable.getCellRect(i, 9, false);    // updating totaltime  callcount
                if (visibleRect.contains((Rectangle) cellRect4) || visibleRect.intersects((Rectangle) cellRect4)) {
                    methodTable.repaint(cellRect4);
                }
            }
            visibleRect = null;
        }
    }
}
