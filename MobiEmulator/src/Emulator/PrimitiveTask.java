/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class PrimitiveTask extends PaintTask {
    int            anchor;
    int            arcHeight;
    int            arcWidth;
    int            endAngle;
    Font           f;
    int            height;
    private String paintString;
    private String stackTrace;
    int            startAngle;
    int            type;
    int            width;
    int            x;
    int            x1;
    int            x2;
    int            y;
    int            y1;
    int            y2;
    public PrimitiveTask(Graphics g) {
        super.getGraphicsSettings(g);
    }

    public PrimitiveTask paintLine(int x1, int y1, int x2, int y2) {
        this.x  = x1;
        this.y  = y1;
        this.x1 = x2;
        this.y1 = y2;

        return this;
    }
    public PrimitiveTask paintRect(int x, int y, int w, int h) {
        this.x      = x;
        this.y      = y;
        this.width  = w;
        this.height = h;

        return this;
    }
    public PrimitiveTask paintArc(int x, int y, int w, int h, int startangle, int endangle) {
        paintRect(x, y, w, h);
        this.startAngle = startangle;
        this.endAngle   = endangle;

        return this;
    }
    public PrimitiveTask paintRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight) {
        paintRect(x, y, w, h);
        this.arcWidth  = arcWidth;
        this.arcHeight = arcHeight;

        return this;
    }
    public PrimitiveTask paintTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        paintLine(x1, y1, x2, y2);
        this.x2 = x3;
        this.y2 = y3;

        return this;
    }
    public PrimitiveTask paintString(Font f, String str, int x, int y, int anchor) {
        this.paintString = str;
        this.f           = f;
        this.x           = x;
        this.y           = y;
        this.anchor      = anchor;

        return this;
    }
    public PrimitiveTask setType(int type, String dumpstack) {
        this.type       = type;
        this.stackTrace = dumpstack;

        return this;
    }
    public String getTypeString() {
        switch (this.type) {
        case PaintTask.DRAW_RECT :
            return "drawRect";
        case PaintTask.DRAW_LINE :
            return "drawLine";
        case PaintTask.DRAW_ROUNDRECT :
            return "drawRoundRect";
        case PaintTask.DRAW_ARC :
            return "drawArc";
        case PaintTask.FILL_RECT :
            return "fillRect";
        case PaintTask.FILL_ROUNDRECT :
            return "flllRoundRect";
        case PaintTask.FILL_ARC :
            return "fillArc";
        case PaintTask.FILL_TRIANGLE :
            return "fillTriangle";
        case PaintTask.DRAW_STRING :
            return "drawString";
        }

        return "";
    }
    public String getStackTrace() {
        return stackTrace;
    }
    @Override
    public void drawTask(Graphics g) {
        setGraphicsSettings(g);
        switch (type) {
        case PaintTask.DRAW_RECT :
            g.drawRect(x, y, width, height);

            break;
        case PaintTask.FILL_RECT :
            g.fillRect(x, y, width, height);

            break;
        case PaintTask.DRAW_ROUNDRECT :
            g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);

            break;
        case PaintTask.FILL_ROUNDRECT :
            g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);

            break;
        case PaintTask.DRAW_LINE :
            g.drawLine(x, y, x2, y2);

            break;
        case PaintTask.DRAW_ARC :
            g.drawArc(x, y, width, height, startAngle, endAngle);

            break;
        case PaintTask.FILL_ARC :
            g.fillArc(x, y, width, height, startAngle, endAngle);

            break;
        case PaintTask.FILL_TRIANGLE :
            g.fillTriangle(x, y, x1, y1, x2, y2);

            break;
        case PaintTask.DRAW_STRING :
            g.setFont(f);
            g.setColor(color);
            g.drawString(paintString, x, y, anchor);

            break;
        }
    }
    public String toString() {
        return getTypeString();
    }
}
