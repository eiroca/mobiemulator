package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Ticker {

  public static String TickerString = null;

  public Ticker(final String str) {
    Ticker.TickerString = str;
  }

  public void setString(final String str) {
    if (str == null) { throw new NullPointerException(); }
    Ticker.TickerString = str;
  }

  public String getString() {
    return Ticker.TickerString;
  }

  public void TickerImpl() {
  }

}
