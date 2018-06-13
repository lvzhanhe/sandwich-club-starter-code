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

    TextView nameTV;
    TextView descriptionTV;
    TextView ingredientsTV;
    TextView alsoKnownTV;
    TextView originTV;

    Sandwich sandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        originTV = (TextView) findViewById(R.id.origin_tv);
        descriptionTV = (TextView) findViewById(R.id.description_tv);
        ingredientsTV = (TextView) findViewById(R.id.ingredients_tv);
        alsoKnownTV = (TextView) findViewById(R.id.also_known_tv);
        nameTV = (TextView) findViewById(R.id.name_tv);

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
        sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        nameTV.setText(sandwich.getMainName());
        descriptionTV.setText(sandwich.getDescription());
        originTV.setText(sandwich.getPlaceOfOrigin());

        List<String> ingredients = sandwich.getIngredients();
        if (ingredients.size() == 0) {
            ingredientsTV.setText("N/A");
        } else {
            StringBuilder s1 = new StringBuilder();
            for (int i = 0; i < ingredients.size(); i++) {
                s1.append(ingredients.get(i));
                s1.append(", ");
            }
            s1.deleteCharAt(s1.length() - 1);
            s1.deleteCharAt(s1.length() - 1);
            ingredientsTV.setText(s1.toString());
        }

        List<String> alsoKnown = sandwich.getAlsoKnownAs();
        if (alsoKnown.size() == 0) {
            alsoKnownTV.setText("N/A");
        } else {
            StringBuilder s1 = new StringBuilder();
            for (int i = 0; i < alsoKnown.size(); i++) {
                s1.append(alsoKnown.get(i));
                s1.append(", ");
            }
            s1.deleteCharAt(s1.length() - 1);
            s1.deleteCharAt(s1.length() - 1);
            alsoKnownTV.setText(s1.toString());
        }
    }
}
