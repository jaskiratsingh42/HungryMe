package pt.ismai.hungryme.ui.Foods;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import pt.ismai.hungryme.Food.FoodContent;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.ui.Recipes.RecipeDetailActivity;
import pt.ismai.hungryme.ui.Recipes.RecipeDetailFragment;
import pt.ismai.hungryme.ui.Recipes.RecipeListFragment;
import pt.ismai.hungryme.ui.Recipes.RecipesActivity;
import pt.ismai.hungryme.ui.UI.BaseActivity;
import pt.ismai.hungryme.ui.Utils;

public class FoodsActivity extends BaseActivity implements FoodListFragment.Callback {
    MaterialSearchView searchView;
    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.food_activity_list);
        setupToolbar();

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                 if(query.length()!=0)
                {
                    FoodContent.foods.clear();
                    search = query;
                    Log.d("FoodsActivity-onQueryTextSubmit",search);
                    FoodListFragment.getSearchQuery(search);
                    FoodListFragment.getListData(FoodsActivity.this);
//                    RecipeListFragment fragmentById = (RecipeListFragment) getFragmentManager().findFragmentById(R.id.recipe_list);
//                    fragmentById.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);


                    //                    if (isTwoPaneLayoutUsed()) {
//                        Log.d("RecipesActivity","Inside IsTwoPaneLayoutUsed()");
//                        twoPaneMode = true;
//                        enableActiveItemState();
//                    }
//                    if (savedInstanceState == null) {
//                        Log.d("RecipesActivity","Inside ");
//                        setupDetailFragment();
//                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
        if(!checkConnection(this))
        {
            Toast.makeText(this,"Network Connection Failed.\nHungryMe requires an active internet connection.\tPlease check your device settings.",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onItemSelected(Long id) {

        Intent detailIntent = new Intent(this, FoodDetailActivity.class);
        detailIntent.putExtra("food_id", id);
        startActivityForResult(detailIntent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            recreate();
        }
    }
    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Food Search");
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected int getSelfNavDrawerItem() {
        return 0;
    }
    public boolean providesActivityToolbar() {
        return true;
    }

}
