package eu.z3r0byteapps.soniq.Containers;

import com.google.gson.annotations.SerializedName;

public class SearchResult {
    //@SerializedName vertelt de JSON library wat de naam in de JSON string is van de onderstaande variabele
    @SerializedName("confidence")
    private float confidence;
    @SerializedName("result")
    private String resultString;
    @SerializedName("song_id")
    private int songId;
    @SerializedName("success")
    private Boolean success;
    @SerializedName("time")
    private float time;
    private Song song;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
