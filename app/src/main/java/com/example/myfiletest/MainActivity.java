package com.example.myfiletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp; //存取檔案使用(偏好設定)
    private SharedPreferences.Editor editor; //內部類別

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("game", MODE_PRIVATE);
        editor = sp.edit();
    }
    //取得資料
    public void test1(View view) {
        //第二個值是預設值
        String username = sp.getString("username", "brad");
        Boolean isSound = sp.getBoolean("isSound", true);
        int stage = sp.getInt("stage", 4);
        Log.v("brad", username + isSound + stage);
    }
    public void test2(View view) {
    }
}