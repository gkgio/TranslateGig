package com.gkgio.translate.data.api;

import com.gkgio.translate.data.model.LanguagesResponse;
import com.gkgio.translate.data.model.TranslateTextResponse;
import com.gkgio.translate.di.scope.ActivityScope;
import com.gkgio.translate.helpers.errorHandling.ErrorHandler;
import com.gkgio.translate.helpers.utils.Config;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

@ActivityScope
public class REST implements IService {
    private final static String TAG = REST.class.toString();

    private static REST instance;

    private final ErrorHandler errorHandler;

    private IService service;

    public static REST getInstance() {
        return instance;
    }

    public static void setInstance(REST instance) {
        REST.instance = instance;
    }

    @Inject
    public REST(Retrofit retrofit, ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        service = retrofit.create(IService.class);
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    private <T> ObservableTransformer<T, T> setup() {
        return setup(Config.TIMEOUT, Config.RETRIES);
    }

    private <T> ObservableTransformer<T, T> setup(final int timeout, final int retries) {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .timeout(timeout, TimeUnit.SECONDS)
                        .retry(retries);
            }
        };
    }

    @NotNull
    @Override
    public Observable<LanguagesResponse> getLangs(@NotNull String apiKey, @NotNull String langCode) {
        return service.getLangs(apiKey, langCode).compose(this.<LanguagesResponse>setup());
    }

    @NotNull
    @Override
    public Observable<TranslateTextResponse> translate(@NotNull String lang,
                                                       @NotNull String apiKey, @NotNull String text) {
        return service.translate(lang, apiKey, text).compose(this.<TranslateTextResponse>setup());
    }
}
