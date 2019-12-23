
package pt.ismai.hungryme.LoginAndRegister;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerHelper {
    RequestQueue queue;
    HashMap<String,String> map;
    private Context context;
    public static int food_id,age;
    public static String name,email_id;
    public static double height,last_weight,goal_weight;
    public static String accountMethod;
    public static String resultname,resultage,resultheight,resultlastweight_kg,resultgoalweight_kg;
    public static List<String> favResult = new ArrayList<String>();

    public boolean requestStatus=false;
    public ServerHelper(Context context) {
        this.context = context;
        favResult.clear();
    }

    public boolean accountRequest(final String accountMethod)
    {
            this.accountMethod = accountMethod;
            queue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(Request.Method.POST,"https://hungryme2.000webhostapp.com/userConfig.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("response");

                                if(res.equals("success")){
                                    requestStatus = true;
                                    if(accountMethod.equals("update")){
                                        Toast.makeText(context,"Account details updated successfully.",Toast.LENGTH_SHORT).show();
                                    }
                                    else if(accountMethod.equals("retrieve")){
                                        resultname =json.getString("resultname");
                                        resultage =json.getString("resultage");
                                        resultheight =json.getString("resultheight");
                                        resultlastweight_kg =json.getString("resultweight_kg");
                                        resultgoalweight_kg =json.getString("resultgoalweight_kg");

                                        Toast.makeText(context,"Here are your saved details.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(context,"Server Timed Out. Try Again Later",Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }},new Response.ErrorListener() {@Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error: "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }})
            {   protected Map<String, String> getParams(){
                if(accountMethod.equals("retrieve"))
                {
                    map = new HashMap<>();
                    map.put("method",accountMethod);
                    map.put("email_id",String.valueOf(email_id));
                }
                else if(accountMethod.equals("update"))
                {
                    map = new HashMap<>();
                    map.put("method",accountMethod);
                    map.put("email_id",String.valueOf(email_id));
                    map.put("name",String.valueOf(name));
                    map.put("age",String.valueOf(age));
                    map.put("height",String.valueOf(height));
                    map.put("last_weight",String.valueOf(last_weight));
                    map.put("goal_weight",String.valueOf(goal_weight));
                }
                return map;
            } };
            queue.add(request);
        return requestStatus;
    }

    public boolean favRequest(final String method)
    {
        queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST,"https://hungryme2.000webhostapp.com/favRecipes.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String res = json.getString("res");
                            JSONArray resultArray;

                            if(res.equals("success")){
                                requestStatus = true;

                                if(method.equals("retrieve")){
                                    resultArray =json.getJSONArray("result");
                                    for(int i=0; i<= resultArray.length(); i++)
                                    {
                                        favResult.add(resultArray.getString(i));
                                    }
                                    Toast.makeText(context,"Here are the Favorite Recipes",Toast.LENGTH_SHORT).show();
                                }
                                else if(method.equals("delete")){
                                    Toast.makeText(context,"Recipe Deleted from Favorites",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(res.equals("unknown_err")){
                                Toast.makeText(context,"Server Timed Out. Try Again Later",Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }},new Response.ErrorListener() {@Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context,"Error: "+error.getMessage(),Toast.LENGTH_SHORT).show();
        }})
        {   protected Map<String, String> getParams(){
            map = new HashMap<>();
            map.put("method",method);
            map.put("user_id",String.valueOf(email_id));
            map.put("recipe_id",String.valueOf(food_id));
            return map;
        } };
        queue.add(request);
        return requestStatus;
    }

    public boolean loginRequest(final String username, final String password)
    {
//        https://hungryme2.000webhostapp.com/userLogin.php
        queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST,"https://hungryme2.000webhostapp.com/userLogin.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String res = json.getString("res");
                            if(res.equals("success")){
                                //Intent
                                Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
                                requestStatus = true;
                            }
                            else if(res.equals("username_err") || res.equals("password_err")){
//                                Toast.makeText(context,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context,"Server Timed Out. Try Again Later",Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }},new Response.ErrorListener() {@Override
                    public void onErrorResponse(VolleyError error) {
                             //toast
                            Toast.makeText(context,"Error: "+error.getMessage(),Toast.LENGTH_SHORT).show();
                    }})
        {   protected Map<String, String> getParams(){
            map = new HashMap<>();
            map.put("username",username);
            map.put("password",password);
            return map;
        } };
        queue.add(request);
    return requestStatus;
    }
    public boolean signupRequest(final String username, final String password)
    {
//        https://hungryme2.000webhostapp.com/userSignup.php
        queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST,"https://hungryme2.000webhostapp.com/userSignup.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("onResponse", "Inside onResponse "+ response);
                            JSONObject json = new JSONObject(response);
                            String res = json.getString("res");
                            if(res.equals("success")){
                                //Intent
                                Toast.makeText(context,"Account Created Successfully",Toast.LENGTH_SHORT).show();
                                requestStatus = true;
                            }
                            else if(res.equals("username_err")){
                                Toast.makeText(context,"Username Invalid",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context,"Server Timed out, try again later",Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }},new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse", "onErrorResponse:");
                    //toast
                    Toast.makeText(context,"Error01: "+error.getMessage(),Toast.LENGTH_SHORT).show();
        }})
        {
            protected Map<String, String> getParams(){
            map = new HashMap<>();
            map.put("username",username);
            map.put("password",password);
            return map;
            }
        };
        queue.add(request);
        return requestStatus;
    }
}

