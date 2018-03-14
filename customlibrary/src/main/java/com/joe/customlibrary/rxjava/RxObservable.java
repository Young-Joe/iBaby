package com.joe.customlibrary.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * 封装RxJava类 -> create Observable
 * Created by QiaoJF on 16/12/7.
 */

public class RxObservable <T> {

    public RxObservable() {}

    /**
     *
     * 在callable的call方法中进行耗时操作
     * 注意:切勿使用Rx在返回方法中对成员变量进行赋值.会无法取值.(subscribeOn(Schedulers.io()))
     * @param callable
     * @return
     */
    public Observable<T> getObservableIo(final RxCallable<T> callable) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                e.onNext(callable.call());
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<T> getObservable(T defaultValue){
        return new Variable<T>(defaultValue).asObservable();
    }

}
