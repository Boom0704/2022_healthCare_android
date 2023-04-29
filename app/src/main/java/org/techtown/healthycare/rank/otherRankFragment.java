package org.techtown.healthycare.rank;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.healthycare.R;
import org.techtown.healthycare.Sport;
import org.techtown.healthycare.UserAccount;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link otherRankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class otherRankFragment extends Fragment {

    private DocumentReference docRef;
    private FirebaseFirestore db;
    private ArrayList<UserAccount> userList = new ArrayList<>();;
    private ArrayList<Sport> sportList;

    private RankRecycylerAdapter adapter;
    private ArrayList<UserAccount> sortedUser;
    private RecyclerView recyclerView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public otherRankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment allRankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static otherRankFragment newInstance(String param1, String param2) {
        otherRankFragment fragment = new otherRankFragment();
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
        // Inflate the layout for this fragment

        // 리싸이클러 뷰 설정 id값 찾을때 container가 아니고 view 임!
        View view = (View) inflater.inflate(R.layout.fragment_other_rank, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_rank);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setHasFixedSize(true);

        Bundle bundle = getArguments();  //번들 받기. getArguments() 메소드로 받음.
        String name = "불러오기 오류";
        if(bundle != null){
             name = bundle.getString("Name");
        }

        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(name+"칼로리 소모량 순위");

        getFireBaseData(name);
        Log.d("sortedUser:",userList.toString());

        sorted();
        return view;
    }

    private void sorted() {


    }

    private void getFireBaseData(String name) {
        db = FirebaseFirestore.getInstance();

        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                sportList=new ArrayList<>();
                                int newPoint = 0;

                                try {
                                    //json 오브젝트 생성
                                    Map<String, Object>  data = document.getData();
                                    JSONObject jsonObject = new JSONObject(data);
                                    //json 으로부터 키에 맞는값 가져오기
                                    int userPoint = jsonObject.getInt("userPoint");
                                    String pass = jsonObject.getString("pass");
                                    String userEmail = jsonObject.getString("userEmail");
                                    int age = jsonObject.getInt("age");

                                    //user sport 배열
                                    JSONArray jsonArray = jsonObject.getJSONArray("sport");

                                    for (int i=0; i < jsonArray.length(); i++)
                                    {
                                        jsonObject = jsonArray.getJSONObject(i);
                                        int sportCalorie = jsonObject.getInt("calorie");
                                        String sportName = jsonObject.getString("sportName");
                                        int runTime = jsonObject.getInt("runTime");
                                        if(sportName.equals(name)){
                                            newPoint=runTime*sportCalorie/60;
                                        }
                                    }

                                    //유저생성
                                    UserAccount user = new UserAccount(userEmail, pass,age,newPoint,userPoint);
                                    userList.add(user);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } else {
                            Log.d("db", "Error getting documents: ", task.getException());
                        }

                        //정렬된 유저 리스트 반환
                        sortedUser =(ArrayList<UserAccount>) userList.stream()
                                .sorted(Comparator.comparing(UserAccount::getCalorie).reversed())
                                .collect(Collectors.toList());
                        adapter = new RankRecycylerAdapter(sortedUser, getContext());




                        recyclerView.removeAllViewsInLayout();
                        recyclerView.setAdapter(adapter);
                        recyclerView.setHasFixedSize(false);
                        // adapter.notifyDataSetChanged();
                    }
                });
    }

}