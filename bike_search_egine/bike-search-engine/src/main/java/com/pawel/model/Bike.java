// src/main/java/com/pawel/model/Bike.java
package com.pawel.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Bike {

    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String url;

    @SerializedName("price")
    private String price;

    @SerializedName("year")
    private String year;

    @SerializedName("mileage")
    private String mileage;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("make")
    private String make;

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getYear() { return year; }
    public String getMileage() { return mileage; }
    public String getImageUrl() { return normalizeUrl(imageUrl); }
    public String getMake() { return make; }

    /** Normalized absolute URL (handles //... form). */
    public String getUrl() { return normalizeUrl(url); }

    /** Stable normalized ID for seen/favorites. */
    public String getId() { return getUrl(); }

    /** Backward-compat alias (in case you still have old calls). */
    public String key() { return getId(); }

    private String normalizeUrl(String raw) {
        if (raw == null) return "";
        String s = raw.trim();
        if (s.startsWith("//")) return "https:" + s;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bike bike)) return false;
        return Objects.equals(getId(), bike.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
