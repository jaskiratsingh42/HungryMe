package pt.ismai.hungryme.HelpingClass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.CompactRecipe;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Ingredient;
import com.fatsecret.platform.model.Recipe;
import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.services.android.Request;
import com.fatsecret.platform.services.android.ResponseListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pt.ismai.hungryme.Food.FoodContent;
import pt.ismai.hungryme.Food.ServingSpinnerAdapter;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;
import pt.ismai.hungryme.ui.Foods.FoodDetailActivity;
import pt.ismai.hungryme.ui.Foods.FoodDetailFragment;
import pt.ismai.hungryme.ui.Recipes.RecipeDetailActivity;
import pt.ismai.hungryme.ui.Recipes.RecipeDetailFragment;
import pt.ismai.hungryme.ui.WebViewActivity;

import static pt.ismai.hungryme.API.Config.key;
import static pt.ismai.hungryme.API.Config.secret;
import static pt.ismai.hungryme.ui.Foods.FoodDetailActivity.cardTitle1;
import static pt.ismai.hungryme.ui.Foods.FoodDetailActivity.directions1;
import static pt.ismai.hungryme.ui.Foods.FoodDetailActivity.calories1;
import static pt.ismai.hungryme.ui.Foods.FoodDetailActivity.fat;
import static pt.ismai.hungryme.ui.Foods.FoodDetailActivity.protein;
import static pt.ismai.hungryme.ui.Foods.FoodDetailActivity.carb;
import static pt.ismai.hungryme.ui.Foods.FoodDetailActivity.toolbar;

import static pt.ismai.hungryme.ui.Recipes.RecipeDetailFragment.author;
import static pt.ismai.hungryme.ui.Recipes.RecipeDetailFragment.backdropImg;
import static pt.ismai.hungryme.ui.Recipes.RecipeDetailFragment.calories;
import static pt.ismai.hungryme.ui.Recipes.RecipeDetailFragment.cardTitle;
import static pt.ismai.hungryme.ui.Recipes.RecipeDetailFragment.collapsingToolbar;
import static pt.ismai.hungryme.ui.Recipes.RecipeDetailFragment.ingredients;
import static pt.ismai.hungryme.ui.Recipes.RecipeDetailFragment.label;
import static pt.ismai.hungryme.ui.Recipes.RecipeDetailFragment.directions;

public class APIhelper{
    private MyRecipeListAdapter recipeListAdapter;
    private MyFoodListAdapter foodListAdapter;
    public Spinner spinner;
    private Context context;
    private String method;
    public static String search;
    public static long food_id,recipe_id;

    public APIhelper(Context context,String method) {
        this.context = context; this.method = method;
        FoodContent.foods.clear(); RecipeContent.recipes.clear();
    }
    public APIhelper(Context context, String method, MyRecipeListAdapter adapter) {
        this.context = context; this.method = method; this.recipeListAdapter = adapter;
        FoodContent.foods.clear(); RecipeContent.recipes.clear();
    }
    public APIhelper(Context context, String method, MyFoodListAdapter adapter) {
        this.context = context; this.method = method; this.foodListAdapter = adapter;
        FoodContent.foods.clear(); RecipeContent.recipes.clear();
    }
    public Boolean doTask(){
        if(method.equals("getFoodItem"))
        {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                Listener listener = new Listener();
                Request req = new Request(key, secret, listener);
                req.getFood(requestQueue,food_id);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        else if (method.equals("getFoodList"))
        {
            try {
                Log.d("Search Value",search);
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                Listener listener = new Listener();
                Request req = new Request(key, secret, listener);
                req.getFoods(requestQueue,search,0);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        else if (method.equals("getRecipeItem"))
        {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                Listener listener = new Listener();
                Request req = new Request(key, secret, listener);
                req.getRecipe(requestQueue,recipe_id);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        else if (method.equals("getRecipeList"))
        {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                Listener listener = new Listener();
                Request req = new Request(key, secret, listener);
                req.getRecipes(requestQueue,search,0);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        else
            return false;
    }

    class Listener implements ResponseListener, com.android.volley.Response.ErrorListener {
        @Override
        public void onFoodListRespone(Response<CompactFood> response) {
            System.out.println("TOTAL FOOD ITEMS: " + response.getTotalResults());
            FoodContent.foods = response.getResults();
            //This list contains summary information about the food items
            for (CompactFood food: FoodContent.foods) {
                System.out.println(food.getName());
            }
            foodListAdapter.notifyDataSetChanged();
        }
        @Override
        public void onRecipeListRespone(Response<CompactRecipe> response) {
            response.setTotalResults(20);
            RecipeContent.recipes = response.getResults();
            while(RecipeContent.recipes.size()>20){
                RecipeContent.recipes.remove(RecipeContent.recipes.size()-1);
            }
            for (CompactRecipe recipe: RecipeContent.recipes) {
                System.out.println(recipe.getName());
            }
            Log.d("test","notify");
            recipeListAdapter.notifyDataSetChanged();
        }
        @Override
        public void onFoodResponse(final Food food) {
            System.out.println("FOOD NAME: " + food.getName());
            FoodContent.servingUnits.clear();
            for(int i=0; i < food.getServings().size();i++)
            {
                FoodContent.servingUnits.add(food.getServings().get(i).getMeasurementDescription());
                System.out.println("FOOD desc: " + food.getServings().get(i).getMeasurementDescription());
                System.out.println("FOOD desc: " + food.getServings().get(i).getNumberOfUnits());
                System.out.println("FOOD CARBS: " + food.getServings().get(i).getCarbohydrate());
                System.out.println("FOOD UNIT: " + food.getServings().get(i).getMetricServingUnit());
                System.out.println("FOOD CALORIES: " + food.getServings().get(i).getCalories());
                System.out.println("FOOD AMT: " + food.getServings().get(i).getMetricServingAmount());
            }
            FoodContent.food = food;
            FoodContent.servings = food.getServings();


            FoodDetailActivity.food = food;
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,FoodContent.servingUnits);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //        spinnerAdapter = new ServingSpinnerAdapter(this);
            spinner.setAdapter(spinnerAdapter);
//            ServingSpinnerAdapter.servings = food.getServings();
//            FoodDetailActivity.spinnerAdapter.notifyDataSetChanged();

            //----------
            if( toolbar != null){
                Log.d("API helper","inside if");
                toolbar.setTitle(food.getName());
                calories1.setText("Calories: " + food.getServings().get(0).getCalories() + " kcal");
                protein.setText("Proteins\n" + food.getServings().get(0).getProtein() + " g");
                fat.setText("Fats\n" + food.getServings().get(0).getFat() + " g");
                carb.setText("Carbs\n" + food.getServings().get(0).getCarbohydrate() + " g");

//            Button dButton = (Button) findViewById(R.id.dButton);
                directions1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent webBrowser = new Intent(context, WebViewActivity.class);
                        webBrowser.putExtra("url", food.getUrl());
                        context.startActivity(webBrowser);
                    }
                });
            }
            }
        @Override
        public void onRecipeResponse(final Recipe recipe) {
            System.out.println("RECIPE NAME: " + recipe.getName());
            System.out.println("RECIPE DESC: " + recipe.getDescription());
            System.out.println("RECIPE INC: " + recipe.getIngredients());
            System.out.println("RECIPE CKINGTIME: " + recipe.getCookingTime());
            System.out.println("RECIPE Rating: " + recipe.getRating());
            RecipeContent.recipe = recipe;
            RecipeDetailFragment.recipe = recipe;
            if(collapsingToolbar != null){
                Log.d("collapsing","inside if");
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
                directions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent webBrowser = new Intent(context, WebViewActivity.class);
                        webBrowser.putExtra("url", recipe.getUrl());
                        context.startActivity(webBrowser);
                    }
                });
            }
                try {
                    Glide.with(context)
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
        public void onErrorResponse(VolleyError error) {
            Log.d("onErrorResponse",error.getMessage());
        }
    }

}
