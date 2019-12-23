package pt.ismai.hungryme.ui.Recipes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import pt.ismai.hungryme.HelpingClass.APIhelper;
import pt.ismai.hungryme.HelpingClass.MyRecipeListAdapter;
import pt.ismai.hungryme.Recipe.RecipeContent;

public class RecipeListFragment extends ListFragment {
    public static String search;
    private Callback callback = dummyCallback;
    static MyRecipeListAdapter adapter;

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
        Log.d("test","getdata");
        APIhelper rad = new APIhelper(context, "getRecipeList", adapter);
        rad.search = search;
        rad.doTask();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String search = super.getActivity().getIntent().getStringExtra("search");
        Log.d("RecipeListFragment","Inside RecipeListFragment");
        //JS Code
        adapter = new MyRecipeListAdapter(getActivity());
        setListAdapter(adapter);
        setHasOptionsMenu(true);

//        MyListAdapter adapter = new MyListAdapter(getActivity());
//        JSONParser rad = new JSONParser(adapter);
//        rad.search = search;
//        rad.execute();
//        setListAdapter(adapter);
//        setHasOptionsMenu(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        callback.onItemSelected(RecipeContent.recipes.get(position).getId());
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
