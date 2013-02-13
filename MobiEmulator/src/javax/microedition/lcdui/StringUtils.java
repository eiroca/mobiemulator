/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class StringUtils {
    public static ArrayList wrapToLines(String text, Font f, int maxWidth) {
        ArrayList lines           = new ArrayList();
        boolean   paragraphFormat = false;
        if (text == null) {
            return lines;
        }
        if (f.stringWidth(text) < maxWidth) {
            lines.add(text);

            return lines;
        } else {
            char chars[]         = text.toCharArray();
            int  len             = chars.length;
            int  count           = 0;
            int  charWidth       = 0;
            int  curLinePosStart = 0;
            while (count < len) {
                if ((charWidth += f.charWidth(chars[count])) > (maxWidth - 4) || (count == len - 1))    // wrap to next line
                {
                    if (count == len - 1) {
                        count++;
                    }
                    String line = new String(chars, curLinePosStart, count - curLinePosStart);
                    if (paragraphFormat) {
                        int    lastSpacePosition = line.lastIndexOf(" ");
                        String l                 = new String(chars, curLinePosStart,
                                                       (lastSpacePosition != -1) ? lastSpacePosition + 1
                                : (count == len - 1) ? count - curLinePosStart + 1
                                                     : count - curLinePosStart);
                        lines.add(l);
                        curLinePosStart = (lastSpacePosition != -1) ? lastSpacePosition + 1
                                : count;
                    } else {
                        lines.add(line);
                        curLinePosStart = count;
                    }
                    charWidth = 0;
                }
                count++;
            }

            return lines;
        }
    }
}
