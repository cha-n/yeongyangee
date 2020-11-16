package com.example.user.a1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {
    CalendarView calan;
    TextView sik;
    String date ;
    String result1;

    TextView name;
    SimpleAdapter adapter;
    ListView lView;
    double amount=0;
    String user;
    String year;
    String month;
    String day;
    String hour;
    String min;

    String[] from = {"name_item"};
    int[] to = {R.id.name_item};
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashmap;



    // 현재시간을 msec 으로 구한다.
    long now = System.currentTimeMillis();
    // 현재시간을 date 변수에 저장한다.
    Date date1 = new Date(now);
    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
    SimpleDateFormat NowDate = new SimpleDateFormat("yyyy년 MM월 dd일");
    // nowDate 변수에 값을 저장한다.
    String formatDate = NowDate.format(date1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent intent
                = getIntent();
        user =intent.getStringExtra("Username");
        //user="abcd";

        long now = System.currentTimeMillis();        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat Year = new SimpleDateFormat("yyyy");        // nowDate 변수에 값을 저장한다.
        year = Year.format(date);

        SimpleDateFormat Month = new SimpleDateFormat("MM");        // nowDate 변수에 값을 저장한다.
        month = Month.format(date);

        SimpleDateFormat Day = new SimpleDateFormat("dd");        // nowDate 변수에 값을 저장한다.
        day = Day.format(date);

        SimpleDateFormat Hour = new SimpleDateFormat("HH");        // nowDate 변수에 값을 저장한다.
        hour = Hour.format(date);

        SimpleDateFormat Min = new SimpleDateFormat("mm");        // nowDate 변수에 값을 저장한다.
        min = Min.format(date);

        sik = (TextView) findViewById(R.id.sikdan);
        sik.setText(year + "년 " + month + "월 " + day + "일 "); // date에 선택된 날짜가 저장됨

        //String user="abcd";

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        new CalendarActivity.ParseTask().execute();
    }





    private class ParseTask extends AsyncTask<Void, Void, String> {
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

        protected void onPostExecute(final String strJson) {
            super.onPostExecute(strJson);


            lView = (ListView) findViewById(R.id.lvMain);
            String array[]=new String[10];
            array[0]="100";array[1]="100";array[2]="100";array[3]="100";array[4]="100";array[5]="100";array[6]="100";array[7]="100";
            array[8]="100";array[9]="100";
            int s=0;


            try {

                JSONArray jArray = new JSONArray(strJson);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject friend = jArray.getJSONObject(i);
                    String name1 = friend.getString("username");
                    String nameOS = friend.getString("foodname");
                    String nameOS2 = friend.getString("year");
                    String nameOS3 = friend.getString("month");
                    String nameOS4 = friend.getString("day");
                    String nameOS5 = friend.getString("hour");
                    String nameOS6 = friend.getString("min");
                    String nameOS7 = friend.getString("열량_kcal");

                    int abc=0;

                    if (user.equals(name1)) {
                        if (nameOS2.equals(year) && nameOS3.equals(month) && nameOS4.equals(day)) {

                            for(int j=0;j<10;j++)
                            {
                                if(array[j].equals(nameOS6)==true) //시간 중복된것.
                                {
                                    abc=1;

                                }
                            }

                            if (abc==0) {

                                array[s]=nameOS6;
                                s++;
                                hashmap = new HashMap<String, String>();
                                hashmap.put("name_item", "-" + nameOS5 + "시 " + nameOS6 + "분");
                                arrayList.add(hashmap);

                                hashmap = new HashMap<String, String>();
                                hashmap.put("name_item", "" + nameOS + "  " + nameOS7 + "kcal");
                                arrayList.add(hashmap);
                            }
                            else
                            {
                                hashmap = new HashMap<String, String>();
                                hashmap.put("name_item", "" + nameOS + "  " + nameOS7 + "kcal");
                                arrayList.add(hashmap);
                            }

                        }
                    }
                }
                adapter = new SimpleAdapter(CalendarActivity.this, arrayList, R.layout.item2, from, to);

                lView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            calan = (CalendarView) findViewById(R.id.calendarView1);
            sik = (TextView) findViewById(R.id.sikdan);
            calan.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                @Override

                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                    if(month>8) {
                        if(dayOfMonth<10)
                            date = year + "년 " + (month + 1) + "월 0" +dayOfMonth + "일 "; // date에 선택된 날짜가 저장됨
                        else
                            date = year + "년 " + (month + 1) + "월 " + dayOfMonth + "일 "; // date에 선택된 날짜가 저장됨
                        sik.setText(date);
                    }
                    else
                    {
                        if(dayOfMonth<10)
                            date = year + "년 0" + (month + 1) + "월 0" +dayOfMonth + "일 "; // date에 선택된 날짜가 저장됨
                        else
                            date = year + "년 0" + (month + 1) + "월 " + dayOfMonth + "일 "; // date에 선택된 날짜가 저장됨
                        sik.setText(date);
                    }


                    arrayList.clear();

                    String array2[]=new String[10];
                    array2[0]="100";array2[1]="100";array2[2]="100";array2[3]="100";array2[4]="100";array2[5]="100";array2[6]="100";array2[7]="100";
                    array2[8]="100";array2[9]="100";
                    int s2=0;

                    try {
                        JSONArray jArray = new JSONArray(strJson);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject friend = jArray.getJSONObject(i);
                            String name1 = friend.getString("username");
                            String nameOS = friend.getString("foodname");
                            String nameOS2 = friend.getString("year");
                            String nameOS3 = friend.getString("month");
                            String nameOS4 = friend.getString("day");
                            String nameOS5 = friend.getString("hour");
                            String nameOS6 = friend.getString("min");
                            String nameOS7 = friend.getString("열량_kcal");

                            String y = Integer.toString(year);
                            String m = Integer.toString(month + 1);
                            if(month+1<=9)
                                m="0"+m;
                            String d = Integer.toString(dayOfMonth);
                            if (dayOfMonth < 10)
                                d = "0" + d;

                            int abc2=0;


                            if (user.equals(name1)) {
                                if (nameOS2.equals(y)) {
                                    if (nameOS3.equals(m)) {
                                        if (nameOS4.equals(d)) {

                                            for(int j=0;j<10;j++)
                                            {
                                                if(array2[j].equals(nameOS6)==true) //시간 중복된것.
                                                {
                                                    abc2=1;

                                                }
                                            }

                                            if (abc2==0) {

                                                array2[s2]=nameOS6;
                                                s2++;
                                                hashmap = new HashMap<String, String>();
                                                hashmap.put("name_item", "-" + nameOS5 + "시 " + nameOS6 + "분");
                                                arrayList.add(hashmap);

                                                hashmap = new HashMap<String, String>();
                                                hashmap.put("name_item", "" + nameOS + "  " + nameOS7 + "kcal");
                                                arrayList.add(hashmap);
                                            }
                                            else
                                            {
                                                hashmap = new HashMap<String, String>();
                                                hashmap.put("name_item", "" + nameOS + "  " + nameOS7 + "kcal");
                                                arrayList.add(hashmap);
                                            }

                                            /*hashmap = new HashMap<String, String>();
                                            hashmap.put("name_item", "" + nameOS5 + "시 " + nameOS6 + "분");
                                            arrayList.add(hashmap);

                                            hashmap = new HashMap<String, String>();
                                            hashmap.put("name_item", "" + nameOS + "  " + nameOS7 + "kcal");
                                            arrayList.add(hashmap);*/
                                        }
                                    }

                                }
                            }
                        }
                        adapter = new SimpleAdapter(CalendarActivity.this, arrayList, R.layout.item2, from, to);
                        lView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }

            });







        }

    }




    public void BackClick(View v)
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("Username",user);
        startActivity(intent);
        finish();
    }
}