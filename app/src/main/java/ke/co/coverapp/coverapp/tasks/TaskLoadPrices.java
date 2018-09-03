package ke.co.coverapp.coverapp.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.callbacks.PricesLoadedListener;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Feature;
import ke.co.coverapp.coverapp.pojo.Prices;
import ke.co.coverapp.coverapp.utility.network.LoadUtil;

/**
 * Created by Clifford Owino on 1/19/2017.
 */

public class TaskLoadPrices extends AsyncTask<Void, Void, ArrayList<Prices>> {
    private PricesLoadedListener myComponent;
    private RequestQueue requestQueue;

    /**
     * @param myComponent | requesting context
     */
    public TaskLoadPrices(PricesLoadedListener myComponent) {

        this.myComponent = myComponent;
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }


    @Override
    protected ArrayList<Prices> doInBackground(Void... params) {
        return LoadUtil.loadPrices(requestQueue);
    }

    @Override
    protected void onPostExecute(ArrayList<Prices> listPrices) {
        if (myComponent != null) {
            myComponent.onPricesLoaded(listPrices);
        }
    }
}