package org.techtown.healthycare.bottomFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.healthycare.R;
import org.techtown.healthycare.Sport;
import org.techtown.healthycare.UserAccount;
import org.techtown.healthycare.rank.allRankFragment;
import org.techtown.healthycare.rank.otherRankFragment;
import org.techtown.healthycare.rank.rankFragmentAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link rankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class rankFragment extends Fragment {



    ViewPager2 viewPager2;
    TabLayout tabLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<Sport> sportList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public rankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static rankFragment newInstance(String param1, String param2) {
        rankFragment fragment = new rankFragment();
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
        View view = (View) inflater.inflate(R.layout.fragment_3, container, false);

        viewPager2 = view.findViewById(R.id.viewPager2);
        tabLayout = view.findViewById(R.id.tabs);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String str_userEmail = firebaseUser.getEmail().toString();


        sportList = new ArrayList<Sport>();
        db = FirebaseFirestore.getInstance();

        //비동기
        DocumentReference docRef = db.collection("user").document(str_userEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("sport data", "DocumentSnapshot OK");

                        UserAccount user = document.toObject(UserAccount.class);

                        for (int i = 0; i < user.getSport().size(); i++) {
                            sportList.add(user.getSport().get(i));
                        }

                        String[] titles = new String[sportList.size()+2];
                        titles[0]="전체 랭킹";
                        int size=1;
                        for (int i = 0; i < sportList.size(); i++) {
                            String temp = sportList.get(i).getSportName();
                            titles[size++] = temp;
                        }
                        rankFragmentAdapter fgAdapter = new rankFragmentAdapter(getActivity());


                        fgAdapter.addFragment(new allRankFragment());

                        for(int j=0; j<sportList.size(); j++) {
                            Bundle bundle = new Bundle();
                            bundle.putString("Name", sportList.get(j).getSportName());
                            otherRankFragment base =new otherRankFragment();
                            base.setArguments(bundle);
                            fgAdapter.addFragment(base);
                        }

                        viewPager2.setAdapter(fgAdapter);
                        new TabLayoutMediator(tabLayout, viewPager2,(tab, position) -> tab.setText(titles[position])).attach();

                    } else {
                        Log.d("sport data", "DocumentSnapshot failed");
                    }
                } else {
                    Log.d("sport data", "No such document");
                }

            }
        });
        return view;
    }
}