package com.example.administrator.retrofituploadfiletoserver;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 10/10/2017.
 */

public class Message {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
