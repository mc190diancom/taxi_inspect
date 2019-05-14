package com.miu30.common.app;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 网络出错重试
 */
public class MyRetryWithDelay implements Function<Observable<Throwable>, ObservableSource<?>> {

    public final String TAG = this.getClass().getSimpleName();
    private final int maxRetries;
    private final int retryDelaySecond;
    private int retryCount;

    public MyRetryWithDelay(int maxRetries, int retryDelaySecond) {
        this.maxRetries = maxRetries;
        this.retryDelaySecond = retryDelaySecond;
    }

    @Override
    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable
                .flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                        if (ResponseErrorListenerImpl.needTry(throwable)) {
                            if (++retryCount <= maxRetries) {
                                // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                                Log.d(TAG, "Observable get error, it will try after " + retryDelaySecond
                                        + " second, retry count " + retryCount);
                                return Observable.timer(retryDelaySecond,
                                        TimeUnit.SECONDS);
                            }
                        }
                        Log.d(TAG, "重试 失败 发送");
                        return  Observable.error(throwable);
                    }
                });
    }
}
