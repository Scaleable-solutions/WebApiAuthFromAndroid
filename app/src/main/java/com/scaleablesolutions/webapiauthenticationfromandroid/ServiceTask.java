package com.scaleablesolutions.webapiauthenticationfromandroid;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ScaleableSolutions on 8/2/2016.
 */
public class ServiceTask extends AsyncTask<String, String, String> {
    public static final int GET_USER_ID = 1;
    public static final int GET_FULL_NAME = 2;
    String userId;
    private WebServiceCallBack delegate;
    private int task;
    private LocalStorage storage;

    public ServiceTask(Context context, int Task, WebServiceCallBack delegate) {
        this.task = Task;
        storage = new LocalStorage(context);
        this.delegate = delegate;
    }

    public ServiceTask(Context context, int Task, String userId, WebServiceCallBack delegate) {
        this.task = Task;
        storage = new LocalStorage(context);
        this.delegate = delegate;
        this.userId = userId;
    }

    @Override
    protected String doInBackground(String... params) {
        ServiceHandler handler = new ServiceHandler();
        if (task == GET_USER_ID) {
            return handler.whoAmIRequest(storage.getAccessToken());
        } else if (task == GET_FULL_NAME) {
            return handler.userInfoRequest(storage.getAccessToken(), userId);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (task == GET_USER_ID) {
            JSONObject json = null;
            String id = "";
            try {
                json = new JSONObject(s);
                id = json.getString("UserId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            delegate.OnSuccess(id);
        } else if (task == GET_FULL_NAME) {
            JSONObject json = null;
            String fullname = "";
            try {
                json = new JSONObject(s);
                fullname = json.getString("fullname");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            delegate.OnSuccess(fullname);
        }
    }
}
