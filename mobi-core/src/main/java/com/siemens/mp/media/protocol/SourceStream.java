package com.siemens.mp.media.protocol;

import java.io.IOException;
import com.siemens.mp.media.Controllable;

public abstract interface SourceStream
    extends Controllable {

  public static final int NOT_SEEKABLE = 0;
  public static final int SEEKABLE_TO_START = 1;
  public static final int RANDOM_ACCESSIBLE = 2;

  public abstract ContentDescriptor getContentDescriptor();

  public abstract long getContentLength();

  public abstract int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException;

  public abstract int getTransferSize();

  public abstract long seek(long paramLong)
      throws IOException;

  public abstract long tell();

  public abstract int getSeekType();
}

/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\media\protocol\SourceStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
