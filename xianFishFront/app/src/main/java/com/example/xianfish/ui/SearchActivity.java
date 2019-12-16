package com.example.xianfish.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xianfish.R;
import com.example.xianfish.utils.AssistantBook;
import com.example.xianfish.utils.AssistantBookAdapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity {

    private String[] mStrings = new String[]{"说好不哭", "等你下课", "不爱我就拉到", "123456"};
    private RecyclerView recyclerView;
    private List<AssistantBook> assistantBookList = new ArrayList<>();
    public String responseData;
    private AssistantBookAdapter bookAdapter;
    static  final int SUCCESS=1;
    static  final int FAIL=0;
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final ListView listView = findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, mStrings));
        //设置ListView启用过滤
        listView.setTextFilterEnabled(true);
        SearchView searchView = findViewById(R.id.searchview);
        //设置该SearchView默认是否自动缩小为图标
        searchView.setIconifiedByDefault(false);
        //设置该SearchView显示搜索按钮
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查找");
        //为该SearchView组件设置事件监听器
        recyclerView = findViewById(R.id.assistantBook_search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SUCCESS:
                        bookAdapter = new AssistantBookAdapter(SearchActivity.this,assistantBookList);
                        bookAdapter.setRecyclerViewOnItemClickListener(new AssistantBookAdapter.RecyclerViewOnItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, int position) {
                                AssistantBook newBook = new AssistantBook();
                                newBook = assistantBookList.get(position).getAssistantBook();
                                Intent intent = new Intent(SearchActivity.this, ShowActivity.class);
                                intent.putExtra("name",newBook);
                                startActivity(intent);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单机搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                //实际应用中应该在该方法内执行实际查询，此处仅使用Toast显示用户输入的查询内容

                bookAdapter = new AssistantBookAdapter(SearchActivity.this,assistantBookList);
                getMessage(query);
                bookAdapter.setRecyclerViewOnItemClickListener(new AssistantBookAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position) {
                        AssistantBook newBook = new AssistantBook();
                        newBook = assistantBookList.get(position).getAssistantBook();
                        Intent intent = new Intent(SearchActivity.this, ShowActivity.class);
                        intent.putExtra("name",newBook);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(bookAdapter);
                return false;
            }

            //用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                //如果newText不是长度为0的字符串
                if (TextUtils.isEmpty(newText)) {
                    //清除ListView的过滤
                    listView.clearTextFilter();
                } else {
                    //使用用户输入的内容对ListView的列表项进行过滤
                    listView.setFilterText(newText);
                }
                return true;
            }
        });
    }

    private void getMessage(String query){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

            }
        };
        requestBody.
        Request request = new Request.Builder()
                .url("http://122.112.159.211/message/0/returnList")
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