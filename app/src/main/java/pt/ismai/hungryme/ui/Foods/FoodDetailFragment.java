package pt.ismai.hungryme.ui.Foods;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Recipe;
import com.fatsecret.platform.model.Serving;

import java.math.BigDecimal;

import butterknife.Bind;
import pt.ismai.hungryme.Food.FoodContent;
import pt.ismai.hungryme.Food.ServingSpinnerAdapter;
import pt.ismai.hungryme.HelpingClass.APIhelper;
import pt.ismai.hungryme.HelpingClass.SQLiteOpenHelper;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;
import pt.ismai.hungryme.ui.UI.BaseActivity;
import pt.ismai.hungryme.ui.UI.BaseFragment;
import pt.ismai.hungryme.ui.WebViewActivity;


public class FoodDetailFragment extends BaseFragment {

    public static Food food;
    static long food_id;
    public static final String ARG_ITEM_ID = "item_id";
    private RecipeContent.RecipeItem recipeItem;
    private FloatingActionButton fab;
    SharedPreferences prefs;
    SQLiteOpenHelper dbRecipes;
    private Spinner spinner;
    public static ServingSpinnerAdapter spinnerAdapter;
    public EditText quantityEditText;

    public static Button directions1;
    public static TextView calories1;
    public static TextView cardTitle1;
    public static TextView protein;
    public static TextView fat;
    public static TextView carb;
    public static Toolbar toolbar;
//    @Bind(R.id.dButton)
//    Button directions;
//
//    @Bind(R.id.calories)
//    TextView calories;
//
////    @Bind(R.id.quantityEditText)
////    EditText quantityEditText;
//
//    @Bind(R.id.card_Title)
//    TextView cardTitle;
//
//    @Bind(R.id.protein)
//    TextView protein;
//
//    @Bind(R.id.fat)
//    TextView fat;
//
//    @Bind(R.id.carb)
//    TextView carb;
//
//
//    @Bind(R.id.toolbar)
//    Toolbar toolbar;
//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("onCreate","Inside Food Detail Fragment");
        setHasOptionsMenu(true);
//        FoodContent.food = null;
        spinner = new Spinner(getActivity());
        Log.d("inside","onCreate");
//        spinnerAdapter = new ServingSpinnerAdapter(getActivity());
        spinner.setAdapter(spinnerAdapter);

    }
    public void qtyEntered()
    {
        BigDecimal qty = BigDecimal.valueOf(Long.valueOf(quantityEditText.getText().toString()));
        food.getServings().get(0).setMetricServingAmount(qty);
        calories1.setText("Calories: " + food.getServings().get(0).getCalories() + " kcal");
        protein.setText("Proteins\n" + food.getServings().get(0).getProtein() + " g");
        fat.setText("Fats\n" + food.getServings().get(0).getFat() + " g");
        carb.setText("Carbs\n" + food.getServings().get(0).getCarbohydrate() + " g");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflateAndBind(inflater, container, R.layout.food_fragment_quick_detail);

        directions1 = rootView.findViewById(R.id.dButton);
        calories1 = rootView.findViewById(R.id.calories);
        protein = rootView.findViewById(R.id.protein);
        fat = rootView.findViewById(R.id.fat);
        carb = rootView.findViewById(R.id.carb);
        cardTitle1 = rootView.findViewById(R.id.card_Title);
        toolbar = rootView.findViewById(R.id.toolbar);

        quantityEditText = rootView.findViewById(R.id.quantityEditText);
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                qtyEntered();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!((BaseActivity) getActivity()).providesActivityToolbar()) {
            ((BaseActivity) getActivity()).setToolbar((Toolbar) rootView.findViewById(R.id.toolbar));
        }

        if (food != null) {
            toolbar.setTitle(food.getName());
//            collapsingToolbar.setExpandedTitleTextAppearance(R.style.toolbar_text);
            cardTitle1.setText("Enter quantity of Foo:");
//            author.setText(food.getDescription());
//            label.setText(food.getBrandName());

            calories1.setText("Calories: " + food.getServings().get(0).getCalories() + " kcal");
//            ingredients.setText(recipe.getIngredients()+ "");
            protein.setText("Proteins\n" + food.getServings().get(0).getProtein() + " g");
            fat.setText("Fats\n" + food.getServings().get(0).getFat() + " g");
            carb.setText("Carbs\n" + food.getServings().get(0).getCarbohydrate() + " g");

            Button dButton = (Button) rootView.findViewById(R.id.dButton);
            dButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webBrowser = new Intent(getActivity(), WebViewActivity.class);
                    webBrowser.putExtra("url", food.getUrl());
                    startActivity(webBrowser);
                }
            });

            dbRecipes = new SQLiteOpenHelper(getActivity());

            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String nameR = recipeItem.title;
                        String authorR = recipeItem.author;
                        String caloriesR = recipeItem.calories;
                        String ingredientsR = recipeItem.ingredients;
                        String urlR = recipeItem.url;
                        String imageR = recipeItem.photoURL;

                        dbRecipes.addRecipe(nameR,authorR, caloriesR, ingredientsR, urlR, imageR);
                        Toast.makeText(getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return rootView;
    }

    private void loadBackdrop() {
//        Glide.with(getActivity())
//                .load(food.getImages().get(0))
//                .asBitmap()
//                .centerCrop()
//                .into(new BitmapImageViewTarget(backdropImg) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        super.setResource(resource);
//                    }
//                });
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

    public static FoodDetailFragment newInstance(Long itemID) {
       FoodDetailFragment fragment = new FoodDetailFragment();
        Bundle args = new Bundle();
        args.putLong(FoodDetailFragment.ARG_ITEM_ID,itemID);
        fragment.setArguments(args);
        return fragment;
    }

    public FoodDetailFragment() {}
}
