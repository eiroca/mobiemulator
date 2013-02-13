/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.event;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.midlet.MIDletIdentity;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class EventData {
    public static final java.lang.String APPLICATION_RELAUNCH_PREFIX  = "APPLICATION_RELAUNCH: ";
    public static final java.lang.String AUDIO_MUTE                   = "AUDIO_MUTE";
    public static final java.lang.String BACKLIGHT                    = "BACKLIGHT";
    public static final int              BACKLIGHT_DIM                = 2;
    public static final int              BACKLIGHT_OFF                = 0;
    public static final int              BACKLIGHT_ON                 = 1;
    public static final java.lang.String BATTERY_CHARGING             = "BATTERY_CHARGING";
    public static final java.lang.String BATTERY_LEVEL                = "BATTERY_LEVEL";
    public static final java.lang.String BATTERY_LOW                  = "BATTERY_LOW";
    public static final java.lang.String DATA_NETWORK                 = "DATA_NETWORK";
    public static final java.lang.String EXTERNAL_POWER               = "EXTERNAL_POWER";
    public static final java.lang.String FLIGHT_MODE                  = "FLIGHT_MODE";
    public static final java.lang.String NETWORK_3GPP_CSD             = "3GPP_CSD";
    public static final java.lang.String NETWORK_3GPP_PD              = "3GPP_PD";
    public static final java.lang.String NETWORK_3GPP_PD_3G           = "3GPP_3G";
    public static final java.lang.String NETWORK_3GPP_PD_EDGE         = "3GPP_EDGE";
    public static final java.lang.String NETWORK_3GPP_PD_HSDPA        = "3GPP_HSDPA";
    public static final java.lang.String NETWORK_802DOT11             = "802.11";
    public static final java.lang.String NETWORK_802DOT16             = "802.16";
    public static final java.lang.String NETWORK_CDMA                 = "CDMA";
    public static final java.lang.String PROFILE_ACTIVATED            = "PROFILE_ACTIVATED";
    public static final java.lang.String PROFILE_GENERAL              = "GENERAL";
    public static final java.lang.String PROFILE_MEETING              = "MEETING";
    public static final java.lang.String PROFILE_OFFLINE              = "OFFLINE";
    public static final java.lang.String PROFILE_OUTDOOR              = "OUTDOOR";
    public static final java.lang.String PROFILE_PAGER                = "PAGER";
    public static final java.lang.String PROFILE_SILENT               = "SILENT";
    public static final java.lang.String PROFILE_SYSTEM1              = "SYSTEM1";
    public static final java.lang.String PROFILE_SYSTEM2              = "SYSTEM2";
    public static final java.lang.String PROFILE_SYSTEM3              = "SYSTEM3";
    public static final java.lang.String PROFILE_SYSTEM4              = "SYSTEM4";
    public static final java.lang.String PROFILE_USER1                = "USER1";
    public static final java.lang.String PROFILE_USER2                = "USER2";
    public static final java.lang.String PROFILE_USER3                = "USER3";
    public static final java.lang.String PROFILE_USER4                = "USER4";
    public static final java.lang.String SCREENSAVER_MODE             = "SCREENSAVER_MODE";
    public static final java.lang.String SCREENSAVER_MODE_ACTIVATED   = "ACTIVATED";
    public static final java.lang.String SCREENSAVER_MODE_DEACTIVATED = "DEACTIVATED";
    public static final java.lang.String SYSTEM_STATE                 = "SYSTEM_STATE";
    public static final java.lang.String SYSTEM_STATE_NORMAL          = "NORMAL";
    public static final java.lang.String SYSTEM_STATE_SHUTDOWN        = "SHUTDOWN";
    public static final java.lang.String SYSTEM_STATE_STANDBY         = "STANDBY";
    public static final java.lang.String SYSTEM_STATE_STARTUP         = "STARTUP";
    public static final java.lang.String VOICE_CALL                   = "VOICE_CALL";
    public boolean                       booleanvalue;
    public String                        event;
    public Object                        info;
    public long                          longValue;
    public String                        message;
    public String                        strValue;
    public double                        value;
    public EventData(java.lang.String event, boolean value, java.lang.String message, java.lang.Object info) {
        if (event == null) {
            throw new NullPointerException();
        }
        this.event        = event;
        this.booleanvalue = value;
        this.message      = message;
        this.info         = info;
    }

    public EventData(java.lang.String event, double value, java.lang.String message, java.lang.Object info) {
        if (event == null) {
            throw new NullPointerException();
        }
        this.event   = event;
        this.value   = value;
        this.message = message;
        this.info    = info;
    }

    public EventData(java.lang.String event, java.lang.String value, java.lang.String message, java.lang.Object info) {
        if ((event == null) || (value == null)) {
            throw new NullPointerException();
        }
        this.event    = event;
        this.strValue = value;
        this.message  = message;
        this.info     = info;
    }

    public EventData(java.lang.String event, long value, java.lang.String message, java.lang.Object info) {
        if (event == null) {
            throw new NullPointerException();
        }
        this.event     = event;
        this.longValue = value;
        this.message   = message;
        this.info      = info;
    }

    public java.lang.String getName() {
        return event;
    }
    public int hashCode() {
        return this.info.hashCode();
    }
    public boolean equals(java.lang.Object object) {
        EventData obj = (EventData) object;

        return ((obj.event.equals(this.event)) && (obj.message == this.message) && (obj.value == this.value));
    }
    public java.lang.Object getInfo() {
        return this.info;
    }
    public java.lang.String getMessage() {
        return this.message;
    }
    public float getFloat() {
        return 1.0f;
    }
    public double getDouble() {
        return 1.0;
    }
    public long getTimestamp() {
        return 0L;
    }
    public MIDletIdentity getSourceInfo() {
        return null;
    }
    public java.lang.String toString() {
        return getString();
    }
    private String getString() {
        return "";
    }
    public java.lang.Object getValue() {
        return "";
    }
    public boolean getBoolean() {
        return false;
    }
}
