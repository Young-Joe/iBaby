package com.joe.customlibrary.rxjava;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


public class Variable<T> {

    private  T val;

    private final Subject<T> subject;

    public Variable() {
        BehaviorSubject<T> subject = BehaviorSubject.create();
        this.subject = subject.toSerialized();
    }

    public Variable(@NonNull T defaultValue) {
        val = defaultValue;
        BehaviorSubject<T> subject = BehaviorSubject.createDefault(defaultValue);
        this.subject = subject.toSerialized();
    }

    @Nullable
    public T value() {
        synchronized (this) {
            return val;
        }
    }

    public Variable<T> setValue(@NonNull T value) {
        synchronized (this) {
            this.val = value;
        }
        subject.onNext(value);
        return this;
    }

    public Observable<T> asObservable() {
        return subject;
    }

}
