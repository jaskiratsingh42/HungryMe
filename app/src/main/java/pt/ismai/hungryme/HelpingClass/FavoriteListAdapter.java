package pt.ismai.hungryme.HelpingClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Recipe;

import java.util.ArrayList;

import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.FavoriteRecipe;

public class FavoriteListAdapter extends ArrayAdapter<Recipe> {

    String url;
    Activity activity;
    public static ArrayList<Recipe> recipesList = new ArrayList<>();

    public FavoriteListAdapter(Activity activity, int textViewResourceId, ArrayList<Recipe> objects) {
        super(activity, textViewResourceId, objects);
        this.recipesList = objects;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item_recipe, null);
        TextView textView = (TextView) v.findViewById(R.id.recipe_title);
        TextView textView2 = (TextView) v.findViewById(R.id.recipe_calories);
        TextView textView3 = (TextView) v.findViewById(R.id.label);
        final ImageView imageView = (ImageView) v.findViewById(R.id.thumbnail);

//Required at this point
        textView.setText(recipesList.get(position).getName());
        textView2.setText("Calories: " +recipesList.get(position).getDescription());
        textView3.setText(recipesList.get(position).getCategories()+ " ");

        url = recipesList.get(position).getUrl();
        try{Glide.with(getContext())
                .load(recipesList.get(position).getImages().get(0))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
        }
        catch(IndexOutOfBoundsException exception)
        {
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), BitmapFactory.decodeResource(activity.getResources(), R.drawable.casual) );
            circularBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(circularBitmapDrawable);
        }
        return v;
    }
}