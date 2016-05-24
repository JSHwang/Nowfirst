package com.nordicsemi.nrfUARTv2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends FragmentActivity {

    static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myEbook/";

//    private List<String> mFileNames = new ArrayList<String>();
    ViewPager mFileListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        mFileListView = (ViewPager) findViewById(R.id.file_listview);
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.setItems();
        mFileListView.setAdapter(adapter);

//
//        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(FileListActivity.this, FileReadActivity.class);
//                intent.putExtra("path", path + mFileNames.get(i));
//                startActivity(intent);
//            }
//        });
//
//
//        this.updateFileList();

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<String> mFileNames = new ArrayList<String>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void setItems(){
            File files = new File(path);
            for(File file : files.listFiles())
            {
                mFileNames.add(file.getName());
            }
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.create(mFileNames.get(position), 30);
        }

        @Override
        public int getCount() {
            return mFileNames.size();
        }

    }

//
//    public void updateFileList()
//    {
//
//        File files = new File(path);
//        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.file_list_item, mFileNames);
//
//        for(File file : files.listFiles())
//        {
//            mFileNames.add(file.getName());
//        }
//
//        mFileListView.setAdapter(fileList);
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
