package com.example.user.a1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name;
    EditText id;
    EditText password;
    EditText password2;
    EditText age;

    String gender;

    Button man;
    Button woman;
    Button btnRegist;

    TextView tvIsConnected, tvResponse;
    Person person;
    static String strJson = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button cancel = (Button) findViewById(R.id.cancel);
        //Button register = (Button) findViewById(R.id.register);

        //tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        //tvResponse = (TextView) findViewById(R.id.tvResponse);

        name=(EditText)findViewById(R.id.name);
        id = (EditText) findViewById(R.id.editText1);
        password = (EditText) findViewById(R.id.editText2);
        password2 = (EditText) findViewById(R.id.editText3);
        age = (EditText) findViewById(R.id.editText4);
        man = (Button) findViewById(R.id.manbutton);
        woman = (Button) findViewById(R.id.womanbutton);
        btnRegist = (Button) findViewById(R.id.register);

        // check if you are connected or not
        /*if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        }
        else{
            tvIsConnected.setText("You are NOT conncted");
        }*/

        // add click listener to Button "POST"
        btnRegist.setOnClickListener(this);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //로그인정보 보내기
    public static String POST(String url, Person person){
        InputStream is = null;
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", person.getName());
            jsonObject.accumulate("username", person.getID());
            jsonObject.accumulate("passward", person.getPass());
            jsonObject.accumulate("age", person.getAge());
            jsonObject.accumulate("gender", person.getGender());

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

    /*
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    */

    @Override
    public void onClick(View view) {

        if (id.getText().toString().length() == 0)
            Toast.makeText(RegisterActivity.this, "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
        else if (password.getText().toString().length() == 0)
            Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
        else if(age.getText().toString().length() == 0)
            Toast.makeText(RegisterActivity.this, "나이를 입력하세요.", Toast.LENGTH_LONG).show();
        else if (password2.getText().toString().length() == 0)
            Toast.makeText(RegisterActivity.this, "비밀번호를 한번 더 입력하세요.", Toast.LENGTH_LONG).show();
        else if (password.getText().toString().equals(password2.getText().toString()) == false) {
            Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
        } else {
            switch(view.getId()){
                case R.id.register:
                    if(!validate())
                        Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                    else {
                        int Iage = Integer.parseInt(age.getText().toString());
                        String age2;
                        Iage= (Iage/10) * 10;
                        //age2=Integer.toString(Iage);
                        age.setText(Integer.toString(Iage));
                        // call AsynTask to perform network operation on separate thread
                        HttpAsyncTask httpTask = new HttpAsyncTask(RegisterActivity.this);
                        //httpTask.execute("http://117.16.244.117:2001/auth/login", id.getText().toString(),password.getText().toString(),name.getText().toString(), age.getText().toString(), gender);
                        httpTask.execute("http://117.16.244.117:2001/auth/register", id.getText().toString(),password.getText().toString(),name.getText().toString(), age.getText().toString(), gender);
                    }
                    break;
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }



    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private RegisterActivity registerAct;

        HttpAsyncTask(RegisterActivity registerActivity) {
            this.registerAct = registerActivity;
        }
        @Override
        protected String doInBackground(String... urls) {

            person = new Person();
            person.setName(urls[1]);
            person.setID(urls[2]);
            person.setPass(urls[3]);
            person.setAge(urls[4]);
            person.setGender(urls[5]);

            return POST(urls[0],person);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            registerAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(registerAct, "Received!", Toast.LENGTH_LONG).show();
                    try {
                        JSONArray json = new JSONArray(strJson);
                        registerAct.tvResponse.setText(json.toString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private boolean validate(){
        if(name.getText().toString().trim().equals(""))
            return false;
        else if(id.getText().toString().trim().equals(""))
            return false;
        else if(password.getText().toString().trim().equals(""))
            return false;
        else if(age.getText().toString().trim().equals(""))
            return false;
        else if(gender.trim().equals(""))
            return false;
        else
            return true;
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

    public void ManClick(View v) {
        woman.setBackgroundColor(Color.parseColor("#88A95A"));
        man.setBackgroundColor(Color.parseColor("#4E6C24")); //검정색
        //gender.setText("man");
        gender="m";
    }

    public void WomanClick(View v) {
        man.setBackgroundColor(Color.parseColor("#88A95A"));
        woman.setBackgroundColor(Color.parseColor("#4E6C24")); //검정색
        //gender.setText("woman");
        gender="f";
    }

/*
    public void RegisterClick(View v) {
        if (id.getText().toString().length() == 0)
            Toast.makeText(RegisterActivity.this, "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
        else if (password.getText().toString().length() == 0)
            Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
        else if(age.getText().toString().length() == 0)
            Toast.makeText(RegisterActivity.this, "나이를 입력하세요.", Toast.LENGTH_LONG).show();
        else if (password2.getText().toString().length() == 0)
            Toast.makeText(RegisterActivity.this, "비밀번호를 한번 더 입력하세요.", Toast.LENGTH_LONG).show();
        else if (password.getText().toString().equals(password2.getText().toString()) == false) {
            Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }
*/

}
