package com.example.xianfish.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.example.xianfish.R;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn, chg_Mode;
    String muser, passwd;
    int flag = 0;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG);
                    com.alibaba.fastjson.JSONObject json = JSONObject.parseObject(msg.obj.toString());
                    if (json.getString("message").compareTo("Success") == 0) {
                        flag = 1;
                        if (flag == 1) {
                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                            editor.putInt("flag", flag);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "账号/密码错误", Toast.LENGTH_LONG);
                    }
                }
                break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences x = getSharedPreferences("data", MODE_PRIVATE);
        flag = x.getInt("flag", 0);
        if (flag == 1) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strZhangHao = "", strMiMa;
                EditText zhanghao = (EditText) findViewById(R.id.zhanghaoEdit);
                strZhangHao = zhanghao.getText().toString();
                EditText MiMa = (EditText) findViewById(R.id.mimaEdit);
                strMiMa = MiMa.getText().toString();
                muser = strZhangHao;
                passwd = strMiMa;
                if (passwd.compareTo("") != 0 && muser.compareTo("") != 0) {
                    sendPost();
                }


            }
        });
    }

    private void sendPost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;


                OkHttpClient client = new OkHttpClient();
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("muser", muser);
                builder.add("passwd", passwd);
                FormBody formBody = builder.build();
                Request request = new Request.Builder()
                        .url("http://122.112.159.211/authorize/loginIn")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .post(formBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Looper.prepare();
                    message.obj = responseData;
                    mHandler.sendMessage(message);
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("perInfo", responseData);
                    editor.apply();
                    Looper.loop();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

