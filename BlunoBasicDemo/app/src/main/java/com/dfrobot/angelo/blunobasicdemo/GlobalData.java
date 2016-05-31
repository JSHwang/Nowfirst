package com.dfrobot.angelo.blunobasicdemo;

public class GlobalData {
    private static boolean silence = false;

    public static void switchSilence() {silence = !silence;}
    public static void setSilence(boolean b) {silence = b;}
    public static void setStringSilence(String s) {if (s.equals("T")) silence = true; else silence = false;}
    public static boolean getSilence() {return silence;}
    public static String getStringSilence() {if (silence) return "T"; else return "F";}
}
