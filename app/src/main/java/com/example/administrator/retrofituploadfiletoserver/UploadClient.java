package com.example.administrator.retrofituploadfiletoserver;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadClient {

    @Multipart
    @POST("upload.php")
    Call<Message> uploadphoto(
            //Thang nay chi la string don gian thi su dung class RequestBody
//            @Part("description") RequestBody description,
            //Thang nay` mo ta qua nhieu nen xai MultipartBody.Part
            @Part MultipartBody.Part photo
    );
}