package com.example.xianfish.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xianfish.R;
import com.example.xianfish.utils.AssistantBook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ShowActivity extends AppCompatActivity {
    public ImageView showP;
    public TextView showU;
    public TextView showPrice;
    public TextView showPhone;
    public TextView showDescribtion;
    public Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        final Intent intent = getIntent();
        AssistantBook assistantBook = (AssistantBook) intent.getSerializableExtra("name");

        showP = findViewById(R.id.show_image);
        Glide.with(this).load(assistantBook.getAsssistentBookImage()).into(showP);

        showU = findViewById(R.id.show_user);
        showU.setText(assistantBook.getName());

        showPrice = findViewById(R.id.show_price);
        showPrice.setText(assistantBook.getPrice());

        showPhone = findViewById(R.id.show_phone);
        showPhone.setText(assistantBook.getAttachNum());

        showDescribtion = findViewById(R.id.show_discription);
        showDescribtion.setText(assistantBook.getDiscription());

        back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ShowActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });

    }
}
