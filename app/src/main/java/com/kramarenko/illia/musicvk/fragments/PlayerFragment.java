package com.kramarenko.illia.musicvk.fragments;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
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


public class PlayerFragment extends Fragment
        implements View.OnClickListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {

    protected final String TAG = getClass().getSimpleName();

    // Error display if request to VK is failed
    private TextView errorTextBox;

    // Fragment with list of audios
    private AudioListFragment audioListFragment;

    // Set of playback control buttons
    private ImageButton playPauseButton;
    private ImageButton skipPrevButton;
    private ImageButton skipNextButton;

    // Seekbar
    private SeekBar seekBar;
    // current seek
    private int seek=0;

    //  Song title in player
    private TextView playerSongTitle;

    // Media player
    private MediaPlayer mediaPlayer;
    // Checks if MP is paused
    private boolean isPaused=false;

    // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class
    private int mediaFileLengthInMilliseconds;

    // Curent song number
    private int currentSongNumber = 0;
    // Progress
    private int seekProgress = 0;

    private final Handler handler = new Handler();

    // Required empty public constructor
    public PlayerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_player, container, false);

        initViev(v);
        loadListFragment();

        return v;
    }

    // Initialize view elements
    private void initViev(View v){
        Log.d(TAG, "Initializing view");

        // Error message textbox
        errorTextBox = (TextView) v.findViewById(R.id.smallTestText);

        // Set of playback buttons
        playPauseButton = (ImageButton) v.findViewById(R.id.playPauseButton);
        skipPrevButton = (ImageButton) v.findViewById(R.id.skipPrevButton);
        skipNextButton = (ImageButton) v.findViewById(R.id.skipNextbutton);
        playPauseButton.setOnClickListener(this);
        skipPrevButton.setOnClickListener(this);
        skipNextButton.setOnClickListener(this);

        // Seekbar
        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        int playPositionInMilliseconds = (mediaFileLengthInMilliseconds / 100) * seekProgress;
                        mediaPlayer.seekTo(playPositionInMilliseconds);
                    }
                }
                return false;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO: TRY TO GET RID OF THIS UGLY HACK... or at least make it less ugly
                seekProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // song title
        playerSongTitle = (TextView) v.findViewById(R.id.playerSongTitle);

        // Media player
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
    }

    // Load list of music loaded from vk
    private void loadListFragment(){
        Log.d(TAG, "loading List Fragment");
        audioListFragment = new AudioListFragment();
        audioListFragment.setErrorTextBox(errorTextBox);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.player_cont_big, audioListFragment)
                .commit();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.playPauseButton){
            /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
            if(mediaPlayer.isPlaying()) {
                Log.d(TAG, "MediaPlayer is playing... Paused");
                isPaused = true;
                pause();
            } else {
                if(isPaused) {
                    Log.d(TAG, "Media Player is paused... Playing again");
                    isPaused = false;
                    play();
                } else {
                    Log.d(TAG, "Media Player is not ready. Preparing and starting");
                    prepareAndStart();
                    Log.d(TAG, "Song number: " + currentSongNumber);
                }
            }
        }
        if(v.getId() == R.id.skipNextbutton){
           skipNext();
        }
        if(v.getId() == R.id.skipPrevButton){
            if(mediaPlayer.getCurrentPosition() > 5000 || currentSongNumber < 1){
                replayCurrent();
            } else {
                skipPrev();
            }
        }
    }


    private void skipNext(){
        Log.d(TAG, "Skipping to next song");
        stopAndReset();
        ++currentSongNumber;
        prepareAndStart();
        Log.d(TAG, "New song number: " + currentSongNumber);
    }

    private void skipPrev(){
        Log.d(TAG, "Skipping to previous song");
        stopAndReset();
        --currentSongNumber;
        prepareAndStart();
        Log.d(TAG, "New song number: " + currentSongNumber);
    }

    private void replayCurrent(){
        Log.d(TAG, "Replay current song");
        stopAndReset();
        prepareAndStart();
        Log.d(TAG, "Current song number: " + currentSongNumber);
    }

    // Prepare song for playing
    private void prepareAndStart(){
        try {
            // Get song
            AudioItem currentSong = audioListFragment.getItem(currentSongNumber);
            // set UI stuff
            playerSongTitle.setText(currentSong.getArtist() + " - " + currentSong.getTitle());
            // Set player
            mediaPlayer.setDataSource(currentSong.getUrl());
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Play song when media player is prepared
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL
        play();
        primarySeekBarProgressUpdater();
    }

    // Obvious method is obvious
    private void stopAndReset(){
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    // Play and set pause icon
    private void play(){
        mediaPlayer.start();
        playPauseButton.setImageResource(R.drawable.ic_pause_black_48dp);
    }

    // Pause and set play icon
    private void pause(){
        mediaPlayer.pause();
        playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
    }

    // MediaPlayer onCompletion event handler. Method which calls then song playing is complete
    @Override
    public void onCompletion(MediaPlayer mp) {
        skipNext();
    }

    // This construction give a percentage of "was playing"/"song length" on seekbar
    private void primarySeekBarProgressUpdater() {
        if(seekBar != null
                && mediaPlayer != null
                && handler != null) {
            seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));
            if (mediaPlayer.isPlaying()) {
                Runnable notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater();
                    }
                };
                handler.postDelayed(notification, 250);
            }
        }
    }


    @Override
    public void onStop (){
        super.onStop();

        // Release media player
        if(mediaPlayer != null){
            mediaPlayer.release();
            Log.d(TAG, "Media player RELEASED");
        }
        else
            Log.d(TAG, "Media player is already null");
        mediaPlayer = null;
    }

}
