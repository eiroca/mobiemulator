/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.midlet;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class AutoStartPermission {
    public AutoStartPermission() {}

    public boolean implies(java.security.Permission permission) {
        return true;
    }
}
