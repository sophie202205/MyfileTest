package com.example.myfiletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
        //官網 : https://developer.android.com/training/permissions/requesting?hl=zh-tw#java
        //有沒有得到授權
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }else{
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            init();
        }else{
            finish();
        }
    }

    private void init(){
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

    /*test1和test2,test3,test4是跟著專案共存亡，清除app資料就清除所以存入資料，刪除app就無法再復原了*/
}