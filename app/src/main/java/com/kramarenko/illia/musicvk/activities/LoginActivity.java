package com.kramarenko.illia.musicvk.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kramarenko.illia.musicvk.R;


public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    private Button okButton;
    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Login Activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        okButton = (Button) findViewById(R.id.loginButton);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

    }

    public void login(View view) {
        if(okButton != null
                && login != null
                && password != null){
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "login: " + login.getText().toString()
                            + " password: " + password.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
