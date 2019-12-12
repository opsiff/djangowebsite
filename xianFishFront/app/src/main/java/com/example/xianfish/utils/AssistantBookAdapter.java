package com.example.xianfish.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xianfish.R;

import java.util.List;

public class AssistantBookAdapter extends RecyclerView.Adapter<AssistantBookAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private RecyclerViewOnItemClickListener onItemClickListener;
    private RecyclerViewOnItemLongClickListener onItemLongClickListener;


    private List<AssistantBook> myAssistantBookList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private View view;
        ImageView imageView;
        TextView assistantBookName;
        TextView assistantBookPrice;
        TextView assistantBookDiscription;

        public ViewHolder(View view){
            super(view);
            this.view=view;
            imageView = view.findViewById(R.id.assistent_book_image);
            assistantBookName = view.findViewById(R.id.assistantBook_name);
            assistantBookPrice = view.findViewById(R.id.assistantBook_price);
            assistantBookDiscription = view.findViewById(R.id.assistantBook_discription);
        }
    }

    public AssistantBookAdapter(List<AssistantBook> AssistantBookList){
        myAssistantBookList = AssistantBookList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assistantbook_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onClick(View v) { if (onItemClickListener != null) {
        onItemClickListener.onItemClickListener(v, (Integer) v.getTag());
    }
    }

    @Override public boolean onLongClick(View v) {
        return onItemLongClickListener != null && onItemLongClickListener.onItemLongClickListener(v, (Integer) v.getTag());
    }

    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }
    public void setOnItemLongClickListener(RecyclerViewOnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface RecyclerViewOnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    public interface RecyclerViewOnItemLongClickListener {
        boolean onItemLongClickListener(View view, int position);
    }
    @Override

    public void onBindViewHolder(final ViewHolder holder, int position) {

        AssistantBook assistantBook = myAssistantBookList.get(position);
        holder.assistantBookName.setText(assistantBook.getName());
        holder.imageView.setImageBitmap(assistantBook.getAsssistentBookImage());
        holder.assistantBookPrice.setText(assistantBook.getPrice());
        holder.assistantBookDiscription.setText(assistantBook.getDiscription());
        holder.view.setTag(position);
    }


    @Override
    public int getItemCount() {
        return myAssistantBookList.size();
    }
}
