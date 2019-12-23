package pt.ismai.hungryme.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.ui.Foods.FoodsActivity;
import pt.ismai.hungryme.ui.Recipes.RecipesActivity;
import pt.ismai.hungryme.ui.UI.BaseActivity;

public class MainActivity extends BaseActivity {

    @Bind(R.id.button1)
    Button recipe;
    @Bind(R.id.button2)
    Button food;
    @Bind(R.id.button3)
    Button reminder;
    @Bind(R.id.button4)
    Button about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        setupToolbar();
        recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecipesActivity.class));
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FoodsActivity.class));
            }
        });
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DeviceConfigActivity.class));
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
        getSupportActionBar().setTitle("DashBoard");
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
        return R.id.nav_dash;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

}
