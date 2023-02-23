package com.eustress.moong;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.tabs.TabLayout;

public class DiaryActivity extends FragmentActivity {
    TabLayout tabs;

    EmotionCloudFragment fragment1;
    CloudDiaryFragment fragment2;
    CloudTodayFragment fragment3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_cloud);
        setContentView(R.layout.activity_diary);

        fragment1 = new EmotionCloudFragment();
        fragment2 = new CloudDiaryFragment();
        fragment3 = new CloudTodayFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("감정 구름"));
        tabs.addTab(tabs.newTab().setText("구름 일기장"));
        tabs.addTab(tabs.newTab().setText("오늘의 구름"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0)
                    selected = fragment1;
                else if(position == 1)
                    selected = fragment2;
                else if(position == 2)
                    selected = fragment3;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
