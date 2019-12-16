package com.example.xianfish.ui.Assistant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.xianfish.R;
import com.example.xianfish.ui.ShowActivity;
import com.example.xianfish.utils.AssistantBook;
import com.example.xianfish.utils.AssistantBookAdapter;
import com.example.xianfish.utils.AssistantBookAdapter2;
import com.example.xianfish.utils.mPagerAdapter;

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

public class AssistantFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<AssistantBook> assistantBookList = new ArrayList<>();
    private View root;
    public String responseData;
    private AssistantBookAdapter2 bookAdapter;
    static  final int SUCCESS=1;
    static  final int FAIL=0;
    public Handler handler;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_assistant, container, false);

        recyclerView = root.findViewById(R.id.assistantBook_Ras);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
        StaggeredGridLayoutManager layoutManager=new
                StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SUCCESS:
                        bookAdapter = new AssistantBookAdapter2(getActivity(),assistantBookList);
                        bookAdapter.setRecyclerViewOnItemClickListener(new AssistantBookAdapter2.RecyclerViewOnItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, int position) {
                                AssistantBook newBook = new AssistantBook();
                                newBook = assistantBookList.get(position).getAssistantBook();
                                Intent intent = new Intent(getActivity(), ShowActivity.class);
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
        bookAdapter = new AssistantBookAdapter2(getActivity(),assistantBookList);


        getMessage();
        bookAdapter.setRecyclerViewOnItemClickListener(new AssistantBookAdapter2.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                AssistantBook newBook = new AssistantBook();
                newBook = assistantBookList.get(position).getAssistantBook();
                Intent intent = new Intent(getActivity(), ShowActivity.class);
                intent.putExtra("name",newBook);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(bookAdapter);
        return root;
    }



    private void getMessage(){
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