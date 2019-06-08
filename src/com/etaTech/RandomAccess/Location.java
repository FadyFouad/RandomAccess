package com.etaTech.RandomAccess;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/****************************************************
 *** Created by Fady Fouad on 5/9/2019 at 4:45 PM.***
 ***************************************************/

public class Location implements Serializable {
    private final int locationID;
    private final String desc;
    private final Map<String, Integer> exits;
    private long serialVersionUID = 1L;

    public int getLocationID() {
        return locationID;
    }

    public String getDesc() {
        return desc;
    }

    public Map<String, Integer> getExits() {
        return new HashMap<>(exits);//cant change outside this class
    }

    public Location(int locationID, String desc, Map<String, Integer> exits) {
        this.locationID = locationID;
        this.desc = desc;
        this.exits = new HashMap<>(exits);
        this.exits.put("Q", 0);
    }

    protected void addExit(String dir, int destenation) {
        exits.put(dir, destenation);
    }
}