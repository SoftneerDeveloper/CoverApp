package ke.co.coverapp.coverapp.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.callbacks.PricesLoadedListener;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.pojo.Prices;
import ke.co.coverapp.coverapp.utility.network.LoadUtil;

/**
 * Created by Clifford Owino on 2/9/2017.
 */

public class TaskLoadBalance extends AsyncTask<Void, Void, ArrayList<Balance>> {
    private BalanceLoadedListener myComponent;
    private RequestQueue requestQueue;

    /**
     * @param myComponent | requesting context
     */
    public TaskLoadBalance(BalanceLoadedListener myComponent) {

        this.myComponent = myComponent;
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    @Override
    protected ArrayList<Balance> doInBackground(Void... params) {
        return LoadUtil.loadBalance(requestQueue);
    }

    @Override
    protected void onPostExecute(ArrayList<Balance> balanceData) {
        if (myComponent != null) {
            myComponent.onBalanceLoaded(balanceData);
        }
    }
}