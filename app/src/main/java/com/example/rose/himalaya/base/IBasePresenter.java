package com.example.rose.himalaya.base;

import com.example.rose.himalaya.interfaces.IAlbumDetailViewCallback;

/**
 * Created by rose on 2019/11/12.
 */

public interface IBasePresenter<T> {
    /**
     * 注册UI通知接口
     */
    void registerViewCallback(T t);

    //删除UI通知接口
    void unRegisterViewCallback(T t);
}
