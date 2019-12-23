package pt.ismai.hungryme.LoginAndRegister;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.ui.FavoritesActivity;
import pt.ismai.hungryme.ui.MainActivity;
import pt.ismai.hungryme.ui.Recipes.RecipesActivity;

import static pt.ismai.hungryme.ui.UI.BaseActivity.checkConnection;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
        private Button login;
        private TextView register;
        private EditText etEmail, etPass;
        private DbHelper db;
        private Session session;
        private ServerHelper server;
        public static final String MyPREFERENCES = "MyPrefs" ;
        SharedPreferences sharedpreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            //Passing Context to ServerHelper Class
            server = new ServerHelper(this);
            db = new DbHelper(this);
            session = new Session(this);
            login = (Button)findViewById(R.id.btnLogin);
            register = (TextView) findViewById(R.id.registerLink);
            etEmail = (EditText)findViewById(R.id.emailT);
            etPass = (EditText)findViewById(R.id.password);
            login.setOnClickListener(this);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    finish();
                }
            });

            if(session.loggedin()){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            if(!checkConnection(this))
            {
                Toast.makeText(this,"Network Connection Failed.\nHungryMe requires an active internet connection.\tPlease check your device settings.",Toast.LENGTH_LONG).show();
            }
        }

    public void toRegister(View v)
    {
        Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(myIntent);
    }
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnLogin:
                    login();
                    break;
                default:
            }
        }

    public void login(){
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        if(db.getUser(email,pass))
        if(server.loginRequest(email,pass))
        {
            session.setLoggedin(true);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("EMAIL", email);
            editor.commit();
            startActivity(new Intent(this, FavoritesActivity.class));
            finish();
        }
        }

}