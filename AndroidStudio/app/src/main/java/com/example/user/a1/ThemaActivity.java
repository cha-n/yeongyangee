package com.example.user.a1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ThemaActivity extends AppCompatActivity {

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thema);
        Intent intent = getIntent();
        username=intent.getStringExtra("Username");
    }

    public void RegistClick(View v) { //카메라메뉴
        Intent intent=new Intent(this,CameraActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    public void CalendarClick(View v) { //캘린더메뉴
        Intent intent=new Intent(this,CalendarActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }



    public void OnPbClick(View v) {
        Intent intent=new Intent(this,RecomListActivity.class);
        String s="pb";
        intent.putExtra("Username",username);
        intent.putExtra("Thema",s);
        startActivity(intent);
    }
    public void OnPregClick(View v) {
        Intent intent=new Intent(this,RecomListActivity.class);
        String s="preg";
        intent.putExtra("Username",username);
        intent.putExtra("Thema",s);
        startActivity(intent);
    }
    public void OnOsClick(View v) {
        Intent intent=new Intent(this,RecomListActivity.class);
        String s="obesity";
        intent.putExtra("Username",username);
        intent.putExtra("Thema",s);
        startActivity(intent);
    }
    public void OnCancerClick(View v) {
        Intent intent=new Intent(this,RecomListActivity.class);
        String s="cancer";
        intent.putExtra("Username",username);
        intent.putExtra("Thema",s);
        startActivity(intent);
    }
    public void OnCholeClick(View v) {
        Intent intent=new Intent(this,RecomListActivity.class);
        String s="chole";
        intent.putExtra("Username",username);
        intent.putExtra("Thema",s);
        startActivity(intent);
    }
    public void OnBsClick(View v) {
        Intent intent=new Intent(this,RecomListActivity.class);
        String s="bs";
        intent.putExtra("Username",username);
        intent.putExtra("Thema",s);
        startActivity(intent);
    }

    public void BackClick(View v)
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
        finish();
    }

}
