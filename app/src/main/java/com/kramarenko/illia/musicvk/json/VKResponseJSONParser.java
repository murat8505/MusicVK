package com.kramarenko.illia.musicvk.json;

import android.util.Log;

import com.kramarenko.illia.musicvk.ops.AudioItem;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by justify on 16.06.2015.
 */
public class VKResponseJSONParser {

    protected final static String TAG = "VKResponseJSONParser";

    public static AudioItem[] parseJSONvkresponse(VKResponse response){
        Log.d(TAG, "Entered parse method");
        JSONObject dataJsonObj = response.json;
        AudioItem[] items = null;
        try {
            Log.d(TAG, "try to fill in Audios Array");
            JSONObject responseJSON = dataJsonObj.getJSONObject("response");
            Log.d(TAG, "Found 'response' object in JSON response");
            JSONArray audios = responseJSON.getJSONArray("items");
            Log.d(TAG, "Found 'items' array in JSON response");
            items = new AudioItem[audios.length()];
            Log.d(TAG, "Created AudioItems array with " + items.length + " elements");
            // Store all audio entries
            for (int i = 0; i < audios.length(); i++) {
                JSONObject song = audios.getJSONObject(i);

                String artist = song.getString("artist");
                String title = song.getString("title");
                long duration = song.getLong("duration");
                items[i] = new AudioItem(artist, title, duration);

                Log.d(TAG, "Audio " + i + " added:" + artist
                        + " - " + title + " " + duration);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(items != null) {
            Log.d(TAG, "AudioItems is OK... Returning...");
            return items;
        } else {
            Log.d(TAG, "AudioItems is null somehow. Returning ERRitem");
            AudioItem[] errItem = new AudioItem[1];
            errItem[0] = new AudioItem("Error while", "getting audioItems array", 20L );
            return errItem;
        }
    }
}
