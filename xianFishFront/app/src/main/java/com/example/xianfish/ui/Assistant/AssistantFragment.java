package com.example.xianfish.ui.Assistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xianfish.R;
import com.example.xianfish.ui.ShowActivity;
import com.example.xianfish.utils.AssistantBook;
import com.example.xianfish.utils.AssistantBookAdapter;

import java.util.ArrayList;
import java.util.List;

public class AssistantFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<AssistantBook> assistantBookList = new ArrayList<>();
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_assistant, container, false);
        recyclerView = root.findViewById(R.id.assistantBook_Rview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        AssistantBookAdapter bookAdapter = new AssistantBookAdapter(assistantBookList);


        bookAdapter.setRecyclerViewOnItemClickListener(new AssistantBookAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(getActivity(), ShowActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(bookAdapter);
        initAssistantBooks();
        return root;
    }

    private void initAssistantBooks(){
        AssistantBook book1 = new AssistantBook();
        book1.setAsssistentBookImage(R.drawable.issue1);book1.setName("233");book1.setPrice("123");book1.setDiscription("qwfqwfqwfq");
        assistantBookList.add(book1);
        AssistantBook book2 = new AssistantBook();
        book2.setAsssistentBookImage(R.drawable.issue2);book2.setName("234");book2.setPrice("124");book2.setDiscription("fegewgewgew");
        assistantBookList.add(book2);
        AssistantBook book3 = new AssistantBook();
        book3.setAsssistentBookImage(R.drawable.issue3);book3.setName("235");book3.setPrice("125");book3.setDiscription("cddsvsdvdsvdsv");
        assistantBookList.add(book3);
        AssistantBook book4 = new AssistantBook();
        book4.setAsssistentBookImage(R.drawable.issue3);book4.setName("235");book4.setPrice("125");book4.setDiscription("cddsvsdvdsvdsv");
        assistantBookList.add(book4);
        AssistantBook book5 = new AssistantBook();
        book5.setAsssistentBookImage(R.drawable.issue3);book5.setName("235");book5.setPrice("125");book5.setDiscription("cddsvsdvdsvdsv");
        assistantBookList.add(book5);
        AssistantBook book6 = new AssistantBook();
        book6.setAsssistentBookImage(R.drawable.issue3);book6.setName("235");book6.setPrice("125");book6.setDiscription("cddsvsdvdsvdsv");
        assistantBookList.add(book6);
    }

}