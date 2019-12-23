package pt.ismai.hungryme.ui.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import pt.ismai.hungryme.LoginAndRegister.LoginActivity;
import pt.ismai.hungryme.LoginAndRegister.Session;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.ui.AccountActivity;
import pt.ismai.hungryme.ui.DeviceConfigActivity;
import pt.ismai.hungryme.ui.FavoritesActivity;
import pt.ismai.hungryme.ui.Foods.FoodsActivity;
import pt.ismai.hungryme.ui.MainActivity;
import pt.ismai.hungryme.ui.SettingsActivity;
import pt.ismai.hungryme.ui.Recipes.RecipesActivity;


public abstract class BaseActivity extends AppCompatActivity {
    private Session session;
    protected static final int NAV_DRAWER_ITEM_INVALID = -1;
    private DrawerLayout drawerLayout;
    private Toolbar actionBarToolbar;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String title = "";
    SharedPreferences prefs;
    String verify;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
        session = new Session(this);
    }

    private void setupNavDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout == null) {
            return;
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerSelectListener(navigationView);
            setSelectedItem(navigationView);
            View hView = navigationView.getHeaderView(0);
            TextView nav_user = (TextView) hView.findViewById(R.id.emailHeader);
            prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            verify = prefs.getString("namestring", "");

            if(!verify.matches("")) {
                nav_user.setText(prefs.getString("namestring", ""));
            } else {
                nav_user.setText(prefs.getString("EMAIL", ""));
            }
        }

    }

    private void logout(){
        session.setLoggedin(false);
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }

    private void setSelectedItem(NavigationView navigationView) {
        int selectedItem = getSelfNavDrawerItem();
        navigationView.setCheckedItem(selectedItem);
    }

    public void setupDrawerSelectListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        drawerLayout.closeDrawers();
                        onNavigationItemClicked(menuItem.getItemId());
                        return true;
                    }
                });
    }

    public void onNavigationItemClicked(final int itemId) {
        if(itemId == getSelfNavDrawerItem()) {
            closeDrawer();
            return;
        }
        goToNavDrawerItem(itemId);
    }

    public void goToNavDrawerItem(int item) {
        Intent intent1 = new Intent(this, RecipesActivity.class);
        Intent intent2 = new Intent(this, FoodsActivity.class);

        switch (item) {
            case R.id.nav_dash:
                intent1=null;
                intent2=null;
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.nav_food:
                intent1 = null;
                intent2.putExtra("search", "healthy");
                title ="Foods Search";
                intent2.putExtra("title", title);
                break;
            case R.id.nav_recipe:
                intent1.putExtra("search", "pasta");
                intent2 = null;
                title ="Recipes Search";
                intent1.putExtra("title", title);
                break;
            case R.id.nav_bt:
                intent1 = null;
                intent2 = null;
                startActivity(new Intent(this, DeviceConfigActivity.class));
                break;
                case R.id.nav_reminder:
            case R.id.nav_settings:
                intent1 = null;
                intent2 = null;
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_account:
                intent1 = null;
                intent2 = null;
                startActivity(new Intent(this, AccountActivity.class));
                break;
            case R.id.nav_logout:
                intent1 = null;
                intent2 = null;
                logout();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
            if(intent1 != null) startActivity(intent1);
            else if(intent2!=null) startActivity(intent2);
    }

    protected ActionBar getActionBarToolbar() {
        if (actionBarToolbar == null) {
            actionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (actionBarToolbar != null) {
                setSupportActionBar(actionBarToolbar);
            }
        }
        return getSupportActionBar();
    }

    protected int getSelfNavDrawerItem() {
        return NAV_DRAWER_ITEM_INVALID;
    }

    protected void openDrawer() {
        if(drawerLayout == null)
            return;
        drawerLayout.openDrawer(GravityCompat.START);
    }

    protected void closeDrawer() {
        if(drawerLayout == null)
            return;
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public abstract boolean providesActivityToolbar();

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
