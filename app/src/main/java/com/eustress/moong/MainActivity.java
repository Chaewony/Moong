package com.eustress.moong;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private Button anonymous;
    private com.google.android.material.textfield.TextInputEditText email_main;
    private com.google.android.material.textfield.TextInputEditText pwd_main;
    private FirebaseAuth mAuth;

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("743777353010-fu63lvc3touji7lr9k8alvdmmbijdmm5.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);

        login = (Button) findViewById(R.id.login_btn);
        anonymous = (Button) findViewById(R.id.anonymous_btn);
        email_main = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.edit_id);
        pwd_main = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.edit_pw);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_main.getText().toString().trim();
                String pwd = pwd_main.getText().toString().trim();
                //String형 변수 email.pwd(edittext에서 받오는 값)으로 로그인하는것
                mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {//성공했을때
                                    //화면전환
                                    Intent intent = new Intent(MainActivity.this, CloudActivity.class);
                                    startActivity(intent);
                                } else {//실패했을때
                                    Toast.makeText(MainActivity.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInAnonymously:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    
                                    //기본 데이터베이스 생성
                                    String uid = user.getUid();
                                    HashMap<Object,String> hashMap = new HashMap<>();

                                    hashMap.put("uid",uid);
                                    hashMap.put("name", "익명의 뭉티");

                                    //Users 산하에 uid 넣고, uid 산하에 위에서 만든 해시맵 넣기
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getInstance().getReference().child("Users");
                                    reference.child(uid).setValue(hashMap);
                                    
                                    //화면전환
                                    Intent intent = new Intent(MainActivity.this, CloudActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //로그아웃 하지 않으면 계속 로그인 되어있기
        if (mAuth.getCurrentUser() != null) {
            //Intent intent = new Intent(getApplication(), CloudActivity.class);
            //startActivity(intent);
            //finish();
        }
    }

    public void emailJoinClicked(View view){
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    public void googleJoinClicked(View view){
        /*Intent intent = new Intent(this, GoogleJoinActivity.class);
        startActivity(intent);*/
        // Initialize sign in intent
        Intent intent = googleSignInClient.getSignInIntent();
        // Start activity for result
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                        // Check credential
                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Check condition
                                if (task.isSuccessful()) {
                                    // When task is successful redirect to profile activity display Toast
                                    startActivity(new Intent(MainActivity.this, CloudActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    //displayToast("Firebase authentication successful");
                                } else {
                                    // When task is unsuccessful display Toast
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
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
}