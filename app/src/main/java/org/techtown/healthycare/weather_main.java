package org.techtown.healthycare;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class weather_main extends AppCompatActivity {
    // WeatherActivity 뷰
    Spinner icSpinner;
    TextView nowDateText;
    ListView icListView;
    Button loadBtn;

    // listview_item 뷰 정보
    ImageView icWeatherImg;
    TextView icWeatherTime,icTemp,icWfKor,icPop;

    // 입력 스트림, InputStream ( 날씨 누리에서 데이터를 받을 스트림 객체 )
    InputStream inputStream;

    // 인천의 날씨 정보를 저장해줄 ArrayList 생성 및 초기화.
    ArrayList<ICWeatherData> weatherDataArrayList = new ArrayList<>();

    // 스피너에서 사용할 arrayAdapter 만들기
    ArrayAdapter<String> arrayAdapter;

    // Adapter class 객체.
    ICWeatherAdapter weatherAdapter;

    // 날씨 정보 저장 주소 배열
    String [] addressArray = {"https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=1100000000","https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=2600000000",
            "https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=2700000000","https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=2800000000",
            "https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=2900000000","https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=3000000000",
            "https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=3100000000","https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=3600000000",
            "https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4100000000","https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4200000000",
            "https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4300000000","https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4400000000",
            "https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4500000000","https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4600000000",
            "https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4700000000","https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4800000000",
            "https://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=5000000000"};

    // Spinner에서 보여질 이름의 배열
    String [] spinnerArray = {"서울특별시","부산광역시","대구광역시","인천광역시","광주광역시","대전광역시","울산광역시","세종특별자치시","경기도","강원도","충북","충남",
            "전북","전남","경북","경남","제주특별자치도",};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

        icSpinner = findViewById(R.id.icSpinner);
        nowDateText = findViewById(R.id.nowDateText);
        icListView = findViewById(R.id.icListView);

        arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,spinnerArray);
        icSpinner.setAdapter(arrayAdapter);

        weatherAdapter = new ICWeatherAdapter(this);
        icListView.setAdapter(weatherAdapter);

        icSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                weatherDataArrayList.clear();       // arrayList를 지운다.
                ICWeatherHttpAsyncTask task = new ICWeatherHttpAsyncTask();
                task.execute(addressArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    // Adapter class
    class ICWeatherAdapter extends BaseAdapter
    {
        Context context;

        ICWeatherAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public int getCount()
        {
            return weatherDataArrayList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.listview_item,parent,false);
            }

            icWeatherImg = convertView.findViewById(R.id.icWeatherImg);
            icWeatherTime = convertView.findViewById(R.id.icWeatherTime);
            icTemp = convertView.findViewById(R.id.icTemp);
            icWfKor = convertView.findViewById(R.id.icWfKor);
            icPop = convertView.findViewById(R.id.icPop);

            ICWeatherData weatherData = weatherDataArrayList.get(position);
            icWeatherTime.setText(weatherData.date + "시");
            icTemp.setText("기온 : " + weatherData.temp + "도");
            icWfKor.setText("날씨 : " + weatherData.wfKor);
            icPop.setText("강수확률 : " + weatherData.pop + "%");
            if(weatherData.wfKor.equals("맑음"))
            {
                icWeatherImg.setImageResource(R.drawable.sunny);
            }

            return convertView;
        }
    }

    // AsyncTask 정의
    class ICWeatherHttpAsyncTask extends AsyncTask<String,Void,String>
    {
        String recive;

        ProgressDialog dlg = new ProgressDialog(weather_main.this);

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dlg.setMessage("날씨 정보를 로딩중입니다...");     // 다이얼로그의 메시지
            dlg.show();     // 다이얼로그를 보여준다.
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            nowDateText.setText(recive);     // 화면에 날짜 정보를 출력한다.
            weatherAdapter.notifyDataSetChanged();      // 날씨 정보가 arrayList에 입력되었음을, 어댑터에게 공지한다.
            dlg.dismiss();      // 다이얼로그를 종료.
        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                URL url = new URL(strings[0]);     // 인터넷 주소 처리.
                inputStream = url.openStream(); // xml데이터를 입력 스트림으로 받는다.
                recive = xmlParsing();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return recive;
        }
    }

    // xmlParsing 함수 정의
    public String xmlParsing()
    {
        String date = null;

        try
        {
            // parser 공장을 생성 - > parser 생성 - > 데이터 입력.
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  // 싱글턴 패턴으로 대입.
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream,"UTF-8");       // charset 변환.
            String tag = "";        // xml 태그명 저장 변수
            int eventType = parser.getEventType();      // xml 문서의 요소 위치를 변수 eventType으로 저장.
            ICWeatherData tempWeatherData = null;

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();     // 시작 태그의 이름을 가져온다.
                        if(tag.equals("data"))
                        {
                            // 시작 태그가 data라면, ICWeatherData를 생성.
                            tempWeatherData = new ICWeatherData();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(tag.equals("pubDate"))
                        {
                            // 날짜 정보를 가져온다.
                            date = parser.getText();
                            date = date.trim();
                        }
                        else if(tag.equals("hour"))
                        {
                            tempWeatherData.date = parser.getText();
                        }
                        else if(tag.equals("temp"))
                        {
                            tempWeatherData.temp = parser.getText();
                        }
                        else if(tag.equals("wfKor"))
                        {
                            tempWeatherData.wfKor = parser.getText();
                        }
                        else if(tag.equals("pop"))
                        {
                            tempWeatherData.pop = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        // 종료 태그가 data면 weatherDataArrayList에 tempWeatherData 추가
                        tag = parser.getName();
                        if(tag.equals("data"))
                        {
                            weatherDataArrayList.add(tempWeatherData);
                        }
                        tag = "";       // 태그 초기화.
                        break;
                }
                eventType = parser.next();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return date;
    }
}