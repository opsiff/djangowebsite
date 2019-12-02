package com.example.xianfish.ui.Add;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xianfish.R;
import com.example.xianfish.ui.LoginActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {


    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public String attaName;
    public String phone;
    public String price;
    public Button affirm;
    public String detail;
    public EditText editUser;
    public EditText editPhone;
    public EditText editPrice;
    public EditText editDiscription;
    private ImageView picture;
    File outputImage;
    JSONObject json = new JSONObject();
    //图片本地的真实路径
    private Uri imageUri;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //do something,refresh UI;
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_LONG).show();
//                    SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
//                    String s = msg.obj.toString();
//                    JSONObject json = new JSONObject().getJSONObject(s);
//                    editor.putBoolean("userData",);
                    break;
                default:
                    break;
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add, container, false);
        picture = root.findViewById(R.id.add_image);

        affirm = root.findViewById(R.id.affirm_btn);
        editUser = root.findViewById(R.id.edit_user);
        editPhone = root.findViewById(R.id.edit_phone);
        editPrice = root.findViewById(R.id.edit_price);
        affirm = root.findViewById(R.id.affirm_btn);
        editDiscription = root.findViewById(R.id.edit_discription);
        picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                Log.v(".,.,.,", "xxxx");
                builder.setItems(getResources().getStringArray(R.array.Gary), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("which", String.valueOf(which));
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

        affirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attaName = editUser.getText().toString();
                phone = editPhone.getText().toString();
                price = editPrice.getText().toString();
                detail = editDiscription.getText().toString();
//                    json.put("price",price);
//                    json.put("phone",phone);
//                    json.put("user",attaName);
                sendPost();


            }
        });
        return root;
    }


    private void openAlbum() {
//隐式Intent
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "You deny the permission", Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
//拍照成功后显示图片
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
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
        if (imagePath != null) {
//显示选中的图片
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_LONG).show();
        }
    }

    //通过ContentResolver查询相册中的数据
    private String getImagePath(Uri uri, String selection) {
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

    private void sendPost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                OkHttpClient client = new OkHttpClient();

                FormBody.Builder builder = new FormBody.Builder();
                builder.add("price", price);
                builder.add("phone", phone);
                builder.add("user", attaName);
                builder.add("detail", detail);
//                Log.v("<><><>",AddFragment.this.outputImage.getAbsolutePath());
                //File file = new File(picture.getDrawable());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Bitmap bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bytes = bos.toByteArray();
                Log.v("byte", bytes.toString());
                BufferedOutputStream boss = null;
                FileOutputStream fos = null;
                File file = null;
                file = new File("");
                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                boss = new BufferedOutputStream(fos);
                try {
                    boss.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                File file=AddFragment.getFile(bytes,"./","temp.png");
                RequestBody fileBody=RequestBody.create( MediaType.parse("image/*"),file);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("price",price)
                        .addFormDataPart("phone",phone)
                        .addFormDataPart("user",attaName)
                        .addFormDataPart("detail",detail)
                        .addFormDataPart("img",file.getName(),fileBody)
                        .build();
                Request request = new Request.Builder()
                        .url("http://122.112.159.211/message/0/submmit")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .post(requestBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Looper.prepare();
                    message.obj = responseData;
//                    mHandler .sendMessage(message);
//                    SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
//                    editor.putString("perInfo",responseData);
                    Looper.loop();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
