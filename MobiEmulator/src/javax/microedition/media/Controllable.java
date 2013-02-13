/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.media;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface Controllable {
    public javax.microedition.media.Control getControl(String controlType);
    public javax.microedition.media.Control[] getControls();
}
