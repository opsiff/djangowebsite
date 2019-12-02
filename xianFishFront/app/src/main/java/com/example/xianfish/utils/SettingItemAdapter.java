package com.example.xianfish.utils;

import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xianfish.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SettingItemAdapter extends ArrayAdapter<SettingItem> {

    private int resourceId;
    public SettingItemAdapter(@NonNull Context context, int textViewRId,
                              ArrayList<SettingItem> objects)
    {
        super(context, textViewRId,objects);
        resourceId=textViewRId;
    }
    @Override
    public View getView(int pos,View convertView ,ViewGroup parent){
        SettingItem item=getItem(pos);
        View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView icon=(ImageView) view.findViewById(R.id.item_icon);
        TextView name=(TextView) view.findViewById(R.id.item_name);
        icon.setImageResource(item.iconId);
        name.setText(item.name);
        return view;
    }
}
