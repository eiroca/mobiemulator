/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.wireless.messaging;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface MessageListener {
    public abstract void notifyIncomingMessage(MessageConnection paramMessageConnection);
}
