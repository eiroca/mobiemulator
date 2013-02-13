/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.midlet;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ActionsDeniedPermission {
    public ActionsDeniedPermission() {}

    public boolean implies(java.security.Permission permission) {
        return true;
    }
}
