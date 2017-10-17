package com.example.administrator.retrofituploadfiletoserver;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView imghinh;
    Button btnsenddata;
    String path = "";
    Uri uri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imghinh = (ImageView) findViewById(R.id.imageviewfolder);
        btnsenddata = (Button) findViewById(R.id.buttonsenddata);
        imghinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 123);
            }
        });
        btnsenddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient.Builder okhttp = new OkHttpClient.Builder();
                Retrofit.Builder builder = new Retrofit.Builder().
                        baseUrl("http://192.168.1.181/Upload/")
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.client(okhttp.build()).build();
                UploadClient uploadClient = retrofit.create(UploadClient.class);

                File file = new File(path);
                String file_path = file.getAbsolutePath();
                RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)),file);


                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), requestBody);
                Call<Message> call = uploadClient.uploadphoto(body);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (response!= null){
                            Message message = response.body();
                            if (message!= null){
                                if (message.getMessage().equals("Success")) {
                                    Toast.makeText(MainActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "Null", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
             uri = data.getData();
            path = getRealPathFromURI(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imghinh.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }
    public String getType(String path){
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
