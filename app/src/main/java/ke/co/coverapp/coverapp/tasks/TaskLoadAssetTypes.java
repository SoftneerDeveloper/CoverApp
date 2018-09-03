package ke.co.coverapp.coverapp.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.callbacks.AssetTypesLoadedListener;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.AssetTypes;
import ke.co.coverapp.coverapp.utility.network.LoadUtil;

/**
 * Created by nick on 6/16/17.
 */

public class TaskLoadAssetTypes extends AsyncTask<Void, Void, ArrayList<AssetTypes>> {
    private AssetTypesLoadedListener myComponent;
    private RequestQueue requestQueue;

    public TaskLoadAssetTypes(AssetTypesLoadedListener myComponent){
        this.myComponent = myComponent;
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    @Override
    protected ArrayList<AssetTypes> doInBackground(Void... voids) {
        return LoadUtil.loadAssetTypes(requestQueue);
    }

    @Override
    protected void onPostExecute(ArrayList<AssetTypes> assetTypes) {
        if (myComponent != null) {
            myComponent.onAssetTypesLoaded(assetTypes);
        }
    }
}
