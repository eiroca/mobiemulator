/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnectionImpl;
import javax.wireless.messaging.TextMessage;
import javax.wireless.messaging.TextMessageImpl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class CallSMSManager extends JFrame {
    private boolean        blockIncommingMessage = false;
    private String         message               = null;
    private Vector         smsBulk               = new Vector();
    JButton                ignore                = new JButton("Ignore");
    JButton                accept                = new JButton("Accept");
    private JButton        callButton;
    private JLabel         callLabel;
    private IntegerField   callNumber;
    private JPanel         callPanel;
    private JButton        disConnect;
    private CallSMSManager instance;
    JOptionPane            pane;
    private JButton        smsClear;
    private JPanel         smsPanel;
    private JButton        smsSend;
    private JTextArea      smsTextArea;
    public CallSMSManager() {
        createWindow();
    }

    public final Message receive(String address) throws IOException, InterruptedIOException {
        if (this.smsBulk.size() > 0) {
            String str = (String) this.smsBulk.firstElement();
            this.smsBulk.removeElementAt(0);
            TextMessageImpl textMessage = new TextMessageImpl(address);
            textMessage.setPayloadText(str);

            return textMessage;
        }

        return null;
    }
    public final void send(Message mess, String paramString) throws IOException, InterruptedIOException {
        if (this.blockIncommingMessage) {
            throw new InterruptedIOException();
        }
        Message currMessage;
        if (mess instanceof TextMessage) {
            currMessage = (TextMessage) mess;
            message     = "Message From " + currMessage.getAddress() + "\n"
                          + ((TextMessage) currMessage).getPayloadText();
        } else if (mess instanceof BinaryMessage) {
            currMessage = (BinaryMessage) mess;
            message     = "Message From " + currMessage.getAddress() + "\n "
                          + new String(((BinaryMessage) currMessage).getPayloadData());
        }
        System.out.println(message);
        // Emulator.getEmulator().getLogStream().println(jdField_a_of_type_JavaLangString);
        // c.a(new aU(this, null));
    }
    public void setBlockIncomeMessage(boolean value) {
        this.blockIncommingMessage = value;
    }
    private void createWindow() {
        setTitle("Call/Sms Console");
        setSize(new Dimension(240, 120));
        instance   = this;
        callPanel  = new JPanel();
        callLabel  = new JLabel("Call");
        callNumber = new IntegerField(10);
        callButton = new JButton();
        callButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (callNumber.getText() != null) {
                    doCall(callNumber.getText());
                }
            }
        });
        callButton.setText("Call");
//      disConnect = new JButton();
//      disConnect.setText("Disconnect");
//      disConnect.addActionListener(new ActionListener() {
//
//          public void actionPerformed(ActionEvent e) {
//              //MobiEmulator.mobiEmulatorInstance.getFocusOwner().setVisible(false);
//              //ignore.doClick();
//              pane.setVisible(false);
//          }
//      });
        callPanel.setLayout(new FlowLayout());
        callPanel.setBorder(new TitledBorder("CALL"));
        callPanel.add(callLabel);
        callPanel.add(callNumber);
        callPanel.add(callButton);
        setLayout(new BorderLayout());
        add(callPanel, BorderLayout.NORTH);
        ignore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });
        smsPanel = new JPanel();
        smsPanel.setBorder(new TitledBorder("Sms Console"));
        smsTextArea = new JTextArea();
        smsClear    = new JButton();
        smsClear.setText("Clear");
        smsClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                smsTextArea.setText("");
            }
        });
        smsSend = new JButton();
        smsSend.setText("Send");
        smsSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (smsTextArea.getText() != null) {
                    String address = System.getProperty("wireless.messaging.sms.smsc");
                    if (address.startsWith("+")) {
                        address.substring(1);
                    }
                    String                content = smsTextArea.getText();
                    MessageConnectionImpl msgConn = new MessageConnectionImpl(address);
                    TextMessage           tm      =
                        (TextMessage) msgConn.newMessage(MessageConnectionImpl.TEXT_MESSAGE, address);
                    tm.setPayloadText(content);
                    try {
                        msgConn.send(tm);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    JOptionPane.showInternalMessageDialog(MobiEmulator.mobiEmulatorInstance.getContentPane(), content,
                            "SMS FROM " + address, JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        smsPanel.setLayout(new BorderLayout());
        smsPanel.add(smsTextArea, BorderLayout.NORTH);
        smsPanel.add(smsClear, BorderLayout.WEST);
        smsPanel.add(smsSend, BorderLayout.EAST);
        add(smsPanel, BorderLayout.SOUTH);
        pack();
    }
    private void disconnect() {
        MobiEmulator.mobiEmulatorInstance.resumeMenuItem.doClick();
    }
    private void doCall(String text) {
        MobiEmulator.mobiEmulatorInstance.suspendMenuitem.doClick();
        String options[] = { "Accept", "Ignore" };
        System.out.println("Incomming call from " + text);
        int result = JOptionPane.showInternalOptionDialog(MobiEmulator.mobiEmulatorInstance.getContentPane(),
                         "Call From" + text, "Call", JOptionPane.YES_NO_OPTION,
                         JOptionPane.INFORMATION_MESSAGE, null, options, "Accept");
        if (result == 0) {
            // Accepted
            int selected = JOptionPane.showInternalOptionDialog(MobiEmulator.mobiEmulatorInstance.getContentPane(), "On Call " + text,
                               "Running Call", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                               new String[]{ "Disconnect" }, "Disconnect");
            if (selected == 0) {
                MobiEmulator.mobiEmulatorInstance.resumeMenuItem.doClick();
            }
        } else {
            System.out.println("Call Ignored");
            // not accepted ignored
            disconnect();
        }
    }
}


