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

    // ??????????????? ??????
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

        // ??????????????? ?????? ??????
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();

            }
        });
        // ????????? ????????? ?????? ??????
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CloudActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
            }
        });

        // ???????????? ????????? ?????? ??????
        fabDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CloudActivity.this, "???????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CloudActivity.this, DiaryActivity.class);
                startActivity(intent);
            }
        });

        //???????????? ?????? ?????? ????????????
        final FirebaseUser user = mAuth.getCurrentUser();
        //uid_text.setText(databaseReference.child("Users").child(user.getUid()).child("birth").toString());
        readUser(user.getUid());
        userUid = user.getUid();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            //???????????? ?????? ???????????? ???????????? ??????
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            //???????????? ?????? ???????????? ???????????? ??????
            public void onClick(View v) {
                mAuth.getCurrentUser().delete();
            }
        });

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            //?????? ?????? ???????????? ???????????? ??????
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
            //?????? ?????? ?????? ???????????? ???????????? ??????
            public void onClick(View v) {
                renameUser(user.getUid(),edtRename.getText().toString());
            }

        });
    }

    private void renameUser(String userId, String newName) {
        //????????? ??????
        databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                user.setName(newName);
                databaseReference.child("Users").child(userId).child("name").setValue(newName);


                uid_text.setText("??????: " + user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { //????????? ????????? ??? ??? ?????? ??? ??????
                Toast.makeText(getApplicationContext(),"?????? ????????? ??????????????????" , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void readUser(String userId) {
        //????????? ??????
        databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                uid_text.setText("??????: " + user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { //????????? ????????? ??? ??? ?????? ??? ??????
                Toast.makeText(getApplicationContext(),"???????????? ??????????????? ??????????????????" , Toast.LENGTH_LONG).show();
                uid_text.setText("????????? ????????????");
            }
        });
    }

    private void registerEmail(String userId, String email) {
        //????????? ??????
        databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                user.setEmail(email);
                databaseReference.child("Users").child(userId).child("email").setValue(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { //????????? ????????? ??? ??? ?????? ??? ??????
                Toast.makeText(getApplicationContext(),"???????????? ??????????????? ??????????????????" , Toast.LENGTH_LONG).show();
                uid_text.setText("????????? ????????????");
            }
        });
    }


    private void signOut() {
        mAuth.signOut();
        //?????? ?????????
        Intent intent = new Intent(CloudActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();

        //?????? ?????????
        Toast.makeText(CloudActivity.this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CloudActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //?????? ????????? ????????? ?????? ??????(????????? ????????? ?????? ???????????? ????????????)
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

                                            //????????????????????? ????????? ?????? ??????
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
            Toast.makeText(CloudActivity.this, "\'??????\' ????????? ?????? ??? ???????????? ???????????????", Toast.LENGTH_SHORT).show();
            return;
        }

        //2??? ?????? ???????????? ????????? ?????? ??? ?????? ??? ??? ??????...??? ???????????? ??? ??????????????? ?????????????
        else{
            finish();
        }
    }

    // ????????? ?????? ?????? ????????? ??????????????? ??????
    public void toggleFab() {
        if(fabMain_status) {
            // ????????? ?????? ?????? ??????
            // ??????????????? ??????
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabCamera, "translationY", 0f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabDiary, "translationY", 0f);
            fe_animation.start();
            // ?????? ????????? ????????? ??????
            fabMain.setImageResource(R.drawable.ic_home);

        }else {
            // ????????? ?????? ?????? ??????
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabCamera, "translationY", -200f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabDiary, "translationY", -400f);
            fe_animation.start();
            // ?????? ????????? ????????? ??????
            fabMain.setImageResource(R.drawable.ic_home_pressed);
        }
        // ????????? ?????? ?????? ??????
        fabMain_status = !fabMain_status;
    }

}

