package com.kramarenko.illia.musicvk.ops;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kramarenko.illia.musicvk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justify on 15.06.2015.
 */
public class AudioItemsAdapter extends BaseAdapter{

    protected final String TAG = getClass().getSimpleName();

    private List<AudioItem> items = new ArrayList<AudioItem>();
    private final Context context;


    public AudioItemsAdapter(Context c){
        context = c;
    }

    public void add(AudioItem item) {
        Log.d(TAG, "ADD");
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        Log.d(TAG, "CLEAR");
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        Log.d(TAG, "GET COUNT");
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d(TAG, "GET ITEM");
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "GET ITEM ID");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "GET VIEW");
        final AudioItem audioItem = items.get(position);

        RelativeLayout audioLayout;

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            audioLayout = (RelativeLayout) layoutInflater.inflate(R.layout.audio_item, null, false);
            Log.d(TAG, "Made NEW View");
        } else {
            audioLayout = (RelativeLayout) convertView;
            Log.d(TAG, "Recycling IS FUTURE OF MY VIEWS");
        }
        // Title
        final TextView title = (TextView) audioLayout.findViewById(R.id.songTitle);
        final TextView duration = (TextView) audioLayout.findViewById(R.id.duration);
        title.setText(audioItem.getArtist() + " - " + audioItem.getTitle());
        // Duration
        final int min = (int) audioItem.getDuration()/60;
        final int sec = (int) audioItem.getDuration() - min*60;
        if(sec < 10)
            duration.setText(String.valueOf(min) + ":0" + String.valueOf(sec));
        else
            duration.setText(String.valueOf(min) + ":" + String.valueOf(sec));
        // Play button DUMMY
        // TODO: get proper play function
        final ImageButton play = (ImageButton) audioLayout.findViewById(R.id.playButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Now playing: " + audioItem.getArtist() + ":" + audioItem.getTitle(), Toast.LENGTH_SHORT)
                        .show();
            }
        });


        return audioLayout;
    }
}
