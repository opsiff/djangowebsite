package com.example.xianfish.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.xianfish.R;
import com.example.xianfish.ui.ShowActivity;
import com.example.xianfish.utils.AssistantBook;
import com.example.xianfish.utils.AssistantBookAdapter;
import com.example.xianfish.utils.mPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    private ViewPager mAdvertiseViewPager;
    private RecyclerView recyclerView;
    private int[] mImage = new int[3];
    private List<AssistantBook> assistantBookList = new ArrayList<>();
    private View root;
    private Bitmap bitmap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        mPagerAdapter mPagerAdapter = new mPagerAdapter(mImage);
        mAdvertiseViewPager.setAdapter(mPagerAdapter);

        recyclerView = root.findViewById(R.id.assistantBook_Rview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        AssistantBookAdapter bookAdapter = new AssistantBookAdapter(assistantBookList);


        bookAdapter.setRecyclerViewOnItemClickListener(new AssistantBookAdapter.RecyclerViewOnItemClickListener() {
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

    private void initView() {
        mAdvertiseViewPager=root.findViewById(R.id.adertiseViewpager);
        mImage[0] = R.drawable.issue1;
        mImage[1] = R.drawable.issue2;
        mImage[2] = R.drawable.issue3;
    }
}