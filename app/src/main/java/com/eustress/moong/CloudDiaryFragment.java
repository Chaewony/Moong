package com.eustress.moong;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.annotations.Nullable;

public class CloudDiaryFragment extends Fragment {
    @Nullable
    GradientDrawable drawable;
    ImageView imageView;
    Button btnRed;
    Button btnGreen;
    Button btnBlue;

    private Paint maskingPaint = new Paint();
    private Drawable mask = drawable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_clouddiary, container, false);
        //drawable = (GradientDrawable) getResources().getDrawable(R.drawable.mesh_gradients_sample);
        CloudGradientDrawable cloudGradientDrawable = new CloudGradientDrawable(Color.RED,Color.GREEN,Color.BLUE,1,Color.BLACK,00);
        imageView = rootView.findViewById(R.id.imageButton);
        btnRed = rootView.findViewById(R.id.btn_color_red);
        btnGreen = rootView.findViewById(R.id.btn_color_green);
        btnBlue = rootView.findViewById(R.id.btn_color_blue);

        imageView.setImageDrawable(cloudGradientDrawable);

        // Red button
        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                cloudGradientDrawable.SetStartColor(Color.RED);
                imageView.setImageDrawable(cloudGradientDrawable);
                Toast.makeText(getActivity(), "빨간버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        // Green button
        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                cloudGradientDrawable.SetStartColor(Color.GREEN);
                imageView.setImageDrawable(cloudGradientDrawable);
                Toast.makeText(getActivity(), "초록버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        // Blue button
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                cloudGradientDrawable.SetStartColor(Color.BLUE);
                imageView.setImageDrawable(cloudGradientDrawable);
                Toast.makeText(getActivity(), "파란버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
