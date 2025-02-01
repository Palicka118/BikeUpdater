package com.pawel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for data operations related to motorcycle data and seen bikes.
 */
public class DataUtils {

    /**
     * Loads motorcycle data from a JSON file.
     *
     * @param filePath the path to the JSON file
     * @return a list of maps containing the motorcycle data
     */
    public static List<Map<String, Object>> loadMotorcycleData(String filePath) {
        System.out.println("Loading motorcycle data from: " + filePath);
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> data = new Gson().fromJson(reader, listType);
            System.out.println("Motorcycle data loaded: " + (data != null ? data.size() : 0) + " items");
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads seen bikes from a JSON file.
     *
     * @param filePath the path to the JSON file
     * @return a set of strings containing the seen bike IDs
     */
    public static Set<String> loadSeenBikes(String filePath) {
        System.out.println("Loading seen bikes from: " + filePath);
        try (FileReader reader = new FileReader(filePath)) {
            Type setType = new TypeToken<Set<String>>() {}.getType();
            Set<String> data = new Gson().fromJson(reader, setType);
            System.out.println("Seen bikes loaded: " + (data != null ? data.size() : 0) + " items");
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads favorite bikes from a JSON file.
     *
     * @param filePath the path to the JSON file
     * @return a set of strings containing the favorite bike IDs
     */
    public static Set<String> loadFavoriteBikes(String filePath) {
        System.out.println("Loading favorite bikes from: " + filePath);
        try (FileReader reader = new FileReader(filePath)) {
            Type setType = new TypeToken<Set<String>>() {}.getType();
            Set<String> data = new Gson().fromJson(reader, setType);
            System.out.println("Favorite bikes loaded: " + (data != null ? data.size() : 0) + " items");
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves seen bikes to a JSON file.
     *
     * @param seenBikes the set of seen bike IDs to save
     */
    public static void saveSeenBikes(Set<String> seenBikes) {
        System.out.println("Saving seen bikes...");
        try (FileWriter writer = new FileWriter("bike_search_egine/bike-search-engine/scripts/seen_bikes.json")) {
            new Gson().toJson(seenBikes, writer);
            System.out.println("Seen bikes saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a bike to the favorites list and saves it to the favorite bikes file.
     *
     * @param bikeId the unique identifier of the bike
     * @param item a map containing the motorcycle item details
     */
    public static void addToFavorites(String bikeId, Map<String, Object> item) {
        System.out.println("Adding bike " + bikeId + " to favorites.");
        Set<String> favoriteBikes = loadFavoriteBikes("bike_search_egine/bike-search-engine/scripts/favorite_bikes.json");
        if (favoriteBikes != null && favoriteBikes.add(bikeId)) {
            try (FileWriter writer = new FileWriter("bike_search_egine/bike-search-engine/scripts/favorite_bikes.json")) {
                new Gson().toJson(favoriteBikes, writer);
                System.out.println("Bike " + bikeId + " added to favorites.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes a bike from the favorites list and saves the updated list to the favorite bikes file.
     *
     * @param bikeId the unique identifier of the bike
     */
    public static void removeFromFavorites(String bikeId) {
        System.out.println("Removing bike " + bikeId + " from favorites.");
        Set<String> favoriteBikes = loadFavoriteBikes("bike_search_egine/bike-search-engine/scripts/favorite_bikes.json");
        if (favoriteBikes != null && favoriteBikes.remove(bikeId)) {
            try (FileWriter writer = new FileWriter("bike_search_egine/bike-search-engine/scripts/favorite_bikes.json")) {
                new Gson().toJson(favoriteBikes, writer);
                System.out.println("Bike " + bikeId + " removed from favorites.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Clears the JSON files for motorcycle data, seen bikes, and favorite bikes.
     */
    public static void clearJsonFiles() {
        System.out.println("Clearing JSON files...");
        try (FileWriter writer = new FileWriter("bike_search_egine/bike-search-engine/scripts/motorcycles.json")) {
            writer.write("[]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("bike_search_egine/bike-search-engine/scripts/seen_bikes.json")) {
            writer.write("[]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON files cleared.");
    }

    public static void clearFavorites() {
        try (FileWriter writer = new FileWriter("bike_search_egine/bike-search-engine/scripts/favorite_bikes.json")) {
            writer.write("[]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
