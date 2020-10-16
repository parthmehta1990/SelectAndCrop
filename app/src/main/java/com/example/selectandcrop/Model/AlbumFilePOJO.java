package com.example.selectandcrop.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class AlbumFilePOJO implements Parcelable {

    private String mMimeType;
    private long mSize;
    private String mPath;

    public AlbumFilePOJO(Parcel in) {
        mMimeType = in.readString();
        mSize = in.readLong();
        mPath = in.readString();
    }

    public static final Creator<AlbumFilePOJO> CREATOR = new Creator<AlbumFilePOJO>() {
        @Override
        public AlbumFilePOJO createFromParcel(Parcel in) {
            return new AlbumFilePOJO(in);
        }

        @Override
        public AlbumFilePOJO[] newArray(int size) {
            return new AlbumFilePOJO[size];
        }
    };

    public AlbumFilePOJO() {

    }

    public String getmMimeType() {
        return mMimeType;
    }

    public void setmMimeType(String mMimeType) {
        this.mMimeType = mMimeType;
    }

    public long getmSize() {
        return mSize;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMimeType);
        dest.writeLong(mSize);
        dest.writeString(mPath);
    }
}
