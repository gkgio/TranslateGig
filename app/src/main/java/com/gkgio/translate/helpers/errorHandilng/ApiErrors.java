package com.gkgio.translate.helpers.errorHandilng;

import android.content.res.Resources;
import android.support.annotation.IntDef;

import com.gkgio.translate.R;
import com.gkgio.translate.di.scope.ActivityScope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;

@ActivityScope
public class ApiErrors {

    public static final int SERVER_CONNECT_TIMEOUT = R.string.server_connect_timeout;
    public static final int NO_CONNECTION = R.string.no_internet_connection;
    public static final int UNEXPECTED_ERROR = R.string.unexpected_error;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            SERVER_CONNECT_TIMEOUT,
            NO_CONNECTION,
            UNEXPECTED_ERROR
    })
    public @interface ErrorCode {
    }

    private final Resources res;

    @Inject
    public ApiErrors(Resources res) {
        this.res = res;
    }

    public String getMessage(@ErrorCode int error) {
        return res.getString(error);
    }

}
