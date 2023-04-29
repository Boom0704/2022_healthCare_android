package org.techtown.healthycare.bottomFragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import org.techtown.healthycare.R;
import org.techtown.healthycare.WebViewActivity;
import org.techtown.healthycare.gps_main;
import org.techtown.healthycare.mainImageAdapter;
import org.techtown.healthycare.mainImageModel;
import org.techtown.healthycare.weather_main;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    ViewPager2 imageContainer;
    mainImageAdapter adapter;
    WormDotsIndicator Indicator ;
    int currentPage = 0;
    Timer timer;
    ImageButton imageButton;
    Button gpsbutton, weabutton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);
        imageContainer = view.findViewById(R.id.skin_container); //뷰페이져2
        Indicator =  view.findViewById(R.id.indicator);
        imageButton = view.findViewById(R.id.imageButton);
        gpsbutton = view.findViewById(R.id.button_myloc);
        weabutton = view.findViewById(R.id.button_mywea);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                startActivity(intent);
            }
        });
        //gps 연결버튼
        gpsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loc_intent = new Intent(getActivity(), gps_main.class);
                startActivity(loc_intent);
            }
        });
        //날씨 연결버튼
        weabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wea_intent = new Intent(getActivity(), weather_main.class);
                startActivity(wea_intent);
            }
        });

        // 슬라이더 설정
        setSilder();

        //어댑터 초기화
        init();
        getData();

        // 페이지 전환 콜백 메서드
        imageContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            // 페이지가 선택된다면
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        return view;
    }
    private void setSilder() {
        Handler sliderHandler = new Handler();
        Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == 3){
                    currentPage = 0;
                }
                imageContainer.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sliderHandler.post(Update);
            }
        }, 500, 3000);
    }
    // 사진
    private void getData() {
        adapter.addSkin( new mainImageModel(R.drawable.image_thread3,"규칙적운동"));
        adapter.addSkin( new mainImageModel(R.drawable.image_thread2,"동기부여"));
        adapter.addSkin( new mainImageModel(R.drawable.image_thread1,"랭킹"));

        adapter.notifyDataSetChanged();
    }

    private void init(){
        adapter = new mainImageAdapter();   // 어댑터 생성
        imageContainer.setAdapter(adapter); // 뷰페이져2 어댑터 설정
        Indicator.setViewPager2(imageContainer); // 인디게이터 설정
    }



}