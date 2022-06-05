package com.example.myfiletest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp; //存取檔案使用(偏好設定)
    private SharedPreferences.Editor editor; //內部類別
    private File sdroot, approot;
    private TextView mesg;

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
        mesg = findViewById(R.id.mesg);

        sp = getSharedPreferences("game", MODE_PRIVATE);
        editor = sp.edit();

        sdroot = Environment.getExternalStorageDirectory();
        Log.v("brad", sdroot.getAbsolutePath()); //取得絕對路徑
        approot = new File(sdroot, "Android/data/" + getPackageName() + "/");
        if(!approot.exists()){
            if(approot.mkdir()){
                Log.v("brad", "mkdir ok"); //新增成功
            }else {
                Log.v("brad", "mkdir xx"); //新增失敗
            }
        }
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

    //資料寫出->存在內存空間，空間較小
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
    //存在sd卡，不隨著應用程式共存亡->sd卡，空間較大
    public void test5(View view) {
        File file1 = new File(sdroot, "test5.txt");
        try {
            FileOutputStream fout = new FileOutputStream(file1);
            fout.write("ok1".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this, "save5 Ok", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.v("brad", "TEST5 : " + e.toString());
        }
    }
    //存在sd卡，隨著應用程式共存亡
    public void test6(View view) {
        File file1 = new File(approot, "test5.txt");
        try {
            FileOutputStream fout = new FileOutputStream(file1);
            fout.write("ok1".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this, "save6 Ok", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.v("brad", "TEST6 : " + e.toString());
        }
    }
    public void test7(View view) {
        File file1 = new File(sdroot, "test5.txt");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
            String line;
            StringBuffer sb = new StringBuffer();
            while((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            br.close();
            mesg.setText(sb);
        }catch (Exception e){
            mesg.setText("error");
        }
    }
    public void test8(View view) {
        File file1 = new File(approot, "test5.txt");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
            String line;
            StringBuffer sb = new StringBuffer();
            while((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            br.close();
            mesg.setText(sb);
        }catch (Exception e){
            mesg.setText("");
        }
    }

    /*test1和test2,test3,test4是跟著專案共存亡，清除app資料就清除所以存入資料，刪除app就無法再復原了*/
}