package com.joe.ibaby.helper

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.joe.ibaby.ui.login.LoginViewModel

/**
 * @author qiaojianfeng on 18/2/27.
 */
class ViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}