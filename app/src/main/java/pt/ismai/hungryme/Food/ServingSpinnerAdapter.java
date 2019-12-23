package pt.ismai.hungryme.Food;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fatsecret.platform.model.Serving;

import java.util.ArrayList;
import java.util.List;

import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;


public class ServingSpinnerAdapter extends BaseAdapter {

    Activity activity;
//    FoodContent.servings = FoodContent.food.getServings();
    public static List<Serving> servings = new ArrayList<>();
    public ServingSpinnerAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        if(servings==null){
            return 0;
        }
        return servings.size();
    }

    @Override
    public Object getItem(int position) {
        if(servings==null){
            return null;
        }
        return servings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(activity).inflate(R.layout.spinner_view,parent,false);
        }
        ((TextView) convertView.findViewById(R.id.text2)).setText(servings.get(position).getMetricServingUnit());
        return convertView;
    }
}
