package pt.ismai.hungryme.ui.Recipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import pt.ismai.hungryme.HelpingClass.APIhelper;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;
import pt.ismai.hungryme.ui.UI.BaseActivity;
import pt.ismai.hungryme.ui.Utils;

public class RecipeDetailActivity extends BaseActivity {

    private String TAG = RecipeContent.class.getSimpleName();
    public static long recipe_id;

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
    public static void getRecipeItem(Context context){
        APIhelper rad = new APIhelper(context,"getRecipeItem");
        APIhelper.recipe_id = recipe_id;
        rad.doTask();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_detail);
        Intent i = getIntent();
        recipe_id = (i.getLongExtra("recipe_id",0));
        getRecipeItem(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        RecipeDetailFragment.getRecipeItem(this,RecipeDetailFragment.class);
        RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(getIntent().getLongExtra(RecipeDetailFragment.ARG_ITEM_ID, 0));
        getFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragment).commit();

        if(!checkConnection(this))
        {
            Toast.makeText(this,"Network Connection Failed.\nHungryMe requires an active internet connection.\tPlease check your device settings.",Toast.LENGTH_LONG).show();
        }
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
        return R.id.nav_recipe;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

}
