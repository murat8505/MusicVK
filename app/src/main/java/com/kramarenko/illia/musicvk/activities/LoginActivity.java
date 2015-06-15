package com.kramarenko.illia.musicvk.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kramarenko.illia.musicvk.R;
import com.kramarenko.illia.musicvk.fragments.FragmentLogin;
import com.kramarenko.illia.musicvk.fragments.FragmentLogout;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;


public class LoginActivity extends Activity {


    /**
     * Scope is set of required permissions for your application
     * @see <a href="https://vk.com/dev/permissions">vk.com api permissions documentation</a>
     */
    private static final String[] sMyScope = new String[] {
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS
    };
    public static final String SCOPE_KEY = "scope" ;

    private static final String appId = "4920885";

    private static final String TAG = "LoginActivity";

    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Login Activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        VKSdk.initialize(sdkListener, appId);
        VKUIHelper.onCreate(this);

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

    }

    public void login(View view) {
        Toast.makeText(getApplicationContext(), "login: " + login.getText().toString()
                            + " password: " + password.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * VK API required method changes
     */

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        if (VKSdk.isLoggedIn()) {
            showLogin();
        } else {
            showLogout();
        }

    }

    public void showLogin() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new FragmentLogin())
                .commit();
    }

    public void showLogout() {
        Bundle scopeBundle = new Bundle();
        scopeBundle.putStringArray(SCOPE_KEY, sMyScope);
        Fragment f = new FragmentLogout();
        f.setArguments(scopeBundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, f)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    /**
     * SDKListener implementation
     */
    private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show(LoginActivity.this);

        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            startMainActivity();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            startMainActivity();
        }
    };

    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
