package javax.wireless.messaging;

import java.util.Date;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class BinaryMessageImpl implements BinaryMessage {

  private String address;
  private byte[] data;
  private long timestamp;

  public BinaryMessageImpl(final String address) {
    this.address = address;
  }

  @Override
  public byte[] getPayloadData() {
    return data;
  }

  @Override
  public void setPayloadData(final byte[] data) {
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
    return new Date(timestamp);
  }

}
