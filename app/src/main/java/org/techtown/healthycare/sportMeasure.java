package org.techtown.healthycare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class sportMeasure extends AppCompatActivity {
    TextView textView;
    Button btn,btn_startAndStop;

    private Boolean isRunning = false;
    private long pauseOffset;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore db;
    private long instantaneousTime = 0;
    private int newUserPoint;
    private int newUserCalorie;
    private ArrayList<Sport> sportList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_measure);
        Intent intent = getIntent(); // 메인 액티비티에서 넘어온 intent 받아옴.



        btn = findViewById(R.id.button);
        textView = findViewById(R.id.text);
        btn_startAndStop = findViewById(R.id.btn_startAndStop);
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        String str_userEmail = firebaseUser.getEmail().toString();

        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);


        // 메인 액티비티가 넘겨준 StringExtre 값 받음.
        String data = intent.getStringExtra("sport"); //받은데이터 설정


        textView.setText(data);

        btn_startAndStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    btn_startAndStop.setText("시작");
                    btn_startAndStop.setBackgroundColor(Color.LTGRAY);
                    chronometer.stop();
                    pauseOffset= SystemClock.elapsedRealtime()-chronometer.getBase();
                    instantaneousTime = SystemClock.elapsedRealtime();
                    isRunning=false;
                }
                else{
                    btn_startAndStop.setText("일시정지");
                    btn_startAndStop.setBackgroundColor(Color.BLUE);
                    chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
                    chronometer.start();
                    isRunning=true;
                }

                int elapsed;
                if(isRunning==true){
                    elapsed = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
                }
                else{
                    if(instantaneousTime==0){
                        return;}
                    elapsed = (int) (instantaneousTime - chronometer.getBase());
                }
                //////////////////////////////////////////////////
                DocumentReference docRef  = db.collection("user").document(firebaseUser.getEmail());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                UserAccount user = document.toObject(UserAccount.class);
                                sportList = new ArrayList<Sport>();
                                for(int i = 0 ;i < user.getSport().size(); i++){
                                    if(user.getSport().get(i).getSportName().equals(data)){
                                        user.getSport().get(i).setRunTime(user.getSport().get(i).getRunTime() + elapsed/1000);
                                    }
                                    sportList.add(user.getSport().get(i));
                                    newUserPoint = user.getUserPoint() + user.getSport().get(i).getCalorie()*elapsed/1000/60;
                                    newUserCalorie = user.getCalorie() + user.getSport().get(i).getCalorie()*elapsed/1000/60;
                                }
                            }
                        }
                    }
                });
                //////////////////



            }
        });

        btn.setOnClickListener(V->{
            String runningTime;
            int elapsed;
            if(isRunning==true){
                elapsed = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
            }
            else{
                if(instantaneousTime==0){
                    Toast.makeText(sportMeasure.this,
                            "운동을 시작하고 눌러주세요", Toast.LENGTH_SHORT).show();
                    return;}
                elapsed = (int) (instantaneousTime - chronometer.getBase());
            }
            runningTime = String.valueOf(elapsed/1000);


            DocumentReference docRef  = db.collection("user").document(firebaseUser.getEmail());
            docRef.update("sport", sportList)
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

            DocumentReference washingtonRef2 = db.collection("user").document(str_userEmail);
            washingtonRef2.update("userPoint", newUserPoint);
            DocumentReference washingtonRef3  = db.collection("user").document(str_userEmail);
            washingtonRef3.update("calorie", newUserCalorie);


            //resultCode와 데이터 값 전달을 위한 intent 생성
            Intent finish_intent = new Intent(getApplicationContext(), MainActivity.class);
            finish_intent.putExtra("runningTime", runningTime);
            finish_intent.putExtra("sport", data);
            setResult(2022, finish_intent);
            // 이전과 똑같이 setResult 메서드로 (ResultCode)결과 코드와intent값 전달
            finish();       // 액티비티 종료

        });
    }
}