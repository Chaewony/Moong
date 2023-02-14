package com.eustress.moong;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    private Button join;
    private Button verification;
    private com.google.android.material.textfield.TextInputEditText name;
    private com.google.android.material.textfield.TextInputEditText birth;
    private com.google.android.material.textfield.TextInputEditText email;
    private com.google.android.material.textfield.TextInputEditText pw;
    private com.google.android.material.textfield.TextInputEditText pwCheck;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private com.google.android.material.textfield.TextInputLayout email_layout;
    private com.google.android.material.textfield.TextInputLayout pw_layout;
    private com.google.android.material.textfield.TextInputLayout pwCheck_layout;

    private boolean rightEmail = false; //회원가입을 정상적으로 할 수 있는지 판별하는 변수
    private boolean rightPW = false; //회원가입을 정상적으로 할 수 있는지 판별하는 변수
    private boolean rightPWCheck = false; //회원가입을 정상적으로 할 수 있는지 판별하는 변수

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        join = (Button) findViewById(R.id.join_btn);
        verification = (Button) findViewById(R.id.verify_btn);
        email = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.join_edit_email);
        birth = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.join_edit_birth);
        name = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.join_edit_name);
        pw = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.join_edit_pw);
        pwCheck = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.join_edit_pwCheck);
        firebaseAuth = firebaseAuth.getInstance();
        email_layout = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.join_edit_email_layout);
        pw_layout = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.join_edit_pw_layout);
        pwCheck_layout = (com.google.android.material.textfield.TextInputLayout) findViewById(R.id.join_edit_pwCheck_layout);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            //가입하기 버튼 눌렀을때 실행되는 코드
            public void onClick(View v) {
                //공백인 부분을 제거하고 보여주는 trim();
                final String s_email = email.getText().toString().trim();
                final String s_pwd = pw.getText().toString().trim();
                final String s_pwdCheck = pwCheck.getText().toString().trim();
                final String s_birth = birth.getText().toString().trim();
                final String s_name = name.getText().toString().trim();

                //비밀번호 확인와 비밀번호가 일치했을 때
                if (rightEmail && rightPW && rightPWCheck) {
                    firebaseAuth.createUserWithEmailAndPassword(s_email, s_pwd)
                            .addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        String email = user.getEmail();
                                        String uid = user.getUid();

                                        //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                        HashMap<Object,String> hashMap = new HashMap<>();

                                        hashMap.put("uid",uid);
                                        hashMap.put("email",email);
                                        hashMap.put("name",s_name);
                                        hashMap.put("birth",s_birth);

                                        //Users 산하에 uid 넣고, uid 산하에 위에서 만든 해시맵 넣기
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getInstance().getReference().child("Users");
                                        reference.child(uid).setValue(hashMap);
                                        
                                        //화면 바꾸기
                                        Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(JoinActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                }
                //비밀번호 확인와 비밀번호가 일치하지 않았을 때
                else {
                    Toast.makeText(JoinActivity.this, "잘못 입력한 영역이 있는지 확인해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });

        //정상적인 이메일 형식인지 확인
        setupEmailLabelError();
        //정상적인 비밀번호 형식인지 확인
        setupPWLabelError();
        //비밀번호 확인란에 작성한 비밀번호와 비밀번호가 일치한지 확인
        setupPWCheckLabelError();
    }

    //edit text의 error 관련 함수
    private void setupEmailLabelError() {
        final TextInputLayout floatingUseLabel = (TextInputLayout) findViewById(R.id.join_edit_email_layout);
        email_layout.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                /*if (text.length() > 0 && text.length() <= 4) {
                    floatingUsernameLabel.setError("오류");
                    floatingUsernameLabel.setErrorEnabled(true);
                } else {
                    floatingUsernameLabel.setErrorEnabled(false);
                }*/

                Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;

                if (pattern.matcher(email.getText().toString().trim()).matches()) {
                    //이메일 맞음!
                    email_layout.setErrorEnabled(false);
                    email.setBackgroundResource(R.drawable.text_field_background);
                    rightEmail = true;
                    verification.setVisibility(View.VISIBLE);

                } else {
                    //이메일 아님!
                    email_layout.setError(getText(R.string.join_wrong_email));
                    email_layout.setErrorEnabled(true);
                    email.setBackgroundResource(R.drawable.text_field_background_error);
                    rightEmail = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupPWLabelError() {
        final TextInputLayout floatingUseLabel = (TextInputLayout) findViewById(R.id.join_edit_pw_layout);
        pw_layout.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() > 5 && text.length() <= 20) {
                    pw_layout.setErrorEnabled(false);
                    pw.setBackgroundResource(R.drawable.text_field_background);
                    rightPW = true;
                } else {
                    pw_layout.setError(getText(R.string.join_wrong_pw));
                    pw_layout.setErrorEnabled(true);
                    pw.setBackgroundResource(R.drawable.text_field_background_error);
                    rightPW = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupPWCheckLabelError() {
        final TextInputLayout floatingUseLabel = (TextInputLayout) findViewById(R.id.join_edit_pwCheck_layout);
        pwCheck_layout.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (pw.getText().toString().trim().equals(pwCheck.getText().toString().trim())) {
                    pwCheck_layout.setErrorEnabled(false);
                    pwCheck.setBackgroundResource(R.drawable.text_field_background);
                    rightPWCheck = true;
                } else {
                    pwCheck_layout.setError(getText(R.string.join_wrong_pwCheck));
                    pwCheck_layout.setErrorEnabled(true);
                    pwCheck.setBackgroundResource(R.drawable.text_field_background_error);
                    rightPWCheck = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
