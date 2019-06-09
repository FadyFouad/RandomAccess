package com.etaTech.RandomAccess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/****************************************************
 *** Created by Fady Fouad on 5/9/2019 at 4:44 PM.***
 ***************************************************/
public class Main {
    private static Locations locationMap  = new Locations();

    public static void main(String[] args) throws IOException {
        System.out.println("\n============================= Adventure =========================\n");
        Scanner scanner = new Scanner(System.in);


        Map<String ,String> vocap = new HashMap();
        vocap.put("QUIT","Q");
        vocap.put("WEST","W");
        vocap.put("EAST","E");
        vocap.put("SOUTH","S");
        vocap.put("NORTH","N");

        int location = 1;
        Location currentLocaction = locationMap.getLocation(location);
        while (true) {
            System.out.println(currentLocaction.getDesc());
            if (currentLocaction.getLocationID() == 0) {
                break;
            }
            Map<String, Integer> exits = new HashMap(locationMap.get(location).getExits());

            for (String exit :
                    exits.keySet()) {
                System.out.println(exit + " -> " + " " + locationMap.get(exits.get(exit)).getDesc());
            }
            String dir = scanner.nextLine().toUpperCase();
            if (dir.length()>1){
                String[] words = dir.split(" ");
                for (String word:
                     words) {
                    if (vocap.containsKey(word)){
                        dir = vocap.get(word);
                        break;
                    }
                }
            }
            if (exits.containsKey(dir)) {
                currentLocaction = locationMap.getLocation(currentLocaction.getExits().get(dir));
            } else {
                System.out.println("You Cannt Go There");
            }
            if (!locationMap.containsKey(dir)) {
                System.out.println("Location Not Available");
                break;
            }
        locationMap.close();
        }

    }
}