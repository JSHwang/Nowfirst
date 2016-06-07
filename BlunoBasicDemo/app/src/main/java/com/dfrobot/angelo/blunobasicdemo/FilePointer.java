package com.dfrobot.angelo.blunobasicdemo;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FilePointer {
//경로의 파일 열기, 닫기
//정해진 갯수 만큼 파일 읽기
//정해진 갯수 만큼 위치 이동
//현재 문자열 반환

    private RandomAccessFile fp;
    private static long eof = 0;
    private static long now = 0;
    public static String nowStr = "";

    public static int NUM_READ = 6;


    //경로의 파일 열기
    //path는 절대 경로, 파일 이름을 제외한
    FilePointer(String path) throws IOException {
        now = eof = 0;
        nowStr = "";
//        inputFile = new File(path, fileName);
        fp = new RandomAccessFile(path,"r");
        eof = fp.length();
    }


    //현재 위치는 now, now에서 fb가 +면 오른쪽 -면 왼쪽으로 읽어야함
    String readFile(int fb) throws IOException {
        //+면 현재 위치에서 현재 위치 + NUM_READ(or eof
        //-면 현재 위치 - 2*NUM_READ에서 현재 위치-1(or eof)
        if(fb  < 0)      now -= 2*NUM_READ;

        //만약 현재 위치가 0보다 작으면 now = 0
        //만약 현재 위치가 eof보다 크면 return
        if(now < 0)     now = 0;
        if(now >= eof-1)  return nowStr;

        StringBuilder total = new StringBuilder();

        fp.seek(now);
        for(int i = 0; i < NUM_READ && now+i != eof; i++){
            total.append((char)fp.read());
        }
        now += NUM_READ;

        nowStr = total.toString();

        return nowStr;
    }


    void setPrevNumRead(){
        now -= NUM_READ;
        if(now <= 0)     now = NUM_READ;
    }

    void setInitNumRead(){
        now = NUM_READ;
    }


    void setNextNumRead(){
        now += NUM_READ;
        if(now >= eof-1)  now = eof-1;
    }

    public static int getNumRead() {
        return NUM_READ;
    }

    public static void setNumRead(int numRead) {
        NUM_READ = numRead;
    }

    public String getNowStr() {
        return nowStr;
    }

    public static long getNow() {
        return now;
    }

    public void initFilePointer(){
        now = 0;
        nowStr = "";
    }

    public void closeFilePointer() throws IOException {
        fp.close();
    }

}
