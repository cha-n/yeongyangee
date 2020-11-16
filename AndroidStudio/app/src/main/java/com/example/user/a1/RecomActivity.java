package com.example.user.a1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class RecomActivity extends YouTubeBaseActivity{

    private static final String YoutubeDeveloperKey = "key";
    YouTubePlayerView youTubeView;
    Button button;
    YouTubePlayer.OnInitializedListener listener;

    TextView textView;
    String username;
    TextView food;
    String foodname;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recom);

        Intent intent = getIntent();

        username =intent.getStringExtra("Username");
        foodname=intent.getStringExtra("Food");

        url=intent.getStringExtra("Url");

        textView=(TextView) findViewById(R.id.tx);
        food=(TextView)findViewById(R.id.food);

        textView.setText(url);
        food.setText("-"+foodname+" 레시피");

        button = (Button)findViewById(R.id.youtubebutton);

        youTubeView = (YouTubePlayerView)findViewById(R.id.youtubeView);

        //리스너 등록부분

        listener = new YouTubePlayer.OnInitializedListener(){

            //초기화 성공시

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo(url);//url의 맨 뒷부분 ID값만 넣으면 됨

            }

            @Override

            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        button.setOnClickListener(new View.OnClickListener(){



            @Override

            public void onClick(View view) {

                //첫번째 인자는 API키값 두번째는 실행할 리스너객체를 넘겨줌

                youTubeView.initialize(YoutubeDeveloperKey, listener);

                if(button.getVisibility() == view.VISIBLE)
                    button.setVisibility(view.GONE);

            }

        });
    }

    public void BackClick(View v)
    {
        // Intent intent=new Intent(this,RecomListActivity.class);
        // intent.putExtra("Username",username);
        // startActivity(intent);
        finish();
    }

}
