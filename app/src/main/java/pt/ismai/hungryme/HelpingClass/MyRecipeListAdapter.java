package pt.ismai.hungryme.HelpingClass;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.concurrent.ExecutionException;

import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;

public class MyRecipeListAdapter extends BaseAdapter {
    private Activity activity;
    public MyRecipeListAdapter(Activity baseActivity) {
        RecipeContent.recipes.clear();
        activity = baseActivity;
    }

    @Override
    public int getCount() {
        return RecipeContent.recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return RecipeContent.recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
//        return RecipeContent.recipes.get(position).id.hashCode();
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_item_recipe, container, false);
        }
        try {
//        final RecipeContent.RecipeItem item = (RecipeContent.RecipeItem) getItem(position);
            ((TextView) convertView.findViewById(R.id.recipe_title)).setText(RecipeContent.recipes.get(position).getName());
//            ((TextView) convertView.findViewById(R.id.recipe_calories)).setText(RecipeContent.recipes.get(position).getDescription());
            ((TextView) convertView.findViewById(R.id.label)).setText(RecipeContent.recipes.get(position).getDescription());
            final ImageView img = (ImageView) convertView.findViewById(R.id.thumbnail);
            try {
                Glide.with(activity)
                        .load(RecipeContent.recipes.get(position).getImages().get(0))
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                img.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } catch (IndexOutOfBoundsException exception) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), BitmapFactory.decodeResource(activity.getResources(), R.drawable.casual));
                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
