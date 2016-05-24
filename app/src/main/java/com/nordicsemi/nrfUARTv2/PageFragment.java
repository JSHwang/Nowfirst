package com.nordicsemi.nrfUARTv2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import mehdi.sakout.fancybuttons.FancyButton;

public class PageFragment extends Fragment {

    static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myEbook/";

    private String mTitle;
    private int mProgress;
    private FancyButton mButton;
    private String mColor;

    public static PageFragment create(String title, int progress, String color) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putString("Title", title);
        args.putInt("Progress", progress);
        args.putString("Color", color);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("Title");
        mProgress = getArguments().getInt("Progress");
        mColor = getArguments().getString("Color");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.file_list_item, container, false);
        ((FancyButton) rootView.findViewById(R.id.title)).setText(mTitle);
        ((RoundCornerProgressBar) rootView.findViewById(R.id.progress)).setProgress(mProgress);

        mButton = ((FancyButton) rootView.findViewById(R.id.title));
        mButton.setText(mTitle.replace(".txt", ""));
        mButton.setBackgroundColor(Color.parseColor(mColor));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FileReadActivity.class);
                intent.putExtra("path", path + mTitle);
                startActivity(intent);
            }
        });
        return rootView;
    }


}