package com.example.user.a1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity{ //implements View.OnClickListener{

    String name,age,gender;

    Person person;
    Person person2;
    static    String strJson = "";
    TextView tvIsConnected, tvResponse;

    EditText ID1;
    EditText PASS;

    String ID;
    String password;
    EditText ID2;
    EditText PASS2;

    //네이버 로그인
    private static String OAUTH_CLIENT_ID = "clientID";
    private static String OAUTH_CLIETN_SECRET="secret";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    public static OAuthLoginButton mOAuthLoginButton;
    public static OAuthLogin mOAuthLoginInstance;

    public static Context mContext;

    //public static boolean login_success;
    static String accessToken;
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler(){
        @Override
        public void run(boolean success){
            //로그인 인증 성공
            if(success){
                //사용자 정보 가져오기
                accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
                new NaverProfileGet().execute();
            }
            else{
                //로그인 인증 실패
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext,"errorCode:"+errorCode + ", errorDesc:"+errorDesc,Toast.LENGTH_SHORT).show();
            }
        }

    };
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //네이버 로그인
        //cntext 저장
        mContext = LoginActivity.this;

        //초기화
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIETN_SECRET,OAUTH_CLIENT_NAME);

        //로그인 버튼 셋팅
        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

        //커스텀의 경우 수동으로 로그인 기능 사용하기
        //mOAuthLoginInstance.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
        //

        Button button1=(Button)findViewById(R.id.button);
        Button login=(Button)findViewById(R.id.login);
        ID2=(EditText)findViewById(R.id.editText);
        PASS2=(EditText)findViewById(R.id.editText2);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        ID1=(EditText)findViewById(R.id.editText);
        PASS=(EditText)findViewById(R.id.editText2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ID=ID1.getText().toString();
                password=PASS.getText().toString();

                if (ID1.getText().toString().length() == 0)
                    Toast.makeText(LoginActivity.this, "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
                else if (PASS.getText().toString().length() == 0)
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                else {
                    LoginActivity.HttpAsyncTasks httpTask = new HttpAsyncTasks(LoginActivity.this);

                    httpTask.execute("http://117.16.244.117:2001/auth/login",ID,password);

                    //LoginActivity.HttpAsyncTasks httpTask1 = new HttpAsyncTasks(LoginActivity.this);
                    //httpTask1.execute("http://117.16.244.117:2001/main",ID,password);

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("Username",ID);

                    startActivity(intent);
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }

        });
    }

    private class HttpAsyncTasks extends AsyncTask<String, Void, String> {

        private LoginActivity LoginAct;

        HttpAsyncTasks(LoginActivity loginActivity) {
            this.LoginAct = loginActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            person2 = new Person();
            person2.setID(urls[1]);
            person2.setPass(urls[2]);

            return POST2(urls[0],person2);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            LoginAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(LoginAct, "Received!", Toast.LENGTH_LONG).show();
                    try {
                        JSONArray json = new JSONArray(strJson);
                        LoginAct.tvResponse.setText(json.toString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
    public static String POST2(String url, Person person){
        InputStream is = null;
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username", person.getID());
            jsonObject.accumulate("password", person.getPass());

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            byte[] source=json.getBytes("UTF-8");
            String remake=new String(source,"UTF-8");

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


    //네이버 프로필 조회 API
    public class NaverProfileGet extends AsyncTask<String, Void, String> {
        //네이버 프로필 조회 API에 보낼 헤더. 그대로 쓰면 된다.
        String header = "Bearer " + accessToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            //네이버 프로필 조회 API에서 프로필을 jSON 형식으로 받아오는 부분.
            //이 부분도 그대로 사용하면 된다.
            StringBuffer response = new StringBuffer();
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", header);
                int responseCode = conn.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response.toString();
        }

        //네이버 프로필 조회 API에서 받은 jSON에서 원하는 데이터를 뽑아내는 부분
        //여기서는 닉네임, 프로필사진 주소, 이메일을 얻어오지만, 다른 값도 얻어올 수 있다.
        //이 부분을 원하는 대로 수정하면 된다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Toast.makeText(LoginActivity.this,"Received!", Toast.LENGTH_LONG).show();
            try {
                //Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                //Intent intent2= new Intent(LoginActivity.this,MainActivity.class);
                JSONObject jsonObject1 = new JSONObject(result);
                JSONObject jsonObject2 = (JSONObject) jsonObject1.get("response");

                //String image = jsonObject2.getString("profile_image");

                name = jsonObject2.getString("name");
                age = jsonObject2.getString("age");
                gender = jsonObject2.getString("gender");

                age = age.split("-")[0];

                 if(gender.equals("F"))
                    gender="f";
                 else
                     gender="m";

                password=name;
                ID=name;

                LoginActivity.HttpAsyncTask httpTask = new HttpAsyncTask(LoginActivity.this);
                httpTask.execute("http://117.16.244.117:2001/auth/register", name,ID,password, age, gender);

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("Username",ID);
                intent.putExtra("Gender",gender);

                startActivity(intent);
                //String name = jsonObject2.getString("name");
                //String age = jsonObject2.getString("age");
                //String gender = jsonObject2.getString("gender");
                //String email = jsonObject2.getString("email");

                //intent.putExtra("Nickname", name);
                //intent.putExtra("Age", age);
                //intent.putExtra("Gender", gender);
                //intent.putExtra("Image", image);
                //intent.putExtra("Email", email);
                //startActivity(intent);

                //startActivity(intent2);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

        public class JSONTask extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("user_id", "androidTest");

                jsonObject.accumulate("name", "yun");


                HttpURLConnection con = null;

                BufferedReader reader = null;


                try {

                    //URL url = new URL("http://192.168.25.16:3000/users");

                    URL url = new URL(urls[0]);//url을 가져온다.

                    con = (HttpURLConnection) url.openConnection();

                    con.connect();//연결 수행


                    //입력 스트림 생성

                    InputStream stream = con.getInputStream();


                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.

                    reader = new BufferedReader(new InputStreamReader(stream));


                    //실제 데이터를 받는곳

                    StringBuffer buffer = new StringBuffer();


                    //line별 스트링을 받기 위한 temp 변수

                    String line = "";


                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.

                    while ((line = reader.readLine()) != null) {

                        buffer.append(line);

                    }


                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까

                    return buffer.toString();


                    //아래는 예외처리 부분이다.

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                } finally {

                    //종료가 되면 disconnect메소드를 호출한다.

                    if (con != null) {

                        con.disconnect();

                    }

                    try {

                        //버퍼를 닫아준다.

                        if (reader != null) {

                            reader.close();

                        }

                    } catch (IOException e) {

                        e.printStackTrace();

                    }

                }//finally 부분

            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;

        }


        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            //tvData.setText(result);

            if(result=="OK") //액티비티 변경함
            {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }

        }

    }


    //로그인 정보 보내기
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
            jsonObject.accumulate("password", person.getPass());
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private LoginActivity mainAct;

        HttpAsyncTask(LoginActivity mainActivity) {
            this.mainAct = mainActivity;
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
            mainAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(mainAct, "Received!", Toast.LENGTH_LONG).show();
                    try {
                        JSONArray json = new JSONArray(strJson);
                        mainAct.tvResponse.setText(json.toString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private boolean validate(){
        if(name.trim().equals(""))
            return false;
        else if(ID.trim().equals(""))
            return false;
        else if(password.trim().equals(""))
            return false;
        else if(age.trim().equals(""))
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

/*
    public void LoginClick(View v) {

        if (id1.getText().toString().length() == 0)
            Toast.makeText(LoginActivity.this, "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
        else if (pass.getText().toString().length() == 0)
            Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
        else {

            LoginActivity.HttpAsyncTask httpTask = new HttpAsyncTask(LoginActivity.this);
            httpTask.execute("http://117.16.244.117:2001/auth/login", id1.getText().toString(), pass.getText().toString());

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }
*/
}
