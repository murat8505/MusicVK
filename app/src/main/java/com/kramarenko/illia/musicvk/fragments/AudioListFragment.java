package com.kramarenko.illia.musicvk.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kramarenko.illia.musicvk.json.VKResponseJSONParser;
import com.kramarenko.illia.musicvk.ops.AudioItem;
import com.kramarenko.illia.musicvk.ops.AudioItemsAdapter;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;


public class AudioListFragment extends ListFragment {

    protected final String TAG = getClass().getSimpleName();

    // Number of audios to download
    public static final int COUNT = 30;
    // Offset to load more music
    public static int OFFSET = 0;
    // Adapter for list
    private AudioItemsAdapter adapter;
    // Error display if request to VK is failed
    private TextView errorTextBox;
    // Check if loading audio for the first time
    private boolean firstTimeLoad = true;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AudioListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new AudioItemsAdapter(getActivity().getApplicationContext(), this);
        setListAdapter(adapter);

        getAudio();
    }

    // Get audio files from VK
    public void getAudio(){
        if(firstTimeLoad){
            Log.d(TAG, "getAudio First Time method");
        }else{
            // Increase offset to load more audio next time
            OFFSET = OFFSET + COUNT;
            Log.d(TAG, "Getting MORE audio. New OFFSET is: " + OFFSET);
        }
        VKRequest request = VKApi.audio().get(VKParameters.from(VKApiConst.COUNT, COUNT, VKApiConst.OFFSET, OFFSET));
        request.secure = false;
        request.useSystemLanguage = false;
        request.executeWithListener(getAudioRequestListener);
        firstTimeLoad = false;
    }

    // Listener
    VKRequest.VKRequestListener getAudioRequestListener = new VKRequest.VKRequestListener()
    {
        @Override
        public void onComplete(VKResponse response)
        {
            Log.d(TAG, "getAudioRequestListener onComplete");
            AudioItem[] audioItems = VKResponseJSONParser.parseAudioItems(response);
            Log.d(TAG, "Sending parsed items to fragment");
            addItems(audioItems);
        }

        @Override
        public void onError(VKError error)
        {
            Log.d(TAG, "getAudioRequestListener onError");
            //TODO: comment err display, because of list may not be empty;
            errorTextBox.setText(error.toString());
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded,
                               long bytesTotal)
        {
            Log.d(TAG, "getAudioRequestListener onProgress. DOESN'T WORK WTF");
            errorTextBox.setText(progressType.toString() + ": " + String.valueOf(bytesLoaded) + "/" + String.valueOf(bytesTotal));
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts)
        {
            Log.d(TAG, "getAudioRequestListener attemptFailed");
            errorTextBox.setText(String.format("Attempt %d/%d failed\n", attemptNumber, totalAttempts));
        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    // Add audioItems
    public void addItems(AudioItem[] items){
        adapter.addItems(items);
    }

    // Get audioItem
    public AudioItem getItem(int position){
        return ((AudioItem) adapter.getItem(position));
    }

    // Pass a link to Error Text Box
    // TODO: think about usage of this textView
    public void setErrorTextBox(TextView textBox){
        this.errorTextBox = textBox;
    }


}
