package com.kramarenko.illia.musicvk.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kramarenko.illia.musicvk.R;
import com.kramarenko.illia.musicvk.fragments.FragmentLogout;
import com.kramarenko.illia.musicvk.fragments.PlayerFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

public class MainActivity extends LifecycleLoggingActivity {

    // Tag for debug
    protected final String TAG = getClass().getSimpleName();

    /**
     * Scope is set of required permissions for your application
     * @see <a href="https://vk.com/dev/permissions">vk.com api permissions documentation</a>
     */
    private static final String[] sMyScope = new String[] {
            VKScope.AUDIO,
            VKScope.NOHTTPS
    };
    public static final String SCOPE_KEY = "scope" ;

    // App id on vk/dev
    private static final String appId = "4920885";

    // player fragment
    private PlayerFragment playerFragment;

    // Toolbar
    private Toolbar toolbar;

    // Navigation drawer
    private Drawer result;

    // dummy
    private String profileName = "Illia Kramarenko";
    private String profileID = "id7658894";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VKUIHelper.onCreate(this);
        // Init vk api
        VKSdk.initialize(sdkListener, appId);

        initView();
    }

    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);

        if (VKSdk.isLoggedIn()) {
            if(!playerFragment.isAdded()){
                loadPlayerFragment();
            }
        } else {
            showLogout();
        }
    }

    /**
     * SDKListener implementation
     */
    private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show(MainActivity.this);

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
            loadPlayerFragment();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            loadPlayerFragment();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
        // NPE SHIT playerFragment.killPlayer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    // Loading fragment if user is Logged out
    public void showLogout() {
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        Bundle scopeBundle = new Bundle();
        scopeBundle.putStringArray(SCOPE_KEY, sMyScope);
        Fragment f = new FragmentLogout();
        f.setArguments(scopeBundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, f)
                .commit();
    }

    // Loading player
    private void loadPlayerFragment(){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, playerFragment)
                .commit();
        if(getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setTitle(R.string.my_music);
        } else {
            Log.d(TAG, "getSupportActionBar is NULL");
        }
    }

    // Initializing view
    private void initView(){
        // Init and set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init player fragment
        playerFragment = new PlayerFragment();

        /** NAVIGATION DRAWER SECTION*/
        // Header for Nav drawer
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabled(false)
                .addProfiles(
                        new ProfileDrawerItem().withName(profileName).withIcon(getResources().getDrawable(R.drawable.profile)).withEmail(profileID)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        // Rest of the naw drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        //0
                        new PrimaryDrawerItem().withName(R.string.drawer_my_music),
                        //1
                        new PrimaryDrawerItem().withName(R.string.drawer_downloaded)
                                .withDisabledTextColor(R.color.secondary_text)
                                .setEnabled(false),
                        //2
                        new PrimaryDrawerItem().withName(R.string.drawer_search)
                                .withDisabledTextColor(R.color.secondary_text)
                                .setEnabled(false),
                        //3
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings)
                                .withDisabledTextColor(R.color.secondary_text)
                                .setEnabled(false),
                        //4
                        new SectionDrawerItem(),
                        //5
                        new SecondaryDrawerItem().withName(R.string.logout)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position) {
                            case 0:
                                if(playerFragment.isAdded()){
                                    result.closeDrawer();
                                } else {
                                    loadPlayerFragment();
                                }
                                break;
                            case 1:
                                Toast.makeText(MainActivity.this, position + "not yet", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(MainActivity.this, position + "not yet", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(MainActivity.this, position + "not yet", Toast.LENGTH_SHORT).show();
                                break;
                            case 5:
                                VKSdk.logout();
                                if (!VKSdk.isLoggedIn()) {
                                    showLogout();
                                }
                                result.closeDrawer();
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "DEFAULT", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                })
                .build();

        //use the result object to get different views of the drawer or modify it's data
        //some sample calls
        /*
        result.setSelectionByIdentifier(1);
        result.openDrawer();
        result.closeDrawer();
        result.isDrawerOpen();*/

    }

}
