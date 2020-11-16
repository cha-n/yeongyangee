package com.example.user.a1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CalorieActivity extends AppCompatActivity {

    EditText editText;
    TextView name;
    TextView result;
    SimpleAdapter adapter;
    SimpleAdapter adapter2;
    SimpleAdapter adapter3;
    Button delete;

    String username;

    ListView lView;
    ListView lView2;
    ListView lView3;
    double amount=0;
    double cal=0;



    String[] from = {"name_item"};
    int[] to = {R.id.name_item};
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashmap;

    ArrayList<HashMap<String, String>> arrayList2 = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashmap2;

    ArrayList<HashMap<String, String>> arrayList3 = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashmap3;

    ArrayList<HashMap<String, String>> arrayList4 = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashmap4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

        Intent intent = getIntent();
        username =intent.getStringExtra("Username");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        new ParseTask().execute();
        //name = (TextView) findViewById(R.id.name);
        //name.setText("식품이름            일회제공량(g)      열량(kcal)");
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json = "http://117.16.244.117:2001/foods";
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
            lView3 = (ListView) findViewById(R.id.lvMain3);

         /*   String[] from = {"name_item"};
            int[] to = {R.id.name_item};
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> hashmap; */
            try {
                JSONArray jArray = new JSONArray(strJson);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject friend = jArray.getJSONObject(i);
                    String nameOS = friend.getString("식품이름");

                    hashmap = new HashMap<String, String>();
                    hashmap.put("name_item", "" + nameOS);
                    arrayList.add(hashmap);
                }
                adapter = new SimpleAdapter(CalorieActivity.this, arrayList, R.layout.item, from, to);
                lView.setAdapter(adapter);
                //lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            editText = (EditText) findViewById(R.id.editText);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //listView.setFilterText(editText.getText().toString());
                    (CalorieActivity.this).adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });


            lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    ImageView line=findViewById(R.id.line);
                    //name = (TextView) findViewById(R.id.name);
                    //name.setText("식품이름            일회제공량(g)      열량(kcal)");

                    String a = (adapter.getItem(position)).toString();
                    int len = a.length() - 1;
                    a = a.substring(11, len);
                    // textview2.setText(a);


                    lView2 = (ListView) findViewById(R.id.lvMain2);
                    lView3 = (ListView) findViewById(R.id.lvMain3);
                    //  textview2=(TextView) findViewById(R.id.textView12);
                    result = (TextView) findViewById(R.id.result1);


                    try {
                        JSONArray jArray = new JSONArray(strJson);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject friend = jArray.getJSONObject(i);
                            String nameOS = friend.getString("식품이름");

                            //  textview2.setText(a);
                            if (nameOS.equals(a)) {

                                String nameOS2 = friend.getString("일회제공량_g");
                                amount = amount + Double.parseDouble(nameOS2);

                                String nameOS3 = friend.getString("열량_kcal");
                                cal = cal + Double.parseDouble(nameOS3);

                                hashmap2 = new HashMap<String, String>();
                                hashmap2.put("name_item", "     " + nameOS);
                                arrayList2.add(hashmap2);


                                hashmap3 = new HashMap<String, String>();
                                hashmap3.put("name_item", nameOS2 + "                    " + nameOS3);
                                arrayList3.add(hashmap3);


                            }
                        }
                        adapter2 = new SimpleAdapter(CalorieActivity.this, arrayList2, R.layout.item2, from, to);
                        lView2.setAdapter(adapter2);

                        adapter3 = new SimpleAdapter(CalorieActivity.this, arrayList3, R.layout.item2, from, to);
                        lView3.setAdapter(adapter3);
                        //lView3.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    result.setText("총 칼로리 : " + String.format("%.2f", cal)+"kcal");
                    line.setVisibility(View.VISIBLE);

                }
            });

            lView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    String a = (adapter3.getItem(position)).toString();
                    int len = a.length() - 1;
                    a = a.substring(11, len);
                    String[] words = a.split("                 ");
                    //double g1=Double.parseDouble(words[0]);
                    double k1=Double.parseDouble(words[1]);
                    //amount=amount-g1;
                    cal=cal-k1;

                    if (cal<0.001)
                        cal=0;


                    result = (TextView) findViewById(R.id.result1);
                    result.setText("총 칼로리 : " + String.format("%.2f", cal)+"kcal");


                    lView3.setAdapter(null);
                    arrayList3.remove(position);
                    adapter3 = new SimpleAdapter(CalorieActivity.this, arrayList3, R.layout.item2, from, to);
                    lView3.setAdapter(adapter3);
                    adapter3.notifyDataSetChanged();

                    lView2.setAdapter(null);
                    arrayList2.remove(position);
                    adapter2 = new SimpleAdapter(CalorieActivity.this, arrayList2, R.layout.item2, from, to);
                    lView2.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();







                }
            });






        }

    }

    public void BackClick(View v)
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
        finish();
    }



}