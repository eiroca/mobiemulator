package javax.wireless.messaging;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface MessageListener {

  public abstract void notifyIncomingMessage(MessageConnection paramMessageConnection);

}
