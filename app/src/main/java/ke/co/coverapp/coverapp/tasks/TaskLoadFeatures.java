package ke.co.coverapp.coverapp.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.callbacks.FeaturesLoadedListener;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Feature;
import ke.co.coverapp.coverapp.utility.network.LoadUtil;

/**
 * Created by Clifford Owino on 11/16/2016.
 */

public class TaskLoadFeatures  extends AsyncTask<Void, Void, ArrayList<Feature>> {
    private FeaturesLoadedListener myComponent;
    private RequestQueue requestQueue;
    private int product;

    /**
     * @param myComponent | requesting context
     * @param product     | the product to whose features are being loaded
     */
    public TaskLoadFeatures(FeaturesLoadedListener myComponent, int product) {

        this.myComponent = myComponent;
        this.product = product;
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }


    @Override
    protected ArrayList<Feature> doInBackground(Void... params) {
        return LoadUtil.loadFeatures(requestQueue, product);
    }

    @Override
    protected void onPostExecute(ArrayList<Feature> listFeatures) {
        if (myComponent != null) {
            myComponent.onFeaturesLoaded(listFeatures);
        }
    }
}

