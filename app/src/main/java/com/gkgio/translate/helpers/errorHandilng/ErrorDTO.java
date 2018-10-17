package com.gkgio.translate.helpers.errorHandilng;

import com.google.gson.annotations.SerializedName;

/**
 * {"code":6,"message":"Path not found"}
 */
public class ErrorDTO {
    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;

}
