package com.scaleablesolutions.webapiauthenticationfromandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NameActivity extends AppCompatActivity implements WebServiceCallBack<String> {

    TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        tv_name = (TextView) findViewById(R.id.tv_name);

        String userId = getIntent().getExtras().getString("user_id", null);
        ServiceTask serviceTask = new ServiceTask(NameActivity.this, ServiceTask.GET_FULL_NAME, userId, this);
        serviceTask.execute();
    }

    @Override
    public void OnSuccess(String result) {
        tv_name.setText(result);
    }

    @Override
    public void OnError(String error) {

    }
}
