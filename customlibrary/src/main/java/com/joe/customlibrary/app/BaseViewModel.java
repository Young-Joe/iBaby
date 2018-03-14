package com.joe.customlibrary.app;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

/**
 * Created by qiaojianfeng on 17/12/1.
 */

public class BaseViewModel extends ViewModel {

    public Context context;

    public BaseViewModel() {
        this.context = BaseApplication.getContext();

    }
}
