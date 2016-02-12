package com.scaleablesolutions.webapiauthenticationfromandroid;

/**
 * Created by ScaleableSolutions on 8/2/2016.
 */
public interface WebServiceCallBack<T> {

    void OnSuccess(T result);
    void OnError(String error);
}
