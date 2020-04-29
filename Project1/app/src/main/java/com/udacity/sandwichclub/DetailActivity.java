package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        TextView alsoKnownTv = findViewById(R.id.also_known_tv);
        TextView originTv = findViewById(R.id.origin_tv);
        TextView descriptionTv = findViewById(R.id.description_tv);
        TextView ingredientsTv = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        List<String> alsoKnownAs = sandwich.getAlsoKnownAs();
        if (!alsoKnownAs.isEmpty()) {
            for (String anotherName : alsoKnownAs
            ) {
                alsoKnownTv.append("\u2022  ");
                alsoKnownTv.append(anotherName + "\n");
            }

        } else {
            alsoKnownTv.setText(R.string.not_available);
        }

        List<String> ingredients = sandwich.getIngredients();
        if (!ingredients.isEmpty()) {
            for (String ingredient : ingredients
            ) {
                ingredientsTv.append("\u2022  ");
                ingredientsTv.append(ingredient + "\n");
            }
        } else {
            ingredientsTv.setText(R.string.not_available);
        }

        if (!sandwich.getPlaceOfOrigin().isEmpty()) {
            originTv.setText(sandwich.getPlaceOfOrigin());
        } else {
            originTv.setText(R.string.not_available);
        }

        if (!sandwich.getDescription().isEmpty()) {
            descriptionTv.setText(sandwich.getDescription());
        } else {
            descriptionTv.setText(R.string.not_available);
        }

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {

    }
}
