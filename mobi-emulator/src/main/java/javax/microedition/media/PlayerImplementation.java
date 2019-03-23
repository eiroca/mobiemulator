package javax.microedition.media;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
// ~--- non-JDK imports --------------------------------------------------------
import javax.microedition.media.control.ToneControlImplementation;
import javax.microedition.media.control.VolumeControlImplementation;
import javax.microedition.midlet.MidletUtils;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class PlayerImplementation implements Player, MetaEventListener {

  public String ContentType = null;
  private final int END_OF_TRACK_MESSAGE = 47;
  public String FileURL = null;
  int currentState = 0;
  int playcount = 0;
  private String playerStatus = "UNREALIZED";
  javax.microedition.media.Control volume;
  javax.microedition.media.Control tone = new ToneControlImplementation();
  javax.microedition.media.Control ctrls[] = {
      volume, tone
  };
  public long MediaPosition;
  private double Soundduration;
  public AudioInputStream ain;
  SourceDataLine auline;
  private Clip clip; // clip for wav file
  private ByteArrayInputStream currentSound;
  public InputStream filestream;
  private boolean isMidiFile;
  private Synthesizer midiSynthesizer;
  Sequence mySeq;
  private PlayerImplementation playerInstance;
  private PlayerListener playerlistener;
  private Sequencer sequencer;
  private TimeBaseImpl tbase;
  FloatControl volumeControl;

  PlayerImplementation() {
  }

  public PlayerImplementation(final String fileurl) throws InvalidMidiDataException {
    FileURL = fileurl;
    try {
      filestream = MidletUtils.getInstance().getMidletListener().getResourceStream(FileURL);
    }
    catch (final IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    volume = new VolumeControlImplementation(this);
    playerInstance = this;
    createPlayer(filestream, (getMediaFormat(filestream)) ? "audio/midi"
        : "audio/x-wav");

  }

  public PlayerImplementation(final InputStream is, final String type) {
    volume = new VolumeControlImplementation(this);
    playerInstance = this;
    createPlayer(is, type);
  }

  public boolean getMediaFormat(final InputStream audioStream) {
    try {
      MidiSystem.getMidiFileFormat(filestream);
      isMidiFile = true;
    }
    catch (final IOException ex) {
      System.out.println("Unknown Format");
    }
    catch (final InvalidMidiDataException e) {
      System.out.println("invalid middata exception ");
      isMidiFile = false;
    }

    return isMidiFile;
  }

  public void createPlayer(final InputStream is, final String type) {
    ContentType = type;
    filestream = is;
    isMidiFile = getMediaFormat(is);
    byte byteArray[] = null;
    try {
      byteArray = new byte[is.available()];
      filestream.read(byteArray);
    }
    catch (final IOException ex) {
      Logger.getLogger(PlayerImplementation.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.out.println("midi file length " + byteArray.length);
    if (byteArray.length <= 0) { throw new NullPointerException("Player InputStream null"); }
    currentSound = new ByteArrayInputStream(byteArray);
    if (isMidiFile) {
      try {
        sequencer = MidiSystem.getSequencer(); // midi is a file of
        // various sequences so
        // using sequencer
        sequencer.open();
        sequencer.addMetaEventListener(this);
        mySeq = MidiSystem.getSequence(currentSound);
        sequencer.setSequence(mySeq);
        // if(sequencer instanceof Synthesizer)
        {
          midiSynthesizer = MidiSystem.getSynthesizer();
          midiSynthesizer.open();
          sequencer.getTransmitter().setReceiver(midiSynthesizer.getReceiver());
        }
        // else
        // {
        // System.out.println(" in else");
        // //sequencer.start();
        // midiSynthesizer=((Synthesizer)sequencer);
        // }

      }
      catch (final MidiUnavailableException ex) {
        new Exception("File Not Found");
      }
      catch (final InvalidMidiDataException e) {
        new Exception("Unsupported Format");
      }
      catch (final IOException e) {
        new IOException("IOException");
      }
      catch (final Exception e) {
        e.printStackTrace();
      }
    }
    else {
      try {
        ain = AudioSystem.getAudioInputStream(currentSound);
        final AudioFormat format = ain.getFormat();
        auline = null;
        final DataLine.Info info = new DataLine.Info((javax.sound.sampled.Clip.class), format);
        clip = (Clip)AudioSystem.getLine(info);
        clip.open(ain);
        /*
         * Line.Info linfo = new Line.Info(Clip.class); Line line =
         * AudioSystem.getLine(linfo); clip =AudioSystem.getClip(); ain
         * = AudioSystem.getAudioInputStream(filestream);
         * clip.open(ain);
         */
      }
      catch (final Exception e) {
        System.out.println("Exception during getting Stream");
        e.printStackTrace();
      }
    }
    currentState = Player.UNREALIZED;
    playerStatus = "UNREALIZED";
    updateListenerStatus(playerStatus, null);
  }

  @Override
  public void realize() {
    currentState = Player.REALIZED;
    playerStatus = "REALIZED";
    updateListenerStatus(playerStatus, null);
  }

  @Override
  public void prefetch() {
    if (currentSound instanceof ByteArrayInputStream) {
      if (isMidiFile) {
        try {
          sequencer.open();
          sequencer.setSequence(mySeq);
          System.out.println("*******player prefetched midi");
        }
        catch (final MidiUnavailableException ex) {
          System.out.println("midi unavailable");
        }
        catch (final InvalidMidiDataException ex) {
          System.out.println("Invalid Midi");
        }
      }
      else {
        try {
          clip.open(ain);
          System.out.println("*******player prefetched wav");
        }
        catch (final LineUnavailableException ex) {
          System.out.println("Wav file Unavailable");
        }
        catch (final IOException ex) {
          System.out.println("Invalid Wav or Wav not found");
        }
      }
      currentState = Player.PREFETCHED;
      playerStatus = "PREFETCHED";
      updateListenerStatus(playerStatus, null);
    }
  }

  public void reset() {
    stop();
    if (isMidiFile) {
      sequencer.setTickPosition(0);
    }
    else {
      clip.setMicrosecondPosition(0);
    }
    MediaPosition = 0;
  }

  public void setVolume(final int currentVolumeLevel) {
    if (isMidiFile) {
      final MidiChannel[] channels = midiSynthesizer.getChannels();
      for (final MidiChannel channel : channels) {
        channel.controlChange(7, (int)((currentVolumeLevel / 100.0F) * 127.0F)); // 0 -
      }
      // 127
      System.out.println("currentVolumeLevel is " + currentVolumeLevel);
      System.out.println("length of sound is " + getDuration());
    }
    else if (clip instanceof javax.sound.sampled.Clip) {
      try {
        final Clip tempClip = clip;
        volumeControl = (FloatControl)tempClip.getControl(FloatControl.Type.MASTER_GAIN);
        final float volumevalue = ((currentVolumeLevel <= 0) ? volumeControl.getMinimum()
            : ((currentVolumeLevel >= 100) ? volumeControl.getMaximum()
                : (((currentVolumeLevel * 20.0F) / 100.0F) - 20.0F)));
        // float value=(float)currentVolumeLevel/REALIZED.0f; //(
        // volumeControl.getMinimum() + (currentVolumeLevel *
        // (volumeControl.getMaximum()-volumeControl.getMinimum())) /
        // 1000.0f);
        // float value = (float)(Math.log((currentVolumeLevel == 0.0D) ?
        // 0.0001D : currentVolumeLevel) / Math.log(10.0D) * 20.0D);
        // float value= (currentVolumeLevel * (max-min) / 1000.0f);
        volumeControl.setValue(volumevalue);
        updateListenerStatus("VOLUME_CHANGED", null);
        System.out.println("volume value is " + volumeControl.getMinimum() + " max is "
            + volumeControl.getMaximum());
      }
      catch (final Exception e) {
        e.printStackTrace();
      }
      System.out.println("length of sound is " + getDuration());
    }
  }

  public void setMute(final boolean isMute) {
    if (isMute) {
      if (isMidiFile) {
        final MidiChannel[] channels = midiSynthesizer.getChannels();
        for (final MidiChannel channel : channels) {
          channel.setMute(isMute);
        }
      }
      else if (clip instanceof javax.sound.sampled.Clip) {
        final Clip tempClip = clip;
        volumeControl = (FloatControl)tempClip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(volumeControl.getMinimum());
      }
    }
  }

  @Override
  public void start() {
    try {
      if ((currentState == Player.REALIZED) || (currentState == Player.PREFETCHED)) {
        if (isMidiFile) {
          try {
            sequencer.open();
            if (mySeq == null) {
              System.out.println(">> Sound Data Unavailable");

              return;
            }
            sequencer.setSequence(mySeq);
            sequencer.start();
            System.out.println("player started midi playing");
          }
          catch (final Exception e) {
            e.printStackTrace();
          }
        }
        else {
          clip.open(ain);
          clip.start();
        }
      }
      else if (currentState == Player.UNREALIZED) {
        realize();
        prefetch();
        while (playcount > 1) {
          if (isMidiFile) {
            sequencer.start();
          }
          else {
            clip.start();
          }
          --playcount;
        }
      }
      else {
        new Exception("Player null");
      }
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
    final String str = (isMidiFile) ? "midi"
        : "wav";
    System.out.println("*******player started " + str);
    currentState = Player.STARTED;
    playerStatus = "STARTED";
    updateListenerStatus(playerStatus, 0L);
  }

  @Override
  public void stop() {
    if (isMidiFile) {
      if ((sequencer != null) && sequencer.isOpen()) {
        sequencer.stop();
        sequencer.setMicrosecondPosition(0);
      }
    }
    else {
      if (clip.isOpen() && (clip != null)) {
        clip.stop();
      }
    }
    currentState = Player.PREFETCHED;
    playerStatus = "STOPPED";
    updateListenerStatus(playerStatus, null);
  }

  @Override
  public void deallocate() {
    if (isMidiFile) {
      mySeq = null;
    }
    else {
      clip.drain();
    }
  }

  @Override
  public void close() {
    if (isMidiFile) {
      sequencer.close();
    }
    else {
      clip.close();
    }
    currentState = Player.CLOSED;
    playerStatus = "CLOSED";
    updateListenerStatus(playerStatus, null);
  }

  @Override
  public int getState() {
    return currentState;
  }

  @Override
  public long getDuration() {
    if (isMidiFile) {
      Soundduration = sequencer.getMicrosecondLength() / 1000000.0;
    }
    else {
      Soundduration = (clip.getMicrosecondLength() / 1000); // wav
    }
    // duration

    return (long)Soundduration;
  }

  @Override
  public String getContentType() {
    if (ContentType != null) {
      return ContentType;
    }
    else {
      return "Null type";
    }
  }

  @Override
  public long setMediaTime(final long Time) {
    if (Time <= 0) {
      MediaPosition = 0;
    }
    else if (Time > sequencer.getMicrosecondLength()) {
      MediaPosition = sequencer.getMicrosecondLength();
    }
    else {
      MediaPosition = Time;
    }
    if (isMidiFile) {
      sequencer.setTickPosition(MediaPosition);
    }
    else {
      clip.setMicrosecondPosition(MediaPosition * 1000);
    }

    return MediaPosition;
  }

  @Override
  public long getMediaTime() {
    return getDuration();
  }

  @Override
  public void setLoopCount(final int Count) {
    if (getState() == Player.STARTED) { return; }
    playcount = Count;
    if (isMidiFile) {
      sequencer.setLoopCount(playcount);
    }
  }

  @Override
  public void addPlayerListener(final PlayerListener playerListener) throws IllegalStateException {
    playerlistener = playerListener;
  }

  @Override
  public void removePlayerListener(final PlayerListener playerListener) throws IllegalStateException {
    playerlistener = playerListener;
  }

  public void updateListenerStatus(final String playerStatus, final Object data) {
    if (playerlistener != null) {
      playerlistener.playerUpdate(playerInstance, playerStatus, data);
    }
  }

  @Override
  public javax.microedition.media.Control getControl(final String controlType) {
    if (controlType.equals("VolumeControl")) { return volume; }
    if (controlType.equals("ToneControl")) { return tone; }

    return null;
  }

  @Override
  public javax.microedition.media.Control[] getControls() {
    return ctrls;
  }

  @Override
  public void setTimeBase(final TimeBase master) throws MediaException {
    tbase = (TimeBaseImpl)master;
  }

  @Override
  public TimeBase getTimeBase() {
    return tbase;
  }

  @Override
  public void meta(final MetaMessage meta) {
    if (meta.getType() == END_OF_TRACK_MESSAGE) {
      updateListenerStatus("END_OF_MEDIA", null);
      setMediaTime(0L);
    }
  }

}
