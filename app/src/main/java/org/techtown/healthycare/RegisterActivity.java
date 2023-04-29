package org.techtown.healthycare;

import static android.content.ContentValues.TAG;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;
    private EditText et_email, et_pwd, et_age;
    private Button btn_register;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_age = findViewById(R.id.et_age);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strEmail = et_email.getText().toString();
                String strPass = et_pwd.getText().toString();
                String strAge = et_age.getText().toString();
                int intAge = parseInt(strAge);
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

//                            Map<String, Object> user = new HashMap<>();// 단일 필드
//                            user.put("userEmail", strEmail);
//                            user.put("userPoint", 0);
//                            user.put("userAge", intAge);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            String time1 = simpleDateFormat.format(date);

                            // 유저에게 추가될 기본운동
                            Sport sport1 = new Sport("축구", 9,time1,0);
                            Sport sport2 = new Sport("수영", 8,time1,0);
                            Sport sport3 = new Sport("달리기", 10,time1,0);

                            //유저 기본 정보 셋팅 및 유저 생성
                            UserAccount users =
                                    new UserAccount(strEmail, strPass, intAge, 0,0, Arrays.asList(sport1, sport2, sport3));


                            db.collection("user").document(strEmail)
                                    .set(users)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                            Toast.makeText(getApplicationContext(), "파이어스토어 유저 저장", Toast.LENGTH_SHORT).show();
                                        }
                                    })


                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                            Toast.makeText(getApplicationContext(), "파이어스토어 유저 저장실패", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}