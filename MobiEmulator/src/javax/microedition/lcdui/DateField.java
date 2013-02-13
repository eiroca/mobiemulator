/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- JDK imports ------------------------------------------------------------

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class DateField extends Item {
    public static final int DATE        = 1;
    public static final int DATE_TIME   = 3;
    public static final int TIME        = 2;
    private Date            date        = new java.util.Date();
    private int             lineSpacing = 4;
    private DateFormat      dateFormat;
    private String          label;
    private int             mode;
    private String          str;
    private TimeZone        timeZone;
    public DateField(String label, int mode) {
        this(label, mode, null);
    }

    public DateField(String label, int mode, TimeZone timeZone) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
        this.label = label;
        if ((mode == DATE) || (mode == TIME) || (mode == DATE_TIME)) {
            this.mode = mode;
        } else {
            throw new IllegalArgumentException();
        }
        this.timeZone = timeZone;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setInputMode(int mode) {
        if ((mode == DATE) || (mode == TIME) || (mode == DATE_TIME)) {
            this.mode = mode;
        } else {
            throw new IllegalArgumentException();
        }
    }
    public int getInputMode() {
        return this.mode;
    }
    @Override
    public void paint(Graphics g) {
        // paint date as text
        int y      = yPos;
        int x      = 2;
        int anchor = Graphics.TOP | Graphics.LEFT;
        if (mode == DATE) {
            dateFormat = new SimpleDateFormat("HH:mm:ss");
        }
        if (mode == TIME) {
            dateFormat = new SimpleDateFormat("dd MMM yyyy");
        }
        if (mode == DATE_TIME) {
            dateFormat = new SimpleDateFormat("HH:mm:ss - dd MMM yyyy");
        }
        // dateFormat.setTimeZone(timeZone);
        str = dateFormat.format(date);
        int maxWidth = Math.max(Displayable.boldFont.stringWidth((label != null) ? label
                : ""), Displayable.font.stringWidth((str != null) ? str
                : ""));
        if (isSelected) {
            g.setColor(0);
            g.drawRect(x, y, maxWidth + 2, (((label != null) ? (Displayable.boldFontHeight + lineSpacing)
                    : 0) + fontHeight + lineSpacing + 2));
            y += 4;
        }
        if (label != null) {
            g.setColor(0);
            g.setFont(Displayable.boldFont);
            g.drawString(label, x + 2, y, anchor);
            y += (Displayable.boldFontHeight) + lineSpacing;
        }
        g.setColor(0);
        g.setFont(font);
        g.drawString(str, x + 2, y, anchor);
        y          += (fontHeight + lineSpacing);
        itemHeight = y - yPos;
    }
    @Override
    public void doLayout() {
        int y = yPos;
        if (label != null) {
            y += (Displayable.boldFontHeight + lineSpacing);
        }
        y          += (fontHeight + lineSpacing);
        itemHeight = y - yPos;
    }
    @Override
    public void handleKey(int keyCode) {
        switch (keyCode) {
        case Displayable.KEY_UP_ARROW :
        case Displayable.KEY_NUM2 :
            handledKey = false;    // true for consumed by this item it won't move up / down to next item

            break;
        case Displayable.KEY_DOWN_ARROW :
        case Displayable.KEY_NUM8 :
            handledKey = false;

            break;
        }
    }
}
