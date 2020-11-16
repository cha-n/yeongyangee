package com.example.user.a1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class SettingActivity extends AppCompatActivity {

    ArrayList<String> LarrayList = new ArrayList<String>();
    ArrayList<String> EarrayList = new ArrayList<String>();

    double ukcal=0, utan=0 , udan=0 , ugi=0 , udang=0 , una=0;
    double kcal=0, tan=0 , dan=0 , gi=0 , dang=0 , na=0;
    String username;

    int ran1=0; int ran2=0; int ran3=0;

    LinearLayout lLayout;
    LinearLayout eLayout;
    LinearLayout iLayout;
    Handler handler = new Handler();  // 외부쓰레드 에서 메인 UI화면을 그릴때 사용
    TextView user;

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
    String day= Day.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        lLayout=(LinearLayout) findViewById(R.id.lLayout);
        eLayout=(LinearLayout)findViewById(R.id.eLayout);
        iLayout=(LinearLayout)findViewById(R.id.iLayout);
        user=(TextView)findViewById(R.id.user);

        //랜덤숫자 1~15중 3개 뽑기
        ran1 = (int)(Math.random()*15+1);
        ran2 = (int)(Math.random()*15+1);
        while(ran1==ran2)
            ran2 = (int)(Math.random()*15+1);
        ran3 = (int)(Math.random()*15+1);
        while(ran1==ran3 || ran2==ran3)
            ran3 = (int)(Math.random()*15+1);

        //id 가져오기
        Intent intent = getIntent();
        username =intent.getStringExtra("Username");

        String comment=username.concat("님의 한 끼 영양성분 분석결과");
        user.setText(String.valueOf(comment));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        new SettingActivity.SungbunTask().execute();
        new SettingActivity.UserTask().execute();

    }

    public String LorE(double init, double user){
        String fd="";
        if(init>user)
            fd="부족";
        if(init<user)
            fd="과다";
        return fd;
    }

    //부족한 영양성분 채울 메뉴 이미지&성분 가져오기 - 지방
    private class giTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;

        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json = "http://117.16.244.117:2001/gi";
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
                int yes=0;
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject friend = jArray.getJSONObject(i);

                    String jungbo="";
                    int unum=friend.getInt("번호");
                    if(unum==ran1 || unum==ran2 || unum==ran3) {
                        yes=yes+1;
                        if(yes==1)
                            addtxt("부족한 지방을 위한 음식",1);
                        String uname=friend.getString("식품이름");
                        jungbo="- ";
                        jungbo=jungbo.concat(uname);
                        ukcal = friend.getDouble("열량_kcal");
                        utan = friend.getDouble("탄수화물_g");
                        udan = friend.getDouble("단백질_g");
                        ugi = friend.getDouble("지방_g");
                        udang = friend.getDouble("당류_g");
                        una = friend.getDouble("나트륨_mg");
                        jungbo=jungbo.concat("\n열량(kcal): ");
                        jungbo=jungbo.concat(String.valueOf(ukcal));
                        jungbo=jungbo.concat("\n탄수화물(g): ");
                        jungbo=jungbo.concat(String.valueOf(utan));
                        jungbo=jungbo.concat("\n단백질(g): ");
                        jungbo=jungbo.concat(String.valueOf(udan));
                        jungbo=jungbo.concat("\n지방(g): ");
                        jungbo=jungbo.concat(String.valueOf(ugi));
                        jungbo=jungbo.concat("\n당류(g): ");
                        jungbo=jungbo.concat(String.valueOf(udang));
                        jungbo=jungbo.concat("\n나트륨(mg): ");
                        jungbo=jungbo.concat(String.valueOf(una));
                        addImg(unum,"gi/",jungbo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //부족한 영양성분 채울 메뉴 이미지&성분 가져오기 - 단백질
    private class danTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;

        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json = "http://117.16.244.117:2001/dan";
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
                int yes=0;
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject friend = jArray.getJSONObject(i);

                    String jungbo="";
                    int unum=friend.getInt("번호");
                    if(unum==ran1 || unum==ran2 || unum==ran3) {
                        yes=yes+1;
                        if(yes==1)
                            addtxt("부족한 단백질을 위한 음식",1);
                        String uname=friend.getString("식품이름");
                        jungbo="- ";
                        jungbo=jungbo.concat(uname);
                        ukcal = friend.getDouble("열량_kcal");
                        utan = friend.getDouble("탄수화물_g");
                        udan = friend.getDouble("단백질_g");
                        ugi = friend.getDouble("지방_g");
                        udang = friend.getDouble("당류_g");
                        una = friend.getDouble("나트륨_mg");
                        jungbo=jungbo.concat("\n열량(kcal): ");
                        jungbo=jungbo.concat(String.valueOf(ukcal));
                        jungbo=jungbo.concat("\n탄수화물(g): ");
                        jungbo=jungbo.concat(String.valueOf(utan));
                        jungbo=jungbo.concat("\n단백질(g): ");
                        jungbo=jungbo.concat(String.valueOf(udan));
                        jungbo=jungbo.concat("\n지방(g): ");
                        jungbo=jungbo.concat(String.valueOf(ugi));
                        jungbo=jungbo.concat("\n당류(g): ");
                        jungbo=jungbo.concat(String.valueOf(udang));
                        jungbo=jungbo.concat("\n나트륨(mg): ");
                        jungbo=jungbo.concat(String.valueOf(una));
                        addImg(unum,"dan/",jungbo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //부족한 영양성분 채울 메뉴 이미지&성분 가져오기 - 탄수화물
    private class tanTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;

        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json = "http://117.16.244.117:2001/tan";
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
                int yes=0;
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject friend = jArray.getJSONObject(i);

                    String jungbo="";
                    int unum=friend.getInt("번호");
                    if(unum==ran1 || unum==ran2 || unum==ran3) {
                        yes=yes+1;
                        if(yes==1)
                            addtxt("부족한 탄수화물을 위한 음식",1);
                        String uname=friend.getString("식품이름");
                        jungbo="- ";
                        jungbo=jungbo.concat(uname);
                        ukcal = friend.getDouble("열량_kcal");
                        utan = friend.getDouble("탄수화물_g");
                        udan = friend.getDouble("단백질_g");
                        ugi = friend.getDouble("지방_g");
                        udang = friend.getDouble("당류_g");
                        una = friend.getDouble("나트륨_mg");
                        jungbo=jungbo.concat("\n열량(kcal): ");
                        jungbo=jungbo.concat(String.valueOf(ukcal));
                        jungbo=jungbo.concat("\n탄수화물(g): ");
                        jungbo=jungbo.concat(String.valueOf(utan));
                        jungbo=jungbo.concat("\n단백질(g): ");
                        jungbo=jungbo.concat(String.valueOf(udan));
                        jungbo=jungbo.concat("\n지방(g): ");
                        jungbo=jungbo.concat(String.valueOf(ugi));
                        jungbo=jungbo.concat("\n당류(g): ");
                        jungbo=jungbo.concat(String.valueOf(udang));
                        jungbo=jungbo.concat("\n나트륨(mg): ");
                        jungbo=jungbo.concat(String.valueOf(una));
                        addImg(unum,"tan/",jungbo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //사용자 영양성분 합 가져오기
    public class UserTask extends AsyncTask<Void, Void, String> {
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

                LarrayList.add(0,"부족한 영양성분");
                EarrayList.add(0,"과다한 영양성분");

                int i=1;
                int j=1;
                if(LorE(tan,utan).equals("부족"))
                {
                    LarrayList.add(i,"탄수화물");
                    i++;
                }
                else if(LorE(tan,utan).equals("과다"))
                {
                    EarrayList.add(j,"탄수화물");
                    j++;
                }
                if(LorE(dan,udan).equals("부족"))
                {
                    LarrayList.add(i,"단백질");
                    i++;
                }
                else  if(LorE(dan,udan).equals("과다"))
                {
                    EarrayList.add(j,"단백질");
                    j++;
                }
                if(LorE(gi,ugi).equals("부족"))
                {
                    LarrayList.add(i,"지방");
                    i++;
                }
                else  if(LorE(gi,ugi).equals("과다"))
                {
                    EarrayList.add(j,"지방");
                    j++;
                }
                if(LorE(dang,udang).equals("과다"))
                {
                    EarrayList.add(j,"당류");
                    j++;
                }
                if(LorE(na,una).equals("과다"))
                {
                    EarrayList.add(j,"나트륨");
                    j++;
                }
                i--;
                j--;

                if(i==0) {
                    LarrayList.add(1, "없음");
                    i++;
                }
                if(j==0) {
                    EarrayList.add(1,"없음");
                    j++;
                }

                //탄수화물, 단백질, 지방 부족한 정도
                ArrayList<Double> Lnumarray = new ArrayList<Double>();
                double rtan=tan-utan;
                double rdan=dan-udan;
                double rgi=gi-ugi;

                double tt = Double.parseDouble(String.format("%.1f",rtan));
                double dd = Double.parseDouble(String.format("%.1f",rdan));
                double gg = Double.parseDouble(String.format("%.1f",rgi));

                for(int m=1;m<LarrayList.size();m++)
                {
                    if(LarrayList.get(m).equals("탄수화물"))
                        Lnumarray.add(m-1, tt);

                    else if(LarrayList.get(m).equals("단백질"))
                        Lnumarray.add(m-1, dd);

                    else if(LarrayList.get(m).equals("지방"))
                        Lnumarray.add(m-1, gg);
                }

                //가장 부족한 순으로 정렬
                double tmp=0;
                for(int a=0;a<Lnumarray.size();a++)
                {
                    for(int b=0;b<Lnumarray.size()-1;b++)
                    {
                        if(Lnumarray.get(b)<Lnumarray.get(b+1))
                        {
                            tmp=Lnumarray.get(b);
                            Lnumarray.set(b,Lnumarray.get(b+1));
                            Lnumarray.set(b+1,tmp);
                        }
                    }
                }

                //user.setText(String.valueOf(String.format("%.1f",gg)));
                //Lnumarray.add(2,dd);

                for(int n=0;n<Lnumarray.size();n++)
                {
                    if(Lnumarray.get(n)==tt)
                        new SettingActivity.tanTask().execute();
                    if(Lnumarray.get(n)==dd)
                        new SettingActivity.danTask().execute();
                    if(Lnumarray.get(n)==gg)
                        new SettingActivity.giTask().execute();
                }

                for(int l=1;l<LarrayList.size();l++)
                {
                    if(LarrayList.get(l).equals("탄수화물")) {
                        String t = LarrayList.get(l);
                        t = t.concat("(");
                        t = t.concat(String.format("%.1f",rtan));
                        t = t.concat("↓)");

                        LarrayList.set(l, t);
                    }
                    else if(LarrayList.get(l).equals("단백질")) {
                        String d=LarrayList.get(l);
                        d=d.concat("(");
                        d=d.concat(String.format("%.1f",rdan));
                        d=d.concat("↓)");

                        LarrayList.set(l,d);
                    }
                    else if(LarrayList.get(l).equals("지방")) {
                        String g=LarrayList.get(l);
                        g=g.concat("(");
                        g=g.concat(String.format("%.1f",rgi));
                        g=g.concat("↓)");

                        LarrayList.set(l,g);
                    }
                }

                String comment="";
                for(int l=1;l<EarrayList.size();l++)
                {
                    if(EarrayList.get(l).equals("탄수화물")) {
                        String t = EarrayList.get(l);
                        comment=comment.concat("[탄수화물] ");
                        t = t.concat("(");
                        t = t.concat(String.format("%.1f",utan-tan));
                        t = t.concat("↑)");
                        EarrayList.set(l, t);
                        //new SettingActivity.tanTask().execute();}
                    }
                    else if(EarrayList.get(l).equals("단백질")) {
                        String d=EarrayList.get(l);
                        comment=comment.concat("[단백질] ");
                        d=d.concat("(");
                        d=d.concat(String.format("%.1f",udan-dan));
                        d=d.concat("↑)");

                        EarrayList.set(l,d);
                        //new SettingActivity.danTask().execute();
                    }
                    else if(EarrayList.get(l).equals("지방")) {
                        String g=EarrayList.get(l);
                        comment=comment.concat("[지방] ");
                        g=g.concat("(");
                        g=g.concat(String.format("%.1f",ugi-gi));
                        g=g.concat("↑)");

                        EarrayList.set(l,g);
                        //new SettingActivity.giTask().execute();
                    }
                    else if(EarrayList.get(l).equals("나트륨")) {
                        String n=EarrayList.get(l);
                        comment=comment.concat("[나트륨] ");
                        n=n.concat("(");
                        n=n.concat(String.format("%.1f",una-na));
                        n=n.concat("↑)");

                        EarrayList.set(l,n);
                        //new SettingActivity.giTask().execute();
                    }
                    else if(EarrayList.get(l).equals("당류")) {
                        String da=EarrayList.get(l);
                        comment=comment.concat("[당류] ");
                        da=da.concat("(");
                        da=da.concat(String.format("%.1f",udang-dang));
                        da=da.concat("↑)");

                        EarrayList.set(l,da);
                        //new SettingActivity.giTask().execute();
                    }
                }
                comment=comment.concat("의\n섭취량을 줄이세요!");
                if(!(EarrayList.get(1).equals("없음")))
                    addtxt(comment,2);
                if(LarrayList.get(1).equals("없음") && EarrayList.get(1).equals("없음"))
                {
                    comment="부족하거나 과다한 것 없이 균형있는 식사를 하셨네요!\n참 잘했어요!";
                    addtxt(comment,4);
                }

                boolean OK=true;
                for(int k=0;k<=i;k++)
                {
                    if(k>0) {
                        OK = false;
                    }
                    LaddItem(LarrayList.get(k),OK);
                }
                OK=true;
                for(int k=0;k<=j;k++)
                {
                    if(k>0)
                        OK=false;
                    EaddItem(EarrayList.get(k),OK);
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
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void LaddItem(String value,boolean OK) {
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
        if(OK) {
            textView.setTextSize(19);
            //params2.width = 600;
            params2.setMargins(-50, 30, 0, 5);
        }
        else{
            textView.setTextSize(15);
            //params2.width = 500;
            params2.setMargins(-50, 10, 0, 0);
            //textView.setTranslationX(100);
        }
        params2.width = 600;
        textView.setText(value);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        //params2.setMargins(0, 20, 0, 0);
        itemLayout.addView(textView, params2);

        lLayout.addView(itemLayout, params3);

    }

    private void EaddItem(String value, boolean OK) {
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
        if(OK) {
            textView.setTextSize(19);
            //params2.width = 600;
            params2.setMargins(-50, 30, 0, 5);
        }
        else{
            textView.setTextSize(15);
            //params2.width = 500;
            params2.setMargins(-50, 10, 0, 0);
        }
        params2.width = 600;
        textView.setText(value);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);

        //params2.setMargins(0, 20, 0, 0);
        itemLayout.addView(textView, params2);

        eLayout.addView(itemLayout, params3);
    }


    private void addtxt(String value,int what) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        if(what==1) {
            // 텍스트뷰 추가
            TextView textView = new TextView(this);
            textView.setTextSize(16);
            params2.width = 1000;
            textView.setGravity(Gravity.CENTER);
            textView.setText(value);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundDrawable(this.getBaseContext().getResources().getDrawable((R.drawable.menu)));
            params2.setMargins(80, 10, 0, 30);
            itemLayout.addView(textView, params2);
        }
        else if(what==2)
        {
            // 텍스트뷰 추가
            TextView textView = new TextView(this);
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.RED);
            params2.width = 1000;
            textView.setText(value);
            textView.setBackgroundDrawable(this.getBaseContext().getResources().getDrawable((R.drawable.feed1)));
            params2.setMargins(80, 0, 0, 15);
            itemLayout.addView(textView, params2);
        }

        else
        {
            // 텍스트뷰 추가
            TextView textView = new TextView(this);
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);
            params2.width = 1000;
            textView.setText(value);
            textView.setTextColor(Color.BLUE);
            params2.setMargins(30, 0, 0, 20);
            itemLayout.addView(textView, params2);
        }

        iLayout.addView(itemLayout, params3);
    }

    private void addImg(final int value,final String yy,String foodname) {
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

        // 이미지뷰 추가
        final ImageView iv = new ImageView(this);
        params.setMargins(90, 0, 0, 10);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                // TODO Auto-generated method stub
                try{

                    String ImageUrl="http://117.16.244.117:2001/";
                    ImageUrl=ImageUrl.concat(yy);
                    ImageUrl=ImageUrl.concat(String.valueOf(value));
                    ImageUrl=ImageUrl.concat(".jpg");

                    URL url=new URL(ImageUrl); //기본 url
                    // 걍 외우는게 좋다 -_-;
                    //   final ImageView iv = (ImageView)findViewById(R.id.Imgtan1);
                    InputStream is = url.openStream();

                    final Bitmap bm = BitmapFactory.decodeStream(is);

                    handler.post(new Runnable() {

                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            iv.setImageBitmap(bm);
                        }
                    });
                    //iv.setImageBitmap(bm); //비트맵 객체로 보여주기

                } catch(Exception e){

                }

            }
        });

        t.start();

        itemLayout.addView(iv, params);

        // 텍스트뷰 추가
        TextView textView = new TextView(this);
        textView.setTextSize(15);
        params2.width = 700;
        textView.setText(foodname);
        textView.setTextColor(Color.BLACK);
        params2.setMargins(100, 0, 0, 0);
        itemLayout.addView(textView, params2);

        iLayout.addView(itemLayout, params3);

        //iLayout.removeView(itemLayout);
    }



    public void BackClick(View v)
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
        finish();
    }

}