package com.eustress.moong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private com.google.android.material.textfield.TextInputEditText email_main;
    private com.google.android.material.textfield.TextInputEditText pwd_main;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        login = (Button) findViewById(R.id.login_btn);
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

        //로그아웃 하지 않으면 계속 로그인 되어있기
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), CloudActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void emailJoinClicked(View view){
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    public void googleJoinClicked(View view){
        Intent intent = new Intent(this, GoogleJoinActivity.class);
        startActivity(intent);
    }
}