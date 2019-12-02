package com.example.xianfish.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.xianfish.R;
import com.example.xianfish.utils.PersonalInfo;
import com.example.xianfish.utils.PersonalInfoAdapter;




import java.util.ArrayList;
import java.util.prefs.Preferences;

public class InfoActivity extends AppCompatActivity {
    private ArrayList<PersonalInfo> infoItems=new ArrayList<PersonalInfo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        this.initData();
//        try {
//            t
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        PersonalInfoAdapter infoItemAdapter = new PersonalInfoAdapter(this,R.layout.info_item,infoItems);
        ListView listView= findViewById(R.id.info_ListView);
        listView.setAdapter(infoItemAdapter);

    }
    public void initData()  {
        SharedPreferences editor=getSharedPreferences("data",MODE_PRIVATE);
        String str=editor.getString("perInfo",null);

        String []types=new String[]{"真实姓名","学号","专业","性别"};
        String [] details=new String[]{"xx","041702324","计算机","男"};
        if (str!=null){

            JSONObject json= JSONObject.parseObject(str);
            JSONObject data=  json.getJSONObject("data");
            details=new String[]{data.getString("name"), data.getString("number"),
                        data.getString("major"),data.getString("sex")};
        }

        for (int i=0;i<types.length;i++){
            PersonalInfo item=new PersonalInfo(types[i],details[i]);
            infoItems.add(item);
        }
    }
}
