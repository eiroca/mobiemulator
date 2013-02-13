package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyPad extends JFrame implements ActionListener, MouseListener {
    private int currentKeyPressed = -99;
    private JButton dpadDOWN;
    private JButton dpadFIRE;
    private JButton dpadLEFT;
    private JButton dpadRIGHT;
    private JButton dpadUP;
    private MobiEmulator emulinstance;
    private JButton eight;
    private JButton five;
    private JButton four;
    private JButton hash;
    private JButton nine;
    private JButton one;
    private JButton seven;
    private JButton six;
    private JButton star;
    private JButton three;
    private JButton two;
    private JButton zero;
    private JButton callButton;
    private JButton endButton;
    private JButton lskButton;
    private JButton rskButton;

    /**
     * Create the frame
     */
    public KeyPad(MobiEmulator mobiEmulatorInstance) {
        super();
        emulinstance = mobiEmulatorInstance;
        getContentPane().setLayout(null);
        setSize(238, 328);
        addMouseListener(this);
        lskButton = new JButton();
        lskButton.setActionCommand("101");
        lskButton.setBounds(7, 12, 52, 26);
        lskButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_F1);
                currentKeyPressed = KeyEvent.VK_F1;
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_F1);
                currentKeyPressed = -99;
            }
        });
        lskButton.addActionListener(this);
        getContentPane().add(lskButton);
        lskButton.setText("LSK");
        rskButton = new JButton();
        rskButton.setActionCommand("102");
        rskButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_F2);
                currentKeyPressed = KeyEvent.VK_F2;
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_F2);
                currentKeyPressed = -99;
            }
        });
        rskButton.setBounds(175, 12, 43, 26);
        rskButton.addActionListener(this);
        getContentPane().add(rskButton);
        rskButton.setText("RSK");
        callButton = new JButton();
        callButton.setActionCommand("91");
        callButton.setBounds(7, 44, 52, 26);
        callButton.addActionListener(this);
        getContentPane().add(callButton);
        callButton.setText("Call");
        endButton = new JButton();
        endButton.setActionCommand("92");
        endButton.setBounds(175, 44, 43, 26);
        endButton.addActionListener(this);
        getContentPane().add(endButton);
        endButton.setText("End");
        one = new JButton();
        one.setActionCommand("1");
        one.setText("1");
        one.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_NUMPAD1);
                currentKeyPressed = KeyEvent.VK_NUMPAD1;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_NUMPAD1);
                currentKeyPressed = -99;
            }
        });
        one.setBounds(25, 120, 52, 26);
        one.addActionListener(this);
        getContentPane().add(one);
        two = new JButton();
        two.setActionCommand("2");
        two.setText("2");
        two.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_NUMPAD2);
                currentKeyPressed = KeyEvent.VK_NUMPAD2;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_NUMPAD2);
                currentKeyPressed = -99;
            }
        });
        two.setBounds(83, 120, 52, 26);
        two.addActionListener(this);
        getContentPane().add(two);
        three = new JButton();
        three.setActionCommand("3");
        three.setText("3");
        three.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_NUMPAD3);
                currentKeyPressed = KeyEvent.VK_NUMPAD3;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_NUMPAD3);
                currentKeyPressed = -99;
            }
        });
        three.setBounds(141, 120, 52, 26);
        three.addActionListener(this);
        getContentPane().add(three);
        four = new JButton();
        four.setActionCommand("4");
        four.setText("4");
        four.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_NUMPAD4);
                currentKeyPressed = KeyEvent.VK_NUMPAD4;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_NUMPAD4);
                currentKeyPressed = -99;
            }
        });
        four.setBounds(25, 152, 52, 26);
        four.addActionListener(this);
        getContentPane().add(four);
        five = new JButton();
        five.setActionCommand("5");
        five.setText("5");
        five.setBounds(83, 152, 52, 26);
        five.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_NUMPAD5);
                currentKeyPressed = KeyEvent.VK_NUMPAD5;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_NUMPAD5);
                currentKeyPressed = -99;
            }
        });
        five.addActionListener(this);
        getContentPane().add(five);
        six = new JButton();
        six.setActionCommand("6");
        six.setText("6");
        six.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_NUMPAD6);
                currentKeyPressed = KeyEvent.VK_NUMPAD6;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_NUMPAD6);
                currentKeyPressed = -99;
            }
        });
        six.setBounds(141, 152, 52, 26);
        six.addActionListener(this);
        getContentPane().add(six);
        seven = new JButton();
        seven.setActionCommand("7");
        seven.setText("7");
        seven.setBounds(25, 184, 52, 26);
        seven.addActionListener(this);
        getContentPane().add(seven);
        eight = new JButton();
        eight.setActionCommand("8");
        eight.setText("8");
        eight.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_NUMPAD8);
                currentKeyPressed = KeyEvent.VK_NUMPAD8;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_NUMPAD8);
                currentKeyPressed = -99;
            }
        });
        eight.setBounds(83, 184, 52, 26);
        eight.addActionListener(this);
        getContentPane().add(eight);
        nine = new JButton();
        nine.setActionCommand("9");
        nine.setText("9");
        nine.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_NUMPAD9);
                currentKeyPressed = KeyEvent.VK_NUMPAD9;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_NUMPAD9);
                currentKeyPressed = -99;
            }
        });
        nine.setBounds(141, 184, 52, 26);
        nine.addActionListener(this);
        getContentPane().add(nine);
        star = new JButton();
        star.setActionCommand("11");
        star.setText("*");
        star.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_MULTIPLY);
                currentKeyPressed = KeyEvent.VK_MULTIPLY;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_MULTIPLY);
                currentKeyPressed = -99;
            }
        });
        star.setBounds(25, 216, 52, 26);
        star.addActionListener(this);
        getContentPane().add(star);
        zero = new JButton();
        zero.setActionCommand("0");
        zero.setText("0");
        zero.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_NUMPAD0);
                currentKeyPressed = KeyEvent.VK_NUMPAD0;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_NUMPAD0);
                currentKeyPressed = -99;
            }
        });
        zero.setBounds(83, 216, 52, 26);
        zero.addActionListener(this);
        getContentPane().add(zero);
        hash = new JButton();
        hash.setActionCommand("12");
        hash.setText("#");
        hash.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_DIVIDE);
                currentKeyPressed = KeyEvent.VK_DIVIDE;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_DIVIDE);
                currentKeyPressed = -99;
            }
        });
        hash.setBounds(141, 216, 52, 26);
        hash.addActionListener(this);
        getContentPane().add(hash);
        dpadUP = new JButton();
        dpadUP.setActionCommand("51");
        dpadUP.setText("Up");
        dpadUP.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_UP);
                currentKeyPressed = KeyEvent.VK_UP;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_UP);
                currentKeyPressed = -99;
            }
        });
        dpadUP.setBounds(105, 5, 20, 16);
        dpadUP.addActionListener(this);
        getContentPane().add(dpadUP);
        dpadLEFT = new JButton();
        dpadLEFT.setActionCommand("54");
        dpadLEFT.setText("Left");
        dpadLEFT.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_LEFT);
                currentKeyPressed = KeyEvent.VK_LEFT;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_LEFT);
                currentKeyPressed = -99;
            }
        });
        dpadLEFT.setBounds(70, 30, 20, 26);
        dpadLEFT.addActionListener(this);
        getContentPane().add(dpadLEFT);
        dpadRIGHT = new JButton();
        dpadRIGHT.setActionCommand("53");
        dpadRIGHT.setText("Right");
        dpadRIGHT.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_RIGHT);
                currentKeyPressed = KeyEvent.VK_RIGHT;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_RIGHT);
                currentKeyPressed = -99;
            }
        });
        dpadRIGHT.setBounds(135, 30, 20, 26);
        dpadRIGHT.addActionListener(this);
        getContentPane().add(dpadRIGHT);
        dpadDOWN = new JButton();
        dpadDOWN.setActionCommand("52");
        dpadDOWN.setText("Down");
        dpadDOWN.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_DOWN);
                currentKeyPressed = KeyEvent.VK_DOWN;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_DOWN);
                currentKeyPressed = -99;
            }
        });
        dpadDOWN.setBounds(104, 64, 20, 16);
        dpadDOWN.addActionListener(this);
        getContentPane().add(dpadDOWN);
        dpadFIRE = new JButton();
        dpadFIRE.setActionCommand("55");
        dpadFIRE.setText("Fire");
        dpadFIRE.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyPressed(KeyEvent.VK_ENTER);
                currentKeyPressed = KeyEvent.VK_ENTER;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MobiEmulator.mobiEmulatorInstance.canvasPanel.getCurrentDisplayable().invokeKeyReleased(KeyEvent.VK_ENTER);
                currentKeyPressed = -99;
            }
        });
        dpadFIRE.setBounds(100, 30, 26, 26);
        dpadFIRE.addActionListener(this);
        getContentPane().add(dpadFIRE);
        //
    }

    public void actionPerformed(ActionEvent e) {
        JButton j = (JButton) e.getSource();
        System.out.println("  pressed " + Integer.parseInt(j.getActionCommand()));
        currentKeyPressed = Integer.parseInt(j.getActionCommand());
        /*
         * switch(Integer.parseInt(j.getActionCommand())) { case 0:
         * mobiEmulatorInstance.
         * canvasPanel.midp_canvas.invokeKeyPressed(KeyEvent.VK_NUMPAD0); break;
         * case 1:
         * mobiEmulatorInstance.canvasPanel.midp_canvas.invokeKeyPressed(KeyEvent
         * .VK_NUMPAD1); break; case 2:
         * mobiEmulatorInstance.canvasPanel.midp_canvas.invokeKeyPressed
         * (KeyEvent.VK_NUMPAD2); break; case 3:
         * mobiEmulatorInstance.canvasPanel.midp_canvas
         * .invokeKeyPressed(KeyEvent.VK_NUMPAD3); break; case 4:
         * mobiEmulatorInstance.canvasPanel
         * .midp_canvas.invokeKeyPressed(KeyEvent.VK_NUMPAD4); break; case 5:
         * mobiEmulatorInstance
         * .canvasPanel.midp_canvas.invokeKeyPressed(KeyEvent.VK_NUMPAD5);
         * break; case 6:
         * mobiEmulatorInstance.canvasPanel.midp_canvas.invokeKeyPressed(KeyEvent
         * .VK_NUMPAD6); break; case 7:
         * mobiEmulatorInstance.canvasPanel.midp_canvas.invokeKeyPressed
         * (KeyEvent.VK_NUMPAD7); break; case 8:
         * mobiEmulatorInstance.canvasPanel.midp_canvas
         * .invokeKeyPressed(KeyEvent.VK_NUMPAD8); break; case 9:
         * mobiEmulatorInstance.canvasPanel
         * .midp_canvas.invokeKeyPressed(KeyEvent.VK_NUMPAD9); break; case 11:
         * mobiEmulatorInstance
         * .canvasPanel.midp_canvas.invokeKeyPressed(KeyEvent.VK_MULTIPLY);
         * break; case 12:
         * mobiEmulatorInstance.canvasPanel.midp_canvas.invokeKeyPressed(
         * KeyEvent.VK_DIVIDE); break; case 101:
         * mobiEmulatorInstance.canvasPanel.midp_canvas
         * .invokeKeyPressed(KeyEvent.VK_F1); break; case 102:
         * mobiEmulatorInstance.canvasPanel
         * .midp_canvas.invokeKeyPressed(KeyEvent.VK_F2); break; case 91:
         * mobiEmulatorInstance
         * .canvasPanel.midp_canvas.invokeKeyPressed(KeyEvent.VK_INSERT); break;
         * case 92:
         * mobiEmulatorInstance.canvasPanel.midp_canvas.invokeKeyPressed(KeyEvent
         * .VK_END); break; case 51:
         * mobiEmulatorInstance.canvasPanel.midp_canvas.invokeKeyPressed
         * (KeyEvent.VK_UP); break; case 52:
         * mobiEmulatorInstance.canvasPanel.midp_canvas
         * .invokeKeyPressed(KeyEvent.VK_DOWN); break; case 53:
         * mobiEmulatorInstance.canvasPanel
         * .midp_canvas.invokeKeyPressed(KeyEvent.VK_RIGHT); break; case 54:
         * mobiEmulatorInstance
         * .canvasPanel.midp_canvas.invokeKeyPressed(KeyEvent.VK_LEFT); break;
         * case 55:
         * mobiEmulatorInstance.canvasPanel.midp_canvas.invokeKeyPressed(KeyEvent
         * .VK_ENTER); break;
         *
         * }
         * mobiEmulatorInstance.canvasPanel.midp_canvas.invokeKeyReleased(currentKeyPressed
         * ); currentKeyPressed=-99;
         */
    }
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("releasing keypad " + currentKeyPressed);
        MobiEmulator.mobiEmulatorInstance.canvasPanel.midp_canvas.invokeKeyReleased(currentKeyPressed);
    }
}
