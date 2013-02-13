package javax.microedition.m3g;

import java.lang.ref.WeakReference;
import java.util.Enumeration;
import java.util.Hashtable;

class Engine
{
  static final int ANIMATIONCONTROLLER = 0;
  static final int ANIMATIONTRACK = 1;
  static final int APPEARANCE = 2;
  static final int BACKGROUND = 3;
  static final int CAMERA = 4;
  static final int COMPOSITINGMODE = 5;
  static final int FOG = 6;
  static final int GRAPHICS3D = 7;
  static final int GROUP = 8;
  static final int IMAGE2D = 9;
  static final int KEYFRAMESEQUENCE = 10;
  static final int LIGHT = 11;
  static final int LOADER = 12;
  static final int MATERIAL = 13;
  static final int MESH = 14;
  static final int MORPHINGMESH = 15;
  static final int PLASMAIMAGE = 16;
  static final int POLYGONMODE = 17;
  static final int RAYINTERSECTION = 18;
  static final int SKINNEDMESH = 19;
  static final int SPRITE3D = 20;
  static final int STAGESET = 21;
  static final int TEXTURE2D = 22;
  static final int TRANSFORM = 23;
  static final int TRIANGLESTRIPARRAY = 24;
  static final int VERTEXARRAY = 25;
  static final int VERTEXBUFFER = 26;
  static final int WORLD = 27;
  static final int UNKNOWN = 28;
  static final int OBJECT3D_FID = 0;
  static final int GRAPHICS3D_FID = 1;
  static final int LOADER_FID = 2;
  static final int RAYINTERSECTION_FID = 3;
  static final int TRANSFORM_FID = 4;
  static Hashtable peerTable = new Hashtable();

  static Object3D[] XOT = new Object3D[0];

  static int XOTlength = 0;

  private static Object3D[] tmpXOT = null;

  private static boolean cleanPeerTable = false;

  private static Key lookup = new Key(0);

  static native int getVersionMajor();

  static native int getVersionMinor();

  static native int getRevisionMajor();

  static native int getRevisionMinor();

  static native int getBranchNumber();

  static native void releaseHandle(int paramInt);

  static native int getHandleType(int paramInt);

  static native int getHandleSize(int paramInt);
  
  static Object instantiateJavaPeer(int handle)
    {
        boolean success;
        if(handle == 0)
        {
            return null;
        }
        success = false;
        Object3D o3d;
        Object peer = getJavaPeer(handle);
        if(peer != null)
        {
           o3d = ((Object3D) (peer));
            if(!success)
            {
                releaseHandle(handle);
            }
             return o3d;
        }
        
        switch(getHandleType(handle))
        {
        case 0: // '\0'
            peer = new AnimationController(handle);
            break;

        case 1: // '\001'
            peer = new AnimationTrack(handle);
            break;

        case 2: // '\002'
            peer = new Appearance(handle);
            break;

        case 3: // '\003'
            peer = new Background(handle);
            break;

        case 4: // '\004'
            peer = new Camera(handle);
            break;

        case 5: // '\005'
            peer = new CompositingMode(handle);
            break;

        case 6: // '\006'
            peer = new Fog(handle);
            break;

        case 7: // '\007'
            peer = new Graphics3D(handle);
            break;

        case 8: // '\b'
            peer = new Group(handle);
            break;

        case 9: // '\t'
        case 16: // '\020'
        case 21: // '\025'
            peer = new Image2D(handle);
            break;

        case 10: // '\n'
            peer = new KeyframeSequence(handle);
            break;

        case 11: // '\013'
            peer = new Light(handle);
            break;

        case 12: // '\f'
            peer = new Loader(handle);
            break;

        case 13: // '\r'
            peer = new Material(handle);
            break;

        case 14: // '\016'
            peer = new Mesh(handle);
            break;

        case 15: // '\017'
            peer = new MorphingMesh(handle);
            break;

        case 17: // '\021'
            peer = new PolygonMode(handle);
            break;

        case 18: // '\022'
            peer = new RayIntersection(handle);
            break;

        case 19: // '\023'
            peer = new SkinnedMesh(handle);
            break;

        case 20: // '\024'
            peer = new Sprite3D(handle);
            break;

        case 22: // '\026'
            peer = new Texture2D(handle);
            break;

        case 23: // '\027'
            peer = new Transform(handle);
            break;

        case 24: // '\030'
            peer = new TriangleStripArray(handle);
            break;

        case 25: // '\031'
            peer = new VertexArray(handle);
            break;

        case 26: // '\032'
            peer = new VertexBuffer(handle);
            break;

        case 27: // '\033'
            peer = new World(handle);
            break;

        default:
            throw new IllegalArgumentException();
        }
        if(peer instanceof Object3D)
        {
            o3d = (Object3D)peer;
            Hashtable params = null;
            int len;
            for(int index = 0; (len = o3d.getUserParameterValue(index, null)) != -1; index++)
            {
                if(params == null)
                {
                    params = new Hashtable();
                }
                Integer id = new Integer(o3d.getUserParameterID(index));
                byte buffer[] = new byte[len];
                o3d.getUserParameterValue(index, buffer);
                params.put(id, buffer);
            }

            if(params != null)
            {
                o3d.removeUserParameters();
                o3d.setUserObject(params);
            }
        }
        addJavaPeer(handle, peer);
        success = true;
        o3d = ((Object3D) (peer));
        return o3d;
        
    }

  
//  
//  
//  static Object instantiateJavaPeer(int handle)
//  {
//    if (handle == 0) {
//      return null;
//    }
//    boolean success = false;
//    Object3D o3d=null;
//    try
//    {
//      Object peer = getJavaPeer(handle);
//      
//      if (peer != null)
//      {
//        o3d = (Object3D)peer;
//        releaseHandle(handle);
//        return o3d;
//      }
//
//      switch (getHandleType(handle)) {
//      case 0:
//        peer = new AnimationController(handle); break;
//      case 1:
//        peer = new AnimationTrack(handle); break;
//      case 2:
//        peer = new Appearance(handle); break;
//      case 3:
//        peer = new Background(handle); break;
//      case 4:
//        peer = new Camera(handle); break;
//      case 5:
//        peer = new CompositingMode(handle); break;
//      case 6:
//        peer = new Fog(handle); break;
//      case 7:
//        peer = new Graphics3D(handle); break;
//      case 8:
//        peer = new Group(handle); break;
//      case 9:
//      case 16:
//      case 21:
//        peer = new Image2D(handle); break;
//      case 10:
//        peer = new KeyframeSequence(handle); break;
//      case 11:
//        peer = new Light(handle); break;
//      case 12:
//        peer = new Loader(handle); break;
//      case 13:
//        peer = new Material(handle); break;
//      case 14:
//        peer = new Mesh(handle); break;
//      case 15:
//        peer = new MorphingMesh(handle); break;
//      case 17:
//        peer = new PolygonMode(handle); break;
//      case 18:
//        peer = new RayIntersection(handle); break;
//      case 19:
//        peer = new SkinnedMesh(handle); break;
//      case 20:
//        peer = new Sprite3D(handle); break;
//      case 22:
//        peer = new Texture2D(handle); break;
//      case 23:
//        peer = new Transform(handle); break;
//      case 24:
//        peer = new TriangleStripArray(handle); break;
//      case 25:
//        peer = new VertexArray(handle); break;
//      case 26:
//        peer = new VertexBuffer(handle); break;
//      case 27:
//        peer = new World(handle); break;
//      default:
//        throw new IllegalArgumentException();
//      }
//
//      if ((peer instanceof Object3D))
//      {
//        o3d = (Object3D)peer;
//
//        Hashtable params = null;
//        int index = 0;
//        int len;
//        while ((len = ((Object3D)o3d).getUserParameterValue(index, null)) != -1)
//        {
//          if (params == null) {
//            params = new Hashtable();
//          }
//          Integer id = new Integer(((Object3D)o3d).getUserParameterID(index));
//          byte[] buffer = new byte[len];
//
//          ((Object3D)o3d).getUserParameterValue(index, buffer);
//
//          params.put(id, buffer);
//
//          index++;
//        }
//
//        if (params != null)
//        {
//          ((Object3D)o3d).removeUserParameters();
//          ((Object3D)o3d).setUserObject(params);
//        }
//      }
//
//      addJavaPeer(handle, peer);
//
//      success = true;
//
//      o3d = (Object3D) peer;
//      return o3d;
//    }
//    finally
//    {
//      if (!success) releaseHandle(handle); 
//      return o3d;
//    }
//    
//  }

  private static void cleanUpPeerTable()
  {
    Enumeration keys = peerTable.keys();

    while (keys.hasMoreElements())
    {
      Key key = (Key)keys.nextElement();
      WeakReference ref = (WeakReference)peerTable.get(key);

      if (ref.get() == null)
        peerTable.remove(key);
    }
  }

  static void addJavaPeer(int handle, Object peer)
  {
    synchronized (peerTable)
    {
      if (cleanPeerTable)
      {
        cleanUpPeerTable();
        cleanPeerTable = false;
      }
      peerTable.put(new Key(handle), new WeakReference(peer));
    }
  }

  static Object getJavaPeer(int handle)
  {
    Object entry;
    synchronized (peerTable)
    {
      if (cleanPeerTable)
      {
        cleanUpPeerTable();
        cleanPeerTable = false;
      }

      lookup.setKey(handle);

      entry = peerTable.get(lookup);

      if (entry != null) {
        entry = ((WeakReference)entry).get();
      }
    }
    return entry;
  }

  static int[] getJavaPeerArrayHandles(Object[] peers)
  {
    if (peers == null)
      return null;
    int[] handles;
    synchronized (peers)
    {
      handles = new int[peers.length];

      for (int i = 0; i < peers.length; i++)
      {
        if (peers[i] == null)
          handles[i] = 0;
        else if ((peers[i] instanceof Object3D))
          handles[i] = ((Object3D)peers[i]).swerveHandle;
        else {
          throw new IllegalArgumentException(peers[i] + "is not instanceof Object3D");
        }
      }
    }
    return handles;
  }

  static native void cacheFID(Class paramClass, int paramInt);

  static void addXOT(Object3D obj)
  {
    if ((obj != null) && ((obj.ii) || (obj.uo != null)))
    {
      synchronized (XOT)
      {
        for (int i = 0; i < XOTlength; i++) {
          if (XOT[i] == obj) {
            return;
          }
        }
        if (XOTlength == XOT.length)
        {
          tmpXOT = new Object3D[XOT.length == 0 ? 2 : XOT.length * 2];
          System.arraycopy(XOT, 0, tmpXOT, 0, XOT.length);
          XOT = tmpXOT;
          tmpXOT = null;
        }

        XOT[(XOTlength++)] = obj;
      }
    }
  }

  static void addXOT(Object3D[] obj)
  {
    if (obj != null)
    {
      for (int i = 0; i < obj.length; i++)
      {
        addXOT(obj[i]);
      }
    }
  }
}