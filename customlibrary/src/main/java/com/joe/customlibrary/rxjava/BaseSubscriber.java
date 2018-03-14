package com.joe.customlibrary.rxjava;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 *
 * Created by QiaoJF on 16/12/5.
 */

public class BaseSubscriber<T> implements Subscriber<T> {


    @Override
    public void onSubscribe(Subscription s) {
        Log.e("MDT--->","onSubscribe");
        //必须设置数量(为onNext的可执行次数),否则无法执行onNext/onComplete
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        Log.e("MDT--->","onNext");
    }

    @Override
    public void onError(Throwable t) {
        //TODO error-log
        Log.e("BaseSubscriber-Error",t.getMessage());
    }

    @Override
    public void onComplete() {
        Log.e("MDT--->","onComplete");
    }
}
