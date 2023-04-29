package org.techtown.healthycare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class usePointActivity extends AppCompatActivity {

    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private int int_userPoint;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_use_point);

            arrayList = new ArrayList<>();
            arrayList.add("※은행 선택");
            arrayList.add("Nh 농협");
            arrayList.add("국민");
            arrayList.add("기업");
            arrayList.add("산업");
            arrayList.add("신한");
            arrayList.add("우리");
            arrayList.add("기타");

            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            final String str_userEmail =firebaseUser.getEmail().toString();

            DocumentReference docRef = db.collection("user").document(str_userEmail);


            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful())
                    {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Log.d("sport data", "DocumentSnapshot OK");

                            UserAccount user = document.toObject(UserAccount.class);
                            int_userPoint = user.getUserPoint();

                        } else {
                            Log.d("sport data", "DocumentSnapshot failed");
                        }
                    }
                    else
                    {
                        Log.d("sport data", "No such document");
                    }

                }
            });



            arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

            EditText tv_account = (EditText) findViewById(R.id.tv_account);
            EditText tv_bankName = (EditText) findViewById(R.id.tv_bankName);
            EditText tv_money = (EditText) findViewById(R.id.tv_money);

            Spinner spinner = (Spinner) findViewById(R.id.spinner);

            Button btn_usePoint = (Button) findViewById(R.id.btn_usePoint);

            arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayList);
            spinner.setAdapter(arrayAdapter);

//아이템 셋
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),arrayList.get(i)+"가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();
                String sp_value = spinner.getSelectedItem().toString();
                if(sp_value.equals("기타")){
                    tv_bankName.setEnabled(true);
                    tv_bankName.setText("");
                }
                else if(sp_value.equals("※은행 선택")){
                    tv_bankName.setText("은행을 선택해주세요.");
                    tv_bankName.setEnabled(false);
                }
                else{
                    tv_bankName.setText(arrayList.get(i));
                    tv_bankName.setEnabled(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        //버튼
        btn_usePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int int_money=Integer.valueOf(tv_money.getText().toString());


                if(int_money<=1000){
                    Toast.makeText(usePointActivity.this, "출금은 천원부터 가능합니다.\n 1000원 이상 출금해주세요.", Toast.LENGTH_LONG).show();
                }
                else if(int_money>int_userPoint){
                    Toast.makeText(usePointActivity.this, "금액이 부족합니다.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(usePointActivity.this, int_money+"원을 출금합니다.", Toast.LENGTH_LONG).show();

                    DocumentReference docRef  = db.collection("user").document(str_userEmail);

                    docRef.update("userPoint", int_userPoint-int_money)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });

                }
            }
        });
    }
}