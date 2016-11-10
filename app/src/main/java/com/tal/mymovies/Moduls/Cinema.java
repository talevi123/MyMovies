package com.tal.mymovies.Moduls;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by ronen_abraham on 11/9/16.
 */

public class Cinema implements Parcelable {

    private long id;
    private String name;
    private String address;
    private List<Movie> movies;

    public Cinema(JSONObject jsonObject) {

    }

    protected Cinema(Parcel in) {
        id = in.readLong();
        name = in.readString();
        address = in.readString();
        int moviesSize = in.readInt();
        for (int i = 0; i < moviesSize; i++) {
            movies.add((Movie) in.readSerializable());
        }
    }

    public static final Creator<Cinema> CREATOR = new Creator<Cinema>() {
        @Override
        public Cinema createFromParcel(Parcel in) {
            return new Cinema(in);
        }

        @Override
        public Cinema[] newArray(int size) {
            return new Cinema[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeInt(movies.size());
        for (Movie movie : movies) {
            dest.writeSerializable(movie);
        }
    }
}
