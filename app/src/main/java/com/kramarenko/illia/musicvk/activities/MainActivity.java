package com.kramarenko.illia.musicvk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kramarenko.illia.musicvk.R;
import com.kramarenko.illia.musicvk.fragments.AudioListFragment;
import com.kramarenko.illia.musicvk.json.VKResponseJSONParser;
import com.kramarenko.illia.musicvk.ops.AudioItem;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

public class MainActivity extends LifecycleLoggingActivity {

    // Tag for debug
    protected final String TAG = getClass().getSimpleName();

    // Number of audios to download
    private final int count = 30;

    // Fragment with list of audios
    private AudioListFragment audioListFragment;

    // Error display if request to VK is failed
    private TextView errorTextBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VKUIHelper.onCreate(this);

        errorTextBox = (TextView) findViewById(R.id.smallTestText);

        audioListFragment = new AudioListFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_cont_big, audioListFragment)
                .commit();

        getAudio();
    }

    private void getAudio(){
        Log.d(TAG, "getAudio method");
        VKRequest request = VKApi.audio().get(VKParameters.from(VKApiConst.COUNT, count));
        request.secure = false;
        request.useSystemLanguage = false;
        request.executeWithListener(mRequestListener);
    }


    VKRequest.VKRequestListener mRequestListener = new VKRequest.VKRequestListener()
    {
        @Override
        public void onComplete(VKResponse response)
        {
            Log.d(TAG, "mRequestListener onComplete");
            AudioItem[] audioItems = VKResponseJSONParser.parseJSONvkresponse(response);
            Log.d(TAG, "Sending parsed items to fragment");
            audioListFragment.addItems(audioItems);
        }

        @Override
        public void onError(VKError error)
        {
            Log.d(TAG, "mRequestListener onError");
            errorTextBox.setText(error.toString());
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded,
                               long bytesTotal)
        {
            Log.d(TAG, "mRequestListener onProgress. SHIT DOESN'T WORK WTF");
            errorTextBox.setText(progressType.toString() + ": " + String.valueOf(bytesLoaded) + "/" + String.valueOf(bytesTotal));
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts)
        {
            Log.d(TAG, "mRequestListener attemptFailed");
            errorTextBox.setText(String.format("Attempt %d/%d failed\n", attemptNumber, totalAttempts));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
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

}
