package pt.ismai.hungryme.ui.Recipes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fatsecret.platform.model.Category;
import com.fatsecret.platform.model.Ingredient;
import com.fatsecret.platform.model.Recipe;

import butterknife.Bind;
import pt.ismai.hungryme.Food.FoodContent;
import pt.ismai.hungryme.HelpingClass.APIhelper;
import pt.ismai.hungryme.HelpingClass.SQLiteOpenHelper;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;
import pt.ismai.hungryme.ui.UI.BaseActivity;
import pt.ismai.hungryme.ui.UI.BaseFragment;
import pt.ismai.hungryme.ui.WebViewActivity;

import static pt.ismai.hungryme.ui.Recipes.RecipeListFragment.adapter;

public class RecipeDetailFragment extends BaseFragment {

    public static Recipe recipe;
    public static long recipe_id;
    public static final String ARG_ITEM_ID = "item_id";
    private RecipeContent.RecipeItem recipeItem;
    private FloatingActionButton fab;
    SharedPreferences prefs;
    SQLiteOpenHelper dbRecipes;
    public static TextView author;
    public static Button directions; public static TextView label; public static ImageView backdropImg; public static TextView cardTitle;public static TextView calories;public static TextView ingredients;public static CollapsingToolbarLayout collapsingToolbar;
//    @Bind(R.id.dButton)
//    Button directions;
//
//    @Bind(R.id.label)
//    TextView label;
//
//    @Bind(R.id.author)
//    TextView author;
//
//    @Bind(R.id.backdrop)
//    ImageView backdropImg;
//
//    @Bind(R.id.card_Title)
//    TextView cardTitle;
//
//    @Bind(R.id.calories)
//    TextView calories;
//
//    @Bind(R.id.ingredients)
//    TextView ingredients;
//
//    @Bind(R.id.collapsing_toolbar)
//    CollapsingToolbarLayout collapsingToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate","Inside Recipe Detail Fragment");
//        getActivity().recipe_id
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflateAndBind(inflater, container, R.layout.fragment_quick_detail);
        Log.d("RecipeDetailFragment Oncreate View","Inside onCreateView");

        directions =(Button) rootView.findViewById(R.id.dButton);
         label =(TextView) rootView.findViewById(R.id.label);
        backdropImg = rootView.findViewById(R.id.backdrop);
        collapsingToolbar = rootView.findViewById(R.id.collapsing_toolbar);
        cardTitle = rootView.findViewById(R.id.card_Title);
        calories = rootView.findViewById(R.id.calories);
        ingredients = rootView.findViewById(R.id.ingredients);
        author = rootView.findViewById(R.id.author);

        if (!((BaseActivity) getActivity()).providesActivityToolbar()) {
            ((BaseActivity) getActivity()).setToolbar((Toolbar) rootView.findViewById(R.id.toolbar));
        }
        if (recipe != null) {
            Log.d("RecipeDetailFragment Oncreate View","Inside Null Recipe checker");

            loadBackdrop();
            try {
                collapsingToolbar.setTitle(recipe.getName());
                collapsingToolbar.setExpandedTitleTextAppearance(R.style.toolbar_text);
                cardTitle.setText(recipe.getDescription());
                author.setText("Serves:" + String.valueOf(recipe.getNumberOfServings()));
                calories.setText("Calories: " + recipe.getServing().getCalories() + "\n" + "Cook Time: " + recipe.getCookingTime() + "m");

                String type = "";
                for(String tp:recipe.getTypes()){
                    type += tp + ",";
                }
                label.setText(type);

                String ing = "";
                for(Ingredient ig:recipe.getIngredients()){
                    ing += "-" +ig.getDescription()+"\n";
                }
                ingredients.setText(ing);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            Button dButton = (Button) rootView.findViewById(R.id.dButton);
            dButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webBrowser = new Intent(getActivity(), WebViewActivity.class);
                    webBrowser.putExtra("url", recipe.getUrl());
                    startActivity(webBrowser);
                }
            });
        }
        return rootView;
    }

    private void loadBackdrop() {
        try {
            Glide.with(getActivity())
                    .load(recipe.getImages().get(0))
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(backdropImg) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                        }
                    });
        }
        catch (Exception e){
            e.printStackTrace();
            backdropImg.setImageResource(R.drawable.casual);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sample_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static RecipeDetailFragment newInstance(Long itemID) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putLong(RecipeDetailFragment.ARG_ITEM_ID,itemID);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeDetailFragment() {}
}
