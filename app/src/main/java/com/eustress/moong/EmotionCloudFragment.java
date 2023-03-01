package com.eustress.moong;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;
import com.google.firebase.database.annotations.Nullable;

public class EmotionCloudFragment extends Fragment {
    Slider discreteSlider;
    ImageView yellowCloud;
    ImageView orangeCloud;
    ImageView pinkCloud;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_emotioncloud, container, false);

        discreteSlider = rootView.findViewById(R.id.discreteSlider);
        yellowCloud = rootView.findViewById(R.id.yellowCloud);
        orangeCloud = rootView.findViewById(R.id.orangeCloud);
        pinkCloud = rootView.findViewById(R.id.pinkCloud);

        yellowCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            //이름 변경 버튼 눌렀을때 실행되는 코드
            public void onClick(View v) {
                discreteSlider.setTickActiveTintList(getResources().getColorStateList(R.color.yellow_cloud));
                discreteSlider.setTrackActiveTintList(getResources().getColorStateList(R.color.yellow_cloud));
            }

        });

        orangeCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            //이름 변경 버튼 눌렀을때 실행되는 코드
            public void onClick(View v) {
                discreteSlider.setTickActiveTintList(getResources().getColorStateList(R.color.orange_cloud));
                discreteSlider.setTrackActiveTintList(getResources().getColorStateList(R.color.orange_cloud));
            }

        });

        pinkCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            //이름 변경 버튼 눌렀을때 실행되는 코드
            public void onClick(View v) {
                discreteSlider.setTickActiveTintList(getResources().getColorStateList(R.color.pink_cloud));
                discreteSlider.setTrackActiveTintList(getResources().getColorStateList(R.color.pink_cloud));
            }

        });

        return rootView;
    }

}
