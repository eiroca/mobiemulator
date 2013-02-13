/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.sprintpcs.util;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Muglet {
    static Muglet MubletObj;

    public Muglet() {
        MubletObj = this;
    }

    public static Muglet getMuglet() {
        return MubletObj;
    }

    public java.lang.String getMediaType() {
        return null;
    }

    public java.lang.String getURI() {
        return null;
    }

    public java.lang.String getReferringURI() {
        return null;
    }
}
