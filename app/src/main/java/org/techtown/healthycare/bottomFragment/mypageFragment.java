package org.techtown.healthycare.bottomFragment;

import static java.lang.Integer.parseInt;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.healthycare.LoginActivity;
import org.techtown.healthycare.R;
import org.techtown.healthycare.Sport;
import org.techtown.healthycare.UserAccount;
import org.techtown.healthycare.WebViewActivity;
import org.techtown.healthycare.usePointActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mypageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mypageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button btn_logout, btn_usePoint;
    private TextView tv_user_email, tv_user_point;
    private int int_userPoint;
    private LinearLayout LL_sportList;

    private ArrayList<Sport> sportList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public mypageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment4.
     */
    // TODO: Rename and change types and number of parameters
    public static mypageFragment newInstance(String param1, String param2) {
        mypageFragment fragment = new mypageFragment();
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

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_4, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String str_userEmail = firebaseUser.getEmail().toString();

        tv_user_email = view.findViewById(R.id.tv_user_email);
        btn_logout = view.findViewById(R.id.btn_logout);
        tv_user_point = view.findViewById(R.id.tv_user_point);
        btn_usePoint = view.findViewById(R.id.btn_usePoint);
        tv_user_email.setText(str_userEmail);

        LL_sportList = view.findViewById(R.id.LL_sportList);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                250);


        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("user").document(str_userEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("sport data", "DocumentSnapshot OK");

                        UserAccount user = document.toObject(UserAccount.class);
                        int_userPoint = user.getUserPoint();
                        tv_user_point.setText(Integer.toString(int_userPoint));

                        for (int i = 0; i < user.getSport().size(); i++) {
                            sportList.add(user.getSport().get(i));
                        }
                        for (int j = 0; j < sportList.size(); j++) {
                            LinearLayout ll = new LinearLayout(getActivity());
                            ll.setOrientation(LinearLayout.HORIZONTAL);

                            // TextView 생성
                            TextView tv_sportName = new TextView(getActivity());
                            tv_sportName.setText(user.getSport().get(j).getSportName());
                            tv_sportName.setTextSize(18);
                            tv_sportName.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            tv_sportName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            ll.addView(tv_sportName, 500, 250);


                            String str_cal = Integer.toString(user.getSport().get(j).getCalorie());
                            TextView tv_cal = new TextView(getActivity());
                            tv_cal.setText(str_cal);
                            tv_cal.setTextSize(15);
                            tv_cal.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            tv_cal.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            ll.addView(tv_cal, 500, 250);


                            String str_time = Integer.toString(user.getSport().get(j).getRunTime());
                            TextView tv_time = new TextView(getActivity());
                            tv_time.setText(str_time);
                            tv_time.setTextSize(15);
                            tv_time.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            tv_time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            ll.addView(tv_time, 500, 250);


                            LL_sportList.addView(ll);
                        }


                    } else {
                        Log.d("sport data", "DocumentSnapshot failed");
                    }
                } else {
                    Log.d("sport data", "No such document");
                }

            }
        });


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btn_usePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), usePointActivity.class);
                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}