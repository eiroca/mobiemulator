package javax.microedition.m3g;

class Key {

  private int key;

  public Key(final int key) {
    this.key = key;
  }

  public void setKey(final int key) {
    this.key = key;
  }

  @Override
  public int hashCode() {
    return key;
  }

  @Override
  public boolean equals(final Object obj) {
    return (obj instanceof Key) && (key == ((Key)obj).key);
  }

  @Override
  public String toString() {
    return Integer.toString(key);
  }
}
