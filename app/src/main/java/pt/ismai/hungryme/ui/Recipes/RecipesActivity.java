package pt.ismai.hungryme.ui.Recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import pt.ismai.hungryme.R;
import pt.ismai.hungryme.ui.UI.BaseActivity;
import pt.ismai.hungryme.ui.Utils;

import static pt.ismai.hungryme.Recipe.RecipeContent.recipes;

public class RecipesActivity extends BaseActivity implements RecipeListFragment.Callback {
    private boolean twoPaneMode;
    MaterialSearchView searchView;
    String search;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_list);
        setupToolbar();

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()!=0)
                {
                    search = query;
                    Log.d("RecipesActivity-onQueryTextSubmit",search);
                    RecipeListFragment.getSearchQuery(search);
                    RecipeListFragment.getListData(RecipesActivity.this);
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
        try {
            Intent detailIntent = new Intent(this, RecipeDetailActivity.class);
            detailIntent.putExtra("recipe_id",id);
            startActivityForResult(detailIntent,0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
        getSupportActionBar().setTitle("Recipe Search");
    }

    private void setupDetailFragment() {
        RecipeDetailFragment fragment =  RecipeDetailFragment.newInstance(recipes.get(0).getId());
        getFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragment).commit();
    }

    private void enableActiveItemState() {
        Log.d("enableActiveItemState","example");
        RecipeListFragment.getSearchQuery(search);
        RecipeListFragment fragmentById = (RecipeListFragment) getFragmentManager().findFragmentById(R.id.recipe_list);
       fragmentById.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private boolean isTwoPaneLayoutUsed() {
        return findViewById(R.id.recipe_detail_container) == null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
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
        return 0;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }


}
