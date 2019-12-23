package pt.ismai.hungryme.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fatsecret.platform.model.Recipe;

import java.util.ArrayList;

import pt.ismai.hungryme.LoginAndRegister.ServerHelper;
import pt.ismai.hungryme.Recipe.FavoriteRecipe;
import pt.ismai.hungryme.HelpingClass.FavoriteListAdapter;
import pt.ismai.hungryme.HelpingClass.SQLiteOpenHelper;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;
import pt.ismai.hungryme.ui.UI.BaseActivity;

public class FavoritesActivity extends BaseActivity {

    ListView lv;
    ArrayList<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_favorites);
        setupToolbar();

//        SQLiteOpenHelper db = new SQLiteOpenHelper(this);

        lv = (ListView) findViewById(R.id.lv);

//        ServerHelper helper = new ServerHelper(this);
//        if(helper.favRequest("retrieve"))
//        {
//
//        }
        recipes = (ArrayList<Recipe>) RecipeContent.fav;

        FavoriteListAdapter favoriteListAdapter = new FavoriteListAdapter(this,R.layout.list_item_recipe, recipes);
        lv.setAdapter(favoriteListAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent webBrowser = new Intent(getBaseContext(), WebViewActivity.class);
                webBrowser.putExtra("url", "");
                startActivity(webBrowser);
            }
        });

        if(!checkConnection(this))
        {
            Toast.makeText(this,"Network Connection Failed.\nHungryMe requires an active internet connection.\tPlease check your device settings.",Toast.LENGTH_LONG).show();
        }
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favorite Recipes");
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
    public boolean providesActivityToolbar() {
        return true;
    }

}
