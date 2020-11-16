package com.example.user.a1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private static final int INPUT_SIZE = 299;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128;
    private static final String INPUT_NAME = "Mul";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/food_stripped_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/food_labels.txt";

    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView textViewResult;
    private TextView textViewResult2;

    private Button btnDetectObject;
    private ImageView imageViewResult;
    private CameraView cameraView;
    ProgressBar progressBar;

    Button btnOK;
    ArrayList<String> arr = new ArrayList<String>();
    String okno="ok";
    Person person;
    static String strJson = "";
    String username;
    String foodname;
    String RealResult;
    static int number;
    static String arr1[];


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
        setContentView(R.layout.activity_camera);
        cameraView = (CameraView) findViewById(R.id.cameraView);
        imageViewResult = (ImageView) findViewById(R.id.imageViewResult);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        textViewResult2=(TextView)findViewById(R.id.nnum);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);

        Intent intent = getIntent();
        username=intent.getStringExtra("Username");
        arr1=new String[10];
        number=-1;


        btnOK = (Button) findViewById(R.id.btnOK);
        textViewResult.setMovementMethod(new ScrollingMovementMethod());

        btnDetectObject = (Button) findViewById(R.id.btnDetectObject);

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                Bitmap bitmap = cameraKitImage.getBitmap();

                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

                imageViewResult.setImageBitmap(bitmap);

                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

                RealResult = results.toString();
                RealResult = RealResult.substring(1, RealResult.length() - 1);


                if (RealResult.equals("albap")) {
                    RealResult = "알밥";
                    arr.add(RealResult);
                } else if (RealResult.equals("baechu kimchi")) {
                    RealResult = "배추김치";
                    arr.add(RealResult);
                } else if (RealResult.equals("carbonara")) {
                    RealResult = "까르보나라";
                    arr.add(RealResult);
                } else if (RealResult.equals("chicken")) {
                    RealResult = "치킨";
                    arr.add(RealResult);
                } else if (RealResult.equals("choco cake")) {
                    RealResult = "초코케익";
                    arr.add(RealResult);
                } else if (RealResult.equals("curry")) {
                    RealResult = "카레라이스";
                    arr.add(RealResult);
                } else if (RealResult.equals("fried dumpling")) {
                    RealResult = "튀김만두";
                    arr.add(RealResult);
                } else if (RealResult.equals("fried egg")) {
                    RealResult = "계란후라이";
                    arr.add(RealResult);
                } else if (RealResult.equals("fried potato")) {
                    RealResult = "감자튀김";
                    arr.add(RealResult);
                } else if (RealResult.equals("baechu kimchi")) {
                    RealResult = "배추김치";
                    arr.add(RealResult);
                } else if (RealResult.equals("fried shrimp")) {
                    RealResult = "새우튀김";
                    arr.add(RealResult);
                } else if (RealResult.equals("galbigui")) {
                    RealResult = "갈비구이";
                    arr.add(RealResult);
                } else if (RealResult.equals("galchigui")) {
                    RealResult = "갈치구이";
                    arr.add(RealResult);
                } else if (RealResult.equals("gimbap")) {
                    RealResult = "김밥";
                    arr.add(RealResult);
                } else if (RealResult.equals("haemul jjim")) {
                    RealResult = "해물찜";
                    arr.add(RealResult);
                } else if (RealResult.equals("hamburger")) {
                    RealResult = "햄버거";
                    arr.add(RealResult);
                } else if (RealResult.equals("jajangmyeon")) {
                    RealResult = "짜장면";
                    arr.add(RealResult);
                } else if (RealResult.equals("jeyuk bokkeum")) {
                    RealResult = "제육볶음";
                    arr.add(RealResult);
                } else if (RealResult.equals("jjamppong")) {
                    RealResult = "짬뽕";
                    arr.add(RealResult);
                } else if (RealResult.equals("jjim dak")) {
                    RealResult = "찜닭";
                    arr.add(RealResult);
                } else if (RealResult.equals("kongjaban")) {
                    RealResult = "콩자반";
                    arr.add(RealResult);
                } else if (RealResult.equals("mukbap")) {
                    RealResult = "묵밥";
                    arr.add(RealResult);
                } else if (RealResult.equals("onion ring")) {
                    RealResult = "양파링";
                    arr.add(RealResult);
                } else if (RealResult.equals("pancake")) {
                    RealResult = "팬케이크";
                    arr.add(RealResult);
                } else if (RealResult.equals("perilla leaf")) {
                    RealResult = "깻잎장아찌";
                    arr.add(RealResult);
                } else if (RealResult.equals("pizza")) {
                    RealResult = "피자";
                    arr.add(RealResult);
                } else if (RealResult.equals("ramen")) {
                    RealResult = "라멘";
                    arr.add(RealResult);
                } else if (RealResult.equals("ramyeon")) {
                    RealResult = "라면";
                    arr.add(RealResult);
                } else if (RealResult.equals("risotto")) {
                    RealResult = "리조또";
                    arr.add(RealResult);
                } else if (RealResult.equals("samgyetang")) {
                    RealResult = "삼계탕";
                    arr.add(RealResult);
                } else if (RealResult.equals("sausage")) {
                    RealResult = "소세지볶음";
                    arr.add(RealResult);
                } else if (RealResult.equals("sundae")) {
                    RealResult = "순대";
                    arr.add(RealResult);
                } else if (RealResult.equals("tiramisu")) {
                    RealResult = "티라미수";
                    arr.add(RealResult);
                } else if (RealResult.equals("tteokguk")) {
                    RealResult = "떡국";
                    arr.add(RealResult);
                } else if (RealResult.equals("uboochobap")) {
                    RealResult = "유부초밥";
                    arr.add(RealResult);
                } else if (RealResult.equals("waffle")) {
                    RealResult = "와플";
                    arr.add(RealResult);
                } else if (RealResult.equals("white rice")) {
                    RealResult = "쌀밥";
                    arr.add(RealResult);
                } else if (RealResult.equals("yakhwa")) {
                    RealResult = "약과";
                    arr.add(RealResult);
                } else if (RealResult.equals("yukhoe")) {
                    RealResult = "육회";
                    arr.add(RealResult);
                } else {
                    RealResult = "다시시도";
                }
                foodname=RealResult;

                imageViewResult.setVisibility(View.VISIBLE);
                textViewResult.setVisibility(View.VISIBLE);


                progressBar.setVisibility(View.GONE);
                textViewResult.setText(RealResult);


                if (RealResult.equals("다시시도")!=true) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }

                    // CustomDialogActivity customDialog = new CustomDialogActivity(CameraActivity.this);

                    NewDialogActivity customDialog = new NewDialogActivity(CameraActivity.this);
                    customDialog.callFunction(textViewResult,textViewResult2);



                }
            }


            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });


        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewResult.setVisibility(View.GONE);
                textViewResult.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(90,179,90),PorterDuff.Mode.MULTIPLY);

                cameraView.captureImage();
                textViewResult2.setText("no");
            }
        });



        initTensorFlowAndLoadModel();



        textViewResult2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // okno=textViewResult2.toString();
                //   if(okno.equals("ok"))
                //     textViewResult2.setText("abc");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().equals(okno)) {
                    number=number+1;
                    String abcd=Integer.toString(number);
                    textViewResult2.setText(abcd);
                    arr1[number]=foodname;

                    //CameraActivity.HttpAsyncTask httpTask = new CameraActivity.HttpAsyncTask(CameraActivity.this);
                    //httpTask.execute("http://117.16.244.117:2001/cal/post", username, year, month, day, hour, min, foodname);
                }
                // }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // if(textViewResult2.toString().equals("ok")) {
                // okno=textViewResult2.toString();
                //  if(okno.equals("ok"))
                //   textViewResult2.setText("abb");
                //CameraActivity.HttpAsyncTask httpTask = new CameraActivity.HttpAsyncTask(CameraActivity.this);
                //  httpTask.execute("http://117.16.244.117:2001/cal/post", username, year, month, day, hour, min, foodname);
                //  }
            }
        });






    }



    //로그인정보 보내기
    public static String POST(String url, Person person) {
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
                jsonObject.accumulate("username", person.getID());
                jsonObject.accumulate("year", person.getYear());
                jsonObject.accumulate("month", person.getMonth());
                jsonObject.accumulate("day", person.getDay());
                jsonObject.accumulate("hour", person.getHour());
                jsonObject.accumulate("min", person.getMin());
                jsonObject.accumulate("foodname", arr1[i]);
                list.put(jsonObject);

            }
            employeeInfo.accumulate("test", list);




            // convert JSONObject to JSON to String
            json = employeeInfo.toString();




            byte[] source = json.getBytes("UTF-8");
            String remake = new String(source, "UTF-8");

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

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

        private CameraActivity registerAct;

        HttpAsyncTask(CameraActivity registerActivity) {
            this.registerAct = registerActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            person = new Person();
            person.setID(urls[1]);
            person.setYear(urls[2]);
            person.setMonth(urls[3]);
            person.setDay(urls[4]);
            person.setHour(urls[5]);
            person.setMin(urls[6]);
            person.setFood(urls[7]);
            return POST(urls[0], person);
        }
        // onPostExecute displays the results of the AsyncTask.


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //strJson = "";
            strJson = result;
            // final String finalStrJson = strJson;
            registerAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //  Toast.makeText(registerAct, "Received!", Toast.LENGTH_LONG).show();
                    try {
                        JSONArray json = new JSONArray(strJson);
                        //registerAct.re.setText(json.toString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }






    private boolean validate(){
        if(username.equals(""))
            return false;
        else if(year.equals(""))
            return false;
        else if(month.equals(""))
            return false;
        else if(day.equals(""))
            return false;
        else if(hour.equals(""))
            return false;
        else if(min.equals(""))
            return false;
        else if(foodname.equals(""))
            return false;
        else
            return true;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }








    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);
                    makeButtonVisible();
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    public void okClick(View v)
    {
        HttpAsyncTask httpTask = new HttpAsyncTask(CameraActivity.this);
        httpTask.execute("http://117.16.244.117:2001/cal/post",username, year, month, day, hour, min, foodname);
        // Intent intent=new Intent(this,FoodResultActivity.class);
        // intent.putExtra("arraylist", arr);
        //  startActivity(intent);
        Intent intent=new Intent(CameraActivity.this,MainActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
        finish();
    }

    private void makeButtonVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnDetectObject.setVisibility(View.VISIBLE);
            }
        });
    }

    public void BackClick(View v)
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
        finish();
    }

    public void newFood(View v)
    {
        Intent intent=new Intent(this,AddFoodActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

}