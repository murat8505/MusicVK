package com.kramarenko.illia.musicvk.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kramarenko.illia.musicvk.R;
import com.kramarenko.illia.musicvk.json.VKResponseJSONParser;
import com.kramarenko.illia.musicvk.ops.AudioItem;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;


public class PlayerFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    // Error display if request to VK is failed
    private TextView errorTextBox;

    // Fragment with list of audios
    private AudioListFragment audioListFragment;

    // Number of audios to download
    private final int count = 30;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioListFragment = new AudioListFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.player_cont_big, audioListFragment)
                .commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        errorTextBox = (TextView) v.findViewById(R.id.smallTestText);

        getAudio();

        return v;
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

}
