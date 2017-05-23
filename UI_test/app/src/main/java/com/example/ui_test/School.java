package com.example.ui_test;

import android.app.ActivityGroup;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by Administrator on 2017-05-20.
 */

public class School extends ActivityGroup {
    TabHost tabHost;
    private Drawable image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_info);
        tabHost = (TabHost)findViewById(R.id.tabhost1);
        tabHost.setup(getLocalActivityManager());
        TabHost.TabSpec tab1 = tabHost.newTabSpec("A").setContent(R.id.tab1).setIndicator("진학");
        tabHost.addTab(tab1);//1번탭 생성
        TabHost.TabSpec tab2 = tabHost.newTabSpec("B").setContent(R.id.tab2).setIndicator("복지");
        tabHost.addTab(tab2);//2번탭 생성
        TabHost.TabSpec tab3 = tabHost.newTabSpec("C").setContent(R.id.tab3).setIndicator("안전");
        tabHost.addTab(tab3);//3번탭 생성


    }
}
