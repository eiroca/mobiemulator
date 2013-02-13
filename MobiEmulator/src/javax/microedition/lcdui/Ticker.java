/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Ticker {
    public static String TickerString = null;
    public Ticker(String str) {
        TickerString = str;
    }

    public void setString(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        TickerString = str;
    }
    public String getString() {
        return TickerString;
    }
    public void TickerImpl() {}
}
