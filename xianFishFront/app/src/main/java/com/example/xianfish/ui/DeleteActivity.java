package com.example.xianfish.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.xianfish.R;
import com.example.xianfish.utils.AssistantBook;
import com.example.xianfish.utils.AssistantBookAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class DeleteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<AssistantBook> assistantBookList = new ArrayList<>();
    public String responseData;
    private AssistantBookAdapter bookAdapter;
    static  final int SUCCESS=1;
    static  final int FAIL=0;
    public static Handler handler;
    private String userID;
    private String returnSatue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        userID = getUseID();
        recyclerView = findViewById(R.id.assistantBook_delete);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DeleteActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SUCCESS:
                        bookAdapter = new AssistantBookAdapter(DeleteActivity.this,assistantBookList);
                        bookAdapter.setRecyclerViewOnItemClickListener(new AssistantBookAdapter.RecyclerViewOnItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, int position) {
                                AssistantBook newBook = new AssistantBook();
                                newBook = assistantBookList.get(position).getAssistantBook();
                                Intent intent = new Intent(DeleteActivity.this, ShowActivity.class);
                                intent.putExtra("name",newBook);
                                startActivity(intent);
                            }
                        });
                        bookAdapter.setOnItemLongClickListener(new AssistantBookAdapter.RecyclerViewOnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClickListener(View view,final int position) {
                                Log.w(TAG,"qwdqwdwd" );
                                AlertDialog.Builder dialog = new AlertDialog.Builder(DeleteActivity.this);
                                dialog.setTitle("警告");
                                dialog.setMessage("是否要删除已发布物品");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String msgID = assistantBookList.get(position).getMsgId();
                                        DeleteItem(msgID);
                                    } 
                                });
                                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                                return true;
                            }
                        });
                        recyclerView.setAdapter(bookAdapter);
                        break;
                    case FAIL:

                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };
        bookAdapter = new AssistantBookAdapter(DeleteActivity.this,assistantBookList);
        getMessage();
        bookAdapter.setRecyclerViewOnItemClickListener(new AssistantBookAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                AssistantBook newBook = new AssistantBook();
                newBook = assistantBookList.get(position).getAssistantBook();
                Intent intent = new Intent(DeleteActivity.this, ShowActivity.class);
                intent.putExtra("name",newBook);
                startActivity(intent);
            }
        });

        bookAdapter.setOnItemLongClickListener(new AssistantBookAdapter.RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean onItemLongClickListener(View view,final int position) {
                Log.w(TAG,"qwdqwdwd" );
                AlertDialog.Builder dialog = new AlertDialog.Builder(DeleteActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("是否要删除已发布物品");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String msgID = assistantBookList.get(position).getMsgId();
                        DeleteItem(msgID);
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
            }
        });
        recyclerView.setAdapter(bookAdapter);
    }

    private void DeleteItem(String msgID){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("msgId", msgID)
                .build();
        Request request = new Request.Builder()
                .url("http://122.112.159.211/message/0/dele")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                responseData = response.body().string();
                parseData(responseData);
                Looper.loop();
            }
        });
        Message msg=new Message();
        msg.what = 1;
        handler.sendMessage(msg);
    }

    private String getUseID(){
        SharedPreferences editor=DeleteActivity.this.getSharedPreferences("data",MODE_PRIVATE);
        String str=editor.getString("perInfo",null);
        String UserID=null;
        if (str!=null) {

//            str = str.replace("\\","");
            com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(str);
            com.alibaba.fastjson.JSONObject data = json.getJSONObject("data");
            UserID = data.getString("number");
        }
        return UserID;
    }

    private void getMessage(){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userID", userID)
                .build();
        Request request = new Request.Builder()
                .url("http://122.112.159.211/message/0/returnMyList")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                responseData = response.body().string();
                parseData(responseData);
                Looper.loop();
            }
        });
    }

    private void parseData(String result){
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                AssistantBook assistantBook = new AssistantBook();
                assistantBook.setMsgId(object.getString("msgId"));
                assistantBook.setName(object.getString("linkman"));
                assistantBook.setAttachNum(object.getString("contactWay"));
                assistantBook.setPrice(object.getString("price"));
                assistantBook.setDiscription(object.getString("detail"));
                String str = object.getString("img");
                String newS = "http://122.112.159.211/message/0/returnimg?img="+str;
                URL url = null;
                try {
                    url = new URL(newS);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                assistantBook.setAsssistentBookImage(url);
                assistantBookList.add(assistantBook);
                Message msg=new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
