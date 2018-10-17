package com.gkgio.translate.helpers.errorHandilng;


import com.gkgio.translate.BuildConfig;
import com.gkgio.translate.di.scope.ActivityScope;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import retrofit2.HttpException;


@ActivityScope
public class ErrorHandler implements ObservableTransformer<Object, Object> {

    private final ErrorConverter errorConverter;
    private final ApiErrors errors;

    @Inject
    ErrorHandler(ErrorConverter errorConverter, ApiErrors errors) {
        this.errorConverter = errorConverter;
        this.errors = errors;
    }

    @Override
    public ObservableSource<Object> apply(Observable<Object> single) {
        return single.onErrorResumeNext(new Function<Throwable, Observable<?>>() {
            @Override
            public Observable<?> apply(Throwable e) {
                Throwable result;

                if (e instanceof HttpException) {

                    final HttpException he = (HttpException) e;

                    ErrorDTO error = null;
                    try {
                        error = errorConverter.getError(he);
                    } catch (Exception e1) {
                        // ignore
                    }

                    final int httpCode = he.code();
                    final int code = (error != null) ? error.code : -1;

                    result = new ServerException(httpCode, error.message, code);

                } else if (e instanceof TimeoutException) {
                    result = new ConnectionException(errors.getMessage(ApiErrors.SERVER_CONNECT_TIMEOUT), e);
                } else if (isConnectionError(e)) {
                    result = new ConnectionException(errors.getMessage(ApiErrors.NO_CONNECTION), e);
                } else {
                    result = new UnexpectedError(errors.getMessage(ApiErrors.UNEXPECTED_ERROR), e);
                }

                if (BuildConfig.DEBUG) {
                    result.printStackTrace();
                }

                return Observable.error(result);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> ObservableTransformer<T, T> cast() {
        return (ObservableTransformer<T, T>) this;
    }


    private boolean isConnectionError(Throwable e) {
        return (e instanceof TimeoutException) ||
                (e instanceof UnknownHostException) ||
                (e instanceof SocketTimeoutException) ||
                (e instanceof java.net.ConnectException);
    }
}
