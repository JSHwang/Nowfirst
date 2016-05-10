package com.nordicsemi.nrfUARTv2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

public class FileListActivity extends Activity {


    static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myEbook/";


    private List<String> mFileNames = new ArrayList<String>();
    ListView mFileListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        mFileListView = (ListView) findViewById(R.id.file_listview);

        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FileListActivity.this, FileReadActivity.class);
                intent.putExtra("path", path + mFileNames.get(i));
                startActivity(intent);
            }
        });


        this.updateFileList();

    }


    public void updateFileList()
    {

        File files = new File(path);
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.file_list_item, mFileNames);

        for(File file : files.listFiles())
        {
            mFileNames.add(file.getName());
        }

        mFileListView.setAdapter(fileList);

    }


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
