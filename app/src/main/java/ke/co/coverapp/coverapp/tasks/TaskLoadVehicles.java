package ke.co.coverapp.coverapp.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.callbacks.VehiclesLoadedListener;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Vehicle;
import ke.co.coverapp.coverapp.utility.network.LoadUtil;

/**
 * Created by Clifford Owino on 3/24/2017.
 */

public class TaskLoadVehicles  extends AsyncTask<Void, Void, ArrayList<Vehicle>> {
    private VehiclesLoadedListener myComponent;
    private RequestQueue requestQueue;

    /**
     * @param myComponent | requesting context
     */
    public TaskLoadVehicles(VehiclesLoadedListener myComponent) {

        this.myComponent = myComponent;
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }


    @Override
    protected ArrayList<Vehicle> doInBackground(Void... params) {
        return LoadUtil.loadVehicles(requestQueue);
    }

    @Override
    protected void onPostExecute(ArrayList<Vehicle> listVehicles) {
        if (myComponent != null) {
            myComponent.onVehiclesLoaded(listVehicles);
        }
    }
}
