package pt.ismai.hungryme.Food;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import pt.ismai.hungryme.R;

public class DiaryListAdapter extends BaseAdapter {
    Activity activity;

    public DiaryListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return FoodContent.diary.size();
    }

    @Override
    public Object getItem(int position) {
        return FoodContent.diary.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_item_recipe, null, false);
        }

        return convertView;
    }
}
