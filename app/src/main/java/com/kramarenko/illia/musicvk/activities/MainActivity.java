package com.kramarenko.illia.musicvk.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kramarenko.illia.musicvk.R;
import com.vk.sdk.VKSdk;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView t = (TextView) findViewById(R.id.hello_main);

        if(VKSdk.isLoggedIn()){
            t.setText(t.getText() + " IS Logged NOW");
        } else {
            t. setText(t.getText() + " IS NOT logged");
        }
    }
}
