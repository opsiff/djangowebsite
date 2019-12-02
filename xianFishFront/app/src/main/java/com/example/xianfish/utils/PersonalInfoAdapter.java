package com.example.xianfish.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.xianfish.R;

import java.util.ArrayList;

public class PersonalInfoAdapter extends ArrayAdapter<PersonalInfo> {

    private int resourceId;
    public PersonalInfoAdapter(@NonNull Context context, int textViewRId,
                               ArrayList<PersonalInfo> objects)
    {
        super(context, textViewRId,objects);
        resourceId=textViewRId;
    }


    @Override
    public View getView(int pos, View convertView , ViewGroup parent){
        PersonalInfo item=getItem(pos);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView type=(TextView) view.findViewById(R.id.info_type);
        TextView detail=(TextView) view.findViewById(R.id.info_detail);
        type.setText(item.type);
        detail.setText(item.detail);
        return view;
    }
}
