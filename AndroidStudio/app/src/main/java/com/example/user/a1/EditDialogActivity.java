package com.example.user.a1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EditDialogActivity extends AppCompatActivity {

    String username;
    String arr1[]; //foodname
    String arr2[]; //hour
    String arr3[]; //min

    int number;
    TextView textViewResult1;
    TextView textViewResult0;
    SimpleAdapter adapter;
    ListView lView;
    Food food;
    String OK="ok";
    int POSITION;


    String foodname;
    String hour;
    String min;

    static String strJson = "";

    String[] from = {"name_item"};
    int[] to = {R.id.name_item};
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashmap;


    long now = System.currentTimeMillis();
    // 현재시간을 date 변수에 저장한다.
    Date date = new Date(now);

    SimpleDateFormat Month = new SimpleDateFormat("MM");        // nowDate 변수에 값을 저장한다.
    String month = Month.format(date);

    SimpleDateFormat Day = new SimpleDateFormat("dd");        // nowDate 변수에 값을 저장한다.
    String day = Day.format(date);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dialog);

        arr1 = new String[15];
        arr2 = new String[15];
        arr3 = new String[15];


        Intent intent
                = getIntent();
        username = intent.getStringExtra("Username");
        arr1 = intent.getStringArrayExtra("array");
        arr2 = intent.getStringArrayExtra("array2");
        arr3 = intent.getStringArrayExtra("array3");

        number = intent.getIntExtra("number", 0);
        lView = findViewById(R.id.listview1);
        textViewResult0 = findViewById(R.id.textViewResult0);
        textViewResult1 = findViewById(R.id.textViewResult1);

        if(number==-1)
        {
            lView=findViewById(R.id.listview1);
            lView.setVisibility(View.GONE);
            TextView nofood=findViewById(R.id.nofood);
            Button finish1=findViewById(R.id.finish);
            Button reset1=findViewById(R.id.reset);
            ImageView underline=findViewById(R.id.underline);

            nofood.setVisibility(View.VISIBLE);
            underline.setVisibility(View.VISIBLE);
            finish1.setVisibility(View.GONE);
            reset1.setVisibility(View.GONE);
        }

        else {


            for (int i = 0; i <= number; i++) {
                hashmap = new HashMap<String, String>();
                hashmap.put("name_item", arr2[i] + "시 " + arr3[i] + "분 : " + arr1[i]);
                arrayList.add(hashmap);
            }

            adapter = new SimpleAdapter(EditDialogActivity.this, arrayList, R.layout.itemsedit, from, to);

            lView.setAdapter(adapter);
        }


        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                POSITION=position;
                textViewResult1.setText("no");
                String a = (adapter.getItem(position)).toString();
                int len = a.length();
                foodname = a.substring(21, len - 1);
                hour = a.substring(11, 13);
                min = a.substring(15, 17);
                textViewResult0.setText(foodname);

                DialogActivity customDialog = new DialogActivity(EditDialogActivity.this);
                customDialog.callFunction(textViewResult0, textViewResult1);



               /* lView.setAdapter(null);
                arrayList.remove(POSITION);
                adapter = new SimpleAdapter(EditDialogActivity.this, arrayList, R.layout.itemsedit, from, to);
                lView.setAdapter(adapter);
                adapter.notifyDataSetChanged();*/
            }
        });


        textViewResult1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().equals(OK)) {

                    lView.setAdapter(null);
                    arrayList.remove(POSITION);
                    adapter = new SimpleAdapter(EditDialogActivity.this, arrayList, R.layout.itemsedit, from, to);
                    lView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    number--;

                    HttpAsyncTask httpTask = new HttpAsyncTask(EditDialogActivity.this);
                    httpTask.execute("http://117.16.244.117:2001/cal/delete",username,hour, min, foodname);

                    if(number==-1)
                    {
                        lView.setVisibility(View.GONE);
                        TextView nofood=findViewById(R.id.nofood);
                        Button finish1=findViewById(R.id.finish);
                        Button reset1=findViewById(R.id.reset);
                        ImageView underline=findViewById(R.id.underline);

                        underline.setVisibility(View.VISIBLE);
                        nofood.setVisibility(View.VISIBLE);
                        finish1.setVisibility(View.GONE);
                        reset1.setVisibility(View.GONE);
                    }

                }
                // }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private EditDialogActivity EditAct;

        HttpAsyncTask(EditDialogActivity editDialogActivity) {
            this.EditAct = editDialogActivity;
        }
        @Override
        protected String doInBackground(String... urls) {

            food = new Food();
            food.setID(urls[1]);
            food.setHour(urls[2]);
            food.setMin(urls[3]);
            food.setFoodname(urls[4]);

            return POST(urls[0], food);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            EditAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EditAct, foodname+"를(을) 삭제했습니다.", Toast.LENGTH_LONG).show();
                    try {
                        JSONArray json = new JSONArray(strJson);
                        // EditAct.result.setText(json.toString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }


    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {

        private EditDialogActivity EditAct;

        HttpAsyncTask2(EditDialogActivity editDialogActivity) {
            this.EditAct = editDialogActivity;
        }
        @Override
        protected String doInBackground(String... urls) {

            food = new Food();
            food.setID(urls[1]);
            food.setMonth(urls[2]);
            food.setDay(urls[3]);

            return POST2(urls[0], food);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            EditAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EditAct, "초기화하였습니다.", Toast.LENGTH_LONG).show();
                    try {
                        JSONArray json = new JSONArray(strJson);
                        //  EditAct.result.setText(json.toString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }


    public static String POST(String url, Food food){
        InputStream is = null;
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username", food.getID());
            jsonObject.accumulate("hour", food.getHour());
            jsonObject.accumulate("min", food.getMin());
            jsonObject.accumulate("foodname", food.getFoodname());

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            byte[] source=json.getBytes("UTF-8");
            String remake=new String(source,"UTF-8");

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(food);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            OutputStream os = httpCon.getOutputStream();
            os.write(remake.getBytes("UTF-8"));
            os.flush();
            // receive response as inputStream
            try {
                is = httpCon.getInputStream();
                // convert inputstream to string
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work!";
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }


    public static String POST2(String url, Food food){
        InputStream is = null;
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username", food.getID());
            jsonObject.accumulate("month", food.getMonth());
            jsonObject.accumulate("day", food.getDay());

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            byte[] source=json.getBytes("UTF-8");
            String remake=new String(source,"UTF-8");

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(food);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            OutputStream os = httpCon.getOutputStream();
            os.write(remake.getBytes("UTF-8"));
            os.flush();
            // receive response as inputStream
            try {
                is = httpCon.getInputStream();
                // convert inputstream to string
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work!";
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public void FinishClick(View v)
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
        finish();

    }

    public void ResetClick(View v)
    {
        HttpAsyncTask2 httpTask2 = new HttpAsyncTask2(EditDialogActivity.this);
        httpTask2.execute("http://117.16.244.117:2001/cal/reset",username,month,day);
        lView=findViewById(R.id.listview1);
        lView.setVisibility(View.GONE);
        TextView nofood=findViewById(R.id.nofood);
        Button finish1=findViewById(R.id.finish);
        Button reset1=findViewById(R.id.reset);
        ImageView underline=findViewById(R.id.underline);

        underline.setVisibility(View.VISIBLE);
        nofood.setVisibility(View.VISIBLE);
        finish1.setVisibility(View.GONE);
        reset1.setVisibility(View.GONE);
        // Toast.makeText(EditDialogActivity.this, "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
    }

    public void BackClick(View v)
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
        finish();
    }


}
