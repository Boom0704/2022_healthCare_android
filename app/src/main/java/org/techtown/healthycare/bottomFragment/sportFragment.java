package org.techtown.healthycare.bottomFragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.healthycare.R;
import org.techtown.healthycare.Sport;
import org.techtown.healthycare.UserAccount;
import org.techtown.healthycare.sportMeasure;
import org.techtown.healthycare.user.sportAdapter;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class sportFragment extends Fragment implements sportAdapter.onItemListener {

    private RecyclerView recyclerView;
    private ArrayList<Sport> sportList;
    private sportAdapter sportAdapter; //자료형 잘보셈
    //    private FirebaseDatabase database;
//    private DatabaseReference databaseReference;
    private Sport selecdSport ;
    ActivityResultLauncher<Intent> activityResultLauncher; // Intent형 activityResultLauncher 객체 생성
    private Animation translateLeftAnim;
    private Animation translateRightAnim;
    private FloatingActionButton addButton;
    private Button addButton2;
    private EditText input1;
    private EditText input2;
    private FirebaseFirestore db;
    private int int_userPoint;

    private UserAccount thisUser;

    LinearLayout page; // 두번째 레이아웃
    boolean isPageOpen = false;
    FirebaseUser firebaseUser ;




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public sportFragment() {
        // Required empty public constructor
    }


    public static sportFragment newInstance(String param1, String param2) {
        sportFragment fragment = new sportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_2, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        page= view.findViewById(R.id.page);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // 사용자 가져오기
        Log.d("getEmail",firebaseUser.getEmail());


        // 좌우 애니매이션 바뀜뀜
        translateLeftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.translate_right);
        translateRightAnim = AnimationUtils.loadAnimation(getContext(), R.anim.translate_left);
        // 애니메이션 리스너 생성
        SlidingPageAnimationListener animationListener = new SlidingPageAnimationListener();
        // 애니메이션 리스너 등록
        translateLeftAnim.setAnimationListener(animationListener);
        translateRightAnim.setAnimationListener(animationListener);


        addButton2 = view.findViewById(R.id.addButton2);
        input1 = view.findViewById(R.id.addSport);
        input2 = view.findViewById(R.id.addSport2);

        addButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String time1 = simpleDateFormat.format(date);

                Sport addSport = new Sport(input1.getText().toString(), Integer.parseInt(input2.getText().toString()), time1);
                sportList.add(addSport);
                DocumentReference washingtonRef = db.collection("user").document(firebaseUser.getEmail());
                washingtonRef.update("sport", FieldValue.arrayUnion(addSport));

                sportAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침


            }
        });

        addButton = view.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen){
                    //애니매이션 시작
                    page.startAnimation(translateRightAnim);
                } else{
                    // 두 번째 레이아웃 visible
                    page.setVisibility(View.VISIBLE);
                    // 애니메이션 시작
                    page.startAnimation(translateLeftAnim);
                }
            }
        });


        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setHasFixedSize(true);

        sportList = new ArrayList<>(); //sport 객체를 담을 어레이 리스트 / 아,, 리사이클러뷰 쓰는 곳에서 데이터 만들어주고 리사이클러뷰 어뎁터에 넣어주고 뷰에 넣는거지


        db = FirebaseFirestore.getInstance();
        DocumentReference doCRef = db.collection("user").document(firebaseUser.getEmail());
        doCRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("sport data", "DocumentSnapshot OK");

                        UserAccount user = document.toObject(UserAccount.class);

                        for(int i = 0 ;i < user.getSport().size(); i++){
                            sportList.add(user.getSport().get(i));
                        }
                        sportAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                    }

                    else
                    {
                        Log.d("sport data", "DocumentSnapshot failed");
                    }
                }
                else
                {
                    Log.d("sport data", "No such document");
                }


            }
        });

        /*
         * 실시간 데이터 베이스
         * */
//        databaseReference = database.getReference("Sport"); //DB 테이블 연결
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            //파이어베이스 데이터베이스의 데이터를 받아오는 곳
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                sportList.clear();// 기존 배열리스트가 존재하지않게 초기화
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Sport sport = dataSnapshot.getValue(Sport.class);// 만들어놨던 sport 객체에 데이터 담음. 데이터 테이블하고 필드랑 일치해야할듯
//                    sportList.add(sport);
//
//                }
//                sportAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("디비로딩 실패","sdfdsfsd");
//
//
//
//            }
//        });




        sportAdapter = new sportAdapter(sportList, getContext());
        recyclerView.setAdapter(sportAdapter);
        sportAdapter.setOnClickListener(this);


        sendData();


        return view;
    }

    private void sendData() {
        //activityResultLauncher 초기화
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == 2022) { //resultCode가 2022로 넘어왔다면

                            Intent intent = result.getData(); // ActivityResult 객체 result로 intent를 받아온다.
                            String backData = intent.getStringExtra("runningTime"); // 설정한 key 값으로 StringExtra를 받아온다.
                            String str_sportName = intent.getStringExtra("sport"); // 설정한 key 값으로 StringExtra를 받아온다.
                            int int_ExerciseTime = Integer.parseInt(backData);
                            Toast.makeText(getContext(),
                                    str_sportName+"를 총  "+ backData + "초 동안 운동하셨습니다.", Toast.LENGTH_SHORT).show();

                            sportAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                        }
                    }
                });
    }

    public void onItemClicked(View view, int position) {
        // Intent intent = new Intent(getContext(), sportMeasure.class);

        selecdSport = sportList.get(position);

        DocumentReference docRef  = db.collection("user").document(firebaseUser.getEmail());
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

        int sportCalorie = selecdSport.getCalorie();
        Toast.makeText(getContext(),
                Integer.toString(selecdSport.getCalorie()), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this.getActivity(), sportMeasure.class);
        intent.putExtra("sport", selecdSport.getSportName());
        intent.putExtra("sportCalorie", sportCalorie);
        intent.putExtra("sportRunTime", selecdSport.getRunTime());
        intent.putExtra("userPoint", int_userPoint);
        activityResultLauncher.launch(intent);
        // startActivityforResult가 아닌, ActivityResultLauncher의 lanunch 메서드로 intent를 실행
    }

    @Override
    public void onDeleteClick(View v, int positon) {

        selecdSport = sportList.get(positon);
        sportList.remove(positon);
        DocumentReference docRef  = db.collection("user").document(firebaseUser.getEmail());
        // Remove the 'capital' field from the document
        Map<String, Object> updates = new HashMap<>();
        updates.put("sport",FieldValue.arrayRemove(selecdSport));
        //   Toast.makeText(getContext(),"삭제된"+FieldValue.arrayRemove(selecdSport),Toast.LENGTH_LONG).show();


        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Log.d("sport data", "DocumentSnapshot OK");

                    sportAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                }

                else
                {
                    Log.d("sport data", "DocumentSnapshot failed");
                }
            }


        });






        sportAdapter.notifyDataSetChanged();
    }


    private class SlidingPageAnimationListener implements Animation.AnimationListener{
        public void onAnimationEnd(Animation animation){
            if(isPageOpen){
                page.setVisibility(View.INVISIBLE);

                isPageOpen = false;
            }
            else{

                isPageOpen = true;
            }
        }
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}