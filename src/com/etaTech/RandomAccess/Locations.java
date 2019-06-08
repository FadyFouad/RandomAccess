package com.etaTech.RandomAccess;

import java.io.*;
import java.util.*;

/****************************************************
 *** Created by Fady Fouad on 6/5/2019 at 12:32.***
 ***************************************************/
public class Locations implements Map<Integer, Location> {
    private static Map<Integer, Location> locationMap = new LinkedHashMap<>();
    private static Map<Integer, IndexRecord> index = new LinkedHashMap<>();

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
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream("locations.dat")))) {
            boolean eof = false;
            while (!eof) {
                try {
                    Location location = (Location) locFile.readObject();
                    System.out.println("Read location " + location.getLocationID() + " : " + location.getDesc());
                    System.out.println("Found " + location.getExits().size() + " exits");
                    locationMap.put(location.getLocationID(), location);
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader("locations_big.txt")))) {
            scanner.useDelimiter(",");
            while (scanner.hasNextLine()) {
                int loc = scanner.nextInt();
                scanner.skip(scanner.delimiter());
                String description = scanner.nextLine();
                System.out.println("Imported loc: " + loc + ": " + description);
                Map<String, Integer> tempExit = new HashMap<>();
                locationMap.put(loc, new Location(loc, description, tempExit));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader dirFile = new BufferedReader(new FileReader("directions_big.txt"))) {
            String input;
            while ((input = dirFile.readLine()) != null) {
                String[] data = input.split(",");
                int loc = Integer.parseInt(data[0]);
                String direction = data[1];
                int destination = Integer.parseInt(data[2]);

                System.out.println(loc + ": " + direction + ": " + destination);
                Location location = locationMap.get(loc);
                location.addExit(direction, destination);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}