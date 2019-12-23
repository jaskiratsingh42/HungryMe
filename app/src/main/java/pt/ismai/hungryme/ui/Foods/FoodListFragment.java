package pt.ismai.hungryme.ui.Foods;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import pt.ismai.hungryme.Food.FoodContent;
import pt.ismai.hungryme.HelpingClass.APIhelper;
import pt.ismai.hungryme.HelpingClass.MyFoodListAdapter;
import pt.ismai.hungryme.HelpingClass.MyRecipeListAdapter;
import pt.ismai.hungryme.Recipe.RecipeContent;

public class FoodListFragment extends ListFragment {
    public static String search;
    private Callback callback = dummyCallback;
    static MyFoodListAdapter adapter;

    public interface Callback {
        void onItemSelected(Long id);
    }

    private static final Callback dummyCallback = new Callback() {
        @Override
        public void onItemSelected(Long id) {
        }
    };
    public static void getSearchQuery(String query)
    {
        search = query;
    }
    public static void getListData(Context context)
    {
        APIhelper rad = new APIhelper(context, "getFoodList", adapter);
        rad.search = search;
        rad.doTask();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String search = super.getActivity().getIntent().getStringExtra("search");
        Log.d("FoodListFragment","Inside FoodListFragment");
        //JS Code
        FoodContent.foods.clear();
        adapter = new MyFoodListAdapter(getActivity());
        adapter.notifyDataSetChanged();
        setListAdapter(adapter);
        setHasOptionsMenu(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        callback.onItemSelected(FoodContent.foods.get(position).getId());
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        if (!(context instanceof Callback)) {
            throw new IllegalStateException("Activity must implement callback interface.");
        }
        callback = (Callback) context;
    }
}
