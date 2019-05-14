package com.miu30.common.app;

import com.miu30.common.async.Result;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * 注意 重试的必须实现 此观察者
 * 网络请求回调，正常为onNext  异常（抛出的异常）为OnError
 *
 * @param <T>
 */
public abstract class MyErrorHandleSubscriber<T extends Result> extends ErrorHandleSubscriber<T> {
    public MyErrorHandleSubscriber(RxErrorHandler rxErrorHandler) {
        super(rxErrorHandler);
    }

    @Override
    public void onError(Throwable t) {
        Result result = new Result();
        result.setError(-1);
        String errorMsg = ResponseErrorListenerImpl.getMsg(t);
        result.setMsg(errorMsg);
        result.setThrowable(t);
        onNext((T) result);
    }

    @Override
    public void onNext(T t) {
        try {
            onNextResult(t);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public abstract void onNextResult(T t) throws Exception;
}
