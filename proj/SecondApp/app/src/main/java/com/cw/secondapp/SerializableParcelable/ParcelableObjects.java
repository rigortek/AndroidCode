package com.cw.secondapp.SerializableParcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

//https://stackoverflow.com/questions/3323074/android-difference-between-parcelable-and-serializable/23647471#23647471

public class ParcelableObjects implements Parcelable {
    private String name;
    private int age;
    private ArrayList<String> address;

    public ParcelableObjects(String name, int age, ArrayList<String> address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    protected ParcelableObjects(Parcel in) {
        name = in.readString();
        age = in.readInt();
        address = in.createStringArrayList();
    }

    public static final Creator<ParcelableObjects> CREATOR = new Creator<ParcelableObjects>() {
        @Override
        public ParcelableObjects createFromParcel(Parcel in) {
            return new ParcelableObjects(in);
        }

        @Override
        public ParcelableObjects[] newArray(int size) {
            return new ParcelableObjects[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeStringList(address);
    }

    public ArrayList<String> getAddress() {
        if (!(address == null))
            return address;
        else
            return new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
