package me.antox.funplayer.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "song")
public class SongModel {

    public enum SongGenre{
        POP("Pop"),
        ROCK("Rock"),
        RAP("Rap"),
        CLASSIC("Classic"),
        HOUSE("House"),
        DUBSTEP("Dubstep"),
        BLUES("Blues");

        String readableName;

        SongGenre(String readableName)
        {
            this.readableName = readableName;
        }

        @Override
        public String toString() {
            return this.readableName;
        }
    }

    @NonNull
    @PrimaryKey
    private String name;

    @NonNull
    private String url;

    @NonNull
    private String artBackground;

    @ColumnInfo(name = "genre")
    private String genre;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    @NonNull
    public String getArtBackground() {
        return artBackground;
    }

    public void setArtBackground(@NonNull String artBackground) {
        this.artBackground = artBackground;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public SongModel(){

    }

    @Ignore
    public SongModel(@NonNull String name, @NonNull String url, @NonNull String artBackground, String genre) {
        this.name = name;
        this.url = url;
        this.artBackground = artBackground;
        this.genre = genre;
    }
}
