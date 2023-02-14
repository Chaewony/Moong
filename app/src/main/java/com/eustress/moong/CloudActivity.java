package com.eustress.moong;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CloudActivity extends AppCompatActivity {
    private TextView uid_text;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    Button btnRevoke, btnLogout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        
        uid_text = (TextView) findViewById(R.id.textViewUID);
        mAuth = FirebaseAuth.getInstance();

        btnLogout = (Button)findViewById(R.id.btn_logout);
        btnRevoke = (Button)findViewById(R.id.btn_revoke);

        //로그인한 유저 정보 가져오기
        final FirebaseUser user = mAuth.getCurrentUser();
        //uid_text.setText(databaseReference.child("Users").child(user.getUid()).child("birth").toString());
        readUser(user.getUid());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            //로그아웃 버튼 눌렀을때 실행되는 코드
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            //탈퇴하기 버튼 눌렀을때 실행되는 코드
            public void onClick(View v) {
                mAuth.getCurrentUser().delete();
            }
        });
    }

    private void readUser(String userId) {
        //데이터 읽기
        databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                uid_text.setText("이름: " + user.getName() + " 생일: " + user.getBirth());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { //참조에 액세스 할 수 없을 때 호출
                Toast.makeText(getApplicationContext(),"데이터를 가져오는데 실패했습니다" , Toast.LENGTH_LONG).show();
                uid_text.setText("정보가 없습니다");
            }
        });
    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }


}

