package com.eustress.moong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.annotations.Nullable;

public class FragmentTheme extends Fragment {
    public FragmentTheme () {}

    public static FragmentTheme newInstance() {
        FragmentTheme themeFragment= new FragmentTheme();
        Bundle bundle = new Bundle();
        return themeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);

        return view;
    }
}
