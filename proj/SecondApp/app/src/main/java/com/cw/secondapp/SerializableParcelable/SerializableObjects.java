package com.cw.secondapp.SerializableParcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class SerializableObjects implements Serializable {
    private String name;
    private int age;
    private ArrayList<String> address;

    public SerializableObjects(String name, int age, ArrayList<String> address) {
        super();
        this.name = name;
        this.age = age;
        this.address = address;
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
