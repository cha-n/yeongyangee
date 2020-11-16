package com.example.user.a1;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout mainLayout;
    LinearLayout suLayout;
    LinearLayout uLayout;
    LinearLayout slash;
    LinearLayout zLayout;

    Resources res;
    Animation growAnim;
    private RecyclerView recyclerview;

    String username;
    static String arr1[]; //foodname
    static String arr2[]; //hour
    static String arr3[]; //min
    static int number;


    TextView Success;
    List<ExpandableListAdapter.Item> data = new ArrayList<>();

    TextView etan;
    TextView edan;
    TextView egi;
    TextView edang;
    TextView ena;
    TextView ekcal;

    double ukcal=0, utan=0 , udan=0 , ugi=0 , udang=0 , una=0;
    double kcal=0, tan=0 , dan=0 , gi=0 , dang=0 , na=0;
    int probarMax = 128;

    //date 지정
    // 현재시간을 msec 으로 구한다.
    long now = System.currentTimeMillis();
    // 현재시간을 date 변수에 저장한다.
    Date date = new Date(now);

    SimpleDateFormat Year = new SimpleDateFormat("yyyy");        // nowDate 변수에 값을 저장한다.
    String year = Year.format(date);

    SimpleDateFormat Month = new SimpleDateFormat("MM");        // nowDate 변수에 값을 저장한다.
    String month = Month.format(date);

    SimpleDateFormat Day = new SimpleDateFormat("dd");        // nowDate 변수에 값을 저장한다.
    String day = Day.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //id 가져오기
        Intent intent = getIntent();
        username =intent.getStringExtra("Username");

        arr1=new String[15];
        arr2=new String[15];
        arr3=new String[15];
        number=-1;


        res = getResources();
        growAnim = AnimationUtils.loadAnimation(this, R.anim.grow);
        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        suLayout = (LinearLayout)findViewById(R.id.suLayout);
        uLayout = (LinearLayout)findViewById(R.id.uLayout);
        slash = (LinearLayout)findViewById(R.id.slash);
        zLayout=(LinearLayout)findViewById(R.id.zLayout);

        etan=(TextView)findViewById(R.id.etan);
        edan=(TextView)findViewById(R.id.edan);
        egi=(TextView)findViewById(R.id.egi);
        edang=(TextView)findViewById(R.id.edang);
        ena=(TextView)findViewById(R.id.ena);
        ekcal=(TextView)findViewById(R.id.ekcal);

        Success=(TextView)findViewById(R.id.textView6) ;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        new MainActivity.UserTask().execute();
        new MainActivity.SungbunTask().execute();
        new MainActivity.SikdanTask().execute();

        recyclerview=new RecyclerView(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        addItem4("/");
        addItem4("/");
        addItem4("/");
        addItem4("/");
        addItem4("/");
        addItem4("/");

    }

    //로그인 성공, 실패 가져오기
    private class  LoginTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;

        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json = "http://117.16.244.117:2001/auth/success";
                URL url = new URL($url_json);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
                Log.d("FOR_LOG", resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                Success.setText("성공");
                JSONArray jArray = new JSONArray(result);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //사용자 식단 가져오기
    private class SikdanTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;

        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json = "http://117.16.244.117:2001/cal/get";
                URL url = new URL($url_json);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
                Log.d("FOR_LOG", resultJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                String sameHour="초기값";
                String date="";
                List<ExpandableListAdapter.Item> data = new ArrayList<>();

                JSONArray jArray = new JSONArray(result);

                boolean sikdan=false;
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject friend = jArray.getJSONObject(i);
                    String nameOS = friend.getString("username");
                    String nameOS2 = friend.getString("year");
                    String nameOS3 = friend.getString("month");
                    String nameOS4 = friend.getString("day");
                    String nameOS5 = friend.getString("hour");
                    String nameOS6 = friend.getString("min");
                    String nameOS7 = friend.getString("foodname");

                    if (nameOS.equals(username)) {
                        if (nameOS2.equals(year) && nameOS3.equals(month) && nameOS4.equals(day)) {
                            sikdan=true;

                            date=nameOS5.concat(":");
                            date=date.concat(nameOS6);

                            number=number+1;
                            arr1[number]=nameOS7;
                            arr2[number]=nameOS5;
                            arr3[number]=nameOS6;


                            if(!date.equals(sameHour)) {
                                data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, date));
                                sameHour=date;
                            }
                            data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,nameOS7));

                            recyclerview.setAdapter(new ExpandableListAdapter(data));
                        }
                    }
                }

                if(sikdan!=true)
                {
                    addtxt("오늘의 식단을 등록하세요!");
                }
                else
                {
                    addRecycler(recyclerview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //사용자 영양성분 합 가져오기
    private class UserTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json = "http://117.16.244.117:2001/main2";
                URL url = new URL($url_json);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
                Log.d("FOR_LOG", resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            utan=0; udan=0; ugi=0; udang=0; una=0; ukcal=0;
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject friend = jArray.getJSONObject(i);
                    String nameOS = friend.getString("username");
                    String nameOS2 = friend.getString("year");
                    String nameOS3 = friend.getString("month");
                    String nameOS4 = friend.getString("day");

                    if (nameOS.equals(username)) {
                        if (nameOS2.equals(year) && nameOS3.equals(month) && nameOS4.equals(day)) {
                            ukcal = friend.getDouble("열량_kcal");
                            utan = friend.getDouble("탄수화물_g");
                            udan = friend.getDouble("단백질_g");
                            ugi = friend.getDouble("지방_g");
                            udang = friend.getDouble("당류_g");
                            una = friend.getDouble("나트륨_mg");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //사용자 영양성분 초기값 가져오기
    private class SungbunTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;

        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json = "http://117.16.244.117:2001/main";
                URL url = new URL($url_json);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
                Log.d("FOR_LOG", resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject friend = jArray.getJSONObject(i);
                    String nameOS = friend.getString("username");

                    if(nameOS.equals(username)) {
                        kcal= friend.getDouble("에너지_kcal");
                        tan = friend.getDouble("탄수화물_g");
                        dan = friend.getDouble("단백질_g");
                        gi = friend.getDouble("지방_g");
                        dang = friend.getDouble("당류_g");
                        na = friend.getDouble("나트륨_mg");

                        if(ukcal==0)
                        {
                            addItem("탄수화물", 3, Color.parseColor("#e8938e")); //그래프그리기
                            addItem("단백질", 3, Color.parseColor("#f6e4bf"));
                            addItem("지방", 3, Color.parseColor("#d4e5c5"));
                            addItem("당류", 3, Color.parseColor("#a2cfac"));
                            addItem("나트륨", 3, Color.parseColor("#4b8593"));
                            addItem("총 칼로리", 3, Color.parseColor("#4d5d91"));
                        }
                        else{
                            addItem("탄수화물", (utan / tan) * probarMax, Color.parseColor("#e8938e")); //그래프그리기
                            addItem("단백질", (udan / dan) * probarMax, Color.parseColor("#f6e4bf"));
                            addItem("지방", (ugi / gi) * probarMax, Color.parseColor("#d4e5c5"));
                            addItem("당류", (udang / dang) * probarMax, Color.parseColor("#a2cfac"));
                            addItem("나트륨", (una / na) * probarMax, Color.parseColor("#4b8593"));
                            addItem("총 칼로리", (ukcal / kcal) * probarMax, Color.parseColor("#4d5d91"));
                        }
                        String what1="", what2="", what3="", what4="", what5="", what6="";
                        if((utan-tan)>0) {
                            String t="+";
                            what1="초과";
                            t=t.concat(String.format("%.1f", utan - tan));
                            etan.setText(t);
                        }

                        if((udan-dan)>0) {
                            String d="+";
                            what2="초과";
                            d=d.concat(String.format("%.1f", udan - dan));
                            edan.setText(d);
                        }
                        if((ugi-gi)>0) {
                            String g="+";
                            what3="초과";
                            g=g.concat(String.format("%.1f", ugi - gi));
                            egi.setText(g);
                        }
                        if((udang-dang)>0) {
                            String da="+";
                            what4="초과";
                            da=da.concat(String.format("%.1f", udang - dang));
                            edang.setText(da);
                        }
                        if((una-na)>0) {
                            String n="+";
                            what5="초과";
                            n=n.concat(String.format("%.1f", una - na));
                            ena.setText(n);
                        }
                        if((ukcal-kcal)>0) {
                            String k="+";
                            what6="초과";
                            k=k.concat(String.format("%.1f", ukcal - kcal));
                            ekcal.setText(k);
                        }

                        addItem2(Double.parseDouble(String.format("%.1f",tan)));
                        addItem2(Double.parseDouble(String.format("%.1f",dan)));
                        addItem2(Double.parseDouble(String.format("%.1f",gi)));
                        addItem2(Double.parseDouble(String.format("%.1f",dang)));
                        addItem2(Double.parseDouble(String.format("%.1f",na)));
                        addItem2(Double.parseDouble(String.format("%.1f",kcal)));

                        addItem3(Double.parseDouble(String.format("%.1f",utan)),what1);
                        addItem3(Double.parseDouble(String.format("%.1f",udan)),what2);
                        addItem3(Double.parseDouble(String.format("%.1f",ugi)),what3);
                        addItem3(Double.parseDouble(String.format("%.1f",udang)),what4);
                        addItem3(Double.parseDouble(String.format("%.1f",una)),what5);
                        addItem3(Double.parseDouble(String.format("%.1f",ukcal)),what6);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addItem(String name, double value, int color) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 텍스트뷰 추가
        TextView textView = new TextView(this);
        textView.setText(name);
        textView.setTextColor(Color.BLACK);
        params.width = 150;
        params.setMargins(50, 35, 0, 0);
        itemLayout.addView(textView, params);

        // 프로그레스바 추가
        ProgressBar proBar = new ProgressBar(this, null,
                android.R.attr.progressBarStyleHorizontal);
        proBar.setIndeterminate(false);
        proBar.setMax(100);
        proBar.setProgress(100);

        if(color == Color.parseColor("#e8938e"))
            proBar.setProgressDrawable(

                    getResources().getDrawable(R.drawable.progressbar_color_red));
        else if(color == Color.parseColor("#f6e4bf"))
            proBar.setProgressDrawable(

                    getResources().getDrawable(R.drawable.progressbar_color_yellow));
        else if(color == Color.parseColor("#d4e5c5"))
            proBar.setProgressDrawable(

                    getResources().getDrawable(R.drawable.progressbar_color_green));


        else if(color == Color.parseColor("#a2cfac"))
            proBar.setProgressDrawable(

                    getResources().getDrawable(R.drawable.progressbar_color_green2));

        else if(color == Color.parseColor("#4b8593"))
            proBar.setProgressDrawable(

                    getResources().getDrawable(R.drawable.progressbar_color_blue));

        else
            proBar.setProgressDrawable(

                    getResources().getDrawable(R.drawable.progressbar_color_blue2));

        proBar.setAnimation(growAnim);


        params2.height = 70;
        params2.width = (int)value * 4;
        params2.gravity = Gravity.CENTER_VERTICAL;
        params2.setMargins(0,20,0,0);
        itemLayout.addView(proBar, params2);

        mainLayout.addView(itemLayout, params3);

    }

    private void addItem2(double value) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 텍스트뷰 추가
        TextView textView = new TextView(this);
        textView.setText(String.valueOf(value));
        textView.setTextColor(Color.BLACK);
        params.width = 280;
        params.setMargins(0, 35, 0, 0);
        itemLayout.addView(textView, params);

        suLayout.addView(itemLayout, params3);

    }

    private void addItem3(double value,String s) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 텍스트뷰 추가
        TextView textView = new TextView(this);
        textView.setText(String.valueOf(value));
        if(s.equals("초과"))
            textView.setTextColor(Color.RED);
        else
            textView.setTextColor(Color.BLACK);

        params.width = 280;
        params.setMargins(0, 35, 0, 0);
        itemLayout.addView(textView, params);

        uLayout.addView(itemLayout, params3);
    }

    private void addItem4(String value) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 텍스트뷰 추가
        TextView textView = new TextView(this);
        textView.setText(String.valueOf(value));
        textView.setTextColor(Color.BLACK);
        params.width = 25;
        params.setMargins(0, 35, 0, 0);
        itemLayout.addView(textView, params);

        slash.addView(itemLayout, params3);

    }

    private void addtxt(String value) {

        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 텍스트뷰 추가
        TextView textView = new TextView(this);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#FFAD1818"));
        params2.width = 1000;
        textView.setText(value);
        textView.setBackgroundDrawable(this.getBaseContext().getResources().getDrawable((R.drawable.bottomline)));
        params2.setMargins(27, 200, 0, 200);
        itemLayout.addView(textView, params2);

        zLayout.addView(itemLayout, params3);
    }

    private void addRecycler(RecyclerView recycler) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params2.width=1000;

        itemLayout.addView(recycler, params2);

        zLayout.addView(itemLayout, params3);
    }

    public void RegistClick(View v) { //카메라메뉴
        Intent intent=new Intent(this,CameraActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    public void ClorieClick(View v) { //설정메뉴
        Intent intent=new Intent(this,CalorieActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    public void CalendarClick(View v) { //캘린더메뉴
        Intent intent=new Intent(this,CalendarActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    public void ThemaClick(View v) { //추천레시피메뉴
        Intent intent=new Intent(this,ThemaActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    public void SettingClick(View v) { //설정메뉴
        Intent intent=new Intent(this,SettingActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    public void EditClick(View v) {
        Intent intent=new Intent(this,EditDialogActivity.class);
        intent.putExtra("Username",username);
        intent.putExtra("array",arr1);
        intent.putExtra("array2",arr2);
        intent.putExtra("array3",arr3);
        intent.putExtra("number",number);
        startActivity(intent);
    }
}