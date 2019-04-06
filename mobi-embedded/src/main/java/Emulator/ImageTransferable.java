package Emulator;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ImageTransferable implements Transferable {

  // the Image object which will be housed by the ImageSelection
  private final Image image;

  public ImageTransferable(final Image image) {
    this.image = image;
  }

  // Returns the supported flavors of our implementation
  @Override
  public DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[] {
        DataFlavor.imageFlavor
    };
  }

  // Returns true if flavor is supported
  @Override
  public boolean isDataFlavorSupported(final DataFlavor flavor) {
    return DataFlavor.imageFlavor.equals(flavor);
  }

  // Returns Image object housed by Transferable object
  @Override
  public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if (!DataFlavor.imageFlavor.equals(flavor)) { throw new UnsupportedFlavorException(flavor); }
    // else return the payload
    return image;
  }

}
