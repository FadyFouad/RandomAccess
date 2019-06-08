package com.etaTech.RandomAccess;

/****************************************************
 *** Created by Fady Fouad on 6/9/2019 at 01:06.***
 ***************************************************/
public class IndexRecord {
    private int startBytes;
    private int length;

    public IndexRecord(int startBytes, int length) {
        this.startBytes = startBytes;
        this.length = length;
    }

    public int getStartBytes() {
        return startBytes;
    }

    public void setStartBytes(int startBytes) {
        this.startBytes = startBytes;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}