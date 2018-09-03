package ke.co.coverapp.coverapp.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.callbacks.AssetCategoriesLoadedListener;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.AssetCategories;
import ke.co.coverapp.coverapp.utility.network.LoadUtil;

/**
 * Created by nick on 6/16/17.
 */

public class TaskLoadAssetCategories extends AsyncTask<Void, Void, ArrayList<AssetCategories>>{
    private AssetCategoriesLoadedListener myComponent;
    private RequestQueue requestQueue;

    public TaskLoadAssetCategories(AssetCategoriesLoadedListener myComponent){
        this.myComponent = myComponent;
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    @Override
    protected ArrayList<AssetCategories> doInBackground(Void... params) {
        return LoadUtil.loadAssetCategories(requestQueue);
    }

    @Override
    protected void onPostExecute(ArrayList<AssetCategories> assetCategories) {
        if (myComponent != null) {
            myComponent.onAssetCategoriesLoaded(assetCategories);
        }
    }
}
