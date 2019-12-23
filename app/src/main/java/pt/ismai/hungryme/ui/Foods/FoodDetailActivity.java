package pt.ismai.hungryme.ui.Foods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fatsecret.platform.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.ismai.hungryme.Food.FoodContent;
import pt.ismai.hungryme.Food.ServingSpinnerAdapter;
import pt.ismai.hungryme.HelpingClass.APIhelper;
import pt.ismai.hungryme.HelpingClass.SQLiteOpenHelper;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;
import pt.ismai.hungryme.ui.UI.BaseActivity;
import pt.ismai.hungryme.ui.Utils;
import pt.ismai.hungryme.ui.WebViewActivity;

import static pt.ismai.hungryme.ui.Foods.FoodDetailFragment.food_id;

public class FoodDetailActivity extends BaseActivity {

    private String TAG = RecipeContent.class.getSimpleName();
    public static Food food;
    static long food_id;
    public static final String ARG_ITEM_ID = "item_id";
    private RecipeContent.RecipeItem recipeItem;
    private FloatingActionButton fab;
    SharedPreferences prefs;
    SQLiteOpenHelper dbRecipes;
    public static ServingSpinnerAdapter spinnerAdapter;
    int spinnerClickedIndex;
    public String btData;

    public static Button directions1;
    public static TextView calories1;
    public static TextView cardTitle1;
    public static TextView protein;
    public static TextView fat;
    public static TextView carb;
    public static Toolbar toolbar;

//    @Bind(R.id.dButton)
//    Button dButton;
//
//    @Bind(R.id.calories)
//    TextView calories;
//
    @Bind(R.id.quantityEditText)
    EditText quantityEditText;

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
    @Bind(R.id.spinner)
    Spinner spinner;

//    @Bind(R.id.toolbar)
//    Toolbar toolbar;

    FirebaseDatabase database;
    DatabaseReference myRef;
    Double weightVal;

    public static void getFoodItem(Context context ){
        APIhelper rad = new APIhelper(context,"getFoodItem");
        APIhelper.food_id = food_id;
        rad.doTask();
        ServingSpinnerAdapter.servings = FoodContent.food.getServings();
        Log.d("inside","getFoodItem");
//        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
     }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.food_fragment_quick_detail);
        setupToolbar();
        ButterKnife.bind(this);

        directions1 =(Button) findViewById(R.id.dButton);
        toolbar = findViewById(R.id.toolbar);
        cardTitle1 = findViewById(R.id.card_Title);
        calories1 = findViewById(R.id.calories);
        protein = findViewById(R.id.protein);
        carb = findViewById(R.id.carb);
        fat = findViewById(R.id.fat);

        food_id = getIntent().getLongExtra("food_id",0);
        APIhelper rad = new APIhelper(this,"getFoodItem");

        APIhelper.food_id = food_id;
        rad.spinner = spinner;
        rad.doTask();

        if (food != null) {
            try{
                toolbar.setTitle(food.getName());
            }
            catch (Exception e){
                e.printStackTrace();
            }
            calories1.setText("Calories: " + food.getServings().get(0).getCalories() + " kcal");
            protein.setText("Proteins\n" + food.getServings().get(0).getProtein() + " g");
            fat.setText("Fats\n" + food.getServings().get(0).getFat() + " g");
            carb.setText("Carbs\n" + food.getServings().get(0).getCarbohydrate() + " g");

//            Button dButton = (Button) findViewById(R.id.dButton);
            directions1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webBrowser = new Intent(FoodDetailActivity.this, WebViewActivity.class);
                    webBrowser.putExtra("url", food.getUrl());
                    startActivity(webBrowser);
                }
            });
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerClickedIndex = position;

                Log.d("log_pos",spinnerClickedIndex+","+ServingSpinnerAdapter.servings.size());
                if(spinner.getSelectedItem().toString().equals("g")){
                    quantityEditText.setEnabled(false);
                    Log.d("log_cgk","vakue");
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("food_weight");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            weightVal = dataSnapshot.getValue(Double.class);

                            quantityEditText.setText(String.valueOf(weightVal));

                            Log.d("log_weight",String.valueOf(weightVal));

                            calories1.setText("Calories: " + String.valueOf(food.getServings().get(spinnerClickedIndex).getCalories().multiply(BigDecimal.valueOf(weightVal))) + " kcal");
                            protein.setText("Proteins\n" + (food.getServings().get(spinnerClickedIndex).getProtein().multiply(BigDecimal.valueOf(weightVal))) + " g");
                            fat.setText("Fats\n" + (food.getServings().get(spinnerClickedIndex).getFat().multiply(BigDecimal.valueOf(weightVal)) + " g"));
                            carb.setText("Carbs\n" + (food.getServings().get(spinnerClickedIndex).getCarbohydrate().multiply(BigDecimal.valueOf(weightVal))) + " g");
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
                else
                    quantityEditText.setEnabled(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        calories1.setText("Calories: " + String.valueOf(food.getServings().get(spinnerClickedIndex).getCalories().multiply(BigDecimal.valueOf(Double.valueOf(s.toString())))) + " kcal");
                        protein.setText("Proteins\n" + (food.getServings().get(spinnerClickedIndex).getProtein().multiply(BigDecimal.valueOf(Double.valueOf(s.toString()))) + " g"));
                        fat.setText("Fats\n" + (food.getServings().get(spinnerClickedIndex).getFat().multiply(BigDecimal.valueOf(Double.valueOf(s.toString()))) + " g"));
                        carb.setText("Carbs\n" + (food.getServings().get(spinnerClickedIndex).getCarbohydrate().multiply(BigDecimal.valueOf(Double.valueOf(s.toString()))) + " g"));
                    }
                }
        });
            if (!checkConnection(this)) {
                Toast.makeText(this, "Network Connection Failed.\nHungryMe requires an active internet connection.\tPlease check your device settings.", Toast.LENGTH_LONG).show();
            }
        }
    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_food;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

}
