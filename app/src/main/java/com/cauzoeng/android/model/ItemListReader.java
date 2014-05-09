package com.cauzoeng.android.model;

import android.util.Log;

import com.cauzoeng.android.Constants;
import com.cauzoeng.android.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jp on 5/9/14.
 */
public class ItemListReader {
    private final JsonObject obj;
    private final JsonArray obj_array;

    private final List<String> item_ids;
    private final List<String> users;
    private final List<String> titles;
    private final List<Double> prices;
    private final List<String> currencies;
    private final List<String> descriptions;
    private final List<String> created_dates;
    private final List<Integer> imageIds;

    public ItemListReader(JsonObject obj) {
        Log.i(Constants.JSON_TAG, ">> Obj: " + obj);
        this.obj = obj;

        this.item_ids = new ArrayList<String>();
        this.users = new ArrayList<String>();
        this.titles = new ArrayList<String>();
        this.prices = new ArrayList<Double>();
        this.currencies = new ArrayList<String>();
        this.descriptions = new ArrayList<String>();
        this.created_dates = new ArrayList<String>();
        this.imageIds = new ArrayList<Integer>();

        this.obj_array = obj.getAsJsonArray("items");

        try {
            for (JsonElement result : this.obj_array) {
                JsonObject item = result.getAsJsonObject();

                if (item.has("id")) {
                    this.item_ids.add(item.get("id").getAsString());
                } else {
                    this.item_ids.add("");
                }

                if (item.has("user")) {
                    this.users.add(item.get("user").getAsString());
                } else {
                    this.users.add("");
                }

                if (item.has("title")) {
                    this.titles.add(item.get("title").getAsString());
                } else {
                    this.titles.add("");
                }

                if (item.has("price")) {
                    this.prices.add(item.get("price").getAsDouble());
                } else {
                    this.prices.add(0.0);
                }

                if (item.has("currency")) {
                    this.currencies.add(item.get("currency").getAsString());
                } else {
                    this.currencies.add("");
                }

                if (item.has("description")) {
                    this.descriptions.add(item.get("description").getAsString());
                } else {
                    this.descriptions.add("");
                }

                if (item.has("created_date")) {
                    this.created_dates.add(item.get("created_date").getAsString());
                } else {
                    this.created_dates.add("");
                }

                this.imageIds.add(R.drawable.ic_launcher);
            }
            Log.d(Constants.JSON_TAG, "Successful parsing GSON.");
        } catch (JsonParseException e) {
            Log.e(Constants.JSON_TAG, "Could not parse GSON: " + e.toString());
        }
    }

    private int getLength() {
        return this.obj_array.size();
    }

    public String[] getItemIds() {
        return this.item_ids.toArray(new String[this.getLength()]);
    }

    public String[] getTitles() {
        return this.titles.toArray(new String[this.getLength()]);
    }

    public Double[] getPrices() {
        return this.prices.toArray(new Double[this.getLength()]);
    }

    public String[] getCurrencies() {
        return this.currencies.toArray(new String[this.getLength()]);
    }

    public String[] getDescriptions() {
        return this.descriptions.toArray(new String[this.getLength()]);
    }

    public String[] getCreateDates() {
        return this.created_dates.toArray(new String[this.getLength()]);
    }

    public Integer[] getImageIds() {
        return this.imageIds.toArray(new Integer[this.getLength()]);
    }
}
