package eu.z3r0byteapps.soniq.Containers;

import com.google.gson.annotations.SerializedName;

public class Song {
    //@SerializedName vertelt de JSON library wat de naam in de JSON string is van de onderstaande variabele
    @SerializedName("title")
    private String title;
    @SerializedName("artist")
    private String artist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
