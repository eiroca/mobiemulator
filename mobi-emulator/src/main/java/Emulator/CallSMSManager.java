package Emulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Vector;
// ~--- JDK imports ------------------------------------------------------------
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageConnectionImpl;
import javax.wireless.messaging.TextMessage;
import javax.wireless.messaging.TextMessageImpl;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class CallSMSManager extends JFrame {

  /**
   *
   */
  private static final long serialVersionUID = 210875039957763233L;
  private boolean blockIncommingMessage = false;
  private String message = null;
  private final Vector smsBulk = new Vector();
  JButton ignore = new JButton("Ignore");
  JButton accept = new JButton("Accept");
  private JButton callButton;
  private JLabel callLabel;
  private IntegerField callNumber;
  private JPanel callPanel;
  private JButton disConnect;
  private CallSMSManager instance;
  JOptionPane pane;
  private JButton smsClear;
  private JPanel smsPanel;
  private JButton smsSend;
  private JTextArea smsTextArea;

  public CallSMSManager() {
    createWindow();
  }

  public final Message receive(final String address) throws IOException, InterruptedIOException {
    if (smsBulk.size() > 0) {
      final String str = (String)smsBulk.firstElement();
      smsBulk.removeElementAt(0);
      final TextMessageImpl textMessage = new TextMessageImpl(address);
      textMessage.setPayloadText(str);

      return textMessage;
    }

    return null;
  }

  public final void send(final Message mess, final String paramString) throws IOException, InterruptedIOException {
    if (blockIncommingMessage) { throw new InterruptedIOException(); }
    Message currMessage;
    if (mess instanceof TextMessage) {
      currMessage = mess;
      message = "Message From " + currMessage.getAddress() + "\n"
          + ((TextMessage)currMessage).getPayloadText();
    }
    else if (mess instanceof BinaryMessage) {
      currMessage = mess;
      message = "Message From " + currMessage.getAddress() + "\n "
          + new String(((BinaryMessage)currMessage).getPayloadData());
    }
    System.out.println(message);
    // Emulator.getEmulator().getLogStream().println(jdField_a_of_type_JavaLangString);
    // c.a(new aU(this, null));
  }

  public void setBlockIncomeMessage(final boolean value) {
    blockIncommingMessage = value;
  }

  private void createWindow() {
    setTitle("Call/Sms Console");
    setSize(new Dimension(240, 120));
    instance = this;
    callPanel = new JPanel();
    callLabel = new JLabel("Call");
    callNumber = new IntegerField(10);
    callButton = new JButton();
    callButton.addActionListener(e -> {
      if (callNumber.getText() != null) {
        doCall(callNumber.getText());
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
    ignore.addActionListener(e -> disconnect());
    smsPanel = new JPanel();
    smsPanel.setBorder(new TitledBorder("Sms Console"));
    smsTextArea = new JTextArea();
    smsClear = new JButton();
    smsClear.setText("Clear");
    smsClear.addActionListener(e -> smsTextArea.setText(""));
    smsSend = new JButton();
    smsSend.setText("Send");
    smsSend.addActionListener(e -> {
      if (smsTextArea.getText() != null) {
        final String address = System.getProperty("wireless.messaging.sms.smsc");
        if (address.startsWith("+")) {
          address.substring(1);
        }
        final String content = smsTextArea.getText();
        final MessageConnectionImpl msgConn = new MessageConnectionImpl(address);
        final TextMessage tm = (TextMessage)msgConn.newMessage(MessageConnection.TEXT_MESSAGE, address);
        tm.setPayloadText(content);
        try {
          msgConn.send(tm);
        }
        catch (final IOException ex) {
          ex.printStackTrace();
        }
        JOptionPane.showInternalMessageDialog(MobiEmulator.mobiEmulatorInstance.getContentPane(), content,
            "SMS FROM " + address, JOptionPane.PLAIN_MESSAGE);
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

  private void doCall(final String text) {
    MobiEmulator.mobiEmulatorInstance.suspendMenuitem.doClick();
    final String options[] = {
        "Accept", "Ignore"
    };
    System.out.println("Incomming call from " + text);
    final int result = JOptionPane.showInternalOptionDialog(MobiEmulator.mobiEmulatorInstance.getContentPane(),
        "Call From" + text, "Call", JOptionPane.YES_NO_OPTION,
        JOptionPane.INFORMATION_MESSAGE, null, options, "Accept");
    if (result == 0) {
      // Accepted
      final int selected = JOptionPane.showInternalOptionDialog(MobiEmulator.mobiEmulatorInstance.getContentPane(), "On Call " + text,
          "Running Call", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
          new String[] {
              "Disconnect"
          }, "Disconnect");
      if (selected == 0) {
        MobiEmulator.mobiEmulatorInstance.resumeMenuItem.doClick();
      }
    }
    else {
      System.out.println("Call Ignored");
      // not accepted ignored
      disconnect();
    }
  }

}
