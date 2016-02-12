package com.scaleablesolutions.webapiauthenticationfromandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationException;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.PromptBehavior;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity implements WebServiceCallBack<String> {

    TextView tv_name;
    LocalStorage storage;
    private AuthenticationContext context;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            context = new AuthenticationContext(MainActivity.this, Constants.AUTHORITY_URL, true);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        dialog = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setMessage("progressing...");


        tv_name = (TextView) findViewById(R.id.tv_fullname);

        storage = new LocalStorage(this);

        Button btn_Submit = (Button) findViewById(R.id.btn_submit);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.acquireToken(MainActivity.this,
                        Constants.SERVICE_URL,
                        Constants.CLIENT_ID,
                        Constants.REDIRECT_URL, "", PromptBehavior.Auto, "", callback);
                dialog.show();

            }
        });
    }

    private AuthenticationCallback<AuthenticationResult> callback = new AuthenticationCallback<AuthenticationResult>() {

        @Override
        public void onError(Exception exc) {
            if (exc instanceof AuthenticationException) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Error 2", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }

        @Override
        public void onSuccess(AuthenticationResult result) {
            if (result == null || result.getAccessToken() == null || result.getAccessToken().isEmpty()) {
                Toast.makeText(MainActivity.this, "Token is Empty", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                storage.saveAuthenticationState(result.getExpiresOn().toString(),
                        result.getAccessToken(),
                        result.getRefreshToken());
                ServiceTask task = new ServiceTask(MainActivity.this, ServiceTask.GET_USER_ID, new WebServiceCallBack<String>() {
                    @Override
                    public void OnSuccess(String result) {
                        Intent i = new Intent(MainActivity.this,NameActivity.class);
                        i.putExtra("user_id",result);
                        startActivity(i);
                    }

                    @Override
                    public void OnError(String error) {

                    }
                });
                task.execute();
            }
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (context != null) {
            context.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void OnSuccess(String result) {
        tv_name.setText(result);
        dialog.dismiss();
    }

    @Override
    public void OnError(String error) {

    }
}
