package com.example.user.a1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RecomListActivity extends AppCompatActivity {

    SimpleDateFormat NowDate = new SimpleDateFormat("yyyy년 MM월 dd일");
    long now = System.currentTimeMillis();
    Date date1 = new Date(now);
    String formatDate = NowDate.format(date1);
    String username;

    //int a[]=new int[10];

    SimpleAdapter adapter;
    SimpleAdapter adapter2;

    ListView lView;

    String[] from = {"name_item"};
    int[] to = {R.id.name_item};

    String[] from2 = {"url_item"};
    int[] to2 = {R.id.name_item};

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> arrayList2 = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashmap;
    HashMap<String,String> hashMap2;

    String them;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recom_list);



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        new UrlTask().execute();

        Intent intent = getIntent();

        them = intent.getStringExtra("Thema");
        username=intent.getStringExtra("Username");

        //themm=(TextView)findViewById(R.id.textView2);

        //themm.setText(them);

    }

    private class UrlTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;

        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                /*
                String a="http//117.16.244.117:2001/";
                String b="url";
                a=a+b;
                String $url_json = a;
                */
                String $url_json = "http://117.16.244.117:2001/url";
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

            lView = (ListView)findViewById(R.id.lvFood);

            try {
                Intent intent = new Intent(RecomListActivity.this, MainActivity.class);

                JSONArray jArray = new JSONArray(result);

                if(them.equals("pb")) {
                    for(int i=0;i<31;i++) {
                        JSONObject friend = jArray.getJSONObject(i);

                        String  food = friend.getString("음식");
                        url = friend.getString("url");

                        hashmap = new HashMap<String, String>();
                        hashMap2 = new HashMap<String, String>();

                        hashmap.put("name_item", ""+ food);
                        hashMap2.put("url_item",""+ url);

                        arrayList.add(hashmap);
                        arrayList2.add(hashMap2);
                   }
                }
                if(them.equals("preg")){
                    for(int i=31;i<54;i++){
                        JSONObject friend = jArray.getJSONObject(i);

                        String  food = friend.getString("음식");
                        url = friend.getString("url");

                        hashmap = new HashMap<String, String>();
                        hashMap2 = new HashMap<String, String>();

                        hashmap.put("name_item", ""+ food);
                        hashMap2.put("url_item",""+ url);

                        arrayList.add(hashmap);
                        arrayList2.add(hashMap2);

                    }
                }
                if(them.equals("obesity")){
                    for(int i=54;i<68;i++){
                        JSONObject friend = jArray.getJSONObject(i);

                        String  food = friend.getString("음식");
                        url = friend.getString("url");

                        hashmap = new HashMap<String, String>();
                        hashMap2 = new HashMap<String, String>();

                        hashmap.put("name_item", ""+ food);
                        hashMap2.put("url_item",""+ url);

                        arrayList.add(hashmap);
                        arrayList2.add(hashMap2);

                    }
                }
                if(them.equals("cancer")){
                    for(int i=68;i<78;i++){
                        JSONObject friend = jArray.getJSONObject(i);

                        String  food = friend.getString("음식");
                        url = friend.getString("url");

                        hashmap = new HashMap<String, String>();
                        hashMap2 = new HashMap<String, String>();

                        hashmap.put("name_item", ""+ food);
                        hashMap2.put("url_item",""+ url);

                        arrayList.add(hashmap);
                        arrayList2.add(hashMap2);

                    }
                }
                if(them.equals("chole")){
                    for(int i=78;i<87;i++){
                        JSONObject friend = jArray.getJSONObject(i);

                        String  food = friend.getString("음식");
                        url = friend.getString("url");

                        hashmap = new HashMap<String, String>();
                        hashMap2 = new HashMap<String, String>();

                        hashmap.put("name_item", ""+ food);
                        hashMap2.put("url_item",""+ url);

                        arrayList.add(hashmap);
                        arrayList2.add(hashMap2);

                    }
                }
                if(them.equals("bs")){
                    for(int i=87;i<138;i++){
                        JSONObject friend = jArray.getJSONObject(i);

                        String  food = friend.getString("음식");
                        url = friend.getString("url");

                        hashmap = new HashMap<String, String>();
                        hashMap2 = new HashMap<String, String>();

                        hashmap.put("name_item", ""+ food);
                        hashMap2.put("url_item",""+ url);

                        arrayList.add(hashmap);
                        arrayList2.add(hashMap2);

                    }
                }

                adapter = new SimpleAdapter(RecomListActivity.this,arrayList,R.layout.itemrecom,from,to);
                adapter2=new SimpleAdapter(RecomListActivity.this,arrayList2,R.layout.item,from2,to2);
                //lView2.setAdapter(adapter2);
                lView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

            lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                //String food="";
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    url = (adapter2.getItem(position)).toString();
                    String Food = (adapter.getItem(position)).toString();

                    int len = url.length() - 1;
                    int len2=Food.length()-1;

                    url = url.substring(10, len);
                    Food=Food.substring(11,len2);

                    Intent intent=new Intent(RecomListActivity.this,RecomActivity.class);

                    intent.putExtra("Url",url);
                    intent.putExtra("Username",username);
                    intent.putExtra("Food",Food);

                    startActivity(intent);
                }
            });

        }

    }
    public void BackClick(View v)
    {
        Intent intent=new Intent(this,ThemaActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
        finish();
    }
}
