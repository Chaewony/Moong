package com.eustress.moong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.annotations.Nullable;

public class FragmentDiary extends Fragment {
    public static FragmentDiary newInstance() {
        FragmentDiary diaryFragment= new FragmentDiary();
        Bundle bundle = new Bundle();
        return diaryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        return view;
    }
}
