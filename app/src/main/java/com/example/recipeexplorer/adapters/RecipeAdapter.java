package com.example.recipeexplorer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.recipeexplorer.R;
import com.example.recipeexplorer.models.Recipe;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private Context context;
    private List<Recipe> recipes;
    private int selectedPosition = -1; // Track selected position

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Recipe recipe = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_recipe, parent, false);
        }

        // Lookup view for data population
        TextView textTitle = convertView.findViewById(R.id.textTitle);
        TextView textDescription = convertView.findViewById(R.id.textDescription);

        // Populate the data into the template view using the data object
        textTitle.setText(recipe.getTitle());
        textDescription.setText(recipe.getDescription());

        // Highlight the selected item
        if (position == selectedPosition) {
            convertView.setBackgroundResource(R.drawable.selected_item_background); // Example of a custom selector drawable
        } else {
            convertView.setBackgroundResource(android.R.color.transparent); // Clear background if not selected
        }

        return convertView;
    }

    // Method to update selected position
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged(); // Update the list view
    }
}
