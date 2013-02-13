/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.media;

//~--- non-JDK imports --------------------------------------------------------

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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class PlayerImplementation implements Player, MetaEventListener {
    public String                    ContentType          = null;
    private int                      END_OF_TRACK_MESSAGE = 47;
    public String                    FileURL              = null;
    int                              currentState         = 0;
    int                              playcount            = 0;
    private String                   playerStatus         = "UNREALIZED";
    javax.microedition.media.Control volume;
    javax.microedition.media.Control tone                 = new ToneControlImplementation();
    javax.microedition.media.Control ctrls[]              = { volume, tone };
    public long                      MediaPosition;
    private double                   Soundduration;
    public AudioInputStream          ain;
    SourceDataLine                   auline;
    private Clip                     clip;    // clip for wav file
    private ByteArrayInputStream     currentSound;
    public InputStream               filestream;
    private boolean                  isMidiFile;
    private Synthesizer              midiSynthesizer;
    Sequence                         mySeq;
    private PlayerImplementation     playerInstance;
    private PlayerListener           playerlistener;
    private Sequencer                sequencer;
    private TimeBaseImpl             tbase;
    FloatControl                     volumeControl;
    PlayerImplementation() {}

    public PlayerImplementation(String fileurl) throws InvalidMidiDataException {
        FileURL = fileurl;
        try {
            filestream = MidletUtils.getInstance().getMidletListener().getResourceStream(FileURL);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        volume         = new VolumeControlImplementation(this);
        playerInstance = this;
        createPlayer(filestream, (getMediaFormat(filestream)) ? "audio/midi"
                : "audio/x-wav");

    }

    public PlayerImplementation(InputStream is, String type) {
        volume         = new VolumeControlImplementation(this);
        playerInstance = this;
        createPlayer(is, type);
    }

    public boolean getMediaFormat(InputStream audioStream) {
        try {
            MidiSystem.getMidiFileFormat(filestream);
            isMidiFile = true;
        } catch (IOException ex) {
            System.out.println("Unknown Format");
        } catch (InvalidMidiDataException e) {
            System.out.println("invalid middata exception ");
            isMidiFile = false;
        }

        return isMidiFile;
    }
    public void createPlayer(InputStream is, String type) {
        ContentType = type;
        filestream  = is;
        isMidiFile  = getMediaFormat(is);
        byte byteArray[] = null;
        try {
            byteArray = new byte[is.available()];
            filestream.read(byteArray);
        } catch (IOException ex) {
            Logger.getLogger(PlayerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("midi file length " + byteArray.length);
        if (byteArray.length <= 0) {
            throw new NullPointerException("Player InputStream null");
        }
        currentSound = new ByteArrayInputStream(byteArray);
        if (isMidiFile) {
            try {
                sequencer = MidiSystem.getSequencer();    // midi is a file of
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

            } catch (MidiUnavailableException ex) {
                new Exception("File Not Found");
            } catch (InvalidMidiDataException e) {
                new Exception("Unsupported Format");
            } catch (IOException e) {
                new IOException("IOException");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                ain = AudioSystem.getAudioInputStream(currentSound);
                AudioFormat format = ain.getFormat();
                auline = null;
                DataLine.Info info = new DataLine.Info((javax.sound.sampled.Clip.class), format);
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(ain);
                /*
                 * Line.Info linfo = new Line.Info(Clip.class); Line line =
                 * AudioSystem.getLine(linfo); clip =AudioSystem.getClip(); ain
                 * = AudioSystem.getAudioInputStream(filestream);
                 * clip.open(ain);
                 */
            } catch (Exception e) {
                System.out.println("Exception during getting Stream");
                e.printStackTrace();
            }
        }
        currentState = UNREALIZED;
        playerStatus = "UNREALIZED";
        updateListenerStatus(playerStatus, null);
    }
    public void realize() {
        currentState = REALIZED;
        playerStatus = "REALIZED";
        updateListenerStatus(playerStatus, null);
    }
    public void prefetch() {
        if (currentSound instanceof ByteArrayInputStream) {
            if (isMidiFile) {
                try {
                    sequencer.open();
                    sequencer.setSequence(mySeq);
                    System.out.println("*******player prefetched midi");
                } catch (MidiUnavailableException ex) {
                    System.out.println("midi unavailable");
                } catch (InvalidMidiDataException ex) {
                    System.out.println("Invalid Midi");
                }
            } else {
                try {
                    clip.open(ain);
                    System.out.println("*******player prefetched wav");
                } catch (LineUnavailableException ex) {
                    System.out.println("Wav file Unavailable");
                } catch (IOException ex) {
                    System.out.println("Invalid Wav or Wav not found");
                }
            }
            currentState = PREFETCHED;
            playerStatus = "PREFETCHED";
            updateListenerStatus(playerStatus, null);
        }
    }
    public void reset() {
        stop();
        if (isMidiFile) {
            sequencer.setTickPosition(0);
        } else {
            clip.setMicrosecondPosition(0);
        }
        MediaPosition = 0;
    }
    public void setVolume(int currentVolumeLevel) {
        if (isMidiFile) {
            MidiChannel[] channels = midiSynthesizer.getChannels();
            for (MidiChannel channel : channels) {
                channel.controlChange(7, (int) ((currentVolumeLevel / 100.0F) * 127.0F));    // 0 -
            }
            // 127
            System.out.println("currentVolumeLevel is " + currentVolumeLevel);
            System.out.println("length of sound is " + getDuration());
        } else if (clip instanceof javax.sound.sampled.Clip) {
            try {
                Clip tempClip = this.clip;
                volumeControl = (FloatControl) tempClip.getControl(FloatControl.Type.MASTER_GAIN);
                float volumevalue = ((currentVolumeLevel <= 0) ? volumeControl.getMinimum()
                        : ((currentVolumeLevel >= 100) ? volumeControl.getMaximum()
                        : (currentVolumeLevel * 20.0F / 100.0F - 20.0F)));
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("length of sound is " + getDuration());
        }
    }
    public void setMute(boolean isMute) {
        if (isMute) {
            if (isMidiFile) {
                MidiChannel[] channels = midiSynthesizer.getChannels();
                for (MidiChannel channel : channels) {
                    channel.setMute(isMute);
                }
            } else if (clip instanceof javax.sound.sampled.Clip) {
                Clip tempClip = this.clip;
                volumeControl = (FloatControl) tempClip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(volumeControl.getMinimum());
            }
        }
    }
    public void start() {
        try {
            if ((currentState == REALIZED) || (currentState == PREFETCHED)) {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    clip.open(ain);
                    clip.start();
                }
            } else if (currentState == UNREALIZED) {
                realize();
                prefetch();
                while (playcount > 1) {
                    if (isMidiFile) {
                        sequencer.start();
                    } else {
                        clip.start();
                    }
                    --playcount;
                }
            } else {
                new Exception("Player null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = (isMidiFile) ? "midi"
                : "wav";
        System.out.println("*******player started " + str);
        currentState = STARTED;
        playerStatus = "STARTED";
        updateListenerStatus(playerStatus, 0L);
    }
    public void stop() {
        if (isMidiFile) {
            if ((sequencer != null) && sequencer.isOpen()) {
                sequencer.stop();
                sequencer.setMicrosecondPosition(0);
            }
        } else {
            if (clip.isOpen() && (clip != null)) {
                clip.stop();
            }
        }
        currentState = PREFETCHED;
        playerStatus = "STOPPED";
        updateListenerStatus(playerStatus, null);
    }
    public void deallocate() {
        if (isMidiFile) {
            mySeq = null;
        } else {
            clip.drain();
        }
    }
    public void close() {
        if (isMidiFile) {
            sequencer.close();
        } else {
            clip.close();
        }
        currentState = CLOSED;
        playerStatus = "CLOSED";
        updateListenerStatus(playerStatus, null);
    }
    public int getState() {
        return currentState;
    }
    public long getDuration() {
        if (isMidiFile) {
            Soundduration = sequencer.getMicrosecondLength() / 1000000.0;
        } else {
            Soundduration = (clip.getMicrosecondLength() / 1000);    // wav
        }
        // duration

        return (long) Soundduration;
    }
    public String getContentType() {
        if (ContentType != null) {
            return ContentType;
        } else {
            return "Null type";
        }
    }
    public long setMediaTime(long Time) {
        if (Time <= 0) {
            MediaPosition = 0;
        } else if (Time > sequencer.getMicrosecondLength()) {
            MediaPosition = sequencer.getMicrosecondLength();
        } else {
            MediaPosition = Time;
        }
        if (isMidiFile) {
            sequencer.setTickPosition(MediaPosition);
        } else {
            clip.setMicrosecondPosition(MediaPosition * 1000);
        }

        return MediaPosition;
    }
    public long getMediaTime() {
        return getDuration();
    }
    public void setLoopCount(int Count) {
        if (getState() == Player.STARTED) {
            return;
        }
        playcount = Count;
        if (isMidiFile) {
            sequencer.setLoopCount(playcount);
        }
    }
    public void addPlayerListener(PlayerListener playerListener) throws IllegalStateException {
        playerlistener = playerListener;
    }
    public void removePlayerListener(PlayerListener playerListener) throws IllegalStateException {
        playerlistener = playerListener;
    }
    public void updateListenerStatus(String playerStatus, Object data) {
        if (playerlistener != null) {
            playerlistener.playerUpdate(playerInstance, playerStatus, data);
        }
    }
    public javax.microedition.media.Control getControl(String controlType) {
        if (controlType.equals("VolumeControl")) {
            return volume;
        }
        if (controlType.equals("ToneControl")) {
            return tone;
        }

        return null;
    }
    public javax.microedition.media.Control[] getControls() {
        return ctrls;
    }
    public void setTimeBase(TimeBase master) throws MediaException {
        tbase = (TimeBaseImpl) master;
    }
    public TimeBase getTimeBase() {
        return tbase;
    }
    public void meta(MetaMessage meta) {
        if (meta.getType() == END_OF_TRACK_MESSAGE) {
            updateListenerStatus("END_OF_MEDIA", null);
            setMediaTime(0L);
        }
    }
}
