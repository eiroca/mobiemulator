package javax.microedition.m3g;

public class World extends Group
{
  World(int handle)
  {
    super(handle);
  }



  public World()
  {
    this(create());
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != World.class);
  }

  private static native int create();

  public Camera getActiveCamera()
  {
    return (Camera)Engine.instantiateJavaPeer(getActiveCameraImpl());
  }

  private native int getActiveCameraImpl();

  public Background getBackground()
  {
    return (Background)Engine.instantiateJavaPeer(getBackgroundImpl());
  }

  private native int getBackgroundImpl();

  public void setActiveCamera(Camera ActiveCamera)
  {
    setActiveCameraImpl(ActiveCamera);
    Engine.addXOT(ActiveCamera);
  }

  private native void setActiveCameraImpl(Camera paramCamera);

  public void setBackground(Background Background)
  {
    setBackgroundImpl(Background);
    Engine.addXOT(Background);
  }

  private native void setBackgroundImpl(Background paramBackground);
}