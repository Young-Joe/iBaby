package com.joe.customlibrary.rxjava;

/**
 *
 * Offline操作结果返回接口
 * Created by QiaoJF on 16/12/7.
 */

public interface RxCallable <T> {

    T call()throws Exception;

}
