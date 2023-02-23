package com.eustress.moong;

import static android.content.ContentValues.TAG;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CloudActivity extends AppCompatActivity {
    private TextView uid_text;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    Button btnRevoke, btnLogout, btnBackup, btnRename;
    EditText edtRename;
    private long backKeyPressedTime=0;
    GoogleSignInClient googleSignInClient;
    String userUid;

    private FloatingActionButton fabMain;
    private FloatingActionButton fabCamera;
    private FloatingActionButton fabDiary;

    // 플로팅버튼 상태
    private boolean fabMain_status = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        
        uid_text = (TextView) findViewById(R.id.textViewUID);
        mAuth = FirebaseAuth.getInstance();
        btnLogout = (Button)findViewById(R.id.btn_logout);
        btnRevoke = (Button)findViewById(R.id.btn_revoke);
        btnBackup = (Button)findViewById(R.id.btn_backup);
        btnRename = (Button)findViewById(R.id.btn_name);
        edtRename = (EditText)findViewById(R.id.edt_name);

        fabMain = findViewById(R.id.fabMain);
        fabCamera = findViewById(R.id.fabCamera);
        fabDiary = findViewById(R.id.fabDiary);

        // 메인플로팅 버튼 클릭
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();

            }
        });
        // 카메라 플로팅 버튼 클릭
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CloudActivity.this, "카메라 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        // 다이어리 플로팅 버튼 클릭
        fabDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CloudActivity.this, "다이어리 버튼 클릭", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CloudActivity.this, DiaryActivity.class);
                startActivity(intent);
            }
        });

        //로그인한 유저 정보 가져오기
        final FirebaseUser user = mAuth.getCurrentUser();
        //uid_text.setText(databaseReference.child("Users").child(user.getUid()).child("birth").toString());
        readUser(user.getUid());
        userUid = user.getUid();
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

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            //백업 버튼 눌렀을때 실행되는 코드
            public void onClick(View v) {
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("743777353010-fu63lvc3touji7lr9k8alvdmmbijdmm5.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                // Initialize sign in client
                googleSignInClient = GoogleSignIn.getClient(CloudActivity.this, googleSignInOptions);

                Intent intent = googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent, 100);
            }

        });

        btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            //이름 변경 버튼 눌렀을때 실행되는 코드
            public void onClick(View v) {
                renameUser(user.getUid(),edtRename.getText().toString());
            }

        });
    }

    private void renameUser(String userId, String newName) {
        //데이터 읽기
        databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                user.setName(newName);
                databaseReference.child("Users").child(userId).child("name").setValue(newName);


                uid_text.setText("이름: " + user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { //참조에 액세스 할 수 없을 때 호출
                Toast.makeText(getApplicationContext(),"이름 변경에 실패했습니다" , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void readUser(String userId) {
        //데이터 읽기
        databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                uid_text.setText("이름: " + user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { //참조에 액세스 할 수 없을 때 호출
                Toast.makeText(getApplicationContext(),"데이터를 가져오는데 실패했습니다" , Toast.LENGTH_LONG).show();
                uid_text.setText("정보가 없습니다");
            }
        });
    }

    private void registerEmail(String userId, String email) {
        //데이터 읽기
        databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                user.setEmail(email);
                databaseReference.child("Users").child(userId).child("email").setValue(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { //참조에 액세스 할 수 없을 때 호출
                Toast.makeText(getApplicationContext(),"이메일을 가져오는데 실패했습니다" , Toast.LENGTH_LONG).show();
                uid_text.setText("정보가 없습니다");
            }
        });
    }


    private void signOut() {
        mAuth.signOut();
        //화면 바꾸기
        Intent intent = new Intent(CloudActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();

        //화면 바꾸기
        Toast.makeText(CloudActivity.this, "회원 탈퇴 완료", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CloudActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //구글 계정과 비회원 계정 연동(비회원 계정을 구글 계정으로 인증받기)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @com.google.firebase.database.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
                // When google sign in successful initialize string
                String s = "Google sign in successful";
                // Display Toast
                //displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        mAuth.getCurrentUser().linkWithCredential(authCredential)
                                .addOnCompleteListener(CloudActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "linkWithCredential:success");
                                            FirebaseUser user = task.getResult().getUser();
                                            //updateUI(user);

                                            //데이터베이스에 이메일 정보 넣기
                                            registerEmail(user.getUid(),user.getEmail());
                                        } else {
                                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                                            Toast.makeText(CloudActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            //updateUI(null);
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onBackPressed()
    {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            Toast.makeText(CloudActivity.this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).show();
            return;
        }

        //2초 이내 뒤로가기 버튼을 한번 더 클릭 시 앱 종료...를 원하는데 왜 첫화면으로 돌아가지?
        else{
            finish();
        }
    }

    // 플로팅 액션 버튼 클릭시 애니메이션 효과
    public void toggleFab() {
        if(fabMain_status) {
            // 플로팅 액션 버튼 닫기
            // 애니메이션 추가
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabCamera, "translationY", 0f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabDiary, "translationY", 0f);
            fe_animation.start();
            // 메인 플로팅 이미지 변경
            fabMain.setImageResource(R.drawable.ic_home);

        }else {
            // 플로팅 액션 버튼 열기
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabCamera, "translationY", -200f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabDiary, "translationY", -400f);
            fe_animation.start();
            // 메인 플로팅 이미지 변경
            fabMain.setImageResource(R.drawable.ic_home_pressed);
        }
        // 플로팅 버튼 상태 변경
        fabMain_status = !fabMain_status;
    }

}

