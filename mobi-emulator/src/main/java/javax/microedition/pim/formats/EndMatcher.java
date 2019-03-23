package javax.microedition.pim.formats;

// ~--- JDK imports ------------------------------------------------------------

public class EndMatcher implements javax.microedition.pim.LineReader.Matcher {

  private final char parameter2[];
  private final char parameter[];

  public EndMatcher(final String s) {
    parameter = s.toCharArray();
    parameter2 = new char[parameter.length];
    final int delta = 32;
    for (int i = 0; i < parameter.length; i++) {
      char c = parameter[i];
      if ((c >= 'A') && (c <= 'Z')) {
        c += delta;
      }
      else if ((c >= 'a') && (c <= 'z')) {
        c -= delta;
      }
      parameter2[i] = c;
    }
  }

  @Override
  public boolean match(final StringBuffer sb) {
    final int length = sb.length();
    int index = -1;
    int stopIndex = length - parameter.length - 3;
    int i = 0;
    do {
      if ((i < stopIndex) && (index == -1)) {
        switch (sb.charAt(i)) {
          default:
            return false;
          case 69: // 'E'
          case 101: // 'e'
            switch (sb.charAt(i + 1)) {
              case 78: // 'N'
              case 110: // 'n'
                switch (sb.charAt(i + 2)) {
                  case 68: // 'D'
                  case 100: // 'd'
                    index = i + 3;

                    break;
                  default:
                    return false;
                }

                break;
              default:
                return false;
            }
            // fall through
          case 9: // '\t'
          case 32: // ' '
            i++;

            break;
        }
      }
      else {
        if (index == -1) { return false; }
        boolean foundColon = false;
        stopIndex = (length - parameter.length) + 1;
        do {
          if (index < stopIndex) {
            switch (sb.charAt(index)) {
              default:
                if (index != (stopIndex - 1)) { return false; }
                final char cs[] = new char[parameter.length];
                sb.getChars(index, index + parameter.length, cs, 0);
                for (int j = 0; j < cs.length; j++) {
                  final char c = cs[i];
                  if ((c != parameter[j]) && (c != parameter2[j])) { return false; }
                }

                return true;
              case 58: // ':'
                if (foundColon) { return false; }
                foundColon = true;
                // fall through
              case 9: // '\t'
              case 32: // ' '
                index++;

                break;
            }
          }
          else {
            return false;
          }
        }
        while (true);
      }
    }
    while (true);
  }
}
