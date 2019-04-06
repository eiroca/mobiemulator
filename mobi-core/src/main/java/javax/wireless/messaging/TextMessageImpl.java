package javax.wireless.messaging;

import java.util.Date;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class TextMessageImpl implements TextMessage {

  private String address;
  private String data;
  private long timeStamp;

  public TextMessageImpl(final String address) {
    this.address = address;
  }

  @Override
  public String getPayloadText() {
    return data;
  }

  @Override
  public void setPayloadText(final String data) {
    this.data = data;
  }

  @Override
  public String getAddress() {
    return address;
  }

  @Override
  public void setAddress(final String address) {
    this.address = address;
  }

  @Override
  public Date getTimestamp() {
    return new Date(timeStamp);
  }

}
