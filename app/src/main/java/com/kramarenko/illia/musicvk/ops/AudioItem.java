package com.kramarenko.illia.musicvk.ops;

/**
 * Created by justify on 15.06.2015.
 */
public class AudioItem {

    private String artist;
    private String title;
    private long duration;
    private String url;

    public AudioItem(){
        artist = "Unknown";
        title = "Unknown";
        duration = 0;
        url = "";
    }

    public AudioItem(String artist, String title, long duration, String url){
        this.artist = artist;
        this.title = title;
        this.duration = duration;
        this.url = url;
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

    public String getUrl() {
        return url;
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

    public void setUrl(String url) {
        this.url = url;
    }
}
