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

public class DataUtils {

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
}
