package com.example.myfiletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
    //取得資料-偏好設定
    public void test1(View view) {
        //第二個值是預設值
        String username = sp.getString("username", "brad");
        Boolean isSound = sp.getBoolean("isSound", true);
        int stage = sp.getInt("stage", 4);
        Log.v("brad", username + isSound + stage);
    }

    //寫入資料 ->關掉app還會留存紀錄
    public void test2(View view) {
        editor.putString("username", "test1");
        editor.putBoolean("isSound", false);
        editor.putInt("stage", 7);
        editor.commit(); // 真正寫入資料
        Toast.makeText(this, "save Ok", Toast.LENGTH_SHORT).show();
    }

    //資料寫出
    public void test3(View view) {
        try {
          //FileOutputStream fout = openFileOutput("test1.text", MODE_PRIVATE);
          //文字會加在檔案後面
            FileOutputStream fout = openFileOutput("test1.text", MODE_APPEND);
          fout.write("read ryrtr".getBytes());
          fout.flush(); //資料抽出
          fout.close();
          Toast.makeText(this, "save Ok", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    //資料輸入
    public void test4(View view) {
        try {
            FileInputStream fin = openFileInput("test1.text");
            BufferedReader br = new BufferedReader( new InputStreamReader(fin));
            String line;
            while ((line = br.readLine()) != null){
                Log.v("brad", line);
            }
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}