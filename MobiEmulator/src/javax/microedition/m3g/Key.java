package javax.microedition.m3g;

class Key
{
  private int key;

  public Key(int key)
  {
    this.key = key;
  }

  public void setKey(int key)
  {
    this.key = key;
  }

  public int hashCode()
  {
    return this.key;
  }

  public boolean equals(Object obj)
  {
      return (obj instanceof Key) && this.key == ((Key) obj).key;
  }

  public String toString()
  {
    return Integer.toString(this.key);
  }
}