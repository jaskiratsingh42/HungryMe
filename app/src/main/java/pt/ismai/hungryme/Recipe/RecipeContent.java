package pt.ismai.hungryme.Recipe;

import android.content.Context;

import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.CompactRecipe;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecipeContent {
    //JS code
    private Context context;
    long recipe_id;
    public static Recipe recipe = new Recipe();
    public static Food food = new Food();
    public static List<CompactRecipe> recipes = new ArrayList<>();
    public static List<CompactFood> foods = new ArrayList<>();
    public static List<Recipe> fav = new ArrayList<>();



    //pre-written code
//    Recipe recipeItem = new Recipe();
    public static final List<RecipeItem> ITEMS = new ArrayList<>();
    public static final Map<String, RecipeItem> ITEM_MAP = new HashMap<>(7);

        private static void addFoodItem(CompactRecipe r){

        }

    private static void addItem(RecipeItem item) {
        ITEMS.add(item);
//        recipes.add(item);
        ITEM_MAP.put(item.id, item);
        ITEM_MAP.put(item.title, item);
        ITEM_MAP.put(item.url, item);
        ITEM_MAP.put(item.title, item);
        ITEM_MAP.put(item.label, item);
        ITEM_MAP.put(item.healthlabel, item);
        ITEM_MAP.put(item.ingredients, item);
        ITEM_MAP.put(String.valueOf(item.calories), item);
        ITEM_MAP.put(item.url, item);
    }

    //JS Code
    public RecipeContent(Context context) {
        this.context = context;
    }
//Pre-Written Code
        public static class RecipeItem {
        public String id;
        public String photoURL;
        public String title;
        public String author;
        public String label;
        public String ingredients;
        public String calories;
        public String url;
        public String healthlabel;

        public RecipeItem(String id, String photoURL, String title, String author, String label,String healthlabel, String ingredients, String calories, String url) {
            super();
            this.id = id;
            this.photoURL = photoURL;
            this.title = title;
            this.author = author;
            this.healthlabel = healthlabel;
            this.label = label;
            this.ingredients = ingredients;
            this.calories = calories;
            this.url= url;
        }
    }
}
