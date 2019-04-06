package javax.microedition.lcdui;

import java.util.ArrayList;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class StringUtils {

  public static ArrayList wrapToLines(final String text, final Font f, final int maxWidth) {
    final ArrayList lines = new ArrayList();
    final boolean paragraphFormat = false;
    if (text == null) { return lines; }
    if (f.stringWidth(text) < maxWidth) {
      lines.add(text);
      return lines;
    }
    else {
      final char chars[] = text.toCharArray();
      final int len = chars.length;
      int count = 0;
      int charWidth = 0;
      int curLinePosStart = 0;
      while (count < len) {
        if (((charWidth += f.charWidth(chars[count])) > (maxWidth - 4)) || (count == (len - 1))) {
          // wrap to next line
          if (count == (len - 1)) {
            count++;
          }
          final String line = new String(chars, curLinePosStart, count - curLinePosStart);
          if (paragraphFormat) {
            final int lastSpacePosition = line.lastIndexOf(" ");
            final String l = new String(chars, curLinePosStart, (lastSpacePosition != -1) ? lastSpacePosition + 1 : (count == (len - 1)) ? (count - curLinePosStart) + 1 : count - curLinePosStart);
            lines.add(l);
            curLinePosStart = (lastSpacePosition != -1) ? lastSpacePosition + 1 : count;
          }
          else {
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
