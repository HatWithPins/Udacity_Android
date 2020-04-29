package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        try {
            JSONObject sandwichJson = new JSONObject(json);
            JSONObject name = sandwichJson.getJSONObject("name");
            String mainName = name.getString("mainName");
            JSONArray alsoKnownAsList = name.getJSONArray("alsoKnownAs");
            List<String> alsoKnownAs= new ArrayList<String>();
            if(alsoKnownAsList != null){
                int len = alsoKnownAsList.length();
                for (int i = 0; i < len; i++){
                    alsoKnownAs.add(alsoKnownAsList.getString(i));
                }
            }
            else {
                alsoKnownAs.add("");
            }
            String placeOfOrigin = sandwichJson.getString("placeOfOrigin");
            String description = sandwichJson.getString("description");
            String image = sandwichJson.getString("image");
            JSONArray ingredientsList = sandwichJson.getJSONArray("ingredients");
            List<String> ingredients = new ArrayList<String>();
            if(ingredientsList != null) {
                int len = ingredientsList.length();
                for (int i = 0; i < len; i++) {
                    ingredients.add(ingredientsList.getString(i));
                }
            }
            else {
                ingredients.add("");
            }
            Sandwich sandwich = new Sandwich(mainName, alsoKnownAs, placeOfOrigin,description,image,ingredients);
            return sandwich;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
