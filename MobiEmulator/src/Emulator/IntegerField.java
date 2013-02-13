package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * This class is a <CODE>TextField</CODE> that only allows integer values to be
 * entered into it.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class IntegerField extends JTextField implements FocusListener {
    public boolean       isFocusGained   = false;
    public RoundedBorder textFieldBorder = new RoundedBorder();
    Border               empty           = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);

    /**
     *     Default ctor.
     */
    public IntegerField() {
        super();
        //setOpaque(false);
        // Add an empty border around us to compensate for
        // the rounded corners.
        //setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
//      setBorder(new RoundedBorder());
        addFocusListener(this);
    }

    /**
     * Ctor specifying the field width.
     *
     * @param cols
     *            Number of columns.
     */
    public IntegerField(int cols) {
        super(cols);
        //setOpaque(false);
        //setBorder(empty);
        // setBorder(textFieldBorder);
        addFocusListener(this);
    }

    /**
     * Retrieve the contents of this field as an <TT>int</TT>.
     *
     * @return the contents of this field as an <TT>int</TT>.
     */
    public int getInt() {
        final String text = getText();
        if ((text == null) || (text.length() == 0)) {
            return 0;
        }

        return Integer.parseInt(text);
    }

    /**
     * Set the contents of this field to the passed <TT>int</TT>.
     *
     * @param value
     *            The new value for this field.
     */
    public void setInt(int value) {
        setText(String.valueOf(value));
    }

    /**
     * Create a new document model for this control that only accepts integral
     * values.
     *
     * @return The new document model.
     */
    @Override
    protected Document createDefaultModel() {
        return new IntegerDocument();
    }
//    public void paintComponent(Graphics g) {
//        int width  = getWidth() - getInsets().left - getInsets().right;
//        int height = getHeight() - getInsets().top - getInsets().bottom;
//        // Paint a rounded rectangle in the background.
//        g.setColor(getBackground());
//        g.fillRoundRect(getInsets().left + 1, getInsets().top + 1, width - 1, height - 1, 8, 8);
//        super.paintComponent(g);
//    }
    public void updateTextFieldBorder(boolean isFocued) {
        if (!isFocued) {
            setBorder(empty);
        } else {
            setBorder(textFieldBorder);
        }
    }
    public void focusGained(FocusEvent e) {
        isFocusGained = true;
        updateTextFieldBorder(isFocusGained);
    }
    public void focusLost(FocusEvent e) {
        isFocusGained = false;
        updateTextFieldBorder(isFocusGained);
    }

    /**
     * This document only allows integral values to be added to it.
     */
    static class IntegerDocument extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str != null) {
                if (str.startsWith("-")) {
                    super.insertString(offs, str, a);

                    return;
                }
                try {
                    Integer.decode(str);
                    super.insertString(offs, str, a);
                } catch (NumberFormatException ex) {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
    }

    class RoundedBorder extends AbstractBorder {
        Color backgroundcolor = null;
        public RoundedBorder() {}

        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = 18;

            return insets;
        }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            int w = width;
            int h = height;
            if (backgroundcolor == null) {
                backgroundcolor = c.getBackground();
            }
            g.translate(x, y);
            // if(isFocusGained)
            {
                g.setColor(Color.BLUE);
                g.drawRoundRect(0, 0, w + 1, h + 1, 8, 8);
                g.drawRoundRect(-1, -1, w, h, 8, 8);
            }
//          else{
//          g.setColor( backgroundcolor );  
//          g.drawRoundRect( 0, 0, w-2, h-2, 8, 8 );  
//          }
            g.translate(-x, -y);
        }
        public boolean isBorderOpaque() {
            return true;
        }
    }
}
