/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
//
//This class encapsulates the information for mapping J2SE keypresses into
//J2ME keypresses and game actions
//
public class KeyActionMapping {
    int     j2se_key_code;
    String  key_name;
    int     midp_game_action;
    int     midp_key_code;
    int     orig_midp_key_code;
    boolean pressed;
    public KeyActionMapping(int j2se_key_code, int midp_game_action, int midp_key_code, String key_name) {
        this.j2se_key_code      = j2se_key_code;
        this.midp_game_action   = midp_game_action;
        this.midp_key_code      = midp_key_code;
        this.key_name           = key_name;
        this.orig_midp_key_code = midp_key_code;
    }
}
