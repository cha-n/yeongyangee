package com.example.user.a1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.Date;

public class AddFoodActivity extends AppCompatActivity {

    String username;
    EditText CAL;
    EditText TAN;
    EditText DAN;
    EditText GI;
    EditText DANG;
    EditText NA;
    EditText FOODNAME;
    TextView array;


    String Kcal;
    String Tan;
    String Dan;
    String Gi;
    String Dang;
    String Na;
    String foodname;

    Food food;

    static int number;
    static String strJson = "";
    static String arr1[];
    static String arr2[];
    static String arr3[];
    static String arr4[];
    static String arr5[];
    static String arr6[];
    static String arr7[];


    long now = System.currentTimeMillis();        // 현재시간을 date 변수에 저장한다.
    Date date = new Date(now);        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
    SimpleDateFormat Year = new SimpleDateFormat("yyyy");        // nowDate 변수에 값을 저장한다.
    String year = Year.format(date);

    SimpleDateFormat Month = new SimpleDateFormat("MM");        // nowDate 변수에 값을 저장한다.
    String month = Month.format(date);

    SimpleDateFormat Day = new SimpleDateFormat("dd");        // nowDate 변수에 값을 저장한다.
    String day = Day.format(date);

    SimpleDateFormat Hour = new SimpleDateFormat("HH");        // nowDate 변수에 값을 저장한다.
    String hour = Hour.format(date);

    SimpleDateFormat Min = new SimpleDateFormat("mm");        // nowDate 변수에 값을 저장한다.
    String min = Min.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        //id 가져오기
        Intent intent
                = getIntent();
        username =intent.getStringExtra("Username");

        arr1=new String[15];
        arr2=new String[15];
        arr3=new String[15];
        arr4=new String[15];
        arr5=new String[15];
        arr6=new String[15];
        arr7=new String[15];

        number=-1;

        array=findViewById(R.id.array);



    }

    public void okClick1(View v)
    {
        CAL =findViewById(R.id.cal);
        TAN =findViewById(R.id.tan);
        DAN =findViewById(R.id.dan);
        GI =findViewById(R.id.gi);
        DANG =findViewById(R.id.dang);
        NA =findViewById(R.id.na);
        FOODNAME =findViewById(R.id.foodname);


        if (FOODNAME.getText().toString().length() == 0)
            Toast.makeText(AddFoodActivity.this, "음식이름을 입력하세요.", Toast.LENGTH_LONG).show();
        else if (CAL.getText().toString().length() == 0)
            Toast.makeText(AddFoodActivity.this, "칼로리를 입력하세요.", Toast.LENGTH_LONG).show();
        else if (TAN.getText().toString().length() == 0)
            Toast.makeText(AddFoodActivity.this, "탄수화물을 입력하세요.", Toast.LENGTH_LONG).show();
        else if (DAN.getText().toString().length() == 0)
            Toast.makeText(AddFoodActivity.this, "단백질을 입력하세요.", Toast.LENGTH_LONG).show();
        else if (GI.getText().toString().length() == 0)
            Toast.makeText(AddFoodActivity.this, "지방을 입력하세요.", Toast.LENGTH_LONG).show();
        else if (DANG.getText().toString().length() == 0)
            Toast.makeText(AddFoodActivity.this, "당류를 입력하세요.", Toast.LENGTH_LONG).show();
        else if (NA.getText().toString().length() == 0)
            Toast.makeText(AddFoodActivity.this, "나트륨을 입력하세요.", Toast.LENGTH_LONG).show();

        else //array에 넣기
        {
            foodname = FOODNAME.getText().toString();
            Kcal = CAL.getText().toString();
            Tan = TAN.getText().toString();
            Dan = DAN.getText().toString();
            Gi = GI.getText().toString();
            Dang = DANG.getText().toString();
            Na = NA.getText().toString();
            number=number+1;
            arr1[number]= Kcal;
            arr2[number]= Tan;
            arr3[number]= Dan;
            arr4[number]= Gi;
            arr5[number]= Dang;
            arr6[number]= Na;
            arr7[number]= foodname;

            Toast.makeText(AddFoodActivity.this, foodname +"을(를) 등록했습니다.", Toast.LENGTH_LONG).show();
            CAL.setText("");
            TAN.setText("");
            DAN.setText("");
            GI.setText("");
            DANG.setText("");
            NA.setText("");
            FOODNAME.setText("");

            //result.setText(arr1[number]+" "+arr2[number]+" "+arr3[number]+" "+arr4[number]+" "+arr5[number]+" "+arr6[number]+" "+arr7[number]);
        }

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    //로그인정보 보내기
    public static String POST(String url, Food food) {
        InputStream is = null;
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) urlCon.openConnection();

            String json = "";
            JSONObject employeeInfo = new JSONObject();
            JSONArray list = new JSONArray();
            int number2=number;
            // build jsonObject
            for (int i = 0; i <= number2; i++) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("username", food.getID());

                jsonObject.accumulate("foodname", arr7[i]);
                jsonObject.accumulate("Kcal", arr1[i]);
                jsonObject.accumulate("Tan", arr2[i]);
                jsonObject.accumulate("Dan", arr3[i]);
                jsonObject.accumulate("Gi", arr4[i]);
                jsonObject.accumulate("Dang", arr5[i]);
                jsonObject.accumulate("Na", arr6[i]);

                jsonObject.accumulate("year", food.getYear());
                jsonObject.accumulate("month", food.getMonth());
                jsonObject.accumulate("day", food.getDay());
                jsonObject.accumulate("hour", food.getHour());
                jsonObject.accumulate("min", food.getMin());
                list.put(jsonObject);

            }
            employeeInfo.accumulate("test", list);






            // convert JSONObject to JSON to String
            json = employeeInfo.toString();




            byte[] source = json.getBytes("UTF-8");
            String remake = new String(source, "UTF-8");

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
                if (is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work!";
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpCon.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private AddFoodActivity addAct;

        HttpAsyncTask(AddFoodActivity addfoodActivity) {
            this.addAct = addfoodActivity;
        }
        @Override
        protected String doInBackground(String... urls) {

            food = new Food();
            food.setID(urls[1]);
            food.setFoodname(urls[2]);
            food.setCal(urls[3]);
            food.setTan(urls[4]);
            food.setDan(urls[5]);
            food.setGi(urls[6]);
            food.setDang(urls[7]);
            food.setNa(urls[8]);
            food.setYear(urls[9]);
            food.setMonth(urls[10]);
            food.setDay(urls[11]);
            food.setHour(urls[12]);
            food.setMin(urls[13]);

            return POST(urls[0],food);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            addAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // Toast.makeText(addAct, "Received!", Toast.LENGTH_LONG).show();
                    try {
                        JSONArray json = new JSONArray(strJson);
                        addAct.array.setText(json.toString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }





    public void okClick2(View v) //jsonarray 전송
    {
        AddFoodActivity.HttpAsyncTask httpTask = new AddFoodActivity.HttpAsyncTask(AddFoodActivity.this);
        httpTask.execute("http://117.16.244.117:2001/cal/post/my_food",username, foodname, Kcal, Tan, Dan, Gi, Dang, Na,year,month,day,hour,min);
        // Intent intent=new Intent(this,FoodResultActivity.class);
        // intent.putExtra("arraylist", arr);
        //  startActivity(intent);
        Intent intent=new Intent(AddFoodActivity.this,MainActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
        finish();
    }

    public void BackClick(View v)
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
        finish();
    }
}
