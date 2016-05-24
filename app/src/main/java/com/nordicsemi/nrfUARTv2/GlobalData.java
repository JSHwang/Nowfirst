package com.nordicsemi.nrfUARTv2;

/**
 * Created by 김승현 on 2016-05-24.
 */
public class GlobalData {
    private static boolean silence = false;

    public static void switchSilence() {silence = !silence;}
    public static void setSilence(boolean b) {silence = b;}
    public static void setStringSilence(String s) {if (s.equals("T")) silence = true; else silence = false;}
    public static boolean getSilence() {return silence;}
    public static String getStringSilence() {if (silence) return "T"; else return "F";}
}
