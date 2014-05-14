package com.cauzoeng.android.model;

import com.google.gson.JsonObject;

/**
 * Created by jp on 5/9/14.
 */
public class Item {
    private final String title;
    private final Double price;
    private final String currency;
    private final String description;
    private final String created_date;
    private final String url;
    private final Double lat;
    private final Double lon;

    public Item(String title, Double price, String currency, String description,
                String created_date, String url, Double lat, Double lon) {
        this.title = title;
        this.price = price;
        this.currency = currency;
        this.description = description;
        this.created_date = created_date;
        this.url = url;
        this.lat = lat;
        this.lon = lon;
    }

    private JsonObject toJson () {
        JsonObject obj = new JsonObject();
        obj.addProperty("title", this.title);
        obj.addProperty("price", this.price);
        obj.addProperty("currency", this.currency);
        obj.addProperty("description", this.description);
        obj.addProperty("created_date", this.created_date);
        obj.addProperty("url", this.url);

        if (this.lat != null && this.lon != null) {
            JsonObject location = new JsonObject();
            location.addProperty("lat", this.lat);
            location.addProperty("lon", this.lon);
            obj.add("location", location);
        }
        return obj;
    }

}
