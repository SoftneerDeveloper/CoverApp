package ke.co.coverapp.coverapp.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.utility.network.LoadUtil;

/**
 * Created by Clifford Owino on 3/17/2017.
 */

public class TaskLoadAssets extends AsyncTask<Void, Void, ArrayList<Assets>> {
    private AssetsLoadedListener myComponent;
    private RequestQueue requestQueue;
    private int last_id;

    /**
     * @param myComponent | requesting context
     */
    public TaskLoadAssets(AssetsLoadedListener myComponent, int last_id) {

        this.myComponent = myComponent;
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        this.last_id = last_id;
    }


    @Override
    protected ArrayList<Assets> doInBackground(Void... params) {
        return LoadUtil.loadAssets(requestQueue, last_id);
    }

    @Override
    protected void onPostExecute(ArrayList<Assets> assetsData) {
        if (myComponent != null) {
            myComponent.onAssetsLoaded(assetsData);
        }
    }
}
