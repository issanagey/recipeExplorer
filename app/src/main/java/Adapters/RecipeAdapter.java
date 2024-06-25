package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.recipeexplorer.R;
import model.Recipe;
import com.example.recipeexplorer.model.Recipe;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
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

        // Return the completed view to render on screen
        return convertView;
}
}
