package com.etaTech.RandomAccess;

import java.io.*;
import java.util.*;

/****************************************************
 *** Created by Fady Fouad on 6/5/2019 at 12:32.***
 ***************************************************/
public class Locations implements Map<Integer, Location> {
    private static Map<Integer, Location> locationMap = new LinkedHashMap<>();
    private static Map<Integer, IndexRecord> index = new LinkedHashMap<>();
    private static RandomAccessFile accessFile;

    public static void main(String[] args) throws IOException {


        try (RandomAccessFile randomAccessFile = new RandomAccessFile("locations_rand.dat","rwd")) {
            randomAccessFile.writeInt(locationMap.size());
            int indexSize = locationMap.size()*3*Integer.BYTES;
            int locStart = (int)(indexSize+randomAccessFile.getFilePointer()+Integer.BYTES);
            randomAccessFile.writeInt(locStart);
            long indexStart = randomAccessFile.getFilePointer();
            int startPointer = locStart;
            randomAccessFile.seek(startPointer);
            for (Location location :
                    locationMap.values()) {
                randomAccessFile.writeInt(location.getLocationID());
                randomAccessFile.writeUTF(location.getDesc());
                StringBuilder stringBuilder = new StringBuilder();
                for (String dir :
                        location.getExits().keySet()) {
                    if (!dir.equalsIgnoreCase("Q")){
                        stringBuilder.append(",");
                        stringBuilder.append(location.getExits().get(dir));
                        stringBuilder.append(",");
                    }
                }
                randomAccessFile.writeUTF(stringBuilder.toString());
                IndexRecord indexRecord = new IndexRecord(startPointer, (int) (randomAccessFile.getFilePointer()-startPointer));
                index.put(location.getLocationID(),indexRecord);
                startPointer = (int) randomAccessFile.getFilePointer();

            }
            randomAccessFile.seek(indexSize);
            for (Integer locationID :
                    index.keySet()) {
                randomAccessFile.writeInt(locationID);
                randomAccessFile.writeInt(index.get(locationID).getStartBytes());
                randomAccessFile.writeInt(index.get(locationID).getLength());
            }
        }
    }

    static {
        try {
            accessFile = new RandomAccessFile("locations_rand.dat","rwd");
            int numLoc= accessFile.readInt();
            long locationStartPoint = accessFile.readInt();
            while (accessFile.getFilePointer()<locationStartPoint){
                int locationID = accessFile.readInt();
                int locationStart = accessFile.readInt();
                int locationLegnth = accessFile.readInt();
                IndexRecord record = new IndexRecord(locationStart,locationLegnth);
                index.put(locationID,record);
            }

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public Location getLocation(int locationId) throws IOException {

        IndexRecord record = index.get(locationId);
        accessFile.seek(record.getStartBytes());
        int id = accessFile.readInt();
        String description = accessFile.readUTF();
        String exits = accessFile.readUTF();
        String[] exitPart = exits.split(",");

        Location location = new Location(locationId, description, null);

        if(locationId != 0) {
            for(int i=0; i<exitPart.length; i++) {
                System.out.println("exitPart = " + exitPart[i]);
                System.out.println("exitPart[+1] = " + exitPart[i+1]);
                String direction = exitPart[i];
                int destination = Integer.parseInt(exitPart[++i]);
                location.addExit(direction, destination);
            }
        }

        return location;
    }

    @Override
    public int size() {
        return locationMap.size();
    }

    @Override
    public boolean isEmpty() {
        return locationMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return locationMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return locationMap.containsKey(value);
    }

    @Override
    public Location get(Object key) {
        return locationMap.get(key);
    }

    @Override
    public Location put(Integer key, Location value) {
        return locationMap.put(key, value);
    }

    @Override
    public Location remove(Object key) {
        return locationMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Location> m) {


    }

    @Override
    public void clear() {

        locationMap.clear();

    }

    @Override
    public Set<Integer> keySet() {
        return locationMap.keySet();
    }

    @Override
    public Collection<Location> values() {
        return locationMap.values();
    }

    @Override
    public Set<Entry<Integer, Location>> entrySet() {
        return locationMap.entrySet();
    }

    public void close()throws IOException{
        accessFile.close();
    }
}