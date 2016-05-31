package com.dfrobot.angelo.blunobasicdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;

public class FileReadActivity extends Activity {

    static final int PREV = -1;
    static final int INIT = 0;
    static final int NEXT = 1;

    String path;

    static Braille braille;
    public static FilePointer fp;
    private static ListView messageListView;
    private static ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_read);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");


        messageListView = (ListView) findViewById(R.id.listMessage);
        listAdapter = new ArrayAdapter<String>(this, R.layout.message_detail);
        messageListView.setAdapter(listAdapter);
        messageListView.setDivider(null);



        try {
            fp = new FilePointer(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        braille = new Braille();

    }

    @Override
    protected void onDestroy (){
        super.onDestroy();
        try {
            fp.closeFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void myOnClick(View v) throws UnsupportedEncodingException {
        String org = "";
        String str = "";

        switch (v.getId()) {
            case R.id.next:
                if((str = braille.nextBraille()) == null){
                    try {
                        org = fp.readFile(NEXT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    str = braille.toBraille(org);
                }else{
                    fp.setNextNumRead();
                }
                break;

            case R.id.prev:
                if((str = braille.prevBraille())==null){
                    try {
                        org = fp.readFile(PREV);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    str = braille.toBraille(org);
                }else{
                    fp.setPrevNumRead();
                }
                break;
            case R.id.init:
                fp.initFilePointer();
                if (!(braille.isListEmpty())) {
                    braille.setCurrentIndexToZero();
                    str = braille.nextBraille();
                    fp.setInitNumRead();
                } else {
                    try {
                        org = fp.readFile(NEXT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    str = braille.toBraille(org);
                }
                break;

        }
        str = sendText(str);
        Log.d("FileReadActivity", org);

        Log.d("FileReadActivity", str);
        Log.d("FileReadActivity", fp.getNowStr());
    }

    public static void setFilePointer(int sign){
        String org = "";
        String str = "";

        switch (sign){
            case INIT:
                fp.initFilePointer();
                braille.setCurrentIndexToZero();
                sign = NEXT;
                try {
                    org = fp.readFile(sign);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                str = braille.toBraille(org);
                break;
            case PREV:
                if((str = braille.prevBraille())==null){
                    try {
                        org = fp.readFile(sign);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    str = braille.toBraille(org);
                }else{
                    fp.setPrevNumRead();
                }
                break;
            case NEXT:
                if((str = braille.nextBraille()) == null){
                    try {
                        org = fp.readFile(sign);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    str = braille.toBraille(org);
                }else{
                    fp.setNextNumRead();
                }
                break;
        }

        str = sendText(str);
        Log.d("FileReadActivity", sign + org);

        Log.d("FileReadActivity", str);
        Log.d("FileReadActivity", fp.getNowStr());
    }

    private static String sendText(String str){
        byte[] value;


        try {
//            str = braille.toBraille(origin);

            //send data to service
            value = str.getBytes("UTF-8");
            MainActivity.serialSend(value);

//            //Update the log with time stamp
            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
            listAdapter.add("[" + currentDateTimeString + "] TX: " + " " + str);
            messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return str;

    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_file_read, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
