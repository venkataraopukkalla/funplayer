package com.vikas.funplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SongsDetails implements Serializable,Parcelable {

    private String songTitle;
    private  String songData;
    private String songDuration;
    private String songArtist;
    private  int songNumber;

    public SongsDetails(String songTitle, String songDuration,String songData, String songArtist,int songNumber) {
        this.songTitle = songTitle;
        this.songData = songData;
        this.songDuration = songDuration;
        this.songArtist = songArtist;
        this.songNumber=songNumber;
    }

    protected SongsDetails(Parcel in) {
        songTitle = in.readString();
        songData = in.readString();
        songDuration = in.readString();
        songArtist = in.readString();
        songNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songTitle);
        dest.writeString(songData);
        dest.writeString(songDuration);
        dest.writeString(songArtist);
        dest.writeInt(songNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SongsDetails> CREATOR = new Creator<SongsDetails>() {
        @Override
        public SongsDetails createFromParcel(Parcel in) {
            return new SongsDetails(in);
        }

        @Override
        public SongsDetails[] newArray(int size) {
            return new SongsDetails[size];
        }
    };

    //
//    protected SongsDetails(Parcel in) {
//        songTitle = in.readString();
//        songData = in.readString();
//        songDuration = in.readString();
//        songArtist = in.readString();
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(songTitle);
//        dest.writeString(songData);
//        dest.writeString(songDuration);
//        dest.writeString(songArtist);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<SongsDetails> CREATOR = new Creator<SongsDetails>() {
//        @Override
//        public SongsDetails createFromParcel(Parcel in) {
//            return new SongsDetails(in);
//        }
//
//        @Override
//        public SongsDetails[] newArray(int size) {
//            return new SongsDetails[size];
//        }
//    };
//
    public String getSongTitle() {
        return songTitle;
    }

    public String getSongData() {
        return songData;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public int getSongNumber() {
        return songNumber;
    }

    @Override
    public String toString() {
        return "SongsDetails{" +
                "songTitle='" + songTitle + '\'' +
                ", songData='" + songData + '\'' +
                ", songDuration='" + songDuration + '\'' +
                ", songArtist='" + songArtist + '\'' +
                ", songNumber=" + songNumber +
                '}';
    }
}
