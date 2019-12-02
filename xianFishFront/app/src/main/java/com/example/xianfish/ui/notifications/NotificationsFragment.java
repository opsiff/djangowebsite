package com.example.xianfish.ui.notifications;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.example.xianfish.R;
import com.example.xianfish.ui.InfoActivity;
import com.example.xianfish.ui.LoginActivity;
import com.example.xianfish.ui.MainActivity;
import com.example.xianfish.utils.SettingItem;
import com.example.xianfish.utils.SettingItemAdapter;




import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment {
    private ArrayList<SettingItem> settingItems=new ArrayList<SettingItem>() ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        initData();
        SettingItemAdapter settingItemAdapter = new SettingItemAdapter(getActivity(),R.layout.setting_item,settingItems);
        ListView listView= root.findViewById(R.id.setting);
        listView.setAdapter(settingItemAdapter);
        listView.setOnItemClickListener(new OnClickItem());


        LinearLayout InfoView=root.findViewById(R.id.introduce);
        InfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"个人资料",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                startActivity(intent);
            }
        });
        loadInfo(root);
        return root;
    }
//    public void  replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.nav_host_fragment,fragment);
//        transaction.commit();
//
//    }
    public void initData(){
        String []names=new String[]{"我完成的","我的发布","我的收藏","注销"};
        int [] resId=new int[]{R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.loginout};
        for (int i=0;i<names.length;i++){
            SettingItem item=new SettingItem(names[i],resId[i]);
            settingItems.add(item);
        }
    }
    public void loadInfo(View root)  {
        SharedPreferences editor=getActivity().getSharedPreferences("data",MODE_PRIVATE);
        String str=editor.getString("perInfo",null);
        if (str!=null){

//            str = str.replace("\\","");
            JSONObject json = JSONObject.parseObject(str);
            JSONObject data= json.getJSONObject("data");
            TextView username=root.findViewById(R.id.UserName);
            username.setText(data.getString("name"));
            TextView usernum=root.findViewById(R.id.UserNumber);
            usernum.setText(data.getString("number"));


        }
//        Looper.loop();

    }
    class OnClickItem implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 3: {
                    SharedPreferences.Editor editor= NotificationsFragment.this.getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putInt("flag",0);
                    editor.apply();
                    Intent intent = new Intent(NotificationsFragment.this.getActivity(), LoginActivity.class);
                    startActivity(intent);
                    NotificationsFragment.this.getActivity().finish();

                }break;
            }
        }
    }
}

