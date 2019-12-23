package pt.ismai.hungryme.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import de.hdodenhof.circleimageview.CircleImageView;
import pt.ismai.hungryme.LoginAndRegister.ServerHelper;
import pt.ismai.hungryme.LoginAndRegister.Session;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.ui.UI.BaseActivity;

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private TextView nav_user;
    public static final String MyPREFERENCES = "MyPrefs";
    FloatingActionButton btnSelectImage;
    CircleImageView imgView;
    Spinner spinner, spinnerHealth;
    SharedPreferences prefs;
    String verify;
    private FloatingActionButton buttonChoose;
    EditText name,age,weight,goalWeight,height;
    void fetchData(){
        ServerHelper.accountMethod ="retrieve";
        ServerHelper.email_id = prefs.getString("EMAIL", "");
        ServerHelper helper = new ServerHelper(AccountActivity.this);
        helper.accountRequest("retrieve");
        name.setText(ServerHelper.resultname);
        age.setText(ServerHelper.resultage);
        height.setText(ServerHelper.resultheight);
        weight.setText(ServerHelper.resultlastweight_kg);
        goalWeight.setText(ServerHelper.resultgoalweight_kg);

        name.setText(prefs.getString("namestring", ""));
        verify = name.getText().toString();

        if (!verify.matches("")) {
            nav_user.setText(prefs.getString("namestring", ""));
        } else {
            nav_user.setText(prefs.getString("EMAIL", ""));
        }
        goalWeight.setText(prefs.getString("goalweightstring", ""));
        height.setText(prefs.getString("heightstring", ""));
        age.setText(prefs.getString("agestring", ""));
        weight.setText(prefs.getString("weightstring", ""));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_account);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setupToolbar();

        btnSelectImage = (FloatingActionButton) findViewById(R.id.btnSelectImage);
        imgView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.user_profile_photo);

        final SharedPreferences preferences = getSharedPreferences("myprefs",MODE_PRIVATE);
        String img_str = preferences.getString("userphoto2", "");
        if (!img_str.equals("")){
            String base = img_str;
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            imgView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length) );
        }
        buttonChoose = (FloatingActionButton) findViewById(R.id.btnSelectImage);
        nav_user = (TextView) findViewById(R.id.user_profile_email);
        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.diet_labels, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner = (Spinner) findViewById(R.id.diet_spinner);
//        spinner.setAdapter(adapter);
//        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.health_labels, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerHealth = (Spinner) findViewById(R.id.health_spinner);
//        spinnerHealth.setAdapter(adapter2);

        buttonChoose.setOnClickListener(this);

        name = (EditText) findViewById(R.id.txtName);
        age = (EditText) findViewById(R.id.txtAge);
        weight = (EditText) findViewById(R.id.txtWeight);
        goalWeight = (EditText) findViewById(R.id.txtGoalWeight);
        height = (EditText) findViewById(R.id.txtHeight);
        fetchData();

        Button submit = (Button) findViewById(R.id.btnSubmit);
        Button cancel = (Button) findViewById(R.id.btnCancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namestring = name.getText().toString();
                String agestring = age.getText().toString();
                String weightstring = weight.getText().toString();
                String goalweightstring = goalWeight.getText().toString();
                String heightstring = height.getText().toString();

                SharedPreferences.Editor editor = prefs.edit();

                ServerHelper.accountMethod ="update";
                ServerHelper.email_id = prefs.getString("EMAIL", "");
                ServerHelper.name = namestring;
                ServerHelper.age = Integer.valueOf(agestring);
                ServerHelper.height = Double.valueOf(heightstring);
                ServerHelper.last_weight = Double.valueOf(weightstring);
                ServerHelper.goal_weight = Double.valueOf(goalweightstring);
                ServerHelper helper = new ServerHelper(AccountActivity.this);
                helper.accountRequest("update");
                fetchData();

                editor.putString("namestring", namestring);
                editor.putString("agestring", agestring);
                editor.putString("weightstring", weightstring);
                editor.putString("goalweightstring", goalweightstring);
                editor.putString("heightstring", heightstring);

                verify = name.getText().toString();
                if (!verify.matches("")) {
                    nav_user.setText(name.getText().toString());
                    startActivity(new Intent(getBaseContext(), AccountActivity.class));
                } else {
                    nav_user.setText(prefs.getString("EMAIL", ""));
                }

                setProfileImage(view);
                editor.commit();
                Toast.makeText(getBaseContext(), "Submitted", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                height.setText("");
                weight.setText("");
                age.setText("");
                goalWeight.setText("");
                Toast.makeText(getBaseContext(), "Canceled", Toast.LENGTH_LONG).show();
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
        getSupportActionBar().setTitle("Account");
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
        return R.id.nav_account;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }


    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
        }
    }

    public void setProfileImage(View view){

        imgView.buildDrawingCache();
        Bitmap bitmap = imgView.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        String img_str = Base64.encodeToString(image, 0);

        String base=img_str;
        byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);

        imgView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes,0, imageAsBytes.length) );

        SharedPreferences preferences = getSharedPreferences("myprefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userphoto2",img_str);
        editor.commit();
    }
}
