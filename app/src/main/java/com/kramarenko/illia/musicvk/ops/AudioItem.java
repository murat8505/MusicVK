package com.kramarenko.illia.musicvk.ops;

/**
 * Created by justify on 15.06.2015.
 */
public class AudioItem {

    private String artist;
    private String title;
    private long duration;

    public AudioItem(){
        artist = "Unknown";
        title = "Unknown";
        duration = 0;
    }

    public AudioItem(String artist, String title, long duration){
        this.artist = artist;
        this.title = title;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
