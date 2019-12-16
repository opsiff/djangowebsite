package com.example.xianfish.ui.notifications;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.example.xianfish.R;
import com.example.xianfish.ui.DeleteActivity;
import com.example.xianfish.ui.InfoActivity;
import com.example.xianfish.ui.LoginActivity;
import com.example.xianfish.ui.MainActivity;
import com.example.xianfish.utils.CircleImageView;
import com.example.xianfish.utils.SettingItem;
import com.example.xianfish.utils.SettingItemAdapter;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.xianfish.ui.Add.AddFragment.CHOOSE_PHOTO;
import static com.example.xianfish.ui.Add.AddFragment.TAKE_PHOTO;
import static java.lang.Thread.sleep;

public class NotificationsFragment extends Fragment {
    private Uri imageUri;
    File outputImage;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public CircleImageView circleImageView;
    org.json.JSONObject json = new org.json.JSONObject();
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

        circleImageView = root.findViewById(R.id.avata_image);
        String imagePath = null;
        SharedPreferences sharedPreferences = getActivity(). getSharedPreferences("logo", MODE_PRIVATE);
        imagePath = sharedPreferences.getString("logoPath","");
        Log.w(TAG, imagePath);

        if (!imagePath.isEmpty()){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            circleImageView.setImageBitmap(bitmap);
            Log.w(TAG, imagePath  );
            Log.w(TAG, bitmap.toString() );
        }


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(getResources().getStringArray(R.array.Gary), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
                            try {
                                if (outputImage.exists()) {
                                    outputImage.delete();
                                }
                                outputImage.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (Build.VERSION.SDK_INT >= 24) {
//File对象转换为Uri标识对象
                                imageUri = FileProvider.getUriForFile(getActivity(), "com.example.xianfish.fileprovider", outputImage);
                            } else {
//指定图片的输出地址
                                imageUri = Uri.fromFile(outputImage);
                            }
//隐式Intent，启动相机程序
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, TAKE_PHOTO);
                        } else {

                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            } else {
                                openAlbum();
                            }
                        }
                    }
                });
                builder.show();
            }
        });



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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        circleImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
//判断手机版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads//public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }

        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_LONG).show();

        if (imagePath != null) {
//显示选中的图片
            Log.v(TAG,"caommsmmsm");
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            //TODO:圆形view怎么加载图片
            circleImageView.setImageBitmap(bitmap);
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("logo", MODE_PRIVATE).edit();
            String filePath = imagePath;
            editor.putString("logoPath",filePath);
            editor.commit();
            Toast.makeText(getActivity(), imagePath, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_LONG).show();
        }
    }

    //通过ContentResolver查询相册中的数据
    private String getImagePath(Uri uri, String selection) {
        Log.v(TAG,"wquiqiuia");
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    public void initData(){
        String []names=new String[]{"我完成的","我的发布","我的收藏","注销"};
        int [] resId=new int[]{R.drawable.mydone,R.drawable.mysubmit,R.drawable.star,R.drawable.loginout};
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
                case 1:{
                    Intent intent= new Intent(NotificationsFragment.this.getActivity(), DeleteActivity.class);
                    startActivity(intent);

                }break;
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
    public static File getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }
}

